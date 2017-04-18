<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="auth cell">
	<div class="authLogin">
		<div class="loginLayout">
			<c:choose>
				<c:when test="${empty sessionScope.currentUser}">
					<div class="middle">
						<div class="swiper-container smallSwiper">
							<div class="swiper-wrapper">
								<div class="swiper-slide bannerOne"></div>
								<div class="swiper-slide bannerTwo"></div>
								<div class="swiper-slide bannerThree"></div>
							</div>
							<div class="swiper-pagination"></div>
						</div>
						<div class="ceng cengOne"></div>
						<div class="loginOne">
							<div class="out">
								<div class="Name user">
									<div class="userIcon icon"></div>
									<input type="text" placeholder="Email/ Cloud Account"
										class="userNameInput" onfocus="setStyle(this.id);"
										onblur="removeStyle(this.id);" id="login_email" /> <span
										class="wrongName" id="login-email-tip"></span>
								</div>
								<div class="Name password">
									<div class="passwordIcon icon"></div>
									<input type="password" placeholder="Password"
										class="userNameInput passwordInput"
										onfocus="setStyle(this.id);" onblur="removeStyle(this.id);"
										id="login_pwd" /> <span class="wrongName" id="login-pwd-tip"></span>
								</div>
								<div class="remember">
									<input type="checkbox" class="chk" id="remember" /> <label
										for="remember"></label>
									<p class="rememberme">Remember me</p>
									<p class="forgot">
										<a class="forget-link"
											href="https://account.glodon.com/forgetInit" target="_blank">Forget
											password?</a>
									</p>
								</div>
								<div class="signIn">
									<input type="submit" value="Log In" onclick="login();" />
								</div>
								<div class="last">
									<span class="singUp"><a
										href="<c:url value='/auth/register'/>">Sign Up</a></span>
								</div>
							</div>
						</div>
				</c:when>
				<c:otherwise>
					<div class="middle">
						<div class="swiper-container smallSwiper">
							<div class="swiper-wrapper">
								<div class="swiper-slide bannerOne"></div>
								<div class="swiper-slide bannerTwo"></div>
								<div class="swiper-slide bannerThree"></div>
							</div>
							<div class="swiper-pagination"></div>
						</div>
						<div class="ceng cengTwo"></div>
						<div class="loginTwo">
							<div class="good">
								<span class="headPortrait"> </span> <span class="username">Hello!
									${sessionScope.currentUser.email}<br>
								</span> <span class="goodMoring"></span>
							</div>
							<span class="line"></span> <a href="${context}/project/index">
								<div class="projectList">
									<span class="list">Project list</span>
								</div>
							</a>
						</div>
					</div>
 
				</c:otherwise>
			</c:choose>
		</div>
		<div class="mainBottom">
			<div class="mainCenter">
				<span class="what">What is E-tender?</span>
				<div class="line"></div>
				<p>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;As a
					system to inquire about sub contracts for construction projects,<br>E-tender
					can reduce the cost of inquiries about sub cons and make it more
					efficient,<br> accurate and secure.
				</p>
			</div>
			<div class="features">
				<div class="efficient feature">
					<img
						src="<c:url value='/Images/etender/auth/login/efficient.png'/>" />
					<span>Efficient</span>
					<p>
						Compare &amp; Analyze quotations automatically;<br />3A mode for
						various locations.
					</p>
				</div>
				<div class="accurate feature">
					<img src="<c:url value='/Images/etender/auth/login/accurate.png'/>" />
					<span>Accurate</span>
					<p>Built-in formula to calculate autmatically for accuracy.</p>
				</div>
				<div class="secure feature">
					<img src="<c:url value='/Images/etender/auth/login/secure.png'/>" />
					<span>Secure</span>
					<p>Quotations from sub cons will not be disclosed.</p>
				</div>
				<div class="easy feature">
					<img src="<c:url value='/Images/etender/auth/login/easy.png'/>" />
					<span>Easy</span>
					<p>
						Easy to check progress in quotations;<br /> Easy to learn and
						use.
					</p>
				</div>
				<div class="environment feature">
					<img
						src="<c:url value='/Images/etender/auth/login/environment-friendly.png'/>" />
					<span>Environment-friendly</span>
					<p>Paperless office,reducing cost and protecting the
						environment.</p>
				</div>
			</div>
		</div>
	</div>
</div>
<a href="<c:url value='/help/index'/>">
	<div class="videoHelp">
		<span class="videoIcon">Video Help</span>
	</div>
</a>
<script type="text/javascript"
	src="<c:url value='/Scripts/thirdparty/swiper/swiper-3.3.1.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/arithmetic/des.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/auth/login.js'/>"></script>