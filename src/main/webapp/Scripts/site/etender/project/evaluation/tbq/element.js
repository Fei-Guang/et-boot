function extendFolder(id, type) {
	var c = $('#' + id + ' span:first').attr('class');
	if (c.indexOf('open') != -1) {
		$('#' + id + ' span:first').removeClass('open');
		$('#' + id + ' span:first').addClass('fold');
	} else {
		$('#' + id + ' span:first').removeClass('fold');
		$('#' + id + ' span:first').addClass('open');
	}
	if (type == 0) {
		if (c.indexOf('open') != -1) {
			// 折叠，隐藏2,3级
			$('div.menu2').each(function(i) {
				var sId = $(this).attr("sId");
				if (sId == id) {
					$(this).hide();
					$(this).children("span.icon").removeClass('open');
					$(this).children("span.icon").addClass('fold');

				}
			});
			$('div.menu3').each(function(i) {
				var sId = $(this).attr("sId");
				if (sId == id) {
					$(this).hide();
				}
			});
		} else {
			// 展开，展开2级
			$('div.menu2').each(function(i) {
				var sId = $(this).attr("sId");
				if (sId == id) {
					$(this).show();
					$(this).children("span.icon").removeClass('open');
					$(this).children("span.icon").addClass('fold');
				}
			});
		}
	} else if (type == 1) {
		if (c.indexOf('open') != -1) {
			// 折叠，隐藏3级
			$('div.menu3').each(function(i) {
				var pId = $(this).attr("pId");
				if (pId == id) {
					$(this).hide();
				}
			});
		} else {
			// 展开，展开3级
			$('div.menu3').each(function(i) {
				var pId = $(this).attr("pId");
				if (pId == id) {
					$(this).show();
				}
			});
		}
	}
}

function loadBillItem(id, type) {
	$(".selected").removeClass("selected");
	var flag = "";
	if (type == 0) {
		$("#root" + id).addClass("selected");
	} else if (type == 1) {
		$("#branch" + id).addClass("selected");
	} else if (type == 2) {
		$("#leaf" + id).addClass("selected");
	}
	showProgress();
	$.get(rootFolder+'/project/evaluation/billitem4tbq?t=' + Math.random(), {
		eleID : encodeURIComponent(id),
		eleType : encodeURIComponent(type)
	}, function(data) {
		$("#evaluationPage").html(data);
		$('input').on('click',function(event){			
			nOldValue = transfer2Num($(this).val());
			if($(this).attr('readonly')=='readonly'){
				$(this).blur();
			}
		});
		//处理事件切换
		$("#bQh").live('click',function(){
	        $(this).toggle(function(i){
	        		
	        		$(this).html('Double click to adopt');			    	
			    	$(this).addClass("bt7Active");
			    	registerDoubleClickEditEvent();
			    	return false;
	            },
	            function(i){
	            	            	
	            	$(this).html('Double click to edit');			    	    	    	
			    	$(this).removeClass("bt7Active");
			    	registerAdoptEvent();
			    	return false;		    	
	            }
	        );
	        $(this).trigger('click');
	    });
		initEvaluationBillItem();
		hideProgress();
	});
}