/*提示切换功能*/
var n = 1;
function switchTip() {
	if (n % 2 == 0) {
		$("div.projectEvaluation div.control span.bt7").text(
				"Double click to edit");
		$("div.projectEvaluation div.control span").eq(3).addClass("bt7")
				.removeClass("bt7Active");
		$("div#evaluationPage div.doubleTip p")
				.text(
						'Click "Double click to edit"  to switch to "Double click to adopt"');
		$("div.projectEvaluation table tr td input").off("dblclick");
		registerAdoptEvent();
	} else {
		$("div.projectEvaluation div.control span").eq(3).addClass("bt7Active");
		$("div.projectEvaluation div.control span.bt7").text(
				"Double click to adopt");
		$("div#evaluationPage div.doubleTip p")
				.text(
						'Click "Double click to adopt"  to switch to "Double click to edit"');
		$("div.projectEvaluation table tr td input").off("dblclick");
		registerDoubleClickEditEvent();
	}
	n++;
}
function hideTip() {
	$("div.doubleTip").hide();
}
function displaySetting() {
	goTop();
	$('#evalDisplaySettingDialog').addClass('popupAnimate').show().css(
			'height', $(document).height());
	var h = $(window).height() - 400;
	$('div#evalDisplaySettingDialog div.autoScroll').css("height", h);
}
/* 设置显示列 */
function selectTdName(btn) {
	var button = $(btn);
	var id = button.attr("for");
	var tdc = $('input#' + id).parent().parent().find("td").eq(1).attr("class");
	var parent = $('input#' + id).parent().parent().find("td");
	var tdName = $('input#' + id).parent().parent().find("td." + tdc).attr(
			"class");
	var s = 1;
	if ($('input#' + id).prop("checked")) {
		$(parent).css("color", "#0074ba");
		$("table#dragTable tr").each(
				function() {
					if (tdName == "td3" || tdName == "td5" || tdName == "td6") {
						if ($(this).attr("class") == "tr1") {
							var n = $(this).find("td").eq(0);
							var b = $(n).attr("colspan");
							$(n).attr("colspan", parseInt(b) + s);
							var a = $(n).attr("colspan");
							$(n).attr("colspan", a);
						}
						$(this).find("td." + tdName).show();
						$("table#treeTable td." + tdName).show();
					}
					if (tdName == "s") {
						if ($(this).attr("class") == "tr1") {
							var td1 = $(this).find("td").eq(1);
							var td1c = $(td1).attr("colspan");
							$(td1).attr("colspan", parseInt(td1c) + s);
							var nn = $(td1).attr("colspan");
							$(td1).attr("colspan", nn);
							$(this).find("td").eq(2).attr("colspan", nn);
							var mark = $(this).find("td[name='mark']").attr(
									"colspan");
							$(this).find("td[name='mark']").attr("colspan",
									parseInt(mark) + s);
						} else {
							var tds = $(this).find("td[name='discount']").attr(
									"colspan");
							$(this).find("td[name='discount']").attr("colspan",
									parseInt(tds) + s);
						}
						$(this).find("td." + tdName).show();
						$("table#treeTable td." + tdName).show();
					}
					if (tdName == "remark") {
						if ($(this).attr("class") == "tr1") {
							var td4 = $(this).find("td").eq(1);
							var td4c = $(td4).attr("colspan");
							$(td4).attr("colspan", parseInt(td4c) + s);
							var ss = $(td4).attr("colspan");
							$(td4).attr("colspan", ss);
							$(this).find("td").eq(2).attr("colspan", ss);
							var remark = $(this).find("td.hiddenPrice").attr(
									"colspan");
							$(this).find("td.hiddenPrice").attr("colspan",
									parseInt(remark) + s);
						} else {
							var dis = $(this).find("td.disnumber").attr(
									"colspan");
							$(this).find("td.disnumber").attr("colspan",
									parseInt(dis) + s);
						}
						$(this).find("td." + tdName).show();
						$("table#treeTable td." + tdName).show();
					}
				})
		$("table#bottomTable tr").each(function() {
			if (tdName == "td3" || tdName == "td5" || tdName == "td6") {
				if ($(this).attr("class") == "bb") {
					var col = $(this).find("td").eq(1);
					var c = $(col).attr("colspan");
					$(col).attr("colspan", parseInt(c) + s);
					var nn = $(col).attr("colspan");
					$(col).attr("colspan", nn);
				} else {
					var col = $(this).find("td").eq(0);
					var c = $(col).attr("colspan");
					$(col).attr("colspan", parseInt(c) + s);
					var nn = $(col).attr("colspan");
					$(col).attr("colspan", nn);
				}
			}
			$(this).find("td." + tdName).show();
		})

	} else {
		$(parent).css("color", "#999999");
		$("table#dragTable tr").each(
				function() {
					if (tdName == "td3" || tdName == "td5" || tdName == "td6") {
						if ($(this).attr("class") == "tr1") {
							var n = $(this).find("td").eq(0);
							var b = $(n).attr("colspan"); /* 获得第一个td的属性 */
							$(n).attr("colspan", b - s); /* 设置所跨的列 */
							var a = $(n).attr("colspan");/* 记录所跨的列数 */
							$(n).attr("colspan", a); /* 重新设置所跨的列数 */
						}
						$(this).find("td." + tdName).hide();
						$("table#treeTable td." + tdName).hide();
					}
					if (tdName == "s") {
						if ($(this).attr("class") == "tr1") {
							var td1 = $(this).find("td").eq(1);
							var td1c = $(td1).attr("colspan");
							$(td1).attr("colspan", td1c - s);
							var nn = $(td1).attr("colspan");
							$(td1).attr("colspan", nn);
							$(this).find("td").eq(2).attr("colspan", nn);
							var mark = $(this).find("td[name='mark']").attr(
									"colspan");
							$(this).find("td[name='mark']").attr("colspan",
									mark - s);
						} else {
							var tds = $(this).find("td[name='discount']").attr(
									"colspan");
							$(this).find("td[name='discount']").attr("colspan",
									tds - s);
						}
						$(this).find("td." + tdName).hide();
						$("table#treeTable td." + tdName).hide();
					}
					if (tdName == "remark") {
						if ($(this).attr("class") == "tr1") {
							var td4 = $(this).find("td").eq(1);
							var td4c = $(td4).attr("colspan");
							$(td4).attr("colspan", td4c - s);
							var ss = $(td4).attr("colspan");
							$(td4).attr("colspan", ss);
							$(this).find("td").eq(2).attr("colspan", ss);
							var remark = $(this).find("td.hiddenPrice").attr(
									"colspan");
							$(this).find("td.hiddenPrice").attr("colspan",
									remark - s);
						} else {
							var dis = $(this).find("td.disnumber").attr(
									"colspan");
							$(this).find("td.disnumber").attr("colspan",
									dis - s);
						}
						$(this).find("td." + tdName).hide();
						$("table#treeTable td." + tdName).hide();
					}
				})
		$("table#bottomTable tr").each(function() {
			if (tdName == "td3" || tdName == "td5" || tdName == "td6") {
				if ($(this).attr("class") == "bb") {
					var col = $(this).find("td").eq(1);
					var c = $(col).attr("colspan");
					$(col).attr("colspan", c - s);
					var nn = $(col).attr("colspan");
					$(col).attr("colspan", nn);
				} else {
					var col = $(this).find("td").eq(0);
					var c = $(col).attr("colspan");
					$(col).attr("colspan", c - s);
					var nn = $(col).attr("colspan");
					$(col).attr("colspan", nn);
				}
			}
			$(this).find("td." + tdName).hide();
		});
	}
}
function hideDisplay() {
	selectTdName();
	$("div#evalDisplaySettingDialog").hide();
}

// 评标表格拖动
function registerTableDragEvent() {
	var dragTD;// 用来存储当前拖动的列
	// 第一步，按下
	$("table#dragTable tr.tr4 td").mousedown(function(e) {
		// 记录初始状态
		if (e.offsetX > $(this).innerWidth() - 10) {
			dragTD = $(this);
			dragTD.mouseDown = true;
			dragTD.oldX = e.pageX;// 鼠标落下去的坐标
			dragTD.oldWidth = $(this).innerWidth();
		}
	});
	// 第二步，拖动
	$("table#dragTable tr.tr4 td")
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
							if (newWidth > 30 && newWidth <= 500) {
								$(dragTD).width(newWidth);
								// 关联调整树形表格
								var c = $(dragTD).attr("class");
								$("table#dragTable td." + c).width(newWidth);
								$("table#treeTable td." + c).width(newWidth);
								$("table#bottomTable td." + c).width(newWidth);

								if (c == 'td2') {
									$("table#treeTable tr.item")
											.each(
													function(index) {
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
																	.css(
																			"width",
																			w);

														}
													});
								}
							}
						}

					});
	$("table#dragTable tr.tr4 td").mouseup(function(e) {
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
// 向右移动逻辑
function showSub() {
	var scrollBarScroll = $('div.content').scrollLeft();
	var content = $('div.content').scrollLeft(scrollBarScroll + 245);
	$('div.content').scrollLeft(scrollBarScroll + 245);
}
/* 汇总名称变化 */
function totalName() {
	var selected = $("div#menu-container div.selected").get(0);
	var id = $(selected).attr("id");
	if (id.startWith('root')) {
		$("div#evaluationPage div.summary tr.bb td.collection").html(
				"Final Summary");
	} else if (id.startWith('branch')) {
		$("div#evaluationPage div.summary tr.bb td.collection").html(
				"Collection Summary");
	} else if (id.startWith('leaf')) {
		$("div#evaluationPage div.summary tr.bb td.collection").html(
				"Collection");

	}
}
// description弹出框
function showDescriptPopup() {
	$("div.descriptPopup").show();
	$("div.descriptPopup").focus();
}
function searchDescript(btn) {
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var evaluation_descript = $($(div).find("input")[0]).val();
	removeFromSession('evaluation_changed', function() {
		if (!isNull(evaluation_descript)) {
			var paramName = 'evaluation_descript';
			var paramValue = evaluation_descript;
			$("table#dragTable tr.tr4 td.td2 span").eq(1).removeClass(
					"evalFilterIcon").addClass("evalFilterIcon_succ");
			var desClass = $("table#dragTable tr.tr4 td.td2 span").eq(1).attr(
					"class");
			$.cookie('desClass', desClass, {
				expires : 7,
				path : '/'
			})
			saveInSession(paramName, paramValue, function() {
				window.location.reload();
			}, function() {
				// TODO 保存错误时候的处理
				$('div.descriptPopup').hide();
			});
		}
	}, function() {
		// TODO 删除错误时候的处理
		$('div.descriptPopup').hide();
	});
}
function filterDescript() {
	var sb = new StringBuilder();
	var trs = $('table.descript tbody').find('tr');
	var d = false;
	for (var i = 0; i < trs.length; i++) {
		var td = $(trs[i]).find('td')[0];// 只要输入过滤图标不切换
		if ($($(td).find('input')[0]).prop('checked')) {
			var t = $($(trs[i]).find('td')[1]).text();
			sb.append(t);
			sb.append(",");
			d = true;
		}
	}
	if (!d) {
		$("table#dragTable tr.tr4 td.td2 span").eq(1).removeClass(
				"evalFilterIcon_succ").addClass("evalFilterIcon");
		var desClass = $("table#dragTable tr.tr4 td.td2 span").eq(1).attr(
				"class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})
	} else {
		$("table#dragTable tr.tr4 td.td2 span").eq(1).removeClass(
				"evalFilterIcon").addClass("evalFilterIcon_succ");
		var desClass = $("table#dragTable tr.tr4 td.td2 span").eq(1).attr(
				"class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})

	}
	var evaluation_descript = $("input#descriptKey").val();
	if (!isNull(evaluation_descript)) {
		sb.append(evaluation_descript);
		$("table#dragTable tr.tr4 td.td2 span").eq(1).removeClass(
				"evalFilterIcon").addClass("evalFilterIcon_succ");
		var desClass = $("table#dragTable tr.tr4 td.td2 span").eq(1).attr(
				"class");
		$.cookie('desClass', desClass, {
			expires : 7,
			path : '/'
		})

	}
	removeFromSession('evaluation_changed', function() {
		var paramName = 'evaluation_descript';
		var paramValue = sb.toString();
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.descriptPopup').hide();

		});
	}, function() {
		// TODO 删除错误时候的处理
		$('div.descriptPopup').hide();
	});

}
function cancelDescript() {
	var paramName = 'evaluation_descript&evaluation_changed';
	$("table#dragTable tr.tr4 td.td2 span").eq(1).removeClass(
			"evalFilterIcon_succ").addClass("evalFilterIcon");
	var desClass = $("table#dragTable tr.tr4 td.td2 span").eq(1).attr("class");
	$.cookie('desClass', desClass, {
		expires : 7,
		path : '/'
	})
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
// trade弹出框
function showTradePopup() {
	$("div.tradePopup").show();
	$("div.tradePopup").focus();
}
function searchTrade(btn) {
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var evaluation_trade = $($(div).find("input")[0]).val();
	removeFromSession('evaluation_changed', function() {
		if (!isNull(evaluation_trade)) {
			var paramName = 'evaluation_trade';
			var paramValue = evaluation_trade;
			saveInSession(paramName, paramValue, function() {
				window.location.reload();
			}, function() {
				// TODO 保存错误时候的处理
				$('div.tradePopup').hide();
			});
		}
	}, function() {
		// TODO 删除错误时候的处理
		$('div.tradePopup').hide();
	});
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
		} else {
			t--;
		}
		if (t == 0) {
			$(
					"div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
					.removeClass("tradePopuIcon_succ")
					.addClass("tradePopuIcon");
			var tradeClass = $(
					"div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
					.attr("class");
			$.cookie('tradeClass', tradeClass, {
				expires : 7,
				path : '/'
			})
		} else {
			$(
					"div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
					.removeClass("tradePopuIcon")
					.addClass("tradePopuIcon_succ");
			var tradeClass = $(
					"div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
					.attr("class");
			$.cookie('tradeClass', tradeClass, {
				expires : 7,
				path : '/'
			})

		}
	}
	var evaluation_trade = $("input#tradeKey").val();
	if (!isNull(evaluation_trade)) {
		sb.append(evaluation_trade);
	}
	removeFromSession('evaluation_changed', function() {
		var paramName = 'evaluation_trade';
		var paramValue = sb.toString();
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.tradePopup').hide();
		});
	}, function() {
		// TODO 删除错误时候的处理
		$('div.tradePopup').hide();
	});
}
function cancelTrade() {
	$("div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
			.removeClass("tradePopuIcon_succ").addClass("tradePopuIcon");
	var tradeClass = $(
			"div.projectEvaluation div.content table#dragTable tr.tr4 td.td3 span")
			.attr("class");
	$.cookie('tradeClass', tradeClass, {
		expires : 7,
		path : '/'
	})
	var paramName = 'evaluation_trade&evaluation_changed';
	removeFromSession(paramName, function() {
		window.location.reload();
	}, function() {
		// TODO 删除错误时候的处理
		$('div.tradePopup').hide();
	});
}
function hideTradePopup() {
	setTimeout('doHideTradePopup();', 400);
}
function doHideTradePopup() {
	var isFocus = $("div.tradePopup div.searchBox input").is(":focus")
			|| $('div.tradePopup').is(":focus");
	if (isFocus == true) {
		return;
	} else {
		$('div.tradePopup').hide();
	}
}
// unit弹出框
function showUnitPopup() {
	$("div.content div.unitPopup").show();
	$("div.content div.unitPopup").focus();
}
function searchUnit(btn) {
	var button = $(btn);
	var div = $(btn).parents("div.searchBox")[0];
	var evaluation_unit = $($(div).find("input")[0]).val();
	removeFromSession('evaluation_changed', function() {
		if (!isNull(evaluation_unit)) {
			var paramName = 'evaluation_unit';
			var paramValue = evaluation_unit;
			saveInSession(paramName, paramValue, function() {
				window.location.reload();
			}, function() {
				// TODO 保存错误时候的处理
				$('div.unitPopup').hide();
			});
		}
	}, function() {
		// TODO 删除错误时候的处理
		$('div.unitPopup').hide();
	});
}

function filterUnit() {
	var sb = new StringBuilder();
	var trs = $('table.unit tbody').find('tr');
	var t = 0;
	for (var i = 0; i < trs.length; i++) {
		var td = $(trs[i]).find('td')[0];
		if ($($(td).find('input')[0]).prop('checked')) {
			var t = $($(trs[i]).find('td')[1]).text();
			sb.append(t);
			sb.append(",");
			t++;
		} else {
			t--;
		}
	}
	if (t == 0) {
		$("table#dragTable tr.tr4 td.td5 span")
				.removeClass("unitPopuIcon_succ").addClass("unitPopuIcon");
		var unitClass = $("table#dragTable tr.tr4 td.td5 span").attr("class");
		$.cookie('unitClass', unitClass, {
			expires : 7,
			path : '/'
		})
	} else {
		$("table#dragTable tr.tr4 td.td5 span").removeClass("unitPopuIcon")
				.addClass("unitPopuIcon_succ");
		var unitClass = $("table#dragTable tr.tr4 td.td5 span").attr("class");
		$.cookie('unitClass', unitClass, {
			expires : 7,
			path : '/'
		})

	}
	var evaluation_unit = $("input#unitKey").val();
	if (!isNull(evaluation_unit)) {
		sb.append(evaluation_unit);
	}
	removeFromSession('evaluation_changed', function() {
		var paramName = 'evaluation_unit';
		var paramValue = sb.toString();
		saveInSession(paramName, paramValue, function() {
			window.location.reload();
		}, function() {
			// TODO 保存错误时候的处理
			$('div.unitPopup').hide();
		});
	}, function() {
		// TODO 删除错误时候的处理
		$('div.unitPopup').hide();
	});
}
function cancelUnit() {
	$("table#dragTable tr.tr4 td.td5 span").removeClass("unitPopuIcon_succ")
			.addClass("unitPopuIcon");
	var unitClass = $("table#dragTable tr.tr4 td.td5 span").attr("class");
	$.cookie('unitClass', unitClass, {
		expires : 7,
		path : '/'
	})
	var paramName = 'evaluation_unit&evaluation_changed';
	removeFromSession(paramName, function() {
		window.location.reload();
	}, function() {
		// TODO 删除错误时候的处理
		$('div.unitPopup').hide();
	});
}
function hideUnitPopup() {
	setTimeout('doHideUnitPopup();', 400);
}
function doHideUnitPopup() {
	var isFocus = $("div.unitPopup div.searchBox input").is(":focus")
			|| $('div.unitPopup').is(":focus");
	if (isFocus == true) {
		return;
	} else {
		$('div.unitPopup').hide();
	}
}
// 显示修改项逻辑
function showChangedItem(obj) {
	var c = $(obj).prop("class");
	if (!contain(c, 'bt4Active')) {
		// 激活显示修改项
		removeFromSession(
				'evaluation_descript&evaluation_trade&evaluation_unit',
				function() {
					var paramName = 'evaluation_changed';
					var paramValue = '1';
					saveInSession(paramName, paramValue, function() {
						window.location.reload();
					}, function() {

					});
				}, function() {

				});
	} else {
		// 取消显示修改项
		var paramName = 'evaluation_descript&evaluation_trade&evaluation_unit&evaluation_changed';
		removeFromSession(paramName, function() {
			window.location.reload();
		}, function() {

		});
	}
}
/** *********************************导出逻辑************************************** */
function getCellValue(td) {
	var l = $(td).find("input").size();
	if (l > 0) {
		return $($(td).find("input")[0]).val();
	} else {
		return $(td).text();
	}
}
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
	// 左侧树形列表
	var jsontree = "[]";
	var tree = eval('(' + jsontree + ')');
	var num = $("div#menu-container div.treeMenu").length;
	for (var i = 0; i < num; i++) {
		var treeMenu = $("div#menu-container div.treeMenu").get(i);
		var span = $(treeMenu).find("span:last")[0];
		tree.push($(span).attr("title"));
	}
	// 表哥头部
	var tr1 = $("div#evaluationPage table.head tr.tr1").get(0);
	var tr2 = $("div#evaluationPage table.head tr.tr2").get(0);
	var tr3 = $("div#evaluationPage table.head tr.tr3").get(0);
	var adoptedTip = $($(tr1).find("td")[1]).text();
	var pteTip = $($(tr1).find("td")[2]).text();
	var jsonSupplier = "[]";
	var supplier = eval('(' + jsonSupplier + ')');
	var l = $(tr1).find("td").size();
	if (l > 3) {
		// 存在供应商
		var data = {};
		for (var i = 3; i < l; i++) {
			var v = $($(tr1).find("td")[i]).text();
			data[i - 3] = v;
		}
		supplier.push(data);
	}
	l = $(tr2).find("td").size();
	if (l > 0) {
		// 存在供应商
		var data = {};
		for (var i = 0; i < l; i++) {
			if (i % 2 == 0) {
				var v = $($(tr2).find("td")[i]).text();
				data[i] = v;
			} else {
				var v = $($($(tr2).find("td")[i]).find("input")[0]).val();
				data[i] = v;
			}
		}
		supplier.push(data);
	}
	l = $(tr3).find("td").size();
	if (l > 0) {
		// 存在供应商
		var data = {};
		for (var i = 0; i < l; i++) {
			if (i % 2 == 0) {
				var v = $($(tr3).find("td")[i]).text();
				data[i] = v;
			} else {
				var v = $($($(tr3).find("td")[i]).find("input")[0]).val();
				data[i] = v;
			}
		}
		supplier.push(data);
	}
	// 数据表
	var jsontable = "[]";
	var table = eval('(' + jsontable + ')');
	var tr4 = $("div#evaluationPage table.head tr.tr4").get(0);
	l = $(tr4).find("td").size();
	if (l > 0) {
		var data = {};
		for (var i = 0; i < l - 1; i++) {
			var v = $($(tr4).find("td")[i]).text();
			data[i] = v;
		}
		table.push(data);
	}
	var num = $("table#treeTable tr").length;
	for (var i = 1; i < num; i++) {
		var tr = $("table#treeTable tr").get(i);
		l = $(tr).find("td").size();
		var data = {};
		for (var j = 0; j < l - 1; j++) {
			var td = $(tr).find("td")[j];
			data[j] = getCellValue(td);
		}
		table.push(data);
	}
	// 分析统计表
	var jsonstatistic = "[]";
	var statistic = eval('(' + jsonstatistic + ')');
	num = $("table.bottom tr").length;
	for (var i = 1; i < num; i++) {
		var tr = $("table.bottom tr").get(i);
		l = $(tr).find("td").size();
		var data = {};
		if (i == 1) {
			for (var j = 1; j < l; j++) {
				var td = $(tr).find("td")[j];
				data[j - 1] = getCellValue(td);
			}
		} else {
			for (var j = 0; j < l; j++) {
				var td = $(tr).find("td")[j];
				data[j] = getCellValue(td);
			}
		}
		statistic.push(data);
	}
	$.post(rootFolder+'/project/evaluation/export4tbq?t=' + Math.random(), {
		treeData : encodeURIComponent(JSON.stringify(tree)),
		supplierData : encodeURIComponent(JSON.stringify(supplier)),
		tableData : encodeURIComponent(JSON.stringify(table)),
		statisticData : encodeURIComponent(JSON.stringify(statistic)),
		adopted : encodeURIComponent(adoptedTip),
		pte : encodeURIComponent(pteTip),
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
function initEvaluationBillItem() {

	$('div#evaluationPage div.autoScroll')
			.css("width", $('table.head').width());
	var h = $(window).height();
	var d = $.cookie("desClass");
	var t = $.cookie("tradeClass");
	var u = $.cookie("unitClass");
	$("table#dragTable tr.tr4 td.td2 span").eq(1).addClass(d);
	$("table#dragTable tr.tr4 td.td3 span").addClass(t);
	$("table#dragTable tr.tr4 td.td5 span").addClass(u);
	$('div#evalDisplaySettingDialog div.autoScroll table td.dis input').attr(
			"choose", "true");
	$('div#evalDisplaySettingDialog div.autoScroll table td').css("color",
			"#0074ba");
	$('div#evalDisplaySettingDialog div.autoScroll table td.dis input').prop(
			"checked", "checked");
	if ($.browser.mozilla) {
		// 火狐浏览器判断
		$("#bigMenuContainer").height(h - 100);
		$('div#evaluationPage div.content').css("height", h - 138);
		$('div#evaluationPage div.autoScroll').css("height", h - 308);
	} else {
		// 谷歌浏览器判断
		$("#menu-container").height(h - 86);
		$('div#evaluationPage div.content').css("height", h - 135);
		$('div#evaluationPage div.autoScroll').css("height", h - 275);
	}
	// 分析面板折叠
	$("div.summary a span").toggle(function() {
		$('div.projectEvaluation div.summary tr.trHidden').show();
		if ($.browser.mozilla) {
			$('div#evaluationPage div.autoScroll').css("height", h - 524);
		} else {
			$('div#evaluationPage div.autoScroll').css("height", h - 520);
		}
		$("div.summary span").removeClass("open").addClass("close");
	}, function() {
		$('div.projectEvaluation div.summary tr.trHidden').hide();
		if ($.browser.mozilla) {
			$('div#evaluationPage div.autoScroll').css("height", h - 308);
		} else {
			$('div#evaluationPage div.autoScroll').css("height", h - 275);
		}
		$("div.summary span").addClass("open").removeClass("close");
	});
	var option = {
		theme : 'vsStyle',
		expandLevel : 99,
		column : 1
	};
	/*
	 * var url = window.location.href; var e_w_url =
	 * url.substring(url.lastIndexOf('/') + 1); var key = "e_w_td_" + e_w_url;
	 * var keys = "e_w_div_" + e_w_url;
	 */
	$('#treeTable').treeTable(option);
	beautifyTreeTable();
	/* selectTdName(); */
	/* adjustTableWidth(); */
	/* registerTableReductionWidthEvent(); */
	totalName();
	registerTipEvent();
	registerEditEvent();
	registerAdoptEvent();
	registerTableDragEvent();
	statistic();
	compare();
	adjustAdoptedMark();
	analysis();
}
// 获取供应商对应单价
function getNetRate(tr, supplierID) {
	return $(
			$(tr)
					.find(
							"input[name='netRate'][supplierID='" + supplierID
									+ "']")[0]).val();
}
// 获取供应商对应合价
function getNetAmount(tr, supplierID) {
	return $(
			$(tr).find(
					"input[name='netAmount'][supplierID='" + supplierID + "']")[0])
			.val();
}
// 获取供应商对应备注
function getRemark(tr, supplierID) {
	return $(
			$(tr).find("input[name='remark'][supplierID='" + supplierID + "']")[0])
			.val();
}
// 判断分包商价格是否发生改动
function isChanged(tr, supplierID) {
	var netRate = $(tr).find(
			"input[name='netRate'][supplierID='" + supplierID + "']")[0];
	var netAmount = $(tr).find(
			"input[name='netAmount'][supplierID='" + supplierID + "']")[0];
	if (($(netRate).attr("originalvalue") != $(netRate).attr("setvalue"))
			|| ($(netAmount).attr("originalvalue") != $(netAmount).attr(
					"setvalue"))) {
		return true;
	}
	return false;
}
// 更新变化状态
function updateChange(obj) {
	var oTr = $(obj).parents("tr")[0];
	var nId = $(oTr).attr('id');
	var netRates = $(oTr).find("input[name='netRate']");
	for (var i = 0; i < netRates.length; i++) {
		var c = $(netRates[i]).prop("class");
		if (contain(c, 'edit')) {
			// 属于可编辑框
			if ($(netRates[i]).attr("originalvalue") != $(netRates[i]).attr(
					"setvalue")) {
				// 价格经过修改
				updateChangeStatus(nId, 1);
				return;
			}
		}
	}
	var netAmounts = $(oTr).find("input[name='netAmount']");
	for (var i = 0; i < netAmounts.length; i++) {
		var c = $(netAmounts[i]).prop("class");
		if (contain(c, 'edit')) {
			// 属于可编辑框
			if ($(netAmounts[i]).attr("originalvalue") != $(netAmounts[i])
					.attr("setvalue")) {
				// 价格经过修改
				updateChangeStatus(nId, 1);
				return;
			}
		}
	}
	updateChangeStatus(nId, 0);
}
function updateChangeStatus(id, change) {
	$.get(rootFolder+'/project/updateChangeStatus?t=' + Math.random(), {
		'billitemid' : encodeURIComponent(id),
		'isChange' : encodeURIComponent(change)
	}, function(data) {

	}, "json");
}
// 判断对应清单是否都已经报价
function isAllPriced(tr) {
	var bIsAllPriced = true;
	var pricetype = getPriceType(tr);
	if (pricetype == OnlyRate) {
		$(tr).find("input[name='netRate']").each(function() {
			if ($(this).attr('supplierID') != "-1") {
				if (isNull($(this).val())) {
					bIsAllPriced = false;
					return false;
				}
			}
		});
	} else if (pricetype == FixAmount) {

	} else if (pricetype == FixRate) {

	} else if (pricetype == LumpSumItem) {

	} else if (pricetype == OnlyAmount) {
		$(tr).find("input[name='netAmount']").each(function() {
			if ($(this).attr('supplierID') != "-1") {
				if (isNull($(this).val())) {
					bIsAllPriced = false;
					return false;
				}
			}
		});
	} else {
		$(tr).find("input[name='netRate']").each(function() {
			if ($(this).attr('supplierID') != "-1") {
				if (isNull($(this).val())) {
					bIsAllPriced = false;
					return false;
				}
			}
		});
	}
	return bIsAllPriced;
}
// 获取清单总价报价最大值
function getMaxAmount(tr) {
	var maxAmount = '';
	$(tr).find("input[name='netAmount']").each(function() {
		var supplierID = $(this).attr('supplierID');
		if (supplierID != "-1") {
			var amount = $(this).val();
			if (!isNull(amount)) {
				var m = parseFloat(transfer2Num(amount));
				if (isNull(maxAmount)) {
					maxAmount = m;
				} else {
					if (m > maxAmount) {
						maxAmount = m;
					}
				}

			}
		}
	});
	return maxAmount;
}
// 获取清单总价报价最小值
function getMinAmount(tr) {
	var minAmount = '';
	$(tr).find("input[name='netAmount']").each(function() {
		var supplierID = $(this).attr('supplierID');
		if (supplierID != "-1") {
			var amount = $(this).val();
			if (!isNull(amount)) {
				var m = parseFloat(transfer2Num(amount));
				if (isNull(minAmount)) {
					minAmount = m;
				} else {
					if (m < minAmount) {
						minAmount = m;
					}
				}
			}
		}
	});
	return minAmount;
}
/** *********************************双击触发的联动逻辑************************************** */
// 采纳完成，按照清单是否带子项执行数值调整与联动计算逻辑
function finishAdopt(src) {
	var supplierID = $(src).attr('supplierID');
	var oTr = $(src).parents("tr")[0];
	var numPricetype = src.parent('tr').attr('pricetype');	
	var billItemID = $(oTr).attr('id');
	var netRate = getNetRate(oTr, supplierID);
	var netAmount = getNetAmount(oTr, supplierID);
	var remark = getRemark(oTr, supplierID);
	var mark = 'PTE';
	if (supplierID != '0') {
		mark = getSupplierMark(supplierID);
		if (isChanged(oTr, supplierID)) {
			mark = mark + '#';
		}
	}
	var adoptRate = $(oTr).find("input[name='netRate'][supplierID='-1']")[0];
	var adoptAmount = $(oTr).find("input[name='netAmount'][supplierID='-1']")[0];
	var adoptRemark = $(oTr).find("input[name='remark'][supplierID='-1']")[0];
	var adoptUserMark = $(oTr).find(
			"input[name='adoptedUserMark'][supplierID='-1']")[0];
	if (hasChild(oTr)) {
		// 父级只带过去备注和标识
		$.get(rootFolder+'/project/adoptbillitem4tbq2evaluation?t='
				+ Math.random(), {
			'supplierID' : encodeURIComponent(supplierID),
			'billitemid' : encodeURIComponent(billItemID),
			'adoptNetRate' : '',
			'adoptNetAmount' : '',
			'adoptRemark' : encodeURIComponent(remark)
		}, function(data) {
			if (data.isPost == 0) {
				updateValue($(adoptRemark), remark, remark, false, true);
				updateValue($(adoptUserMark), mark, mark, false, false);
			} else {
				showTip(data.msg);
			}
		}, "json");
	} else {
		// 子项带过去数值，备注和标识
		$.get(rootFolder+'/project/adoptbillitem4tbq2evaluation?t='
				+ Math.random(), {
			'supplierID' : encodeURIComponent(supplierID),
			'billitemid' : encodeURIComponent(billItemID),
			'adoptNetRate' : encodeURIComponent(netRate),
			'adoptNetAmount' : encodeURIComponent(netAmount),
			'adoptRemark' : encodeURIComponent(remark)
		}, function(data) {
			if (data.isPost == 0) {
				updateValue($(adoptRate), netRate, netRate, true, true);
				updateValue($(adoptAmount), netAmount, netAmount, true, true);
				updateValue($(adoptRemark), remark, remark, false, true);
				updateValue($(adoptUserMark), mark, mark, false, false);
				if (!isNull(netRate)) {
					// 子项带过去的单价不为空，要触发采纳列的单价联动					
					if(numPricetype==3 || numPricetype==4){
						adjustNetRateFF($(adoptRate));
					}else{
						adjustNetRate($(adoptRate));
					}					
				} else if (!isNull(netAmount)) {
					// 否则，再判断子项带过去的合价不为空，要触发采纳列的合价联动					
					adjustNetAmount($(adoptAmount));
				} else {
					// 都为空，代表子项数据值被清空，要触发采纳列的单价联动
					adjustNetRate($(adoptRate));
				}
				// 采纳引发数值变化需要联动差异分析调整
				var differenceMap = {};
				differenceMap['0'] = '';
				$("td[name='mark']").each(function(i) {
					var supplierID = $(this).attr('supplierID');
					differenceMap[supplierID] = '';
				});
				statisticDifference(differenceMap);
			} else {
				showTip(data.msg);
			}
		}, "json");
	}
}
/** *********************************结束编辑状态触发的联动逻辑************************************** */
// 备注编辑完成
function remarkEditFinished(src) {
	var sSupplyID = $(src).attr('supplierID');
	var oTr = $(src).parents("tr")[0];
	var priceTypeMark = getPriceTypeMark(oTr);
	var nId = $(oTr).attr('id');
	var nDataType = 2;
	var sValue = $(src).val();
	/*if (!isNull(priceTypeMark) && !isNull(sValue)) {
		if (sValue.endWith(priceTypeMark)) {
			// 用指定标识结尾，那么要去除指定标识防止标识累加
			sValue = sValue.substring(0, sValue.length - priceTypeMark.length);
		}
	}*/
	$.get(rootFolder+'/project/updatebillitem4tbq2evaluation?t=' + Math.random(),
			{
				'supplierID' : encodeURIComponent(sSupplyID),
				'billitemid' : encodeURIComponent(nId),
				'datatype' : encodeURIComponent(nDataType),
				'data' : encodeURIComponent(sValue)
			}, function(data) {
				if (data.isPost == 0) {
					var uv = sValue;
					updateValue($(src), uv, uv, false, true);
				} else {
					var uv = $(src).attr("originalValue");
					updateValue($(src), uv, uv, false, false);
					showTip(data.msg);
				}
			}, "json");
}
// 合价编辑完成
function netAmountEditFinished(src) {
	var supplierID = $(src).attr('supplierID');
	var oTr = $(src).parents("tr")[0];
	var nId = $(oTr).attr('id');
	var nDataType = 1;
	var sValue = $(src).val();
	var discountPercent = getSupplierDiscountPercent(supplierID);
	$
			.get(
					rootFolder+'/project/updatebillitem4tbq2evaluation?t='
							+ Math.random(),
					{
						'supplierID' : encodeURIComponent(supplierID),
						'billitemid' : encodeURIComponent(nId),
						'datatype' : encodeURIComponent(nDataType),
						'data' : encodeURIComponent(sValue)
					},
					function(data) {
						if (data.isPost == 0) {
							var ov = sValue;
							var uv = ov;
							if (supplierID != '-1' && supplierID != '0'
									&& !isNull(discountPercent)) {
								uv = calculate(ov, '*', discountPercent);
							}
							updateValue($(src), uv, ov, true, true);
							updateChange($(src));
							// 成功才执行联动
							adjustNetAmount(src);
							if (supplierID == '-1') {
								// 修改S列标识为UD
								var adoptUserMark = $(oTr)
										.find(
												"input[name='adoptedUserMark'][supplierID='-1']")[0];
								updateValue($(adoptUserMark), 'UD', 'UD',
										false, false);
								// 触发差异分析调整
								var differenceMap = {};
								differenceMap['0'] = '';
								$("td[name='mark']").each(
										function(i) {
											var supplierID = $(this).attr(
													'supplierID');
											differenceMap[supplierID] = '';
										});
								statisticDifference(differenceMap);
							} else {
								if (supplierID != '0'
										&& !isNull(discountPercent)) {
									// 折扣绝对值的联动调整
									var oDiscountPercent = $("input[name='discountPercent'][supplierID='"
											+ supplierID + "']")[0];
									adjustDiscountPercent($(oDiscountPercent));
								}
								// 处理行最高值与最低值之间的联动调整
								compare();
								// 重新分析
								analysis();
							}
						} else {
							var ov = $(src).attr("originalValue");
							var uv = ov;
							if (supplierID != '-1' && supplierID != '0'
									&& !isNull(discountPercent)) {
								uv = calculate(ov, '*', discountPercent);
							}
							updateValue($(src), uv, ov, true, false);
							showTip(data.msg);
						}
					}, "json");
}
// 单价编辑完成
function netRateEditFinished(src) {
	var supplierID = $(src).attr('supplierID');
	var oTr = $(src).parents("tr")[0];
	var nId = $(oTr).attr('id');
	var nDataType = 0;
	var sValue = $(src).val();
	var discountPercent = getSupplierDiscountPercent(supplierID);
	$
			.get(
					rootFolder+'/project/updatebillitem4tbq2evaluation?t='
							+ Math.random(),
					{
						'supplierID' : encodeURIComponent(supplierID),
						'billitemid' : encodeURIComponent(nId),
						'datatype' : encodeURIComponent(nDataType),
						'data' : encodeURIComponent(sValue)
					},
					function(data) {
						if (data.isPost == 0) {
							var ov = sValue;
							var uv = ov;
							if (supplierID != '-1' && supplierID != '0'
									&& !isNull(discountPercent)) {
								uv = calculate(ov, '*', discountPercent);
							}
							updateValue($(src), uv, ov, true, true);
							updateChange($(src));
							// 成功才执行联动
							adjustNetRate(src);
							if (supplierID == '-1') {
								// 修改S列标识为UD
								var adoptUserMark = $(oTr)
										.find(
												"input[name='adoptedUserMark'][supplierID='-1']")[0];
								updateValue($(adoptUserMark), 'UD', 'UD',
										false, false);
								// 触发差异分析调整
								var differenceMap = {};
								differenceMap['0'] = '';
								$("td[name='mark']").each(
										function(i) {
											var supplierID = $(this).attr(
													'supplierID');
											differenceMap[supplierID] = '';
										});
								statisticDifference(differenceMap);
							} else {
								if (supplierID != '0'
										&& !isNull(discountPercent)) {
									// 折扣绝对值的联动调整
									var oDiscountPercent = $("input[name='discountPercent'][supplierID='"
											+ supplierID + "']")[0];
									adjustDiscountPercent($(oDiscountPercent));
								}
								// 处理行最高值与最低值之间的联动调整
								compare();
								// 重新分析
								analysis();
							}
						} else {
							var ov = $(src).attr("originalValue");
							var uv = ov;
							if (supplierID != '-1' && supplierID != '0'
									&& !isNull(discountPercent)) {
								uv = calculate(ov, '*', discountPercent);
							}
							updateValue($(src), uv, ov, true, false);
							showTip(data.msg);
						}
					}, "json");
}
// 折扣百分比编辑完成
function discountPercentEditFinished(src) {
	var supplierID = $(src).attr('supplierID');
	var nDataType = 1;
	var sValue = $(src).val();
	var sId = getSelectedSubProjectID();
	$.get(rootFolder+'/project/updatediscount4tbq2evaluation?t=' + Math.random(),
			{
				'supplierID' : encodeURIComponent(supplierID),
				'subProjectID' : encodeURIComponent(sId),
				'datatype' : encodeURIComponent(nDataType),
				'data' : encodeURIComponent(sValue)
			}, function(data) {
				if (data.isPost == 0) {
					var uv = sValue;
					updateValue($(src), uv, uv, true, true);
					// 成功才执行联动
					adjustDiscountPercent(src);
					// 处理行最高值与最低值之间的比较联动调整
					compare();
					// 重新分析
					analysis();
				} else {
					var ov = $(src).attr("originalValue");
					updateValue($(src), ov, ov, true, false);
					showTip(data.msg);
				}
			}, "json");
}
// 折扣绝对值编辑完成
function discountAmountEditFinished(src) {
	var supplierID = $(src).attr('supplierID');
	var nDataType = 0;
	var sValue = $(src).val();
	var sId = getSelectedSubProjectID();
	$.get(rootFolder+'/project/updatediscount4tbq2evaluation?t=' + Math.random(),
			{
				'supplierID' : encodeURIComponent(supplierID),
				'subProjectID' : encodeURIComponent(sId),
				'datatype' : encodeURIComponent(nDataType),
				'data' : encodeURIComponent(sValue)
			}, function(data) {
				if (data.isPost == 0) {
					var uv = sValue;
					updateValue($(src), uv, uv, true, true);
					// 成功才执行联动
					adjustDiscountAmount(src);
					// 处理行最高值与最低值之间的比较联动调整
					compare();
					// 重新分析
					analysis();
				} else {
					var ov = $(src).attr("originalValue");
					updateValue($(src), ov, ov, true, false);
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
	} else if (equals(name, 'discountPercent')) {
		if (!validate(src, true)) {
			return;
		}
		// 折扣百分比编辑的联动
		discountPercentEditFinished(src);
	} else if (equals(name, 'discountAmount')) {
		if (!validate(src, true)) {
			return;
		}
		// 折扣绝对值编辑的联动
		discountAmountEditFinished(src);
	}
}
/** ************************************************计算逻辑**************************************************** */
// 获取分包商折扣百分比
function getSupplierDiscountPercent(supplierID) {
	var discountPercent = $(
			"input[name='discountPercent'][supplierID='" + supplierID + "']")
			.val();
	if (isNull(discountPercent)) {
		return "";
	} else {
		discountPercent = calculate(calculate(100, '-', discountPercent), '/',
				100);
		return discountPercent;
	}
}
// 获取调整后的供应商指定清单的合价累积值
function getAdjustAmount4SupplierByID(id, supplierID) {
	var adjustAmount = '';
	$("#treeTable tr[pId='" + id + "']").each(function() {
		$(this).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				adjustAmount = calculate(adjustAmount, '+', $(this).val());
				return false;
			}
		});
	});
	return adjustAmount;
}
// 获取调整后的供应商指定清单的单价累积值
function getAdjustRate4SupplierByID(id, supplierID) {
	var adjustRate = '';
	$("#treeTable tr[pId='" + id + "']").each(function() {
		$(this).find('input[name="netRate"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				adjustRate = calculate(adjustRate, '+', $(this).val());
				return false;
			}
		});
	});
	return adjustRate;
}
// 获取原始的供应商指定清单的合价累积值
function getOriginalAmount4SupplierByID(id, supplierID) {
	var originalAmount = '';
	$("#treeTable tr[pId='" + id + "']").each(
			function() {
				$(this).find('input[name="netAmount"]').each(
						function() {
							if ($(this).attr('supplierID') == supplierID) {
								originalAmount = calculate(originalAmount, '+',
										$(this).attr('originalValue'));
								return false;
							}
						});
			});
	return originalAmount;
}
// 获取原始的供应商指定清单的单价累积值
function getOriginalRate4SupplierByID(id, supplierID) {
	var originalRate = '';
	$("#treeTable tr[pId='" + id + "']").each(
			function() {
				$(this).find('input[name="netRate"]').each(
						function() {
							if ($(this).attr('supplierID') == supplierID) {
								originalRate = calculate(originalRate, '+', $(
										this).attr('originalValue'));
								return false;
							}
						});
			});
	return originalRate;
}
// 获取分包商原始总价
function getTotalOriginalValue(supplierID) {
	var dAmount = '';
	$("#treeTable tr.item").each(
			function() {
				var depth = $(this).attr('depth');
				if (depth == '1') {
					$(this).find('input[name="netAmount"]').each(
							function() {
								if ($(this).attr('supplierID') == supplierID) {
									dAmount = calculate(dAmount, '+', $(this)
											.attr('originalValue'));
									return false;
								}
							});
				}
			});
	$("td.hiddenPrice").each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			dAmount = calculate(dAmount, '+', $(this).attr('hiddenPrice'));
			return false;
		}
	});
	return dAmount;
}
// 获取分包商原始总价中的固定值
function getTotalFixValue(supplierID) {
	var dAmount = '';
	$("td.hiddenPrice").each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			dAmount = $(this).attr('fixAmount');
			return false;
		}
	});
	return dAmount;
}
// 更新合价统计值
function refreshTotalAmount(supplierID) {
	var adjustAmount = '';
	var originalAmount = '';
	$("#treeTable tr.item")
			.each(
					function() {
						var depth = $(this).attr('depth');
						if (depth == '1') {
							$(this)
									.find('input[name="netAmount"]')
									.each(
											function() {
												if ($(this).attr('supplierID') == supplierID) {
													adjustAmount = calculate(
															adjustAmount, '+',
															$(this).val());
													originalAmount = calculate(
															originalAmount,
															'+',
															$(this)
																	.attr(
																			'originalValue'));
													return false;
												}
											});
						}
					});
	$("input[name='collection']").each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			updateValue($(this), adjustAmount, originalAmount, true, true);
			return false;
		}
	});
}
// 更新父级的合价
function updateParentNodeNetAmount(pId, supplierID) {
	var pTr = $("tr[id='" + pId + "']").eq(0);
	while (pId != '0') {
		var adjustAmount = getAdjustAmount4SupplierByID(pId, supplierID);
		var originalAmount = getOriginalAmount4SupplierByID(pId, supplierID);
		$(pTr).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustAmount, originalAmount, true, true);
				return false;
			}
		});
		pId = $(pTr).attr('pId');
		pTr = $("tr[id='" + pId + "']").eq(0);
	}
}
// 合价的联动调整
function adjustNetAmount(src) {	
	var oTr = $(src).parents("tr")[0];
	var pId = $(oTr).attr("pId");
	var supplierID = $(src).attr('supplierID');
	updateParentNodeNetAmount(pId, supplierID);
	refreshTotalAmount(supplierID);
}
//单价联动加入FA,FR
function adjustNetRateFF(src) {
	var oTr = $(src).parents("tr")[0];
	var supplierID = $(src).attr('supplierID');
	var type = getType(oTr);
	if (type == 'Bill Item') {		
		// 清单，仅仅需要根据单价和工程量，调整合价，接着调整合价联动
		var qty = getQty(oTr);
		var adjustRate = $(src).val();
		var originalRate = $(src).attr('originalValue');
		var adjustAmount = calculate(qty, '*', adjustRate);
		var originalAmount = calculate(qty, '*', originalRate);
		// 调整合价
		$(oTr).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {				
				adjustNetAmount($(this));
				return false;
			}
		});
	} else if (type == 'Heading') {
		// 标题
	} else if (type == 'Note') {
		// 说明项
	} else if (type == 'Rate Item') {		
		// 单价子项，需要向上累积清单的单价，然后调整合价，接着调整合价联动
		// 更新父清单节点的单价
		var pId = $(oTr).attr('pId');
		var pTr = $("tr[id='" + pId + "']").eq(0);
		var qty = getQty(pTr);
		var adjustRate = getAdjustRate4SupplierByID(pId, supplierID);
		var originalRate = getOriginalRate4SupplierByID(pId, supplierID);
		var adjustAmount = calculate(adjustRate, '*', qty);
		var originalAmount = calculate(originalRate, '*', qty);
		$(pTr).find('input[name="netRate"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustRate, originalRate, true, true);
				return false;
			}
		});
		$(pTr).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustAmount, originalAmount, true, true);
				adjustNetAmount($(this));
				return false;
			}
		});
	}
}
// 单价的联动调整
function adjustNetRate(src) {
	var oTr = $(src).parents("tr")[0];
	var supplierID = $(src).attr('supplierID');
	var type = getType(oTr);
	if (type == 'Bill Item') {		
		// 清单，仅仅需要根据单价和工程量，调整合价，接着调整合价联动
		var qty = getQty(oTr);
		var adjustRate = $(src).val();
		var originalRate = $(src).attr('originalValue');
		var adjustAmount = calculate(qty, '*', adjustRate);
		var originalAmount = calculate(qty, '*', originalRate);
		// 调整合价
		$(oTr).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustAmount, originalAmount, true, true);
				adjustNetAmount($(this));
				return false;
			}
		});
	} else if (type == 'Heading') {
		// 标题
	} else if (type == 'Note') {
		// 说明项
	} else if (type == 'Rate Item') {		
		// 单价子项，需要向上累积清单的单价，然后调整合价，接着调整合价联动
		// 更新父清单节点的单价
		var pId = $(oTr).attr('pId');
		var pTr = $("tr[id='" + pId + "']").eq(0);
		var qty = getQty(pTr);
		var adjustRate = getAdjustRate4SupplierByID(pId, supplierID);
		var originalRate = getOriginalRate4SupplierByID(pId, supplierID);
		var adjustAmount = calculate(adjustRate, '*', qty);
		var originalAmount = calculate(originalRate, '*', qty);
		$(pTr).find('input[name="netRate"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustRate, originalRate, true, true);
				return false;
			}
		});
		$(pTr).find('input[name="netAmount"]').each(function() {
			if ($(this).attr('supplierID') == supplierID) {
				updateValue($(this), adjustAmount, originalAmount, true, true);
				adjustNetAmount($(this));
				return false;
			}
		});
	}
}
// 折扣百分比的联动调整
function adjustDiscountPercent(src) {
	var supplierID = $(src).attr('supplierID');
	var nDiscountPercent = $(src).val();
	// 折扣引起的价格变化只用于显示，根据初始值来计算折扣
	var nTotalAmount = getTotalOriginalValue(supplierID);
	var nFixAmount = getTotalFixValue(supplierID);
	var nAdjustAmount = calculate(nTotalAmount, '-', nFixAmount);
	var nDiscountAmount = '';
	var discountPercent = '';
	if (!isNull(nDiscountPercent)) {
		discountPercent = calculate(calculate(100, '-', nDiscountPercent), '/',
				100);
		nDiscountAmount = calculate(nAdjustAmount, '-', calculate(
				nAdjustAmount, '*', discountPercent));
	}
	// 调整折扣百分比引发的折扣绝对值变化
	$("input[name='discountAmount']").each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			updateValue($(this), nDiscountAmount, '', true, false);
			return false;
		}
	});
	// 折扣为空，应该进行还原，不为空进行调整
	if (isNull(nDiscountPercent)) {
		// 折扣为空，还原每行数据
		recoveryPrice(supplierID);
	} else {
		// 折扣不为空，不改变原有真实值的条件下进行值的显示调整
		adjustPriceByDiscountPercent(supplierID, discountPercent);
	}
}
// 折扣绝对值的联动调整
function adjustDiscountAmount(src) {
	var supplierID = $(src).attr('supplierID');
	var nDiscountAmount = $(src).val();
	// 折扣引起的价格变化只用于显示，根据初始值来计算折扣
	var nTotalAmount = getTotalOriginalValue(supplierID);
	var nFixAmount = getTotalFixValue(supplierID);
	var nAdjustAmount = calculate(nTotalAmount, '-', nFixAmount);
	var nDiscountPercent = '';
	var discountPercent = '';
	if (!isNull(nDiscountAmount)) {
		discountPercent = calculate(calculate(nAdjustAmount, '-',
				nDiscountAmount), '/', nAdjustAmount);
		nDiscountPercent = calculate(100, '-', calculate(discountPercent, '*',
				100));
	}
	// 调整折扣绝对值引发的折扣百分比变化
	$("input[name='discountPercent']").each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			updateValue($(this), nDiscountPercent, '', true, false);
			return false;
		}
	});
	// 折扣为空，应该进行还原，不为空进行调整
	if (isNull(nDiscountAmount)) {
		// 折扣为空，还原每行数据
		recoveryPrice(supplierID);
	} else {
		// 折扣不为空，不改变原有真实值的条件下进行值的显示调整
		adjustPriceByDiscountPercent(supplierID, discountPercent);
	}
}
// 还原供应商的价格
function recoveryPrice(supplierID) {
	$("input[name='netRate'][supplierID='" + supplierID + "']").each(
			function() {
				var uv = $(this).attr("originalValue");
				updateValue($(this), uv, uv, true, false);
			});
	$("input[name='netAmount'][supplierID='" + supplierID + "']").each(
			function() {
				var uv = $(this).attr("originalValue");
				updateValue($(this), uv, uv, true, false);
			});
	refreshTotalAmount(supplierID);
}
// 按照百分比，统一调整分包商的各项报价，该调整只用于显示，不能改变各项价格的原始值（originalValue）
function adjustPriceByDiscountPercent(supplierID, discountPercent) {
	// 处理折扣信息，折扣仅仅从最底层的子项开始调整，子项调整完毕，再根据子项逐层梳理，最后调整总价
	// 第一步，修改子项显示值，同时记录父级的行标识，后续根据父级行标识依次梳理
	var pids = new Array();
	$("#treeTable tr.item").each(
			function() {
				if (!hasChild($(this))) {
					var pid = getBillitemPID($(this));
					if (pid != 0) {
						pids.push(pid);
					}
					adjustLeafPriceByDiscountPercent($(this), supplierID,
							discountPercent);
				}
			});
	// 第二步，逐层梳理
	while (pids.length > 0) {
		var ids = pids.uniquelize();
		pids = [];
		ids.each(function(id) {
			// 循环处理父节点
			var oTr = $("#treeTable tr[id='" + id + "']")[0];
			var pid = getBillitemPID($(oTr));
			if (pid != 0) {
				pids.push(pid);
			}
			adjustBranchPriceByDiscountPercent($(oTr), supplierID,
					discountPercent);
		});
	}
	// 第三步，调整总价显示值
	refreshTotalAmount(supplierID);
}
// 按照百分比折扣，调整父级清单对应价格，仅调整显示值，原始值不更新
function adjustBranchPriceByDiscountPercent(oTr, supplierID, discountPercent) {
	var type = getType(oTr);
	var priceType = getPriceType(oTr);
	var qty = getQty(oTr);
	var oRateEdit = $(oTr).find(
			"input[name='netRate'][supplierID='" + supplierID + "']")[0];
	var oAmountEdit = $(oTr).find(
			"input[name='netAmount'][supplierID='" + supplierID + "']")[0];
	var childType = getChildType(oTr);
	var id = getBillitemID(oTr);
	if (type == 'Bill Item') {
		if (childType == 'Rate Item') {
			// 子项为单价子项，那么该清单可以类比理解当作普通清单子项
			if (priceType == OnlyRate) {
				// 只报单价
				var adjustRate = getAdjustRate4SupplierByID(id, supplierID);
				updateValue($(oRateEdit), adjustRate, adjustRate, true, false);
			} else if (priceType == OnlyAmount) {
				// 只报总价，根据自身总价值调整
				var sCurrentAmount = $(oAmountEdit).attr('originalValue');
				var adjustPrice = calculate(sCurrentAmount, '*',
						discountPercent);
				updateValue($(oAmountEdit), adjustPrice, adjustPrice, true,
						false);
			} else if (priceType == FixAmount) {
				// 固定总价，不参与折扣计算
			} else if (priceType == FixRate) {
				// 固定单价，不参与折扣计算
			} else if (priceType == LumpSumItem) {
				// 不报价，不参与折扣计算
			} else {
				// 普通清单
				var adjustRate = getAdjustRate4SupplierByID(id, supplierID);
				updateValue($(oRateEdit), adjustRate, adjustRate, true, false);
				var adjustAmount = calculate(qty, '*', adjustRate);
				updateValue($(oAmountEdit), adjustAmount, adjustAmount, true,
						false);
			}
		} else {
			// 否则为清单子项，直接统计合价
			var adjustAmount = getAdjustAmount4SupplierByID(id, supplierID);
			updateValue($(oAmountEdit), adjustAmount, adjustAmount, true, false);
		}
	} else if (type == 'Heading') {
		// 标题，累加子清单的合价
		var adjustAmount = getAdjustAmount4SupplierByID(id, supplierID);
		updateValue($(oAmountEdit), adjustAmount, adjustAmount, true, false);
	} else if (type == 'Note') {
		// 说明项
	} else if (type == 'Rate Item') {
		// 父清单为单价子项是不可能的
	}
}
// 按照百分比折扣，调整叶子清单对应价格，仅调整显示值，原始值不更新
function adjustLeafPriceByDiscountPercent(oTr, supplierID, discountPercent) {
	var type = getType(oTr);
	var priceType = getPriceType(oTr);
	var qty = getQty(oTr);
	var oRateEdit = $(oTr).find(
			"input[name='netRate'][supplierID='" + supplierID + "']")[0];
	var oAmountEdit = $(oTr).find(
			"input[name='netAmount'][supplierID='" + supplierID + "']")[0];
	if (type == 'Bill Item') {
		// 清单子项
		if (priceType == OnlyRate) {
			// 只报单价
			var sCurrentRate = $(oRateEdit).attr('originalValue');
			var adjustPrice = calculate(sCurrentRate, '*', discountPercent);
			updateValue($(oRateEdit), adjustPrice, adjustPrice, true, false);
		} else if (priceType == OnlyAmount) {
			// 只报总价
			var sCurrentAmount = $(oAmountEdit).attr('originalValue');
			var adjustPrice = calculate(sCurrentAmount, '*', discountPercent);
			updateValue($(oAmountEdit), adjustPrice, adjustPrice, true, false);
		} else if (priceType == FixAmount) {
			// 固定总价，不参与折扣计算
		} else if (priceType == FixRate) {
			// 固定单价，不参与折扣计算
		} else if (priceType == LumpSumItem) {
			// 不报价，不参与折扣计算
		} else {
			// 正常清单，调整完单价，再调整合价，合价用调整后单价*工程量，防止精度误差
			var sCurrentRate = $(oRateEdit).attr('originalValue');
			var adjustRate = calculate(sCurrentRate, '*', discountPercent);
			updateValue($(oRateEdit), adjustRate, adjustRate, true, false);
			var adjustAmount = calculate(qty, '*', adjustRate);
			updateValue($(oAmountEdit), adjustAmount, adjustAmount, true, false);
		}
	} else if (type == 'Heading') {
		// 标题
	} else if (type == 'Note') {
		// 说明项
	} else if (type == 'Rate Item') {
		// 单价子项，按照父清单类型调整
		var parentPriceType = getParentPriceType(oTr);
		// 清单项
		if (parentPriceType == OnlyRate) {
			// 只报单价
			var sCurrentRate = $(oRateEdit).attr('originalValue');
			var adjustPrice = calculate(sCurrentRate, '*', discountPercent);
			updateValue($(oRateEdit), adjustPrice, adjustPrice, true, false);
		} else if (parentPriceType == OnlyAmount) {
			// 只报总价，不参与折扣计算
		} else if (parentPriceType == FixAmount) {
			// 固定总价，不参与折扣计算
		} else if (parentPriceType == FixRate) {
			// 固定单价，不参与折扣计算
		} else if (parentPriceType == LumpSumItem) {
			// 不报价，不参与折扣计算
		} else {
			// 正常清单
			var sCurrentRate = $(oRateEdit).attr('originalValue');
			var adjustPrice = calculate(sCurrentRate, '*', discountPercent);
			updateValue($(oRateEdit), adjustPrice, adjustPrice, true, false);
		}
	}
}
// 对该行内元素价格作比较
function comparePrice(oTr) {
	var type = getType(oTr);
	var priceType = getPriceType(oTr);
	var prices = new Array();
	if (type == 'Bill Item') {
		var map = {};
		// 只对清单类型做比较
		if (priceType == OnlyRate) {
			// 只报单价比单价
			$(oTr).find("input[name='netRate']").each(function() {
				var supplierID = $(this).attr('supplierID');
				if (supplierID != '-1') {
					var price = transfer2Num($(this).val());
					if (!isNull(price)) {
						var p = parseFloat(price);
						if (!isNaN(p)) {
							map[supplierID] = p;
							prices.push(p);
						}
					}
				}
			});
		} else if (priceType == OnlyAmount) {
			// 只报总价比总价
			$(oTr).find("input[name='netAmount']").each(function() {
				var supplierID = $(this).attr('supplierID');
				if (supplierID != '-1') {
					var price = transfer2Num($(this).val());
					if (!isNull(price)) {
						var p = parseFloat(price);
						if (!isNaN(p)) {
							map[supplierID] = p;
							prices.push(p);
						}
					}
				}
			});
		} else if (priceType == FixAmount) {
			// 固定总价，不参与比较
		} else if (priceType == FixRate) {
			// 固定单价，不参与比较
		} else if (priceType == LumpSumItem) {
			// 不报价，不参与比较
		} else {
			// 正常清单比单价
			$(oTr).find("input[name='netRate']").each(function() {
				var supplierID = $(this).attr('supplierID');
				if (supplierID != '-1') {
					var price = transfer2Num($(this).val());
					if (!isNull(price)) {
						var p = parseFloat(price);
						if (!isNaN(p)) {
							map[supplierID] = p;
							prices.push(p);
						}
					}
				}
			});
		}
		prices = prices.uniquelize();
		if (prices.length > 1) {
			// 代表有2个以上不同价格，这样才显示比较结果
			var min = prices.min();
			var max = prices.max();
			$(oTr).find('td.s').each(function() {
				var prop = $(this).attr('supplierID');
				if (prop != '-1') {
					if (map[prop] == min) {
						$(this).html('<span class="lowest"></span>');
					} else if (map[prop] == max) {
						$(this).html('<span class="highest"></span>');
					} else {
						$(this).html('');
					}
				}
			});
		} else {
			$(oTr).find('td.s').each(function() {
				var prop = $(this).attr('supplierID');
				if (prop != '-1') {
					$(this).html('');
				}
			});
		}
	}
}
// 统计各个供应商的总价
function statistic() {
	refreshTotalAmount("-1");
	refreshTotalAmount("0");
	$("td[name='mark']")
			.each(
					function(i) {
						var supplierID = $(this).attr('supplierID');
						var discountPercent = $(
								"input[name='discountPercent'][supplierID='"
										+ supplierID + "']").val();
						var discountAmount = $(
								"input[name='discountAmount'][supplierID='"
										+ supplierID + "']").val();
						if (!isNull(discountPercent)) {
							// 折扣百分比不为空
							discountPercent = calculate(calculate(100, '-',
									discountPercent), '/', 100);
							var nTotalAmount = getTotalOriginalValue(supplierID);
							var nFixAmount = getTotalFixValue(supplierID);
							var nAdjustAmount = calculate(nTotalAmount, '-',
									nFixAmount);
							var nDiscountAmount = calculate(nAdjustAmount, '-',
									calculate(nAdjustAmount, '*',
											discountPercent));
							// 调整折扣百分比引发的折扣绝对值变化
							$("input[name='discountAmount']")
									.each(
											function() {
												if ($(this).attr('supplierID') == supplierID) {
													updateValue($(this),
															nDiscountAmount,
															'', true, false);
													return false;
												}
											});
							// 折扣不为空，不改变原有真实值的条件下进行值的显示调整
							adjustPriceByDiscountPercent(supplierID,
									discountPercent);
						} else if (!isNull(discountAmount)) {
							// 折扣绝对值不为空
							var nTotalAmount = getTotalOriginalValue(supplierID);
							var nFixAmount = getTotalFixValue(supplierID);
							var nAdjustAmount = calculate(nTotalAmount, '-',
									nFixAmount);
							discountPercent = calculate(calculate(
									nAdjustAmount, '-', discountAmount), '/',
									nAdjustAmount);
							var nDiscountPercent = calculate(100, '-',
									calculate(discountPercent, '*', 100));
							// 调整折扣绝对值引发的折扣百分比变化
							$("input[name='discountPercent']")
									.each(
											function() {
												if ($(this).attr('supplierID') == supplierID) {
													updateValue($(this),
															nDiscountPercent,
															'', true, false);
													return false;
												}
											});
							// 折扣不为空，不改变原有真实值的条件下进行值的显示调整
							adjustPriceByDiscountPercent(supplierID,
									discountPercent);
						} else {
							refreshTotalAmount(supplierID);
						}
					});
}
// 做价格比较
function compare() {
	$('#treeTable tr.item').each(function(i) {
		comparePrice($(this));
	});
}
// 调整采纳价格的显示标识
function adjustAdoptedMark() {
	$("input[name='adoptedUserMark'][supplierID='-1']").each(function(i) {
		var mark = $(this).val();
		if (!isNull(mark) && mark != 'PTE' && mark != 'UD') {
			// 分包商标识调整
			// 先取分包商标识对应的ID
			var supplierID = $(this).attr('adoptedUserID');
			var oTr = $(this).parents("tr")[0];
			if (isChanged(oTr, supplierID)) {
				mark = mark + '#';
			}
			$(this).val(mark);
		}
	});
}
// 执行分析
function analysis() {
	var commonTotalAmountMap = {};
	var balanceAmountMap = {};
	var maxMakeupAmountMap = {};
	var maxTotalAmountMap = {};
	var minMakeupAmountMap = {};
	var minTotalAmountMap = {};
	var differenceMap = {};
	doAnalysisNumber("0");
	commonTotalAmountMap['0'] = '';
	balanceAmountMap['0'] = '';
	maxMakeupAmountMap['0'] = '';
	maxTotalAmountMap['0'] = '';
	minMakeupAmountMap['0'] = '';
	minTotalAmountMap['0'] = '';
	differenceMap['0'] = '';
	$("td[name='mark']").each(function(i) {
		var supplierID = $(this).attr('supplierID');
		doAnalysisNumber(supplierID);
		commonTotalAmountMap[supplierID] = '';
		balanceAmountMap[supplierID] = '';
		maxMakeupAmountMap[supplierID] = '';
		maxTotalAmountMap[supplierID] = '';
		minMakeupAmountMap[supplierID] = '';
		minTotalAmountMap[supplierID] = '';
		differenceMap[supplierID] = '';
	});
	statisticCommonTotalAmount(commonTotalAmountMap);
	statisticBalanceAmount(balanceAmountMap);
	statisticMaxMakeupAmount(maxMakeupAmountMap);
	statisticMaxTotalAmount(maxTotalAmountMap, commonTotalAmountMap,
			balanceAmountMap, maxMakeupAmountMap);
	statisticMinMakeupAmount(minMakeupAmountMap);
	statisticMinTotalAmount(minTotalAmountMap, commonTotalAmountMap,
			balanceAmountMap, minMakeupAmountMap);
	statisticDifference(differenceMap);
}
// 对指定分包商未报价清单数量进行分析
function doAnalysisNumber(supplierID) {
	statisticUnpricedItemNumber(supplierID);
	statisticUnpricedReadOnlyItemNumber(supplierID);
}
// 清单是否满足统计条件
function canStatistic(tr) {
	var bStatistic = false;
	if (hasChild($(tr))) {
		// 有子项，只能是单价子项，就参与合价统计
		var childType = getChildType($(tr));
		if (childType == 'Rate Item') {
			bStatistic = true;
		}
	} else {
		// 无子项就参与统计
		var type = getType($(tr));
		if (type == 'Bill Item') {
			bStatistic = true;
		}
	}
	return bStatistic;
}
// 对未报价清单按照已报价的最大值进行替换后，再统计总价数量进行分析
function statisticMaxMakeupAmount(maxMakeupAmountMap) {
	$("#treeTable tr.item")
			.each(
					function() {
						var bStatistic = canStatistic($(this));
						if (bStatistic) {
							var bIsAllPriced = isAllPriced($(this));
							if (!bIsAllPriced) {
								// 未报价，取最大值
								var maxAmount = getMaxAmount($(this));
								if (!isNull(maxAmount)) {
									$(this)
											.find("input[name='netAmount']")
											.each(
													function() {
														var supplierID = $(this)
																.attr(
																		'supplierID');
														if (supplierID != "-1") {
															if (isNull($(this)
																	.val())) {
																// 对应值为空，才取最大值累加
																maxMakeupAmountMap[supplierID] = calculate(
																		maxMakeupAmountMap[supplierID],
																		'+',
																		maxAmount);
															}
														}
													});
								}
							}
						}
					});
	$.each(maxMakeupAmountMap, function(key, value) {
		$("input[name='maxMakeupAmount']").each(function() {
			if ($(this).attr('supplierID') == key) {
				if (isNull(value)) {
					value = '0';
				}
				updateValue($(this), value, value, true, false);
				return false;
			}
		});
	});
}
// 用A+B+C求和得到
function statisticMaxTotalAmount(maxTotalAmountMap, commonTotalAmountMap,
		balanceAmountMap, maxMakeupAmountMap) {
	var prices = new Array();
	var map = {};
	$.each(maxTotalAmountMap, function(key, value) {
		$("input[name='maxTotalAmount']").each(
				function() {
					if ($(this).attr('supplierID') == key) {
						var v = calculate(calculate(commonTotalAmountMap[key],
								'+', balanceAmountMap[key]), '+',
								maxMakeupAmountMap[key]);
						if (!isNull(v)) {
							var p = parseFloat(transfer2Num(v));
							if (!isNaN(p)) {
								map[key] = p;
								prices.push(p);
							}
						} else {
							v = '0';
						}
						updateValue($(this), v, v, true, false);
						return false;
					}
				});
	});
	prices = prices.uniquelize();
	if (prices.length > 1) {
		// 代表有2个以上不同价格，这样才显示比较结果
		var min = prices.min();
		var max = prices.max();
		$("tr.maxTotalAmount").find('td.s').each(function() {
			var prop = $(this).attr('supplierID');
			if (prop != '-1') {
				if (map[prop] == min) {
					$(this).html('<span class="lowest"></span>');
				} else if (map[prop] == max) {
					$(this).html('<span class="highest"></span>');
				} else {
					$(this).html('');
				}
			}
		});
	} else {
		$("tr.maxTotalAmount").find('td.s').each(function() {
			var prop = $(this).attr('supplierID');
			if (prop != '-1') {
				$(this).html('');
			}
		});
	}
}
// 对未报价清单按照已报价的最小值进行替换后，再统计总价数量进行分析
function statisticMinMakeupAmount(minMakeupAmountMap) {
	$("#treeTable tr.item")
			.each(
					function() {
						var bStatistic = canStatistic($(this));
						if (bStatistic) {
							var bIsAllPriced = isAllPriced($(this));
							if (!bIsAllPriced)
								// 未报价，取最小值
								var minAmount = getMinAmount($(this));
							if (!isNull(minAmount)) {
								$(this)
										.find("input[name='netAmount']")
										.each(
												function() {
													var supplierID = $(this)
															.attr('supplierID');
													if (supplierID != "-1") {
														if (isNull($(this)
																.val())) {
															// 对应值为空，才取最小值累加
															minMakeupAmountMap[supplierID] = calculate(
																	minMakeupAmountMap[supplierID],
																	'+',
																	minAmount);
														}
													}
												});
							}
						}
					});
	$.each(minMakeupAmountMap, function(key, value) {
		$("input[name='minMakeupAmount']").each(function() {
			if ($(this).attr('supplierID') == key) {
				if (isNull(value)) {
					value = '0';
				}
				updateValue($(this), value, value, true, false);
				return false;
			}
		});
	});
}
// 用A+B+D求和得到
function statisticMinTotalAmount(minTotalAmountMap, commonTotalAmountMap,
		balanceAmountMap, minMakeupAmountMap) {
	var prices = new Array();
	var map = {};
	$.each(minTotalAmountMap, function(key, value) {
		$("input[name='minTotalAmount']").each(
				function() {
					if ($(this).attr('supplierID') == key) {
						var v = calculate(calculate(commonTotalAmountMap[key],
								'+', balanceAmountMap[key]), '+',
								minMakeupAmountMap[key]);
						if (!isNull(v)) {
							var p = parseFloat(transfer2Num(v));
							if (!isNaN(p)) {
								map[key] = p;
								prices.push(p);
							}
						}
						if (isNull(v)) {
							v = '0';
						}
						updateValue($(this), v, v, true, false);
						return false;
					}
				});
	});
	prices = prices.uniquelize();
	if (prices.length > 1) {
		// 代表有2个以上不同价格，这样才显示比较结果
		var min = prices.min();
		var max = prices.max();
		$("tr.minTotalAmount").find('td.s').each(function() {
			var prop = $(this).attr('supplierID');
			if (prop != '-1') {
				if (map[prop] == min) {
					$(this).html('<span class="lowest"></span>');
				} else if (map[prop] == max) {
					$(this).html('<span class="highest"></span>');
				} else {
					$(this).html('');
				}
			}
		});
	} else {
		$("tr.minTotalAmount").find('td.s').each(function() {
			var prop = $(this).attr('supplierID');
			if (prop != '-1') {
				$(this).html('');
			}
		});
	}
}
// 统计各个分包商总价与采纳总价之间的差值与百分比
function statisticDifference(differenceMap) {
	var adoptCollection = $("input[name='collection'][supplierID='-1']").val();
	$
			.each(
					differenceMap,
					function(key, value) {
						var collection = $(
								"input[name='collection'][supplierID='" + key
										+ "']").val();
						var oDifferenceAmountEdit = $("input[name='differenceAmount'][supplierID='"
								+ key + "']");
						var oDifferencePercentEdit = $("input[name='differencePercent'][supplierID='"
								+ key + "']");
						var value = calculate(collection, '-', adoptCollection);
						var percent = calculate(calculate(value, '/',
								adoptCollection), '*', 100);
						if (isNull(value)) {
							value = '0';
						}
						updateValue($(oDifferenceAmountEdit), value, value,
								true, false);
						if (isNull(percent)) {
							percent = '0%';
						} else {
							percent = formatFloat(transfer2Num(percent), 2)
									+ '%';
						}
						updateValue($(oDifferencePercentEdit), percent,
								percent, false, false);
					});
}
// 统计所有报价商都进行报价的清单总价
function statisticBalanceAmount(balanceAmountMap) {
	$
			.each(
					balanceAmountMap,
					function(key, value) {
						var collection = $(
								"input[name='collection'][supplierID='" + key
										+ "']").val();
						var commonTotalAmount = $(
								"input[name='commonTotalAmount'][supplierID='"
										+ key + "']").val();
						var oBalanceAmountEdit = $("input[name='balanceAmount'][supplierID='"
								+ key + "']");
						var value = calculate(collection, '-',
								commonTotalAmount);
						if (isNull(value)) {
							value = '0';
						}
						balanceAmountMap[key] = value;
						updateValue($(oBalanceAmountEdit), value, value, true,
								false);
					});
}
// 统计所有报价商都进行报价的清单总价
function statisticCommonTotalAmount(commonTotalAmountMap) {
	$("#treeTable tr.item")
			.each(
					function() {
						var bStatistic = canStatistic($(this));
						if (bStatistic) {
							var bIsAllPriced = isAllPriced($(this));
							if (bIsAllPriced) {
								$(this)
										.find("input[name='netAmount']")
										.each(
												function() {
													var supplierID = $(this)
															.attr('supplierID');
													if (supplierID != "-1") {
														commonTotalAmountMap[supplierID] = calculate(
																commonTotalAmountMap[supplierID],
																'+', $(this)
																		.val());
													}
												});
							}
						}
					});
	$.each(commonTotalAmountMap, function(key, value) {
		$("input[name='commonTotalAmount']").each(function() {
			if ($(this).attr('supplierID') == key) {
				if (isNull(value)) {
					value = '0';
				}
				updateValue($(this), value, value, true, false);
				return false;
			}
		});
	});
}
// 统计各个分包商未报价的正常报价清单和只报总价清单数量
function statisticUnpricedItemNumber(supplierID) {
	var number = 0;
	$("#treeTable tr.item").each(function() {
		var type = getType($(this));
		// 只统计清单
		if (type == 'Bill Item') {
			var priceType = getPriceType($(this));
			// 先判断是否有子项，子项是清单子项，就不参与统计，如果是单价子项，再根据清单自己的价格类型判断，普通找单价，只报总价找总价
			if (hasChild($(this))) {
				if (getChildType($(this)) == 'Rate Item') {
					if (priceType == Normal) {
						// 正常报价，找单价
						if (!quote4NetRate($(this), supplierID)) {
							number += 1;
						}
					} else if (priceType == OnlyAmount) {
						// 只报总价，找总价
						if (!quote4NetAmount($(this), supplierID)) {
							number += 1;
						}
					}
				}
			} else {
				// 子清单，根据清单自己的价格类型判断，普通找单价，只报总价找总价
				if (priceType == Normal) {
					// 正常报价，找单价
					if (!quote4NetRate($(this), supplierID)) {
						number += 1;
					}
				} else if (priceType == OnlyAmount) {
					// 只报总价，找总价
					if (!quote4NetAmount($(this), supplierID)) {
						number += 1;
					}
				}
			}
		}
	});
	$('input[name="numberOfUnpriced"][supplierID="' + supplierID + '"]').val(
			number);
}
// 统计各个分包商未报价的只报单价清单数量
function statisticUnpricedReadOnlyItemNumber(supplierID) {
	var number = 0;
	$("#treeTable tr.item").each(function() {
		var type = getType($(this));
		if (type == 'Bill Item') {
			// 只统计清单
			var priceType = getPriceType($(this));
			if (priceType == OnlyRate) {
				// 仅报单价，找单价数值
				if (hasChild($(this)) && getChildType($(this)) == 'Bill Item') {
					// 有子项，并且子项类型是清单，不参与统计
				} else {
					if (!quote4NetRate($(this), supplierID)) {
						number += 1;
					}
				}
			}
		}
	});
	$('input[name="numberOfUnpricedRO"][supplierID="' + supplierID + '"]').val(
			number);
}
$(function() {
	$('#evalDisplaySettingDialog').on('click', '.close', function() {
		$('#evalDisplaySettingDialog').hide();
	});
	$('#evalDisplaySettingDialog').on('click', '.button .cancel', function() {
		$('#evalDisplaySettingDialog').hide();
	});
});