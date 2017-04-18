package com.web.job;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.dao.etender.UserMapper;
import com.web.utils.AppContext;
import com.web.utils.WebUtil;

/**
 * Spring启动完成后执行初始化操作
 * 
 * @author liao.lh
 * 
 */
@Component("initialServlet")
public class InitialServlet extends HttpServlet {

	private static final long serialVersionUID = 2255067164384732989L;

	private static Logger logger = LoggerFactory
			.getLogger(InitialServlet.class);

	@Autowired
	private AppContext appContext;
	@Autowired
	private UserMapper userMapper;

	// TODO 把网站启动要执行的任务放在这里
	public void init(ServletConfig config) throws ServletException {
		logger.info("Get time zones......");
		List<String> timeZones = WebUtil.getTimeZones();
		appContext.getServletContext().setAttribute("timeZones", timeZones);
		logger.info("Get predefined trades......");
		Hashtable<String, String> predefinedTrades = WebUtil
				.getPredefinedTrade();
		appContext.getServletContext().setAttribute("predefinedTrades",
				predefinedTrades);
		// 把所有用户的邮箱设置到系统缓存里面，提高后续发邮件通知时的速度
		Set<String> emails = new HashSet<String>();
		List<String> es = userMapper.loadEmails();
		for (String e : es) {
			emails.add(e);
		}
		appContext.getServletContext().setAttribute("emails", emails);
	}

}
