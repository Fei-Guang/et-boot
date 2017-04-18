<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="header shadow">
	<div class="bannerCell">
		<div id="logo">
			<a href="<c:url value='/'/>"
				title="<spring:message code="common.banner.back"/>"> <img
				src="<c:url value='/Images/etender/header/logo-top.png'/>"
				alt="E-tender" />
			</a>
		</div>
		<c:if test="${empty sessionScope.currentUser}">
			<div class="rightHome">
				<ul class="homeSupport">
					<li class="homePage"><span class="home"> <a href="${context}/"/>Home Page</a></span></li>					
					<li class="support threeLi"><span class="support">Support</span><span
						class="switchIcon"></span>
						<div class="systemMenu popupMenu support loginSupport"
							tabindex="0">
							<div class="arrowTop">
								<div class="arrow1"></div>
								<div class="arrow2"></div>
							</div>
							<div id="menuList">
								<ul class="menuContent">
									<%-- <li class="about"><a
										href="<c:url value='/error/index'/>"> About Us</a></li> --%>
									<li class="help"><a href="<c:url value='/help/index'/>">
											Help</a></li>
									<%-- <li class="contact"><a
										href="<c:url value='/error/index'/>" onclick="clearCookie();">Contact
											Us</a></li> --%>
								</ul>
							</div>
							</div>
						</li>
				</ul>
			</div>
		</c:if>
		<c:if test="${!empty sessionScope.currentUser}">
			<div class="right sysMenu">
				<ul class="rightPersonalInformation supportUl">
					<li class="secondLi"><a href="<c:url value='/'/>">Home
							Page</a></li>

					<li class="threeLi"><span class="support">Support</span><span
						class="switchIcon"></span>
						<div class="systemMenu popupMenu support" tabindex="0">

							<div class="arrowTop">
								<div class="arrow1"></div>
								<div class="arrow2"></div>
							</div>
							<div id="menuList">
								<ul class="menuContent">
									<%-- <li class="about"><a
										href="<c:url value='/error/index'/>"> About Us</a></li> --%>
									<li class="help"><a href="<c:url value='/help/index'/>">
											Help</a></li>
									<%-- <li class="contact"><a
										href="<c:url value='/error/index'/>" onclick="clearCookie();">Contact
											Us</a></li> --%>
								</ul>
							</div></li>
					<!-- 	<li class="fourLi"><span class="infors"></span><span
						class="num"></span><span class="notif"><a href="#"
							onclick="showInformationBox();">Notifications</a></span></li> -->
					<li class="firstLi userLi"><span class="icon"></span> <span
						class="mailName" onclick="toggleSystemMenu();">Hello!
							${sessionScope.currentUser.email}</span> <span class="triangleIcon"></span>
						<!-- 弹出框 -->
						<div class="systemMenu popupMenu userMenu" tabindex="0">
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
		</c:if>
	</div>
</div>
<div id="informationTipBox">
	<div class="messageFirst">
		<div class="messageTime">
			<span class="envelopeIcon"></span><span class="time">2016/10/02
				17:11</span>
		</div>
		<div class="specificMessage">
			XXX(分包公司名称)公司给您发送了关于XXX(总工程名称)工程的报价文件，请在报价截止时间之后<br>查看。
		</div>
	</div>
	<div class="messageFirst messageSecond">
		<div class="messageTime">
			<span class="envelopeIcon"></span><span class="time">2016/10/02
				17:11</span>
		</div>
		<div class="specificMessage">
			XXX(分包公司名称)公司给您发送了关于XXX(总工程名称)工程的报价文件，请在报价截止时间之后<br>查看。
		</div>
	</div>
	<div class="messageFirst messageThird">
		<div class="messageTime">
			<span class="envelopeIcon"></span><span class="time">2016/10/02
				17:11</span>
		</div>
		<div class="specificMessage">
			XXX(分包公司名称)公司给您发送了关于XXX(总工程名称)工程的报价文件，请在报价截止时间之后<br>查看。
		</div>
	</div>
</div>
<div id="rightPromptBox">
	<div class="receiveInfo">
		XXX(分包公司名称)公司给您发送了关于XXX(总工程名称)工程的报价文件，请在报价截止时间之后查看。</div>
	<div class="notReadMore">
		<div class="left">
			<span class="noreadIcon"></span><span class="noread">未读</span>
		</div>
		<div class="right">
			<span class="moreIcon"></span><span class="more">more</span>
		</div>
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