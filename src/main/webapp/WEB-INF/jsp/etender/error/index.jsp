<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="error cell">
	<div class="error-tip">
		<div class="errorMain">
			<div class="center">
				<div class="icon">
					<img src="<c:url value='/Images/etender/error/tip.png'/>" />
				</div>
				<span class="four">404</span> <span class="not">This page
					isn't available.Sorry about that.</span> <input type="button"
					value="Back to homepage" class="input"
					onclick="window.location.href='${context}';">
			</div>
		</div>
	</div>
</div>