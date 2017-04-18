package com.thirdparty.baidu.ueditor.upload;

import com.thirdparty.baidu.ueditor.PathFormat;
import com.thirdparty.baidu.ueditor.define.AppInfo;
import com.thirdparty.baidu.ueditor.define.BaseState;
import com.thirdparty.baidu.ueditor.define.FileType;
import com.thirdparty.baidu.ueditor.define.State;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());
		if (isAjaxUpload) {
			upload.setHeaderEncoding("UTF-8");
		}
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			String savePath = (String) conf.get("savePath");
			String originFileName = "";
			String suffix = "";
			State storageState = null;
			// 检查form中是否有enctype="multipart/form-data"
			if (multipartResolver.isMultipart(request)) {
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				Iterator<String> it = multiRequest.getFileNames();
				// 遍历文件
				while (it.hasNext()) {
					MultipartFile file = multiRequest.getFile(it.next()
							.toString());
					if (file != null) {
						originFileName = file.getOriginalFilename();
						suffix = FileType.getSuffixByFilename(originFileName);
						originFileName = originFileName.substring(0,
								originFileName.length() - suffix.length());
						savePath = savePath + suffix;
						long maxSize = ((Long) conf.get("maxSize")).longValue();
						if (!validType(suffix,
								(String[]) conf.get("allowFiles"))) {
							return new BaseState(false,
									AppInfo.NOT_ALLOW_FILE_TYPE);
						}
						savePath = PathFormat.parse(savePath, originFileName);
						String physicalPath = (String) conf.get("rootPath")
								+ savePath;
						InputStream is = file.getInputStream();
						storageState = StorageManager.saveFileByInputStream(is,
								physicalPath, maxSize);
						is.close();
					}
				}
			}
			if (storageState != null && storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}
			return storageState;
		} catch (IOException e) {
			
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
