$(function() {
	$.get(rootFolder+'/project/quote/element4tbq?t=' + Math.random(), function(data) {
		$("#navigation").html(data);
		$($("div.selected").find("span")[1]).click();
	});
	$('div.layout').css({
		'min-width' : '1400px'
	});
});