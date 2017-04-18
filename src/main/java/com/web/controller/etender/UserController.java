package com.web.controller.etender;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.model.etender.User;
import com.web.service.etender.IUserService;

@Controller
@RequestMapping("/user")
public class UserController extends CommonController {

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/supplyPersonalInfo")
	public String supplyPersonalInfo(HttpServletRequest request) {
		request.setAttribute("page", "user_supplyPersonalInfo");
		return "etender/template";
	}

	@RequestMapping(value = "/modifyPersonalInfo")
	public String modifyPersonalInfo(HttpServletRequest request) {
		request.setAttribute("page", "user_modifyPersonalInfo");
		return "etender/template";
	}

	@RequestMapping(value = "/supplyPersonalInfo", method = RequestMethod.POST)
	public void supplyPersonalInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isSupply = true;
		String msg = "";
		User user = (User) request.getSession().getAttribute(
				ConstantData.CONST_CAS_ASSERTION);
		if (user != null) {
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String company = request.getParameter("company");
			String address = request.getParameter("address");
			String website = request.getParameter("website");
			try {
				int ret = userService.supplyPersonalInfo(user.getUserid(),
						name, phone, company, address, website);
				if (ret == ConstantData.FAILED) {
					isSupply = false;
					msg = ConstantData.Error4Other;
				} else {
					user.setName(name);
					user.setTelephone(phone);
					user.setCompany(company);
					user.setAddress(address);
					user.setWebsite(website);
				}
			} catch (Exception e) {
				logger.error("SupplyPersonalInfo:", e);
				isSupply = false;
				msg = ConstantData.Error4Other;
			}
		} else {
			isSupply = false;
			msg = ConstantData.Error4Login;
		}
		JSONObject jo = new JSONObject();
		jo.put("isSupply", isSupply);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/modifyPersonalInfo", method = RequestMethod.POST)
	public void modifyPersonalInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isModify = true;
		String msg = "";
		User user = (User) request.getSession().getAttribute(
				ConstantData.CONST_CAS_ASSERTION);
		if (user != null) {
			String origPsw = request.getParameter("origPsw");
			String newPsw = request.getParameter("newPsw");
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String company = request.getParameter("company");
			String address = request.getParameter("address");
			String website = request.getParameter("website");
			try {
				int ret = userService.modifyPersonalInfo(user, origPsw, newPsw,
						name, phone, company, address, website);
				if (ret == ConstantData.FAILED) {
					isModify = false;
					msg = ConstantData.Error4Other;
				} else if (ret == ConstantData.PASSWORD_INVALID) {
					isModify = false;
					msg = ConstantData.Error4OriginalPsw;
				}
			} catch (Exception e) {
				logger.error("ModifyPersonalInfo:", e);
				isModify = false;
				msg = ConstantData.Error4Other;
			}
		} else {
			isModify = false;
			msg = ConstantData.Error4Login;
		}
		JSONObject jo = new JSONObject();
		jo.put("isModify", isModify);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}
}
