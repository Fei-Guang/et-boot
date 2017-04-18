var end = false;
function scrollLoad(func) {
	$(window).scroll(function() {
		if (!end) {
			// 滚动条在最底端的时候
			if (document.documentElement.scrollHeight == (document.documentElement.scrollTop | document.body.scrollTop)
					+ document.documentElement.clientHeight) {
				if (typeof (func) == "function") {
					func();
				}
			}
			// 滚动条在最顶端的时候
			if ((document.documentElement.scrollTop | document.body.scrollTop) == 0) {

			}
		}
	});
}