package com.web.controller.etender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.LoginUtil;
import com.utils.DateUtil;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.etools.Message;
import com.web.model.etender.User;
import com.web.service.etender.IProjectService;
import com.web.service.etender.IUserService;
import com.web.utils.WebUtil;

@Controller
@RequestMapping("/tbq")
public class TBQController extends CommonController {

	@Autowired
	protected IProjectService projectService;
	@Autowired
	protected IUserService userService;

	private JSONObject getResponseJSON(int status, String msg, String uuid,
			String access_token) {
		JSONObject jsonData = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("datetime", DateUtil.getTimeStampGMT(false));
		header.put("version", Message.VERSION);
		JSONObject response = new JSONObject();
		response.put("status", status);
		response.put("msg", msg);
		response.put("uuid", uuid);
		response.put("access_token", access_token);
		jsonData.put("header", header);
		jsonData.put("response", response);
		return jsonData;
	}

	/**
	 * 利用广联云验证token的有效性
	 * 
	 * @param access_token
	 * @return
	 */
	private boolean isValidToken2(String access_token) {
		if (!StringUtil.isNull(access_token)) {
			// access_token获取login_token
			String login_token = LoginUtil.getLoginToken(access_token);
			if (!StringUtil.isNull(login_token)) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidToken(String access_token) {
		logger.info("bypass token validate");
		return true;
	}

	/**
	 * auth from yun
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login_yun", method = RequestMethod.POST)
	@ResponseBody
	public String login_yun(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "login",
				" trace into"));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject grant = reqMsg.getJsonData().getJSONObject("body")
					.getJSONObject("grant");
			String email = grant.getString("email");
			String password = grant.getString("password");
			String access_token = "";
			String access_token_json = LoginUtil.getAccessTokenInfo(email,
					password);
			if (!StringUtil.isNull(access_token_json)) {
				JSONObject atj = JSON.parseObject(access_token_json);
				access_token = atj.getString("access_token");
			}
			int count = 1;
			logger.info("#{}: requesting access_token {}", count, access_token);
			while (StringUtil.isNull(access_token)) {
				if (count >= ConstantData.RETRYNUM) {
					break;
				}
				Thread.sleep(1000);
				access_token_json = LoginUtil.getAccessTokenInfo(email,
						password);
				if (!StringUtil.isNull(access_token_json)) {
					JSONObject atj = JSON.parseObject(access_token_json);
					access_token = atj.getString("access_token");
				}
				count++;
				logger.info("#{}: requesting access_token {}", count,
						access_token);

			}
			if (!StringUtil.isNull(access_token)) {
				String userid = userService.verifyTBQUser(email, password,
						access_token);
				if (!StringUtil.isNull(userid)) {
					logger.info("{} userid={}", email, userid);
					JSONObject resData = getResponseJSON(200, "succeed.", "",
							access_token);
					resData.put("userid", userid);
					resMsg = new Message(resData);
				}
			} else {
				resMsg = new Message(getResponseJSON(501, "error.", "login",
						" failed to ge token after 3 times"));
				logger.error("{}: requesting access_token {} after 3 times",
						count, access_token);
			}

		} catch (Exception e) {
			logger.error("Login:", e);
			resMsg = new Message(getResponseJSON(502, "error.", "login",
					e.getMessage()));
		}
		return resMsg.toString();
	}
	
	
	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "login",
				" trace into"));
		String data = request.getParameter("data");
		
		Message reqMsg = new Message(data);
		JSONObject grant = reqMsg.getJsonData().getJSONObject("body")
				.getJSONObject("grant");
		String email = grant.getString("email");
		String password = grant.getString("password");
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		String access_token = "";
		int ret = ConstantData.LOGIN_INVALID;
		try {
			ret = userService.grant(user);
			logger.info("email={} userid={}", email, user.getUserid());
			JSONObject resData = getResponseJSON(200, "succeed.", "",
					access_token);
			resData.put("userid", user.getUserid());
			resMsg = new Message(resData);
		} catch (Exception e) {
			logger.error("Grant:", e.getMessage());
			resMsg = new Message(getResponseJSON(502, "error.", "login",
					e.getMessage()));
		}
		
			
			
		return resMsg.toString();
	}
	
	/**
	 * tbq发送分包工程文件接口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadProject", method = RequestMethod.POST)
	@ResponseBody
	public String uploadProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			logger.info("UploadProject:" + uuid);
			if (isValidToken(access_token)) {
				String userid = body.getString("userid");
				JSONObject project = body.getJSONObject("project");
				JSONObject jo = new JSONObject();
				jo.put("userid", userid);
				jo.put("uuid", uuid);
				jo.put("project", project);
				
				// 记录总工程信息
				String recordPath = WebUtil.getTempPath() + "tbq/" + uuid
						+ ".json";
				logger.info("tbq msg write to {}: {}", recordPath,jo.toJSONString());
				if (FileUtil.newFile(recordPath, jo.toJSONString())) {
					resMsg = new Message(getResponseJSON(200, "succeed.", uuid,
							access_token));
				}
			} else {

				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			logger.error("UploadProject:", e);
		}
		return resMsg.toString();
	}

	@RequestMapping(value = "/uploadSubProject", method = RequestMethod.POST)
	@ResponseBody
	public String uploadSubProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("trace...");
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			logger.info("UploadSubProject:" + uuid);
			if (isValidToken(access_token)) {
				String recordPath = WebUtil.getTempPath() + "tbq/" + uuid
						+ ".json";
				String json = FileUtil.readContent(recordPath, true);
				if (!StringUtil.isNull(json)) {
					String userid = body.getString("userid");
					JSONObject record = JSON.parseObject(json);
					if (record.getString("uuid").equals(uuid)
							&& record.getString("userid").equals(userid)) {
						// 代表是同一批数据任务
						JSONObject subproject = body
								.getJSONObject("subproject");
						// 记录分包工程信息
						JSONArray subprojects = record
								.getJSONArray("subprojects");
						if (subprojects != null) {
							subprojects.add(subproject);
						} else {
							subprojects = new JSONArray();
							subprojects.add(subproject);
							record.put("subprojects", subprojects);
						}
						if (FileUtil.newFile(recordPath, record.toJSONString())) {
							resMsg = new Message(getResponseJSON(200,
									"succeed.", uuid, access_token));
						}
					} else {
						resMsg = new Message(getResponseJSON(505,
								"not the same batch of tasks.", uuid,
								access_token));
					}
				}
			} else {
				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UploadSubProject:", e);
		}
		return resMsg.toString();
	}

	@RequestMapping(value = "/uploadFinish", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFinish(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			logger.info("UploadFinish:" + uuid);
			if (isValidToken(access_token)) {
				String recordPath = WebUtil.getTempPath() + "tbq/" + uuid
						+ ".json";
				String status = body.getString("status");
				if (!StringUtil.isNull(status)) {
					// 记录分包工程信息
					if (status.equalsIgnoreCase("cancel")) {
						// 删除记录
						FileUtil.delFileOrFolder(recordPath);
						resMsg = new Message(getResponseJSON(200, "succeed.",
								uuid, access_token));
					} else if (status.equalsIgnoreCase("finish")) {
						// 将json里面的数据信息放在一个事务中提交到数据库中，然后删除记录
						String json = FileUtil.readContent(recordPath, true);
						int queryprojectid = projectService.importTBQData(JSON
								.parseObject(json));
						if (queryprojectid > 0) {
							JSONObject jsonObject = getResponseJSON(200,
									"succeed.", uuid, access_token);
							jsonObject.getJSONObject("response").put(
									"queryprojectid",
									WebUtil.crypt(queryprojectid + ""));
							logger.debug("queryprojectid {}={}",
									queryprojectid,
									WebUtil.crypt(queryprojectid + ""));
							resMsg = new Message(jsonObject);
							// logger.info("TBQ data recordPath:" + recordPath);
							// FileUtil.delFileOrFolder(recordPath);
							logger.debug("resMsg:{} #### {}",
									resMsg.getJsonData(), resMsg.toString());
							return resMsg.toString();
						} else
							logger.error("importTBQData error for {}", uuid);

					}
				}
			} else {
				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			logger.error("UploadFinish:", e);
		}
		return resMsg.toString();
	}

	/**
	 * tbq下载中标文件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadProject", method = RequestMethod.POST)
	@ResponseBody
	public String downloadProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			
			if (isValidToken(access_token)) {
				String userid = body.getString("userid");
				String tbqProjectID = body.getString("tbqProjectID");
				if (!StringUtil.isNull(userid)
						&& !StringUtil.isNull(tbqProjectID)) {
					JSONObject jsonData = getResponseJSON(200, "succeed.",
							uuid, access_token);
					// 获取用户对应工程的最新信息
					projectService.loadEtenderProject(userid,
							Integer.parseInt(tbqProjectID),
							jsonData.getJSONObject("response"));
					resMsg = new Message(jsonData);
				}
			} else {
				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			logger.error("DownloadProject:", e);
		}
		return resMsg.toString();
	}

	@RequestMapping(value = "/downloadSubProject", method = RequestMethod.POST)
	@ResponseBody
	public String downloadSubProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			logger.info("DownloadSubProject:" + uuid);
			if (isValidToken(access_token)) {
				String userid = body.getString("userid");
				String tbqSubProjectID = body.getString("tbqSubProjectID");
				String etenderProjectID = body.getString("etenderProjectID");
				if (!StringUtil.isNull(userid)
						&& !StringUtil.isNull(tbqSubProjectID)
						&& !StringUtil.isNull(etenderProjectID)) {
					JSONObject jsonData = getResponseJSON(200, "succeed.",
							uuid, access_token);
					// 获取用户对应分包工程的最新信息
					logger.info("Tbq downloading adopted quote...");
					projectService.loadEtenderSubProject(userid,
							Integer.parseInt(tbqSubProjectID),
							Integer.parseInt(etenderProjectID),
							jsonData.getJSONObject("response"));
					resMsg = new Message(jsonData);
				}
			} else {
				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			logger.error("DownloadSubProject:", e);
		}
		logger.info("Send adopted tender to tbq:{}", resMsg.toString());
		return resMsg.toString();
	}

	@RequestMapping(value = "/batchDownloadProject", method = RequestMethod.POST)
	@ResponseBody
	public String batchDownloadProject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message resMsg = new Message(getResponseJSON(500, "error.", "", ""));
		String data = request.getParameter("data");
		try {
			Message reqMsg = new Message(data);
			JSONObject body = reqMsg.getJsonData().getJSONObject("body");
			String access_token = body.getString("access_token");
			String uuid = body.getString("uuid");
			logger.info("BatchDownloadProject:" + uuid);
			if (isValidToken(access_token)) {
				String userid = body.getString("userid");
				String tbqProjectID = body.getString("tbqProjectID");
				if (!StringUtil.isNull(userid)
						&& !StringUtil.isNull(tbqProjectID)) {
					JSONObject jsonData = getResponseJSON(200, "succeed.",
							uuid, access_token);
					// 获取用户对应工程的最新信息
					JSONObject project = projectService.loadLastTBQProject(
							userid, Integer.parseInt(tbqProjectID));
					jsonData.getJSONObject("response").put("project", project);
					resMsg = new Message(jsonData);
				}
			} else {
				resMsg = new Message(getResponseJSON(505, "invalid token.",
						uuid, access_token));
			}
		} catch (Exception e) {
			logger.error("BatchDownloadProject:", e);
		}
		return resMsg.toString();
	}

}
