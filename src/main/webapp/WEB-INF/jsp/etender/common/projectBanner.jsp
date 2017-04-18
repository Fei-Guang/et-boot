<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="header projectHeader">
	<div id="logoLeft">
		<a href="<c:url value='/'/>"
			title="<spring:message code="common.banner.back"/>"> <img
			src="<c:url value='/Images/etender/header/logo-top.png'/>"
			alt="E-tender" />
		</a>
	</div>
	<div class="right sysMenu">
		<ul class="rightPersonalInformation projectSysmenuRight">
			<li class="secondLi"><span class="hp icon"><a
					href="<c:url value='/'/>">Home Page</a></span></li>
			<li class="fourLi"><span class="pl icon"><a
					href="<c:url value='/project/index'/>">Project List</a></span></li>
			<li class="fiveLi threeLi"><span class="su icon"><a
					href="<c:url value='/'/>">Support</a></span><span class="switchIcon"></span>
				<div class="systemMenu popupMenu support projectSupport"
					tabindex="0">
					<div class="arrowTop">
						<div class="arrow1"></div>
						<div class="arrow2"></div>
					</div>
					<div id="menuList">
						<ul class="menuContent">
							<%-- <li class="about"><a href="<c:url value='/error/index'/>">
									About Us</a></li> --%>
							<li class="help"><a
								href="<c:url value='/help/index'/>"> Help</a></li>
							<%-- <li class="contact"><a href="<c:url value='/error/index'/>"
								onclick="clearCookie();">Contact Us</a></li> --%>
						</ul>
					</div></li>
			<!-- <li class="sixLi"><span class="infors"></span><span class="num"></span><span
				class="notif"><a href="#" onclick="showInformationBox();">Notifications</a></span></li> -->
			<li class="firstLi userLi"><span class="icon"></span> <span
				class="mailName" onclick="showUserMenu();">Hello!
					${sessionScope.currentUser.email}</span> <span
				class="triangleIcon"></span>
				<div class="systemMenu popupMenu userMenu" tabindex="0"
					>
					<div class="arrowTop">
						<div class="arrow1"></div>
						<div class="arrow2"></div>
					</div>
					<div id="menuList">
						<ul class="menuContent">
							<li class="sub"><a href="<c:url value='/supplier/index'/>">Sub-Con
									Management</a></li>
							<!-- <li class="inforLi"><span class="infor"></span> <span
										class="num">5</span><a href="#"
										onclick="showInformationBox();">Information</a></li> -->
							<li class="personInfo"><a
								href="<c:url value='/user/modifyPersonalInfo'/>">Personal
									information</a></li>
							<li class="logout"><c:choose>
									<c:when test="${empty sessionScope._logout_url_}">
										<a href="<c:url value='/auth/logout'/>"
											onclick="clearCookie();">Logout</a>
									</c:when>
									<c:otherwise>
										<a href="${sessionScope._logout_url_}"
											onclick="clearCookie();">Logout</a>
									</c:otherwise>
								</c:choose></li>
						</ul>
					</div>
				</div></li>
		</ul>
	</div>
</div>
<!-- 进度提示 -->
<div id="progressDialog" class="dialog">
	<div class="back"></div>
	<img src="<c:url value='/Images/etender/header/loading.gif'/>" alt="" />
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/thirdparty/artDialog/artDialog.source.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/thirdparty/artDialog/iframeTools.source.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/common/banner.js'/>"></script>