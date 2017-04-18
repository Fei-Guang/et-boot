// 清单类型
// 标题
var Heading = 1;
// 清单
var BillItem = 2;
// 单价子项
var RateItem = 3;
// 说明项
var Note = 4;

// 清单报价类型
// 正常
var Normal = 1;
// 仅报单价
var OnlyRate = 2;
// 固定总价
var FixAmount = 3;
// 固定单价
var FixRate = 4;
// 不报价
var LumpSumItem = 5;
// 仅报总价
var OnlyAmount = 6;

// 更新文本框的值，needFormat代表是否进行数字格式化，needUpdate代表是否更新原始值
function updateValue(obj, value, originalValue, needFormat, needUpdate) {
	if (needUpdate) {
		$(obj).attr("originalValue", originalValue);
	}
	if (needFormat) {
		$(obj).val(formatNum(value));
	} else {
		$(obj).val(value);
	}
}
// 获取所选分包工程标识
function getSelectedSubProjectID() {
	return $("div.selected").eq(0).attr('subProjectID');
}
// 获取清单行对应的清单ID
function getBillitemID(tr) {
	return $(tr).attr('id');
}
// 获取清单行归属的父清单ID
function getBillitemPID(tr) {
	return $(tr).attr('pId');
}
// 获取价格类型
function getPriceType(tr) {
	return $(tr).attr('priceType');
}
// 获取价格类型标志
function getPriceTypeMark(tr) {
	var pricetype = $(tr).attr('priceType');
	if (pricetype == OnlyRate) {
		return '[Rate Only]';
	} else if (pricetype == FixAmount) {
		return '[Fixed Amount]';
	} else if (pricetype == FixRate) {
		return '[Fixed Rate]';
	} else if (pricetype == LumpSumItem) {
		return '[Empty Price]';
	} else if (pricetype == OnlyAmount) {
		return '[Amount Only]';
	} else {
		return '';
	}
}
// 获取清单类型
function getType(tr) {
	var type = $(tr).attr('type');
	if (type == Heading) {
		return 'Heading';
	} else if (type == BillItem) {
		return 'Bill Item';
	} else if (type == RateItem) {
		return 'Rate Item';
	} else if (type == Note) {
		return 'Note';
	} else {
		return '';
	}
}
// 判断行是否有子元素
function hasChild(tr) {
	var haschild = $(tr).attr('haschild');
	if (!isNullOrUndefined(haschild) && haschild == 'true') {
		var h = false;
		var pid = $(tr).attr('id');
		$("tr[pId='" + pid + "']").each(function(i) {
			if (getType($(this)) != 'Note') {
				h = true;
				return false;
			}
		});
		return h;
	}
	return false;
}
// 获取数据行对应子项的类型
function getChildType(tr) {
	var pid = $(tr).attr('id');
	var type = '';
	$("tr[pId='" + pid + "']").each(function(i) {
		type = getType($(this));
		if (type != 'Note') {
			return false;
		}
	});
	return type;
}
// 获取数据行对应父项的类型
function getParentType(tr) {
	var pid = $(tr).attr('pId');
	if (pid == '0') {
		return "";
	} else {
		var cTr = $("tr[id='" + pid + "']").eq(0);
		return getType(cTr);
	}
}
// 获取数据行对应父项的报价类型
function getParentPriceType(tr) {
	var pid = $(tr).attr('pId');
	if (pid == '0') {
		return "";
	} else {
		var cTr = $("tr[id='" + pid + "']").eq(0);
		return getPriceType(cTr);
	}
}
// 获取清单对应工程量
function getQty(tr) {
	return $($(tr).find("input[name='qty']")[0]).val();
}
// 获取供应商标识
function getSupplierMark(supplierID) {
	var supplyName;
	$("td[supplierID='" + supplierID + "'][name='mark']").each(function() {
		supplyName = $(this).text();
		return false;
	});
	return supplyName;
}
// 判断供应商是否报单价
function quote4NetRate(tr, supplierID) {
	var quoted = true;
	$(tr).find('input[name="netRate"]').each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			var rate = $(this).attr('setValue');
			if (isNull(rate)) {
				quoted = false;
				return false;
			}
		}
	});
	return quoted;
}
// 判断供应商是否报总价
function quote4NetAmount(tr, supplierID) {
	var quoted = true;
	$(tr).find('input[name="netAmount"]').each(function() {
		if ($(this).attr('supplierID') == supplierID) {
			var amount = $(this).attr('setValue');
			if (isNull(amount)) {
				quoted = false;
				return false;
			}
		}
	});
	return quoted;
}
// 表格区域是否是编辑状态
function isEditStatus() {
	var edit = false;
	$("input.edit").each(function() {		
		if ($(this).attr('readonly') != 'readonly') {
			edit = true;
			$(this).validationEngine("validate");
			return false;
		}
	});
	return edit;
}
// 验证单元格输入值是否合理
function validate(src, excludeOperatorModifier) {
	if ($(src).attr('readonly') == 'readonly') {
		// 只读状态不需要执行编辑联动事件
		return false;
	}
	// 还原样式
	recoveryCell($(src));
	if ($(src).validationEngine("validate")) {
		// 这里有点奇怪，非法的返回为true
		focusCell($(src).parent());
		return false;
	}
	if (excludeOperatorModifier) {
		if (validateOperatorModifier(src)) {
			// 是数字修饰符
			$(src).validationEngine("showPrompt", "* Please enter numbers.",
					"error", "bottomLeft", true);
			focusCell($(src).parent());
			return false;
		}
	}
	// 合法，设置为只读
	$(src).attr("readonly", "readonly");
	return true;
}
// 验证是否是数字修饰符
function validateOperatorModifier(src) {
	if (equals(trim($(src).val()), '+') || equals(trim($(src).val()), '-')) {
		return true;
	}
	return false;
}
// 美化树表格为类似excel的多级树表格
function beautifyTreeTable() {
	$('#treeTable tr.item').each(
			function(i) {
				var depth = $(this).attr("depth");
				var type = $(this).attr('type');
				var w = 380 - 20 * parseInt(depth) - 5;
				$($($(this).find("td.treeControl")[0]).find("div")[0]).css(
						"width", w);
				if (depth == 1) {
					if (type == Heading) {
						$(this).addClass("excel_1");
						$(this).find("div").addClass("excel_2").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_2").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_2").addClass(
								"excel_13");

					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					}
				} else if (depth == 2) {
					if (type == Heading) {
						$(this).addClass("excel_3");
						$(this).find("div").addClass("excel_4").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_4").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_4").addClass(
								"excel_13");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 3) {
					if (type == Heading) {
						$(this).addClass("excel_5");
						$(this).find("div").addClass("excel_6").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_6").addClass(
								"excel_12").addClass("excel_13").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_6").addClass(
								"excel_13");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 4) {
					if (type == Heading) {
						$(this).find("div").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 5) {
					if (type == Heading) {
						$(this).find("div").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td.input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 6) {
					if (type == Heading) {
						$(this).find("div").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 7) {
					if (type == Heading) {
						$(this).addClass("excel_7").addClass("excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 8) {
					if (type == Heading) {
						$(this).addClass("excel_7").addClass("excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 9) {
					if (type == Heading) {
						$(this).addClass("excel_7").addClass("excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else if (depth == 10) {
					if (type == Heading) {
						$(this).addClass("excel_7").addClass("excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					} else if (type == BillItem) {
						$(this).find("div").addClass("excel_8");
						$(this).find("td").addClass("excel_8");
						$(this).find("td input").addClass("excel_8");
					} else if (type == Note) {
						$(this).find("div").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_11").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_11").addClass(
								"excel_14");
					} else if (type == RateItem) {
						$(this).find("div").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td").addClass("excel_10").addClass(
								"excel_14");
						$(this).find("td input").addClass("excel_10").addClass(
								"excel_14");
					}
				} else {
					if (type == Heading) {
						$(this).addClass("excel_7").addClass("excel_15");
						$(this).find("td.td6").addClass("excel_7").addClass(
								"excel_15");
						$(this).find("td input").addClass("excel_7");
					}
				}
				/*
				 * var prevRateItem = $(this).prev().attr("type"); var prevDepth =
				 * $(this).prev().attr("depth"); if (type == BillItem && type ==
				 * prevRateItem && depth > prevDepth) {
				 * $(this).find("div").addClass("excel_9");
				 * $(this).find("td.td6").addClass("excel_9"); }
				 */
			});
	$("#treeTable tr.item td").on('click',function() {
		//处理备注新逻辑		
		var nTd = $(this);
		if( nTd.find('input').attr('name')=='remark'){
			var oTr = nTd.parent();
			priceTypeMark = getPriceTypeMark(oTr);			
			nTd.find('input').attr('remark',priceTypeMark);	
		}

		if($("table.quoteData").length > 0){
			if($(this).find('.edit').length==1){
				highlightCell($(this));
			}
		}else{
			//highlightCell($(this));
			if (!isEditStatus()) {
				highlightCell($(this));
			}
		}		
	});

}

// 聚焦单元格
var targetCell;
function focusCell(obj) {
	targetCell = obj;
	setTimeout("doFocus();", 100);
}
function doFocus() {
	highlightCell($(targetCell));
	$($(targetCell).find("input")[0]).focus();
}
// 高亮单元格
function highlightCell(obj) {
	$(obj).css("border", "#0074ba 2px solid");
	$(obj).siblings("td").not("td.scrollbar")
			.css("border", "1px #d3d3d3 solid");
	$(obj).parent("tr").siblings("tr").find("td").not("td.scrollbar").css(
			"border", "1px #d3d3d3 solid");
}
// 还原单元格显示样式
function recoveryCell(obj) {
	$(obj).validationEngine("hideAll");
	$(obj).parents("td").css("border", "1px #d3d3d3 solid");
}
// 对数字编辑框，还原数字显示
function processTextField(obj) {
	var name = $(obj).attr('name');
	if (name != 'remark') {
		var num = $(obj).val();
		$(obj).val(transfer2Num(num));
	}
}
// 注册自定义的提示事件
function registerTipEvent() {
	$('tr td.showTitle').mouseover(function() {
		var inputValue = $(this).find("input").val();
		var titleValue = $(this).attr("title", inputValue);
	});
}
// 注册双击编辑事件
var remarkValue;
function registerDoubleClickEditEvent() {	
		$("input[class~='edit']").each(function() {			
			$(this).dblclick(function() {

				if($('#bQh').hasClass('bt7Active') || $("table.quoteData").length>0){
					$('input.edit').each(function(){
						$(this).attr('readonly','readonly');
					});			
					//这里调整备注信息
					processTextField($(this));
					if($(this).attr('name')=='remark'){
						$(this).attr('ifdbl','1');		
						if(nOldValue != null){					
							var nowValue = $(this).val();					
							var reg = new RegExp('\\[(.+?)\\]',"g");
							var unMark=nowValue.replace(reg,'');
							$(this).removeAttr("readonly").val(unMark+"").focus();
						}else{					
							$(this).removeAttr("readonly").val(" ").val("").focus();
						}
					}else{								
						if(nOldValue != null){					
							$(this).removeAttr("readonly").val(nOldValue+'').focus();
						}else{
							$(this).removeAttr("readonly").val(" ").val("").focus();
						}
					}
				}	
				
					
			});
		});
	
}
// 注册键盘编辑事件
function registerEditEvent() {
	$("input[class~='edit']").each(
			function() {
				var oEdit = $(this);
				// 按E允许编辑
				$(oEdit.parent()).keyup(function(event){					
					if ($(oEdit).attr('readonly') == 'readonly') {
						if (event.which == 69 || event.keyCode == 69) {	
										
							/*if (!isEditStatus()) {																			
								processTextField($(oEdit));										
								if (!isEditStatus()) {
									processTextField($(this));				
									if(!nOldValue){
										oEdit.removeAttr("readonly").val(" ").val("").focus();					
									}else{
										oEdit.removeAttr("readonly").val(nOldValue+" ").val(nOldValue).focus();					
									}				
								}
							}*/
							$('input.edit').each(function(){
								$(this).attr('readonly','readonly');
							});

							processTextField(oEdit);
							if(oEdit.attr('name')=='remark'){
								oEdit.attr('ifdbl','1');		
								if(nOldValue != null){					
									var nowValue = oEdit.val();					
									var reg = new RegExp('\\[(.+?)\\]',"g");
									var unMark=nowValue.replace(reg,'');
									oEdit.removeAttr("readonly").val(unMark+"").focus();
								}else{					
									oEdit.removeAttr("readonly").val(" ").val("").focus();
								}
							}else{								
								if(nOldValue != null){					
									oEdit.removeAttr("readonly").val(formatNum(nOldValue)+'').focus();
								}else{
									oEdit.removeAttr("readonly").val(" ").val("").focus();
								}
							}
						}
					}
				});
				// 失去焦点编辑
				$(this).blur(function() {
					var nNewVal = $(this).val();			
					if($(this).attr('name')=='remark'){
						if($(this).attr('ifdbl')==1){
							var nNewVal = $(this).val();							
							var val = $(this).attr('remark')+nNewVal;				
							$(this).val(val);
							$(this).removeAttr('ifdbl');
						}						
						finishEdit($(this));
					}else{
						if(nNewVal!=''){
							var nowInputValue = parseFloat(transfer2Num(nNewVal));							
							if(isNaN(nowInputValue)){
								$(this).val('');
							}else{								
								$(this).val(nowInputValue);
							}												
						}
						if(nNewVal!=''){							
							var r = /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/;
							
							if(nNewVal.length>50){
								$(this).validationEngine("showPrompt", "Hint:The length cannot exceed 20 characters.Please enter again..",
						"error", "bottomLeft", true);
								$(this).val('');
								focusCell($(this).parent());
								return false;
							}else{
								var ifNum =nNewVal;							
								if(!r.test(ifNum)){
									$(this).validationEngine("showPrompt", "* Please enter numbers.",
							"error", "bottomLeft", true);
									$(this).val('');
									focusCell($(this).parent());
									return false;
								}
							}						
						}
						if($("table.quoteData").length > 0){
							finishEdit($(this));
						}else{
							if(!nOldValue || nNewVal=='' || nNewVal!=nOldValue){
								finishEdit($(this));
							}else{
								$(this).attr('readonly',true);
							}
						}
						$(this).val(formatNum(nNewVal));
					}
					
				});
				$(this).keypress(function(event) {
					if (event.which == 13 || event.keyCode == 13) {						
						var nNewVal = $(this).val();			
					if($(this).attr('name')=='remark'){
						if($(this).attr('ifdbl')==1){
							var nNewVal = $(this).val();							
							var val = $(this).attr('remark')+nNewVal;				
							$(this).val(val);
							$(this).removeAttr('ifdbl');
						}						
						finishEdit($(this));
					}else{
						if(nNewVal!=''){
							var nowInputValue = parseFloat(transfer2Num(nNewVal));							
							if(isNaN(nowInputValue)){
								$(this).val('');
							}else{								
								$(this).val(nowInputValue);
							}												
						}
						if(nNewVal!=''){							
							var r = /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/;
							
							if(nNewVal.length>50){
								$(this).validationEngine("showPrompt", "Hint:The length cannot exceed 20 characters.Please enter again.",
						"error", "bottomLeft", true);
								$(this).val('');
								focusCell($(this).parent());
								return false;
							}else{
								var ifNum =nNewVal;							
								if(!r.test(ifNum)){
									$(this).validationEngine("showPrompt", "* Please enter numbers.",
							"error", "bottomLeft", true);
									$(this).val('');
									focusCell($(this).parent());
									return false;
								}
							}						
						}
						if($("table.quoteData").length > 0){
							finishEdit($(this));
						}else{
							if(!nOldValue || nNewVal=='' || nNewVal!=nOldValue){
								finishEdit($(this));
							}else{
								$(this).attr('readonly',true);
							}
						}
						$(this).val(formatNum(nNewVal));
					}
					}
				});
			});
}
// 注册双击采纳事件 
function registerAdoptEvent() {
	$("td.adopt").each(function() {
		$(this).dblclick(function() {			
			if (!isEditStatus()) {
				if(!$('#bQh').hasClass('bt7Active')){
					finishAdopt($(this));
				}				
			}
		});
	});
}
function menuHide() {
	$("div.tab").hide();
	$("div.tab_page").css("left", "10px");
	$("div.tab_page span.menuShow").show();
}
function menuShow() {
	$("div.tab").show();
	$("div.tab_page").css("left", "230px");
	$("div.tab_page span.menuShow").hide();
}