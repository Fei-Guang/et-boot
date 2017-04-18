<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<div id="browserupgrade"
	style="line-height:25px;text-align:left;text-indent:10px;color:#fff;background-color:#333;display:none;font-size:14px;">
	You are using an <strong class="red" style="font-style:italic;">outdated</strong> browser. Please <a href="http://browsehappy.com/?locale=en" style="color:#999;">upgrade
		your browser</a> to improve your experience.
</div>
<script type="text/javascript">
	$(function() {
		if ($.browser.msie) {
			// IE浏览器判断
			if ($.browser.version < 10) {
				$("div#browserupgrade").show();
			}
		} else if ($.browser.mozilla) {
			// 火狐浏览器判断
		} else if ($.browser.webkit) {
			// 谷歌浏览器判断
		} else if ($.browser.safari) {
			// 苹果浏览器判断
		}
	});
</script>
