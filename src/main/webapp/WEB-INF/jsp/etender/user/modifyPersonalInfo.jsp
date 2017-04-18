<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="user cell">
	<div class="userInfo">
		<div class="modifyInfoLayout"></div>
		<div class="modifyContent">
			<div class="content">
				<div class="modify title">Personal Information</div>
				<div class="row-tip"></div>
				<div class="item">
					<div class="startEmail">
						<span class="star"></span><span>Email</span>
					</div>
					<div class="inputEmail">
						<span id="email">${sessionScope.currentUser.email}</span> <span
							onclick="showPsw();" class="modifyPassword">【<a>Modify
								password</a>】
						</span>
					</div>
				</div>
				<div class="clear" style="clear:both"></div>
				<div class="row-tip"></div>
				<!-- 密码隐藏信息 -->
				<div class="hiddenArea">
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Original Password</span>
						</div>
						<div class="inputEmail">
							<input type="password" value="" id="origPass" class="notHidden"
								onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="origPass-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Input Password</span>
						</div>
						<div class="inputEmail">
							<input type="password" value="" id="inputPass" class="notHidden"
								onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="inputPass-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Confirm Password</span>
						</div>
						<div class="inputEmail">
							<input type="password" value="" id="confirmPass"
								class="notHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);">
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="confirmPass-tip"></div>
				</div>
				<!-- 初始用户信息 -->
				<div class="userInfoArea">
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Name</span>
						</div>
						<div class="inputEmail">
							<input type="text" value="${sessionScope.currentUser.name}"
								id="name" class="inputHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);" />
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="name-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Phone</span>
						</div>
						<div class="inputEmail">

							<input type="text" value="${sessionScope.currentUser.telephone}"
								id="phone" class="inputHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);" />
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="phone-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="star"></span><span>Company Name</span>
						</div>
						<div class="inputEmail">
							<input type="text" value="${sessionScope.currentUser.company}"
								id="company" class="inputHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);" />
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip" id="company-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="placeholder"></span><span class="indent">Company
								Address</span>
						</div>
						<div class="inputEmail">
							<input type="text" value="${sessionScope.currentUser.address}"
								id="address" class="inputHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);" />
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip"></div>
					<div class="item">
						<div class="startEmail">
							<span class="placeholder"></span><span class="indent">Company
								Website</span>
						</div>
						<div class="inputEmail">
							<input type="text" value="${sessionScope.currentUser.website}"
								id="website" class="inputHidden" onfocus="plusBorder(this.id);"
								onblur="startBorder(this.id);" />
						</div>
					</div>
					<div class="clear" style="clear:both"></div>
					<div class="row-tip"></div>
				</div>
				<div class="control">
					<div id="modify-tip"></div>
					<div class="btn">
						<input type="submit" value="OK" class="yes"
							onclick="modifyInfo();" /> <input type="button" value="Cancel"
							class="no" onclick="window.location.href='${context}';" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/arithmetic/des.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/user/modifyPersonalInfo.js'/>"></script>