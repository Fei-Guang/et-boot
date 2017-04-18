package com.web.service.etender.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.LoginUtil;
import com.thirdparty.glodon.yun.RegisterUtil;
import com.thirdparty.glodon.yun.model.InviteUserDomain;
import com.thirdparty.glodon.yun.model.InvitedUser;
import com.utils.ArrayUtil;
import com.utils.DataUtil;
import com.utils.DateUtil;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.utils.office.ExcelUtil;
import com.utils.office.excel.ColorConverter;
import com.utils.office.excel.WebColor;
import com.web.common.ConstantData;
import com.web.etools.AttachMent;
import com.web.etools.EvaluationBillItem;
import com.web.etools.QuoteBillItem;
import com.web.etools.Element;
import com.web.etools.EvaluationSubProjectElement;
import com.web.etools.InquireRecord;
import com.web.etools.NoticeEmail;
import com.web.etools.Project_Quote;
import com.web.etools.QuoteInfo;
import com.web.etools.QuoteRecord;
import com.web.etools.QuoteSubProjectElement;
import com.web.etools.SubProject_Subcontractor;
import com.web.etools.UserQuoteInfo;
import com.web.model.etender.Project_Query;
import com.web.model.etender.Quoted_Adopt4tbq;
import com.web.model.etender.Quoted_Billitem4tbq;
import com.web.model.etender.Quoted_Detail4tbq;
import com.web.model.etender.Quoted_Discount4tbq;
import com.web.model.etender.Quoted_Element4tbq;
import com.web.model.etender.Subproject;
import com.web.model.etender.Supplier;
import com.web.model.etender.Supplier_Subproject;
import com.web.model.etender.User;
import com.web.service.etender.IProjectService;
import com.web.utils.ServletContextUtil;
import com.web.utils.WebUtil;
import com.web.utils.arithmetic.DES;

@Service("projectService")
public class ProjectService extends BaseService implements IProjectService {

	/**
	 * 保存记录
	 * 
	 * @param userid
	 * @param jo
	 * @return
	 */
	private synchronized boolean saveRecord(String userid, JSONObject jo) {
		String recordPath = WebUtil.getTempPath() + "/evaluation/" + userid
				+ "/filter.json";
		return FileUtil.newFile(recordPath, jo.toJSONString());
	}
	
	
	/**
	 * 保存记录
	 * 
	 * @param userid
	 * @param jo
	 * @return
	 */
	private synchronized boolean saveRecord(String userid, JSONObject jo, String source) {
		String recordPath = WebUtil.getTempPath() + "/"+ source+ "/" + userid
				+ "/filter.json";
		return FileUtil.newFile(recordPath, jo.toJSONString());
	}

	/**
	 * 获取记录
	 * 
	 * @param userid
	 * @return
	 */
	private synchronized JSONObject getRecord(String userid) {
		String recordPath = WebUtil.getTempPath() + "/evaluation/" + userid
				+ "/filter.json";
		String json = FileUtil.readContent(recordPath, true);
		if (StringUtil.isNull(json)) {
			return new JSONObject();
		} else {
			return JSON.parseObject(json);
		}
	}
	
	
	/**
	 * 获取记录
	 * 
	 * @param userid
	 * @return
	 */
	private synchronized JSONObject getRecord(String userid, String source) {
		String recordPath = WebUtil.getTempPath() + "/"+ source + "/" + userid
				+ "/filter.json";
		String json = FileUtil.readContent(recordPath, true);
		if (StringUtil.isNull(json)) {
			return new JSONObject();
		} else {
			return JSON.parseObject(json);
		}
	}
	
	

	/**
	 * 保存过滤条件
	 * 
	 * @param userid
	 * @param ks
	 * @param key
	 */
	private boolean saveFilter(String userid, List<String> ks, String key) {
		JSONObject jo = getRecord(userid);
		JSONObject filter = jo.getJSONObject(key);
		if (filter == null) {
			filter = new JSONObject();
			jo.put(key, filter);
		}
		for (String k : ks) {
			filter.put(k, "");
		}
		return saveRecord(userid, jo);
	}
	
	
	/**
	 * 保存过滤条件
	 * 
	 * @param userid
	 * @param ks
	 * @param key
	 */
	private boolean saveFilter(String userid, List<String> ks, String key, String source) {
		
		if ( source.equals("quote") ){
			JSONObject jo = getRecord(userid, source);
			JSONObject filter = jo.getJSONObject(key);
			if (filter == null) {
				filter = new JSONObject();
				jo.put(key, filter);
			}
			for (String k : ks) {
				filter.put(k, "");
			}
			return saveRecord(userid, jo, source);
		}
		return false;
	}

	@Override
	public Set<String> loadFilter(String userid, String key) {
		JSONObject jo = getRecord(userid);
		JSONObject filter = jo.getJSONObject(key);
		if (filter != null) {
			return filter.keySet();
		} else {
			return null;
		}
	}
	
	@Override
	public Set<String> loadFilter(String userid, String key, String source){
		if ( source.equals("quote") ){			
			JSONObject jo = getRecord(userid, source);
			JSONObject filter = jo.getJSONObject(key);
			if (filter != null) {
				return filter.keySet();
			} else {
				return null;
			}
		}		
		return null;
			
	}

	/**
	 * 获取最后结束的分包工程
	 * 
	 * @param selectCondition
	 * @return
	 */
	private Subproject loadLastEndSubProject(
			HashMap<String, Object> selectCondition) {
		// TODO 先用数据库获取时间最后的分包工程，后面需要按照时区进行转换，统一转换为东八区时间再进行比较
		return subprojectMapper.loadLastEndSubProject(selectCondition);
	}

	/**
	 * 判断分包工程是否结束报价
	 * 
	 * @param subproject
	 * @return
	 */
	private boolean isEndQuote(Subproject subproject) {
		Date endtime = subproject.getEndtime();
		// TODO 后面需要按照时区进行转换，统一转换为东八区时间再进行判断
		if (endtime.before(new Date())) {
			// 当前时间已经超过报价截止时间
			return true;
		}
		return false;
	}

	@Override
	public int createProject(Project_Query project) {
		String name = project.getName();
		String userid = project.getUserid();
		List<String> projectNames = project_QueryMapper
				.selectProjectNameByUserID(userid);
		for (String projectName : projectNames) {
			if (projectName.equalsIgnoreCase(name)) {
				return ConstantData.NameEXISTED;
			}
		}
		int projectid = ConstantData.ERROR;
		if (project_QueryMapper.insertSelective(project) == 1) {
			projectid = project.getQueryprojectid();
		}
		return projectid;
	}

	@Override
	public int validateProjectName(String name) {
		if (project_QueryMapper.projectNameExisted(name)) {
			return 1;
		}
		return 0;
	}

	@Override
	public byte getQueryProjectStatus(int queryProjectID) {
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("queryProjectID", queryProjectID);
		selectCondition.put("logicDelete", 0);
		// TODO 五种状态：Not
		// Inquired、Inquiring、Inquired、Evaluating、Evaluated取状态最慢的分包工程状态，
		// 这个逻辑导致目前数据表中的状态字段没有意义，未来酌情考虑删除数据表中的状态字段或者将状态字段作为它用
		List<Integer> quoteSubProjectIDs = subprojectMapper
				.loadQuoteSubProjectIDs(selectCondition);
		if (quoteSubProjectIDs.size() == 0) {
			// 没有分包工程，代表属于新建的工程
			return 0;
		} else {
			// TODO 评标结束要等待回填之后，才能确定该状态，该逻辑在以后需要完善
			byte status = 4;
			for (Integer quoteSubProjectID : quoteSubProjectIDs) {
				// 如果该分包工程还没有发送记录，那么就是未询价，属于最晚状态，直接跳出循环
				int sendRecordCount = supplier_SubprojectMapper
						.loadSendRecordCount(quoteSubProjectID);
				if (sendRecordCount == 0) {
					status = 0;
					break;
				} else {
					Subproject subproject = subprojectMapper
							.selectByPrimaryKey(quoteSubProjectID);
					// 已经有发送文件
					if (isEndQuote(subproject)) {
						// 当前时间已经超过报价截止时间
						int reviewTimeCount = supplier_SubprojectMapper
								.loadReviewTimeCount(quoteSubProjectID);
						if (reviewTimeCount == 0) {
							// 没查看，询价结束
							if (ConstantData.inquired < status) {
								status = ConstantData.inquired;
							}
						} else {
							// 已经查看，评标中
							if (ConstantData.evaluating < status) {
								status = ConstantData.evaluating;
							}
						}
					} else {
						// 当前时间没有超过报价截止时间，调整状态为询价中
						if (ConstantData.inquiring < status) {
							status = ConstantData.inquiring;
						}
					}
				}
			}
			return status;
		}
	}

	@Override
	public List<Project_Query> loadQueryProject(String userid) {
		List<Project_Query> queryProjects = project_QueryMapper
				.loadQueryProject(userid);
		for (Project_Query queryProject : queryProjects) {
			// 询价中状态才需要在展示工程列表时做调整
			int queryProjectID = queryProject.getQueryprojectid();
			queryProject.setStatus(getQueryProjectStatus(queryProjectID));
		}
		return queryProjects;
	}

	@Override
	public List<Project_Quote> loadQuoteProject(User user) {
		List<Project_Quote> project_Quotes = new ArrayList<Project_Quote>();
		// 第一步，获取用户分包工程标识信息
		List<Integer> subProjectIDs = supplier_SubprojectMapper
				.loadSubProjectIDs(user.getEmail());
		if (subProjectIDs.size() > 0) {
			// 第二步，根据分包工程信息，归类获得总包工程标识信息
			List<Integer> queryProjectIDs = subprojectMapper
					.loadQueryProjectIDs(subProjectIDs);
			if (queryProjectIDs.size() > 0) {
				// 第三步，根据总包工程标识信息，依次获取各个报价工程信息
				for (Integer queryProjectID : queryProjectIDs) {
					Project_Quote project_Quote = new Project_Quote();
					Project_Query project_Query = project_QueryMapper
							.selectByPrimaryKey(queryProjectID);
					project_Quote.setQueryprojectid(queryProjectID);
					project_Quote.setName(project_Query.getName());
					project_Quote.setCreateDate(project_Query.getCreatetime());
					User mainCon = userMapper.selectByPrimaryKey(project_Query
							.getUserid());
					project_Quote.setMainCon(mainCon.getCompany());
					project_Quote.setContacts(mainCon.getName());
					project_Quote.setTelephone(mainCon.getTelephone());
					HashMap<String, Object> selectCondition = new HashMap<String, Object>();
					selectCondition.put("subProjectIDs", subProjectIDs);
					selectCondition.put("queryProjectID", queryProjectID);
					selectCondition.put("logicDelete", 0);
					// 结束时间取最后结束的分包工程时间
					Subproject subproject = loadLastEndSubProject(selectCondition);
					project_Quote.setDueTime(subproject.getEndtime());
					project_Quote.setTimeZone(subproject.getTimezone());
					// 三种状态：Not Quoted、Quoting、Quoted，取状态最慢的分包工程状态
					List<Integer> quoteSubProjectIDs = subprojectMapper
							.loadQuoteSubProjectIDs(selectCondition);
					String status = "Quoted";
					for (Integer quoteSubProjectID : quoteSubProjectIDs) {
						// TODO
						// 随着以后数据量增大，多次查询效率可能会降低，到时需要考虑修改数据库清单表和类目表，增加分包工程字段外键关联，减少多次查询
						List<Integer> elementIDs = quoted_Element4tbqMapper
								.loadTBQElementIDs(quoteSubProjectID,
										ConstantData.SECTION);
						List<Integer> billItemIDs = quoted_Billitem4tbqMapper
								.loadBillItemIDs(elementIDs);
						HashMap<String, Object> condition = new HashMap<String, Object>();
						condition.put("userID", user.getUserid());
						condition.put("billItemIDs", billItemIDs);
						// 先找未报价状态，如果找到就直接跳出循环，再找报价结束状态，如果找到就直接执行下一个状态判断，未找到代表这是报价中状态继续下一个状态判断
						int count = quoted_Detail4tbqMapper
								.getQuoteBillitemCount(condition);
						if (count == 0) {
							status = "Not Quoted";
							break;
						} else {
							HashMap<String, Object> sc = new HashMap<String, Object>();
							sc.put("subProjectID", quoteSubProjectID);
							sc.put("email", user.getEmail());
							sc.put("logicDelete", "0");
							// TODO 同一个分包工程存在多个发送记录，取最后发送记录的状态，这里未来需要仔细梳理逻辑
							List<Supplier_Subproject> supplier_Subprojects = supplier_SubprojectMapper
									.loadSupplierSubProject(sc);
							if (supplier_Subprojects.size() > 0) {
								if (supplier_Subprojects.get(0).getSubmittime() != null) {
									// 已提交，代表该分包工程报价结束
								} else {
									status = "Quoting";
								}
							}
						}
					}
					project_Quote.setStatus(status);
					project_Quotes.add(project_Quote);
				}
			}
		}
		return project_Quotes;
	}

	@Override
	public boolean deleteInquireProjectByLogic(Integer queryprojectid) {
		return project_QueryMapper.deleteProjectByLogic(queryprojectid) == 1;
	}

	@Override
	public boolean deleteQuoteProjectByLogic(Integer queryprojectid,
			String email) {
		// 第一步，获取询价工程对应的分包工程标识
		List<Integer> subProjectIDs = subprojectMapper
				.loadSubProjectIDs(queryprojectid);
		// 第二步，从分包工程与供应商记录中逻辑删除用户对应的记录
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("email", email);
		selectCondition.put("subProjectIDs", subProjectIDs);
		// 此处会返回受影响的记录行数
		supplier_SubprojectMapper
				.deleteSupplierSubProjectByLogic(selectCondition);
		return true;
	}

	@Override
	public Project_Query loadProject(Integer projectID) {
		return project_QueryMapper.selectByPrimaryKey(projectID);
	}

	@Override
	public int editProject(Project_Query project) {
		String name = project.getName();
		String userid = project.getUserid();
		if (StringUtil.isNull(userid)) {
			userid = project_QueryMapper.selectByPrimaryKey(
					project.getQueryprojectid()).getUserid();
		}
		List<String> projectNames = project_QueryMapper
				.selectProjectNameByUserID(userid);
		for (String projectName : projectNames) {
			if (projectName.equalsIgnoreCase(name)) {
				return ConstantData.NameEXISTED;
			}
		}
		project_QueryMapper.updateByPrimaryKeySelective(project);
		return project.getQueryprojectid();
	}

	@Override
	public int validName(String name, String userid) {
		List<String> projectNames = project_QueryMapper
				.selectProjectNameByUserID(userid);
		for (String projectName : projectNames) {
			if (projectName.equalsIgnoreCase(name)) {
				return ConstantData.NameEXISTED;
			}
		}
		return ConstantData.OK;
	}

	@Override
	public List<Subproject> loadSubProject(Integer projectID) {
		return subprojectMapper.loadSubProject(projectID);
	}

	@Override
	public boolean saveSubContractRecord(String userid, int projectID,
			String subContract, String subProjectID) {
		JSONObject jo = getRecord(userid, projectID);
		jo.put("lastSetting", subContract);
		JSONArray records = jo.getJSONArray("records");
		if (records == null) {
			records = new JSONArray();
			jo.put("records", records);
		} else {
			int size = records.size();
			for (int i = 0; i < size; i++) {
				JSONObject record = records.getJSONObject(i);
				if (record.getString("subContract").equalsIgnoreCase(
						subContract)) {
					// 找到对应分包工程节点
					record.put("subProjectID", subProjectID);
					return saveRecord(userid, projectID, jo);
				}
			}
		}
		// 到这里，代表没有找到分包工程节点，那么新建一个完整的分包工程节点
		JSONObject record = new JSONObject();
		record.put("subContract", subContract);
		record.put("subProjectID", subProjectID);
		records.add(record);
		return saveRecord(userid, projectID, jo);
	}

	@Override
	public boolean saveTimeZoneRecord(String userid, int projectID,
			String subContract, String timeZone) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		if (records == null) {
			records = new JSONArray();
			jo.put("records", records);
		} else {
			int size = records.size();
			for (int i = 0; i < size; i++) {
				JSONObject record = records.getJSONObject(i);
				if (record.getString("subContract").equalsIgnoreCase(
						subContract)) {
					// 找到对应分包工程节点
					record.put("timeZone", timeZone);
					return saveRecord(userid, projectID, jo);
				}
			}
		}
		// 到这里，代表没有找到分包工程节点，那么新建一个完整的分包工程节点
		JSONObject record = new JSONObject();
		record.put("subContract", subContract);
		record.put("timeZone", timeZone);
		records.add(record);
		return saveRecord(userid, projectID, jo);
	}

	@Override
	public boolean saveEndTimeRecord(String userid, int projectID,
			String subContract, String endTime) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		if (records == null) {
			records = new JSONArray();
			jo.put("records", records);
		} else {
			int size = records.size();
			for (int i = 0; i < size; i++) {
				JSONObject record = records.getJSONObject(i);
				if (record.getString("subContract").equalsIgnoreCase(
						subContract)) {
					// 找到对应分包工程节点
					record.put("endTime", endTime);
					return saveRecord(userid, projectID, jo);
				}
			}
		}
		// 到这里，代表没有找到分包工程节点，那么新建一个完整的分包工程节点
		JSONObject record = new JSONObject();
		record.put("subContract", subContract);
		record.put("endTime", endTime);
		records.add(record);
		return saveRecord(userid, projectID, jo);
	}

	@Override
	public boolean saveSupplierRecord(String userid, int projectID,
			String subContract, String supplierID, String name, String email) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		int size = records.size();
		for (int i = 0; i < size; i++) {
			JSONObject record = records.getJSONObject(i);
			if (record.getString("subContract").equalsIgnoreCase(subContract)) {
				// 找到对应分包工程节点，取对应分包商节点
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				if (subcontractors == null) {
					subcontractors = new JSONArray();
					record.put("subcontractors", subcontractors);
				}
				// 新建一个完整的分包商节点
				JSONObject subcontractor = new JSONObject();
				subcontractor.put("id", supplierID);
				subcontractor.put("name", name);
				subcontractor.put("email", email);
				Supplier supplier = supplierMapper.selectByPrimaryKey(Integer
						.parseInt(supplierID));
				subcontractor.put("telephone", supplier.getTelephone());
				subcontractor.put("trade", supplier.getTrade());
				subcontractor.put("level", supplier.getLevel());
				subcontractor.put("address", supplier.getAddress());
				subcontractor.put("contacts", supplier.getContacts());
				subcontractors.add(subcontractor);
				return saveRecord(userid, projectID, jo);
			}
		}
		return false;
	}

	@Override
	public boolean deleteSupplierRecord(String userid, int projectID,
			String subContract, String supplierID) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		int size = records.size();
		for (int i = 0; i < size; i++) {
			JSONObject record = records.getJSONObject(i);
			if (record.getString("subContract").equalsIgnoreCase(subContract)) {
				// 找到对应分包工程节点，取对应分包商节点
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				int subcontractorSize = subcontractors.size();
				for (int j = 0; j < subcontractorSize; j++) {
					JSONObject subcontractor = subcontractors.getJSONObject(j);
					if (subcontractor.getString("id").equalsIgnoreCase(
							supplierID)) {
						// 找到分包商
						subcontractors.remove(j);
						return saveRecord(userid, projectID, jo);
					}
				}
			}
		}
		return false;
	}

	@Override
	public int saveSupplierAttachRecord(String userid, int projectID,
			String fileName, String attachPath, String subContract,
			String supplierID) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		int size = records.size();
		logger.info("{} attach records for supplierID={} projectID={}", size, supplierID, projectID);
		for (int i = 0; i < size; i++) {
			JSONObject record = records.getJSONObject(i);
			String sc = record.getString("subContract");			
			if ( sc.equalsIgnoreCase(subContract)) {
				// 找到对应分包工程节点，取对应分包商节点
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				int subcontractorSize = subcontractors.size();
				for (int j = 0; j < subcontractorSize; j++) {
					JSONObject subcontractor = subcontractors.getJSONObject(j);
					if (subcontractor.getString("id").equalsIgnoreCase(
							supplierID)) {
						// 找到分包商
						JSONObject attach = new JSONObject();
						attach.put("fileName", fileName);
						attach.put("attachPath", attachPath);
						JSONArray attachs = subcontractor
								.getJSONArray("attachs");
						if (attachs == null) {
							attachs = new JSONArray();
							subcontractor.put("attachs", attachs);
						}

						attachs.add(attach);
						logger.info("{} attachs for suppiler {}", attachs.size(), subcontractor.getString("id"));
						if  ( saveRecord(userid, projectID, jo))
						   return  attachs.size();
					}
				}
			}else
				logger.error("recorded subContract={} is not equal sc={} from browser", sc, subContract);
		}
		return -1;
	}

	@Override
	public boolean deleteSupplierAttachRecord(String userid, int projectID,
			String attachPath, String subContract, String supplierID) {
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		int size = records.size();
		for (int i = 0; i < size; i++) {
			JSONObject record = records.getJSONObject(i);
			if (record.getString("subContract").equalsIgnoreCase(subContract)) {
				// 找到对应分包工程节点，取对应分包商节点
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				int subcontractorSize = subcontractors.size();
				for (int j = 0; j < subcontractorSize; j++) {
					JSONObject subcontractor = subcontractors.getJSONObject(j);
					if (subcontractor.getString("id").equalsIgnoreCase(
							supplierID)) {
						// 找到分包商节点，取对应附件节点
						JSONArray attachs = subcontractor
								.getJSONArray("attachs");
						int attachSize = attachs.size();
						for (int k = 0; k < attachSize; k++) {
							JSONObject attach = attachs.getJSONObject(k);
							if (attach.getString("attachPath")
									.equalsIgnoreCase(attachPath)) {
								// 找到分包商
								logger.info("delete currentAttach={}", attachPath);
								attachs.remove(k);
								return saveRecord(userid, projectID, jo);
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 保存记录
	 * 
	 * @param userid
	 * @param jo
	 * @return
	 */
	private synchronized boolean saveRecord(String userid, int projectID,
			JSONObject jo) {
		String recordPath = WebUtil.getTempPath() + "/inquire/" + userid
				+ "/" + projectID + ".json";
		logger.info("recordPath={}", recordPath);
		return FileUtil.newFile(recordPath, jo.toJSONString());
	}

	/**
	 * 获取记录
	 * 
	 * @param userid
	 * @param projectID
	 * @return
	 */
	public synchronized JSONObject getRecord(String userid, int projectID) {
		
		String recordPath = WebUtil.getTempPath() + "/inquire/" + userid
				+ "/" + projectID + ".json";
		logger.info("recordPath={}", recordPath);
		String json = FileUtil.readContent(recordPath, true);
		if (StringUtil.isNull(json)) {
			return new JSONObject();
		} else {
			return JSON.parseObject(json);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param userid
	 * @param projectID
	 * @return
	 */
	public synchronized boolean deleteRecord(String userid, int projectID) {
		String recordPath = WebUtil.getTempPath() + "/inquire/"+userid
				+ "/" + projectID + ".json";
		return FileUtil.delFileOrFolder(recordPath);
	}

	@Override
	public List<SubProject_Subcontractor> getSubProjectSubcontractorMapping(
			String userid, int projectID, List<Supplier> suppliers) {
		List<SubProject_Subcontractor> subProject_SubcontractorMapping = new ArrayList<SubProject_Subcontractor>();
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		if (records != null) {
			// 获取用户对应的所有分包商标识集合
			List<String> sls = new ArrayList<String>();
			for (Supplier supplier : suppliers) {
				sls.add(String.valueOf(supplier.getSupplierid()) + "|"
						+ supplier.getName() + "|" + supplier.getEmail());
			}
			boolean update = false;
			int recordSize = records.size();
			for (int i = 0; i < recordSize; i++) {
				JSONObject record = records.getJSONObject(i);
				String subproject = record.getString("subContract");
				String datetime = record.getString("endTime");
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				if (subcontractors != null) {
					int subcontractorSize = subcontractors.size();
					for (int j = subcontractorSize - 1; j >= 0; j--) {
						JSONObject subcontractor = subcontractors
								.getJSONObject(j);
						String id = subcontractor.getString("id");
						String name = subcontractor.getString("name");
						String email = subcontractor.getString("email");
						if (sls.contains(id + "|" + name + "|" + email)) {
							// 用户分包商包含该分包商
							SubProject_Subcontractor subProject_Subcontractor = new SubProject_Subcontractor();
							subProject_Subcontractor.setSubproject(subproject);
							subProject_Subcontractor.setDatetime(datetime);
							subProject_Subcontractor.setSubcontractorId(id);
							subProject_Subcontractor
									.setSubcontractor(subcontractor
											.getString("name"));
							subProject_Subcontractor.setEmail(subcontractor
									.getString("email"));
							JSONArray attachs = subcontractor
									.getJSONArray("attachs");
							if (attachs != null && attachs.size() > 0) {
								subProject_Subcontractor.setHasAttach(true);
							}
							subProject_SubcontractorMapping
									.add(subProject_Subcontractor);
						} else {
							// 不包含，删除该分包商
							subcontractors.remove(j);
							update = true;
						}
					}
				}
			}
			if (update) {
				// 更新记录
				saveRecord(userid, projectID, jo);
			}
		}
		return subProject_SubcontractorMapping;
	}

	@Override
	public List<AttachMent> loadAttach(String userid, int projectID,
			String subContract, String supplierID) {
		List<AttachMent> attachMents = new ArrayList<AttachMent>();
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		boolean find = false;
		if (records != null) {
			int recordSize = records.size();
			for (int i = 0; i < recordSize; i++) {
				JSONObject record = records.getJSONObject(i);
				String subproject = record.getString("subContract");
				if (subproject.equalsIgnoreCase(subContract)) {
					// 找到分包工程
					JSONArray subcontractors = record
							.getJSONArray("subcontractors");
					if (subcontractors != null) {
						int subcontractorSize = subcontractors.size();
						for (int j = 0; j < subcontractorSize; j++) {
							JSONObject subcontractor = subcontractors
									.getJSONObject(j);
							if (subcontractor.getString("id").equalsIgnoreCase(
									supplierID)) {
								find = true;
								// 找到分包商
								JSONArray attachs = subcontractor
										.getJSONArray("attachs");
								if (attachs != null) {
									int attachSize = attachs.size();
									for (int k = 0; k < attachSize; k++) {
										JSONObject attach = attachs
												.getJSONObject(k);
										AttachMent attachMent = new AttachMent();
										attachMent.setSubproject(subContract);
										attachMent
												.setSubcontractorId(supplierID);
										attachMent.setFileName(attach
												.getString("fileName"));
										attachMent.setAttachPath(attach
												.getString("attachPath"));
										attachMents.add(attachMent);
									}
								}
							}
							if (find) {
								break;
							}
						}
					}
				}
				if (find) {
					break;
				}
			}
		}
		return attachMents;
	}

	@Override
	public synchronized int importTBQData(JSONObject tbqData)
			throws Exception {
		int queryprojectid = 0;	
		String userid = tbqData.getString("userid");
		User user = userMapper.selectByPrimaryKey(userid);
		// 第一步，导入总工程数据
		String name = tbqData.getJSONObject("project").getString("name");
		int tbqprojectid = tbqData.getJSONObject("project").getIntValue(
				"tbqProjectID");
		byte accuracy = tbqData.getJSONObject("project").getByteValue(
				"accuracy");
		// TODO 对名称进行重复性处理，按照蔡雷提供的命名逻辑，每次拼接一个数字，但是这种方式无法绝对保障工程是来源于同一个TBQ工程
		int count = 0;
		List<String> ns = project_QueryMapper.selectProjectNameByUserID(userid);
		for (String n : ns) {
			if (count == 0 && n.equalsIgnoreCase(name)) {
				count = 1;
			}
			if (n.contains("-")) {
				int index = n.lastIndexOf("-");
				if (n.substring(0, index).equalsIgnoreCase(name)) {
					String c = n.substring(index + 1);
					try {
						int max = Integer.parseInt(c);
						count = max + 1;
						break;
					} catch (Exception e) {

					}
				}
			}
		}
		logger.info("{} records in project_Query of {}", count, name);
		if (count > 0) {
			name = name + "-" + count;
		}
		Project_Query project = new Project_Query();
		project.setName(name);
		project.setUserid(user.getUserid());
		project.setCreateby(user.getNickname());
		project.setTbqprojectid(tbqprojectid);
		project.setAccuracy(accuracy);
		if (project_QueryMapper.insertSelective(project) == 1) {
			queryprojectid = project.getQueryprojectid();
			// 第二步，导入分包工程数据
			JSONArray subprojects = tbqData.getJSONArray("subprojects");
			int subprojectSize = subprojects.size();
			for (int i = 0; i < subprojectSize; i++) {
				JSONObject subproject = subprojects.getJSONObject(i);
				String subProjectName = subproject.getString("name");
				int tbqSubProjectID = subproject.getIntValue("tbqSubProjectID");
				Subproject sp = new Subproject();
				sp.setQueryprojectid(queryprojectid);
				sp.setTbqsubprojectid(tbqSubProjectID);
				sp.setName(subProjectName);
				sp.setCreateby(user.getNickname());
				if (subprojectMapper.insertSelective(sp) == 1) {
					int subprojectid = sp.getSubprojectid();
					// 第三步，导入章节数据
					JSONArray elements = subproject.getJSONArray("element");
					int elementSize = elements.size();
					for (int j = 0; j < elementSize; j++) {
						JSONObject element = elements.getJSONObject(j);
						int elementID = element.getIntValue("elementID");
						int pElementID = 0;
						String pID = element.getString("pElementID");
						if (!StringUtil.isNull(pID)) {
							pElementID = Integer.parseInt(pID);
						}
						byte type = element.getByteValue("type");
						String elementName = element.getString("name");
						Quoted_Element4tbq ele = new Quoted_Element4tbq();
						ele.setTbqelementid(elementID);
						ele.setTbqpelementid(pElementID);
						ele.setSubprojectid(subprojectid);
						ele.setType(type);
						ele.setName(elementName);
						if (quoted_Element4tbqMapper.insertSelective(ele) == 1) {
							int elementid = ele.getElementid();
							// 第四步，导入清单数据
							JSONArray billitems = element
									.getJSONArray("billitem");
							if (billitems != null) {
								int billitemSize = billitems.size();
								for (int k = 0; k < billitemSize; k++) {
									JSONObject billitem = billitems
											.getJSONObject(k);
									int billItemID = billitem
											.getIntValue("billItemID");
									String description = billitem
											.getString("description");
									String netRate = billitem
											.getString("netRate");
									String netAmount = billitem
											.getString("netAmount");
									int pBillItemID = 0;
									try {
										pBillItemID = billitem
												.getIntValue("pBillItemID");
									} catch (Exception e) {

									}
									byte priceType = billitem
											.getByteValue("priceType");
									String qty = billitem.getString("qty");
									String remark = billitem
											.getString("remark");
									String trade = billitem.getString("trade");
									byte biType = billitem.getByteValue("type");
									String unit = billitem.getString("unit");
									// 这里对工程量，单价，合价统一做一次数值处理，防止非数字字符串入库
									if (!StringUtil.isNull(qty)) {
										if (!DataUtil.isNumber(qty)) {
											qty = "";
										}
									}
									if (!StringUtil.isNull(netRate)) {
										if (!DataUtil.isNumber(netRate)) {
											netRate = "";
										}
									}
									if (!StringUtil.isNull(netAmount)) {
										if (!DataUtil.isNumber(netAmount)) {
											netAmount = "";
										}
									}
									Quoted_Billitem4tbq bi = new Quoted_Billitem4tbq();
									bi.setElementid(elementid);
									bi.setTbqbillitemid(billItemID);
									bi.setDescription(description);
									bi.setNetrate(netRate);
									bi.setAdjustnetrate(netRate);
									bi.setNetamount(netAmount);
									bi.setAdjustnetamount(netAmount);
									bi.setTbqpbillitemid(pBillItemID);
									bi.setPricetype(priceType);
									bi.setQty(qty);
									bi.setRemark(remark);
									bi.setTrade(trade);
									bi.setType(biType);
									bi.setUnit(unit);
									if (quoted_Billitem4tbqMapper
											.insertSelective(bi) != 1) {
										throw new RuntimeException(
												"Save billitem failed!");
									}
								}
							}
						} else {
							throw new RuntimeException("Save element failed!");
						}
					}
				} else {
					throw new RuntimeException("Save subproject failed!");
				}
			}
		} 
		return queryprojectid;
	}

	@Override
	public List<QuoteSubProjectElement> loadQuoteSubProjectElement(
			String email, int projectID) {
		List<QuoteSubProjectElement> quoteSubProjectElements = new ArrayList<QuoteSubProjectElement>();
		// TODO 获取发送给用户的分包工程列表
		List<Integer> subProjectIDs = supplier_SubprojectMapper
				.loadSubProjectIDs(email);
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("queryProjectID", projectID);
		selectCondition.put("subProjectIDs", subProjectIDs);
		selectCondition.put("logicDelete", "0");
		List<Subproject> subprojects = subprojectMapper
				.loadQuoteSubProject(selectCondition);
		for (Subproject subproject : subprojects) {
			QuoteSubProjectElement quoteSubProjectElement = new QuoteSubProjectElement();
			quoteSubProjectElement
					.setSubprojectid(subproject.getSubprojectid());
			quoteSubProjectElement.setQueryprojectid(projectID);
			quoteSubProjectElement.setName(subproject.getName());
			// 获取分包工程下面所有章
			List<Element> elements = new ArrayList<Element>();
			if (subproject.getSource() == ConstantData.TBQ) {
				List<Quoted_Element4tbq> element4tbqs = quoted_Element4tbqMapper
						.loadTBQElement(subproject.getSubprojectid(),
								ConstantData.CHAPTER);
				for (Quoted_Element4tbq element4tbq : element4tbqs) {
					Element element = new Element();
					element.setElementid(element4tbq.getElementid());
					element.setSubprojectid(element4tbq.getSubprojectid());
					element.setName(element4tbq.getName());
					element.setType(ConstantData.CHAPTER);
					// 获取分包工程章下面所有节
					List<Element> childElements = new ArrayList<Element>();
					List<Quoted_Element4tbq> childelement4tbqs = quoted_Element4tbqMapper
							.loadTBQChildElement(subproject.getSubprojectid(),
									element4tbq.getTbqelementid());
					for (Quoted_Element4tbq childelement4tbq : childelement4tbqs) {
						Element childelement = new Element();
						childelement.setElementid(childelement4tbq
								.getElementid());
						childelement.setSubprojectid(childelement4tbq
								.getSubprojectid());
						childelement.setName(childelement4tbq.getName());
						childelement.setType(ConstantData.SECTION);
						childElements.add(childelement);
					}
					element.setChildElements(childElements);
					elements.add(element);
				}
			}
			quoteSubProjectElement.setElements(elements);
			quoteSubProjectElements.add(quoteSubProjectElement);
		}
		return quoteSubProjectElements;
	}

	@Override
	public List<EvaluationSubProjectElement> loadEvaluationSubProjectElement(
			int projectID) {
		List<EvaluationSubProjectElement> evaluationSubProjectElements = new ArrayList<EvaluationSubProjectElement>();
		// TODO 获得时间到期可评标的分包工程，到时需要根据时区做判断
		List<Integer> subProjectIDs = subprojectMapper
				.loadEvaluationSubProjectIDs(projectID);
		for (Integer subProjectID : subProjectIDs) {
			Subproject subproject = subprojectMapper
					.selectByPrimaryKey(subProjectID);
			EvaluationSubProjectElement evaluationSubProjectElement = new EvaluationSubProjectElement();
			evaluationSubProjectElement.setSubprojectid(subproject
					.getSubprojectid());
			evaluationSubProjectElement.setQueryprojectid(projectID);
			evaluationSubProjectElement.setName(subproject.getName());
			// 获取分包工程下面所有章
			List<Element> elements = new ArrayList<Element>();
			if (subproject.getSource() == ConstantData.TBQ) {
				List<Quoted_Element4tbq> element4tbqs = quoted_Element4tbqMapper
						.loadTBQElement(subproject.getSubprojectid(),
								ConstantData.CHAPTER);
				for (Quoted_Element4tbq element4tbq : element4tbqs) {
					Element element = new Element();
					element.setElementid(element4tbq.getElementid());
					element.setSubprojectid(element4tbq.getSubprojectid());
					element.setName(element4tbq.getName());
					element.setType(ConstantData.CHAPTER);
					// 获取分包工程章下面所有节
					List<Element> childElements = new ArrayList<Element>();
					List<Quoted_Element4tbq> childelement4tbqs = quoted_Element4tbqMapper
							.loadTBQChildElement(subproject.getSubprojectid(),
									element4tbq.getTbqelementid());
					for (Quoted_Element4tbq childelement4tbq : childelement4tbqs) {
						Element childelement = new Element();
						childelement.setElementid(childelement4tbq
								.getElementid());
						childelement.setSubprojectid(childelement4tbq
								.getSubprojectid());
						childelement.setName(childelement4tbq.getName());
						childelement.setType(ConstantData.SECTION);
						childElements.add(childelement);
					}
					element.setChildElements(childElements);
					elements.add(element);
				}
			}
			evaluationSubProjectElement.setElements(elements);
			evaluationSubProjectElements.add(evaluationSubProjectElement);
		}
		return evaluationSubProjectElements;
	}

	@Override
	public List<QuoteBillItem> loadQuoteBillItem(String userid, String email,
			int projectID, String eleID, String eleType, byte accuracy, String descript) {
		// 所有节标识
		List<Integer> elementIDs = new ArrayList<Integer>();
		if (!StringUtil.isNull(eleID) && !StringUtil.isNull(eleType)) {
			if (eleType.equalsIgnoreCase("0")) {
				// 分包工程的所有节标识
				elementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						Integer.parseInt(eleID), ConstantData.SECTION);
			} else if (eleType.equalsIgnoreCase("1")) {
				// 章的所有节标识
				Quoted_Element4tbq quoted_Element4tbq = quoted_Element4tbqMapper
						.selectByPrimaryKey(Integer.parseInt(eleID));
				elementIDs = quoted_Element4tbqMapper.loadTBQChildElementIDs(
						quoted_Element4tbq.getSubprojectid(),
						quoted_Element4tbq.getTbqelementid());
			} else if (eleType.equalsIgnoreCase("2")) {
				// 节
				elementIDs.add(Integer.parseInt(eleID));
			}
		} else {
			// TODO 默认取发送给用户的第一个分包工程的所有节标识
			List<Integer> subProjectIDs = supplier_SubprojectMapper
					.loadSubProjectIDs(email);
			HashMap<String, Object> selectCondition = new HashMap<String, Object>();
			selectCondition.put("queryProjectID", projectID);
			selectCondition.put("subProjectIDs", subProjectIDs);
			selectCondition.put("logicDelete", "0");
			List<Subproject> subprojects = subprojectMapper
					.loadQuoteSubProject(selectCondition);
			if (subprojects.size() > 0) {
				int subProjectID = subprojects.get(0).getSubprojectid();
				elementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						subProjectID, ConstantData.SECTION);
			}
		}
		
		List<String> descriptKeys = null;
		if (!StringUtil.isNull(descript)) {
			descriptKeys = StringUtil.getListByDescription(
					descript.toLowerCase(), ",");
			// TODO 保存失败怎么办，这里未来需要考虑一下
			saveFilter(userid, descriptKeys, "descript", "quote");
		}
		
		
		List<QuoteBillItem> items = new ArrayList<QuoteBillItem>();
		for (Integer elementID : elementIDs) {
			// 这里的逻辑比较绕，根据节标识取得的所有清单，已经包含若干次层级关系，但是在集合中属于并行排列，为了在前端页面展现清晰，需要按照顺序梳理出来，
			// 这种交错逻辑容易让一般开发者陷入困顿之境，老夫在这里用“大鸟绝学 ”破解此困境，后来晚辈如若看到此法，务必用心牢记！！！
			// 第一步，获取节下面的所有清单
			List<Quoted_Billitem4tbq> quoted_Billitem4tbqs = quoted_Billitem4tbqMapper
					.loadBillItem(elementID);
			// 第二步，利用map对集合中的所有清单进行划分，同时用用户对清单的报价替换清单对象的中的单价和合价
			Map<Integer, List<Quoted_Billitem4tbq>> map = new HashMap<Integer, List<Quoted_Billitem4tbq>>();
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				quoted_Billitem4tbq.accuracy = accuracy;
				int tbqpbillitemid = quoted_Billitem4tbq.getTbqpbillitemid();
				if (map.containsKey(tbqpbillitemid)) {
					map.get(tbqpbillitemid).add(quoted_Billitem4tbq);
				} else {
					List<Quoted_Billitem4tbq> billitem4tbqs = new ArrayList<Quoted_Billitem4tbq>();
					billitem4tbqs.add(quoted_Billitem4tbq);
					map.put(tbqpbillitemid, billitem4tbqs);
				}
				// 替换价格
				int billitemid = quoted_Billitem4tbq.getBillitemid();
				Quoted_Detail4tbq quoted_Detail4tbq = quoted_Detail4tbqMapper
						.selectBillitemQuoteDetail(billitemid, userid);
				if (quoted_Detail4tbq != null) {
					if (quoted_Billitem4tbq.getPricetype() == ConstantData.FixRate
							|| quoted_Billitem4tbq.getPricetype() == ConstantData.FixAmount) {
						// 固定单价和固定总价的价格不存在被替换
					} else {
						// 其他价格类型如果存在需要替换
						quoted_Billitem4tbq.setAdjustnetrate(quoted_Detail4tbq
								.getNetrate());
						quoted_Billitem4tbq
								.setAdjustnetamount(quoted_Detail4tbq
										.getNetamount());
					}
					quoted_Billitem4tbq
							.setRemark(quoted_Detail4tbq.getRemark());
				} else {
					if (quoted_Billitem4tbq.getPricetype() != ConstantData.FixRate
							&& quoted_Billitem4tbq.getPricetype() != ConstantData.FixAmount) {
						// 不是固定单价或者固定总价，那么默认是空，代表用户未报价
						quoted_Billitem4tbq.setAdjustnetrate("");
						quoted_Billitem4tbq.setAdjustnetamount("");
					}
				}
			}
			
			
			
			
			// 第三步，对价格进行统计，对于单价子项要向上统计单价，对于说明项不用统计，其他都是统计总价
			// 清单和标题才需要做统计，标题需要统计总价，清单要区分下级属于清单还是单价子项，对应清单需要统计总价，对于单价子项需要统计单价
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				byte type = quoted_Billitem4tbq.getType();
				if (type == ConstantData.BillItem
						|| type == ConstantData.Heading) {
					String netrate = statisticAdjustNetRate(
							quoted_Billitem4tbq, map);
					quoted_Billitem4tbq.setAdjustnetrate(netrate);
				}
			}
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				byte type = quoted_Billitem4tbq.getType();
				quoted_Billitem4tbq.accuracy = accuracy;
				if (type == ConstantData.BillItem
						|| type == ConstantData.Heading) {
					String netamount = statisticAdjustNetAmount(
							quoted_Billitem4tbq, map);
					if (!StringUtil.isNull(netamount)) {
						// 固定单价或者固定总价，用总价精度进行约束
						if (quoted_Billitem4tbq.getPricetype() == ConstantData.FixRate
								|| quoted_Billitem4tbq.getPricetype() == ConstantData.FixAmount) {
							netamount = DataUtil.double2String(
									Double.parseDouble(netamount), accuracy);
						}
					}
					quoted_Billitem4tbq.setAdjustnetamount(netamount);
				}
			}
			// 第四步，从根节点开始，依次往下梳理，梳理的目的主要是把节点的顺序整理正确，这里利用递归实现逐层添加
			// 根节点的特征是tbqPBillItemID=0
			addQuoteBillItem(items, 0, 0, map.get(0), map);
		}
		return items;
	}

	@Override
	public List<EvaluationBillItem> loadEvaluationBillItem(
			String currentUserid, int projectID, String eleID, String eleType,
			List<UserQuoteInfo> userQuoteInfos, byte accuracy, String descript,
			String trade, String unit, String changed) {
		int subProjectID = 0;
		// 分包工程对应的所有节标识
		List<Integer> allElementIDs = new ArrayList<Integer>();
		// 所有节标识
		List<Integer> elementIDs = new ArrayList<Integer>();
		logger.debug("eleID={} eleType={}", eleID, eleType);
		if (!StringUtil.isNull(eleID) && !StringUtil.isNull(eleType)) {
			if (eleType.equalsIgnoreCase("0")) {
				// 分包工程的所有节标识
				subProjectID = Integer.parseInt(eleID);
				elementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						subProjectID, ConstantData.SECTION);
				allElementIDs = elementIDs;
			} else if (eleType.equalsIgnoreCase("1")) {
				// 章的所有节标识
				Quoted_Element4tbq quoted_Element4tbq = quoted_Element4tbqMapper
						.selectByPrimaryKey(Integer.parseInt(eleID));
				subProjectID = quoted_Element4tbq.getSubprojectid();
				elementIDs = quoted_Element4tbqMapper.loadTBQChildElementIDs(
						subProjectID, quoted_Element4tbq.getTbqelementid());
				allElementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						subProjectID, ConstantData.SECTION);
			} else if (eleType.equalsIgnoreCase("2")) {
				// 节
				Quoted_Element4tbq quoted_Element4tbq = quoted_Element4tbqMapper
						.selectByPrimaryKey(Integer.parseInt(eleID));
				subProjectID = quoted_Element4tbq.getSubprojectid();
				elementIDs.add(Integer.parseInt(eleID));
				allElementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						subProjectID, ConstantData.SECTION);
			}
		} else {
			// TODO 默认取时间到期可评标的第一个分包工程的所有节标识，到时需要根据时区做判断
			List<Integer> subProjectIDs = subprojectMapper
					.loadEvaluationSubProjectIDs(projectID);
			if (subProjectIDs.size() > 0) {
				subProjectID = subProjectIDs.get(0);
				elementIDs = quoted_Element4tbqMapper.loadTBQElementIDs(
						subProjectID, ConstantData.SECTION);
				allElementIDs = elementIDs;
			}
		}
		List<String> descriptKeys = null;
		if (!StringUtil.isNull(descript)) {
			descriptKeys = StringUtil.getListByDescription(
					descript.toLowerCase(), ",");
			// TODO 保存失败怎么办，这里未来需要考虑一下
			saveFilter(currentUserid, descriptKeys, "descript");
		}
		List<String> tradeKeys = null;
		if (!StringUtil.isNull(trade)) {
			tradeKeys = StringUtil.getListByDescription(trade.toLowerCase(),
					",");
			// TODO 保存失败怎么办，这里未来需要考虑一下
			saveFilter(currentUserid, tradeKeys, "trade");
		}
		List<String> unitKeys = null;
		if (!StringUtil.isNull(unit)) {
			unitKeys = StringUtil.getListByDescription(unit.toLowerCase(), ",");
			// TODO 保存失败怎么办，这里未来需要考虑一下
			saveFilter(currentUserid, unitKeys, "unit");
		}
		List<EvaluationBillItem> items = new ArrayList<EvaluationBillItem>();
		for (Integer elementID : elementIDs) {
			// 这里的逻辑比较绕，根据节标识取得的所有清单，已经包含若干次层级关系，但是在集合中属于并行排列，为了在前端页面展现清晰，需要按照顺序梳理出来，
			// 这种交错逻辑容易让一般开发者陷入困顿之境，老夫在这里用“大鸟绝学 ”破解此困境，后来晚辈如若看到此法，务必用心牢记！！！
			// 第一步，获取节下面的所有清单
			List<Quoted_Billitem4tbq> quoted_Billitem4tbqs = quoted_Billitem4tbqMapper
					.loadBillItem(elementID);
			// 第二步，利用map对集合中的所有清单进行划分
			Map<Integer, List<Quoted_Billitem4tbq>> map = new HashMap<Integer, List<Quoted_Billitem4tbq>>();
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				quoted_Billitem4tbq.accuracy = accuracy;
				quoted_Billitem4tbq.vendor = "PTE";
				int tbqpbillitemid = quoted_Billitem4tbq.getTbqpbillitemid();
				if (map.containsKey(tbqpbillitemid)) {
					map.get(tbqpbillitemid).add(quoted_Billitem4tbq);
				} else {
					List<Quoted_Billitem4tbq> billitem4tbqs = new ArrayList<Quoted_Billitem4tbq>();
					billitem4tbqs.add(quoted_Billitem4tbq);
					map.put(tbqpbillitemid, billitem4tbqs);
				}
			}
			// 第三步，按照过滤条件做过滤
			if (descriptKeys != null || tradeKeys != null || unitKeys != null
					|| !StringUtil.isNull(changed)) {
				int size = quoted_Billitem4tbqs.size();
				if (size > 0) {
					// 用于记录条目是否满足过滤条件
					Map<Integer, Boolean> filterMap = new HashMap<Integer, Boolean>();
					for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
						// 自顶向下，依次分析条目是否满足过滤条件，能够保证每个条目都被分析过，同时利用map做判断能够防止重复分析，提高速度
						Integer billitemid = quoted_Billitem4tbq
								.getBillitemid();
						if (filterMap.containsKey(billitemid)) {
							// 该条目已经被分析过
						} else {
							// 该条目没有被分析过
							matchFilterConditions(quoted_Billitem4tbq,
									filterMap, map, descriptKeys, tradeKeys,
									unitKeys, changed);
						}
					}
					boolean r = false;
					for (int i = size - 1; i >= 0; i--) {
						Integer billitemid = quoted_Billitem4tbqs.get(i)
								.getBillitemid();
						// 不满足过滤条件，就移除
						if (!filterMap.get(billitemid)) {
							quoted_Billitem4tbqs.remove(i);
							r = true;
						}
					}
					if (r) {
						// 过滤掉一些条目，需要更新层级关系
						map.clear();
						map = new HashMap<Integer, List<Quoted_Billitem4tbq>>();
						for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
							int tbqpbillitemid = quoted_Billitem4tbq
									.getTbqpbillitemid();
							if (map.containsKey(tbqpbillitemid)) {
								map.get(tbqpbillitemid)
										.add(quoted_Billitem4tbq);
							} else {
								List<Quoted_Billitem4tbq> billitem4tbqs = new ArrayList<Quoted_Billitem4tbq>();
								billitem4tbqs.add(quoted_Billitem4tbq);
								map.put(tbqpbillitemid, billitem4tbqs);
							}
						}
					}
				}
			}
			// 第四步，对价格进行统计，对于单价子项要向上统计单价，对于说明项不用统计，其他都是统计总价
			// 清单和标题才需要做统计，标题需要统计总价，清单要区分下级属于清单还是单价子项，对应清单需要统计总价，对于单价子项需要统计单价
			// TODO 统计标底价格和标底调整价格(quoted_Billitem4tbq的数据就是PTE承包商出价)
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				byte type = quoted_Billitem4tbq.getType();
				if (type == ConstantData.BillItem
						|| type == ConstantData.Heading) {
					String adjustnetrate = statisticAdjustNetRate(
							quoted_Billitem4tbq, map);
					quoted_Billitem4tbq.setAdjustnetrate(adjustnetrate);
					String netrate = statisticNetRate(quoted_Billitem4tbq, map);
					quoted_Billitem4tbq.setNetrate(netrate);
				}
			}
			//标底都受tbq精度控制			
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				    byte type = quoted_Billitem4tbq.getType();			    
				
					String adjustnetamount = statisticAdjustNetAmount(
							quoted_Billitem4tbq, map);
					if (!StringUtil.isNull(adjustnetamount)) {
						// 固定单价或者固定总价，用总价精度进行约束
									
							adjustnetamount = DataUtil.double2String(
									Double.parseDouble(adjustnetamount),
									accuracy);							
						
					}
					quoted_Billitem4tbq.setAdjustnetamount(adjustnetamount);
					String netamount = statisticNetAmount(quoted_Billitem4tbq,
							map);
					if (!StringUtil.isNull(netamount)) {
						// 固定单价或者固定总价，用总价精度进行约束
												
							netamount = DataUtil.double2String(
									Double.parseDouble(netamount), accuracy);							
						
					}					
					
					quoted_Billitem4tbq.setNetamount(netamount);
				
			}
						
			// 第五步，从根节点开始，依次往下梳理，梳理的目的主要是把节点的顺序整理正确，这里利用递归实现逐层添加
			// 根节点的特征是tbqPBillItemID=0
			addEvaluationBillItem(items, 0, 0, map.get(0), map);
		}
		// TODO 调整报价采纳信息，这里也需要根据价格类型做递归计算
		statisticAdoptInfo(items, accuracy);
		// 获取分包工程对应的所有报价清单集合
		List<Quoted_Billitem4tbq> allBillItems = new ArrayList<Quoted_Billitem4tbq>();
		for (Integer elementID : allElementIDs) {
			List<Quoted_Billitem4tbq> quoted_Billitem4tbqs = quoted_Billitem4tbqMapper
					.loadBillItem(elementID);
			for (Quoted_Billitem4tbq quoted_Billitem4tbq : quoted_Billitem4tbqs) {
				allBillItems.add(quoted_Billitem4tbq);
			}
		}
		double fixAmount = getFixAmount(allBillItems);		
		
		// 获取所有提交分包工程报价信息的供应商针对清单的报价信息集合
		Subproject subproject = subprojectMapper
				.selectByPrimaryKey(subProjectID);
		Date endtime = subproject.getEndtime();
		//分包工程对应的报价的供应商
		List<String> emails = supplier_SubprojectMapper.loadSupplierEmails(
				subProjectID, endtime);
		
		//报价供应商（注册用户ID->mark/adoptedUserMark)
		//EvaluationBillItem.adoptedUserMark=UserQuoteInfo.mark
		//PTE USERID=0
		//UT  USERID=-1
		Map<String, String> supplierMap = new HashMap<String, String>();		
		supplierMap.put("0", "PTE");
		supplierMap.put("-1", "UD");
		int index = 1;
		for (String email : emails) {
			User user = userMapper.loadByEmail4Glodon(email);
			if (user != null) {
				UserQuoteInfo userQuoteInfo = new UserQuoteInfo();
				String userid = user.getUserid();
				userQuoteInfo.setUserid(userid);
				
				List<Supplier> ss = supplierMapper.selectSubcontractByEmail(currentUserid, email);				
				if ( ss!= null )
					if ( ss.size() > 1 ){
						logger.error("selectSubcontractByEmail return more than 1 record for currentUserid={} email={}", currentUserid, email);
						for ( Supplier s :ss ){
							logger.error("supplier:{}", s.getName());
						}
					}else if ( ss.size() == 1 ){						
						Supplier s = ss.get(0);
						if ( s != null ){
							userQuoteInfo.setName(ss.get(0).getName());
						}
					}else
						logger.error("no supplier data for currentUserid={} email={}", currentUserid, email);
				else{
					logger.error("can't get supplier for userid={} email={}", userid, email);
					userQuoteInfo.setName(user.getCompany());
				}
				// 获取供应商标识
				try {
					String mark = ExcelUtil.indexToColumn(index);
					logger.debug("user mark={} for suppiler {}", mark, userid );
					userQuoteInfo.setMark(mark);
					supplierMap.put(userid, mark);
					index++;
				} catch (Exception e) {
					// 这里不可能出现错误，因为传递的列号肯定是正数
				}
				// 获取用户针对该分包工程设置的折扣
				Quoted_Discount4tbq quoted_Discount4tbq = quoted_Discount4tbqMapper
						.selectBillitemQuoteDiscount(userid, subProjectID);
				if (quoted_Discount4tbq != null) {
					userQuoteInfo
							.setDiscount(quoted_Discount4tbq.getDiscount());
					userQuoteInfo.setDiscountPercent(quoted_Discount4tbq
							.getDiscountpercent());
				}
				// 获取所有清单对应的报价信息
				List<QuoteInfo> quoteInfo = new ArrayList<QuoteInfo>();
				for (EvaluationBillItem item : items) {
					QuoteInfo qi = new QuoteInfo();					
					qi.setBillitemid(item.getBillitemid());
					qi.setBillitempid(item.getBillitempid());
					qi.setType(item.getType());
					qi.setQty(item.getQty());
					qi.setPricetype(item.getPricetype());
					qi.setNetrateEdit(item.isNetrateEdit());
					qi.setNetamountEdit(item.isNetamountEdit());
					Quoted_Detail4tbq quoted_Detail4tbq = quoted_Detail4tbqMapper
							.selectBillitemQuoteDetail(item.getBillitemid(),
									userid);
					if (quoted_Detail4tbq != null) {
						if (item.getPricetype() == ConstantData.FixRate
								|| item.getPricetype() == ConstantData.FixAmount) {
							// 分包商有报价的清单，有可能只修改了备注，这对于固定价格类型的清单，需要把价格带入
							qi.resetQuote(item, accuracy);
						} else {
							qi.setNetrate(quoted_Detail4tbq.getNetrate());
							qi.setAdjustnetrate(quoted_Detail4tbq
									.getAdjustnetrate());
							qi.setNetamount(quoted_Detail4tbq.getNetamount());
							qi.setAdjustnetamount(quoted_Detail4tbq
									.getAdjustnetamount());
						}
						qi.setRemark(quoted_Detail4tbq.getRemark());
					} else {
						// 分包商没有报价的清单，如果该清单属于固定总价或者固定单价的类型，需要把价格带入
						if (item.getPricetype() == ConstantData.FixRate
								|| item.getPricetype() == ConstantData.FixAmount) {
							logger.debug("item.getNetrate()={} item.getAdjustnetrate()={} item.getNetamount()={} item.getAdjustnetamount()={}",item.getNetrate(), item.getAdjustnetrate(), item.getNetamount(), item.getAdjustnetamount());
							
							qi.resetQuote(item, accuracy);						
							
							qi.setRemark(item.getRemark());
						}
					}
					quoteInfo.add(qi);
				}
				// TODO 调整报价信息，这里也需要根据价格类型做递归计算
				statisticQuoteInfo(quoteInfo, accuracy);
				userQuoteInfo.setQuoteInfo(quoteInfo);
				// 统计报价隐藏部分总价
				if (!StringUtil.isNull(eleID) && !StringUtil.isNull(eleType)) {
					if (eleType.equalsIgnoreCase("0")) {
						// 取的所有分包工程，不存在隐藏项
						userQuoteInfo.setHiddenPrice4SubProject("0");
						userQuoteInfo.setFixAmount(fixAmount + "");
					} else {
						// 取的章和节，存在隐藏项
						// 这里所有报价仅仅用于计算分包商对分包工程的总报价，所以统计的时候为了减少程序运算量，取了一个巧，只用报价清单的层级关系去处理
						List<QuoteInfo> allQuoteInfo = getAllQuoteInfo(
								allBillItems, userid, accuracy);
						double allTotalAmount = getTotalAmount(allQuoteInfo);
						double showTotalAmount = getTotalAmount(quoteInfo);
						userQuoteInfo
								.setHiddenPrice4SubProject((allTotalAmount - showTotalAmount)
										+ "");
						userQuoteInfo.setFixAmount(fixAmount + "");
					}
				} else {
					// 取的所有分包工程，不存在隐藏项
					userQuoteInfo.setHiddenPrice4SubProject("0");
					userQuoteInfo.setFixAmount(fixAmount + "");
				}
				userQuoteInfos.add(userQuoteInfo);
			}
		}
		// 调整清单采纳标识(Source列)
		for (EvaluationBillItem item : items) {
			String adoptedUserID = item.getAdoptedUserID();
			if (!StringUtil.isNull(adoptedUserID)) {
				item.setAdoptedUserMark(supplierMap.get(adoptedUserID));
			}
		}
		return items;
	}

	private boolean matchFilterConditions(
			Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, Boolean> filterMap,
			Map<Integer, List<Quoted_Billitem4tbq>> map,
			List<String> descriptKeys, List<String> tradeKeys,
			List<String> unitKeys, String changed) {
		if (quoted_Billitem4tbq.getType() == ConstantData.BillItem) {
			// 只需要分析清单
			int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
			List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
			// 如果该清单有子项，子项是清单子项，要判断清单子项是否满足过滤条件做递归；子项是单价子项或者说明项，那么只需要判断自身即可，同时子项的过滤状态也跟随判断结果，如果没有子项，那么就判断自身
			// 先判断是否有清单子项
			boolean hasBillItem = false;
			if (billitem4tbqs != null && billitem4tbqs.size() > 0) {
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					if (billitem4tbq.getType() == ConstantData.BillItem) {
						// 存在清单子项
						hasBillItem = true;
						break;
					}
				}
			}
			boolean matchFilter = true;
			if (hasBillItem) {
				boolean hasMatchFilter = false;
				// 有清单子项，依次判断各个子清单，只要有一个子清单符合过滤条件，这个清单就属于符合过滤条件的清单，这里会走递归流程做判断
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					if (billitem4tbq.getType() == ConstantData.BillItem) {
						if (matchFilterConditions(billitem4tbq, filterMap, map,
								descriptKeys, tradeKeys, unitKeys, changed)) {
							hasMatchFilter = true;
							break;
						}
					}
				}
				matchFilter = hasMatchFilter;
			} else {
				// 无清单子项，判断自身，然后关联设置子项的类型
				if (!StringUtil.isNull(changed)
						&& changed.equalsIgnoreCase("1")) {
					// 显示修改项做过滤
					matchFilter = quoted_Billitem4tbq.getIschange();
					if (!matchFilter) {
						// 如果清单没修改，但是存在单价子项，就用单价子项做判断
						if (billitem4tbqs != null && billitem4tbqs.size() > 0) {
							for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
								if (billitem4tbq.getType() == ConstantData.RateItem) {
									// 存在单价子项
									matchFilter = billitem4tbq.getIschange();
									if (matchFilter) {
										break;
									}
								}
							}
						}
					}
				} else {
					// 按照过滤条件做过滤
					// 按描述做过滤
					if (descriptKeys != null) {
						// 条目描述
						String des = quoted_Billitem4tbq.getDescription();
						if (!StringUtil.isNull(des)) {
							boolean contain = true;
							for (String descriptKey : descriptKeys) {
								if (!des.contains(descriptKey)) {
									contain = false;
									break;
								}
							}
							if (!contain) {
								// 不完全包含，就移除
								matchFilter = false;
							}
						} else {
							// 为空，直接去除
							matchFilter = false;
						}
					}
					if (matchFilter) {
						// 按专业做过滤
						if (tradeKeys != null) {
							String l = quoted_Billitem4tbq.getTrade();
							if (!StringUtil.isNull(l)) {
								boolean contain = true;
								for (String tradeKey : tradeKeys) {
									if (!l.contains(tradeKey)) {
										contain = false;
										break;
									}
								}
								if (!contain) {
									// 不完全包含，就移除
									matchFilter = false;
								}
							} else {
								// 为空，直接去除
								matchFilter = false;
							}

						}
					}
					if (matchFilter) {
						// 按单位做过滤
						if (unitKeys != null) {
							String l = quoted_Billitem4tbq.getUnit();
							if (!StringUtil.isNull(l)) {
								// 不为空进行判断
								List<String> sll = StringUtil
										.getListByDescription(l.toLowerCase(),
												",");
								if (ArrayUtil.getIntersection(unitKeys, sll)
										.size() != unitKeys.size()) {
									// 不完全包含所选资质，就移除
									matchFilter = false;
								}
							} else {
								// 为空，直接去除
								matchFilter = false;
							}
						}
					}
				}
				if (billitem4tbqs != null && billitem4tbqs.size() > 0) {
					for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
						filterMap
								.put(billitem4tbq.getBillitemid(), matchFilter);
					}
				}
			}
			filterMap.put(quoted_Billitem4tbq.getBillitemid(), matchFilter);
			return matchFilter;
		} else {
			filterMap.put(quoted_Billitem4tbq.getBillitemid(), true);
			return true;
		}
	}
	
	
	
	private boolean matchFilterConditions(
			Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, Boolean> filterMap,
			Map<Integer, List<Quoted_Billitem4tbq>> map,
			List<String> descriptKeys, String source) {
		if (quoted_Billitem4tbq.getType() == ConstantData.BillItem) {
			// 只需要分析清单
			int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
			List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
			// 如果该清单有子项，子项是清单子项，要判断清单子项是否满足过滤条件做递归；子项是单价子项或者说明项，那么只需要判断自身即可，同时子项的过滤状态也跟随判断结果，如果没有子项，那么就判断自身
			// 先判断是否有清单子项
			boolean hasBillItem = false;
			if (billitem4tbqs != null && billitem4tbqs.size() > 0) {
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					if (billitem4tbq.getType() == ConstantData.BillItem) {
						// 存在清单子项
						hasBillItem = true;
						break;
					}
				}
			}
			boolean matchFilter = true;
			if (hasBillItem) {
				boolean hasMatchFilter = false;
				// 有清单子项，依次判断各个子清单，只要有一个子清单符合过滤条件，这个清单就属于符合过滤条件的清单，这里会走递归流程做判断
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					if (billitem4tbq.getType() == ConstantData.BillItem) {
						if (matchFilterConditions(billitem4tbq, filterMap, map,
								descriptKeys, source)) {
							hasMatchFilter = true;
							break;
						}
					}
				}
				matchFilter = hasMatchFilter;
			} else {
				
					// 按照过滤条件做过滤
					// 按描述做过滤
					if (descriptKeys != null) {
						// 条目描述
						String des = quoted_Billitem4tbq.getDescription();
						if (!StringUtil.isNull(des)) {
							boolean contain = true;
							for (String descriptKey : descriptKeys) {
								if (!des.contains(descriptKey)) {
									contain = false;
									break;
								}
							}
							if (!contain) {
								// 不完全包含，就移除
								matchFilter = false;
							}
						} else {
							// 为空，直接去除
							matchFilter = false;
						}
					}
					
				}
				if (billitem4tbqs != null && billitem4tbqs.size() > 0) {
					for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
						filterMap
								.put(billitem4tbq.getBillitemid(), matchFilter);
					}
				}
			
			filterMap.put(quoted_Billitem4tbq.getBillitemid(), matchFilter);
			return matchFilter;
		} else {
			filterMap.put(quoted_Billitem4tbq.getBillitemid(), true);
			return true;
		}
	}


	private List<QuoteInfo> getAllQuoteInfo(
			List<Quoted_Billitem4tbq> allBillItems, String userid, byte accuracy) {
		List<QuoteInfo> quoteInfo = new ArrayList<QuoteInfo>();
		for (Quoted_Billitem4tbq item : allBillItems) {
			QuoteInfo qi = new QuoteInfo();
			qi.setBillitemid(item.getTbqbillitemid());
			qi.setBillitempid(item.getTbqpbillitemid());
			qi.setType(item.getType());
			qi.setQty(item.getQty());
			qi.setPricetype(item.getPricetype());
			Quoted_Detail4tbq quoted_Detail4tbq = quoted_Detail4tbqMapper
					.selectBillitemQuoteDetail(item.getBillitemid(), userid);
			if (quoted_Detail4tbq != null) {
				if (item.getPricetype() == ConstantData.FixRate
						|| item.getPricetype() == ConstantData.FixAmount) {
					// 分包商有报价的清单，有可能只修改了备注，这对于固定价格类型的清单，需要把价格带入
					String v = DataUtil.double2String(item.getNetrate(), accuracy);
					qi.setNetrate(v);
					v = DataUtil.double2String(item.getAdjustnetrate(), accuracy);
					qi.setAdjustnetrate(v);
					qi.setNetamount(item.getNetamount());
					qi.setAdjustnetamount(item.getAdjustnetamount());
				} else {
					qi.setNetrate(quoted_Detail4tbq.getNetrate());
					qi.setAdjustnetrate(quoted_Detail4tbq.getAdjustnetrate());
					qi.setNetamount(quoted_Detail4tbq.getNetamount());
					qi.setAdjustnetamount(quoted_Detail4tbq
							.getAdjustnetamount());
				}
				qi.setRemark(quoted_Detail4tbq.getRemark());
			} else {
				// 分包商没有报价的清单，如果该清单属于固定总价或者固定单价的类型，需要把价格带入
				if (item.getPricetype() == ConstantData.FixRate
						|| item.getPricetype() == ConstantData.FixAmount) {
					qi.setNetrate(item.getNetrate());
					qi.setAdjustnetrate(item.getAdjustnetrate());
					qi.setNetamount(item.getNetamount());
					qi.setAdjustnetamount(item.getAdjustnetamount());
					qi.setRemark(item.getRemark());
				}
			}
			quoteInfo.add(qi);
		}
		// TODO 调整报价信息，这里也需要根据价格类型做递归计算
		statisticQuoteInfo(quoteInfo, accuracy);
		return quoteInfo;
	}

	/**
	 * 获取所有清单中，属于固定价格类别的清单价格总和
	 * 
	 * @param allQuoteInfo
	 * @return
	 */
	private double getFixAmount(List<Quoted_Billitem4tbq> allBillItems) {
		double totalAmount = 0;
		// 利用map对集合做一次梳理
		Map<Integer, List<Quoted_Billitem4tbq>> map = new HashMap<Integer, List<Quoted_Billitem4tbq>>();
		for (Quoted_Billitem4tbq quoted_Billitem4tbq : allBillItems) {
			int tbqpbillitemid = quoted_Billitem4tbq.getTbqpbillitemid();
			if (map.containsKey(tbqpbillitemid)) {
				map.get(tbqpbillitemid).add(quoted_Billitem4tbq);
			} else {
				List<Quoted_Billitem4tbq> billitem4tbqs = new ArrayList<Quoted_Billitem4tbq>();
				billitem4tbqs.add(quoted_Billitem4tbq);
				map.put(tbqpbillitemid, billitem4tbqs);
			}
		}
		for (Quoted_Billitem4tbq item : allBillItems) {
			if (item.getType() == ConstantData.BillItem) {
				int tbqbillitemid = item.getTbqbillitemid();
				List<Quoted_Billitem4tbq> child = map.get(tbqbillitemid);
				boolean canStatistic = true;
				if (child != null && hasChild4QuotedBillitem(child)) {
					// 有子项，并且子项不是单价子项就不能统计
					byte childType = getChildType4QuotedBillitem(child);
					if (childType != ConstantData.RateItem) {
						canStatistic = false;
					}
				}
				if (canStatistic) {
					byte pricetype = item.getPricetype();
					if (pricetype == ConstantData.FixRate) {
						// 固定单价
						String qty = item.getQty();
						String netrate = item.getAdjustnetrate();
						if (!StringUtil.isNull(qty)
								&& !StringUtil.isNull(netrate)) {
							totalAmount += Double.parseDouble(qty)
									* Double.parseDouble(netrate);
						}
					} else if (pricetype == ConstantData.FixAmount) {
						// 固定总价
						String netamount = item.getAdjustnetamount();
						if (!StringUtil.isNull(netamount)) {
							totalAmount += Double.parseDouble(netamount);
						} else {
							String qty = item.getQty();
							String netrate = item.getAdjustnetrate();
							if (!StringUtil.isNull(qty)
									&& !StringUtil.isNull(netrate)) {
								totalAmount += Double.parseDouble(qty)
										* Double.parseDouble(netrate);
							}
						}
					}
				}
			}
		}
		
		return Double.parseDouble(DataUtil.double2String(totalAmount,
				ConstantData.ACCURACY));
	}

	private byte getChildType4QuotedBillitem(List<Quoted_Billitem4tbq> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (Quoted_Billitem4tbq child : childs) {
				byte type = child.getType();
				if (type != ConstantData.Note) {
					return type;
				}
			}
		}
		return ConstantData.Note;
	}

	private boolean hasChild4QuotedBillitem(List<Quoted_Billitem4tbq> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (Quoted_Billitem4tbq child : childs) {
				if (child.getType() != ConstantData.Note) {
					return true;
				}
			}
		}
		return false;
	}

	private int getChildType4EvaluationBillitem(List<EvaluationBillItem> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (EvaluationBillItem child : childs) {
				byte type = child.getType();
				if (type != ConstantData.Note) {
					return type;
				}
			}
		}
		return ConstantData.Note;
	}

	private boolean hasChild4EvaluationBillitem(List<EvaluationBillItem> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (EvaluationBillItem child : childs) {
				if (child.getType() != ConstantData.Note) {
					return true;
				}
			}
		}
		return false;
	}

	private int getChildType4QuoteInfo(List<QuoteInfo> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (QuoteInfo child : childs) {
				byte type = child.getType();
				if (type != ConstantData.Note) {
					return type;
				}
			}
		}
		return ConstantData.Note;
	}

	private boolean hasChild4QuoteInfo(List<QuoteInfo> childs) {
		if (childs.size() > 0) {
			// 子项存在，并且不能是说明项
			for (QuoteInfo child : childs) {
				if (child.getType() != ConstantData.Note) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取用户报价的总和
	 * 
	 * @param quoteInfo
	 * @return
	 */
	private double getTotalAmount(List<QuoteInfo> qis) {
		double totalAmount = 0;
		for (QuoteInfo qi : qis) {
			if (qi.getBillitempid() == 0) {
				String netamount = qi.getAdjustnetamount();
				if (!StringUtil.isNull(netamount)) {
					totalAmount += Double.parseDouble(netamount);
				}
			}
		}
		return Double.parseDouble(DataUtil.double2String(totalAmount,
				ConstantData.ACCURACY));
	}

	private void statisticAdoptInfo(List<EvaluationBillItem> items,
			byte accuracy) {
		// 第一步，利用map对集合中的所有清单进行划分
		Map<Integer, List<EvaluationBillItem>> map = new HashMap<Integer, List<EvaluationBillItem>>();
		for (EvaluationBillItem item : items) {
			item.accuracy = accuracy;
			int billitempid = item.getBillitempid();
			if (map.containsKey(billitempid)) {
				map.get(billitempid).add(item);
			} else {
				List<EvaluationBillItem> qis = new ArrayList<EvaluationBillItem>();
				qis.add(item);
				map.put(billitempid, qis);
			}
		}
		// 第二步，对价格进行统计，对于单价子项要向上统计单价，对于说明项不用统计，其他都是统计总价
		// 清单和标题才需要做统计，标题需要统计总价，清单要区分下级属于清单还是单价子项，对应清单需要统计总价，对于单价子项需要统计单价
		for (EvaluationBillItem item : items) {
			byte type = item.getType();
			
			if (type == ConstantData.BillItem || type == ConstantData.Heading) {
				if ( item.getBillitemid() == 308494 ){
				String adoptedNetrate = statisticAdoptNetRate(item, map);
				item.setAdoptedNetrate(adoptedNetrate);
				
				}
			}
		}
		for (EvaluationBillItem item : items) {
			byte type = item.getType();
			if (type == ConstantData.BillItem || type == ConstantData.Heading) {
				String adoptnetamount = statisticAdoptNetAmount(item, map);
				if (!StringUtil.isNull(adoptnetamount)) {
					// 固定单价或者固定总价，用总价精度进行约束
					if (item.getPricetype() == ConstantData.FixRate
							|| item.getPricetype() == ConstantData.FixAmount) {
						adoptnetamount = DataUtil.double2String(
								Double.parseDouble(adoptnetamount), accuracy);
					}
				}
				logger.debug("adoptnetamount={}", adoptnetamount);
				item.setAdoptedNetamount(adoptnetamount);
			}
		}
	}

	private void statisticQuoteInfo(List<QuoteInfo> quoteInfos, byte accuracy) {
		// 第一步，利用map对集合中的所有清单进行划分
		Map<Integer, List<QuoteInfo>> map = new HashMap<Integer, List<QuoteInfo>>();
		for (QuoteInfo quoteInfo : quoteInfos) {
			quoteInfo.accuracy = accuracy;
			int billitempid = quoteInfo.getBillitempid();
			if (map.containsKey(billitempid)) {
				map.get(billitempid).add(quoteInfo);
			} else {
				List<QuoteInfo> qis = new ArrayList<QuoteInfo>();
				qis.add(quoteInfo);
				map.put(billitempid, qis);
			}
		}
		// 第二步，对价格进行统计，对于单价子项要向上统计单价，对于说明项不用统计，其他都是统计总价
		// 清单和标题才需要做统计，标题需要统计总价，清单要区分下级属于清单还是单价子项，对应清单需要统计总价，对于单价子项需要统计单价
		for (QuoteInfo quoteInfo : quoteInfos) {
			byte type = quoteInfo.getType();
			if (type == ConstantData.BillItem || type == ConstantData.Heading) {
				String netrate = statisticNetRate(quoteInfo, map);
				quoteInfo.setNetrate(netrate);
				String adjustnetrate = statisticAdjustNetRate(quoteInfo, map);
				quoteInfo.setAdjustnetrate(adjustnetrate);
			}
		}
		for (QuoteInfo quoteInfo : quoteInfos) {
			byte type = quoteInfo.getType();
			if (type == ConstantData.BillItem || type == ConstantData.Heading) {
				String adjustnetamount = statisticAdjustNetAmount(quoteInfo,
						map);
				
				
				if (!StringUtil.isNull(adjustnetamount)) {
					// 固定单价或者固定总价，用总价精度进行约束
					if (quoteInfo.getPricetype() == ConstantData.FixRate
							|| quoteInfo.getPricetype() == ConstantData.FixAmount) {
						adjustnetamount = DataUtil.double2String(
								Double.parseDouble(adjustnetamount), accuracy);
					}
				}
				quoteInfo.setAdjustnetamount(adjustnetamount);
				
				
				String netamount = statisticNetAmount(quoteInfo, map);
				if (!StringUtil.isNull(netamount)) {
					// 固定单价或者固定总价，用总价精度进行约束
					if (quoteInfo.getPricetype() == ConstantData.FixRate
							|| quoteInfo.getPricetype() == ConstantData.FixAmount) {
						netamount = DataUtil.double2String(
								Double.parseDouble(netamount), accuracy);
					}
				}
				quoteInfo.setNetamount(netamount);
				
			}
		}
	}

	/**
	 * 统计报价商单价
	 * 
	 * @param quoteInfo
	 * @param map
	 * @return
	 */
	private String statisticNetRate(QuoteInfo quoteInfo,
			Map<Integer, List<QuoteInfo>> map) {
		int billitemid = quoteInfo.getBillitemid();
		List<QuoteInfo> qis = map.get(billitemid);
		if (qis != null && hasChild4QuoteInfo(qis)) {
			// 只有单价子项需要统计单价
			if (getChildType4QuoteInfo(qis) == ConstantData.RateItem) {
				double netRate = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (QuoteInfo qi : qis) {
					String nr = qi.getNetrate();
					if (!StringUtil.isNull(nr)) {
						isEmpty = false;
						try {
							netRate += Double.parseDouble(nr);
						} catch (Exception e) {
							logger.error("StatisticNetRate:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return String.valueOf(netRate);
				}
			} else {
				return quoteInfo.getNetrate();
			}
		} else {
			return quoteInfo.getNetrate();
		}
	}

	/**
	 * 统计报价商调整单价
	 * 
	 * @param quoteInfo
	 * @param map
	 * @return
	 */
	private String statisticAdjustNetRate(QuoteInfo quoteInfo,
			Map<Integer, List<QuoteInfo>> map) {
		int billitemid = quoteInfo.getBillitemid();
		List<QuoteInfo> qis = map.get(billitemid);
		if (qis != null && hasChild4QuoteInfo(qis)) {
			// 只有单价子项需要统计单价
			if (getChildType4QuoteInfo(qis) == ConstantData.RateItem) {
				double netRate = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (QuoteInfo qi : qis) {
					String nr = qi.getAdjustnetrate();
					if (!StringUtil.isNull(nr)) {
						isEmpty = false;
						try {
							netRate += Double.parseDouble(nr);
						} catch (Exception e) {
							logger.error("StatisticNetRate:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return String.valueOf(netRate);
				}
			} else {
				return quoteInfo.getAdjustnetrate();
			}
		} else {
			return quoteInfo.getAdjustnetrate();
		}
	}

	/**
	 * 统计清单单价
	 * 
	 * @return
	 */
	private String statisticNetRate(Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
		List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
		if (billitem4tbqs != null && hasChild4QuotedBillitem(billitem4tbqs)) {
			// 只有单价子项需要统计单价
			if (getChildType4QuotedBillitem(billitem4tbqs) == ConstantData.RateItem) {
				double netRate = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					String nr = billitem4tbq.getNetrate();
					if (!StringUtil.isNull(nr)) {
						isEmpty = false;
						try {
							netRate += Double.parseDouble(nr);
						} catch (Exception e) {
							logger.error("StatisticNetRate:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netRate,
							ConstantData.ACCURACY);
				}
			} else {
				return quoted_Billitem4tbq.getNetrate();
			}
		} else {
			return quoted_Billitem4tbq.getNetrate();
		}
	}

	/**
	 * 统计清单调整单价
	 * 
	 * @return
	 */
	private String statisticAdjustNetRate(
			Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
		List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
		if (billitem4tbqs != null && hasChild4QuotedBillitem(billitem4tbqs)) {
			// 只有单价子项需要统计单价
			if (getChildType4QuotedBillitem(billitem4tbqs) == ConstantData.RateItem) {
				double netRate = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					String nr = billitem4tbq.getAdjustnetrate();
					if (!StringUtil.isNull(nr)) {
						isEmpty = false;
						try {
							netRate += Double.parseDouble(nr);
						} catch (Exception e) {
							logger.error("StatisticNetRate:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netRate,
							ConstantData.ACCURACY);
				}
			} else {
				return quoted_Billitem4tbq.getAdjustnetrate();
			}
		} else {
			return quoted_Billitem4tbq.getAdjustnetrate();
		}
	}

	/**
	 * 统计清单采纳单价
	 * 
	 * @param item
	 * @param map
	 * @return
	 */
	private String statisticAdoptNetRate(EvaluationBillItem item,
			Map<Integer, List<EvaluationBillItem>> map) {
		int billitemid = item.getBillitemid();
		List<EvaluationBillItem> billitem4tbqs = map.get(billitemid);
		if (billitem4tbqs != null && hasChild4EvaluationBillitem(billitem4tbqs)) {
			// 只有单价子项需要统计单价
			if (getChildType4EvaluationBillitem(billitem4tbqs) == ConstantData.RateItem) {
				double netRate = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (EvaluationBillItem billitem4tbq : billitem4tbqs) {
					String nr = billitem4tbq.getAdoptedNetrate();
					if (!StringUtil.isNull(nr)) {
						isEmpty = false;
						try {
							netRate += Double.parseDouble(nr);
						} catch (Exception e) {
							logger.error("StatisticNetRate:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netRate,
							ConstantData.ACCURACY);
				}
			} else {
				return item.getAdoptedNetrate();
			}
		} else {
			return item.getAdoptedNetrate();
		}
	}

	/**
	 * 统计报价商总价
	 * 
	 * @param quoteInfo
	 * @param map
	 * @return
	 */
	private String statisticNetAmount(QuoteInfo quoteInfo,
			Map<Integer, List<QuoteInfo>> map) {
		// 对于仅报总价的清单，直接取TBQ上传的总价，其他类型清单需要从子清单去统计
		byte pricetype = quoteInfo.getPricetype();
		String qty = quoteInfo.getQty();
		String netrate = quoteInfo.getNetrate();
		int billitemid = quoteInfo.getBillitemid();
		List<QuoteInfo> qis = map.get(billitemid);
		// 如果子项不存在，并且自己属于只报总价，就直接取自己的总价，否则判断自己的单价和工程量都不为空，就用自己的单价和工程量相乘，否则用子项去统计
		if (qis != null && hasChild4QuoteInfo(qis)) {
			if (getChildType4QuoteInfo(qis) != ConstantData.RateItem) {
				// 子项类型不是单价子项，那么一定是子项累积统计
				double netAmount = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (QuoteInfo qi : qis) {
					String na = statisticNetAmount(qi, map);
					if (!StringUtil.isNull(na)) {
						isEmpty = false;
						try {
							netAmount += Double.parseDouble(na);
						} catch (Exception e) {
							logger.error("StatisticNetAmount:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netAmount,
							ConstantData.ACCURACY);
				}
			} else {
				if (pricetype == ConstantData.OnlyAmount) {
					return quoteInfo.getNetamount();
				} else if (!StringUtil.isNull(qty)
						&& !StringUtil.isNull(netrate)) {
					return calculateNetAmount(qty, netrate);
				} else {
					return "";
				}
			}
		} else if (pricetype == ConstantData.OnlyAmount) {
			return quoteInfo.getNetamount();
		} else if (pricetype == ConstantData.OnlyRate) {
			// 只报单价，又没有子项，合价一定是空
			return "";
		} else if (!StringUtil.isNull(qty) && !StringUtil.isNull(netrate)) {
			String na=calculateNetAmount(qty, netrate);
			if ( pricetype == ConstantData.FixAmount || pricetype == ConstantData.FixRate ){
				qty = DataUtil.double2String(Double.parseDouble(qty), quoteInfo.accuracy);
				netrate = DataUtil.double2String(Double.parseDouble(netrate),  quoteInfo.accuracy);
			    String na2 = DataUtil.double2String(Double.parseDouble(qty)	* Double.parseDouble(netrate),  quoteInfo.accuracy);
			    logger.debug("na={} na2={} getAdjustnetamount={}", na, na2, quoteInfo.getAdjustnetamount()); 
			    if ( !na.equals( na2 ) ){
			    	logger.debug("na1={} na2={}", na, na2);			    	
			        na= na2;
			    }
			     
			}			 
			return na;
		} else {
			return "";
		}
	}

	/**
	 * 统计报价商调整总价
	 * 
	 * @param quoteInfo
	 * @param map
	 * @return
	 */
	private String statisticAdjustNetAmount(QuoteInfo quoteInfo,
			Map<Integer, List<QuoteInfo>> map) {
		// 对于仅报总价的清单，直接取TBQ上传的总价，其他类型清单需要从子清单去统计
		byte pricetype = quoteInfo.getPricetype();
		String qty = quoteInfo.getQty();
		String adjustnetrate = quoteInfo.getAdjustnetrate();
		int billitemid = quoteInfo.getBillitemid();
		List<QuoteInfo> qis = map.get(billitemid);
		// 如果子项不存在，并且自己属于只报总价，就直接取自己的总价，否则判断自己的单价和工程量都不为空，就用自己的单价和工程量相乘，否则用子项去统计
		if (qis != null && hasChild4QuoteInfo(qis)) {
			if (getChildType4QuoteInfo(qis) != ConstantData.RateItem) {
				// 子项类型不是单价子项，那么一定是子项累积统计
				double netAmount = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (QuoteInfo qi : qis) {
					String na = statisticAdjustNetAmount(qi, map);
					if (!StringUtil.isNull(na)) {
						isEmpty = false;
						try {
							netAmount += Double.parseDouble(na);
						} catch (Exception e) {
							logger.error("StatisticAdjustNetAmount:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netAmount,
							ConstantData.ACCURACY);
				}
			} else {
				if (pricetype == ConstantData.OnlyAmount) {
					return quoteInfo.getAdjustnetamount();
				} else if (!StringUtil.isNull(qty)
						&& !StringUtil.isNull(adjustnetrate)) {
					return calculateNetAmount(qty, adjustnetrate);
				} else {
					return "";
				}
			}
		} else if (pricetype == ConstantData.OnlyAmount) {
			return quoteInfo.getAdjustnetamount();
		} else if (pricetype == ConstantData.OnlyRate) {
			// 只报单价，又没有子项，合价一定是空
			return "";
		} else if (!StringUtil.isNull(qty) && !StringUtil.isNull(adjustnetrate)) {
			
			if ( pricetype == ConstantData.FixAmount  || pricetype == ConstantData.FixRate)				
			    return quoteInfo.getAdjustnetamount();
			
			return calculateNetAmount(qty, adjustnetrate);
		} else {
			return "";
		}
	}

	/**
	 * 统计清单总价
	 * 
	 * @return
	 */
	private String statisticNetAmount(Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		// 对于仅报总价的清单，直接取TBQ上传的总价，其他类型清单需要从子清单去统计
		byte pricetype = quoted_Billitem4tbq.getPricetype();
		String qty = quoted_Billitem4tbq.getQty();
		String netrate = quoted_Billitem4tbq.getNetrate();
		int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
		List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
		// 如果子项不存在，并且自己属于只报总价，就直接取自己的总价，否则判断自己的单价和工程量都不为空，就用自己的单价和工程量相乘，否则用子项去统计
		if (billitem4tbqs != null && hasChild4QuotedBillitem(billitem4tbqs)) {
			if (getChildType4QuotedBillitem(billitem4tbqs) != ConstantData.RateItem) {
				// 子项类型不是单价子项，那么一定是子项累积统计
				double netAmount = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					String na = statisticNetAmount(billitem4tbq, map);
					if (!StringUtil.isNull(na)) {
						isEmpty = false;
						try {
							netAmount += Double.parseDouble(na);
						} catch (Exception e) {
							logger.error("StatisticNetAmount:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netAmount,
							ConstantData.ACCURACY);
				}
			} else {
				if (pricetype == ConstantData.OnlyAmount) {
					return quoted_Billitem4tbq.getNetamount();
				} else if (!StringUtil.isNull(qty)
						&& !StringUtil.isNull(netrate)) {
					return calculateNetAmount(qty, netrate);
				} else {
					return "";
				}
			}
		} else if (pricetype == ConstantData.OnlyAmount) {
			return quoted_Billitem4tbq.getNetamount();
		} else if (pricetype == ConstantData.OnlyRate) {
			// 只报单价，又没有子项，合价一定是空
			return "";
		} else if (!StringUtil.isNull(qty) && !StringUtil.isNull(netrate)) {
			return calculateNetAmount(qty, netrate);
		} else {
			return "";
		}
	}

	/**
	 * 统计清单调整总价
	 * 
	 * @return
	 */
	private String statisticAdjustNetAmount(
			Quoted_Billitem4tbq quoted_Billitem4tbq,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		// 对于仅报总价的清单，直接取TBQ上传的总价，其他类型清单需要从子清单去统计
		byte pricetype = quoted_Billitem4tbq.getPricetype();
		String qty = quoted_Billitem4tbq.getQty();
		String adjustnetrate = quoted_Billitem4tbq.getAdjustnetrate();
		int tbqbillitemid = quoted_Billitem4tbq.getTbqbillitemid();
		List<Quoted_Billitem4tbq> billitem4tbqs = map.get(tbqbillitemid);
		// 如果子项不存在，并且自己属于只报总价，就直接取自己的总价，否则判断自己的单价和工程量都不为空，就用自己的单价和工程量相乘，否则用子项去统计
		if (billitem4tbqs != null && hasChild4QuotedBillitem(billitem4tbqs)) {
			if (getChildType4QuotedBillitem(billitem4tbqs) != ConstantData.RateItem) {
				// 子项类型不是单价子项，那么一定是子项累积统计
				double netAmount = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
					String na = statisticAdjustNetAmount(billitem4tbq, map);
					if (!StringUtil.isNull(na)) {
						isEmpty = false;
						try {
							if ( quoted_Billitem4tbq.vendor.equals("PTE") )
								na = DataUtil.double2String(Double.parseDouble(na),quoted_Billitem4tbq.accuracy);
							netAmount += Double.parseDouble(na);
						} catch (Exception e) {
							logger.error("StatisticNetAmount:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netAmount,
							ConstantData.ACCURACY);
				}

			} else {
				// 子项类型是单价子项，如果自己的单价和工程量都不为空，就用自己的单价和工程量相乘
				if (pricetype == ConstantData.OnlyAmount) {
					return quoted_Billitem4tbq.getAdjustnetamount();
				} else if (!StringUtil.isNull(qty)
						&& !StringUtil.isNull(adjustnetrate)) {
					String na= calculateNetAmount(qty, adjustnetrate);
					if ( pricetype == ConstantData.FixAmount  || pricetype == ConstantData.FixRate){
						//qty = DataUtil.double2String(Double.parseDouble(qty),quoted_Billitem4tbq.accuracy);
					    //adjustnetrate = DataUtil.double2String(Double.parseDouble(adjustnetrate),quoted_Billitem4tbq.accuracy);
					    String na2 = DataUtil.double2String(Double.parseDouble(qty)	* Double.parseDouble(adjustnetrate), quoted_Billitem4tbq.accuracy);
					    if ( !na.equals( na2 ) ){
					    	logger.debug("na1={} na2={}", na, na2);			    	
					        na= na2;
					     }	
					}
					return na;
					
				} else {
					return "";
				}
			}
		} else if (pricetype == ConstantData.OnlyAmount) {
			return quoted_Billitem4tbq.getAdjustnetamount();
		} else if (pricetype == ConstantData.OnlyRate) {
			// 只报单价，又没有子项，合价一定是空
			return "";
		} else if (!StringUtil.isNull(qty) && !StringUtil.isNull(adjustnetrate)) {
			String na= calculateNetAmount(qty, adjustnetrate);
			if ( pricetype == ConstantData.FixAmount  || pricetype == ConstantData.FixRate){
				//qty = DataUtil.double2String(Double.parseDouble(qty),quoted_Billitem4tbq.accuracy);
			    //adjustnetrate = DataUtil.double2String(Double.parseDouble(adjustnetrate),quoted_Billitem4tbq.accuracy);
			    String na2 = DataUtil.double2String(Double.parseDouble(qty)	* Double.parseDouble(adjustnetrate), quoted_Billitem4tbq.accuracy);
			    if ( !na.equals( na2 ) ){
			    	logger.debug("na1={} na2={}", na, na2);			    	
			        na= na2;
			     }	
			}
			return na;
			
		} else {
			return "";
		}
	}

	private String statisticAdoptNetAmount(EvaluationBillItem item,
			Map<Integer, List<EvaluationBillItem>> map) {
		// 对于仅报总价的清单，直接取TBQ上传的总价，其他类型清单需要从子清单去统计
		byte pricetype = item.getPricetype();
		String qty = item.getQty();
		String adoptednetrate = item.getAdoptedNetrate();
		int billitemid = item.getBillitemid();
		List<EvaluationBillItem> billitem4tbqs = map.get(billitemid);
		// 如果子项不存在，并且自己属于只报总价，就直接取自己的总价，否则判断自己的单价和工程量都不为空，就用自己的单价和工程量相乘，否则用子项去统计
		if (billitem4tbqs != null && hasChild4EvaluationBillitem(billitem4tbqs)) {
			if (getChildType4EvaluationBillitem(billitem4tbqs) != ConstantData.RateItem) {
				// 子项类型不是单价子项，那么一定是子项累积统计
				double netAmount = 0;
				// 空和0是不一样的，所以这里需要做是否为空的判断
				boolean isEmpty = true;
				for (EvaluationBillItem billitem4tbq : billitem4tbqs) {
					String na = statisticAdoptNetAmount(billitem4tbq, map);
					if (!StringUtil.isNull(na)) {
						isEmpty = false;
						try {
							netAmount += Double.parseDouble(na);
						} catch (Exception e) {
							logger.error("StatisticNetAmount:", e);
						}
					}
				}
				if (isEmpty) {
					return "";
				} else {
					return DataUtil.double2String(netAmount,
							ConstantData.ACCURACY);
				}
			} else {
				// 子项类型是单价子项，如果自己的单价和工程量都不为空，就用自己的单价和工程量相乘
				if (pricetype == ConstantData.OnlyAmount) {
					return item.getAdoptedNetamount();
				} else if (!StringUtil.isNull(qty)
						&& !StringUtil.isNull(adoptednetrate)) {
					return calculateNetAmount(qty, adoptednetrate);
				} else {
					return "";
				}
			}
		} else if (pricetype == ConstantData.OnlyAmount) {
			return item.getAdoptedNetamount();
		} else if (pricetype == ConstantData.OnlyRate) {
			// 只报单价，又没有子项，合价一定是空
			return "";
		} else if ( pricetype == ConstantData.FixAmount )
			return item.getAdoptedNetamount();
		else if ( pricetype == ConstantData.FixRate ){ 		
			String na= calculateNetAmount(qty, adoptednetrate);
			if  (!StringUtil.isNull(qty)
				&& !StringUtil.isNull(adoptednetrate)) {
			
				qty = DataUtil.double2String(Double.parseDouble(qty), item.accuracy);
				adoptednetrate = DataUtil.double2String(Double.parseDouble(adoptednetrate),item.accuracy);
			    String na2 = DataUtil.double2String(Double.parseDouble(qty)	* Double.parseDouble(adoptednetrate), item.accuracy);
			    if ( !na.equals( na2 ) ){
			    	logger.debug("na1={} na2={}", na, na2);			    	
			        na= na2;
			    }
			}
			return na;
			
		} else if  (!StringUtil.isNull(qty)
				&& !StringUtil.isNull(adoptednetrate)) {
			return calculateNetAmount(qty, adoptednetrate);
		}else{	
			return "";
		}

	}

	/**
	 * 计算合价
	 * 
	 * @param qty
	 * @param adjustnetrate
	 * @return
	 */
	private String calculateNetAmount(String qty, String adjustnetrate) {
		try {
			return DataUtil
					.double2String(
							Double.parseDouble(qty)
									* Double.parseDouble(adjustnetrate),
							ConstantData.ACCURACY);
		} catch (Exception e) {
			logger.error("CalculateNetAmount:", e);
			return "";
		}
	}

	private void addQuoteBillItem(List<QuoteBillItem> items, int billitempid,
			int pbillitemPriceType, List<Quoted_Billitem4tbq> billitem4tbqs,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		if (billitem4tbqs != null) {
			for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
				String qty = billitem4tbq.getQty();
				String netrate = billitem4tbq.getAdjustnetrate();
				String netamount = billitem4tbq.getAdjustnetamount();
				byte pricetype = billitem4tbq.getPricetype();
				QuoteBillItem item = new QuoteBillItem();
				item.setBillitemid(billitem4tbq.getBillitemid());
				item.setBillitempid(billitempid);
				item.setDescription(billitem4tbq.getDescription());
				item.setTrade(billitem4tbq.getTrade());
				item.setUnit(billitem4tbq.getUnit());
				item.setType(billitem4tbq.getType());
				item.setQty(qty);
				item.setNetrate(netrate);
				item.setNetamount(netamount);
				item.setPricetype(pricetype);
				item.setRemark(billitem4tbq.getRemark());
				// 先获取子清单
				int tbqbillitemid = billitem4tbq.getTbqbillitemid();
				List<Quoted_Billitem4tbq> billitems = map.get(tbqbillitemid);
				// 工程量的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else if (item.getType() == ConstantData.Heading) {
					// 标题绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是普通清单或者固定单价清单才显示工程量
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setQtyShow(true);
							}
						}
					} else {
						// 该清单属于叶子清单，根据清单价格类型决定是否显示工程量
						if (item.getType() == ConstantData.BillItem) {
							// 只有正常类型，固定单价才显示工程量
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setQtyShow(true);
							}
						}
					}
				}
				// 单价的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else if (item.getType() == ConstantData.Heading) {
					// 标题绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是正常，仅报单价，固定单价才显示单价
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyRate
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setNetrateShow(true);
							}
						}
					} else {
						// 该清单属于叶子清单，根据清单报价类型决定是否显示单价，只有正常，仅报单价，固定单价显示单价
						if (item.getType() == ConstantData.BillItem) {
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyRate
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setNetrateShow(true);
							}
						} else if (item.getType() == ConstantData.RateItem) {
							item.setNetrateShow(true);
						}
					}
				}
				// 合价的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是正常，仅报单价，固定单价才显示合价
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyAmount
									|| item.getPricetype() == ConstantData.FixRate
									|| item.getPricetype() == ConstantData.FixAmount) {
								item.setNetamountShow(true);
							}
						} else {
							// 下级清单属于清单，不管什么价格类型，都显示合价
							item.setNetamountShow(true);
						}
					} else {
						if (item.getType() == ConstantData.Heading
								|| item.getType() == ConstantData.BillItem) {
							// 该清单属于叶子清单，根据清单报价类型决定是否显示合价，除了不报价，其他情况都显示合价
							if (item.getPricetype() != ConstantData.LumpSumItem
									&& item.getPricetype() != ConstantData.OnlyRate) {
								item.setNetamountShow(true);
							}
						}
					}
				}
				// 单价的编辑判断
				if (billitems != null && hasChild4QuotedBillitem(billitems)) {
					// 该清单属于父清单，单价不允许编辑
				} else {
					// 该清单属于叶子清单
					if (item.getType() == ConstantData.RateItem) {
						// 单价子项，只有对应父清单是普通清单或者是只报单价清单才可以修改
						if (pbillitemPriceType == ConstantData.Normal
								|| pbillitemPriceType == ConstantData.OnlyRate) {
							item.setNetrateEdit(true);
						}
					} else if (item.getType() == ConstantData.BillItem) {
						// 清单子项，根据清单报价类型决定是否可以编辑
						if (item.getPricetype() == ConstantData.Normal
								|| item.getPricetype() == ConstantData.OnlyRate) {
							item.setNetrateEdit(true);
						}
					}
				}
				// 合价的编辑判断
				if (billitems != null && hasChild4QuotedBillitem(billitems)) {
					if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
						// 下级清单属于单价子项，只报总价清单，才可以修改
						if (item.getPricetype() == ConstantData.OnlyAmount) {
							item.setNetamountEdit(true);
						}
					} else {
						// 下级清单属于清单，那么都不能编辑
					}
				} else {
					// 该清单属于叶子清单
					if (item.getType() == ConstantData.RateItem) {
						// 单价子项，合价绝对不允许编辑
					} else {
						// 清单子项，根据清单报价类型决定是否可以编辑，只报总价清单，才可以修改
						if (item.getPricetype() == ConstantData.OnlyAmount) {
							item.setNetamountEdit(true);
						}
					}
				}
				items.add(item);
				addQuoteBillItem(items, item.getBillitemid(),
						item.getPricetype(), billitems, map);
			}
		}
	}

	private void addEvaluationBillItem(List<EvaluationBillItem> items,
			int billitempid, int pbillitemPriceType,
			List<Quoted_Billitem4tbq> billitem4tbqs,
			Map<Integer, List<Quoted_Billitem4tbq>> map) {
		if (billitem4tbqs != null) {
			for (Quoted_Billitem4tbq billitem4tbq : billitem4tbqs) {
				String qty = billitem4tbq.getQty();
				String netrate = billitem4tbq.getNetrate();
				String netamount = billitem4tbq.getNetamount();
				String adjustnetrate = billitem4tbq.getAdjustnetrate();
				String adjustnetamount = billitem4tbq.getAdjustnetamount();
				byte pricetype = billitem4tbq.getPricetype();
				EvaluationBillItem item = new EvaluationBillItem();
				int billitemid = billitem4tbq.getBillitemid();
				item.setBillitemid(billitemid);
				item.setBillitempid(billitempid);
				item.setDescription(billitem4tbq.getDescription());
				item.setTrade(billitem4tbq.getTrade());
				item.setUnit(billitem4tbq.getUnit());
				item.setType(billitem4tbq.getType());
				item.setQty(qty);
				item.setNetrate(netrate);
				item.setAdjustnetrate(adjustnetrate);
				item.setNetamount(netamount);
				item.setAdjustnetamount(adjustnetamount);
				item.setPricetype(pricetype);
				item.setRemark(billitem4tbq.getRemark());
				// 获取采纳价格信息
				Quoted_Adopt4tbq quoted_Adopt4tbq = quoted_Adopt4tbqMapper
						.selectByPrimaryKey(billitemid);
				
				if (quoted_Adopt4tbq != null) {
					logger.info("quoted_Adopt4tbq.getUserid()={}", quoted_Adopt4tbq.getUserid());
					logger.info("billitemid={} item.getAdjustnetrate()={} item.getAdjustnetamount()", billitemid, item.getAdjustnetrate(), item.getAdjustnetamount());
					
					item.setAdoptedUserID(quoted_Adopt4tbq.getUserid());
					item.setAdoptedNetrate(quoted_Adopt4tbq.getAdoptnetrate());
					item.setAdoptedNetamount(quoted_Adopt4tbq
							.getAdoptnetamount());
					logger.info("item.getAdoptedNetamount()={}", item.getAdoptedNetamount());
					item.setAdoptedRemark(quoted_Adopt4tbq.getAdoptremark());
				} else {
					if (item.getPricetype() == ConstantData.FixAmount
							|| item.getPricetype() == ConstantData.FixRate) {
						// 固定单价和固定总价，价格要带入采纳值
						item.setAdoptedNetrate(item.getAdjustnetrate());
						item.setAdoptedNetamount(item.getAdjustnetamount());
					}
				}
				// 先获取子清单
				int tbqbillitemid = billitem4tbq.getTbqbillitemid();
				List<Quoted_Billitem4tbq> billitems = map.get(tbqbillitemid);
				// 工程量的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else if (item.getType() == ConstantData.Heading) {
					// 标题绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是普通清单或者固定单价清单才显示工程量
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setQtyShow(true);
							}
						}
					} else {
						// 该清单属于叶子清单，根据清单价格类型决定是否显示工程量
						if (item.getType() == ConstantData.BillItem) {
							// 只有正常类型，固定单价才显示工程量
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setQtyShow(true);
							}
						}
					}
				}
				// 单价的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else if (item.getType() == ConstantData.Heading) {
					// 标题绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是正常，仅报单价，固定单价才显示单价
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyRate
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setNetrateShow(true);
							}
						}
					} else {
						// 该清单属于叶子清单，根据清单报价类型决定是否显示单价，只有正常，仅报单价，固定单价显示单价
						if (item.getType() == ConstantData.BillItem) {
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyRate
									|| item.getPricetype() == ConstantData.FixRate) {
								item.setNetrateShow(true);
							}
						} else if (item.getType() == ConstantData.RateItem) {
							item.setNetrateShow(true);
						}
					}
				}
				// 合价的显示判断
				if (item.getType() == ConstantData.Note) {
					// 说明项绝对不显示
				} else {
					// 其他按照逻辑判断
					if (billitems != null && hasChild4QuotedBillitem(billitems)) {
						// 该清单属于父清单
						if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
							// 下级清单属于单价子项，那么清单报价类型是正常，仅报单价，固定单价才显示合价
							if (item.getPricetype() == ConstantData.Normal
									|| item.getPricetype() == ConstantData.OnlyAmount
									|| item.getPricetype() == ConstantData.FixRate
									|| item.getPricetype() == ConstantData.FixAmount) {
								item.setNetamountShow(true);
							}
						} else {
							// 下级清单属于清单，不管什么价格类型都显示合价
							item.setNetamountShow(true);
						}
					} else {
						if (item.getType() == ConstantData.Heading
								|| item.getType() == ConstantData.BillItem) {
							// 该清单属于叶子清单，根据清单报价类型决定是否显示合价，除了不报价，其他情况都显示合价
							if (item.getPricetype() != ConstantData.LumpSumItem
									&& item.getPricetype() != ConstantData.OnlyRate) {
								item.setNetamountShow(true);
							}
						}
					}
				}
				// 单价的编辑判断
				if (billitems != null && hasChild4QuotedBillitem(billitems)) {
					// 该清单属于父清单，单价不允许编辑
				} else {
					// 该清单属于叶子清单
					if (item.getType() == ConstantData.RateItem) {
						// 单价子项，只有对应父清单是普通清单或者是只报单价清单才可以修改
						if (pbillitemPriceType == ConstantData.Normal
								|| pbillitemPriceType == ConstantData.OnlyRate) {
							item.setNetrateEdit(true);
						} else if (pbillitemPriceType == ConstantData.FixRate) {

						}
					} else if (item.getType() == ConstantData.BillItem) {
						// 清单子项，根据清单报价类型决定是否可以编辑
						if (item.getPricetype() == ConstantData.Normal
								|| item.getPricetype() == ConstantData.OnlyRate) {
							item.setNetrateEdit(true);
						}
					}
				}
				// 合价的编辑判断
				if (billitems != null && hasChild4QuotedBillitem(billitems)) {
					if (getChildType4QuotedBillitem(billitems) == ConstantData.RateItem) {
						// 下级清单属于单价子项，只报总价清单，才可以修改
						if (item.getPricetype() == ConstantData.OnlyAmount) {
							item.setNetamountEdit(true);
						} else if (pbillitemPriceType == ConstantData.FixAmount) {

						}
					} else {
						// 下级清单属于清单，那么都不能编辑
					}
				} else {
					// 该清单属于叶子清单
					if (item.getType() == ConstantData.RateItem) {
						// 单价子项，合价绝对不允许编辑
					} else {
						// 清单子项，根据清单报价类型决定是否可以编辑，只报总价清单，才可以修改
						if (item.getPricetype() == ConstantData.OnlyAmount) {
							item.setNetamountEdit(true);
						}
					}
				}
				items.add(item);
				addEvaluationBillItem(items, item.getBillitemid(),
						item.getPricetype(), billitems, map);
			}
		}
	}

	@Override
	public boolean sendSubProject(User currentUser, int projectID,
			String returnTo) throws Exception {
		String userid = currentUser.getUserid();
		// 首先，获取token，该值在邀请用户注册时候会用到
		DES des = new DES();
		String password = des.strDec(currentUser.getPassword(),
				ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
		// 重复尝试指定次数，都获取为空，再返回错误
		String access_token = "";
		String access_token_json = LoginUtil.getAccessTokenInfo(currentUser.getEmail(),
				password);
		if (!StringUtil.isNull(access_token_json)) {
			JSONObject atj = JSON.parseObject(access_token_json);
			access_token = atj.getString("access_token");
		} else {
			logger.info("SendSubProject:", "Can not get access token.");
			return false;
		}
		final List<NoticeEmail> nes = new ArrayList<NoticeEmail>();
		// 其次，从记录文件中获取信息，依次给每个分包工程按照选择的分包商发送信息
		JSONObject jo = getRecord(userid, projectID);
		JSONArray records = jo.getJSONArray("records");
		if (records != null) {
			List<Supplier> suppliers = supplierMapper.loadSupplier(userid);
			// 获取用户对应的所有分包商标识集合
			List<String> sls = new ArrayList<String>();
			for (Supplier supplier : suppliers) {
				sls.add(String.valueOf(supplier.getSupplierid()) + "|"
						+ supplier.getName() + "|" + supplier.getEmail());
			}
			int recordSize = records.size();
			// 所有操作放在一个事务中完成
			for (int i = 0; i < recordSize; i++) {
				// 获取一个分包工程的信息
				JSONObject record = records.getJSONObject(i);
				JSONArray subcontractors = record
						.getJSONArray("subcontractors");
				if (subcontractors == null )
					logger.info("No subcontractors");
				else {
					int subcontractorSize = subcontractors.size();
					if (subcontractorSize > 0) {
						// 分包商存在，才进行发送操作
						String subContract = record.getString("subContract");
						int subProjectID = record.getIntValue("subProjectID");
						if (subProjectID == 0) {
							// 信息保存不全时，会出现分包工程标识为空导致获取值为0的情况，这时候应该绕过这个信息段
							continue;
						}
						String timeZone = record.getString("timeZone");
						String endTime = record.getString("endTime");
						Subproject subproject = new Subproject();
						subproject.setSubprojectid(subProjectID);
						subproject.setTimezone(timeZone);
						subproject.setEndtime(DateUtil.parseDate(endTime));
						// 第一步，更新分包工程信息
						int ret = subprojectMapper
								.updateByPrimaryKeySelective(subproject);
						if (ret != 1 && ret != 0) {
							logger.error("Update subproject failed for {}={}", subContract, subProjectID);
							throw new RuntimeException(
									"Update subproject failed!");
						}
						// 第二步，记录分包工程与分包商的关系
						for (int j = 0; j < subcontractorSize; j++) {
							// 获取一个分包商信息
							JSONObject subcontractor = subcontractors
									.getJSONObject(j);
							String id = subcontractor.getString("id");
							String name = subcontractor.getString("name");
							String email = subcontractor.getString("email");
							String telephone = subcontractor
									.getString("telephone");
							String trade = subcontractor.getString("trade");
							String level = subcontractor.getString("level");
							String address = subcontractor.getString("address");
							String contacts = subcontractor
									.getString("contacts");
							StringBuffer attachInfo = new StringBuffer();
							JSONArray attachs = subcontractor
									.getJSONArray("attachs");
							if (attachs != null) {
								int attachSize = attachs.size();
								for (int k = 0; k < attachSize; k++) {
									JSONObject attach = attachs
											.getJSONObject(k);
									String fileName = attach
											.getString("fileName");
									String attachPath = attach
											.getString("attachPath");
									attachInfo.append(fileName).append("|")
											.append(attachPath).append(";");
								}
							}
							if (sls.contains(id + "|" + name + "|" + email)) {
								// 用户分包商包含该分包商
								Supplier_Subproject supplier_Subproject = new Supplier_Subproject();
								supplier_Subproject
										.setSubprojectid(subProjectID);
								supplier_Subproject.setName(name);
								supplier_Subproject.setEmail(email);
								supplier_Subproject.setTelephone(telephone);
								supplier_Subproject.setTrade(trade);
								supplier_Subproject.setLevel(level);
								supplier_Subproject.setAddress(address);
								supplier_Subproject.setContacts(contacts);
								supplier_Subproject.setUserid(userid);
								supplier_Subproject.setAttachinfo(attachInfo
										.toString());
								// 添加记录
								if (supplier_SubprojectMapper
										.insertSelective(supplier_Subproject) != 1) {
									logger.error("Save supplier_subproject failed for {}={}",supplier_Subproject.getEmail(), supplier_Subproject.getSubprojectid());
									throw new RuntimeException(
											"Save supplier_subproject failed!");
								} else {
									NoticeEmail ne = new NoticeEmail();
									ne.setUser(currentUser);
									ne.setSubContract(subContract);
									ne.setEmail(email);
									ne.setName(name);
									ne.setReturnTo(returnTo);
									ne.setAccess_token(access_token);
									nes.add(ne);
								}
							}
						}
					}
				}
			}
		}
		boolean ret = deleteRecord(userid, projectID);
		
		if (ret) {
			logger.info("deleteRecord for {}={}", userid, projectID);
			// 成功才发邮件
			if (nes.size() > 0) {
				// TODO
				// 向分包商发送邮件提醒，放在独立的线程中执行，提高响应速度，但是目前不考虑发送是否成功，未来此处可按照业务逻辑改进
				new Thread(new Runnable() {
					@Override
					public void run() {
						sendNoticeEmail(nes);
					}
				}).start();
			}
		} else {
			throw new RuntimeException("Delete record failed!");
		}
		// 成功执行到这里，那么代表操作成功，需要删除记录
		return ret;
	}

	private void sendNoticeEmail(List<NoticeEmail> nes) {
		for (NoticeEmail ne : nes) {
			try {
				String email = ne.getEmail();
				String returnTo = ne.getReturnTo();
				String name = ne.getName();
				String access_token = ne.getAccess_token();
				User currentUser = ne.getUser();
				String subContract = ne.getSubContract();
				// 判断邮箱是否存在
				if (!containEmail(email)) {
					if (validEmail(email) == ConstantData.OK) {
						// 邮箱还未注册，先邀请注册，再发邮件，邮件是否发送成功结果不影响后续逻辑
						InviteUserDomain inviteUserDomain = new InviteUserDomain();
						inviteUserDomain.setMessage("Join etender");
						inviteUserDomain.setReturnTo(returnTo);
						List<InvitedUser> invitedUserList = new ArrayList<InvitedUser>();
						InvitedUser invitedUser = new InvitedUser();
						invitedUser.setIdentity(email);
						invitedUser.setPassword(ConstantData.DefaultPassword);
						invitedUser.setNickname(name);
						invitedUserList.add(invitedUser);
						inviteUserDomain.setInvitedUserList(invitedUserList);
						String res = RegisterUtil.invite(inviteUserDomain,
								access_token);
						logger.info("Invite user:", res);
						JSONObject jo = JSON.parseObject(res);
						if (jo.getString("ret").equalsIgnoreCase(
								RegisterUtil.RESULT_200)) {
							JSONArray ja = jo.getJSONArray("inviteUser");
							if (ja.size() > 0) {
								// 邀请成功，记录用户信息
								User user = new User();
								user.setUserid(ja.getJSONObject(0).getString(
										"id"));
								user.setNickname(name);
								DES des = new DES();
								user.setPassword(des.strEnc(
										ConstantData.DefaultPassword,
										ConstantData.FirstKey,
										ConstantData.SecondKey,
										ConstantData.ThirdKey));
								user.setEmail(email);
								user.setName(name);
								// 这里不判断是否保存成功可能存在用户信息丢失风险，不过即使用户信息丢失，到时可以在完善个人信息中再次完善
								saveUser(user);
							}
							registerEmail(email);
						}
						doSendNoticeEmail(currentUser, subContract, email,
								name, returnTo, false);
					} else {
						// 邮箱已经注册，邮件是否发送成功结果不影响后续逻辑
						doSendNoticeEmail(currentUser, subContract, email,
								name, returnTo, true);
						registerEmail(email);
					}
				} else {
					// 邮箱已经注册，邮件是否发送成功结果不影响后续逻辑
					doSendNoticeEmail(currentUser, subContract, email, name,
							returnTo, true);
				}
			} catch (Exception e) {
				logger.error("SendNoticeEmail:", e);
			}
		}
	}

	/**
	 * 发送邮件通知
	 * 
	 * @param user
	 * @param subContract
	 * @param email
	 * @param name
	 * @param returnTo
	 * @param hasRegister
	 * @return
	 */
	private boolean doSendNoticeEmail(User user, String subContract,
			String email, String name, String returnTo, boolean hasRegister) {
		logger.info(user.getName() + " send email to " + email + " start.");
		String subject = "Inquiry from Company " + user.getCompany();
		StringBuffer content = new StringBuffer();
		content.append("<p>").append("Dear Mr./Ms. ").append(name).append(",")
				.append("</p>");
		if (hasRegister) {
			content.append("<p>")
					.append("Mr./Ms. ")
					.append(user.getName())
					.append(" from Company ")
					.append(user.getCompany())
					.append(" is inquiring about Project ")
					.append(subContract)
					.append(" by using E-tender. You are invited to tender for the contract. Please check on time and submit quotations by visiting")
					.append(" <a href='" + returnTo + "' target='_blank'>")
					.append(returnTo).append("</a>").append("</p>");
		} else {
			content.append("<p>")
					.append("Mr./Ms. ")
					.append(user.getName())
					.append(" from Company ")
					.append(user.getCompany())
					.append(" is inquiring about Project ")
					.append(subContract)
					.append(" by using E-tender. You are invited to tender for the contract. Please check on time and submit quotations.")
					.append("</p>");
			content.append("<p>")
					.append("You are automatically registered. Please use your email to log in and complete your info by visiting ")
					.append(" <a href='" + returnTo + "' target='_blank'>")
					.append(returnTo).append("</a>").append(" to log in. ")
					.append("</p>");
		}
		content.append("<p>")
				.append("Wish you the best of luck in all of your future endeavors! ")
				.append("</p>");
		content.append("<p>").append("Sincerely,").append("</p>");
		content.append("<p>").append("Glodon E-tender").append("</p>");
		boolean ret = sendEmail(email, subject, content.toString(), null);
		logger.info(user.getName() + " send email to " + email + " finish.");
		return ret;
	}

	@Override
	public List<InquireRecord> loadInquireRecord(int projectID) {
		List<InquireRecord> records = new ArrayList<InquireRecord>();
		List<Subproject> subprojects = subprojectMapper
				.loadSubProject(projectID);
		for (Subproject subproject : subprojects) {
			int subprojectid = subproject.getSubprojectid();
			HashMap<String, Object> selectCondition = new HashMap<String, Object>();
			selectCondition.put("subProjectID", subprojectid);
			List<Supplier_Subproject> supplier_Subprojects = supplier_SubprojectMapper
					.loadSupplierSubProject(selectCondition);
			for (Supplier_Subproject supplier_Subproject : supplier_Subprojects) {
				InquireRecord inquireRecord = new InquireRecord();
				inquireRecord.setSubProjectName(subproject.getName());
				inquireRecord.setSubConName(supplier_Subproject.getName());
				inquireRecord.setSubConEmail(supplier_Subproject.getEmail());
				inquireRecord.setTimeSent(supplier_Subproject.getCreatetime());
				inquireRecord.setTimeReceived(supplier_Subproject
						.getReceivedtime());
				inquireRecord.setStartTime(supplier_Subproject
						.getFirstsavetime());
				inquireRecord.setEndTime(supplier_Subproject.getSubmittime());
				inquireRecord.setDueTime(subproject.getEndtime());
				inquireRecord.setTimeZone(subproject.getTimezone());
				records.add(inquireRecord);
			}
		}
		return records;
	}

	@Override
	public List<QuoteRecord> loadQuoteRecord(Integer queryprojectid,
			String email) {
		List<QuoteRecord> records = new ArrayList<QuoteRecord>();
		// 第一步，获取询价人信息
		Project_Query project_Query = project_QueryMapper
				.selectByPrimaryKey(queryprojectid);
		String inquireUserID = project_Query.getUserid();
		User user = userMapper.selectByPrimaryKey(inquireUserID);
		String mainConName = user.getName();
		String mainConEmail = user.getEmail();
		// 第二步，获取询价工程对应的分包工程标识信息
		List<Integer> subProjectIDs = subprojectMapper
				.loadSubProjectIDs(queryprojectid);
		// 第三步，获取对应供应商的分包工程发送记录
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("subProjectIDs", subProjectIDs);
		selectCondition.put("email", email);
		List<Supplier_Subproject> supplier_Subprojects = supplier_SubprojectMapper
				.loadSupplierSubProject(selectCondition);
		Map<Integer, Subproject> sm = new HashMap<Integer, Subproject>();
		for (Supplier_Subproject supplier_Subproject : supplier_Subprojects) {
			QuoteRecord quoteRecord = new QuoteRecord();
			int subprojectid = supplier_Subproject.getSubprojectid();
			Subproject subproject = sm.get(subprojectid);
			if (subproject == null) {
				subproject = subprojectMapper.selectByPrimaryKey(subprojectid);
				sm.put(subprojectid, subproject);
			}
			quoteRecord.setSubProjectName(subproject.getName());
			quoteRecord.setMainConName(mainConName);
			quoteRecord.setMainConEmail(mainConEmail);
			quoteRecord.setTimeSent(supplier_Subproject.getCreatetime());
			quoteRecord.setTimeReceived(supplier_Subproject.getReceivedtime());
			quoteRecord.setStartTime(supplier_Subproject.getFirstsavetime());
			quoteRecord.setEndTime(supplier_Subproject.getSubmittime());
			quoteRecord.setDueTime(subproject.getEndtime());
			quoteRecord.setTimeZone(subproject.getTimezone());
			quoteRecord.setReviewedAt(supplier_Subproject.getReviewtime());
			records.add(quoteRecord);
		}
		return records;
	}

	@Override
	public boolean updateSupplierReceivedTime(String email,
			Integer queryprojectid) {
		// 获取询价工程对应的分包工程标识信息
		List<Integer> subProjectIDs = subprojectMapper
				.loadSubProjectIDs(queryprojectid);
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("subProjectIDs", subProjectIDs);
		selectCondition.put("email", email);
		supplier_SubprojectMapper.updateSupplierReceivedTime(selectCondition);
		return true;
	}

	@Override
	public int updateBillitem4tbq2quote(User user, String subprojectid,
			String billitemid, String datatype, String data) {
		Quoted_Detail4tbq quoted_Detail4tbq = quoted_Detail4tbqMapper
				.selectBillitemQuoteDetail(Integer.parseInt(billitemid),
						user.getUserid());
		if (quoted_Detail4tbq != null) {
			if (datatype.equalsIgnoreCase("0")) {
				data = data.replaceAll(",", "");
				quoted_Detail4tbq.setNetrate(data);
				quoted_Detail4tbq.setAdjustnetrate(data);
			} else if (datatype.equalsIgnoreCase("1")) {
				data = data.replaceAll(",", "");
				quoted_Detail4tbq.setNetamount(data);
				quoted_Detail4tbq.setAdjustnetamount(data);
			} else if (datatype.equalsIgnoreCase("2")) {
				quoted_Detail4tbq.setRemark(data);
			}
			logger.info("[quote] update quoted_Detail4tbq.setUserid={} Billitemid={}", user.getUserid(), billitemid);
			quoted_Detail4tbqMapper
					.updateByPrimaryKeySelective(quoted_Detail4tbq);
		} else {
			quoted_Detail4tbq = new Quoted_Detail4tbq();
			quoted_Detail4tbq.setBillitemid(Integer.parseInt(billitemid));
			quoted_Detail4tbq.setUserid(user.getUserid());
			if (datatype.equalsIgnoreCase("0")) {
				data = data.replaceAll(",", "");
				quoted_Detail4tbq.setNetrate(data);
				quoted_Detail4tbq.setAdjustnetrate(data);
			} else if (datatype.equalsIgnoreCase("1")) {
				data = data.replaceAll(",", "");
				quoted_Detail4tbq.setNetamount(data);
				quoted_Detail4tbq.setAdjustnetamount(data);
			} else if (datatype.equalsIgnoreCase("2")) {
				quoted_Detail4tbq.setRemark(data);
			}
			logger.info("[quote] update quoted_Detail4tbq.setUserid={} Billitemid={}", user.getUserid(), billitemid);
			quoted_Detail4tbqMapper.insertSelective(quoted_Detail4tbq);
		}
		// 更新用户第一次保存数据时间
		supplier_SubprojectMapper.updateSupplierFirstSaveTime(user.getEmail(),
				subprojectid);
		return quoted_Detail4tbq.getDetailid();
	}

	@Override
	public int updateChangeStatus(String billitemid, String isChange) {
		int itemid = Integer.parseInt(billitemid);
		byte ischange = Byte.parseByte(isChange);
		return quoted_Billitem4tbqMapper.updateChangeStatus(itemid, ischange);
	}

	/**
	 * 根据S列更新报价清单
	 */
	@Override
	public synchronized String  updateBillitem4tbq2evaluation(String supplierid,
			String billitemid, String datatype, String data) {
		int itemid = Integer.parseInt(billitemid);
		if (supplierid.equalsIgnoreCase("-1")) {//S=UD
			Quoted_Adopt4tbq quoted_Adopt4tbq = quoted_Adopt4tbqMapper
					.selectByPrimaryKey(itemid);
			if (quoted_Adopt4tbq != null) {
				if (datatype.equalsIgnoreCase("0")) {
					data = data.replaceAll(",", "");
					quoted_Adopt4tbq.setAdoptnetrate(data);
					quoted_Adopt4tbq.setUserid(supplierid);
				} else if (datatype.equalsIgnoreCase("1")) {
					data = data.replaceAll(",", "");
					quoted_Adopt4tbq.setAdoptnetamount(data);
					quoted_Adopt4tbq.setUserid(supplierid);
				} else if (datatype.equalsIgnoreCase("2")) {
					quoted_Adopt4tbq.setAdoptremark(data);
				}
				quoted_Adopt4tbqMapper
						.updateByPrimaryKeySelective(quoted_Adopt4tbq);
			} else {
				quoted_Adopt4tbq = new Quoted_Adopt4tbq();
				quoted_Adopt4tbq.setBillitemid(itemid);
				quoted_Adopt4tbq.setUserid(supplierid);
				if (datatype.equalsIgnoreCase("0")) {
					data = data.replaceAll(",", "");
					quoted_Adopt4tbq.setAdoptnetrate(data);
				} else if (datatype.equalsIgnoreCase("1")) {
					data = data.replaceAll(",", "");
					quoted_Adopt4tbq.setAdoptnetamount(data);
				} else if (datatype.equalsIgnoreCase("2")) {
					quoted_Adopt4tbq.setAdoptremark(data);
				}
				quoted_Adopt4tbqMapper.insertSelective(quoted_Adopt4tbq);
			}
		} else if (supplierid.equalsIgnoreCase("0")) {//S=PTE
			// 更新标底价格
			if (datatype.equalsIgnoreCase("0")) {
				data = data.replaceAll(",", "");
				quoted_Billitem4tbqMapper.updateAdjustnetrate(itemid, data);
			} else if (datatype.equalsIgnoreCase("1")) {
				data = data.replaceAll(",", "");
				quoted_Billitem4tbqMapper.updateAdjustnetamount(itemid, data);
			} else if (datatype.equalsIgnoreCase("2")) {
				quoted_Billitem4tbqMapper.updateRemark(itemid, data);
			}
		} else {//S=A/B/C...
			Quoted_Detail4tbq quoted_Detail4tbq = quoted_Detail4tbqMapper
					.selectBillitemQuoteDetail(itemid, supplierid);
			if (quoted_Detail4tbq != null) {
				if (datatype.equalsIgnoreCase("0")) {
					data = data.replaceAll(",", "");
					quoted_Detail4tbq.setAdjustnetrate(data);
				} else if (datatype.equalsIgnoreCase("1")) {
					data = data.replaceAll(",", "");
					quoted_Detail4tbq.setAdjustnetamount(data);
				} else if (datatype.equalsIgnoreCase("2")) {
					quoted_Detail4tbq.setRemark(data);
				}
				quoted_Detail4tbqMapper
						.updateByPrimaryKeySelective(quoted_Detail4tbq);
			} else {
				quoted_Detail4tbq = new Quoted_Detail4tbq();
				quoted_Detail4tbq.setBillitemid(itemid);
				quoted_Detail4tbq.setUserid(supplierid);
				if (datatype.equalsIgnoreCase("0")) {
					data = data.replaceAll(",", "");
					quoted_Detail4tbq.setAdjustnetrate(data);
				} else if (datatype.equalsIgnoreCase("1")) {
					data = data.replaceAll(",", "");
					quoted_Detail4tbq.setAdjustnetamount(data);
				} else if (datatype.equalsIgnoreCase("2")) {
					quoted_Detail4tbq.setRemark(data);
				}
				quoted_Detail4tbqMapper.insertSelective(quoted_Detail4tbq);
			}
		}
		return supplierid;
	}

	@Override
	public int updateDiscount4tbq2evaluation(String supplierid,
			String subProjectID, String datatype, String data) {
		Quoted_Discount4tbq quoted_Discount4tbq = quoted_Discount4tbqMapper
				.selectBillitemQuoteDiscount(supplierid,
						Integer.parseInt(subProjectID));
		if (quoted_Discount4tbq != null) {
			if (datatype.equalsIgnoreCase("0")) {
				data = data.replaceAll(",", "");
				quoted_Discount4tbq.setDiscount(data);
				quoted_Discount4tbq.setDiscountpercent("");
			} else if (datatype.equalsIgnoreCase("1")) {
				data = data.replaceAll(",", "");
				quoted_Discount4tbq.setDiscount("");
				quoted_Discount4tbq.setDiscountpercent(data);
			}
			quoted_Discount4tbqMapper
					.updateByPrimaryKeySelective(quoted_Discount4tbq);
		} else {
			quoted_Discount4tbq = new Quoted_Discount4tbq();
			quoted_Discount4tbq.setSubprojectid(Integer.parseInt(subProjectID));
			quoted_Discount4tbq.setUserid(supplierid);
			if (datatype.equalsIgnoreCase("0")) {
				data = data.replaceAll(",", "");
				quoted_Discount4tbq.setDiscount(data);
			} else if (datatype.equalsIgnoreCase("1")) {
				data = data.replaceAll(",", "");
				quoted_Discount4tbq.setDiscountpercent(data);
			}
			quoted_Discount4tbqMapper.insertSelective(quoted_Discount4tbq);
		}
		return quoted_Discount4tbq.getDiscountid();
	}

	@Override
	public String adoptBillitem4tbq2evaluation(String supplierid,
			String adoptNetRate, String adoptNetAmount, String adoptRemark,
			String billitemid) {
		int itemid = Integer.parseInt(billitemid);
		Quoted_Adopt4tbq quoted_Adopt4tbq = quoted_Adopt4tbqMapper
				.selectByPrimaryKey(itemid);
		synchronized( this ) {			
			if (quoted_Adopt4tbq != null) {
				logger.info("update quoted_Adopt4tbq.setUserid={} Billitemid={}", supplierid, itemid);
				quoted_Adopt4tbq.setUserid(supplierid);
				quoted_Adopt4tbq.setAdoptnetrate(adoptNetRate);
				quoted_Adopt4tbq.setAdoptnetamount(adoptNetAmount);
				quoted_Adopt4tbq.setAdoptremark(adoptRemark);
				quoted_Adopt4tbqMapper
						.updateByPrimaryKeySelective(quoted_Adopt4tbq);
			} else {
				logger.info("insert quoted_Adopt4tbq.setUserid={} Billitemid={}", supplierid, itemid);
				quoted_Adopt4tbq = new Quoted_Adopt4tbq();
				quoted_Adopt4tbq.setBillitemid(itemid);
				quoted_Adopt4tbq.setUserid(supplierid);
				quoted_Adopt4tbq.setAdoptnetrate(adoptNetRate);
				quoted_Adopt4tbq.setAdoptnetamount(adoptNetAmount);
				quoted_Adopt4tbq.setAdoptremark(adoptRemark);
				quoted_Adopt4tbqMapper.insertSelective(quoted_Adopt4tbq);
			}
		}
		return supplierid;
	}

	@Override
	public boolean updateMainConReviewTime(Integer projectID) {
		// TODO 获得时间到期可评标的分包工程，到时需要根据时区做判断
		List<Integer> subProjectIDs = subprojectMapper
				.loadEvaluationSubProjectIDs(projectID);
		if (subProjectIDs.size() > 0) {
			supplier_SubprojectMapper.updateMainConReviewTime(subProjectIDs);
		}
		return true;
	}

	@Override
	public int updateSupplierSubmitTime(String email, int subProjectID) {
		Subproject subproject = subprojectMapper
				.selectByPrimaryKey(subProjectID);
		if (new Date().after(subproject.getEndtime())) {
			// TODO 将来需要按照时区做转换，当前时间超过截止时间，不允许提交
			return ConstantData.OverTime;
		}
		supplier_SubprojectMapper.updateSupplierSubmitTime(email, subProjectID);
		return ConstantData.OK;
	}

	@Override
	public boolean isSubProjectSubmit(User user, String subProjectID) {
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("subProjectID", subProjectID);
		selectCondition.put("email", user.getEmail());
		selectCondition.put("logicDelete", "0");
		// TODO 同一个分包工程存在多个发送记录，取最后发送记录的状态，这里未来需要仔细梳理逻辑
		List<Supplier_Subproject> supplier_Subprojects = supplier_SubprojectMapper
				.loadSupplierSubProject(selectCondition);
		if (supplier_Subprojects.size() > 0) {
			if (supplier_Subprojects.get(0).getSubmittime() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int updateInquireProjectStatus2Evaluating(int queryProjectID) {
		Project_Query project_Query = new Project_Query();
		project_Query.setQueryprojectid(queryProjectID);
		project_Query.setStatus(Byte.parseByte(String
				.valueOf(ConstantData.evaluating)));
		return project_QueryMapper.updateByPrimaryKeySelective(project_Query);
	}

	@Override
	public List<AttachMent> loadAttachMents(String email, String subProjectID) {
		List<AttachMent> attachMents = new ArrayList<AttachMent>();
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("subProjectID", subProjectID);
		selectCondition.put("email", email);
		selectCondition.put("logicDelete", "0");
		Supplier_Subproject supplier_Subproject = supplier_SubprojectMapper
				.loadLastSupplierSubProject(selectCondition);
		if (supplier_Subproject != null) {
			String attachinfo = supplier_Subproject.getAttachinfo();
			if (!StringUtil.isNull(attachinfo)) {
				String[] infos = attachinfo.split(";");
				for (String info : infos) {
					String[] is = info.split("\\|");
					if (is.length == 2) {
						AttachMent attachMent = new AttachMent();
						attachMent.setFileName(is[0]);
						attachMent.setAttachPath(is[1]);
						String attachPath = ServletContextUtil.getWebRootPath()
								+ is[1];
						float size = FileUtil.getDirSize(new File(attachPath),
								FileUtil.M);
						attachMent.setFileSize(String.valueOf(size) + "M");
						attachMents.add(attachMent);
					}
				}
			}
		}
		return attachMents;
	}

	@Override
	public int deleteSubProject(String subprojectid) {
		// TODO 删除分包工程的时候，还需要将关联分包工程的记录做删除，这块在后面需要完善
		return subprojectMapper.deleteSubProjectByLogic(Integer
				.parseInt(subprojectid));
	}

	@Override
	public boolean canEvaluation(int queryProjectID) {
		HashMap<String, Object> selectCondition = new HashMap<String, Object>();
		selectCondition.put("queryProjectID", queryProjectID);
		selectCondition.put("logicDelete", 0);
		// TODO 五种状态：Not
		// Inquired、Inquiring、Inquired、Evaluating、Evaluated取状态最慢的分包工程状态，
		// 这个逻辑导致目前数据表中的状态字段没有意义，未来酌情考虑删除数据表中的状态字段或者将状态字段作为它用
		List<Integer> quoteSubProjectIDs = subprojectMapper
				.loadQuoteSubProjectIDs(selectCondition);
		if (quoteSubProjectIDs.size() == 0) {
			// 没有分包工程，代表属于新建的工程
			return false;
		} else {
			// TODO 评标结束要等待回填之后，才能确定该状态，该逻辑在以后需要完善
			for (Integer quoteSubProjectID : quoteSubProjectIDs) {
				// 如果该分包工程还没有发送记录，那么就是未询价，属于最晚状态，直接跳出循环
				int sendRecordCount = supplier_SubprojectMapper
						.loadSendRecordCount(quoteSubProjectID);
				if (sendRecordCount == 0) {

				} else {
					Subproject subproject = subprojectMapper
							.selectByPrimaryKey(quoteSubProjectID);
					// 已经有发送文件
					if (isEndQuote(subproject)) {
						// 当前时间已经超过报价截止时间
						return true;
					} else {
						// 当前时间没有超过报价截止时间

					}
				}
			}
		}
		return false;
	}

	@Override
	public String export4tbq2quote(String treeData, String tableData,
			String summary, String totalAmount, String sheetName) {
		String fileName = StringUtil.getUUID() + ".xls";
		String filePath = WebUtil.getReportPath() + fileName;
		logger.info("will export {}", filePath);
		JSONArray tree = JSON.parseArray(treeData);
		JSONArray table = JSON.parseArray(tableData);
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(new File(filePath));
			WritableSheet wSheet = wwb.createSheet(sheetName, 0);

			ColorConverter colorConverter = new ColorConverter();

			WritableCellFormat head2 = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			head2.setAlignment(Alignment.CENTRE);
			head2.setVerticalAlignment(VerticalAlignment.CENTRE);
			head2.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#d3d3d3")));
			head2.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat head3 = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			head3.setAlignment(Alignment.LEFT);
			head3.setVerticalAlignment(VerticalAlignment.CENTRE);
			head3.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#d5f3f9")));
			head3.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat normal = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 12));
			normal.setAlignment(Alignment.CENTRE);
			normal.setVerticalAlignment(VerticalAlignment.CENTRE);

			int size = tree.size();
			for (int i = 0; i < size; i++) {
				Label l = new Label(0, i, tree.getString(i), normal);
				wSheet.addCell(l);
			}
			size = table.size();
			for (int i = 0; i < size; i++) {
				JSONObject data = table.getJSONObject(i);
				if (i == 0) {
					Label sn = new Label(1, i, data.getString("sn"), head2);
					wSheet.addCell(sn);
					Label description = new Label(2, i,
							data.getString("description"), head2);
					wSheet.addCell(description);
					Label trade = new Label(3, i, data.getString("trade"),
							head2);
					wSheet.addCell(trade);
					Label unit = new Label(4, i, data.getString("unit"), head2);
					wSheet.addCell(unit);
					Label type = new Label(5, i, data.getString("type"), head2);
					wSheet.addCell(type);
					Label qty = new Label(6, i, data.getString("qty"), head2);
					wSheet.addCell(qty);
					Label netRate = new Label(7, i, data.getString("netRate"),
							head2);
					wSheet.addCell(netRate);
					Label netAmount = new Label(8, i,
							data.getString("netAmount"), head2);
					wSheet.addCell(netAmount);
					Label remarks = new Label(9, i, data.getString("remarks"),
							head2);
					wSheet.addCell(remarks);
				} else {
					Label sn = new Label(1, i, data.getString("sn"), normal);
					wSheet.addCell(sn);
					Label description = new Label(2, i,
							data.getString("description"), normal);
					wSheet.addCell(description);
					Label trade = new Label(3, i, data.getString("trade"),
							normal);
					wSheet.addCell(trade);
					Label unit = new Label(4, i, data.getString("unit"), normal);
					wSheet.addCell(unit);
					Label type = new Label(5, i, data.getString("type"), normal);
					wSheet.addCell(type);
					Label qty = new Label(6, i, data.getString("qty"), normal);
					wSheet.addCell(qty);
					Label netRate = new Label(7, i, data.getString("netRate"),
							normal);
					wSheet.addCell(netRate);
					Label netAmount = new Label(8, i,
							data.getString("netAmount"), normal);
					wSheet.addCell(netAmount);
					Label remarks = new Label(9, i, data.getString("remarks"),
							normal);
					wSheet.addCell(remarks);
				}
			}
			wSheet.addCell(new Label(1, size + 1, summary, head3));
			wSheet.mergeCells(1, size + 1, 7, size + 1);
			wSheet.addCell(new Label(8, size + 1, totalAmount, head3));
			wSheet.mergeCells(8, size + 1, 9, size + 1);
			wwb.write();
		} catch (Exception e) {
			logger.error("Export4tbq2quote:", e);
			return null;
		} finally {
			try {
				if (wwb != null) {
					wwb.close();
				}
			} catch (Exception e) {
				logger.error("Close:", e);
			}
		}
		return fileName;
	}

	@Override
	public String export4tbq2evaluation(String treeData, String supplierData,
			String tableData, String statisticData, String adopted, String pte,
			String sheetName) {
		String fileName = StringUtil.getUUID() + ".xls";
		String filePath = WebUtil.getReportPath() + fileName;
		JSONArray tree = JSON.parseArray(treeData);
		JSONArray supplier = JSON.parseArray(supplierData);
		JSONArray table = JSON.parseArray(tableData);
		JSONArray statistic = JSON.parseArray(statisticData);
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(new File(filePath));
			WritableSheet wSheet = wwb.createSheet(sheetName, 0);

			ColorConverter colorConverter = new ColorConverter();

			WritableCellFormat head1 = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			head1.setAlignment(Alignment.CENTRE);
			head1.setVerticalAlignment(VerticalAlignment.CENTRE);
			head1.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#fff9e3")));
			head1.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat head2 = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			head2.setAlignment(Alignment.CENTRE);
			head2.setVerticalAlignment(VerticalAlignment.CENTRE);
			head2.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#d3d3d3")));
			head2.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat head3 = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			head3.setAlignment(Alignment.CENTRE);
			head3.setVerticalAlignment(VerticalAlignment.CENTRE);
			head3.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#d5f3f9")));
			head3.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat odd = new WritableCellFormat(new WritableFont(
					WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			odd.setAlignment(Alignment.CENTRE);
			odd.setVerticalAlignment(VerticalAlignment.CENTRE);
			odd.setBackground(colorConverter.toJXLColour(wwb, new WebColor(
					"#f7f7f8")));
			odd.setBorder(Border.ALL, BorderLineStyle.THIN,
					colorConverter.toJXLColour(wwb, new WebColor("#c3c3c3")));

			WritableCellFormat supplierHead = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 12));
			supplierHead.setAlignment(Alignment.CENTRE);
			supplierHead.setVerticalAlignment(VerticalAlignment.CENTRE);

			WritableCellFormat normal = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 12));
			normal.setAlignment(Alignment.CENTRE);
			normal.setVerticalAlignment(VerticalAlignment.CENTRE);

			int size = tree.size();
			for (int i = 0; i < size; i++) {
				Label l = new Label(0, i, tree.getString(i), normal);
				wSheet.addCell(l);
			}
			wSheet.addCell(new Label(8, 0, adopted, head1));
			wSheet.addCell(new Label(13, 0, pte, head1));
			wSheet.mergeCells(1, 0, 7, 2);
			wSheet.mergeCells(8, 0, 12, 2);
			wSheet.mergeCells(13, 0, 17, 2);
			// 添加分包商数据
			size = supplier.size();
			for (int i = 0; i < size; i++) {
				JSONObject data = supplier.getJSONObject(i);
				int num = data.size();
				int c = 18;
				for (int j = 0; j < num; j++) {
					wSheet.addCell(new Label(c, i, data.getString(j + ""),
							supplierHead));
					if (j % 2 == 0) {
						wSheet.mergeCells(c, i, c + 2, i);
						c += 3;
					} else {
						wSheet.mergeCells(c, i, c + 1, i);
						c += 2;
					}
				}
			}
			// 添加评标数据
			size = table.size();
			for (int i = 0; i < size; i++) {
				JSONObject data = table.getJSONObject(i);
				int num = data.size();
				int c = 1;
				if (i == 0) {
					for (int j = 0; j < num; j++) {
						wSheet.addCell(new Label(c, i + 3, data.getString(j
								+ ""), head2));
						if (j > 8 && ((j - 9) % 4 == 0)) {
							wSheet.mergeCells(c, i + 3, c + 1, i + 3);
							c += 2;
						} else {
							c++;
						}
					}
				} else {
					for (int j = 0; j < num; j++) {
						wSheet.addCell(new Label(c, i + 3, data.getString(j
								+ ""), normal));
						if (j > 8 && ((j - 9) % 4 == 0)) {
							wSheet.mergeCells(c, i + 3, c + 1, i + 3);
							c += 2;
						} else {
							c++;
						}
					}
				}
			}
			// 添加分析统计数据，与评标数据隔一行
			int r = size + 4;
			size = statistic.size();
			for (int i = 0; i < size; i++) {
				JSONObject data = statistic.getJSONObject(i);
				int num = data.size();
				int c = 1;
				if (i == 0) {
					for (int j = 0; j < num; j++) {
						if (j == 0) {
							wSheet.addCell(new Label(c, i + r, data.getString(j
									+ ""), head3));
							wSheet.mergeCells(c, i + r, c + 6, i + r);
							c = 8;
						} else {
							wSheet.addCell(new Label(c, i + r, data.getString(j
									+ ""), head3));
							if (j > 2 && ((j - 3) % 4 == 0)) {
								wSheet.mergeCells(c, i + r, c + 1, i + r);
								c += 2;
							} else {
								c++;
							}
						}
					}
				} else {
					if (i % 2 != 0) {
						for (int j = 0; j < num; j++) {
							if (j == 0) {
								wSheet.addCell(new Label(c, i + r, data
										.getString(j + ""), odd));
								wSheet.mergeCells(c, i + r, c + 6, i + r);
								c = 8;
							} else {
								wSheet.addCell(new Label(c, i + r, data
										.getString(j + ""), odd));
								if (j > 2 && ((j - 3) % 4 == 0)) {
									wSheet.mergeCells(c, i + r, c + 1, i + r);
									c += 2;
								} else {
									c++;
								}
							}
						}
					} else {
						for (int j = 0; j < num; j++) {
							if (j == 0) {
								wSheet.addCell(new Label(c, i + r, data
										.getString(j + ""), normal));
								wSheet.mergeCells(c, i + r, c + 6, i + r);
								c = 8;
							} else {
								wSheet.addCell(new Label(c, i + r, data
										.getString(j + ""), normal));
								if (j > 2 && ((j - 3) % 4 == 0)) {
									wSheet.mergeCells(c, i + r, c + 1, i + r);
									c += 2;
								} else {
									c++;
								}
							}
						}
					}
				}
			}
			wwb.write();
		} catch (Exception e) {
			logger.error("Export4tbq2evaluation:", e);
			return null;
		} finally {
			try {
				if (wwb != null) {
					wwb.close();
				}
			} catch (Exception e) {
				logger.error("Close:", e);
			}
		}
		return fileName;
	}

	@Override
	public JSONObject loadLastTBQProject(String userid, int tbqProjectID) {
		JSONObject project = new JSONObject();
		Project_Query project_Query = project_QueryMapper.loadLastTBQProject(
				userid, tbqProjectID);
		if (project_Query != null) {
			int queryProjectID = project_Query.getQueryprojectid();
			List<Integer> subProjectIDs = subprojectMapper
					.loadEvaluationSubProjectIDs(queryProjectID);
			if (subProjectIDs.size() == 0) {
				// 未到期
				project.put("ret", "0");
			} else {
				// 到期，先判断是否有采纳价格
				boolean adopted = false;
				for (Integer subProjectID : subProjectIDs) {
					List<Integer> eleIDs = quoted_Element4tbqMapper
							.loadTBQElementIDs(subProjectID,
									ConstantData.SECTION);
					if (eleIDs != null && eleIDs.size() > 0) {
						List<Integer> itemIDs = quoted_Billitem4tbqMapper
								.loadBillItemIDs(eleIDs);
						if (itemIDs != null && itemIDs.size() > 0) {
							int count = quoted_Adopt4tbqMapper
									.loadAdoptItemCount(itemIDs);
							if (count > 0) {
								adopted = true;
								break;
							}
						}
					}
				}
				if (adopted) {
					project.put("ret", "1");
					project.put("tbqProjectID", tbqProjectID);
					project.put("name", project_Query.getName());
					project.put("accuracy", project_Query.getAccuracy());
					// 逐个添加分包工程
					JSONArray subproject = new JSONArray();
					for (Integer subProjectID : subProjectIDs) {
						Subproject sp = subprojectMapper
								.selectByPrimaryKey(subProjectID);
						JSONObject sub = new JSONObject();
						sub.put("tbqSubProjectID", sp.getTbqsubprojectid());
						sub.put("name", sp.getName());
						// 逐个添加分包工程对应的章节
						JSONArray element = new JSONArray();
						// 获取章
						List<Quoted_Element4tbq> element4tbqs = quoted_Element4tbqMapper
								.loadTBQElement(sp.getSubprojectid(),
										ConstantData.CHAPTER);
						for (Quoted_Element4tbq element4tbq : element4tbqs) {
							JSONObject ele = new JSONObject();
							ele.put("elementID", element4tbq.getTbqelementid());
							ele.put("pElementID",
									element4tbq.getTbqpelementid());
							ele.put("name", element4tbq.getName());
							ele.put("type", ConstantData.CHAPTER);
							JSONArray billitem = new JSONArray();
							ele.put("billitem", billitem);
							element.add(ele);
						}
						// 获取节
						element4tbqs = quoted_Element4tbqMapper.loadTBQElement(
								sp.getSubprojectid(), ConstantData.SECTION);
						for (Quoted_Element4tbq element4tbq : element4tbqs) {
							JSONObject ele = new JSONObject();
							ele.put("elementID", element4tbq.getTbqelementid());
							ele.put("pElementID",
									element4tbq.getTbqpelementid());
							ele.put("name", element4tbq.getName());
							ele.put("type", ConstantData.SECTION);
							// 添加清单
							JSONArray billitem = new JSONArray();
							List<Quoted_Billitem4tbq> items = quoted_Billitem4tbqMapper
									.loadBillItem(element4tbq.getElementid());
							for (Quoted_Billitem4tbq item : items) {
								JSONObject bi = new JSONObject();
								bi.put("billItemID", item.getTbqbillitemid());
								bi.put("elementID",
										element4tbq.getTbqelementid());
								bi.put("pBillItemID", item.getTbqpbillitemid());
								bi.put("description", item.getDescription());
								bi.put("trade", item.getTrade());
								bi.put("unit", item.getUnit());
								bi.put("type", item.getType());
								// TODO 价格的获取还需要沟通确定
								Quoted_Adopt4tbq quoted_Adopt4tbq = quoted_Adopt4tbqMapper
										.selectByPrimaryKey(item
												.getBillitemid());
								if (quoted_Adopt4tbq != null) {
									bi.put("netRate",
											quoted_Adopt4tbq.getAdoptnetrate());
									bi.put("netAmount", quoted_Adopt4tbq
											.getAdoptnetamount());
								} else {
									bi.put("netRate", "");
									bi.put("netAmount", "");
								}
								bi.put("qty", item.getQty());
								bi.put("priceType", item.getPricetype());
								bi.put("remark", item.getRemark());
								billitem.add(bi);
							}
							ele.put("billitem", billitem);
							element.add(ele);
						}
						sub.put("element", element);
						subproject.add(sub);
					}
					project.put("subproject", subproject);
				} else {
					project.put("ret", "-2");
				}
			}
		} else {
			project.put("ret", "-1");
		}
		return project;
	}

	@Override
	public void loadEtenderProject(String userid, int tbqProjectID,
			JSONObject jsonData) {
		Project_Query project_Query = project_QueryMapper.loadLastTBQProject(
				userid, tbqProjectID);
		if (project_Query != null) {
			int queryProjectID = project_Query.getQueryprojectid();
			List<Integer> subProjectIDs = subprojectMapper
					.loadEvaluationSubProjectIDs(queryProjectID);
			if (subProjectIDs.size() == 0) {
				// 未到期
				jsonData.put("ret", "0");
			} else {
				// 到期，先判断是否有采纳价格
				boolean adopted = false;
				for (Integer subProjectID : subProjectIDs) {
					List<Integer> eleIDs = quoted_Element4tbqMapper
							.loadTBQElementIDs(subProjectID,
									ConstantData.SECTION);
					if (eleIDs != null && eleIDs.size() > 0) {
						List<Integer> itemIDs = quoted_Billitem4tbqMapper
								.loadBillItemIDs(eleIDs);
						if (itemIDs != null && itemIDs.size() > 0) {
							int count = quoted_Adopt4tbqMapper
									.loadAdoptItemCount(itemIDs);
							if (count > 0) {
								adopted = true;
								break;
							}
						}
					}
				}
				if (adopted) {
					jsonData.put("ret", "1");
					jsonData.put("etenderProjectID",
							project_Query.getQueryprojectid());
					jsonData.put("name", project_Query.getName());
					jsonData.put("accuracy", project_Query.getAccuracy());
				} else {
					jsonData.put("ret", "-2");
				}
			}
		} else {
			jsonData.put("ret", "-1");
		}
	}

	@Override
	public void loadEtenderSubProject(String userid, int tbqSubProjectID,
			int etenderProjectID, JSONObject jsonData) {
		Subproject sp = subprojectMapper.loadEtenderSubProject(
				etenderProjectID, tbqSubProjectID);
		if (sp != null) {
			jsonData.put("etenderSubProjectID", sp.getSubprojectid());
			jsonData.put("name", sp.getName());
			// 逐个添加分包工程对应的章节
			JSONArray element = new JSONArray();
			// 获取章
			List<Quoted_Element4tbq> element4tbqs = quoted_Element4tbqMapper
					.loadTBQElement(sp.getSubprojectid(), ConstantData.CHAPTER);
			for (Quoted_Element4tbq element4tbq : element4tbqs) {
				JSONObject ele = new JSONObject();
				ele.put("elementID", element4tbq.getTbqelementid());
				ele.put("pElementID", element4tbq.getTbqpelementid());
				ele.put("name", element4tbq.getName());
				ele.put("type", ConstantData.CHAPTER);
				JSONArray billitem = new JSONArray();
				ele.put("billitem", billitem);
				element.add(ele);
			}
			// 获取节
			element4tbqs = quoted_Element4tbqMapper.loadTBQElement(
					sp.getSubprojectid(), ConstantData.SECTION);
			for (Quoted_Element4tbq element4tbq : element4tbqs) {
				JSONObject ele = new JSONObject();
				ele.put("elementID", element4tbq.getTbqelementid());
				ele.put("pElementID", element4tbq.getTbqpelementid());
				ele.put("name", element4tbq.getName());
				ele.put("type", ConstantData.SECTION);
				// 添加清单
				JSONArray billitem = new JSONArray();
				List<Quoted_Billitem4tbq> items = quoted_Billitem4tbqMapper
						.loadBillItem(element4tbq.getElementid());
				for (Quoted_Billitem4tbq item : items) {
					JSONObject bi = new JSONObject();
					bi.put("billItemID", item.getTbqbillitemid());
					bi.put("elementID", element4tbq.getTbqelementid());
					bi.put("pBillItemID", item.getTbqpbillitemid());
					bi.put("description", item.getDescription());
					bi.put("trade", item.getTrade());
					bi.put("unit", item.getUnit());
					bi.put("type", item.getType());
					// TODO 价格的获取还需要沟通确定
					Quoted_Adopt4tbq quoted_Adopt4tbq = quoted_Adopt4tbqMapper
							.selectByPrimaryKey(item.getBillitemid());
					if (quoted_Adopt4tbq != null) {
						bi.put("netRate", quoted_Adopt4tbq.getAdoptnetrate());
						bi.put("netAmount",
								quoted_Adopt4tbq.getAdoptnetamount());			
						if ( quoted_Adopt4tbq.getUserid().equals("-1") )						
								bi.put("source", "UD");
						else if ( quoted_Adopt4tbq.getUserid().equals("0")){
								bi.put("source", "PTE");
						}else{							
							logger.info("supplier userid={}", quoted_Adopt4tbq.getUserid());
							User u= userMapper.selectByPrimaryKey(quoted_Adopt4tbq.getUserid());							
							if ( u != null){
								List<Supplier> l = supplierMapper.selectSubcontractByEmail(userid, u.getEmail());
								if ( l!=null && l.get(0)!=null  ){
									logger.debug("userid={} company={} supplier={}", u.getUserid(), u.getCompany(), l.get(0).getName());							
									bi.put("source", l.get(0).getName());
									// 0代表未修改，1代表有修改
									if (item.getIschange())
										bi.put("source", l.get(0).getName()+"#");									
								}
							}
						}						
						bi.put("remark", quoted_Adopt4tbq.getAdoptremark());						
					} else {
						bi.put("netRate", "");
						bi.put("netAmount", "");						
					}
					bi.put("qty", item.getQty());
					bi.put("priceType", item.getPricetype());					
					billitem.add(bi);
				}
				ele.put("billitem", billitem);
				element.add(ele);
			}
			jsonData.put("element", element);
		}
	}

}
