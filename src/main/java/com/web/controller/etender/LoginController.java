package com.web.controller.etender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.APIUtil;
import com.thirdparty.glodon.yun.utils.UrlBuilder;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.model.etender.User;
import com.web.service.etender.ITradeService;
import com.web.service.etender.IUserService;

@Controller
@RequestMapping("/login")
public class LoginController extends CommonController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ITradeService tradeService;

	@RequestMapping(value = "/callback")
	public String callback(HttpServletRequest request,
			HttpServletResponse response) {
		// 1.从参数中读取ticket信息
		String ticket = request.getParameter("ticket");
		// 2.拼装读取已登录用户信息的URL
		String readUserUrl = UrlBuilder.buildCasValidateUrl(ticket);
		// 3.发送GET请求到cas_server获取返回值
		String casResponse = APIUtil.getResponseFromServer(readUserUrl);
		// 4.判断cas_server的返回值是否报错
		if (!checkCasResponse(casResponse)) {
			return "redirect:/error/index?cas=" + casResponse;
		}
		// 5.解析cas_server
		User user = extractFromCasResponse(casResponse);
		// 6.把用户数据保存到本地服务器的Session中
		if (user != null) {
			final HttpSession session = request.getSession();
			session.setAttribute(ConstantData.CONST_CAS_ASSERTION, user);
			session.setAttribute(ConstantData.LOGOUT_URL,
					UrlBuilder.buildLogoutUrl(request));
		}
		// 7.跳转到用户界面
		// 获取用户在未登录前，本来应该访问的受保护的页面
		String returnUrl = (String) request.getSession().getAttribute(
				ConstantData.CACHE_URL);
		if (!StringUtil.isNull(returnUrl)) {
			request.getSession().removeAttribute(ConstantData.CACHE_URL);
			return "redirect:" + returnUrl;
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/logout")
	public String logout(Model model, HttpSession session,
			HttpServletRequest request) {
		model.asMap().remove(ConstantData.CONST_CAS_ASSERTION);
		session.invalidate();
		return "redirect:/";
	}

	/**
	 * 判断是否正确返回
	 * 
	 * @param casResponse
	 * @return
	 */
	private boolean checkCasResponse(String casResponse) {
		// 若未成功返回，则表示校验失败
		if (StringUtil.isNull(casResponse)) {
			return false;
		}
		// 若返回失败提示，则表示校验失败
		Pattern failurePattern = Pattern.compile("authenticationFailure");
		Matcher failureMatcher = failurePattern.matcher(casResponse);
		if (failureMatcher.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 从返回的CasResponse中获取
	 * 
	 * @param casResponse
	 * @return
	 */
	private User extractFromCasResponse(String casResponse) {
		final User user = new User();
		Pattern casUserPattern = Pattern
				.compile("<cas:user>([0-9]*)</cas:user>");
		Pattern casAccessTokenPattern = Pattern
				.compile("<cas:access_token>(.*)</cas:access_token>");
		Matcher casUserMatcher = casUserPattern.matcher(casResponse);
		if (casUserMatcher.find()) {
			user.setUserid(casUserMatcher.group(1));
		}
		String accessToken = null;
		Matcher casAccessTokenMatcher = casAccessTokenPattern
				.matcher(casResponse);
		if (casAccessTokenMatcher.find()) {
			accessToken = casAccessTokenMatcher.group(1);
		}
		final String userid = user.getUserid();
		if (Long.parseLong(userid) <= 0) {
			return null;
		}
		final String at = accessToken;
		class GetUserThread extends Thread {
			public void run() {
				String url = "https://account.glodon.com/api/userinfo";
				String output = APIUtil.getResponseFromServer(url, at);
				JSONObject jo = JSONObject.parseObject(output);
				String username = jo.getString("username");
				if (!StringUtil.isNull(username)) {
					user.setName(username);
				}
				String email = jo.getString("email");
				if (!StringUtil.isNull(email)) {
					user.setEmail(email);
				}
				String mobile = jo.getString("mobile");
				if (!StringUtil.isNull(mobile)) {
					user.setTelephone(mobile);
				}
				String nickname = jo.getString("nickname");
				if (!StringUtil.isNull(nickname)) {
					user.setNickname(nickname);
				} else {
					user.setNickname(email);
				}
				userService.saveUser(user);
			}
		}
		new GetUserThread().start();
		return user;
	}
}
