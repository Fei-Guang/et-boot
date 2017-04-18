//获取以"yyyy-mm-dd"表示的当前日期字符串
function getCurrentDate(date) {
	var y = date.getFullYear();
	var m = (date.getMonth() + 1 > 9) ? date.getMonth() + 1 : "0"
			+ (date.getMonth() + 1);
	var d = (date.getDate() > 9) ? date.getDate() : "0" + date.getDate();
	return y + "-" + m + "-" + d;
}

// 获取以"yyyy-mm-dd HH:mm:ss"表示的当前日期字符串
function convertDateTimeToString(date) {
	var y = date.getFullYear();
	var m = (date.getMonth() + 1 > 9) ? date.getMonth() + 1 : "0"
			+ (date.getMonth() + 1);
	var d = (date.getDate() > 9) ? date.getDate() : "0" + date.getDate();
	var h = date.getHours();
	var l = date.getMinutes();
	var s = date.getSeconds();
	return y + "-" + m + "-" + d + " " + h + ":" + l + ":" + s;
}

// 获取用于生成压缩包的以"yyyymmddHHmmss"表示的当前日期字符串
function getZipDateDescription(date) {
	var y = date.getFullYear();
	var m = (date.getMonth() + 1 > 9) ? date.getMonth() + 1 : "0"
			+ (date.getMonth() + 1);
	var d = (date.getDate() > 9) ? date.getDate() : "0" + date.getDate();
	var h = date.getHours();
	var l = date.getMinutes();
	var s = date.getSeconds();
	return y + "" + m + "" + d + "" + h + "" + l + "" + s;
}

// 判断指定日期是否是闰年
function isLeapYear(date) {
	return (0 == date.getYear() % 4 && ((date.getYear() % 100 != 0) || (date
			.getYear() % 400 == 0)));
}

// 获取指定日期的上一天日期
function getYestoday(date) {
	var yesterday_milliseconds = date.getTime() - 1000 * 60 * 60 * 24;
	var yesterday = new Date();
	yesterday.setTime(yesterday_milliseconds);
	return convertDateTimeToString(yesterday);
}

// 获取比指定日期提前指定天数的日期
function getPastday(date, day) {
	var pastday_milliseconds = date.getTime() - 1000 * 60 * 60 * 24 * day;
	var pastday = new Date();
	pastday.setTime(pastday_milliseconds);
	return convertDateTimeToString(pastday);
}

// 获取指定日期的上一月日期
function getLastMonthDate(date) {
	var daysInMonth = new Array([ 0 ], [ 31 ], [ 28 ], [ 31 ], [ 30 ], [ 31 ],
			[ 30 ], [ 31 ], [ 31 ], [ 30 ], [ 31 ], [ 30 ], [ 31 ]);
	var strYear = date.getFullYear();
	var strDay = date.getDate();
	var strMonth = date.getMonth() + 1;
	if (isLeapYear(date)) {
		daysInMonth[2] = 29;
	}
	if (strMonth - 1 == 0) {
		strYear -= 1;
		strMonth = 12;
	} else {
		strMonth -= 1;
	}
	strDay = daysInMonth[strMonth] > strDay ? strDay : daysInMonth[strMonth];
	if (strMonth < 10) {
		strMonth = "0" + strMonth;
	}
	if (strDay < 10) {
		strDay = "0" + strDay;
	}
	datastr = strYear + "-" + strMonth + "-" + strDay;
	return datastr;
}

// 求两个时间的天数差 日期格式为 YYYY-MM-dd
function daysBetween(DateOne, DateTwo) {
	var OneMonth = DateOne.substring(5, DateOne.lastIndexOf('-'));
	var OneDay = DateOne
			.substring(DateOne.length, DateOne.lastIndexOf('-') + 1);
	var OneYear = DateOne.substring(0, DateOne.indexOf('-'));
	var TwoMonth = DateTwo.substring(5, DateTwo.lastIndexOf('-'));
	var TwoDay = DateTwo
			.substring(DateTwo.length, DateTwo.lastIndexOf('-') + 1);
	var TwoYear = DateTwo.substring(0, DateTwo.indexOf('-'));
	var cha = ((Date.parse(OneMonth + '/' + OneDay + '/' + OneYear) - Date
			.parse(TwoMonth + '/' + TwoDay + '/' + TwoYear)) / 86400000);
	return Math.abs(cha);
}
