// 刷新图片验证码
function reloadCaptcha() {
	var captchaUrl = $("#img_captcha").attr("src");
	$("#img_captcha").attr("src", captchaUrl + '?t=' + Math.random());
}
function plusBorder(id) {
	$("input#" + id).addClass("border");
}
function startBorder(id) {
	$("input#" + id).removeClass("border");
}
function clearRegisterTip() {
	$("#email-tip").html('&nbsp;');
	$("#password-tip").html('&nbsp;');
	$("#confirm-tip").html('&nbsp;');
	$("#name-tip").html('&nbsp;');
	$("#phone-tip").html('&nbsp;');
	$("#company-tip").html('&nbsp;');
	$("#captcha-tip").html('&nbsp;');
}
function register() {
	clearRegisterTip();
	var register_email = $("#email").val();
	var register_pwd = $("#password").val();
	var register_confirm = $("#confirm").val();
	var register_name = $("#name").val();
	var register_phone = $("#phone").val();
	var register_company = $("#company").val();
	var register_address = $("#address").val();
	var register_website = $("#website").val();
	var register_captcha = $("#captcha").val();
	if (equals(register_email, $("#email").attr('placeholder'))) {
		register_email = '';
	}
	if (equals(register_pwd, $("#email").attr('placeholder'))) {
		register_pwd = '';
	}
	if (equals(register_confirm, $("#confirm").attr('placeholder'))) {
		register_confirm = '';
	}
	if (equals(register_name, $("#name").attr('placeholder'))) {
		register_name = '';
	}
	if (equals(register_phone, $("#phone").attr('placeholder'))) {
		register_phone = '';
	}
	if (equals(register_company, $("#company").attr('placeholder'))) {
		register_company = '';
	}
	if (equals(register_address, $("#address").attr('placeholder'))) {
		register_address = '';
	}
	if (equals(register_website, $("#website").attr('placeholder'))) {
		register_website = '';
	}
	if (equals(register_captcha, $("#captcha").attr('placeholder'))) {
		register_captcha = '';
	}
	var valid = true;
	if (isNull(register_email)) {
		$("#email-tip").text('The email can\'t be null.');
		valid = false;
	}
	if (isNull(register_pwd)) {
		$("#password-tip").text('The password can\'t be null.');
		valid = false;
	} else if (getLength(register_pwd) < 6 || getLength(register_pwd) > 16) {
		$("#password-tip").text(
				'The password must be between 6 and 16 letters.');
		valid = false;
	}
	if (isNull(register_confirm)) {
		$("#confirm-tip").text('The confirm password can\'t be null.');
		valid = false;
	}
	if (!equals(register_pwd, register_confirm)) {
		$("#confirm-tip")
				.text('The confirm password is not equal to password.');
		valid = false;
	}
	if (isNull(register_name)) {
		$("#name-tip").text('The name can\'t be null.');
		valid = false;
	}
	if (isNull(register_phone)) {
		$("#phone-tip").text('The phone can\'t be null.');
		valid = false;
	}
	if (isNull(register_company)) {
		$("#company-tip").text('The company can\'t be null.');
		valid = false;
	}
	if (isNull(register_captcha)) {
		$("#captcha-tip").text('The verification code can\'t be null.');
		valid = false;
	}
	if (!valid) {
		return;
	}
	if ($('#agree').prop('checked')) {
		showProgress();
		$.post(rootFolder+'/auth/register?t=' + Math.random(), {
			email : strEnc(register_email, firstKey, secondKey, thirdKey),
			pwd : strEnc(register_pwd, firstKey, secondKey, thirdKey),
			name : encodeURIComponent(register_name),
			phone : encodeURIComponent(register_phone),
			company : encodeURIComponent(register_company),
			address : encodeURIComponent(register_address),
			website : encodeURIComponent(register_website),
			captcha : encodeURIComponent(register_captcha)
		}, function(data) {
			hideProgress();
			if (data.isRegister) {
				window.location.href = rootFolder;
			} else {
				$("#register-tip").html(data.msg);
			}
		}, "json");
	} else {
		// 不同意协议就给出提示
		$("#register-tip").text(
				'You do not agree to the Glodon agreement and Privacy.');
	}
}