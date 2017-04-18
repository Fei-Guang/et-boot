package com.web.controller.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.utils.StringUtil;
import com.web.controller.CommonController;

@Controller
@RequestMapping("/error")
public class ErrorController extends CommonController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		String type = request.getParameter("type");
		if (!StringUtil.isNull(type)) {
			request.setAttribute("type", type);
		}
		request.setAttribute("page", "error_index");
		return "etender/template";
	}

}
