package com.web.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.ArrayUtil;
import com.utils.Coder;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.utils.ini.INIFileAccessor;
import com.utils.office.ExcelUtil;
import com.utils.url.Url_Get_Post;
import com.web.common.ConstantData;
import com.web.utils.arithmetic.DES;

public class WebUtil {

	public static final String IMAGETYPE = ".png";

	private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
	
	
	public static void headers(HttpServletRequest request){
		Enumeration<String> h = request.getHeaderNames();
		while ( h.hasMoreElements() ){
			String name = (String )h.nextElement();			
			Enumeration<String> headers = request.getHeaders(name);
			while (headers.hasMoreElements()) {			
			                String headerValue = headers.nextElement();			
			                logger.info("head: {}={}", name, headerValue);			
			}	
		}
	}

	/**
	 * 获取服务器上web系统文件夹所在的真实路径
	 * 
	 * @param webPath
	 * @return
	 */
	public static String convertWebPath(String webPath) {
		
		String path = WebUtil.class.getResource("/").getPath();
		
		//path = path.replace("WEB-INF"+File.separator+"classes"+File.separator, "WEB-INF"+File.separator) + webPath;
		path = path+"../" + webPath;		
		logger.info("webPath={}", path);
		return path;
	}

	/**
	 * 获取网站存放上传图片的路径
	 * 
	 * @param uploadImageMark
	 * @param suffix
	 * @return
	 */
	public static String getUploadImagePath(String uploadImageMark,
			String suffix) {
		StringBuffer sb = new StringBuffer();
		sb.append("../UploadImages/");		
		sb.append(uploadImageMark);
		sb.append(".");
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * 获取网站存放上传视频的路径
	 * 
	 * @param uploadVideoMark
	 * @param suffix
	 * @return
	 */
	public static String getUploadVideoPath(String uploadVideoMark,
			String suffix) {
		StringBuffer sb = new StringBuffer();
		sb.append("../UploadVideos/");
		
		sb.append(uploadVideoMark);
		sb.append(".");
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * 获取网站存放上传附件的路径
	 * 
	 * @param uploadAttachMark
	 * @param suffix
	 * @return
	 */
	public static String getUploadAttachPath(String uploadAttachMark,
			String suffix) {
		StringBuffer sb = new StringBuffer();
		sb.append("../Attachments/");
		
		sb.append(uploadAttachMark);
		sb.append(".");
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * 获取网站存放上传附件的临时路径
	 * 
	 * @param uploadAttachMark
	 * @param suffix
	 * @return
	 */
	public static String getUploadAttachTmpPath(String uploadAttachMark,
			String suffix, String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append("Attachments/");		
		sb.append("tmp/");
		sb.append(uid);
		sb.append("/");
		sb.append(uploadAttachMark);
		sb.append(".");
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * 获取网站报告文件目录
	 * 
	 * @return
	 */
	public static String getReportPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append("/Reports/");
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 获取网站临时文件目录
	 * 
	 * @return
	 */
	public static String getTempPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append("Temp/");
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 获取网站附件目录
	 * 
	 * @return
	 */
	public static String getAttachPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append("Attachments/");
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 获取网站头像目录
	 * 
	 * @return
	 */
	public static String getAvatarPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append(convertWebPath("Avatars/"));
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 获取网站依赖的DLL文件目录
	 * 
	 * @return
	 */
	public static String getDependencyPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append(convertWebPath("Dependency/"));
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		
		return sb.toString();
	}

	/**
	 * 获取网站上传图片目录
	 * 
	 * @return
	 */
	public static String getUploadImagePath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append(convertWebPath("UploadImages/"));
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 获取网站上传视频目录
	 * 
	 * @return
	 */
	public static String getUploadVideoPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(ServletContextUtil.getWebRootPath());
		sb.append(convertWebPath("UploadVideos/"));
		File directory = new File(sb.toString());
		if ( !directory.exists()){
	        directory.mkdir();	        
	    }
		return sb.toString();
	}

	/**
	 * 判断文件格式是否在白名单中，注意：白名单的配置与ueditor保持一致
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean inWhiteList(String suffix) {
		String wl = convertWebPath("conf/")+"whiteList.txt";
		String content = FileUtil.readContent(wl, false);
		return ArrayUtil.contain(content.split(","), suffix);
	}

	/**
	 * 获取系统预定义专业类型
	 * 
	 * @return
	 */
	public static Hashtable<String, String> getPredefinedTrade() {
		String filePath = convertWebPath("conf/")+"trade.ini";
		INIFileAccessor ini = new INIFileAccessor(new File(filePath), false);
		return ini.getSection("Trade");
	}

	/**
	 * 获取时区集合
	 * 
	 * @return
	 */
	public static List<String> getTimeZones() {
		List<String> timeZones = new ArrayList<String>();
		Workbook workbook = null;
		try {
			String filePath = convertWebPath("conf/")+"timeZone.xlsx";
			workbook = ExcelUtil.getWorkbook(filePath);
			Sheet sheet = workbook.getSheetAt(0);
			int firstRowIndex = sheet.getFirstRowNum();
			int lastRowIndex = sheet.getLastRowNum();
			for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
				Row currentRow = sheet.getRow(rowIndex);// 当前行
				Cell currentCell = currentRow.getCell(1);// 当前单元格
				String currentCellValue = ExcelUtil.getCellValue(currentCell,
						true);// 当前单元格的值
				if (!StringUtil.isNull(currentCellValue)) {
					timeZones.add(currentCellValue);
				}
			}
		} catch (Exception e) {
			logger.error("GetTimeZones:", e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("Close:", e);
				}
			}
		}
		return timeZones;
	}

	/**
	 * 获取时间描述
	 * 
	 * @param nowTime
	 * @param lastTime
	 * @return
	 */
	public static String getTimeDesc(Date nowTime, Date lastTime) {
		StringBuffer sb = new StringBuffer();
		// 将差值转换为秒
		long between = (nowTime.getTime() - lastTime.getTime()) / 1000;
		// 计算天数差别
		long day = between / (24 * 3600);
		if (day > 0) {
			if (day > 1) {
				sb.append("<b>").append(day).append("</b>").append(" days");
			} else {
				sb.append("<b>").append(day).append("</b>").append(" day");
			}
		} else {
			long hour = between / 3600;
			if (hour > 0) {
				if (hour > 1) {
					sb.append("<b>").append(hour).append("</b>")
							.append(" hours");
				} else {
					sb.append("<b>").append(hour).append("</b>")
							.append(" hour");
				}
			} else {
				long minute = between / 60;
				if (minute > 0) {
					if (minute > 1) {
						sb.append("<b>").append(minute).append("</b>")
								.append(" minutes");
					} else {
						sb.append("<b>").append(minute).append("</b>")
								.append(" minute");
					}
				} else {
					if (between > 1) {
						sb.append("<b>").append(between).append("</b>")
								.append(" seconds");
					} else {
						sb.append("<b>").append(between).append("</b>")
								.append(" second");
					}
				}
			}
		}
		sb.append(" ago");
		return sb.toString();
	}

	/**
	 * 获取用户请求IP，这里通过多种方式分析是为了避免使用反向代理软件造成客户请求IP不真实的原因
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (!StringUtil.isNull(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				ip = ip.substring(0, index);
			}
			return ip;
		} else {
			return null;
		}
	}

	/**
	 * 获取IP对应的国家
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIpAddr4Country(String ip) {
		String res = Url_Get_Post.get(
				"http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="
						+ ip, null, true);
		JSONObject jo = JSON.parseObject(res);
		String country = jo.getString("country");
		if (!StringUtil.isNull(country)) {
			return Coder.revertUnicode(country);
		} else {
			return null;
		}
	}

	/**
	 * 获取IP对应的省
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIpAddr4Province(String ip) {
		String res = Url_Get_Post.get(
				"http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="
						+ ip, null, true);
		JSONObject jo = JSON.parseObject(res);
		String province = jo.getString("province");
		if (!StringUtil.isNull(province)) {
			return Coder.revertUnicode(province);
		} else {
			return null;
		}
	}

	/**
	 * 获取IP对应的城市
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIpAddr4City(String ip) {
		String res = Url_Get_Post.get(
				"http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="
						+ ip, null, true);
		JSONObject jo = JSON.parseObject(res);
		String city = jo.getString("city");
		if (!StringUtil.isNull(city)) {
			return Coder.revertUnicode(city);
		} else {
			return null;
		}
	}

	/**
	 * 解密信息
	 * 
	 * @param info
	 * @return
	 */
	public static String decrypt(String info) {
		DES des = new DES();
		return des.strDec(info, ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
	}
	
	/**
	 * 加密信息
	 * 
	 * @param info
	 * @return
	 */
	public static String crypt(String info) {
		DES des = new DES();
		return des.strEnc(info, ConstantData.FirstKey, ConstantData.SecondKey,
				ConstantData.ThirdKey);
	}

}
