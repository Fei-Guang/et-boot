$(function() {
	var topDistance = $(window).height() - 300; // go_top距顶端距离
	var showDistance = 200; // 距离顶端多少距离开始显示go_top
	var gotopIcon = "<div class='go_top' title='Back to top'></div>";
	var thisTop = $(window).scrollTop() + topDistance;
	$($(".layout")[0]).css("position", "relative");
	$($(".layout")[0]).append(gotopIcon);
	$(".go_top").css("top", thisTop);
	if ($(window).scrollTop() < showDistance) {
		$(".go_top").hide();
	}
	$(window).scroll(function() {
		thisTop = $(this).scrollTop() + topDistance;
		$(".go_top").css("top", thisTop);
		if ($(this).scrollTop() < showDistance) {
			$(".go_top").fadeOut("fast");
		} else {
			$(".go_top").fadeIn("fast");
		}
	});
	$(".go_top").click(function() {
		$("html").animate({
			scrollTop : 0
		}, "normal"); // IE,FF
		$("body").animate({
			scrollTop : 0
		}, "normal"); // Webkit
		return false;
	});
});