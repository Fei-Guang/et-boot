package com.web.service.etender;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
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

public interface IProjectService {

	/**
	 * 创建询价工程，错误返回错误码（<0），正常返回工程ID
	 * 
	 * @param project
	 * @return
	 */
	int createProject(Project_Query project);

	/**
	 * 验证工程名称是否有效，返回0代表有效，返回1代表重复
	 * 
	 * @param name
	 * @return
	 */
	int validateProjectName(String name);

	/**
	 * 载入用户对应的询价工程列表
	 * 
	 * @param userid
	 * @return
	 */
	List<Project_Query> loadQueryProject(String userid);

	/**
	 * 载入用户对应的报价工程列表
	 * 
	 * @param user
	 * @return
	 */
	List<Project_Quote> loadQuoteProject(User user);

	/**
	 * 逻辑删除询价工程
	 * 
	 * @param projectID
	 * @return
	 */
	boolean deleteInquireProjectByLogic(Integer projectID);

	/**
	 * 逻辑删除报价工程
	 * 
	 * @param projectID
	 * @param email
	 * @return
	 */
	boolean deleteQuoteProjectByLogic(Integer projectID, String email);

	/**
	 * 获取工程信息
	 * 
	 * @param projectID
	 * @return
	 */
	Project_Query loadProject(Integer projectID);

	/**
	 * 编辑工程信息，错误返回错误码（<0），正常返回工程ID
	 * 
	 * @param project
	 * @return
	 */
	int editProject(Project_Query project);

	/**
	 * 验证名称是否有效
	 * 
	 * @param name
	 * @param userid
	 * @return
	 */
	int validName(String name, String userid);

	/**
	 * 载入分包工程
	 * 
	 * @param projectID
	 * @return
	 */
	List<Subproject> loadSubProject(Integer projectID);

	/**
	 * 获取对应工程的选择记录
	 * 
	 * @param userid
	 * @param projectID
	 * @return
	 */
	public JSONObject getRecord(String userid, int projectID);

	/**
	 * 记录最近选择的分包工程
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param subProjectID
	 * @return
	 */
	boolean saveSubContractRecord(String userid, int projectID,
			String subContract, String subProjectID);

	/**
	 * 记录分包工程对应的时区信息
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param timeZone
	 * @return
	 */
	boolean saveTimeZoneRecord(String userid, int projectID,
			String subContract, String timeZone);

	/**
	 * 记录分包工程对应的结束时间
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param endTime
	 * @return
	 */
	boolean saveEndTimeRecord(String userid, int projectID, String subContract,
			String endTime);

	/**
	 * 记录分包工程对应的分包商
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param supplierID
	 * @param name
	 * @param email
	 * @return
	 */
	boolean saveSupplierRecord(String userid, int projectID,
			String subContract, String supplierID, String name, String email);

	/**
	 * 删除分包工程对应的分包商节点
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param supplierID
	 * @return
	 */
	boolean deleteSupplierRecord(String userid, int projectID,
			String subContract, String supplierID);

	/**
	 * 记录分包商与对应上传附件之间的关系
	 * 
	 * @param userid
	 * @param projectID
	 * @param fileName
	 * @param attachPath
	 * @param subContract
	 * @param supplierID
	 * @return
	 */
	int saveSupplierAttachRecord(String userid, int projectID,
			String fileName, String attachPath, String subContract,
			String supplierID);

	/**
	 * 删除分包商与对应上传附件之间的关系
	 * 
	 * @param userid
	 * @param projectID
	 * @param attachPath
	 * @param subContract
	 * @param supplierID
	 * @return
	 */
	boolean deleteSupplierAttachRecord(String userid, int projectID,
			String attachPath, String subContract, String supplierID);

	/**
	 * 导入TBQ上传数据
	 * 
	 * @param tbqData
	 * @return
	 * @throws Exception
	 */
	int importTBQData(JSONObject tbqData) throws Exception;

	/**
	 * 获取分包工程与分包商之间的关联
	 * 
	 * @param userid
	 * @param projectID
	 * @param suppliers
	 *            用户对应的分包商列表
	 * @return
	 */
	List<SubProject_Subcontractor> getSubProjectSubcontractorMapping(
			String userid, int projectID, List<Supplier> suppliers);

	/**
	 * 获取分包工程对应分包商的附件列表
	 * 
	 * @param userid
	 * @param projectID
	 * @param subContract
	 * @param supplierID
	 * @return
	 */
	List<AttachMent> loadAttach(String userid, int projectID,
			String subContract, String supplierID);

	/**
	 * 获取用户待报价的分包工程的类目列表
	 * 
	 * @param email
	 * @param projectID
	 * @return
	 */
	List<QuoteSubProjectElement> loadQuoteSubProjectElement(String email,
			int projectID);

	/**
	 * 获取用户待评标的分包工程的类目列表
	 * 
	 * @param email
	 * @param projectID
	 * @return
	 */
	List<EvaluationSubProjectElement> loadEvaluationSubProjectElement(
			int projectID);

	/**
	 * 获取用户待报价的清单列表
	 * 
	 * @param userid
	 * @param email
	 * @param projectID
	 *            报价总工程标识
	 * @param eleID
	 *            标识
	 * @param eleType
	 *            0代表分包工程，1代表章，2代表节
	 * @param accuracy
	 *            精度
	 * @return
	 */
	List<QuoteBillItem> loadQuoteBillItem(String userid, String email,
			int projectID, String eleID, String eleType, byte accuracy, String descript);

	/**
	 * 获取用户可评标的清单列表
	 * 
	 * @param userid
	 * @param projectID
	 *            询价总工程标识
	 * @param eleID
	 *            标识
	 * @param eleType
	 *            0代表分包工程，1代表章，2代表节
	 * @param quoteInfos
	 *            用户报价信息集合
	 * @param accuracy
	 *            精度
	 * @param descript
	 *            过滤条件---描述
	 * @param trade
	 *            过滤条件---专业
	 * @param unit
	 *            过滤条件---单位
	 * @param changed
	 *            过滤条件---修改项
	 * @return
	 */
	List<EvaluationBillItem> loadEvaluationBillItem(String userid,
			int projectID, String eleID, String eleType,
			List<UserQuoteInfo> userQuoteInfos, byte accuracy, String descript,
			String trade, String unit, String changed);

	/**
	 * 由指定用户向分包工程所设置的分包商发送工程信息
	 * 
	 * @param user
	 * @param projectID
	 * @param returnTo
	 * @return
	 * @throws Exception
	 */
	boolean sendSubProject(User user, int projectID, String returnTo)
			throws Exception;

	/**
	 * 获取询价工程对应的发送记录
	 * 
	 * @param projectID
	 * @return
	 */
	List<InquireRecord> loadInquireRecord(int projectID);

	/**
	 * 获取报价工程对应的发送记录
	 * 
	 * @param projectID
	 * @param email
	 * @return
	 */
	List<QuoteRecord> loadQuoteRecord(Integer projectID, String email);

	/**
	 * 更新供应商第一次打开询价工程的时间
	 * 
	 * @param email
	 * @param projectID
	 * @return
	 */
	boolean updateSupplierReceivedTime(String email, Integer projectID);

	/**
	 * 更新用户对某一条清单的报价
	 * 
	 * @param user
	 * @param subprojectid
	 * @param billitemid
	 * @param datatype
	 * @param data
	 * @return
	 */
	int updateBillitem4tbq2quote(User user, String subprojectid,
			String billitemid, String datatype, String data);

	/**
	 * 更新清单的修改状态
	 * 
	 * @param billitemid
	 * @param isChange
	 */
	int updateChangeStatus(String billitemid, String isChange);

	/**
	 * 总包商调整供应商对某一条清单的报价
	 * 
	 * @param supplierid
	 * @param billitemid
	 * @param datatype
	 * @param data
	 * @return
	 */
	String updateBillitem4tbq2evaluation(String supplierid, String billitemid,
			String datatype, String data);

	/**
	 * 总包商调整供应商对某一分包工程的折扣
	 * 
	 * @param supplierid
	 * @param subProjectID
	 * @param datatype
	 * @param data
	 * @return
	 */
	int updateDiscount4tbq2evaluation(String supplierid, String subProjectID,
			String datatype, String data);

	/**
	 * 对指定清单采纳指定供应商价格
	 * 
	 * @param supplierid
	 * @param adoptNetRate
	 * @param adoptNetAmount
	 * @param adoptRemark
	 * @param billitemid
	 * @return
	 */
	String adoptBillitem4tbq2evaluation(String supplierid, String adoptNetRate,
			String adoptNetAmount, String adoptRemark, String billitemid);

	/**
	 * 更新询价商查看报价时间
	 * 
	 * @param projectID
	 */
	boolean updateMainConReviewTime(Integer projectID);

	/**
	 * 更新分包商提交对应分包工程报价时间
	 * 
	 * @param email
	 * @param subProjectID
	 * @return
	 */
	int updateSupplierSubmitTime(String email, int subProjectID);

	/**
	 * 判断用户是否已经提交分包工程
	 * 
	 * @param user
	 * @param subProjectID
	 * @return
	 */
	boolean isSubProjectSubmit(User user, String subProjectID);

	/**
	 * 更新询价工程状态为评标中
	 * 
	 * @param queryProjectID
	 * @return
	 */
	int updateInquireProjectStatus2Evaluating(int queryProjectID);

	/**
	 * 获取询价工程状态
	 * 
	 * @param queryProjectID
	 * @return
	 */
	byte getQueryProjectStatus(int queryProjectID);

	/**
	 * 获取发送给供应商的附件列表
	 * 
	 * @param email
	 * @param subProjectID
	 * @return
	 */
	List<AttachMent> loadAttachMents(String email, String subProjectID);

	/**
	 * 删除分包工程
	 * 
	 * @param subprojectid
	 * @return
	 */
	int deleteSubProject(String subprojectid);

	/**
	 * 是否满足进入评标界面的条件
	 * 
	 * @param queryProjectID
	 * @return
	 */
	boolean canEvaluation(int queryProjectID);

	/**
	 * 获取与TBQ工程对应的最新工程数据信息
	 * 
	 * @param userid
	 * @param tbqProjectID
	 * @return
	 */
	JSONObject loadLastTBQProject(String userid, int tbqProjectID);

	/**
	 * 获取下发总工程所需要的信息
	 * 
	 * @param userid
	 * @param tbqProjectID
	 * @param jsonData
	 */
	void loadEtenderProject(String userid, int tbqProjectID, JSONObject jsonData);

	/**
	 * 获取下发总工程对应分包工程所需要的信息
	 * 
	 * @param userid
	 * @param tbqSubProjectID
	 * @param etenderProjectID
	 * @param jsonData
	 */
	void loadEtenderSubProject(String userid, int tbqSubProjectID,
			int etenderProjectID, JSONObject jsonData);

	/**
	 * 获取评标条目的过滤条件
	 * 
	 * @param userid
	 * @param string
	 * @return
	 */
	Set<String> loadFilter(String userid, String string);
	
	
	/**
	 * 获取评标条目的过滤条件
	 * 
	 * @param userid
	 * @param string
	 * @return
	 */
	Set<String> loadFilter(String userid, String string, String source);

	/**
	 * 导出用户对应工程的报价数据
	 * 
	 * @param treeData
	 * @param tableData
	 * @param summary
	 * @param totalAmount
	 * @param sheetName
	 * @return 数据文件名称
	 */
	String export4tbq2quote(String treeData, String tableData, String summary,
			String totalAmount, String sheetName);

	/**
	 * 导出用户对应工程的评标数据
	 * 
	 * @param treeData
	 * @param supplierData
	 * @param tableData
	 * @param statisticData
	 * @param adopted
	 * @param pte
	 * @param sheetName
	 * @return 数据文件名称
	 */
	String export4tbq2evaluation(String treeData, String supplierData,
			String tableData, String statisticData, String adopted, String pte,
			String sheetName);

}
