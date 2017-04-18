function backProject() {
	window.location.href = rootFolder+'/project/index?t=' + Math.random();
}
//根据table生成Array
function GetArrayFromTable(tableDataId) {
	var result = [];
	var timeResult = [];	
	var oData = document.getElementById(tableDataId);
	for (var i = 0; i < oData.rows.length; i++) {//遍历所有行
		result[i] = [];
		timeResult[i] = [];
		titleResult[i] =[];
		noLowerResult[i] = [];
		for (var j = 0; j < oData.rows[i].cells.length; j++) { // 遍历Row中的每一列
			var timeTitle = oData.rows[i].cells[j].getAttribute('title');
			var nowText = oData.rows[i].cells[j].innerText;
			var nextTime=0;
			if(j>3 && j<8){				
				if(timeTitle==''){					
					nextTime = 0;
				}else{
					nextTime = Date.parse(new Date(timeTitle));
				}
			}
			if(j==8){
				var hms = timeTitle.split(nowText);
				timeTitle = nowText+' '+hms[1];
				nextTime = Date.parse(new Date(timeTitle));
			}		
									
			result[i][j] = nowText.toLowerCase();
			noLowerResult[i][j] = nowText;
			timeResult[i][j] = nextTime;
			titleResult[i][j] = timeTitle;						
			sCloumnOrder[j] = 'desc';
		}
		result[i].push.apply(result[i],timeResult[i]);
		result[i].push.apply(result[i],titleResult[i]);
	}
	return result;
}
var sCloumnOrder = [];
var titleResult = [];
var noLowerResult = [];
function listSortBy(arr, field, order){ 
    var refer = [], result=[], order = order=='asc'?'asc':'desc', index; 
    for(i=0; i<arr.length; i++){ 
        refer[i] = arr[i][field]+':'+i; 
    }
    if(field<3){
    	refer.sort(function(x, y){
		  return x.localeCompare(y);
		}); 
    }else{
    	refer.sort(function(x, y){
		  return x-y;
		}); 
    }
	    
    if(order=='desc'){
    	if(field<3){
	    	refer.sort(function(x, y){
			  return x.localeCompare(y);
			}).reverse();; 
	    }else{
	    	refer.sort(function(x, y){
			  return x-y;
			}).reverse();; 
	    }    	
    }  
    for(i=0;i<refer.length;i++){ 
        index = refer[i].split(':')[1]; 
        result[i] = noLowerResult[index]; 
    } 
    return result;
}
$(function() {
	var h = $(window).height() - 196;
	$('div.autoScroll').css("height", h);
	var aResult = [];	
	var cTable = document.getElementById('tableData');
	var aTableData = GetArrayFromTable('tableData');

	//点击排序
	$('#recordTable th').not('.noOrder').css('cursor','pointer').click(function() {
		$('.arrowOrder').attr('class','arrowOrder des');
		var Aindex = $(this).index();
		var sArrowClass = $(this).find('.arrowOrder');	
		if(sArrowClass.hasClass('des')){
			sArrowClass.attr('class','arrowOrder asc');
			sCloumnOrder[Aindex] = 'asc';
		}else{
			sArrowClass.attr('class','arrowOrder des');
			sCloumnOrder[Aindex] = 'desc';
		}
		
		if(Aindex>3){			
			aResult = listSortBy(aTableData,Aindex+9, sCloumnOrder[Aindex]);				
		}else{
			aResult = listSortBy(aTableData,Aindex, sCloumnOrder[Aindex]);
		}		
		for (var i = 0; i < cTable.rows.length; i++) {
			for (var j = 0; j < cTable.rows[i].cells.length; j++) { // 遍历Row中的每一列
				cTable.rows[i].cells[j].setAttribute('title',aResult[i][j+18]);
				cTable.rows[i].cells[j].innerText = aResult[i][j];
			}
		}		
	});
})