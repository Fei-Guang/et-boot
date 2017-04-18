<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="auth cell">
	<div class="authRegister tipactive">
		<div class="registerBanner"></div>
		<div class="content">
		   <div class="tipcenter">
		         <span class="icon">
		         </span>
		         <div class="tipcontent">
		           <p class="p1">The activation email sent to  <span class="username">${sessionScope.currentUser.email}</span></p>
		           <p class="p2">Please click the link in the email within 24 hours to activate your account.</p>
		         </div>
		         <div class="button">
		         <a href="/etender/">
		           <input type="submit" value="OK"></a>
		         </div>
		   </div>			
		</div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/auth/tipactive.js'/>"></script>