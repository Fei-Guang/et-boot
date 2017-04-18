package com.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.baidu.ueditor.ActionEnter;
import com.utils.ArrayUtil;
import com.utils.DateUtil;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.model.etender.User;
import com.web.utils.AppContext;
import com.web.utils.ServletContextUtil;
import com.web.utils.WebUtil;
import com.web.utils.arithmetic.DES;

@Controller
public class CommonController {

	protected static Logger logger = LoggerFactory
			.getLogger(CommonController.class);

	// 当前表格页码
	protected static String currentTablePageNum = "currentTablePageNum";
	// 验证码图片的缓存标识
	protected static final String CAPTCHA_SESSION_KEY = "captcha_session_key";

	@Autowired
	protected AppContext appContext;

	/**
	 * 网站启动首页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		Locale locale = new Locale("en", "US");
		request.getSession().setAttribute(
				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		return "etender/template";
	}

	/**
	 * 网站对外提供的加密服务链接
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/encrypt", method = RequestMethod.POST)
	public void encrypt(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String key = request.getParameter("key");
		JSONObject jo = new JSONObject();
		boolean ret = true;
		String msg = "";
		try {
			DES des = new DES();
			String code = des.strEnc(key, ConstantData.FirstKey,
					ConstantData.SecondKey, ConstantData.ThirdKey);
			jo.put("code", code);
		} catch (Exception e) {
			ret = false;
			msg = "process error.";
		}
		jo.put("ret", ret);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	 * 网站对外提供的服务器加密时间戳
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loadServerTimeStamp")
	public void loadServerTimeStamp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JSONObject jo = new JSONObject();
		boolean ret = true;
		String msg = "";
		try {
			DES des = new DES();
			String timeStamp = des.strEnc(DateUtil.getTimeStampGMT(false),
					ConstantData.FirstKey, ConstantData.SecondKey,
					ConstantData.ThirdKey);
			jo.put("timeStamp", timeStamp);
		} catch (Exception e) {
			ret = false;
			msg = "process error.";
		}
		jo.put("ret", ret);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 切换语言版本
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/lang/{langType}")
	@ResponseBody
	public String lang(@PathVariable String langType, HttpServletRequest request) {
		if (!StringUtil.isNull(langType) && langType.equals("zh")) {
			Locale locale = new Locale("zh", "CN");
			request.getSession()
					.setAttribute(
							SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
							locale);
		} else if (!StringUtil.isNull(langType) && langType.equals("en")) {
			Locale locale = new Locale("en", "US");
			request.getSession()
					.setAttribute(
							SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
							locale);
		} else {
			request.getSession().setAttribute(
					SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
					LocaleContextHolder.getLocale());
		}
		return null;
	}

	@RequestMapping(value = "/getFromSession", method = RequestMethod.POST)
	public void getFromSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String key = request.getParameter("key");
		String value = (String) request.getSession().getAttribute(key);
		if (value == null) {
			value = "";
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(value);
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/saveInSession", method = RequestMethod.POST)
	public void saveInSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String keyInfo = request.getParameter("key");
		String valueInfo = request.getParameter("value");
		String[] keys = keyInfo.split("&");
		String[] values = valueInfo.split("&");
		int length = Math.min(keys.length, values.length);
		for (int i = 0; i < length; i++) {
			String key = keys[i];
			String value = values[i];
			request.getSession().setAttribute(key, value);
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("ret", true);
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/removeFromSession", method = RequestMethod.POST)
	public void removeFromSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String keyInfo = request.getParameter("key");
		String[] keys = keyInfo.split("&");
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			request.getSession().removeAttribute(key);
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("ret", true);
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 获取当前用户
	 * 
	 * @param request
	 * @return
	 */
	protected User getCurrentUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(
				ConstantData.CONST_CAS_ASSERTION);
		return user;
	}

	/**
	 * 取消处理过程
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/cancelProcess", method = RequestMethod.POST)
	public void cancelProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String processID = request.getParameter("processID");
		request.getSession().setAttribute(
				ConstantData.CancelExport + "$" + processID, true);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("ret", true);
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 获取当前处理过程状态
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getCurrentProcess", method = RequestMethod.POST)
	public void getCurrentProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String processID = request.getParameter("processID");
		String process = (String) request.getSession().getAttribute(
				ConstantData.CurrentProgress + "$" + processID);
		if (StringUtil.isNull(process)) {
			process = "";
		}
		if (process.equalsIgnoreCase(ConstantData.Canceled)
				|| process.equalsIgnoreCase(ConstantData.Failed)
				|| process.equalsIgnoreCase(ConstantData.Successed)) {
			clearCache(request, ConstantData.CancelExport + "$" + processID,
					ConstantData.CurrentProgress + "$" + processID);
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("message", process);
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 初始化取消状态
	 * 
	 * @param request
	 * @param processID
	 */
	protected void initCancelStatus(HttpServletRequest request, String processID) {
		String key = ConstantData.CancelExport + "$" + processID;
		request.getSession().setAttribute(key, false);
	}

	/**
	 * 开始处理
	 * 
	 * @param request
	 * @param processID
	 */
	protected void startProgress(HttpServletRequest request, String processID) {
		String key = ConstantData.CurrentProgress + "$" + processID;
		request.getSession().setAttribute(key, ConstantData.Started);
	}

	/**
	 * 设置当前处理状态
	 * 
	 * @param request
	 * @param processID
	 */
	protected void setProgressStatus(HttpServletRequest request,
			String processID, String info) {
		String key = ConstantData.CurrentProgress + "$" + processID;
		request.getSession().setAttribute(key, info);
	}

	/**
	 * 是否取消任务
	 * 
	 * @param request
	 * @param processID
	 * @return
	 */
	protected boolean isCancel(HttpServletRequest request, String processID) {
		String key = ConstantData.CancelExport + "$" + processID;
		Object value = request.getSession().getAttribute(key);
		return value == null ? false : (Boolean) value;
	}

	/**
	 * 清除缓存指定项
	 * 
	 * @param request
	 * @param keys
	 */
	protected void clearCache(HttpServletRequest request, String... keys) {
		for (String key : keys) {
			request.getSession().removeAttribute(key);
		}
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String supportSuffix = request.getParameter("suffix");
		String supportSize = request.getParameter("size");
		
		String msg = "";
		User user = getCurrentUser(request);
		JSONObject jo = new JSONObject();
		boolean ret = true;
		if (user == null) {		
			msg = ConstantData.Error4Login;
			jo.put("msg", msg);
			ret = false;			
		}else {			
			Iterator<String> itr = request.getFileNames();
			MultipartFile mpf = null;			
			while (itr.hasNext()) {
				mpf = request.getFile(itr.next());
				if (!StringUtil.isNull(supportSize)) {
					if (Long.parseLong(supportSize) < mpf.getSize()) {
						logger.error("Attachment size={} cannot exceed 100M.", supportSize);
						jo.put("msg", "Attachment size cannot exceed 100M.");
						ret = false;
						break;
					}
				}
				String fileName = mpf.getOriginalFilename();
				String suffix = FileUtil.getSuffix(fileName);
				if (StringUtil.isNull(suffix)) {
					logger.error("The file format {} is unknown suffix={}", fileName, suffix);
					jo.put("msg", "The file format is not correct.");
					ret = false;
					break;
				} else {
					// 这里为了防止用户上传一些脚本运行文件进行攻击，先执行白名单检测
					if (WebUtil.inWhiteList(suffix)) {
						if (!StringUtil.isNull(supportSuffix)) {
							if (!ArrayUtil
									.contain(supportSuffix.split(","), suffix)) {
								logger.error("The file format[{}] is not supported yet.", suffix );
								jo.put("msg", "The file format is not correct.");
								ret = false;
								break;
							}
						}
					} else {
						logger.error("Invalid file format {} suffix={}", fileName, suffix);
						jo.put("msg", "Invalid file format.");
						ret = false;
						break;
					}
					// 构造上传附件唯一性标识
					String uuid = fileName;
					// 上传附件的存放路径
					String attachPath = WebUtil
							.getUploadAttachTmpPath(uuid, suffix, user.getUserid());
					request.getSession().setAttribute(fileName, attachPath);
					String desFilePath = ServletContextUtil.getWebRootPath()
							+ attachPath;
					logger.info("[uploadFile] desFilePath={}",desFilePath);
					File desFile = new File(desFilePath);
					File dir = desFile.getParentFile();
					if (dir != null) {
						if (!dir.exists()) {
							if (!dir.mkdirs()) {
								logger.error("mkdir {} failed", dir.getAbsoluteFile());
								jo.put("msg", "Upload file failed.");
								ret = false;
								break;
							}
						}
					}
					if ( desFile.exists() ){
						jo.put("msg", "File exists:"+ fileName);
						logger.info("File={} exists", desFilePath);
						ret = false;
						break;
					}else{
						try {
							
							logger.info("{} transfer file {} to server...", user.getEmail() , desFile);
							mpf.transferTo(desFile);
							logger.info("{} transfer file {} to server succeed", user.getEmail(), desFile);
							jo.put("fileName", fileName);
							jo.put("attachPath", attachPath.replace('\\', '/'));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("UploadFile:", e);
							jo.put("msg", "Upload file failed.");
							ret = false;
						}
					}
				}
				break;
			}
		}
		jo.put("ret", ret);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public void deleteFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String currentAttachName = request.getParameter("currentAttachName");
		logger.info("delete currentAttachName={}", currentAttachName);
		JSONObject jo = new JSONObject();
		boolean ret = true;
		if (!StringUtil.isNull(currentAttachName)) {
			// 如果传递了当前附件名称过来，那么意味着需要把以前的附件删除，以防止垃圾文件过多
			String attachPath = (String) request.getSession().getAttribute(
					currentAttachName);
			if (!StringUtil.isNull(attachPath)) {
				request.getSession().removeAttribute(currentAttachName);
				// TODO 首先将原有文件删除，如果删除失败这里不再处理，留待后续在周期性清理任务中进行
				ret = FileUtil.delFileOrFolder(ServletContextUtil
						.getWebRootPath() + attachPath);
			}
		}
		jo.put("ret", ret);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String filePath = (String) request.getParameter("filePath");
		String fileName = (String) request.getParameter("fileName");
		if (!StringUtil.isNull(filePath)) {
			File file = new File(ServletContextUtil.getWebRootPath() + filePath);
			if (!file.exists()) {
				// 文件不存在
				response.setCharacterEncoding("utf-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.write("The file is not exist.");
				printWriter.flush();
				printWriter.close();
				return;
			}
			String filename = file.getName();
			if (StringUtil.isNull(fileName)) {
				//response.addHeader("Content-Type", "application/msword");
				response.addHeader(
						"Content-Disposition",
						"attachment;filename=\""
								+ new String(filename.getBytes("utf-8"),
										"iso-8859-1")+"\"");
			} else {
				//response.addHeader("Content-Type", "application/msword");
				response.addHeader(
						"Content-Disposition",
						"attachment;filename=\""
								+ new String(fileName.getBytes("utf-8"),
										"iso-8859-1")+"\"");
			}
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType(request.getSession().getServletContext()
					.getMimeType(filename));
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			OutputStream fos = new BufferedOutputStream(
					response.getOutputStream());
			byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fos.write(buffer);
				buffer = new byte[1024];
			}
			fos.flush();
			fis.close();
			fos.close();
		}
	}

	/**
	 * UEditor上传图片
	 * 
	 * @param upfile
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadImage")
	@ResponseBody
	public String uploadImage(@RequestParam("upfile") MultipartFile upfile,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		JSONObject jo = new JSONObject();
		if (upfile != null) {
			String fileName = upfile.getOriginalFilename();
			String suffix = FileUtil.getSuffix(fileName);
			if (!StringUtil.isNull(suffix)) {
				// 构造上传图片唯一性标识
				String uuid = StringUtil.getUUID();
				// 上传图片的存放路径
				String imagePath = WebUtil.getUploadImagePath(uuid, suffix);
				logger.info("imagePath={}", imagePath);
				String desFilePath = ServletContextUtil.getWebRootPath()
						+ imagePath;
				logger.info("desFilePath={}", desFilePath);
				upfile.transferTo(new File(desFilePath));
				// 返回的json格式
				// {"name":"10571402926855858.jpg", "originalName": "china.jpg",
				// "size": 43086, "state": "SUCCESS", "type": ".jpg", "url":
				// "upload/20140616/10571402926855858.jpg"}
				jo.put("name", uuid + "." + suffix);
				jo.put("originalName", fileName);
				jo.put("size", upfile.getSize());
				jo.put("state", "SUCCESS");
				jo.put("type", "." + suffix);
				jo.put("url", imagePath.replace("\\", "@").replace(".", "_"));
			}
		}
		return jo.toJSONString();
	}

	/**
	 * 获取UEditor上传的图片
	 * 
	 * @param imagePath
	 * @param request
	 * @param response
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUploadImage/{imagePath}")
	public void getUploadImage(@PathVariable String imagePath,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		if (!StringUtil.isNull(imagePath)) {
			imagePath = imagePath.replace("@", "\\");
			imagePath = imagePath.replace("_", ".");
			FileInputStream in = null;
			OutputStream out = null;
			try {
				String desFilePath = ServletContextUtil.getWebRootPath()
						+ imagePath;
				File file = new File(desFilePath);
				in = new FileInputStream(file);
				byte[] b = new byte[(int) file.length()];
				in.read(b);
				response.setContentType("image/*");
				out = response.getOutputStream();
				out.write(b);
				out.flush();
			} catch (IllegalArgumentException e) {
				logger.error("GetUploadImage:", e);
			} catch (SecurityException e) {
				logger.error("GetUploadImage:", e);
			} catch (IOException e) {
				logger.error("GetUploadImage:", e);
			} finally {
				try {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (IOException e) {
					logger.error("GetUploadImage:", e);
				}
			}
		}
	}

	@RequestMapping(value = "/ueditor")
	@ResponseBody
	public String ueditor(HttpServletRequest request,
			HttpServletResponse response) {
		String rootPath = ServletContextUtil.getWebRootPath();
		String result = new ActionEnter(request, rootPath).exec();
		return result;
	}

	/**
	 * 是否具备管理员角色
	 * 
	 * @param roleid
	 * @return
	 */
	protected boolean hasAdministrator(String roleid) {
		return roleid.equalsIgnoreCase(ConstantData.SuperAdministrator + "")
				|| roleid.equalsIgnoreCase(ConstantData.Administrator + "");
	}

	protected String transfer2English(String area) {
		if (area.equalsIgnoreCase("新加坡")) {
			return "Singapore";
		} else if (area.equalsIgnoreCase("马来西亚")) {
			return "Malaysia";
		} else if (area.equalsIgnoreCase("印度尼西亚")) {
			return "Indonesia";
		} else if (area.equalsIgnoreCase("越南")) {
			return "Vietnam";
		} else if (area.equalsIgnoreCase("菲律宾")) {
			return "Philippines";
		} else if (area.equalsIgnoreCase("泰国")) {
			return "Thailand";
		} else if (area.equalsIgnoreCase("阿联酋")) {
			return "United Arab Emirates";
		} else if (area.equalsIgnoreCase("台湾")) {
			return "Taiwan";
		} else if (area.equalsIgnoreCase("香港")) {
			return "Hong Kong";
		} else if (area.equalsIgnoreCase("印度")) {
			return "India";
		} else if (area.equalsIgnoreCase("英国")) {
			return "United Kingdom";
		} else if (area.equalsIgnoreCase("瑞典")) {
			return "Sweden";
		} else if (area.equalsIgnoreCase("芬兰")) {
			return "Finland";
		} else if (area.equalsIgnoreCase("美国")) {
			return "America";
		} else if (area.equalsIgnoreCase("澳大利亚")) {
			return "Australia";
		} else if (area.equalsIgnoreCase("中国")) {
			return "China";
		} else {
			return area;
		}
	}
}
