function createProject() {
	if ($("div#inquireProject tr.edit").length > 0) {
		return;
	}
	$("div#inquireProject span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div#inquireProject span#createProject").removeAttr("status");
	$("div#inquireProject tr.item").each(function(i) {
		$(this).removeClass("border");
	});
	var num = $("div#inquireProject tr.item").length;
	num++;
	var sb = new StringBuilder();
	sb.append('<tr class="item edit border" onclick="selectOneRow(this);">');
	sb
			.append('<td class="sn unedit"><input type="text" value="'
					+ num
					+ '" onfocus="this.blur();"/><input type="hidden" value="" /></td>');
	sb
			.append('<td class="td3 edit" onclick="selectOneRows(this);"><input type="text" id="'
					+ num
					+ '" value="" onkeypress="keyPress(event,this);" onblur="submitModify(this);" class="validate[required,maxSize[255],custom[specialCharacter]]" data-errormessage-value-missing="* Project names cannot be null."/></td>');
	sb
			.append('<td class="td4 unedit"><input type="text" value="" onfocus="this.blur();"/></td>');
	sb
			.append('<td class="td5 unedit"><input type="text" value="" onfocus="this.blur();"/></td>');
	/*
	 * sb .append('<td class="td6 unedit"><input type="text" value=""
	 * onfocus="this.blur();"/></td>');
	 */
	sb.append('</tr>');
	var trFirst=$("div#inquireProject table.data tbody tr")[0];
	if(isNullOrUndefined(trFirst)){
		$("div#inquireProject table.data tbody").append(sb.toString());
	}else{
		$(sb.toString()).insertBefore($(trFirst));
	}
}
function editProject() {
	if ($("div#inquireProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#inquireProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	$("div#inquireProject span.menuItem").each(function(i) {
		$(this).attr("status", "invalid");
	});
	$("div#inquireProject span#editProject").removeAttr("status");
	$(tr).addClass('edit');
	$(tr).find("input").each(function(i) {
		$(this).removeAttr("readonly");
	});
}
function deleteInquireProject() {
	if ($("div#inquireProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#inquireProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	art.dialog({
		title : 'Hint',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : 'Sure to delete the selected project?',
		okVal : 'OK',
		ok : function() {
			doDeleteInquireProject(id);
			return true;
		},
		cancelVal : 'Cancel',
		cancel : true
	});
}
function doDeleteInquireProject(id) {
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	$.get(rootFolder+'/project/deleteInquireProject/' + projectID + '?t='
			+ Math.random(), {

	}, function(data) {
		if (data.isPost) {
			window.location.reload();
		} else {
			showTip('Delete project fail.');
		}
	}, "json");
}
function record4InquireProject() {
	if ($("div#inquireProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#inquireProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	window.location.href = rootFolder+'/project/inquireRecord/' + projectID
			+ "?t=" + Math.random();
}
function send4InquireProject() {
	if ($("div#inquireProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#inquireProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	window.location.href = rootFolder+'/project/inquire/' + projectID + "?t="
			+ Math.random();
}
function inquire(obj) {
	var tr = $(obj);
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	$.get(rootFolder+'/project/doInquire?t=' + Math.random(), {
		queryProjectID : encodeURIComponent(projectID)
	}, function(data) {
		if (data.isPost == 0) {
			window.location.href = data.link;
		} else {
			showTip(data.msg);
		}
	}, "json");
}
function evaluation(obj, update) {
	var tr = $(obj);
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	if (update) {
		$.get(rootFolder+'/project/updateInquireProjectStatus2Evaluating?t='
				+ Math.random(), {
			queryProjectID : encodeURIComponent(projectID)
		}, function(data) {
			if (data.isPost == 0) {
				window.location.href = rootFolder+'/project/evaluation/'
						+ projectID + "?t=" + Math.random();
			} else {
				showTip(data.msg);
			}
		}, "json");
	} else {
		window.location.href = rootFolder+'/project/evaluation/' + projectID
				+ "?t=" + Math.random();
	}
}
// 判断名称是否存在
function isNameExisted() {
	var names = new Array();
	$("#projectForm tr.item").each(function() {
		var name = $($($(this).find("td.td3")[0]).find('input')[0]).val();
		names.push(name);
	});
	var l1 = names.length;
	var l2 = names.uniquelize().length;
	return l1 != l2;
}
function keyPress(event, obj) {
	if (event.keyCode == 13) {
		// 回车键作为快捷方式提交
		submitModify(obj);
	}
}
function submitModify(obj) {
	if ($(obj).parents("tr.edit").length == 0) {
		return;
	}
	var tr = $(obj).parents("tr.edit")[0];
	var queryProjectID = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (queryProjectID == undefined) {
		return;
	}
	if ($(obj).validationEngine("validate")) {
		// 这里有点奇怪，非法的返回为true
		return;
	} else {
		// 前端加一层重复性判断
		if (isNameExisted()) {
			$(obj).validationEngine("showPrompt",
					"* The project name already exists.", "error",
					"bottomLeft", true);
			return;
		}
		var queryProjectName = $($($(tr).find("td.td3")[0]).find("input")[0])
				.val();
		if (isNull(queryProjectID)) {
			// 添加工程
			$.post(rootFolder+'/project/create?t=' + Math.random(), {
				name : encodeURIComponent(queryProjectName)
			}, function(data) {
				if (data.isPost == res_ok) {
					loadProject("inquireProject",
							rootFolder+'/project/inquireList?t=' + Math.random());
				} else if (data.isPost == res_error) {
					// 出错提示
					showTip(data.msg);
				} else {
					// 重复留给前端提示
					$(obj).validationEngine("showPrompt", "* " + data.msg,
							"error", "bottomLeft", true);
				}
			}, "json");
		} else {
			// 编辑工程
			var projectID = strEnc(queryProjectID, firstKey, secondKey,
					thirdKey);
			$.post(rootFolder+'/project/edit?t=' + Math.random(), {
				id : encodeURIComponent(projectID),
				name : encodeURIComponent(queryProjectName)
			}, function(data) {
				if (data.isPost == res_ok) {
					loadProject("inquireProject",
							rootFolder+'/project/inquireList?t=' + Math.random());
				} else if (data.isPost == res_error) {
					// 出错提示
					showTip(data.msg);
				} else {
					// 重复留给前端提示
					$(obj).validationEngine("showPrompt", "* " + data.msg,
							"error", "bottomLeft", true);
				}
			}, "json");
		}
	}
}
$(function() {
	changeMenuItemStatus();
});