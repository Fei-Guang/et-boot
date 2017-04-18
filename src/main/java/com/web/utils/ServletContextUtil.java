package com.web.utils;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ServletContextUtil implements InitializingBean,
		ServletContextAware {

	private static ServletContext servletContext; // Spring应用上下文环境

	@Override
	public void setServletContext(ServletContext servletContext) {
		ServletContextUtil.servletContext = servletContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static String getWebRootPath() {
		String rootPath = servletContext.getRealPath("/");
		String os = System.getProperty("os.name").toLowerCase();
		if ( os.equals("linux"))
				return rootPath;
		else if (!rootPath.endsWith("\\")) {
			rootPath = rootPath + "\\";
		}
		return rootPath;
		
	}

}
