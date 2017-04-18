package com.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.model.etender.User;

/**
 * 登录拦截器
 * 
 * @author liao.lh
 * 
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	private final static Logger logger = LoggerFactory
			.getLogger(LoginInterceptor.class);

	/**
	 * 免检查地址
	 */
	private List<String> uncheckUrls;

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

	private boolean isUncheckUrl(String requestUrl) {
		if (uncheckUrls != null) {
			for (String url : uncheckUrls) {
				if (requestUrl.startsWith(url)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestUrl = request.getRequestURI();
		logger.info("Current handle url:" + requestUrl);
		if (isUncheckUrl(requestUrl)) {
			return true;
		} else {
			// 判断本地是否已经在session中存放了登录后的用户
			HttpSession session = request.getSession();
			User user = (User) session
					.getAttribute(ConstantData.CONST_CAS_ASSERTION);	
			String cp = request.getContextPath();
			if (user == null) {				
				request.getRequestDispatcher("/auth/login").forward(request, response);
				return false;
			} else if (!requestUrl.startsWith(cp+"/auth")
					&& !requestUrl.startsWith(cp+"/user")
					&& (StringUtil.isNull(user.getCompany())
							|| StringUtil.isNull(user.getTelephone()) || StringUtil
								.isNull(user.getName()))) {
				// 授权和用户版块不做用户信息是否完善的验证				
				request.getRequestDispatcher("/user/supplyPersonalInfo").forward(request, response);
				return false;
			}
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
