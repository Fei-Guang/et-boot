var swiper = new Swiper('.swiper-container', {
	pagination : '.swiper-pagination',
	paginationClickable : true,
	spaceBetween : 0,
	loop : true,
	centeredSlides : true,
	autoplay : 4500,
	autoplayDisableOnInteraction : false,
});
function setStyle(x) {
	if (x == "login_email") {
		$("div.user").addClass("border");
		$("div.user").find("div").removeClass("userIcon").addClass(
				"userIcon_hover");
	} else{
		$("div.password").addClass("border");
		$("div.password").find("div").removeClass("passwordIcon").addClass(
				"passwordIcon_hover");
	}
}
function removeStyle(x) {
	if (x == "login_email") {
		$("div.user").removeClass("border");
		$("div.user").find("div").removeClass("userIcon_hover").addClass(
				"userIcon");
	} else if (x == "login_pwd") {
		$("div.password").removeClass("border");
		$("div.password").find("div").removeClass("passwordIcon_hover")
				.addClass("passwordIcon");
	}
}
function saveLoginParam() {
	var email = $('#login_email').val();
	var pwd = $('#login_pwd').val();
	if ($('#remember').prop('checked')) {
		// 保存登录信息
		$.cookie("remember", "true", {
			expires : 7,
			path : '/'
		});
		// 加密保存，防止信息被偷取
		$.cookie('email', strEnc(email, firstKey, secondKey, thirdKey), {
			expires : 7,
			path : '/'
		});
		$.cookie('pwd', strEnc(pwd, firstKey, secondKey, thirdKey), {
			expires : 7,
			path : '/'
		});
	} else {
		clearCookie();
	}
	return true;
}
function clearLoginTip() {
	$("#login-email-tip").html('&nbsp;');
	$("#login-pwd-tip").html('&nbsp;');
}
function login() {
	clearLoginTip();
	var login_email = $("#login_email").val();
	var login_pwd = $("#login_pwd").val();
	if (equals(login_email, $("#login_email").attr('placeholder'))) {
		login_email = '';
	}
	var valid = true;
	if (isNull(login_email)) {
		$("#login-email-tip").text('The email can\'t be null.');
		valid = false;
	} else if (!isEmail(login_email)) {
		$("#login-email-tip").text('The email is invalid.');
		valid = false;
	}
	if (isNull(login_pwd)) {
		$("#login-pwd-tip").text('The password can\'t be null.');
		valid = false;
	}
	if (!valid) {
		return;
	}
	saveLoginParam();
	showProgress();
	$.get(rootFolder+'/auth/grant?t=' + Math.random(), {
		email : strEnc(login_email, firstKey, secondKey, thirdKey),
		pwd : strEnc(login_pwd, firstKey, secondKey, thirdKey)
	}, function(data) {
		hideProgress();
		if (data.isLogin) {
			judgeUserInfo();
		} else {
			if (data.msg.indexOf('password') > 0) {
				$("#login-pwd-tip").html(data.msg);
			} else {
				$("#login-email-tip").html(data.msg);
			}
		}
	}, "json");
}
function autoLogin(login_email, login_pwd) {
	$.get(rootFolder+'/auth/grant?t=' + Math.random(), {
		email : strEnc(login_email, firstKey, secondKey, thirdKey),
		pwd : strEnc(login_pwd, firstKey, secondKey, thirdKey)
	}, function(data) {
		if (data.isLogin) {
			judgeUserInfo();
		} else {
			initLogin();
		}
	}, "json");
}
function judgeUserInfo() {
	$.get(rootFolder+'/auth/isUserInfoPerfect?t=' + Math.random(), {}, function(
			data) {
		if (data.isPerfect) {
			// 登录用户信息完善，跳转到工程列表
			window.location.href = rootFolder+'/project/index';
		} else {
			// 登录用户信息不完善，跳转到完善个人信息页面
			window.location.href = rootFolder+'/user/supplyPersonalInfo';
		}
	}, "json");
}
function initLogin() {
	$('#login_email').keydown(function(event) {
		if (event.keyCode == 13) {
			// 回车键作为快捷方式提交
			login();
		}
	});
	$('#login_pwd').keydown(function(event) {
		if (event.keyCode == 13) {
			// 回车键作为快捷方式提交
			login();
		}
	});
}
$(function() {
	if ($.browser.mozilla) {
		// 火狐浏览器判断
		var w = $(window).width();
		var l = (w - 1180) / 2 + 5;
		$("div.middle div.loginOne").css("right", l);
		var c = $("div.middle div.loginOne").css("right");
		$("div.middle div.cengOne").css("right", l);
		$("div.middle div.cengTwo").css("right", l);
		$("div.middle div.loginTwo").css("right", l);
	} else {
		// 谷歌浏览器判断
		var w = $(window).width();
		var l = (w - 1180) / 2 + 5;
		var t = (w - 1180) / 2 + 10;
		$("div.middle div.loginOne").css("right", l);
		var c = $("div.middle div.loginOne").css("right");
		$("div.middle div.cengOne").css("right", t);
		$("div.middle div.cengTwo").css("right", t);
		$("div.middle div.loginTwo").css("right", l);
	}

	if ($("#remember").length > 0) {
		// 未登录
		if ($.cookie("remember") == "true") {
			// 自动登录
			$("#remember").attr("checked", true);
			var login_email = $.cookie("email");
			var login_pwd = $.cookie("pwd");
			if (login_email && login_pwd) {
				// 加密保存的，这里应该解密
				login_email = strDec(login_email, firstKey, secondKey, thirdKey);
				login_pwd = strDec(login_pwd, firstKey, secondKey, thirdKey);
				$("#login_email").val(login_email);
				$("#login_pwd").val(login_pwd);
				autoLogin(login_email, login_pwd);
			} else {
				initLogin();
			}
		} else {
			initLogin();
		}
	} else {
		// 已经登录
		$
				.get(
						rootFolder+'/auth/isUserInfoPerfect?t=' + Math.random(),
						{},
						function(data) {
							if (data.isPerfect) {
								// 登录用户信息完善，跳转主页，隐藏相关信息
								if (window.location.pathname.indexOf("auth") > 0) {
									window.location.href = rootFolder;
								}
							} else {
								// 登录用户信息不完善，跳转到完善个人信息页面
								window.location.href = rootFolder+'/user/supplyPersonalInfo';
							}
						}, "json");
	}

	var hour = (new Date()).getHours();
	var now_time = 'morning';
	if(hour >= 12 && hour<18) {
		now_time = 'afternoon';
	} else if(hour >= 18) {
		now_time = 'evening';
	}
	now_time = 'Good '+now_time+' !';
	$("div.loginTwo div.good span.goodMoring").text(now_time);	
});