var t=0;
function showPsw() {
	if(t%2==0){
	$('div.content div.hiddenArea').show();
	$('input[type="password"]').val("");
	}else{
	  $('div.content div.hiddenArea').hide();
	}
	t++;
}
function plusBorder(id) {
	$("input#" + id).addClass("border");
}
function startBorder(id) {
	$("input#" + id).removeClass("border");
}
function clearModifyInfoTip() {
	$("#origPass-tip").html('&nbsp;');
	$("#inputPass-tip").html('&nbsp;');
	$("#confirmPass-tip").html('&nbsp;');
	$("#name-tip").html('&nbsp;');
	$("#phone-tip").html('&nbsp;');
	$("#company-tip").html('&nbsp;');
}
function modifyInfo() {
	clearModifyInfoTip();
	var origPass = $("#origPass").val();
	var inputPass = $("#inputPass").val();
	var confirmPass = $("#confirmPass").val();
	var modify_name = $("span.name").text();
	if ($("#name").is(':visible')) {
		modify_name = $("#name").val();
	}
	var modify_phone = $("span.phone").text();
	if ($("#phone").is(':visible')) {
		modify_phone = $("#phone").val();
	}
	var modify_company = $("span.company").text();
	if ($("#company").is(':visible')) {
		modify_company = $("#company").val();
	}
	var modify_address = $("span.address").text();
	if ($("#address").is(':visible')) {
		modify_address = $("#address").val();
	}
	var modify_website = $("span.website").text();
	if ($("#website").is(':visible')) {
		modify_website = $("#website").val();
	}
	var valid = true;
	if ($("#origPass").is(':visible')) {
		if (isNull(origPass)) {
			$("#origPass-tip").text('The original password cannot be null.');
			valid = false;
		} else if (getLength(origPass) < 6 || getLength(origPass) > 16) {
			$("#origPass-tip").text('Please enter 6-16 characters.');
			valid = false;
		}
		if (isNull(inputPass)) {
			$("#inputPass-tip").text('The password cannot be null.');
			valid = false;
		} else if (getLength(inputPass) < 6 || getLength(inputPass) > 16) {
			$("#inputPass-tip").text('Please enter 6-16 characters.');
			valid = false;
		}
		if (isNull(confirmPass)) {
			$("#confirmPass-tip").text('The confirm password cannot be null.');
			valid = false;
		} else if (getLength(confirmPass) < 6 || getLength(confirmPass) > 16) {
			$("#confirmPass-tip").text('Please enter 6-16 characters.');
			valid = false;
		} else {
			if (!isNull(inputPass)) {
				if (inputPass != confirmPass) {
					$("#confirmPass-tip").text(
							'The passwords do not match. Please enter again.');
					valid = false;
				} else if (confirmPass == origPass) {
					// 新密码和旧密码相同
					$("#confirmPass-tip").text(
							'New password cannot equals to the old.');
					valid = false;
				}
			}
		}
	} else {
		origPass = '';
		inputPass = '';
		confirmPass = '';
	}
	if (isNull(modify_name)) {
		$("#name-tip").text('Name cannot be null.');
		valid = false;
	} else if (getLength(modify_name) > 50) {
		$("#name-tip").text('The length should not exceed 50 characters.');
		valid = false;
	}
	if (isNull(modify_phone)) {
		$("#phone-tip").text('Phone cannot be null.');
		valid = false;
	} else if (getLength(modify_phone) > 30) {
		$("#name-tip").text('The length should not exceed 30 characters.');
		valid = false;
	}
	if (isNull(modify_company)) {
		$("#company-tip").text('Company name cannot be null.');
		valid = false;
	} else if (getLength(modify_company) > 30) {
		$("#company-tip").text('The length should not exceed 30 characters.');
		valid = false;
	}
	if (getLength(modify_address) > 100) {
		$("#company-tip").text('The length should not exceed 100 characters.');
		valid = false;
	}
	if (getLength(modify_website) > 50) {
		$("#company-tip").text('The length should not exceed 50 characters.');
		valid = false;
	}
	if (!valid) {
		return;
	}
	showProgress();
	$.post(rootFolder+'/user/modifyPersonalInfo?t=' + Math.random(), {
		origPsw : strEnc(origPass, firstKey, secondKey, thirdKey),
		newPsw : strEnc(inputPass, firstKey, secondKey, thirdKey),
		name : encodeURIComponent(modify_name),
		phone : encodeURIComponent(modify_phone),
		company : encodeURIComponent(modify_company),
		address : encodeURIComponent(modify_address),
		website : encodeURIComponent(modify_website)
	}, function(data) {
		hideProgress();
		if (data.isModify) {
			window.location.href = rootFolder+'/project/index?t=' + Math.random();
		} else {
			$("#modify-tip").html(data.msg);
		}
	}, "json");
}
