package com.web.controller.etender;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.model.etender.Supplier;
import com.web.model.etender.User;
import com.web.service.etender.ISupplierService;
import com.web.utils.WebUtil;

@Controller
@RequestMapping("/supplier")
public class SupplierController extends CommonController {

	@Autowired
	protected ISupplierService supplierService;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("page", "supplier_index");
		User user = getCurrentUser(request);
		if (user != null) {
			List<Supplier> suppliers = supplierService.loadSupplier(
					user.getUserid(), "", "");
			request.setAttribute("suppliers", suppliers);
		}
		return "etender/template";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String trade = request.getParameter("trade");
			String level = request.getParameter("level");
			String address = request.getParameter("address");
			String contacts = request.getParameter("contacts");
			String telephone = request.getParameter("telephone");
			if (StringUtil.isNull(name)) {
				isPost = ConstantData.ERROR;
				msg = "Sub con names cannot be null.";
			} else if (StringUtil.isNull(email)) {
				isPost = ConstantData.ERROR;
				msg = "Emails cannot be null.";
			} else {
				Supplier supplier = new Supplier();
				supplier.setName(name);
				supplier.setEmail(email);
				supplier.setTrade(trade);
				supplier.setLevel(level);
				supplier.setAddress(address);
				supplier.setContacts(contacts);
				supplier.setTelephone(telephone);
				supplier.setUserid(user.getUserid());
				supplier.setCreateby(user.getNickname());
				try {
					int supplierID = supplierService.addSupplier(supplier);
					if (supplierID > 0) {
						jo.put("supplierID", supplierID);
					} else {
						if (supplierID == ConstantData.NameEXISTED) {
							isPost = ConstantData.NameEXISTED;
							msg = "The sub con name already exists.";
						} else if (supplierID == ConstantData.EmailEXISTED) {
							isPost = ConstantData.EmailEXISTED;
							msg = "The email already exists.";
						} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
						}
					}
				} catch (Exception e) {
					logger.error("addSupplier:", e);
					isPost = ConstantData.ERROR;
					msg = ConstantData.Error4Other;
				}
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/delete/{supplierID}")
	public void delete(@PathVariable Integer supplierID,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			try {
				int ret = supplierService.deleteSupplierByLogic(
						user.getUserid(), supplierID);
				if (ret == ConstantData.FAILED) {
					isPost = false;
				}
			} catch (Exception e) {
				logger.error("DeleteSupplierByLogic:", e);
				isPost = false;
			}
		} else {
			isPost = false;
		}
		jo.put("isPost", isPost);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public void batchDelete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String data = request.getParameter("data");
			if (!StringUtil.isNull(data)) {
				List<String> ids = Arrays.asList(data.split(","));
				int ret = supplierService.batchDeleteSupplierByLogic(
						user.getUserid(), ids);
				if (ret == ConstantData.FAILED) {
					isPost = false;
				}
			}
		} else {
			isPost = false;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String userid = user.getUserid();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String trade = request.getParameter("trade");
			String level = request.getParameter("level");
			String address = request.getParameter("address");
			String contacts = request.getParameter("contacts");
			String telephone = request.getParameter("telephone");
			if (StringUtil.isNull(name)) {
				isPost = ConstantData.ERROR;
				msg = "Sub con names cannot be null.";
			} else if (StringUtil.isNull(email)) {
				isPost = ConstantData.ERROR;
				msg = "Emails cannot be null.";
			} else {
				Supplier supplier = new Supplier();
				supplier.setSupplierid(Integer.parseInt(id));
				supplier.setName(name);
				supplier.setEmail(email);
				supplier.setTrade(trade);
				supplier.setLevel(level);
				supplier.setAddress(address);
				supplier.setContacts(contacts);
				supplier.setTelephone(telephone);
				supplier.setUpdateby(user.getNickname());
				supplier.setUpdatetime(new Date());
				supplier.setUserid(userid);
				try {
					int supplierID = supplierService.editSupplier(userid,
							supplier);
					if (supplierID > 0) {
						jo.put("supplierID", supplierID);
					} else {
						if (supplierID == ConstantData.NameEXISTED) {
							isPost = ConstantData.NameEXISTED;
							msg = "The sub con name already exists.";
						} else if (supplierID == ConstantData.EmailEXISTED) {
							isPost = ConstantData.EmailEXISTED;
							msg = "The email already exists.";
						} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
						}
					}
				} catch (Exception e) {
					logger.error("editSupplier:", e);
					isPost = ConstantData.ERROR;
					msg = ConstantData.Error4Other;
				}
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	//@RequestMapping(value = "/batchEdit", method = RequestMethod.POST, headers="Accept=application/json,content-type=application/x-www-form-urlencoded")
	@RequestMapping(value = "/batchEdit", method = RequestMethod.POST)
	public void batchEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		WebUtil.headers(request);
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String userid = user.getUserid();
			String data = request.getParameter("data");
			StringBuffer sb = new StringBuffer();
			logger.info("sb={}",data);
			JSONArray ja = JSON.parseArray(data);
			int size = ja.size();
			for (int i = 0; i < size; i++) {
				JSONObject js = ja.getJSONObject(i);
				int id = js.getIntValue("supplierid");
				String name = js.getString("name");
				String email = js.getString("email");
				String trade = js.getString("trade");
				String level = js.getString("level");
				String address = js.getString("address");
				String contacts = js.getString("contacts");
				String telephone = js.getString("telephone");
				
				logger.info("name={}", name);
				if (StringUtil.isNull(name)) {
					isPost = ConstantData.ERROR;
					msg = "Sub con names cannot be null.";
				} else if (StringUtil.isNull(email)) {
					isPost = ConstantData.ERROR;
					msg = "Emails cannot be null.";
				} else {
					Supplier supplier = new Supplier();
					supplier.setName(name);
					supplier.setEmail(email);
					supplier.setTrade(trade);
					supplier.setLevel(level);
					supplier.setAddress(address);
					supplier.setContacts(contacts);
					supplier.setTelephone(telephone);
					supplier.setUserid(userid);
					try {
						int supplierID = 0;
						if (id > 0) {
							// 编辑
							supplier.setSupplierid(id);
							supplier.setUpdateby(user.getNickname());
							supplier.setUpdatetime(new Date());
							supplierID = supplierService.editSupplier(userid,
									supplier);
						} else {
							// 添加
							supplier.setUserid(user.getUserid());//suppliers created by current user
							supplier.setCreateby(user.getNickname());
							supplierID = supplierService.addSupplier(supplier);
						}
						if (supplierID <= 0) {
							if (supplierID == ConstantData.NameEXISTED) {
								isPost = ConstantData.NameEXISTED;
								msg = "The sub con name already exists.";
							} else if (supplierID == ConstantData.EmailEXISTED) {
								isPost = ConstantData.EmailEXISTED;
								msg = "The email already exists.";
							} else {
								isPost = ConstantData.ERROR;
								msg = ConstantData.Error4Other;
							}
						}
					} catch (Exception e) {
						logger.error("batchEdit:", e);
						isPost = ConstantData.ERROR;
						sb.append(name).append(",");
						msg = ConstantData.Error4Other;
					}
				}
			}
			if (sb.length() > 0) {
				// TODO 批量处理时，失败的供应商名称
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/importSupplier")
	public void importSupplier(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String attachPath = request.getParameter("data");
			if (!StringUtil.isNull(attachPath)) {
				msg = supplierService.importSupplier(attachPath, user);
				if (msg.equalsIgnoreCase(ConstantData.Successed)) {
					isPost = true;
				} else {
					isPost = false;
				}
			} else {
				isPost = false;
				msg = "The data lost.";
			}
		} else {
			isPost = false;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

}
