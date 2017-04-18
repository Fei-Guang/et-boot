<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="auth cell">
	<div class="authRegister">
		<div class="registerBanner"></div>
		<div class="content">
			<div class="register title">Sign Up</div>
			<div class="row-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Email</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="email"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="email-tip"></div>
		
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Password</span>
				</div>
				<div class="inputEmail">
					<input type="password" value="" id="password"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="password-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Confirm Password</span>
				</div>
				<div class="inputEmail">
					<input type="password" value="" id="confirm"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="confirm-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Name</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="name"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="name-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Phone</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="phone"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="phone-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Company Name</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="company"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip" id="company-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="placeholder"></span> <span>Company Address</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="address"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="placeholder"></span> <span>Company Website</span>
				</div>
				<div class="inputEmail">
					<input type="text" value="" id="website"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
				</div>
			</div>
			<div class="row-tip"></div>
			<div class="item">
				<div class="startEmail">
					<span class="star"></span> <span>Verification Code</span>
				</div>
				<div class="inputCode">
					<input type="text" value="" id="captcha"
						onfocus="plusBorder(this.id);" onblur="startBorder(this.id);">
					<div class="refresh"  onclick="reloadCaptcha();">
						<span class="refreshIcon"></span> Refresh
					</div>
					<img style="cursor:pointer" id="img_captcha" class="absmiddle"
						src="/etender/auth/captcha"
						title="The image is blurred, change another" alt=""
						onclick="reloadCaptcha();" />
				</div>
			</div>
			<div class="row-tip" id="captcha-tip"></div>
			<div class="agree clearfix">
				<input id="agree" class="chk" type="checkbox"><label
					for="agree"></label><span class="agree-tip">I agree to the
						<a target="_blank" href="https://account.glodon.com/agreement">Glodon
							User Agreement</a> and <a target="_blank"
						href="https://account.glodon.com/privacy">Privacy Policy</a>
				</span>
			</div>
			<div class="control">
			<div id="register-tip"></div>
				<div class="btn">
					<input type="submit" value="OK" class="yes" onclick="register();"/>
					<input type="button" value="Cancel" class="no"
						onclick="window.location.href='${context}';" />
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/arithmetic/des.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/auth/register.js'/>"></script>