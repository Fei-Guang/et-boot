function saveInSession(paramName, paramValue, succeed, failed) {
	$.post(rootFolder+'/saveInSession?t=' + Math.random(), {
		key : paramName,
		value : paramValue
	}, function(data) {
		if (data.ret) {
			if (typeof (succeed) == "function") {
				succeed();
			}
		} else {
			if (typeof (failed) == "function") {
				failed();
			}
		}
	}, "json");
}
function removeFromSession(paramName, succeed, failed) {
	$.post(rootFolder+'/removeFromSession?t=' + Math.random(), {
		key : paramName
	}, function(data) {
		if (data.ret) {
			if (typeof (succeed) == "function") {
				succeed();
			}
		} else {
			if (typeof (failed) == "function") {
				failed();
			}
		}
	}, "json");
}
function saveInCookie(paramName, paramValue) {
	$.cookie(paramName, paramValue, {
		expires : 7,
		path : '/'
	});
}
function removeFromCookie(paramName) {
	$.cookie(paramName, '', {
		expires : -1,
		path : '/'
	});
}
function goTop() {
	// 滚动到页面顶部
	$("html").animate({
		scrollTop : 0
	}, "normal"); // IE,FF
	$("body").animate({
		scrollTop : 0
	}, "normal"); // Webkit
}
function position(element, distance) {
	$(element).animate({
		scrollTop : distance
	}, "normal");
}
function doSubmit(event) {
	if (event.keyCode == 13) {
		// 回车键作为快捷方式提交
		document.forms[0].submit();
		event.returnValue = false;
	}
}
function hidePrivacy(str) {
	if (isTelePhone(str)) {
		return str.substring(0, str.length - 5) + "***"
				+ str.substring(str.length - 2);
	}
	if (isMobilePhone(str)) {
		return str.substring(0, 3) + "******" + str.substring(str.length - 2);
	}
	if (isEmail(str)) {
		var index = str.indexOf("@");
		var head = str.substring(0, index);
		return head.charAt(0) + "***" + head.charAt(head.length - 1)
				+ str.substring(index);
	}
	return str;
}
function setCenter(Xelement) {
	var parent = Xelement.parentNode;
	parent.style.position = "absolute";
	Xelement.style.position = "absolute";
	var left = (parent.clientWidth - Xelement.clientWidth) / 2;
	var top = (parent.clientHeight - Xelement.clientHeight) / 2;
	Xelement.style.left = left + "px";
	Xelement.style.top = top + "px";
}
function focusBorder(control) {
	$(control).css("border", "1px solid #01c675");
}
function blurBorder(control) {
	$(control).css("border", "1px solid #e5e5e5");
}
function logout() {
	window.location = rootFolder+'/login/logout';
}
function changeCheckBox(btn) {
	var button = $(btn);
	var id = button.attr("for");
	if ($('input#' + id).attr('choose') == 'true') {
		$('input#' + id).prop("checked", false);
		$('input#' + id).attr('choose', 'false');
	} else {
		$('input#' + id).prop("checked", true);
		$('input#' + id).attr('choose', 'true');
	}
}
function editOneRow(obj) {
	var table = $(obj).parents("table")[0];
	if ($(table).find("tr.edit").length > 0) {
		return;
	}
	$(obj).removeClass("highlight");
	$(obj).addClass("edit");
	$(obj).find("input").each(function(i) {
		$(this).removeAttr("readonly");
	});
}
function selectOneRow(obj) {
	var table = $(obj).parents("table")[0];
	$(table).find("tr").each(function(i) {
		$(this).removeClass("highlight");
	});
	$(obj).addClass("highlight");
}
function selectOneRows(obj) {
	var table = $(obj).parents("table")[0];
	$(table).find("td").each(function(i) {
		$(this).parent().removeClass("highlight");
	});
	$(obj).parent().addClass("highlight");
}
function changeMenuItemStatus() {
	$("span.menuItem").click(function() {
		if ($(this).attr("status")) {
			return;
		}
		$("span.menuItem").removeClass("active");
		$(this).addClass("active");
	});
}
function clearCookie() {
	$.cookie("remember", "false", {
		expires : -1,
		path : '/'
	});
	$.cookie('email', '', {
		expires : -1,
		path : '/'
	});
	$.cookie('pwd', '', {
		expires : -1,
		path : '/'
	});
}
