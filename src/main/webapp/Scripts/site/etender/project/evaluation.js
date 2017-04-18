$(function() {
	$.get(rootFolder+'/project/evaluation/element4tbq?t=' + Math.random(), function(data) {
		$("#navigation").html(data);
		$($("div.selected").find("span")[1]).click();
	});
});