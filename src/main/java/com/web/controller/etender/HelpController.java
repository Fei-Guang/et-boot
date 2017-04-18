package com.web.controller.etender;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.controller.CommonController;

@Controller
@RequestMapping("/help")
public class HelpController extends CommonController {

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("page", "help_index");
		return "etender/template";
	}
}
