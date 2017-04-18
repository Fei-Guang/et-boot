function clearSupplyInfoTip() {
	$("#name-tip").html('&nbsp;');
	$("#phone-tip").html('&nbsp;');
	$("#company-tip").html('&nbsp;');
}
function plusBorder(id) {
	$("input#" + id).addClass("border");
}
function startBorder(id) {
	$("input#" + id).removeClass("border");
}
function supplyInfo() {
	clearSupplyInfoTip();
	var supply_name = $("#name").val();
	var supply_phone = $("#phone").val();
	var supply_company = $("#company").val();
	var supply_address = $("#address").val();
	var supply_website = $("#website").val();
	if (equals(supply_name, $("#name").attr('placeholder'))) {
		supply_name = '';
	}
	if (equals(supply_phone, $("#phone").attr('placeholder'))) {
		supply_phone = '';
	}
	if (equals(supply_company, $("#company").attr('placeholder'))) {
		supply_company = '';
	}
	if (equals(supply_address, $("#address").attr('placeholder'))) {
		supply_address = '';
	}
	if (equals(supply_website, $("#website").attr('placeholder'))) {
		supply_website = '';
	}
	var valid = true;
	if (isNull(supply_name)) {
		$("#name-tip").text('Name cannot be null.');
		valid = false;
	} else if (getLength(supply_name) > 50) {
		$("#name-tip").text('The length should not exceed 50 characters.');
		valid = false;
	}
	if (isNull(supply_phone)) {
		$("#phone-tip").text('Phone cannot be null.');
		valid = false;
	} else if (getLength(supply_phone) > 30) {
		$("#phone-tip").text('The length should not exceed 30 characters.');
		valid = false;
	}
	if (isNull(supply_company)) {
		$("#company-tip").text('Company name cannot be null.');
		valid = false;
	} else if (getLength(supply_company) > 30) {
		$("#company-tip").text('The length should not exceed 30 characters.');
		valid = false;
	}
	if (!valid) {
		return;
	}
	showProgress();
	$.post(rootFolder+'/user/supplyPersonalInfo?t=' + Math.random(), {
		name : encodeURIComponent(supply_name),
		phone : encodeURIComponent(supply_phone),
		company : encodeURIComponent(supply_company),
		address : encodeURIComponent(supply_address),
		website : encodeURIComponent(supply_website)
	}, function(data) {
		hideProgress();
		if (data.isSupply) {
			window.location.href = rootFolder+'/project/index?t=' + Math.random();
		} else {
			$("#supply-tip").html(data.msg);
		}
	}, "json");
}