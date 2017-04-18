function showSubcontractList(id) {
	$("input#" + id).addClass("blueBorder");
	// 第一步，选择分包工程，应该隐藏所有的提示
	$('input#subContract').validationEngine("hide");
	$('input#timeZone').validationEngine("hide");
	$('input#endTime').validationEngine("hide");
	$('div.projectInquire div.subcontractList').show();
	$('div.projectInquire div.subcontractList').focus();
}
function hideSubcontractList() {
	$('div.projectInquire div.subcontractList').hide();
	$("div#selectSubcontract input").removeClass("blueBorder");
	var subcontract = $('input#subContract').val();
	if (isNull(subcontract)) {
		$('input#subContract').validationEngine("showPrompt",
				"* Subcontract cannot be null.", "error", "bottomLeft", true);
	} else {
		$('input#subContract').validationEngine("hide");
	}
}
function selectSubcontract(name, id) {
	$('div.projectInquire div#selectSubcontract input').val(name);
	hideSubcontractList();
	saveSubContractRecord(name, id);
}
function deleteSubItem(btn, id) {
	$.get(rootFolder + '/project/deleteSubProject?t=' + Math.random(), {
		subProjectID : encodeURIComponent(id)
	},
			function(data) {
				if (data.isPost == 0) {
					var subcontractListItem = $(btn).parents(
							"div.subcontractListItem")[0];
					$(subcontractListItem).remove();
				} else {
					// 删除不成功的处理
					showTip(data.msg);
				}
			}, "json");
}

function showTimeZoneList(id) {
	// 第二步选择时区，应该隐藏时区和时间的提示框，并对第一步分包工程进行判断
	$('input#timeZone').validationEngine("hide");
	$('input#endTime').validationEngine("hide");
	var subcontract = $('input#subContract').val();
	if (isNull(subcontract)) {
		$('input#subContract').validationEngine("showPrompt",
				"* Subcontract cannot be null.", "error", "bottomLeft", true);
		return;
	} else {
		$("div#selectTime input#" + id).addClass("blueBorder");
		$('input#subContract').validationEngine("hide");
	}
	$('div.projectInquire div.timeZoneList').show();
	$('div.projectInquire div.timeZoneList').focus();
}
function hideTimeZoneList() {
	$('div.projectInquire div.timeZoneList').hide();
	$("div#selectTime input").eq(0).removeClass("blueBorder");
	var timeZone = $('input#timeZone').val();
	if (isNull(timeZone)) {
		$('input#timeZone').validationEngine("showPrompt",
				"* Timezone cannot be null.", "error", "bottomLeft", true);
		return;
	} else {
		$('input#timeZone').validationEngine("hide");
	}
}
function showAllTimeZone() {
	$('div#timeZoneList div.predefine').hide();
	$('div#timeZoneList div.all').show();
	$('div#timeZoneList').height(250);
}
function selectTimeZone(name) {
	$('div.projectInquire input#timeZone').val(name);
	hideTimeZoneList();
	saveTimeZoneRecord(name);
}
function subConManagement() {
	window.location.href = rootFolder + '/supplier/index?t=' + Math.random()
			+ '&callback=' + window.location;
}
function tradePopupShow() {
	$('div.projectInquire div.table div.tradePopup').show();
	$('div.projectInquire div.table div.tradePopup').focus();
}
function searchTrade(btn) {
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var inquire_trade = $($(div).find("input")[0]).val();
	if (!isNull(inquire_trade)) {
		$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon")
				.addClass("quoteFilterIcon_succ");
		var desClass = $("table#dragTable tr .td3 span").attr("class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
		var paramName = 'inquire_trade';
		var paramValue = inquire_trade;
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.projectInquire div.table div.tradePopup').hide();
		});
	}
}
function filterTrade() {
	var sb = new StringBuilder();
	var trs = $('table.trade tbody').find('tr');
	var t = 0;
	for (var i = 0; i < trs.length; i++) {
		var td = $(trs[i]).find('td')[0];
		if ($($(td).find('input')[0]).prop('checked')) {
			var t = $($(trs[i]).find('td')[1]).text();
			sb.append(t);
			sb.append(",");
			t++;
		}
	}
	if (t == 0) {
		$("div.projectInquire div#allSubCon table .td5 span").removeClass(
				"tradePopuIcon").addClass("pop-up");
		var tradeClass = $("div.projectInquire div#allSubCon table .td5 span")
				.attr("class");
		$.cookie('tradeClass', tradeClass, {
			expires : 7,
			path : '/'
		})
	} else {
		$("div.projectInquire div#allSubCon table .td5 span").removeClass(
				"pop-up").addClass("tradePopuIcon");
		var tradeClass = $("div.projectInquire div#allSubCon table .td5 span")
				.attr("class");
		$.cookie('tradeClass', tradeClass, {
			expires : 7,
			path : '/'
		})
	}
	var inquire_trade = $("input#tradeKey").val();
	if (!isNull(inquire_trade)) {
		sb.append(inquire_trade);
		$("div.projectInquire div#allSubCon table .td5 span").removeClass(
				"pop-up").addClass("tradePopuIcon");
		var tradeClass = $("div.projectInquire div#allSubCon table .td5 span")
				.attr("class");
		$.cookie('tradeClass', tradeClass, {
			expires : 7,
			path : '/'
		})
	}
	var paramName = 'inquire_trade';
	var paramValue = sb.toString();
	saveInSession(paramName, paramValue, function() {
		window.location.reload();
	}, function() {
		// TODO 保存错误时候的处理
		$('div.projectInquire div.table div.tradePopup').hide();
	});
}
function cancelTrade() {
	$("div.projectInquire div#allSubCon table .td5 span").removeClass(
			"tradePopuIcon").addClass("pop-up");
	var tradeClass = $(" div.projectInquire div#allSubCon table .td5 span")
			.attr("class");
	$.cookie('tradeClass', tradeClass, {
		expires : 7,
		path : '/'
	})
	var paramName = 'inquire_trade';
	var paramValue = '';
	saveInSession(paramName, paramValue, function() {
		window.location.reload();
	}, function() {
		// TODO 保存错误时候的处理
		$('div.projectInquire div.table div.tradePopup').hide();
	});
}
function hideTradePopup() {
	setTimeout('doHideTradePopup();', 400);
}
function doHideTradePopup() {
	var isFocus = $("div.tradePopup div.searchBox input").is(":focus")
			|| $('div.projectInquire div.table div.tradePopup').is(":focus");
	if (isFocus == true) {
		return;
	} else {
		$('div.projectInquire div.table div.tradePopup').hide();
	}
}
// 等级过滤框
function levelPopupShow() {
	$('div.projectInquire div.table div.levelPopup').show();
	$('div.projectInquire div.table div.levelPopup').focus();
}
function searchLevel(btn) {
	/* var sb = new StringBuilder(); */
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var inquire_level = $($(div).find("input")[0]).val();
	if (!isNull(inquire_level)) {
		/*
		 * sb.append("<tr>"); sb.append("<td class='box'></td>");
		 * sb.append("<td class='kw'>" + inquire_level + "</td>");
		 * sb.append("<td class='addOrDelete'></td>"); sb.append("</tr>");
		 * sb.toString();
		 */
		var paramName = 'inquire_level';
		var paramValue = inquire_level;
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.projectInquire div.table div.levelPopup').hide();
		});
	}
}
function filterLevel() {
	var sb = new StringBuilder();
	var trs = $('table.level tbody').find('tr');
	var t = 0;
	for (var i = 0; i < trs.length; i++) {
		var td = $(trs[i]).find('td')[0];
		if ($($(td).find('input')[0]).prop('checked')) {
			var t = $($(trs[i]).find('td')[1]).text();
			sb.append(t);
			sb.append(",");
			t++;
		}
	}
	if (t == 0) {
		$("div.projectInquire div#allSubCon table .td6 span").removeClass(
				"levePopuIcon").addClass("pop-up");
		var leveClass = $("div.projectInquire div#allSubCon table .td6 span")
				.attr("class");
		$.cookie('leveClass', leveClass, {
			expires : 7,
			path : '/'
		})
	} else {
		$("div.projectInquire div#allSubCon table .td6 span").removeClass(
				"pop-up").addClass("levePopuIcon");
		var leveClass = $("div.projectInquire div#allSubCon table .td6 span")
				.attr("class");
		$.cookie('leveClass', leveClass, {
			expires : 7,
			path : '/'
		})
	}
	var inquire_level = $("input#levelKey").val();
	if (!isNull(inquire_level)) {
		sb.append(inquire_level);
		$("div.projectInquire div#allSubCon table .td6 span").removeClass(
				"pop-up").addClass("levePopuIcon");
		var leveClass = $("div.projectInquire div#allSubCon table .td6 span")
				.attr("class");
		$.cookie('leveClass', leveClass, {
			expires : 7,
			path : '/'
		})
	}
	var paramName = 'inquire_level';
	var paramValue = sb.toString();
	saveInSession(paramName, paramValue, function() {
		window.location.reload();
	}, function() {
		// TODO 保存错误时候的处理
		$('div.projectInquire div.table div.levelPopup').hide();
	});
}
function cancelLevel() {
	$("div.projectInquire div#allSubCon table .td6 span").removeClass(
			"levePopuIcon").addClass("pop-up");
	var leveClass = $("div.projectInquire div#allSubCon table .td6 span").attr(
			"class");
	$.cookie('leveClass', leveClass, {
		expires : 7,
		path : '/'
	})
	var paramName = 'inquire_level';
	var paramValue = '';
	saveInSession(paramName, paramValue, function() {
		window.location.reload();
	}, function() {
		// TODO 保存错误时候的处理
		$('div.projectInquire div.table div.levelPopup').hide();
	});
}
function hideLevelPopup() {
	setTimeout('doHideLevelPopup();', 400);
}
function doHideLevelPopup() {
	var isFocus = $("div.levelPopup div.searchBox input").is(":focus")
			|| $('div.projectInquire div.table div.levelPopup').is(":focus")
			|| $('div.projectInquire div.table div.choose').is(":focus");
	if (isFocus == true) {
		return;
	} else {
		$('div.projectInquire div.table div.levelPopup').hide();
	}

}

function selectSupplier(btn) {
	var button = $(btn);
	var id = button.attr("for");
	var tr = $(btn).parents("tr")[0];
	var supplierid = $($($(tr).find("td.box")[0]).find("input")[1]).val();
	var name = $($($(tr).find("td.td3")[0]).find("input")[0]).val();
	var email = $($($(tr).find("td.td4")[0]).find("input")[0]).val();
	var subcontract = $('input#subContract').val();
	var timeZone = $('input#timeZone').val();
	var datetime = $('input#endTime').val();
	var identification = subcontract + '_' + supplierid;
	if ($('input#' + id).prop("checked")) {
		// 首先，对第一步分包工程进行判断
		if (isNull(subcontract)) {
			$('input#' + id).prop("checked", false);
			$('input#' + id).attr('choose', 'false');
			$('input#subContract').validationEngine("showPrompt",
					"* Subcontract cannot be null.", "error", "bottomLeft",
					true);
			return;
		} else {
			$('input#subContract').validationEngine("hide");
		}
		// 其次，对第二步时区进行判断
		if (isNull(timeZone)) {
			$('input#' + id).prop("checked", false);
			$('input#' + id).attr('choose', 'false');
			$('input#timeZone').validationEngine("showPrompt",
					"* Timezone cannot be null.", "error", "bottomLeft", true);
			return;
		} else {
			$('input#timeZone').validationEngine("hide");
		}
		// 再次，对第二步时间进行判断
		if (isNull(datetime)) {
			$('input#' + id).prop("checked", false);
			$('input#' + id).attr('choose', 'false');
			$('input#endTime').validationEngine("showPrompt",
					"* Datetime cannot be null.", "error", "bottomLeft", true);
			return;
		} else {
			$('input#endTime').validationEngine("hide");
		}
		// 选中
		var num = $("div#selectedSubCon tr.item").length;
		var parent = $('input#' + id).parent().parent().find("td")
				.find("input");
		$(parent).css("color", "#0074ba");
		for (var i = 0; i < num; i++) {
			var selectedSupplierid = $($("div#selectedSubCon tr.item")[i])
					.attr("id");
			if (selectedSupplierid == identification) {
				return;
			}
		}
		num++;
		var sb = new StringBuilder();
		sb.append('<tr class="item" id="' + identification + '" sid="'
				+ supplierid + '">');
		sb.append('<td class="box"></td>');
		sb.append('<td class="sn unedit"><input type="text" value="' + num
				+ '" readonly="readonly" onfocus="this.blur();" /></td>');
		sb.append('<td class="td3 unedit"><input type="text" title="'
				+ subcontract + '" value="' + subcontract
				+ '" readonly="readonly" onfocus="this.blur();" /></td>');
		sb.append('<td class="td4 unedit"><input type="text" title="' + name
				+ '" value="' + name
				+ '" readonly="readonly" onfocus="this.blur();" /></td>');
		sb.append('<td class="td5 unedit"><input type="text" title="' + email
				+ '" value="' + email
				+ '" readonly="readonly" onfocus="this.blur();" /></td>');
		sb.append('<td class="td6 unedit"><input type="text" title="'
				+ datetime + '" value="' + datetime
				+ '" readonly="readonly" onfocus="this.blur();" /></td>');
		sb
				.append('<td class="td7"><span class="uploadAttach controlItem" onclick="uploadAttach(this);"></span><span	class="plusAttach" onclick="showAttach(this);"></span><span class="deleteItem controlItem" onclick="deleteItem(this);"></span></td>');
		sb.append('<td class="td8"></td>');
		sb.append('</tr>');
		var th = new StringBuilder();
		th.append('<tr class="tip" id="' + identification + '" sid="'
				+ supplierid + '">');
		th.append('<th class="box"></th>');
		th.append('<th class="sn unedit"><input type="text" value="' + num
				+ '" readonly="readonly" onfocus="this.blur();" /></th>');
		th.append('<th class="td3 unedit"><input type="text" title="'
				+ subcontract + '" value="' + subcontract
				+ '" readonly="readonly" onfocus="this.blur();" /></th>');
		th.append('<th class="td4 unedit"><input type="text" title="' + name
				+ '" value="' + name
				+ '" readonly="readonly" onfocus="this.blur();" /></th>');
		th.append('<th class="td5 unedit"><input type="text" title="' + email
				+ '" value="' + email
				+ '" readonly="readonly" onfocus="this.blur();" /></th>');
		th.append('<th class="td6 unedit"><input type="text" title="'
				+ datetime + '" value="' + datetime
				+ '" readonly="readonly" onfocus="this.blur();" /></th>');
		th
				.append('<th class="td7"><span class="uploadAttach controlItem" onclick="uploadAttach(this);"></span><span	class="plusAttach" onclick="showAttach(this);"></span><span class="deleteItem controlItem" onclick="deleteItem(this);"></span></th>');
		th.append('<th class="td8"></th>');
		th.append('</tr>');
		$("div#selectedSubCon table.data tbody").append(sb.toString());
		$("div#selectedSubCon table.tip thead").html(th.toString());
		saveSupplierRecord(supplierid, name, email);
	} else {
		// 未选中
		clearLastSelectedSubCon();
		$("div#selectedSubCon tr").remove(
				"tr[id=" + subcontract + "_" + supplierid + "]");
		$("div#selectedSubCon tr").each(function(i) {
			$($($(this).find("td.sn")[0]).find("input")[0]).val(i - 1);
		});
		var parent = $('input#' + id).parent().parent().find("td")
				.find("input");
		$(parent).css("color", "#000");
		deleteSupplierRecord(subcontract, supplierid);
	}
}
function clearLastSelectedSubCon() {
	var th = new StringBuilder();
	th.append('<tr class="tip">');
	th.append('<th class="box"></th>');
	th.append('<th class="sn"></th>');
	th.append('<th class="td3"></th>');
	th.append('<th class="td4"></th>');
	th.append('<th class="td5"></th>');
	th.append('<th class="td6"></th>');
	th.append('<th class="td7"></th>');
	th.append('<th class="td8"></th>');
	th.append('</tr>');
	$("div#selectedSubCon table.tip thead").html(th.toString());
}
// 保存分包工程选择
function saveSubContractRecord(name, id) {
	$.get(rootFolder + '/project/saveSubContractRecord?t=' + Math.random(), {
		subContract : encodeURIComponent(name),
		subProjectID : encodeURIComponent(id)
	}, function(data) {
		// 刷新设置选项
		recoveryInquireStatus(name);
		if (!data.isPost) {
			// 保存不成功的处理
		}
	}, "json");
}
// 保存分包工程对应的时区
function saveTimeZoneRecord(zone) {
	$.get(rootFolder + '/project/saveTimeZoneRecord?t=' + Math.random(), {
		subContract : encodeURIComponent($('input#subContract').val()),
		timeZone : encodeURIComponent(zone)
	}, function(data) {
		if (!data.isPost) {
			// 保存不成功的处理
		}
	}, "json");
}
// 保存分包工程对应的结束时间
function saveEndTimeRecord(time) {
	$.get(rootFolder + '/project/saveEndTimeRecord?t=' + Math.random(), {
		subContract : encodeURIComponent($('input#subContract').val()),
		endTime : encodeURIComponent(time)
	}, function(data) {
		if (!data.isPost) {
			// 保存不成功的处理
		}
	}, "json");
}
// 保存分包工程对应的分包商
function saveSupplierRecord(id, supplierName, supplierEmail) {
	$.get(rootFolder + '/project/saveSupplierRecord?t=' + Math.random(), {
		subContract : encodeURIComponent($('input#subContract').val()),
		supplierID : encodeURIComponent(id),
		name : encodeURIComponent(supplierName),
		email : encodeURIComponent(supplierEmail)
	}, function(data) {
		if (!data.isPost) {
			// 保存不成功的处理
		}
	}, "json");
}
// 删除分包工程对应的分包商
function deleteSupplierRecord(subcontract, id) {
	$.get(rootFolder + '/project/deleteSupplierRecord?t=' + Math.random(), {
		subContract : encodeURIComponent(subcontract),
		supplierID : encodeURIComponent(id)
	}, function(data) {
		if (!data.isPost) {
			// 删除不成功的处理
		}
	}, "json");
}
// 点击下载附件按钮
function uploadAttach(btn) {
	var tr = $(btn).parents("tr")[0];
	// 分包工程名称
	var subcontract = $($($(tr).find(".td3")[0]).find("input")[0]).val();
	// 分包商ID
	var sid = $(tr).attr("sid");
	if (isNull(subcontract) || isNull(sid)) {
		return;
	}
	$('input#subProject').val(subcontract);
	$('input#supplierID').val(sid);
	$('#fileupload').click();
}
function againLoadAttach() {
	var tr = $('#' + $('#attachment').attr('trindex'))[0];
	// 分包工程名称
	var subcontract = $($($(tr).find(".td3")[0]).find("input")[0]).val();
	// 分包商ID
	var sid = $(tr).attr("sid");
	if (isNull(subcontract) || isNull(sid)) {
		return;
	}
	$('input#subProject').val(subcontract);
	$('input#supplierID').val(sid);
	$('#fileupload').click();
}
// 保存分包商附件记录
function saveSupplierAttachRecord(name, path) {
	var subcontract = $('input#subProject').val();
	var id = $('input#supplierID').val();
	$.get(rootFolder + '/project/saveSupplierAttachRecord?t=' + Math.random(),
			{
				fileName : encodeURIComponent(name),
				attachPath : encodeURIComponent(path),
				subContract : encodeURIComponent(subcontract),
				supplierID : encodeURIComponent(id),
			}, function(data) {
				if (!data.isPost) {
					// 保存不成功的处理
					showTip('The attachment is missing.');
				} else {
					// 显示对应行的附件数量
					var aNum = data.attachs;
					var identification = subcontract + '_' + id;
					$('tr#' + identification).each(
							function(i) {
								$($($(this).find(".td7")[0]).find("span")[1])
										.addClass("showAttach");
								$($($(this).find(".td7")[0]).find("span")[1])
										.html(aNum);
							});
				}
			}, "json");
}
// 显示附件框
function showAttach(btn) {
	var trIndex = $(btn).parent().parent().attr('id');
	$('#attachment').attr('trIndex', trIndex);
	var that = $(this);
	var tr = $(btn).parents("tr")[0];
	var sid = $(tr).attr("sid");
	var offset = $(tr).offset();
	var w = $("div#selectedSubCon table.data").width();
	var subproject = $($($(tr).find(".td3")[0]).find("input")[0]).val();
	$("div#attachment div.data").load(
			rootFolder + '/project/attach?t=' + Math.random(), {
				subContract : encodeURIComponent(subproject),
				supplierID : encodeURIComponent(sid)
			}, function(data) {
				var h = $("div#attachment").height();
				$("div#attachment").css({
					'width' : w,
					'top' : offset.top - 60 - h,
					'display' : 'block'
				});
			});
}
function hideAttach() {
	$("div#attachment").hide();
}
// 删除分包商附件记录
function deleteSupplierAttachRecord(btn, subcontract, id, path) {
	var li = $(btn).parents("li")[0];
	$.get(
			rootFolder + '/project/deleteSupplierAttachRecord?t='
					+ Math.random(), {
				attachPath : encodeURIComponent(path),
				subContract : encodeURIComponent(subcontract),
				supplierID : encodeURIComponent(id)
			}, function(data) {
				if (data.isPost) {
					$(li).remove();
				} else {
					// 删除不成功的处理
				}
			}, "json");
	var tr = $('#' + $('#attachment').attr('trindex'))[0];
	var s = $(tr).find("span.showAttach").text();
	s--;
	$(tr).find("span.showAttach").text(s);
	$("div#selectedSubCon div.autoScroll table.data").find("tr").each(
			function() {
				if ($(this).attr("id") == $(tr).attr("id")) {
					$(this).find("span.showAttach").html(s);
				}
			})
}
// 删除分包商记录
function deleteItem(btn) {
	var tr = $(btn).parents("tr")[0];
	var id = $(tr).attr("id");
	var sid = $(tr).attr("sid");
	$("div#selectedSubCon tr").remove("tr[id=" + id + "]");
	$("div#selectedSubCon tr").each(function(i) {
		$($($(this).find("td.sn")[0]).find("input")[0]).val(i - 1);
	});
	var subcontract = $('input#subContract').val();
	var subproject = $($($(tr).find(".td3")[0]).find("input")[0]).val();
	if (subproject == subcontract) {
		$("div#allSubCon tr").each(
				function(i) {
					var supplierid = $(
							$($(this).find("td.box")[0]).find("input")[1])
							.val();
					if (supplierid == sid) {
						// 取消选中
						$($($(this).find("td.box")[0]).find("input")[0]).prop(
								"checked", false);
						$($($(this).find("td.box")[0]).find("input")[0]).attr(
								'choose', 'false');
					}
				});
	}
	deleteSupplierRecord(subproject, sid);
	clearLastSelectedSubCon();
}
function recoveryInquireStatus(selectSubcontract) {
	$('input.chk').prop("checked", false);
	$('input.chk').attr('choose', 'false');
	$.get(rootFolder + '/project/recoveryInquireStatus?t=' + Math.random(), {
		lastSetting : encodeURIComponent(selectSubcontract)
	}, function(data) {
		if (data.isPost) {
			// 返回成功，还原界面参数
			$('div.projectInquire div#selectSubcontract input').val(
					data.lastSetting);
			$('div.projectInquire input#timeZone').val(data.timeZone);
			$('div.projectInquire input#endTime').val(data.endTime);
			var subcontractors = data.subcontractors;
			var subName = data.lastSetting;
			if (!isNullOrUndefined(subcontractors)) {
				for ( var subcontractor in subcontractors) {
					// 循环设置分包商选中状态
					var sid = subcontractors[subcontractor].id;
					var aNum = subcontractors[subcontractor].attachs.length;
					$('tr#' + subName + '_' + sid).find("span.showAttach")
							.html(aNum);
					$('input[name=select4' + sid + ']').prop("checked", true);
					$('input[name=select4' + sid + ']').attr('choose', 'true');
				}
			}
			var trades = data.trades;
			if (!isNullOrUndefined(trades)) {
				for ( var trade in trades) {
					// 循环设置专业选中状态
					$('input[name=trade4' + trades[trade] + ']').prop(
							"checked", true);
					$('input[name=trade4' + trades[trade] + ']').attr('choose',
							'true');
				}
			}
			var levels = data.levels;
			if (!isNullOrUndefined(levels)) {
				for ( var level in levels) {
					// 循环设置资质选中状态
					$('input[name=level4' + levels[level] + ']').prop(
							"checked", true);
					$('input[name=level4' + levels[level] + ']').attr('choose',
							'true');
				}
			}
		}
	}, "json");
}
function submit() {
	var num = $("div#selectedSubCon tr.item").length;
	if (num > 0) {
		showProgress();
		$.post(rootFolder + '/project/sendSubProject?t=' + Math.random(), {},
				function(data) {
					hideProgress();
					if (data.isPost) {
						// 发送成功
						window.location.href = rootFolder + '/project/index?t='
								+ Math.random();
					} else {
						// 发送失败
						showTip(data.msg);
					}
				}, "json");
	}
}
function doDouble(n){
	return n<10?'0'+n:n;
}
$(function() {
	if ($.browser.mozilla) {
		$("div#selectedSubCon table span.plusAttach").css("right","45px");
		$("div#selectedSubCon table span.deleteItem").css("margin-right","0px")
	}else{
		$("div#selectedSubCon table span.plusAttach").css("right","68px");
		$("div#selectedSubCon table span.deleteItem").css("margin-right","20px")
	}
	if (window.name != "windowRefresh") {
		location.reload();
		window.name = "windowRefresh";
	} else {
		window.name = "";
	}
	var h = $(window).height() - 505;
	$('div.project div#allSubCon div.autoScroll').css("height", h);
	var t = $.cookie("tradeClass");
	var l = $.cookie("leveClass");
	$(" div.projectInquire div#allSubCon table .td5 span").addClass(t);
	$(" div.projectInquire div#allSubCon table .td6 span").addClass(l);
	$('div.projectInquire div#line').css("bottom", "20px");
	$(".ui_timepicker").datetimepicker(
			{
				showOn : 'both',
				showSecond : true,
				dateFormat : 'yy-mm-dd',
				timeFormat : 'hh:mm:ss',
				stepHour : 1,
				stepMinute : 1,
				stepSecond : 1,
				beforeShow : function(input, dp_inst) {
					// 这个方法无法阻止日期选择框显示，所以这里只用于隐藏验证提示
					$('input#endTime').validationEngine("hide");
				},
				onClose : function(dateText, dp_inst) {
					var event=window.event || arguments.callee.caller.arguments[0];
					if(event.textContent=='Done'){
						if(dp_inst.formattedDateTime!=undefined){
							dateText = dp_inst.formattedDateTime;
							$(this).val(dp_inst.formattedDateTime);
						}else{
							var time = $('#ui_tpicker_time_endTime').html();									
							if(time!='00:00:00'){						
								var year = dp_inst.selectedYear;
								var month = doDouble(dp_inst.selectedMonth);
								var day = doDouble(dp_inst.selectedDay);
								var lastday = year+'-'+month+'-'+day+' '+time;
								dateText = lastday;
								$(this).val(lastday);
							}
						}
					}
					if (isNull(dateText)) {
						$('input#endTime').validationEngine("showPrompt",
								"* Datetime cannot be null.", "error",
								"bottomLeft", true);
					} else {
						$('input#endTime').validationEngine("hide");
					}
					
					saveEndTimeRecord(dateText);
				}
			});
	$('#uploadAttach').fileupload(
			{
				url :rootFolder + '/uploadFile?t=' + Math.random(),
				autoUpload : true,
				dataType : 'json',
				// 附加参数
				formData : {
					suffix : '',
					size : 104857600
				},
				// 开始上传的回调函数
				start : function() {
					showProgress();
				},
				// 文件上传完毕的回调函数
				done : function(e, data) {
					hideProgress();
					if (data.result.ret) {
						// 文件上传成功，记录文件路径与供应商之间的关系
						saveSupplierAttachRecord(data.result.fileName,
								data.result.attachPath);
					} else {
						showTip(data.result.msg);
					}
				},
				// 上传进度事件的回调函数
				progressall : function(e, data) {
				}
			});
	$('#inquireForm').validationEngine("attach", {
		promptPosition : "bottomLeft",
		scroll : false
	});
	$("div.popupMenu div#subcontractList div.subcontractListItem")
			.find("span")
			.hover(
					function() {
						$(this).find("span[class='deleteIcon']").show();
					},
					function() {
						$(
								"div.popupMenu div#subcontractList div.subcontractListItem")
								.siblings().find("span[class='deleteIcon']")
								.hide();
					});
	$("div.levelPopup div.choose table.level tr").hover(function() {
		$(this).find("td").eq(2).addClass("dele");
	}, function() {
		$(this).find("td").eq(2).removeClass("dele");
	});
	recoveryInquireStatus('');
});