// “^”定位符规定匹配模式必须出现在目标字符串的开头
// “$”定位符规定匹配模式必须出现在目标对象的结尾
// “\b”定位符规定匹配模式必须出现在目标字符串的开头或结尾的两个边界之一
// “\B”定位符则规定匹配对象必须位于目标字符串的开头和结尾两个边界之内
// g代表全局搜索   
// i代表忽略大小写
// \s:用于匹配单个空格符,包括tab键和换行符
// 删除左边和右边空格
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, '');
}
// 删除左边空格
function ltrim(str) {
	return str.replace(/^\s*/g, '');
}
// 删除右边空格
function rtrim(str) {
	return str.replace(/\s*$/, '');
}
// 验证字符串是否为空
function isNull(str) {
	var re = /^[　\s]*$/;
	return re.test(str);
}
// 判断字符串是否为英文
function isEnglish(str) {
	var re = /^\w*$/;
	return re.test(str);
}
// 判断是否为数字串
function isNumber(str) {
	var re = /^\d*$/;
	return re.test(str);
}
// 判断是否为数字0或1
function is0Or1(str) {
	var re = /^[01]$/;
	return re.test(str);
}
// 判断是否为颜色代码
function isColor(str) {
	var re = /^#?[0-9|a-f|A-F]{6}$/;
	return re.test(str);
}
// 是否是手机号格式
function isMobilePhone(str) {
	var re = /^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$/;
	return re.test(str);
}
// 是否是中国电话号码，只验证区号+电话号+分机号，其中区号和分机号可省略
function isTelePhone(str) {
	var re = /^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
	return re.test(str);
}
// 是否是邮箱格式
function isEmail(str) {
	var re = /^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]|net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC|sg|SG|([a-zA-Z]*))$/;
	var r = str.search(re);
	if (r == -1) {
		return false;
	}
	return true;
}
// 判断字符串是否相同
function equals(str1, str2) {
	if (str1 == str2) {
		return true;
	}
	return false;
}
// 忽略大小写，判断字符串是否相同
function equalsIgnoreCase(str1, str2) {
	if (str1.toUpperCase() == str2.toUpperCase()) {
		return true;
	}
	return false;
}
// 是否是正整数
function isPositiveInteger(str) {
	var re = /^[1-9]\d*$/;
	return re.test(str);
}
// 是否是正数
function isPositiveNumber(str) {
	var re = /^\d+(\.\d+)?$/;
	return re.test(str);
}
// 获取字符串长度
function getLength(str) {
	var realLength = 0, len = str.length, charCode = -1;
	for (var i = 0; i < len; i++) {
		charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128)
			realLength += 1;
		else
			realLength += 2;
	}
	return realLength;
}
// 截取字符串前面指定长度的子串，对于一个中文字符按两个英文字符长度计算
function subString(str, length) {
	var sb = new Array;
	var realLength = 0, len = str.length, charCode = -1;
	for (var i = 0; i < len; i++) {
		charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128)
			realLength += 1;
		else
			realLength += 2;
		if (realLength <= length) {
			sb.push(str.substr(i, 1));
		} else {
			break;
		}
	}
	return sb.join("")
}
// 判断数组中是否包含重复值
function isRepeat(arr) {
	var hash = {};
	for ( var i in arr) {
		if (hash[arr[i]])
			return true;
		hash[arr[i]] = true;
	}
	return false;
}
// 判断是否包含字符串
function contain(str1, str2) {
	if (str1.indexOf(str2) > 0) {
		return true;
	}
	return false;
}
// 反转分隔符
function reverseDelimiter(str) {
	while (str.indexOf('\\') >= 0) {
		str = str.replace('\\', '/');
	}
	return str;
}
// 替换字符串中匹配的所有字符串
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	// 如果不是正则表达式
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")),
				replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
}
// 判断字符串是否以指定子字符串结束
String.prototype.endWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substring(this.length - s.length) == s)
		return true;
	else
		return false;
	return true;
}
// 判断字符串是否以指定子字符串开始
String.prototype.startWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substr(0, s.length) == s)
		return true;
	else
		return false;
	return true;
}
