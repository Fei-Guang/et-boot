package com.web.controller.etender;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.utils.UrlBuilder;
import com.utils.DataUtil;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.etools.AttachMent;
import com.web.etools.EvaluationBillItem;
import com.web.etools.QuoteBillItem;
import com.web.etools.EvaluationSubProjectElement;
import com.web.etools.InquireRecord;
import com.web.etools.Project_Quote;
import com.web.etools.QuoteRecord;
import com.web.etools.QuoteSubProjectElement;
import com.web.etools.SubProject_Subcontractor;
import com.web.etools.UserQuoteInfo;
import com.web.model.etender.Project_Query;
import com.web.model.etender.Subproject;
import com.web.model.etender.Supplier;
import com.web.model.etender.User;
import com.web.service.etender.IProjectService;
import com.web.service.etender.ISupplierService;
import com.web.utils.WebUtil;

@Controller
@RequestMapping("/project")
public class ProjectController extends CommonController {

	@Autowired
	protected IProjectService projectService;
	@Autowired
	protected ISupplierService supplierService;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("page", "project_index");
		return "etender/template";
	}

	@RequestMapping(value = "/inquireList")
	public String inquireList(HttpServletRequest request) {
		// 如果用户不存在，在进入到这个页面之前，会被拦截器处理跳转到登录页面，所以这里不用考虑用户不存在，但是从程序健壮性考虑，这里还是加了判断
		User user = getCurrentUser(request);
		if (user != null) {
			List<Project_Query> queryProjects = projectService
					.loadQueryProject(user.getUserid());
			request.setAttribute("queryProjects", queryProjects);
		}
		return "etender/project/list/inquireList";
	}

	@RequestMapping(value = "/quoteList")
	public String quoteList(HttpServletRequest request) {
		// 如果用户不存在，在进入到这个页面之前，会被拦截器处理跳转到登录页面，所以这里不用考虑用户不存在，但是从程序健壮性考虑，这里还是加了判断
		User user = getCurrentUser(request);
		if (user != null) {
			List<Project_Quote> quoteProjects = projectService
					.loadQuoteProject(user);
			request.setAttribute("quoteProjects", quoteProjects);
		}
		return "etender/project/list/quoteList";
	}

	@RequestMapping(value = "/quoteRecord/{projectID}")
	public String quoteRecord(@PathVariable String projectID,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			String id = WebUtil.decrypt(projectID);
			List<QuoteRecord> records = projectService.loadQuoteRecord(
					Integer.parseInt(id), user.getEmail());
			request.setAttribute("records", records);
		}
		request.setAttribute("page", "project_quoteRecord");
		return "etender/template";
	}

	@RequestMapping(value = "/inquireRecord/{projectID}")
	public String inquireRecord(@PathVariable String projectID,
			HttpServletRequest request) {
		String id = WebUtil.decrypt(projectID);
		List<InquireRecord> records = projectService.loadInquireRecord(Integer
				.parseInt(id));
		request.setAttribute("records", records);
		request.setAttribute("page", "project_inquireRecord");
		return "etender/template";
	}

	@RequestMapping(value = "/inquire/{projectID}")
	public String inquire(@PathVariable String projectID,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			String id = WebUtil.decrypt(projectID);
			int proID = Integer.parseInt(id);
			List<Subproject> subProjects = projectService.loadSubProject(proID);
			request.setAttribute("subProjects", subProjects);
			String trade = (String) request.getSession().getAttribute(
					"inquire_trade");
			String level = (String) request.getSession().getAttribute(
					"inquire_level");
			List<Supplier> suppliers = supplierService.loadSupplier(
					user.getUserid(), trade, level);
			request.setAttribute("suppliers", suppliers);
			List<SubProject_Subcontractor> subProject_Subcontractors = projectService
					.getSubProjectSubcontractorMapping(user.getUserid(), proID,
							suppliers);
			request.setAttribute("subProject_Subcontractors",
					subProject_Subcontractors);
			Set<String> tradeFilters = supplierService.loadFilter(
					user.getUserid(), "trade");
			request.setAttribute("tradeFilters", tradeFilters);
			Set<String> levelFilters = supplierService.loadFilter(
					user.getUserid(), "level");
			request.setAttribute("levelFilters", levelFilters);
			request.getSession().setAttribute("currentProjectID", proID);
		}
		request.setAttribute("page", "project_inquire");
		return "etender/template";
	}

	@RequestMapping(value = "/doInquire")
	public void doInquire(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		String cp = request.getContextPath();
		User user = getCurrentUser(request);
		if (user != null) {
			String queryProjectID = request.getParameter("queryProjectID");
			if (StringUtil.isNull(queryProjectID)) {
				isPost = ConstantData.ERROR;
				msg = "Subproject id cannot be null.";
			} else {
				String projectID = WebUtil.decrypt(queryProjectID);
				boolean canEvaluation = projectService.canEvaluation(Integer
						.parseInt(projectID));
				if (canEvaluation) {
					jo.put("link", cp+"/project/evaluation/"
							+ queryProjectID);
				} else {
					jo.put("link", cp+"/project/inquire/" + queryProjectID);
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

	@RequestMapping(value = "/quote/{projectID}")
	public String quote(@PathVariable String projectID,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		String id = WebUtil.decrypt(projectID);
		int proID = Integer.parseInt(id);
		if (user != null) {
			// 更新接收时间
			String email = user.getEmail();
			//to do: just update time if not quoted
			projectService.updateSupplierReceivedTime(email, proID);
		}
		request.getSession().setAttribute("currentProjectID", proID);
		request.setAttribute("page", "project_quote");
		return "etender/template";
	}

	@RequestMapping(value = "/quote/export4tbq", method = RequestMethod.POST)
	public void export4tbq2quote(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int ret = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String treeData = request.getParameter("treeData");
			String tableData = request.getParameter("tableData");
			String summary = request.getParameter("summary");
			String totalAmount = request.getParameter("totalAmount");
			String sheetName = request.getParameter("sheetName");
			String exportReportName = projectService.export4tbq2quote(treeData,
					tableData, summary, totalAmount, sheetName);
			
			if (StringUtil.isNull(exportReportName)) {
				ret = ConstantData.FAILED;
				msg = "Export failed.";
				logger.error("exportReportName for {} failed: exportReportName is null", user.getEmail());
			} else {
				jo.put("exportReportName", exportReportName);
				logger.info("exportReportName for {}:{}", user.getEmail(), exportReportName);
			}
		}
		jo.put("ret", ret);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/quote/element4tbq")
	public String element4tbq2quote(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			List<QuoteSubProjectElement> elements = projectService
					.loadQuoteSubProjectElement(user.getEmail(), projectID);
			request.setAttribute("elements", elements);
		}
		return "etender/project/quote/tbq/element";
	}

	@RequestMapping(value = "/quote/billitem4tbq")
	public String billitem4tbq2quote(HttpServletRequest request) {
		User user = getCurrentUser(request);		
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			// 价格精度
			Project_Query project_Query = projectService.loadProject(projectID);
			byte accuracy = project_Query.getAccuracy();
			request.setAttribute("accuracy", accuracy);
			// 分包工程
			String subProjectID = request.getParameter("subProjectID");
			boolean isSubmit = projectService.isSubProjectSubmit(user,
					subProjectID);
			request.setAttribute("isSubmit", isSubmit);
			String descript = (String) request.getSession().getAttribute(
					"quote_descript");
			
			
			// 标识
			String eleID = request.getParameter("eleID");
			// 0代表分包工程，1代表章，2代表节
			String eleType = request.getParameter("eleType");
			List<QuoteBillItem> items = projectService.loadQuoteBillItem(
					user.getUserid(), user.getEmail(), projectID, eleID,
					eleType, accuracy, descript);
			request.setAttribute("items", items);
			double totalAmount = 0;
			for (QuoteBillItem item : items) {
				if (item.getBillitempid() == 0) {
					String netamount = item.getNetamount();
					if (!StringUtil.isNull(netamount)) {
						totalAmount += Double.parseDouble(netamount);
					}
				}
			}
			request.setAttribute("totalAmount", totalAmount);
			List<AttachMent> attachMents = projectService.loadAttachMents(
					user.getEmail(), subProjectID);
			request.setAttribute("attachMents", attachMents);
			
			
			// 描述
			Set<String> descriptFilters = projectService.loadFilter(
								user.getUserid(), "descript","quote");
			Map<String, Boolean> descriptFilterMap = new HashMap<String, Boolean>();
			if (descriptFilters != null && descriptFilters.size() > 0) {
				List<String> selectedDescript = StringUtil
									.getListByDescription(descript, ",");
				if (selectedDescript != null) {
					for (String descriptFilter : descriptFilters) {
									descriptFilterMap.put(descriptFilter,
											selectedDescript.contains(descriptFilter));
					}
				} else {
						for (String descriptFilter : descriptFilters) {
									descriptFilterMap.put(descriptFilter, false);
				        }
				}
			}
			request.setAttribute("descriptFilterMap", descriptFilterMap);
			
			
			
		}
		return "etender/project/quote/tbq/billitem";
	}

	@RequestMapping(value = "/updatebillitem4tbq2quote")
	public void updatebillitem4tbq2quote(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String subprojectid = request.getParameter("subprojectid");
			String billitemid = request.getParameter("billitemid");
			// 0代表单价，1代表合价，2代表备注
			String datatype = request.getParameter("datatype");
			// 数值
			String data = StringUtil.processNullStr(request
					.getParameter("data"));
			if (StringUtil.isNull(subprojectid)) {
				isPost = ConstantData.ERROR;
				msg = "Subproject id cannot be null.";
			} else if (StringUtil.isNull(billitemid)) {
				isPost = ConstantData.ERROR;
				msg = "Billitem id cannot be null.";
			} else {
				try {				
					
					logger.info("[quote] update supplierid={} billitemid={}  to {}", user.getUserid(), billitemid, data);
					int detailID = projectService.updateBillitem4tbq2quote(
								user, subprojectid, billitemid, datatype, data);
					if (detailID > 0) {
							jo.put("detailID", detailID);
					} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
					}
								
					
				} catch (Exception e) {
					
					e.printStackTrace();
					isPost = ConstantData.ERROR;
					msg = e.getMessage();
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

	@RequestMapping(value = "/submitQuote4tbq2subproject")
	public void submitQuote4tbq2subproject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String subProjectID = request.getParameter("subProjectID");
			if (StringUtil.isNull(subProjectID)) {
				isPost = ConstantData.ERROR;
				msg = "Subproject id cannot be null.";
			} else {
				try {
					isPost = projectService.updateSupplierSubmitTime(
							user.getEmail(), Integer.parseInt(subProjectID));
					if (isPost == ConstantData.OverTime) {
						msg = ConstantData.Error4OverTime;
					}
				} catch (Exception e) {
					logger.error("SubmitQuote4tbq2subproject:", e);
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

	@RequestMapping(value = "/updateChangeStatus")
	public void updateChangeStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String billitemid = request.getParameter("billitemid");
			// 0代表未修改，1代表有修改
			String isChange = request.getParameter("isChange");
			try {
				projectService.updateChangeStatus(billitemid, isChange);
			} catch (Exception e) {
				logger.error("UpdateChangeStatus:", e);
				isPost = ConstantData.ERROR;
				msg = ConstantData.Error4Other;
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

	@RequestMapping(value = "/updatebillitem4tbq2evaluation")
	public void updatebillitem4tbq2evaluation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			
			String supplierID = request.getParameter("supplierID");
			logger.info("supplierID={}", supplierID);
			String billitemid = request.getParameter("billitemid");
			// 0代表单价，1代表合价，2代表备注
			String datatype = request.getParameter("datatype");
			// 数值
			String data = StringUtil.processNullStr(request
					.getParameter("data"));
			if (StringUtil.isNull(supplierID)) {
				isPost = ConstantData.ERROR;
				msg = "Sub-Con id cannot be null.";
			} else if (StringUtil.isNull(billitemid)) {
				isPost = ConstantData.ERROR;
				msg = "Billitem id cannot be null.";
			} else {
				try {					
					
					logger.info("[evaluation] update supplierid={} billitemid={}  to {}", supplierID, billitemid, data);
					projectService.updateBillitem4tbq2evaluation(supplierID,
								billitemid, datatype, data);
					
					
				} catch (Exception e) {
					e.printStackTrace();					
					isPost = ConstantData.ERROR;
					msg = e.getMessage();
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

	@RequestMapping(value = "/updatediscount4tbq2evaluation")
	public void updatediscount4tbq2evaluation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String supplierID = request.getParameter("supplierID");
			String subProjectID = request.getParameter("subProjectID");
			// 0代表折扣绝对值，1代表折扣百分比
			String datatype = request.getParameter("datatype");
			// 数值
			String data = StringUtil.processNullStr(request
					.getParameter("data"));
			if (StringUtil.isNull(supplierID)) {
				isPost = ConstantData.ERROR;
				msg = "Sub-Con id cannot be null.";
			} else if (StringUtil.isNull(subProjectID)) {
				isPost = ConstantData.ERROR;
				msg = "Subproject id cannot be null.";
			} else {
				try {
					int discountID = projectService
							.updateDiscount4tbq2evaluation(supplierID,
									subProjectID, datatype, data);
					if (discountID > 0) {
						jo.put("discountID", discountID);
					} else {
						isPost = ConstantData.ERROR;
						msg = ConstantData.Error4Other;
					}
				} catch (Exception e) {
					logger.error("Updatediscount4tbq2evaluation:", e);
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

	@RequestMapping(value = "/adoptbillitem4tbq2evaluation")
	public void adoptbillitem4tbq2evaluation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String supplierID = request.getParameter("supplierID");
			
			String billitemid = request.getParameter("billitemid");
			String adoptNetRate = request.getParameter("adoptNetRate");
			String adoptNetAmount = request.getParameter("adoptNetAmount");
			String adoptRemark = request.getParameter("adoptRemark");
			if (StringUtil.isNull(supplierID)) {
				isPost = ConstantData.ERROR;
				msg = "Sub-Con id cannot be null.";
			} else if (StringUtil.isNull(billitemid)) {
				isPost = ConstantData.ERROR;
				msg = "Billitem id cannot be null.";
			} else {
				
				logger.info("[billitemid={}]adopted suppilier={} [adoptNetRate={} adoptNetAmount={}]", billitemid, supplierID, adoptNetRate, adoptNetAmount);
				try {
					projectService.adoptBillitem4tbq2evaluation(
							supplierID.trim(), adoptNetRate.trim(),
							adoptNetAmount.trim(), adoptRemark.trim(),
							billitemid.trim());
				} catch (Exception e) {
					logger.error("Adoptbillitem4tbq2evaluation:", e);
					isPost = ConstantData.ERROR;
					msg = e.getMessage();
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

	@RequestMapping(value = "/updateInquireProjectStatus2Evaluating")
	public void updateInquireProjectStatus2Evaluating(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String queryProjectID = request.getParameter("queryProjectID");
			if (StringUtil.isNull(queryProjectID)) {
				isPost = ConstantData.ERROR;
				msg = "QueryProject id cannot be null.";
			} else {
				try {
					String projectID = WebUtil.decrypt(queryProjectID);
					projectService
							.updateInquireProjectStatus2Evaluating(Integer
									.parseInt(projectID));
				} catch (Exception e) {
					logger.error("UpdateInquireProjectStatus2Evaluating:", e);
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

	@RequestMapping(value = "/evaluation/{projectID}")
	public String evaluation(@PathVariable String projectID,
			HttpServletRequest request) {
		String id = WebUtil.decrypt(projectID);
		int proID = Integer.parseInt(id);
		projectService.updateMainConReviewTime(proID);
		request.getSession().setAttribute("currentProjectID", proID);
		request.setAttribute("page", "project_evaluation");
		return "etender/template";
	}

	@RequestMapping(value = "/evaluation/export4tbq", method = RequestMethod.POST)
	public void export4tbq2evaluation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int ret = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String treeData = request.getParameter("treeData");
			String supplierData = request.getParameter("supplierData");
			String tableData = request.getParameter("tableData");
			String statisticData = request.getParameter("statisticData");
			String adopted = request.getParameter("adopted");
			String pte = request.getParameter("pte");
			String sheetName = request.getParameter("sheetName");
			String exportReportName = projectService.export4tbq2evaluation(
					treeData, supplierData, tableData, statisticData, adopted,
					pte, sheetName);
			if (StringUtil.isNull(exportReportName)) {
				ret = ConstantData.FAILED;
				msg = "Export failed.";
			} else {
				jo.put("exportReportName", exportReportName);
			}
		}
		jo.put("ret", ret);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/evaluation/element4tbq")
	public String element4tbq2evaluation(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			List<EvaluationSubProjectElement> elements = projectService
					.loadEvaluationSubProjectElement(projectID);
			request.setAttribute("elements", elements);
		}
		return "etender/project/evaluation/tbq/element";
	}

	@RequestMapping(value = "/evaluation/billitem4tbq")
	public String billitem4tbq2evaluation(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			// 价格精度
			Project_Query project_Query = projectService.loadProject(projectID);
			byte accuracy = project_Query.getAccuracy();			
			logger.info("accuracy={} projectID={}", accuracy, projectID);
			request.setAttribute("accuracy", accuracy);
			// 标识
			String eleID = request.getParameter("eleID");
			// 0代表分包工程，1代表章，2代表节
			String eleType = request.getParameter("eleType");
			String descript = null;
			String trade = null;
			String unit = null;
			String changed = (String) request.getSession().getAttribute(
					"evaluation_changed");
			if (!StringUtil.isNull(changed) && changed.equalsIgnoreCase("1")) {
				// 显示修改项与过滤项互斥，如果采用显示修改项，要把过滤项清除
				request.getSession().removeAttribute("evaluation_descript");
				request.getSession().removeAttribute("evaluation_trade");
				request.getSession().removeAttribute("evaluation_unit");
			} else {
				descript = (String) request.getSession().getAttribute(
						"evaluation_descript");
				trade = (String) request.getSession().getAttribute(
						"evaluation_trade");
				unit = (String) request.getSession().getAttribute(
						"evaluation_unit");
			}
			List<UserQuoteInfo> userQuoteInfos = new ArrayList<UserQuoteInfo>();
			List<EvaluationBillItem> items = projectService
					.loadEvaluationBillItem(user.getUserid(), projectID, eleID,
							eleType, userQuoteInfos, accuracy, descript, trade,
							unit, changed);
			// 描述
			Set<String> descriptFilters = projectService.loadFilter(
					user.getUserid(), "descript");
			Map<String, Boolean> descriptFilterMap = new HashMap<String, Boolean>();
			if (descriptFilters != null && descriptFilters.size() > 0) {
				List<String> selectedDescript = StringUtil
						.getListByDescription(descript, ",");
				if (selectedDescript != null) {
					for (String descriptFilter : descriptFilters) {
						descriptFilterMap.put(descriptFilter,
								selectedDescript.contains(descriptFilter));
					}
				} else {
					for (String descriptFilter : descriptFilters) {
						descriptFilterMap.put(descriptFilter, false);
					}
				}
			}
			request.setAttribute("descriptFilterMap", descriptFilterMap);
			// 专业
			Set<String> tradeFilters = projectService.loadFilter(
					user.getUserid(), "trade");
			Map<String, Boolean> tradeFilterMap = new HashMap<String, Boolean>();
			if (tradeFilters != null && tradeFilters.size() > 0) {
				List<String> selectedTrade = StringUtil.getListByDescription(
						trade, ",");
				if (selectedTrade != null) {
					for (String tradeFilter : tradeFilters) {
						tradeFilterMap.put(tradeFilter,
								selectedTrade.contains(tradeFilter));
					}
				} else {
					for (String tradeFilter : tradeFilters) {
						tradeFilterMap.put(tradeFilter, false);
					}
				}
			}
			request.setAttribute("tradeFilterMap", tradeFilterMap);
			// 单位
			Set<String> unitFilters = projectService.loadFilter(
					user.getUserid(), "unit");
			Map<String, Boolean> unitFilterMap = new HashMap<String, Boolean>();
			if (unitFilters != null && unitFilters.size() > 0) {
				List<String> selectedUnit = StringUtil.getListByDescription(
						unit, ",");
				if (selectedUnit != null) {
					for (String unitFilter : unitFilters) {
						unitFilterMap.put(unitFilter,
								selectedUnit.contains(unitFilter));
					}
				} else {
					for (String unitFilter : unitFilters) {
						unitFilterMap.put(unitFilter, false);
					}
				}
			}
			request.setAttribute("unitFilterMap", unitFilterMap);
			request.setAttribute("items", items);
			request.setAttribute("quoteInfos", userQuoteInfos);
		}
		return "etender/project/evaluation/tbq/billitem";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String name = request.getParameter("name");
			if (StringUtil.isNull(name)) {
				isPost = ConstantData.ERROR;
				msg = "Project names cannot be null.";
			} else {
				Project_Query project = new Project_Query();
				project.setName(name);
				project.setUserid(user.getUserid());
				project.setCreateby(user.getNickname());
				try {
					int projectID = projectService.createProject(project);
					if (projectID > 0) {
						jo.put("projectID", projectID);
					} else {
						if (projectID == ConstantData.NameEXISTED) {
							isPost = ConstantData.NameEXISTED;
							msg = "The project name already exists.";
						} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
						}
					}
				} catch (Exception e) {
					logger.error("createProject:", e);
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

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			if (StringUtil.isNull(name)) {
				isPost = ConstantData.ERROR;
				msg = "Project names cannot be null.";
			} else {
				id = WebUtil.decrypt(id);
				int proID = Integer.parseInt(id);
				Project_Query project = new Project_Query();
				project.setQueryprojectid(proID);
				project.setName(name);
				project.setUpdateby(user.getNickname());
				project.setUpdatetime(new Date());
				try {
					// 工程列表修改由于只允许修改名称，因此这里直接判断，不经过数据库，减少数据查询占用的时间
					if (projectService.loadProject(proID).getName()
							.equalsIgnoreCase(name)) {
						// 未改动
						jo.put("projectID", id);
					} else {
						int projectID = projectService.editProject(project);
						if (projectID > 0) {
							jo.put("projectID", projectID);
						} else {
							isPost = ConstantData.ERROR;
							if (projectID == ConstantData.NameEXISTED) {
								isPost = ConstantData.NameEXISTED;
								msg = "The project name already exists.";
							} else {
								isPost = ConstantData.ERROR;
								msg = ConstantData.Error4Other;
							}
						}
					}
				} catch (Exception e) {
					logger.error("editProject:", e);
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

	@RequestMapping(value = "/deleteInquireProject/{projectID}")
	public void deleteInquireProject(@PathVariable String projectID,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isPost = false;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			if (!StringUtil.isNull(projectID)) {
				String id = WebUtil.decrypt(projectID);
				isPost = projectService.deleteInquireProjectByLogic(Integer
						.parseInt(id));
			}
		}
		jo.put("isPost", isPost);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/deleteQuoteProject/{projectID}")
	public void deleteQuoteProject(@PathVariable String projectID,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isPost = false;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			if (!StringUtil.isNull(projectID)) {
				String id = WebUtil.decrypt(projectID);
				isPost = projectService.deleteQuoteProjectByLogic(
						Integer.parseInt(id), user.getEmail());
			}
		}
		jo.put("isPost", isPost);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/info/{projectID}")
	public void info(@PathVariable Integer projectID,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			Project_Query project = projectService.loadProject(projectID);
			if (project != null) {
				isPost = true;
				jo.put("name", project.getName());
				jo.put("code", project.getCode());
				jo.put("type", project.getType());
				jo.put("supervisor", project.getSupervisor());
				jo.put("floorArea", project.getFloorarea());
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

	@RequestMapping(value = "/isNameValid")
	public void isNameValid(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = request.getParameter("fieldValue");
		JSONArray ja = new JSONArray();
		String fieldId = request.getParameter("fieldId");
		ja.add(fieldId);
		// 如果名称存在，那么需要提示错误，就把验证结果放在数组中返回
		User user = getCurrentUser(request);
		if (user != null) {
			ja.add(projectService.validName(name, user.getUserid()) == ConstantData.OK);
		} else {
			ja.add(false);
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(ja.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/saveSubContractRecord")
	public void saveSubContractRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String subProjectID = request.getParameter("subProjectID");
			isPost = projectService.saveSubContractRecord(user.getUserid(),
					projectID, subContract, subProjectID);
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

	@RequestMapping(value = "/saveTimeZoneRecord")
	public void saveTimeZoneRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String timeZone = request.getParameter("timeZone");
			isPost = projectService.saveTimeZoneRecord(user.getUserid(),
					projectID, subContract, timeZone);
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

	@RequestMapping(value = "/saveEndTimeRecord")
	public void saveEndTimeRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String endTime = request.getParameter("endTime");
			isPost = projectService.saveEndTimeRecord(user.getUserid(),
					projectID, subContract, endTime);
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

	@RequestMapping(value = "/saveSupplierRecord")
	public void saveSupplierRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String supplierID = request.getParameter("supplierID");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			isPost = projectService.saveSupplierRecord(user.getUserid(),
					projectID, subContract, supplierID, name, email);
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

	@RequestMapping(value = "/deleteSupplierRecord")
	public void deleteSupplierRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String supplierID = request.getParameter("supplierID");
			isPost = projectService.deleteSupplierRecord(user.getUserid(),
					projectID, subContract, supplierID);
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

	@RequestMapping(value = "/saveSupplierAttachRecord")
	public void saveSupplierAttachRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String fileName = request.getParameter("fileName");
			String attachPath = request.getParameter("attachPath");
			String subContract = request.getParameter("subContract");
			String supplierID = request.getParameter("supplierID");
			int num = projectService.saveSupplierAttachRecord(user.getUserid(),
					projectID, fileName, attachPath, subContract, supplierID);
			if ( num < 0 ){
				logger.error("saveSupplierAttachRecord failed");
				isPost = false;
			}
			jo.put("attachs", num);
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

	@RequestMapping(value = "/deleteSupplierAttachRecord")
	public void deleteSupplierAttachRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String attachPath = request.getParameter("attachPath");
			String subContract = request.getParameter("subContract");
			String supplierID = request.getParameter("supplierID");
			isPost = projectService.deleteSupplierAttachRecord(
					user.getUserid(), projectID, attachPath, subContract,
					supplierID);
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
    
	/**
	 * 在发送询价之前，从Temp//inquire/<userid>/<projectID>.json
	 * 读取用户的操作记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/recoveryInquireStatus")
	public void recoveryInquireStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		JSONObject res = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String lastSetting = request.getParameter("lastSetting");
			JSONObject jo = projectService.getRecord(user.getUserid(),
					projectID);
			if (StringUtil.isNull(lastSetting)) {
				lastSetting = jo.getString("lastSetting");
			}
			if (!StringUtil.isNull(lastSetting)) {
				res.put("lastSetting", lastSetting);
				JSONArray records = jo.getJSONArray("records");
				int size = records.size();
				for (int i = 0; i < size; i++) {
					JSONObject record = records.getJSONObject(i);
					// 设置对应分包工程的设置选中项
					if (record.getString("subContract").equalsIgnoreCase(
							lastSetting)) {
						// 找到对应分包工程节点，取对应分包商节点
						res.put("timeZone", record.getString("timeZone"));
						res.put("endTime", record.getString("endTime"));
						res.put("subcontractors",
								record.getJSONArray("subcontractors"));
						logger.info("record.getJSONArray(\"subcontractors\")={}", record.getJSONArray("subcontractors"));
						break;
					}
				}
			}
			// 专业
			String trade = (String) request.getSession().getAttribute(
					"inquire_trade");
			if (!StringUtil.isNull(trade)) {
				String[] ts = trade.split(",");
				JSONArray ja = new JSONArray();
				for (String t : ts) {
					ja.add(t);
				}
				res.put("trades", ja);
			}
			// 资质
			String level = (String) request.getSession().getAttribute(
					"inquire_level");
			if (!StringUtil.isNull(level)) {
				String[] ls = level.split(",");
				JSONArray ja = new JSONArray();
				for (String l : ls) {
					ja.add(l);
				}
				res.put("levels", ja);
			}
		} else {
			isPost = false;
		}
		res.put("isPost", isPost);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(res.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/attach")
	public String attach(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String subContract = request.getParameter("subContract");
			String supplierID = request.getParameter("supplierID");
			List<AttachMent> attachMents = projectService.loadAttach(
					user.getUserid(), projectID, subContract, supplierID);
			request.setAttribute("attachNo", attachMents.size());
			request.setAttribute("attachMents", attachMents);
		}
		return "etender/project/attach";
	}

	@RequestMapping(value = "/sendSubProject", method = RequestMethod.POST)
	public void sendSubProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			int projectID = (Integer) request.getSession().getAttribute(
					"currentProjectID");
			String returnTo = UrlBuilder.buildLocalAppUrl(request);
			try {
				isPost = projectService.sendSubProject(user, projectID,
						returnTo);
				if (!isPost) {
					logger.error("projectService.sendSubProject return flase");
					msg = ConstantData.Error4Other;
				}
					
				
			} catch (Exception e) {
				isPost = false;
				msg = ConstantData.Error4Other;
				e.printStackTrace();
			}
		} else {
			logger.error("unauthorized use");
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

	@RequestMapping(value = "/deleteSubProject")
	public void deleteSubProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String subprojectid = request.getParameter("subProjectID");
			if (StringUtil.isNull(subprojectid)) {
				isPost = ConstantData.ERROR;
				msg = "Subproject id cannot be null.";
			} else {
				try {
					projectService.deleteSubProject(subprojectid);
				} catch (Exception e) {
					logger.error("DeleteSubProject:", e);
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

}
