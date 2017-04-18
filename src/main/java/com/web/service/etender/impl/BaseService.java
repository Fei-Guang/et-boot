package com.web.service.etender.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.RegisterUtil;
import com.utils.FileUtil;
import com.utils.email.MailSenderInfo;
import com.utils.email.SimpleMailSender;
import com.web.common.ConstantData;
import com.web.dao.etender.Project_QueryMapper;
import com.web.dao.etender.Quoted_Adopt4tbqMapper;
import com.web.dao.etender.Quoted_Billitem4tbqMapper;
import com.web.dao.etender.Quoted_Detail4tbqMapper;
import com.web.dao.etender.Quoted_Discount4tbqMapper;
import com.web.dao.etender.Quoted_Element4tbqMapper;
import com.web.dao.etender.SubprojectMapper;
import com.web.dao.etender.SupplierMapper;
import com.web.dao.etender.Supplier_SubprojectMapper;
import com.web.dao.etender.TradeMapper;
import com.web.dao.etender.UserMapper;
import com.web.model.etender.Trade;
import com.web.model.etender.User;
import com.web.utils.AppContext;
import com.web.utils.WebUtil;

@Service("baseService")
public class BaseService {

	protected static Logger logger = LoggerFactory.getLogger(BaseService.class);

	@Autowired
	protected AppContext appContext;
	@Autowired
	protected ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	protected UserMapper userMapper;
	@Autowired
	protected Project_QueryMapper project_QueryMapper;
	@Autowired
	protected SupplierMapper supplierMapper;
	@Autowired
	protected Supplier_SubprojectMapper supplier_SubprojectMapper;
	@Autowired
	protected TradeMapper tradeMapper;
	@Autowired
	protected SubprojectMapper subprojectMapper;
	@Autowired
	protected Quoted_Element4tbqMapper quoted_Element4tbqMapper;
	@Autowired
	protected Quoted_Billitem4tbqMapper quoted_Billitem4tbqMapper;
	@Autowired
	protected Quoted_Detail4tbqMapper quoted_Detail4tbqMapper;
	@Autowired
	protected Quoted_Discount4tbqMapper quoted_Discount4tbqMapper;
	@Autowired
	protected Quoted_Adopt4tbqMapper quoted_Adopt4tbqMapper;

	public boolean saveUser(User user) {
		boolean result = false;
		if (userMapper.selectByPrimaryKey(user.getUserid()) != null) {
			result = userMapper.updateByPrimaryKeySelective(user) == 1;
		} else {
			result = userMapper.insertSelective(user) == 1;
			if (result) {
				// 添加用户的时候，初始化用户可选择的专业列表
				initTrade4User(user.getUserid());
			}
		}
		return result;
	}

	public void initTrade4User(final String userid) {
		logger.info("Asynchronous call initTrade4User("
				+ taskExecutor.getActiveCount() + ")...");
		try {
			taskExecutor.execute(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					logger.info("Start initTrade4User...");
					if (getTradeCount4User(userid) == 0) {
						Hashtable<String, String> predefinedTrades = (Hashtable<String, String>) appContext
								.getServletContext().getAttribute(
										"predefinedTrades");
						Set<String> keys = predefinedTrades.keySet();
						for (String key : keys) {
							Trade trade = new Trade();
							trade.setCode(key);
							trade.setDescription(predefinedTrades.get(key));
							trade.setUserid(userid);
							addTrade(trade);
						}
					}
				}
			});
		} catch (TaskRejectedException te) {
			logger.error("Asynchronous call initTrade4User rejected:"
					+ te.getMessage());
		}
	}

	public int getTradeCount4User(String userid) {
		return tradeMapper.getTradeCount4User(userid);
	}

	public int addTrade(Trade trade) {
		String code = trade.getCode();
		String userid = trade.getUserid();
		List<String> codes = tradeMapper.selectCodeByUserID(userid, -1);
		for (String c : codes) {
			if (code.equalsIgnoreCase(c)) {
				return ConstantData.NameEXISTED;
			}
		}
		int tradeid = ConstantData.ERROR;
		if (tradeMapper.insertSelective(trade) == 1) {
			tradeid = trade.getTradeid();
		}
		return tradeid;
	}

	public int validEmail(String email) {
		if (email.contains(" ")) {
			return ConstantData.INVALID_EMAIL;
		}
		String result = RegisterUtil.check(email);
		if (result.equals(RegisterUtil.RESULT_200)) {
			return ConstantData.OK;
		} else if (result.equals(RegisterUtil.RESULT_409)) {
			return ConstantData.EXISTED_EMAIL;
		} else if (result.equals(RegisterUtil.RESULT_400)) {
			return ConstantData.INVALID_EMAIL;
		} else if (result.equals(RegisterUtil.RESULT_423)) {
			return ConstantData.NAME_NOT_VERIFIED;
		} else {
			return ConstantData.OTHER_EMAIL;
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param email
	 *            收件人
	 * @param subject
	 *            主体
	 * @param content
	 *            邮件内容
	 * @param attachFiles
	 *            附件
	 * @return
	 */
	public boolean sendEmail(String email, String subject, String content,
			File[] attachFiles) {
		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(appContext.getMailServerHost());
		mailInfo.setMailServerPort(String.valueOf(appContext
				.getMailServerPort()));
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName(appContext.getMailUserName());
		// 邮箱密码
		mailInfo.setPassword(appContext.getMailPassword());
		// 发件人邮箱
		mailInfo.setFromAddress(appContext.getMailAddress());
		// 收件人邮箱
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add(email);
		mailInfo.setToAddresses(toAddresses);
		// 邮件标题
		mailInfo.setSubject(subject);
		// 邮件内容
		mailInfo.setContent(content);
		// 附件
		mailInfo.setAttachFiles(attachFiles);
		// 发送邮件
		// 发送html格式
		return SimpleMailSender.sendHtmlMail(mailInfo);
	}

	/**
	 * 邮箱是否已经注册
	 * 
	 * @param email
	 * @return
	 */
	public boolean containEmail(String email) {
		@SuppressWarnings("unchecked")
		Set<String> emails = (Set<String>) appContext.getServletContext()
				.getAttribute("emails");
		return emails.contains(email);
	}

	/**
	 * 向注册邮箱集合中添加邮箱
	 * 
	 * @param email
	 * @return
	 */
	public boolean registerEmail(String email) {
		@SuppressWarnings("unchecked")
		Set<String> emails = (Set<String>) appContext.getServletContext()
				.getAttribute("emails");
		return emails.add(email);
	}

	/**
	 * 删除供应商记录
	 * 
	 * @param userid
	 * @param ids
	 * @return
	 */
	public int deleteSupplierRecord(String userid, List<String> ids) {
		int ret = ConstantData.OK;
		// 删除供应商还需要联动修改发包界面记录的供应商选项
		String recordDirPath = WebUtil.getTempPath() + "/inquire/" + userid;
		File[] fs = new File(recordDirPath).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return FileUtil.getSuffix(pathname).equalsIgnoreCase("json");
			}
		});
		int successedCount = 0;
		int failedCount = 0;
		for (File f : fs) {
			// 每个文件依次扫描调整
			String json = FileUtil.readContent(f, true);
			JSONObject jo = JSON.parseObject(json);
			JSONArray records = jo.getJSONArray("records");
			if (records != null) {
				int size = records.size();
				for (int i = 0; i < size; i++) {
					JSONObject record = records.getJSONObject(i);
					// 取分包商节点
					JSONArray subcontractors = record
							.getJSONArray("subcontractors");
					if (subcontractors != null) {
						int subcontractorSize = subcontractors.size();
						for (int j = subcontractorSize - 1; j >= 0; j--) {
							JSONObject subcontractor = subcontractors
									.getJSONObject(j);
							if (ids.contains(subcontractor.getString("id"))) {
								// 找到分包商
								subcontractors.remove(j);
							}
						}
					}
				}
			}
			// 这里是对每个文件依次调整，存在调整失败的可能，但是文件删除失败，不影响后面询价页面的数据展示，后面询价页面数据展示需要根据实际供应商列表校正一遍
			if (FileUtil.newFile(f.getAbsolutePath(), jo.toJSONString())) {
				successedCount++;
			} else {
				failedCount++;
			}
		}
		if (failedCount > 0) {
			if (successedCount > 0) {
				ret = ConstantData.ERROR;
			} else {
				ret = ConstantData.FAILED;
			}
		}
		return ret;
	}
}
