//表格拖动
function registerTableDragEvent() {
	var dragTD;// 用来存储当前拖动的列
	// 第一步，按下
	$("table#dragTable th").mousedown(function(e) {
		// 记录初始状态
		if (e.offsetX > $(this).innerWidth() - 10) {
			dragTD = $(this);
			dragTD.mouseDown = true;
			dragTD.oldX = e.pageX;
			dragTD.oldWidth = $(this).innerWidth();
		}
	});
	// 第二步，拖动
	$("table#dragTable th")
			.mousemove(
					function(e) {
						// 更改鼠标样式
						if (e.offsetX > $(this).innerWidth() - 10) {
							$(this).css({
								cursor : "col-resize"
							});
						} else {
							$(this).css({
								cursor : "default"
							});
						}
						if (isNullOrUndefined(dragTD)) {
							dragTD = $(this);
						}
						if (!isNullOrUndefined(dragTD.mouseDown)
								&& dragTD.mouseDown == true) {
							$(this).css({
								cursor : "col-resize"
							});
							var difference = e.pageX - dragTD.oldX;
							var newWidth = dragTD.oldWidth + difference;
							// 此处，做了可调整的大小范围限制，这里的限制与样式保持统一
							if (newWidth > 60 && newWidth <= 500) {
								$(dragTD).width(newWidth);
								// 关联调整树形表格
								var c = $(dragTD).attr("class");
								$("table#treeTable td." + c).width(newWidth);
								if (c == 'td3') {
									$("table#treeTable tr.item")
											.each(
													function() {
														var depth = $(this)
																.attr("depth");
														var w = newWidth
																- 20
																* parseInt(depth)
																- 5;
														if (w > 0) {
															$(
																	$(
																			$(
																					this)
																					.find(
																							"td.treeControl")[0])
																			.find(
																					"div")[0])
																	.width(w);
														}
													});
								}
							}
						}
					});
	// 第三步，释放
	$("table#dragTable th").mouseup(function(e) {
		// 还原鼠标样式
		if (isNullOrUndefined(dragTD)) {
			dragTD = $(this);
		}
		dragTD.mouseDown = false;
		$(dragTD).css({
			cursor : "default"
		});
	});
}
function initQuoteBillItem() {
	var h = $(window).height();
	var d = $.cookie("desClass");
	$("table#dragTable tr .td3 span").addClass(d);
	if ($.browser.mozilla) {
		$("#menu-container").height(h - 300);
		$("#bigMenuContainer").height(h - 106);
		$('div#quotePage div.quoteAutoScroll').css("height", h - 270);
	} else {
		$("#bigMenuContainer").height(h - 86);
		$('div#quotePage div.quoteAutoScroll').css("height", h - 255);
	}
	var option = {
		theme : 'vsStyle',
		expandLevel : 99,
		column : 1
	};
	// 保存URL中加密部分
	/*
	 * var url=window.location.href; var
	 * q_w_url=url.substring(url.lastIndexOf('/') + 1); var
	 * key="q_w_td_"+q_w_url; var keys="q_w_div_"+q_w_url;
	 */
	$('#treeTable').treeTable(option);
	beautifyTreeTable();
	/* adjustTableWidth(key); */
	registerTipEvent();
	registerDoubleClickEditEvent();
	registerEditEvent();
	registerTableDragEvent();
	totalName();
	/* registerTableReductionWidthEvent(); */
	/* findByKeywords($("#keyword").val()); */
}

/* 汇总名称 */
function totalName() {
	var selected = $("div#menu-container div.selected").get(0);
	var id = $(selected).attr("id");
	if (id.startWith('root')) {
		$("div#quotePage div.summary b").html("Final Summary");
	} else if (id.startWith('branch')) {
		$("div#quotePage div.summary b").html("Collection Summary");
	} else if (id.startWith('leaf')) {
		$("div#quotePage div.summary b").html("Collection");

	}
}
/** *********************************导出逻辑************************************** */
function exportExcel() {
	showProgress();
	var sheet = '';
	var selected = $("div#menu-container div.selected").get(0);
	var id = $(selected).attr("id");
	if (id.startWith('root')) {
		// 分包工程
		var span = $(selected).find("span:last")[0];
		sheet = $(span).attr("title");
	} else if (id.startWith('branch')) {
		// 章
		var pid = $(selected).attr("pid");
		var subpro = $("div#menu-container").find("div#" + pid)[0];
		var span = $(subpro).find("span:last")[0];
		var subproName = $(span).attr("title");
		span = $(selected).find("span:last")[0];
		sheet = subproName + '~' + $(span).attr("title");
	} else if (id.startWith('leaf')) {
		// 节
		var pid = $(selected).attr("pid");
		var sid = $(selected).attr("sid");
		var subpro = $("div#menu-container").find("div#" + sid)[0];
		var span = $(subpro).find("span:last")[0];
		var subproName = $(span).attr("title");
		var ele = $("div#menu-container").find("div#" + pid)[0];
		span = $(ele).find("span:last")[0];
		var eleName = $(span).attr("title");
		span = $(selected).find("span:last")[0];
		sheet = subproName + '~' + eleName + '~' + $(span).attr("title");
	}
	var jsontree = "[]";
	var tree = eval('(' + jsontree + ')');
	var num = $("div#menu-container div.treeMenu").length;
	for (var i = 0; i < num; i++) {
		var treeMenu = $("div#menu-container div.treeMenu").get(i);
		var span = $(treeMenu).find("span:last")[0];
		tree.push($(span).attr("title"));
	}
	var jsontable = "[]";
	var table = eval('(' + jsontable + ')');
	var tr = $("div#quotePage table.head tr").get(0);
	var sn = $(tr).find(".sn")[0];
	var td3 = $(tr).find(".td3")[0];
	var td4 = $(tr).find(".td4")[0];
	var td5 = $(tr).find(".td5")[0];
	var td6 = $(tr).find(".td6")[0];
	var td7 = $(tr).find(".td7")[0];
	var td8 = $(tr).find(".td8")[0];
	var td9 = $(tr).find(".td9")[0];
	var td10 = $(tr).find(".td10")[0];
	var arr = {
		"sn" : $(sn).text(),
		"description" : $(td3).text(),
		"trade" : $(td4).text(),
		"unit" : $(td5).text(),
		"type" : $(td6).text(),
		"qty" : $(td7).text(),
		"netRate" : $(td8).text(),
		"netAmount" : $(td9).text(),
		"remarks" : $(td10).text()
	}
	table.push(arr);
	num = $("table#treeTable tr").length;
	for (var i = 0; i < num; i++) {
		tr = $("table#treeTable tr").get(i);
		sn = $(tr).find(".sn")[0];
		td3 = $(tr).find(".td3")[0];
		td4 = $(tr).find(".td4")[0];
		td5 = $(tr).find(".td5")[0];
		td6 = $(tr).find(".td6")[0];
		td7 = $(tr).find(".td7")[0];
		td8 = $(tr).find(".td8")[0];
		td9 = $(tr).find(".td9")[0];
		td10 = $(tr).find(".td10")[0];
		var data = {
			"sn" : $(sn).text(),
			"description" : $(td3).text(),
			"trade" : $(td4).text(),
			"unit" : $(td5).text(),
			"type" : $(td6).text(),
			"qty" : $($(td7).find("input")[0]).val(),
			"netRate" : $($(td8).find("input")[0]).val(),
			"netAmount" : $($(td9).find("input")[0]).val(),
			"remarks" : $($(td10).find("input")[0]).val()
		}
		table.push(data);
	}
	var summaryTip = $($('div.summary b')[0]).text();
	var amount = $($('span#totalAmount')[0]).text();
	$.post(rootFolder+'/project/quote/export4tbq?t=' + Math.random(), {
		treeData : encodeURIComponent(JSON.stringify(tree)),
		tableData : encodeURIComponent(JSON.stringify(table)),
		summary : encodeURIComponent(summaryTip),
		totalAmount : encodeURIComponent(amount),
		sheetName : encodeURIComponent(sheet)
	}, function(data) {
		hideProgress();
		if (data.ret == 0) {
			// 生成excel成功
			window.location.href = rootFolder+'/Reports/' + data.exportReportName;
		} else {
			// 生成excel失败
			showTip(data.msg);
		}
	}, "json");
}
/** *********************************描述过滤逻辑************************************** */
// description弹出框
function showDescriptPopup() {
	$("div.descriptPopup").show();
	$("div.descriptPopup").focus();
}
function searchDescript(btn) {
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var quote_descript = $($(div).find("input")[0]).val();
	if (!isNull(quote_descript)) {
		$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon")
				.addClass("quoteFilterIcon_succ");
		var desClass = $("table#dragTable tr .td3 span").attr("class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
		var paramName = 'quote_descript';
		var paramValue = quote_descript;
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.descriptPopup').hide();
		});
	}
}

function filterDescript() {
	var sb = new StringBuilder();
	var trs = $('table.descript tbody').find('tr');
	var d = 0;
	for (var i = 0; i < trs.length; i++) {
		var td = $(trs[i]).find('td')[0];
		if ($($(td).find('input')[0]).prop('checked')) {
			var t = $($(trs[i]).find('td')[1]).text();
			sb.append(t);
			sb.append(",");
			d++;
		}
	}
	if (d == 0) {
		$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon_succ")
				.addClass("quoteFilterIcon");
		var desClass = $("table#dragTable tr .td3 span").attr("class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
	} else {
		$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon")
				.addClass("quoteFilterIcon_succ");
		var desClass = $("table#dragTable tr .td3 span").attr("class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
	}
	var quote_descript = $("input#descriptKey").val();
	if (!isNull(quote_descript)) {
		sb.append(quote_descript);
		$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon")
				.addClass("quoteFilterIcon_succ");
		var desClass = $("table#dragTable tr .td3 span").attr("class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
	}
	var paramName = 'quote_descript';
	var paramValue = sb.toString();
	saveInSession(paramName, paramValue, function() {
		window.location.reload();
	}, function() {
		// TODO 保存错误时候的处理
		$('div.descriptPopup').hide();
	});
}
function cancelDescript() {
	$("table#dragTable tr .td3 span").removeClass("quoteFilterIcon_succ")
			.addClass("quoteFilterIcon");
	var desClass = $("table#dragTable tr .td3 span").attr("class");
	$.cookie('desClass', desClass, {
		expires : 7,
		path : '/'
	})
	var paramName = 'quote_descript';
	removeFromSession(paramName, function() {
		window.location.reload();
	}, function() {
		// TODO 删除错误时候的处理
		$('div.descriptPopup').hide();
	});
}
function hideDescriptPopup() {
	setTimeout('doHideDescriptPopup();', 400);
}
function doHideDescriptPopup() {
	var isFocus = $("div.descriptPopup div.searchBox input").is(":focus")
			|| $('div.descriptPopup').is(":focus");
	if (isFocus == true) {
		return;
	} else {
		$('div.descriptPopup').hide();
	}
}

/** *********************************下载框逻辑************************************** */
function showDownloadDialog() {
	art.dialog({
		content : document.getElementById('downLoadBox'),
		fixed : true,
		id : 'downLoadBox',
		title : 'DownLoad',
		resize : false,
	});
}
/** *********************************提交报价逻辑************************************** */
function submitQuote() {
	art.dialog({
		title : 'Tips',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : 'Sure to submit the quote?',
		okVal : 'OK',
		ok : function() {
			var nsubProjectID = getSelectedSubProjectID();
			$.get(rootFolder+'/project/submitQuote4tbq2subproject?t='
					+ Math.random(), {
				'subProjectID' : encodeURIComponent(nsubProjectID)
			}, function(data) {
				if (data.isPost == 0) {
					window.location.reload();
				} else {
					showTip(data.msg);
				}
			}, "json");
			return true;
		},
		cancelVal : 'Cancel',
		cancel : true
	});
}
/** *********************************结束编辑状态触发的联动逻辑************************************** */
// 备注编辑完成
function remarkEditFinished(src) {
	var sRemark = $(src).attr('remark');
	var oTr = $(src).parents("tr")[0];
	var priceTypeMark = getPriceTypeMark(oTr);
	var nId = $(oTr).attr('id');
	var nDataType = 2;
	var sValue = $(src).val();

	/*
	 * if (!isNull(priceTypeMark) && !isNull(sValue)) { if
	 * (sValue.endWith(priceTypeMark)) { // 用指定标识结尾，那么要去除指定标识防止标识累加 sValue =
	 * sValue.substring(0, sValue.length - priceTypeMark.length); } }
	 */
	var sId = getSelectedSubProjectID();
	$.get(rootFolder+'/project/updatebillitem4tbq2quote?t=' + Math.random(), {
		'subprojectid' : encodeURIComponent(sId),
		'billitemid' : encodeURIComponent(nId),
		'datatype' : encodeURIComponent(nDataType),
		'data' : encodeURIComponent(sValue)
	}, function(data) {
		if (data.isPost == 0) {
			// var uv = priceTypeMark+sRemark+sValue;
			var uv = sValue;
			updateValue($(src), uv, uv, false, true);
		} else {
			var uv = $(src).attr("originalValue")
			updateValue($(src), uv, uv, false, false);
			showTip(data.msg);
		}
	}, "json");
}
// 合价编辑完成
function netAmountEditFinished(src) {
	var oTr = $(src).parents("tr")[0];
	var nId = $(oTr).attr('id');
	var nDataType = 1;
	var sValue = $(src).val();
	var sId = getSelectedSubProjectID();
	$.get(rootFolder+'/project/updatebillitem4tbq2quote?t=' + Math.random(), {
		'subprojectid' : encodeURIComponent(sId),
		'billitemid' : encodeURIComponent(nId),
		'datatype' : encodeURIComponent(nDataType),
		'data' : encodeURIComponent(sValue)
	}, function(data) {
		if (data.isPost == 0) {
			var uv = sValue;
			updateValue($(src), uv, uv, true, true);
			// 成功才执行联动
			adjustNetAmount(src);
		} else {
			var uv = $(src).attr("originalValue");
			updateValue($(src), uv, uv, true, false);
			showTip(data.msg);
		}
	}, "json");
}
// 单价编辑完成
function netRateEditFinished(src) {
	var oTr = $(src).parents("tr")[0];
	var nId = $(oTr).attr('id');
	var nDataType = 0;
	var sValue = $(src).val();
	var sId = getSelectedSubProjectID();
	$.get(rootFolder+'/project/updatebillitem4tbq2quote?t=' + Math.random(), {
		'subprojectid' : encodeURIComponent(sId),
		'billitemid' : encodeURIComponent(nId),
		'datatype' : encodeURIComponent(nDataType),
		'data' : encodeURIComponent(sValue)
	}, function(data) {
		if (data.isPost == 0) {
			var uv = sValue;
			updateValue($(src), uv, uv, true, true);
			// 成功才执行联动
			adjustNetRate(src);
		} else {
			var uv = $(src).attr("originalValue");
			updateValue($(src), uv, uv, true, false);
			showTip(data.msg);
		}
	}, "json");
}
// 编辑完成，按照编辑框类型执行数值调整与联动计算逻辑
function finishEdit(src) {
	// 按照编辑框类别区分计算逻辑,name取值只可能为netRate、netAmount、remark三类，对于remark不需要联动，其他两类存在联动
	var name = trim($(src).attr('name'));
	if (equals(name, 'netRate')) {
		if (!validate(src, true)) {
			return;
		}
		// 单价编辑的联动
		netRateEditFinished(src);
	} else if (equals(name, 'netAmount')) {
		if (!validate(src, true)) {
			return;
		}
		// 合价编辑的联动
		netAmountEditFinished(src);
	} else if (equals(name, 'remark')) {
		if (!validate(src, false)) {
			return;
		}
		// 备注编辑的联动
		remarkEditFinished(src);
	}
}
/** ************************************************计算逻辑**************************************************** */
// 更新合价统计值
function refreshTotalAmount() {
	var dAmount = 0;
	$("#treeTable tr.item").each(function() {
		var depth = $(this).attr('depth');
		if (depth == '1') {
			$(this).find('input[name="netAmount"]').each(function() {
				dAmount = calculate(dAmount, '+', $(this).val());
				return false;
			});
		}
	});
	$("#totalAmount").text(formatNum(dAmount));
}
// 更新父级的合价
function updateParentNodeNetAmount(pId) {
	var pTr = $("tr[id='" + pId + "']").eq(0);
	while (pId != '0') {
		var sparentAmount = '';
		// 求所有的子集的合价之和
		$("tr[pId='" + pId + "']").each(function() {
			$(this).find('input[name="netAmount"]').each(function() {
				sparentAmount = calculate(sparentAmount, '+', $(this).val());
				return false;
			});
		});
		$(pTr).find('input[name="netAmount"]').each(function() {
			var uv = sparentAmount;
			updateValue($(this), uv, uv, true, true);
			return false;
		});
		pId = $(pTr).attr('pId');
		pTr = $("tr[id='" + pId + "']").eq(0);
	}
}
// 合价的联动调整
function adjustNetAmount(src) {
	var oTr = $(src).parents("tr")[0];
	var pId = $(oTr).attr("pId");
	updateParentNodeNetAmount(pId);
	refreshTotalAmount();
}
// 单价的联动调整
function adjustNetRate(src) {
	var oTr = $(src).parents("tr")[0];
	var type = getType(oTr);
	if (type == 'Bill Item') {
		// 清单，仅仅需要根据单价和工程量，然后调整合价
		var qty = getQty(oTr);
		var sRate = $(src).val();
		var sAmount = calculate(qty, '*', sRate);
		// 调整合价
		$(oTr).find('input[name="netAmount"]').each(function() {
			var uv = sAmount;
			updateValue($(this), uv, uv, true, true);
			adjustNetAmount($(this));
			return false;
		});
	} else if (type == 'Heading') {
		// 标题
	} else if (type == 'Note') {
		// 说明项
	} else if (type == 'Rate Item') {
		// 单价子项，需要向上累积清单的单价，然后调整合价
		// 更新父清单节点的单价
		var pId = $(oTr).attr('pId');
		var sparentRate = '';
		$("tr[pId='" + pId + "']").each(function() {
			$(this).find('input[name="netRate"]').each(function() {
				sparentRate = calculate(sparentRate, '+', $(this).val());
				return false;
			});
		});
		var pTr = $("tr[id='" + pId + "']").eq(0);
		var qty = getQty(pTr);
		var sparentAmount = calculate(sparentRate, '*', qty);
		$(pTr).find('input[name="netRate"]').each(function() {
			var uv = sparentRate;
			updateValue($(this), uv, uv, true, true);
			return false;
		});
		$(pTr).find('input[name="netAmount"]').each(function() {
			var uv = sparentAmount;
			updateValue($(this), uv, uv, true, true);
			adjustNetAmount($(this));
			return false;
		});
	}
}