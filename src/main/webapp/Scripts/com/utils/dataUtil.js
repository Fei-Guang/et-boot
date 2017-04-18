// 转换小数，保留指定位数
function formatFloat(src, pos) {
	return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
}
// 该乘法由每位因子的小数位数相加得到积的小数位数
function multiplication(f1, f2) {
	var f1l = 0;
	if (f1.indexOf('.') > 0) {
		f1l = f1.toString().split(".")[1].length;
	}
	var f2l = 0;
	if (f2.indexOf('.') > 0) {
		f2l = f2.toString().split(".")[1].length;
	}
	var pos = f1l + f2l;
	return formatFloat(parseFloat(f1) * parseFloat(f2), pos);
}
// 该加法由每位因子的小数位数最大值得到和的小数位数
function addition(f1, f2) {
	var f1l = 0;
	if (f1.indexOf('.') > 0) {
		f1l = f1.toString().split(".")[1].length;
	}
	var f2l = 0;
	if (f2.indexOf('.') > 0) {
		f2l = f2.toString().split(".")[1].length;
	}
	var pos = Math.max(f1l, f2l);
	return formatFloat(parseFloat(f1) + parseFloat(f2), pos);
}
// 该减法由每位因子的小数位数最大值得到差的小数位数
function subtraction(f1, f2) {
	var f1l = 0;
	if (f1.indexOf('.') > 0) {
		f1l = f1.toString().split(".")[1].length;
	}
	var f2l = 0;
	if (f2.indexOf('.') > 0) {
		f2l = f2.toString().split(".")[1].length;
	}
	var pos = Math.max(f1l, f2l);
	return formatFloat(parseFloat(f1) - parseFloat(f2), pos);
}
// 该除法精度由传入精度控制
function division(f1, f2, pos) {
	return formatFloat(parseFloat(f1) / parseFloat(f2), pos);
}
// 转成纯数字字符串
function transfer2Num(num) {
	if (isNullOrUndefined(num)) {
		return "";
	}
	if (num == 0) {
		return num + "";
	}
	// 转换为字符串
	num = num + '';
	if (isNull(num)) {
		return "";
	}
	num = num.replace(/,/gi, '');
	return num;
}
// 转千分位分隔符
function formatNum(num) {
	// 转换为字符串
	num = num + '';
	if (isNull(num)) {
		return "";
	}
	if (num.toString().indexOf(',') > 0) {
		return num;
	}
	num = parseFloat(num)+'';
	var num = (num || 0).toString(), result = '';
	var bNegative = false;
	if (num.toString().indexOf('-') == 0) {
		num = num.substr(1);
		bNegative = true;
	}
	var nIndex = num.indexOf('.');
	var sInteger;
	if (nIndex > 0) {
		sInteger = num.split('.')[0];
		while (sInteger.length > 3) {
			result = ',' + sInteger.slice(-3) + result;
			sInteger = sInteger.slice(0, sInteger.length - 3);
		}
		if (sInteger) {
			result = sInteger + result;
		}
		result = result + '.' + num.split('.')[1];
	} else {
		while (num.length > 3) {
			result = ',' + num.slice(-3) + result;
			num = num.slice(0, num.length - 3);
		}
		if (num) {
			result = num + result;
		}
	}
	if (bNegative) {
		result = '-' + result;
	}
	return result;
}
function calculate(operation1, sOperator, operation2) {
	// 无效转换
	if (isNullOrUndefined(operation1)) {
		operation1 = '';
	}
	if (isNullOrUndefined(operation2)) {
		operation2 = '';
	}
	// 字符串转换
	operation1 = operation1 + '';
	operation2 = operation2 + '';
	if (isNull(operation1) && !isNull(operation2)) {
		if (sOperator == '*') {
			return '';
		}
		if (sOperator == '/') {
			return '';
		}
		operation1 = '0';
	} else if (isNull(operation2) && !isNull(operation1)) {
		if (sOperator == '*') {
			return '';
		}
		if (sOperator == '/') {
			operation2 = '1';
		} else {
			operation2 = '0';
		}
	} else if (isNull(operation1) && isNull(operation2)) {
		return '';
	}
	operation1 = transfer2Num(operation1);
	operation2 = transfer2Num(operation2);
	if (isNaN(operation1) || isNaN(operation2)) {
		alert('非法计算因子：' + operation1 + "," + operation2);
	}
	if (sOperator == '*') {
		return formatNum(multiplication(operation1, operation2));
	} else if (sOperator == '+') {
		return formatNum(addition(operation1, operation2));
	} else if (sOperator == '-') {
		return formatNum(subtraction(operation1, operation2));
	} else if (sOperator == '/') {
		return formatNum(division(operation1, operation2, 8));
	} else {
		return formatNum(eval('(' + operation1 + ')' + sOperator + '('
				+ operation2 + ')'));
	}
}
