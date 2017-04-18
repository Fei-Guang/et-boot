<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="project main">
	<div class="projectIndex">
		<div class="tableChange">
				<span class="inquireProject" onclick="showInquireProject();"><span
					class="active topBorder"></span>
				<spring:message code="project.index.inquired" /></span> <span
					class="quoteProject" onclick="showQuoteProject();"><span
					class="topBorder"></span>
				<spring:message code="project.index.quoted" /></span>
				<div class="tableLeft">
			</div>
			<div class="rightNone"></div>
		</div>
		<div id="inquireProject"></div>
		<div id="quoteProject"></div>
		<div class="lastRow"></div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/StringBuilder.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/arithmetic/des.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/index.js'/>"></script>