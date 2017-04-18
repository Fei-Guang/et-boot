<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="user cell">
	<div class="userInfo">
		<div class="userInfoLayout"></div>
		<div class="content">
			<div class="supply title">Please complete your account info to
				continue</div>
			<div class="row-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span><span>Name</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="${sessionScope.currentUser.name}"
						id="name" onfocus="plusBorder(this.id);"
						onblur="startBorder(this.id);" />
				</div>
			</div>
			<div class="row-tip" id="name-tip"></div>

			<div class="item">
				<div class="startEmail">
					<span class="star"></span><span>Phone</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="${sessionScope.currentUser.telephone}"
						id="phone" onfocus="plusBorder(this.id);"
						onblur="startBorder(this.id);" />
				</div>
			</div>
			<div class="row-tip" id="phone-tip"></div>

			<div class="item">
				<div class="startEmail">
					<span class="star"></span><span>Company Name</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="${sessionScope.currentUser.company}"
						id="company" onfocus="plusBorder(this.id);"
						onblur="startBorder(this.id);" />
				</div>
			</div>
			<div class="row-tip" id="company-tip"></div>

			<div class="item">
				<div class="startEmail">
					<span class="placeholder"></span><span class="indent">Company
						Address</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="${sessionScope.currentUser.address}"
						id="address" onfocus="plusBorder(this.id);"
						onblur="startBorder(this.id);" />
				</div>
			</div>
			<div class="row-tip"></div>

			<div class="item">
				<div class="startEmail">
					<span class="placeholder"></span><span class="indent">Company
						Website</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="${sessionScope.currentUser.website}"
						id="website" onfocus="plusBorder(this.id);"
						onblur="startBorder(this.id);" />
				</div>
			</div>
			<div class="control">
				<div id="supply-tip"></div>
				<div class="btn">
					<input type="submit" value="OK" class="yes" onclick="supplyInfo();" />
					<input type="button" value="Back" class="no"
						onclick="window.location.href='${context}';" />
				</div>
			</div>
		</div>

	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/user/supplyPersonalInfo.js'/>"></script>