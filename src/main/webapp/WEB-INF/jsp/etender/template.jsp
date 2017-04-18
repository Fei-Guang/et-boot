<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="shortcut icon" href="<c:url value='/Images/favicon.ico'/>"
	type="image/x-icon" />
<title>CubiCost-Etender</title>
<link href="<c:url value='/Styles/common/Site.css'/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value='/Styles/common/Color.css'/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value='/Styles/common/Layout.css'/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value='/Styles/site/etender/animation.css'/>"
	rel="stylesheet" type="text/css">
<link href="<c:url value='/Styles/site/etender/style.css '/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/Styles/jquery/validation/validationEngine.jquery.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/Styles/jquery/ui/jquery-ui-1.8.17.custom.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/Styles/jquery/ui/jquery-ui-timepicker-addon.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/Styles/thirdparty/artDialog/skins/etender.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/Styles/thirdparty/swiper/swiper-3.3.1.min.css'/>"
	rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.browser.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.cookie.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.json-2.4.js'/>"></script>
<script type="text/javascript" src="<c:url value='/Scripts/config.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/global.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/string.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/tools/placeholder4IE.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/util/util.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/followvist/followvist.js'/>"></script>
</head>
<body class="main_body">
	<%@include file="../common/warn.jsp"%>
	<c:set var="context" value="${pageContext.request.contextPath}" />
	<div class="layout">
		<!-- 内容区块 -->
		<div class="wraper clearfix">
			<c:choose>
			    <c:when test="${page=='project_index'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/index.jsp"%>
				</c:when>
				<c:when test="${page=='project_inquireRecord'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/inquireRecord.jsp"%>
				</c:when>
				<c:when test="${page=='project_quoteRecord'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/quoteRecord.jsp"%>
				</c:when>
				<c:when test="${page=='project_inquire'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/inquire.jsp"%>
				</c:when>
				<c:when test="${page=='project_quote'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/quote.jsp"%>
				</c:when>
				<c:when test="${page=='project_evaluation'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="project/evaluation.jsp"%>
				</c:when>
				<c:when test="${page=='auth_login'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="auth/login.jsp"%>
				</c:when>
				<c:when test="${page=='auth_register'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="auth/register.jsp"%>
				</c:when>
				<c:when test="${page=='user_supplyPersonalInfo'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="user/supplyPersonalInfo.jsp"%>
				</c:when>
				<c:when test="${page=='user_modifyPersonalInfo'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="user/modifyPersonalInfo.jsp"%>
				</c:when>
				<c:when test="${page=='supplier_index'}">
					<%@include file="common/projectBanner.jsp"%>
					<%@ include file="supplier/index.jsp"%>
				</c:when>
				<c:when test="${page=='help_index'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="help/index.jsp"%>
				</c:when>
				<c:when test="${page=='error_index'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="error/index.jsp"%>
				</c:when>
				<c:when test="${page=='auth_tipactive'}">
					<%@include file="common/banner.jsp"%>
					<%@ include file="auth/tipactive.jsp"%>
				</c:when>
				<c:otherwise>
					<%@include file="common/banner.jsp"%>
					<%@ include file="auth/login.jsp"%>
				</c:otherwise>
			</c:choose>
		</div>
		<%@ include file="common/copyright.jsp"%>
	</div>
</body>
</html>