var enterFirstLi = false;
var enterSystemMenu = false;
var enterSupport = false;
var enterSupportUp = false;
function showProgress() {
	$("#progressDialog").fadeIn("slow");
}
function hideProgress() {
	$("#progressDialog").fadeOut("slow");
}
function showSelectTip() {
	art.dialog({
		title : 'Tips',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		time : 10,
		content : 'Please select a project.',
		okVal : 'OK',
		ok : true
	});
}
function showTip(msg) {
	art.dialog({
		title : 'Hint',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		time : 10,
		content : msg,
		okVal : 'OK',
		ok : true
	});
}
function showInformationBox() {
	art.dialog({
		id : 'information',
		time : 290,
		title : 'Hint',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : document.getElementById('informationTipBox'),
		width : 568,
		height : 235,
		fixed : true,
		drag : false
	});
}
function showNoticeBox() {
	art.dialog({
		id : 'rightPromptBox',
		time : 290,
		title : 'Information',
		content : document.getElementById('rightPromptBox'),
		width : 289,
		height : 132,
		fixed : true,
		left : '100%',
		top : '100%',
		drag : false
	});
}
function toggleSystemMenu() {
	if (enterFirstLi || enterSystemMenu) {
		$('div.userMenu').show();
		$("div.sysMenu span.triangleIcon").addClass('up');
	} else {
		$('div.userMenu').hide();
		$("div.sysMenu span.triangleIcon").removeClass('up');
	}
}
function toggleSupportMenu() {
	if (enterSupport || enterSupportUp) {
		$('div.support').show();
		$("div.header span.switchIcon").addClass('up');
	} else {
		$('div.support').hide();
		$("div.header span.switchIcon").removeClass('up');
	}
}
$(function() {
	// 调整进度条的垂直位置
	var userLength=$("ul li.userLi").width();
	$("ul li.userLi div.userMenu ul.menuContent").width(userLength+20);
	var m=$("li.firstLi span.mailName").width();
	var i=$("li.firstLi span.icon").width();
	$("div.userMenu div.arrowTop div.arrow1").css("left",m+i+17);
	$("div.userMenu div.arrowTop div.arrow2").css("left",m+i+18);
	var h = $(window).height() * 0.4;
	$("#progressDialog img").css("margin-top", h);
	$('li.userLi').hover(function() {
		enterFirstLi = true;
		toggleSystemMenu();
	}, function() {
		enterFirstLi = false;
		setTimeout("toggleSystemMenu();", 10);
	});
	$('div.userMenu').hover(function() {
		enterSystemMenu = true;
		toggleSystemMenu();
	}, function() {
		enterSystemMenu = false;
		setTimeout("toggleSystemMenu();", 10);
	});
	$('li.threeLi').hover(function() {
		enterSupport = true;
		toggleSupportMenu();
	}, function() {
		enterSupport = false;
		toggleSupportMenu();
	});
	$('div.support').hover(function() {
		enterSupportUp = true;
		toggleSupportMenu();
	}, function() {
		enterSupportUp = false;
		setTimeout("toggleSupportMenu();", 10);
	});
});
