function addSupplier() {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	$("div.supplierIndex span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div.supplierIndex span#addSupplier").removeAttr("status");
	$("div.supplierIndex tr.item").each(function(i) {
		$(this).removeClass("highlight");
	});
	var num = $("div.supplierIndex tr.item").length;
	num++;
	var sb = new StringBuilder();
	sb.append('<tr class="item edit highlight" onclick="selectOneRow(this);">');
	sb
			.append('<td class="sn unedit"><input type="text" value="'
					+ num
					+ '" readonly="readonly" onfocus="this.blur();"/><input type="hidden" value="0" /></td>');
	sb
			.append('<td class="td3 edit" onclick="selectOneRows(this);"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[required,maxSize[255],custom[specialCharacter]]" data-errormessage-value-missing="* Sub con names cannot be null."/></td>');
	sb
			.append('<td class="td4 edit"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[required,custom[email4Null]]" data-errormessage-value-missing="* Emails cannot be null."/></td>');
	sb
			.append('<td class="td5 edit"><input type="text" value="" onclick="showTradeList(this);" onkeypress="keyPress(event,this);" onblur="hideTradeList();submitModify(this);"/></td>');
	sb
			.append('<td class="td6 edit"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[maxSize[10]]"/></td>');
	sb
			.append('<td class="td7 edit"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[maxSize[50]]"/></td>');
	sb
			.append('<td class="td8 edit"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[maxSize[30]]"/></td>');
	sb
			.append('<td class="td9 edit" class="more"><input type="text" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[maxSize[255]]"/></td>');
	sb.append('</tr>');
	$("div.supplierIndex table.data tbody").append(sb.toString());
	$("div.supplierIndex tr td.td3 input").focus();
	$('div#supplier_end')[0].scrollIntoView();
}
function editSupplier() {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	var tr = $("div.supplierIndex tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	$("div.supplierIndex span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div.supplierIndex span#editSupplier").removeAttr("status");
	$(tr).addClass('edit');
	$(tr).find("input").each(function(i) {
		$(this).removeAttr("readonly");
	});
	$($($(tr).find("td.td5")[0]).find("input")[0]).attr("readonly", "readonly");
}
function deleteSupplier() {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	var sb = new StringBuilder();
	var tr = $("div.supplierIndex tr.highlight");
	var sn = $(tr).find("td.sn")[0];
	var supplierid = $($(sn).find("input")[1]).val();
	sb.append(supplierid);
	if (isNull(sb.toString())) {
		showSelectTip();
		return;
	}
	art.dialog({
		title : 'Tips',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : 'Sure to delete the selected subcontractor?',
		okVal : 'OK',
		ok : function() {
			doDeleteSupplier(sb.toString());
			return true;
		},
		cancelVal : 'Cancel',
		cancel : true
	});
}
function doDeleteSupplier(ids) {
	$.post(rootFolder+'/supplier/batchDelete?t=' + Math.random(), {
		data : encodeURIComponent(ids)
	}, function(data) {
		if (data.isPost) {
			window.location.reload();
		} else {
			setTimeout("showTip('Delete fail.');", 1500);
		}
	}, "json");
}
function downloadSupplier() {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	window.location.href = rootFolder+'/Download/subcontractors.xlsx';
}
function showTradeList(obj) {
	if ($(obj).parents("tr.edit").length == 0) {
		return;
	}
	var t = $(obj).val();
	$('#tradeList').load(rootFolder+'/trade/list?t=' + Math.random(), {
		trade : encodeURIComponent(t)
	}, function() {
		var offset = $(obj).offset();
		$('div.tradeList').css({
			top : offset.top + 26,
			left : offset.left
		});
		$('div.tradeList').show();
		$(obj).removeAttr("readonly");
		$(obj).attr("state", "open");
	});
}
function hideTradeList() {
	// 每个文本框焦点丢失时，触发这个事件，但是这个时候，下个组件还没获得焦点，所以这里延迟判断
	setTimeout(function() {
		if ($('div.tradeList').is(":focus")) {
			return;
		}
		var allInput = $('table.data td.td5 input[state="open"]');
		if ($(allInput).is(":focus")) {
			return;
		}
		$('div.tradeList').hide();
		$(allInput).removeAttr("state");
	}, 100);
}
function selectTrade() {
	var num = $("div#tradeList tr.item").length;
	var sb = new StringBuilder();
	for (var i = 0; i < num; i++) {
		var tr = $("div#tradeList tr.item")[i];
		var chk = $(tr).find("input.chk")[0];
		if ($(chk).prop("checked")) {
			var t = $($($(tr).find("td.td3")[0]).find("input")[0]).val();
			sb.append(t);
			sb.append(',');
		}
	}
	var trade = sb.toString();
	if (!isNull(trade)) {
		$(
				$($($("div.supplierIndex tr.edit")[0]).find("td.td5")[0]).find(
						"input")[0]).val(trade.substring(0, trade.length - 1));
	} else {
		$(
				$($($("div.supplierIndex tr.edit")[0]).find("td.td5")[0]).find(
						"input")[0]).val('');
	}
}
function keyPress(event, obj) {
	if (event.keyCode == 13) {
		// 回车键作为快捷方式提交
		if ($(obj).parents("tr.edit").length == 0) {
			return;
		}
		submitSupplier(obj);
	}
}
function submitModify(obj) {
	if ($(obj).parents("tr.edit").length == 0) {
		return;
	}
	// 每个文本框焦点丢失时，触发这个事件，但是这个时候，下个组件还没获得焦点，所以这里延迟判断
	setTimeout(function() {
		var tr = $(obj).parents("tr.edit")[0];
		var num = $(tr).find("input").length;
		var focus = false;
		for (var i = 0; i < num; i++) {
			var input = $(tr).find("input")[i];
			if ($(input).is(":focus")) {
				focus = true;
				break;
			}
		}
		if (!focus) {
			if ($('div.tradeList').is(":focus")) {
				focus = true;
			}
		}
		if (!focus) {
			submitSupplier(obj);
		}
	}, 100);
}
function submitSupplier(obj) {
	var jsonstr = "[]";
	var jsonarray = eval('(' + jsonstr + ')');
	var num = $("div.supplierIndex tr.edit").length;
	for (var i = 0; i < num; i++) {
		var tr = $("div.supplierIndex tr.edit").get(i);
		var sn = $(tr).find(".sn")[0];
		var td3 = $(tr).find(".td3")[0];
		var td4 = $(tr).find(".td4")[0];
		var td5 = $(tr).find(".td5")[0];
		var td6 = $(tr).find(".td6")[0];
		var td7 = $(tr).find(".td7")[0];
		var td8 = $(tr).find(".td8")[0];
		var td9 = $(tr).find(".td9")[0];
		if ($($(td3).find("input")[0]).validationEngine("validate")) {
			// 这里有点奇怪，非法的返回为true
			return;
		}
		if ($($(td4).find("input")[0]).validationEngine("validate")) {
			// 这里有点奇怪，非法的返回为true
			return;
		}
		var supplierid = $($(sn).find("input")[1]).val();
		var name = $($(td3).find("input")[0]).val();
		var email = $($(td4).find("input")[0]).val();
		var trade = $($(td5).find("input")[0]).val();
		var level = $($(td6).find("input")[0]).val();
		var contacts = $($(td7).find("input")[0]).val();
		var telephone = $($(td8).find("input")[0]).val();
		var address = $($(td9).find("input")[0]).val();
		var arr = {
			"supplierid" : encodeURIComponent(supplierid),
			"name" : encodeURIComponent(name),
			"email" : encodeURIComponent(email),
			"trade" : encodeURIComponent(trade),
			"level" : encodeURIComponent(level),
			"contacts" : encodeURIComponent(contacts),
			"telephone" : encodeURIComponent(telephone),
			"address" : encodeURIComponent(address)
		}
		jsonarray.push(arr);
	}
	$.post(rootFolder+'/supplier/batchEdit?t=' + Math.random(), {
		data : JSON.stringify(jsonarray)
	}, function(data) {
		if (data.isPost == res_ok) {
			window.location.reload();
		} else if (data.isPost == res_error) {
			setTimeout("showTip('" + data.msg + "');", 1500);
		} else if (data.isPost == res_nameExisted) {
			var tr = $(obj).parents("tr.edit")[0];
			var td3 = $(tr).find(".td3")[0];
			// 重复留给前端提示
			$($(td3).find("input")[0]).validationEngine("showPrompt",
					"* " + data.msg, "error", "bottomLeft", true);
		} else if (data.isPost == res_emailExisted) {
			var tr = $(obj).parents("tr.edit")[0];
			var td4 = $(tr).find(".td4")[0];
			// 重复留给前端提示
			$($(td4).find("input")[0]).validationEngine("showPrompt",
					"* " + data.msg, "error", "bottomLeft", true);
		}
	}, "json");
}
function cancel() {
	var callback = getParam('callback');
	if (isNull(callback)) {
		window.location.reload();
	} else {
		window.location.href = callback;
	}
}
function importSupplier(attachPath) {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	$.get(rootFolder+'/supplier/importSupplier?t=' + Math.random(), {
		data : attachPath
	}, function(data) {
		hideProgress();
		if (data.isPost) {
			window.location.reload();
		} else {
			setTimeout("showTip('" + data.msg + "');", 1500);
		}
	}, "json");
}
function showTradeManagementDialog() {
	goTop();
	$('#tradeManagementDialog').addClass('popupAnimate').show().css('height',
			$(document).height());
}
function tradeManagement() {
	if ($("div.supplierIndex tr.edit").length > 0) {
		return;
	}
	$('#tradeData').load(rootFolder+'/trade/index?t=' + Math.random(), {},
			function() {
				showTradeManagementDialog();
			});
}
function addTrade() {
	if ($("form#tradeForm tr.edit").length > 0) {
		return;
	}
	$("div#tradeManagementDialog span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div#tradeManagementDialog span#addTrade").removeAttr("status");
	$("div#tradeManagementDialog tr.item").each(function(i) {
		$(this).removeClass("highlight");
	});
	var num = $("form#tradeForm tr.item").length;
	num++;
	var sb = new StringBuilder();
	sb.append('<tr class="item edit highlight" onclick="selectOneRow(this);">');
	sb
			.append('<td class="sn unedit"><input type="text" value="'
					+ num
					+ '" readonly="readonly" onfocus="this.blur();"/><input type="hidden" value="0" /></td>');
	sb
			.append('<td class="td3 edit"><input type="text" value="" onkeypress="keyPressTrade(event,this);" onblur="submitTradeModify(this);" class="validate[required,maxSize[30]]" data-errormessage-value-missing="* Trade cannot be null."/></td>');
	sb
			.append('<td class="td4 edit"><input type="text" value="" onkeypress="keyPressTrade(event,this);" onblur="submitTradeModify(this);" class="validate[maxSize[100]]"/></td>');
	sb.append('</tr>');
	$("form#tradeForm table.data tbody").append(sb.toString());
	$("form#tradeForm table.data td.td3 input").focus();
	$('div#trade_end')[0].scrollIntoView();
}
function editTrade() {
	if ($("form#tradeForm tr.edit").length > 0) {
		return;
	}
	var tr = $("form#tradeForm tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	$("div#tradeManagementDialog span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div#tradeManagementDialog span#editTrade").removeAttr("status");
	$(tr).addClass('edit');
	$(tr).find("input").each(function(i) {
		$(this).removeAttr("readonly");
	});
}
function deleteTrade() {
	if ($("form#tradeForm tr.edit").length > 0) {
		return;
	}
	var sb = new StringBuilder();
	var tr = $("form#tradeForm tr.highlight");
	var sn = $(tr).find("td.sn")[0];
	var tradeid = $($(sn).find("input")[1]).val();
	sb.append(tradeid);
	if (isNull(sb.toString())) {
		showSelectTip();
		return;
	}
	art.dialog({
		title : 'Tips',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : 'Confirm to delete the selected trade?',
		okVal : 'OK',
		ok : function() {
			doDeleteTrade(sb.toString());
			return true;
		},
		cancelVal : 'Cancel',
		cancel : true
	});
}
function doDeleteTrade(ids) {
	$.post(rootFolder+'/trade/batchDelete?t=' + Math.random(), {
		data : encodeURIComponent(ids)
	}, function(data) {
		if (data.isPost) {
			$('#tradeData').load(rootFolder+'/trade/index?t=' + Math.random());
		} else {
			setTimeout("showTip('Delete fail.');", 1500);
		}
	}, "json");
}
function keyPressTrade(event, obj) {
	if (event.keyCode == 13) {
		// 回车键作为快捷方式提交
		if ($(obj).parents("tr.edit").length == 0) {
			return;
		}
		submitTrade(obj);
	}
}
function submitTradeModify(obj) {
	if ($(obj).parents("tr.edit").length == 0) {
		return;
	}
	// 每个文本框焦点丢失时，触发这个事件，但是这个时候，下个组件还没获得焦点，所以这里延迟判断
	setTimeout(function() {
		var tr = $(obj).parents("tr.edit")[0];
		var num = $(tr).find("input").length;
		var focus = false;
		for (var i = 0; i < num; i++) {
			var input = $(tr).find("input")[i];
			if ($(input).is(":focus")) {
				focus = true;
				break;
			}
		}
		if (!focus) {
			submitTrade(obj);
		}
	}, 100);
}
function submitTrade(obj) {
	var jsonstr = "[]";
	var jsonarray = eval('(' + jsonstr + ')');
	var num = $("form#tradeForm tr.edit").length;
	for (var i = 0; i < num; i++) {
		var tr = $("form#tradeForm tr.edit").get(i);
		var sn = $(tr).find(".sn")[0];
		var td3 = $(tr).find(".td3")[0];
		var td4 = $(tr).find(".td4")[0];
		if ($($(td3).find("input")[0]).validationEngine("validate")) {
			// 这里有点奇怪，非法的返回为true
			return;
		}
		if ($($(td4).find("input")[0]).validationEngine("validate")) {
			// 这里有点奇怪，非法的返回为true
			return;
		}
		var tradeid = $($(sn).find("input")[1]).val();
		var code = $($(td3).find("input")[0]).val();
		var description = $($(td4).find("input")[0]).val();
		var arr = {
			"tradeid" : encodeURIComponent(tradeid),
			"code" : encodeURIComponent(code),
			"description" : encodeURIComponent(description)
		}
		jsonarray.push(arr);
	}
	$.post(rootFolder+'/trade/batchEdit?t=' + Math.random(), {
		data : JSON.stringify(jsonarray)
	}, function(data) {
		if (data.isPost == res_ok) {
			$('#tradeData').load(rootFolder+'/trade/index?t=' + Math.random());
		} else if (data.isPost == res_error) {
			setTimeout("showTip('" + data.msg + "');", 1500);			
		} else {			
			// 重复留给前端提示
			var tr = $(obj).parents("tr.edit")[0];
			var td3 = $(tr).find(".td3")[0];
			// 重复留给前端提示
			$($(td3).find("input")[0]).validationEngine("showPrompt",
					"* " + data.msg, "error", "bottomLeft", true);
			//自动滚动到底部
			var tradeScroll = document.getElementById('tradeData');		
			tradeScroll.scrollTop=tradeScroll.scrollHeight;
		}
	}, "json");
}
$(function() {
	var h = $(window).height() - 190;
	$('div.supplierIndex div.autoScroll').css("height", h);
	$('div#tradeManagementDialog div.autoScroll').css("height", h - 80);
	$('#importSupplier').fileupload({
		url : rootFolder+'/uploadFile',
		autoUpload : true,
		dataType : 'json',
		// 附加参数
		formData : {
			suffix : 'xls,xlsx',
			size : 5242880
		},
		// 开始上传的回调函数
		start : function() {
			showProgress();
		},
		// 文件上传完毕的回调函数
		done : function(e, data) {
			if (data.result.ret) {
				// 文件上传成功，开始导入数据
				importSupplier(data.result.attachPath);
			} else {
				hideProgress();
				showTip(data.result.msg);
			}
		},
		// 上传进度事件的回调函数
		progressall : function(e, data) {

		}
	});
	$('#tradeManagementDialog').on('click', '.close', function() {
		$('#tradeManagementDialog').hide();
	});
	$('#tradeManagementDialog').on('click', '.button .cancel', function() {
		$('#tradeManagementDialog').hide();
	});
	$('#supplierForm').validationEngine("attach", {
		promptPosition : "bottomLeft",
		scroll : false
	});
	changeMenuItemStatus();
});