$(function() {
	$('#tradeForm').validationEngine("attach", {
		promptPosition : "bottomLeft",
		scroll : false,
		onFieldFailure:function(){
			var tradeScroll = document.getElementById('tradeData');		
			tradeScroll.scrollTop=tradeScroll.scrollHeight;
		}
	});
	var h = $(window).height() - 400;
	$('div.content div#tradeData').css("height", h);
});