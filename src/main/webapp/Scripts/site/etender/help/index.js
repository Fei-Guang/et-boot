$(function() {
	$("div.right div.one").hover(function() {
		$(this).css("border", "1px solid #0081ce");
		$(this).find("span").css("background-color", "#0081ce");
		$(this).find("a").addClass("turn");
	}, function() {
		$(this).css("border", "1px solid #e8e8e8");
		$(this).find("span").css("background-color", "#40b3e4");
		$(this).find("a").removeClass("turn");
	})

})