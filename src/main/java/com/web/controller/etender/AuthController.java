package com.web.controller.etender;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.RegisterUtil;
import com.thirdparty.glodon.yun.model.CaptchaEntity;
import com.thirdparty.glodon.yun.utils.UrlBuilder;
import com.utils.Coder;
import com.utils.DateUtil;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.model.etender.User;
import com.web.service.etender.ITradeService;
import com.web.service.etender.IUserService;
import com.web.utils.arithmetic.DES;

@Controller
@RequestMapping("/auth")
public class AuthController extends CommonController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ITradeService tradeService;

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		String e = request.getParameter("e");
		String p = request.getParameter("p");
		String t = request.getParameter("t");
		String c = request.getParameter("c");
		if (!StringUtil.isNull(e) && !StringUtil.isNull(p)) {
			try {
				// 带登录条件
				boolean valid = true;
				if (!StringUtil.isNull(t)) {
					String time = Coder.decodeBase64(t);
					Date from = DateUtil.parseDate(time);
					Date end = DateUtil.addSeconds(from, 60);
					// 超过60秒，自动登录链接无效，不超过就执行登录
					valid = new Date().before(end);
				}
				if (valid) {
					String pwd = Coder.decodeBase64(p);
					String mail = Coder.decodeBase64(e);
					if ( pwd == null || mail == null  ){
						logger.error("user account is not correct:mail={} mpwd={},redirect to homepage", mail, pwd);
						return "redirect:/";
					}
					User user = new User();
					user.setEmail(mail);
					user.setPassword(pwd);
					int ret = ConstantData.LOGIN_INVALID;
					try {
						ret = userService.grant(user);
						if (ret == ConstantData.OK) {
							request.getSession().setAttribute(
									ConstantData.CONST_CAS_ASSERTION, user);
							String cp = request.getContextPath();							
							logger.info("redirect to {}", c);
							if (!StringUtil.isNull(c) && c.startsWith("http") ){
								URL u = new URL(c);									
								String path = u.getPath();
								if (u.getPath().startsWith(cp))										
									path = path.replaceFirst(cp, "");
								logger.info("TBQ BUG and redirect to {}", path);
								request.getRequestDispatcher(path).forward(
										request, response);																    
							 }else
								request.getRequestDispatcher("/"+c).forward(
										request, response);
							 return "etender/template";
						}
					} catch (Exception ex) {
						logger.error("Login:", ex);
						ret = ConstantData.ERROR;
					}
				}
			} catch (Exception ex) {

			}
		}
		request.setAttribute("page", "auth_login");
		return "etender/template";
	}
	
	
	@RequestMapping(value = "/v1/login")
	public String login_v1(HttpServletRequest request) {
		String e = request.getParameter("e");
		String p = request.getParameter("p");
		String t = request.getParameter("t");
		String c = request.getParameter("c");
		if (!StringUtil.isNull(e) && !StringUtil.isNull(p)) {
			try {
				// 带登录条件
				DES des = new DES();
				boolean valid = true;
				if (!StringUtil.isNull(t)) {
					String time = des.strDec(t, ConstantData.FirstKey,
							ConstantData.SecondKey, ConstantData.ThirdKey);
					Date from = DateUtil.parseDate(time);
					Date end = DateUtil.addSeconds(from, 60);
					// 超过60秒，自动登录链接无效，不超过就执行登录
					valid = new Date().before(end);
				}
				if (valid) {
					String pwd = des.strDec(p, ConstantData.FirstKey,
							ConstantData.SecondKey, ConstantData.ThirdKey);
					User user = new User();
					user.setEmail(e);
					user.setPassword(pwd);
					int ret = ConstantData.LOGIN_INVALID;
					try {
						ret = userService.grant(user);
						if (ret == ConstantData.OK) {
							request.getSession().setAttribute(
									ConstantData.CONST_CAS_ASSERTION, user);
							if (!StringUtil.isNull(c))
								return "redirect:" + c;
						}
					} catch (Exception ex) {
						logger.error("Login:", ex);
						ret = ConstantData.ERROR;
					}
				}
			} catch (Exception ex) {

			}
		}
		request.setAttribute("page", "auth_login");
		return "etender/template";
	}
	
	
	

	@RequestMapping(value = "/logout")
	public String logout(Model model, HttpSession session,
			HttpServletRequest request) {
		model.asMap().remove(ConstantData.CONST_CAS_ASSERTION);
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/yunLogin")
	public void yunLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		User user = (User) session
				.getAttribute(ConstantData.CONST_CAS_ASSERTION);
		if (user != null) {
			response.sendRedirect(UrlBuilder.buildLocalAppUrl(request));
		} else {
			// 将用户跳转到云账号登录界面前访问的受保护的页面缓存起来，以便于用户在云账号登录成功后，能回到先前的访问地址
			session.setAttribute(ConstantData.CACHE_URL, request
					.getRequestURL().toString());
			// 如果本地session尚未存放用户，则生成重定向URL，到云账户登录界面
			if (request.getServletPath().endsWith("miniuser.jsp")) {
				response.sendRedirect(UrlBuilder
						.buildCasMiniRedirectUrl(request));
			} else {
				response.sendRedirect(UrlBuilder.buildCasRedirectUrl(request));
			}
		}
	}

	@RequestMapping(value = "/grant")
	public void grant(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		boolean isLogin = true;
		String msg = "";
		DES des = new DES();
		String email = des.strDec(request.getParameter("email"),
				ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
		String pwd = des.strDec(request.getParameter("pwd"),
				ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
		User user = new User();
		user.setEmail(email);
		user.setPassword(pwd);
		int ret = ConstantData.LOGIN_INVALID;
		try {
			ret = userService.grant(user);
		} catch (Exception e) {
			logger.error("Grant:", e);
			ret = ConstantData.ERROR;
		}
		if (ret == ConstantData.OK) {
			request.getSession().setAttribute(ConstantData.CONST_CAS_ASSERTION,
					user);
		} else if (ret == ConstantData.LOGIN_FREEZE) {
			msg = "The user is freeze.";
			isLogin = false;
		} else if (ret == ConstantData.NAME_NOT_VERIFIED) {
			msg = "Inactive account. <a href='https://account.glodon.com/activate?accountName="
					+ email
					+ "' target='_blank'><u>Click here</u> to activate</a>.";
			isLogin = false;
		} else if (ret == ConstantData.EMAIL_INVALID) {
			msg = "The email is incorrect.";
			isLogin = false;
		} else if (ret == ConstantData.PASSWORD_INVALID) {
			msg = "The password is incorrect.";
			isLogin = false;
		} else {
			msg = "The server is busy now. Please try again later.";
			isLogin = false;
		}
		JSONObject jo = new JSONObject();
		jo.put("isLogin", isLogin);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 判断用户是否登录，此方法供异步加载页面的逻辑调用
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/isLogin")
	public void isLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		boolean isLogin = true;
		if (request.getSession().getAttribute(ConstantData.CONST_CAS_ASSERTION) == null) {
			isLogin = false;
		}
		JSONObject jo = new JSONObject();
		jo.put("isLogin", isLogin);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 判断登录用户信息是否完善
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/isUserInfoPerfect")
	public void isUserInfoPerfect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isPerfect = true;
		User user = (User) request.getSession().getAttribute(
				ConstantData.CONST_CAS_ASSERTION);
		if (user != null) {
			if (StringUtil.isNull(user.getCompany())
					|| StringUtil.isNull(user.getTelephone())
					|| StringUtil.isNull(user.getName())) {
				isPerfect = false;
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("isPerfect", isPerfect);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) {
		// 从广联云服务器中获取图片验证码
		CaptchaEntity captchaEntity = RegisterUtil.fetch();
		if (captchaEntity == null) {
			logger.error("captchaEntity is null");;
			return;
		} else
			logger.info("captchaEntity.getCaptchaKey()={}", captchaEntity.getCaptchaKey());
		
		// 将获取图片验证码时返回该验证码对应的key值保存在本地Session
		request.getSession().setAttribute(CAPTCHA_SESSION_KEY,
				captchaEntity.getCaptchaKey());
		OutputStream out = null;
		InputStream is = captchaEntity.getInputStream();
		response.setContentType("image/JPEG");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		int len = -1;
		byte[] b = new byte[1024];
		try {
			out = response.getOutputStream();
			while ((len = is.read(b, 0, 1024)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			logger.error("Captcha:", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}
	@RequestMapping(value = "/tipactive")
	public String tipactive(HttpServletRequest request) {
		request.setAttribute("page", "auth_tipactive");
		return "etender/template";
	}
	
	@RequestMapping(value = "/register")
	public String register(HttpServletRequest request) {
		request.setAttribute("page", "auth_register");
		return "etender/template";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isRegister = true;
		String msg = "";
		DES des = new DES();
		String email = des.strDec(request.getParameter("email"),
				ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
		String password = des.strDec(request.getParameter("pwd"),
				ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String company = request.getParameter("company");
		String address = request.getParameter("address");
		String website = request.getParameter("website");
		String captcha = request.getParameter("captcha");
		String captchaKey = (String) request.getSession().getAttribute(
				CAPTCHA_SESSION_KEY);
		String returnTo = UrlBuilder.buildLocalAppUrl(request);
		boolean result = RegisterUtil.check(captchaKey, captcha);
		if (result) {
			int ret = userService.register(email, password, name, phone,
					company, address, website, captcha, captchaKey, returnTo);
			if (ret == ConstantData.OK) {

			} else if (ret == ConstantData.EXISTED_EMAIL) {
				msg = "This email is already registered.";
				isRegister = false;
			} else if (ret == ConstantData.INVALID_EMAIL) {
				msg = "This email is invalid.";
				isRegister = false;
			} else if (ret == ConstantData.NAME_NOT_VERIFIED) {
				msg = "Your email is already registered, <a href='https://account.glodon.com/activate?accountName="
						+ email + "' target='_blank'>Verify</a> your email.";
				isRegister = false;
			} else {
				msg = "The server is busy now. Please try again later.";
				isRegister = false;
			}
		} else {
			msg = "The verification code is incorrect.";
			logger.error("The verification code is incorrect.");
			isRegister = false;
		}
		JSONObject jo = new JSONObject();
		jo.put("isRegister", isRegister);
		jo.put("msg", msg);
		logger.info(msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

}
