package com.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关于File的通用操作
 * 
 * @author liaolinghao
 */
public class FileUtil {

	public static final int BYTE = 0;// 按字节计算

	public static final int K = 1;// 按KB计算

	public static final int M = 2;// 按MB计算

	public static final int G = 3;// 按GB计算

	public static final int T = 4;// 按TB计算

	public static final String[] imageType = new String[] { "bmp", "jpg",
			"png", "gif", "jpeg" };

	public static final String[] htmlType = new String[] { "html", "htm" };

	public static final String[] txtType = new String[] { "txt" };

	public static final String[] wordType = new String[] { "doc", "docx" };

	public static final String[] excelType = new String[] { "xls", "xlsx" };

	public static final String[] powerpointType = new String[] { "ppt", "pptx" };

	public static final String[] audioType = new String[] { "mp3", "wma", "wav" };

	public static final String[] videoType = new String[] { "mp4", "avi",
			"mpg", "wmv", "rm", "rmvb", "mpeg", "mkv" };

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 文件过滤器，对于指定格式的文件返回true,若未指定文件格式，则任何文件都返回true
	 * 
	 * @param temp
	 *            进行判断的文件
	 * @param filter
	 *            文件名后缀集合
	 * @return
	 */
	public static boolean filterResult(File temp, List<String> filter) {
		if (!temp.isFile()) {
			logger.error("The filter file shouldn't be document!");
			return false;
		}
		if (filter == null) {
			return true;
		}
		String fileSuffix = getSuffix(temp);
		for (String suffix : filter) {
			if (fileSuffix.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文件过滤器，对于指定格式的文件返回true,若未指定文件格式，则任何文件都返回true
	 * 
	 * @param fileName
	 *            进行判断的文件名
	 * @param filter
	 *            文件名后缀集合
	 * @return
	 */
	public static boolean filterResult(String fileName, List<String> filter) {
		if (fileName == null) {
			logger.error("The file name shouldn't be null!");
			return false;
		}
		if (filter == null) {
			return true;
		}
		String fileSuffix = getSuffix(fileName);
		for (String suffix : filter) {
			if (fileSuffix.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理文件分隔符,把各种文件分隔符转换成平台标准文件分隔符,同时去除末尾多余分隔符
	 * 
	 * @param filePath
	 * @return
	 */
	public static String processFileSeparator(String filePath) {
		if (filePath == null) {
			return null;
		}
		filePath = filePath.replace("\\", File.separator);
		filePath = filePath.replace("/", File.separator);
		if (filePath.endsWith(File.separator)) {
			filePath = filePath.substring(0, filePath.length() - 1);
		}
		return filePath;
	}

	/**************************************** 判断是否为图像文件 *************************************************/

	/**
	 * 判断文件是否是图像文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : imageType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是图像文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isImageFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : imageType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为网页文件 *************************************************/

	/**
	 * 判断文件是否是网页文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isHtmlFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : htmlType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是网页文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isHtmlFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : htmlType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为txt文件 *************************************************/

	/**
	 * 判断文件是否是txt文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTxtFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : txtType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是txt文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isTxtFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : txtType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为word文件 *************************************************/

	/**
	 * 判断文件是否是word文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isWordFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : wordType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是word文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isWordFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : wordType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为excel文件 *************************************************/

	/**
	 * 判断文件是否是excel文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExcelFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : excelType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是excel文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isExcelFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : excelType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为powerpoint文件 *************************************************/

	/**
	 * 判断文件是否是powerpoint文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isPowerPointFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : powerpointType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是powerpoint文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isPowerPointFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : powerpointType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为音频文件 *************************************************/

	/**
	 * 判断文件是否是音频文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isAudioFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : audioType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是音频文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isAudioFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : audioType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 判断是否为视频文件 *************************************************/

	/**
	 * 判断文件是否是视频文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isVideoFile(File file) {
		if (file == null) {
			return false;
		}
		String suffix = getSuffix(file);
		for (String type : videoType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否是视频文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isVideoFile(String name) {
		if (name == null) {
			return false;
		}
		String suffix = getSuffix(name);
		for (String type : videoType) {
			if (type.equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 获取文件格式 *************************************************/

	/**
	 * 获取文件格式
	 * 
	 * @param file
	 * @return
	 */
	public static String getSuffix(File file) {
		if (file == null) {
			return null;
		}
		String suffix = file.getName();
		int index = suffix.lastIndexOf('.');
		if (index != -1) {
			suffix = suffix.substring(index + 1);
			return suffix;
		}
		return "";
	}

	/**
	 * 获取文件格式
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf('.');
		if (index != -1) {
			String suffix = fileName.substring(index + 1);
			return suffix;
		}
		return "";
	}

	/**************************************** 获取文件名称 *************************************************/

	/**
	 * 获取文件的不包含文件格式的名字
	 * 
	 * @param file
	 * @return
	 */
	public static String getRealName(File file) {
		if (file == null) {
			return null;
		}
		String name = file.getName();
		int index = name.lastIndexOf('.');
		if (index != -1) {
			name = name.substring(0, index);
		}
		return name;
	}

	/**
	 * 获取文件的不包含文件格式的名字
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getRealName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index != -1) {
			fileName = fileName.substring(0, index);
		}
		return fileName;
	}

	/**
	 * 获取完整文件名，该方法并不验证文件的有效性
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileFullName(String filePath) {
		int index = filePath.lastIndexOf(File.separator);
		String fileName = filePath.substring(index + 1);
		return fileName;
	}

	/**
	 * 获取指定文件路径对应的目录绝对路径，如果传递的是一个文件，则获取对应父目录，如果传递的不是目录，也不是文件，则返回空
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFolderPath(String filePath) {
		File file = new File(filePath);
		String folderPath = null;
		if (file.isFile()) {
			folderPath = file.getParentFile().getAbsolutePath();
		} else if (file.isDirectory()) {
			folderPath = file.getAbsolutePath();
		}
		if (!StringUtil.isNull(folderPath)) {
			if (!folderPath.endsWith(File.separator)) {
				folderPath = folderPath + File.separator;
			}
		}
		return folderPath;
	}

	/**************************************** 对象信息序列化与反序列化 *************************************************/

	/**
	 * 保存对象信息到指定路径的文件中
	 * 
	 * @param obj
	 *            要保存的对象
	 * @param path
	 *            指定路径
	 * @return
	 */
	public static boolean saveObj(Object obj, String path) {
		try {
			FileOutputStream bytetOut = new FileOutputStream(path);
			ObjectOutputStream outer = new ObjectOutputStream(bytetOut);
			outer.writeObject(obj);
			outer.flush();
			outer.close();
		} catch (IOException e) {
			logger.error("Save Object:", e);
			return false;
		}
		return true;
	}

	/**
	 * 将文件中的对象信息反序列化为对象
	 * 
	 * @param path
	 *            指定路径
	 * @return
	 */
	public static Object getObj(String path) {
		try {
			InputStream is = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(is);
			Object obj = ois.readObject();
			is.close();
			ois.close();
			return obj;
		} catch (Exception e) {
			logger.error("Get Object:", e);
			return null;
		}
	}

	/**************************************** 新建文件 *************************************************/

	/**
	 * 新建文件，同时写入文件内容，新文件会完全覆盖旧文件
	 * 
	 * @param filePathAndName
	 * @param fileContent
	 *            为null，则只创建文件
	 */
	public static boolean newFile(String filePathAndName, String fileContent) {
		return newFile(filePathAndName, fileContent, false);
	}

	/**
	 * 新建文件，同时写入文件内容
	 * 
	 * @param filePathAndName
	 * @param fileContent
	 * @param append
	 *            文件内容是否采用追加的形式
	 * @return
	 */
	public static boolean newFile(String filePathAndName, String fileContent,
			boolean append) {
		FileWriter resultFile = null;
		PrintWriter myFile = null;
		try {
			File myFilePath = new File(filePathAndName);
			File dir = myFilePath.getParentFile();
			if (dir != null) {
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						return false;
					}
				}
			}
			if (!myFilePath.exists()) {
				if (!myFilePath.createNewFile()) {
					return false;
				}
			}
			if (fileContent != null) {
				resultFile = new FileWriter(myFilePath, append);
				myFile = new PrintWriter(resultFile);
				myFile.println(fileContent);
			}
			return true;
		} catch (Exception e) {
			logger.error("Create File:", e);
			return false;
		} finally {
			if (resultFile != null) {
				try {
					resultFile.close();
				} catch (IOException e) {
					logger.error("Create File:", e);
				}
			}

			if (myFile != null) {
				myFile.close();
			}
		}
	}

	/**
	 * 创建一个文件夹
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean newFolder(String folderPath) {
		boolean bFlag = false;
		if (!StringUtil.isNull(folderPath)) {
			File myFolderPath = new File(folderPath);
			if (myFolderPath.isFile()) {
				myFolderPath = myFolderPath.getParentFile();
			}
			if (myFolderPath != null) {
				if (!myFolderPath.exists()) {
					if (myFolderPath.mkdirs()) {
						bFlag = true;
					}
				} else {
					bFlag = true;
				}
			}
		}
		return bFlag;
	}

	/**
	 * 将字节数组中的内容写入到文件中
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	public static boolean write(String path, byte[] content) {
		return write(new File(path), new ByteArrayInputStream(content));
	}

	/**
	 * 将字节数组中的内容写入到文件中
	 * 
	 * @param tmp
	 * @param content
	 * @return
	 */
	public static boolean write(File tmp, byte[] content) {
		return write(tmp, new ByteArrayInputStream(content));
	}

	/**
	 * 将输入流中的内容写入到文件中
	 * 
	 * @param tmp
	 * @param in
	 */
	public static boolean write(File tmp, InputStream in) {
		FileOutputStream fos = null;
		try {
			File dir = tmp.getParentFile();
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return false;
				}
			}
			fos = new FileOutputStream(tmp);
			int index = 0;
			byte[] buffer = new byte[4096];
			while ((index = in.read(buffer)) != -1) {
				fos.write(buffer, 0, index);
			}
			return true;
		} catch (Exception e) {
			logger.error("Write:", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("Write:", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Write:", e);
				}
			}
		}
	}

	/**
	 * 以指定编码格式将文件内容写入保存的文件路径中
	 * 
	 * @param s_FileTxt
	 *            文件内容
	 * @param s_FilePath
	 *            文件路劲
	 * @param s_Encode
	 *            编码格式
	 * @return
	 */
	public static boolean saveFile(String s_FileTxt, String s_FilePath,
			String s_Encode) {
		Writer out = null;
		try {
			File myFilePath = new File(s_FilePath);
			File dir = myFilePath.getParentFile();
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return false;
				}
			}
			out = new OutputStreamWriter(
					new FileOutputStream(s_FilePath, false), s_Encode);
			out.write(s_FileTxt);
		} catch (Exception e) {
			logger.error("Save file:", e);
			return false;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("Save file:", e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 保存网页，以字节形式保存可以解决乱码问题
	 * 
	 * @param filePathAndName
	 *            要保存到的本地指定文件（包含文件路径）
	 * @param url
	 *            网络路径
	 * @return 保存是否成功
	 */
	public static boolean saveHtmlFileByByte(String filePathAndName, String url) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			File myFilePath = new File(filePathAndName);
			File dir = myFilePath.getParentFile();
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return false;
				}
			}
			if (!myFilePath.exists()) {
				if (!myFilePath.createNewFile()) {
					return false;
				}
			}
			fos = new FileOutputStream(myFilePath);
			URL source = new URL(url);
			String encoding = source.openConnection().getContentEncoding();
			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				is = new GZIPInputStream(source.openStream());
			} else {
				is = source.openStream();
			}
			int index = 0;
			byte[] buffer = new byte[4096];
			while ((index = is.read(buffer)) != -1) {
				fos.write(buffer, 0, index);
			}
			return true;
		} catch (Exception e) {
			logger.error("Save html file by byte:", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("Save html file by byte:", e);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("Save html file by byte:", e);
				}
			}
		}
	}

	/**************************************** 删除文件或者文件夹 *************************************************/

	/**
	 * 删除文件或文件夹
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delFileOrFolder(File file) {
		return delFileOrFolder(file.getPath());
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param fileOrFolderPath
	 * @return
	 */
	public static boolean delFileOrFolder(String fileOrFolderPath) {
		if (fileOrFolderPath == null) {
			return false;
		}
		fileOrFolderPath = processFileSeparator(fileOrFolderPath);
		File file = new File(fileOrFolderPath);
		if (!file.exists()) {
			return false;
		}
		if (!file.isDirectory()) {
			return file.delete();
		} else {
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				temp = new File(fileOrFolderPath + File.separator + tempList[i]);
				if (temp.isFile()) {
					if (!temp.delete()) {
						return false;
					}
				}
				if (temp.isDirectory()) {
					if (!delFileOrFolder(temp.getAbsolutePath())) {
						return false;
					}
				}
			}
			return file.delete();
		}
	}

	/**************************************** 清空文件夹 *************************************************/

	/**
	 * 清空文件夹，不删除该文件夹，删除该文件夹下的所有文件和子文件夹
	 * 
	 * @param dirName
	 * @return
	 */
	public static boolean clearDir(String dirName) {
		File dir = new File(dirName);
		return clearDir(dir);
	}

	/**
	 * 清空文件夹，不删除该文件夹，删除该文件夹下的所有文件和子文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean clearDir(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return false;
		File[] subFiles = dir.listFiles();
		for (int i = 0; i < subFiles.length; i++) {
			if (subFiles[i].isFile()) {
				if (!subFiles[i].delete())
					return false;
			} else if (subFiles[i].isDirectory()) {
				if (!delFileOrFolder(subFiles[i]))
					return false;
			}
		}
		return true;
	}

	/**
	 * 清空目录下除excludeFiles外的所有文件
	 * 
	 * @param dir
	 * @param excludeFiles
	 *            为null或者未指定排除文件，则清空所有文件
	 * @return
	 */
	public static boolean clearDirExcludeFiles(File dir, File[] excludeFiles) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return false;
		}
		if (excludeFiles == null || excludeFiles.length < 1) {
			return clearDir(dir);
		} else {
			boolean exclude = false;
			File[] subFiles = dir.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				exclude = false;
				for (int j = 0; j < excludeFiles.length; j++) {
					if (subFiles[i].getAbsolutePath().equalsIgnoreCase(
							excludeFiles[j].getAbsolutePath())) {
						exclude = true;
						break;
					}
				}
				if (exclude) {
					continue;
				}
				if (subFiles[i].isFile()) {
					if (!subFiles[i].delete()) {
						return false;
					}
				} else if (subFiles[i].isDirectory()) {
					if (!clearDirExcludeFiles(subFiles[i], excludeFiles)) {
						return false;
					}
					if (subFiles[i].list().length == 0) {
						if (!delFileOrFolder(subFiles[i])) {
							return false;
						}
					}
				}
			}
			return true;
		}
	}

	/**************************************** 复制文件或文件夹 *************************************************/

	/**
	 * 复制文件到另一文件中
	 * 
	 * @param srcFile
	 *            源文件
	 * @param desFile
	 *            目标文件
	 * 
	 */
	public static boolean copyFileToOtherFile(File srcFile, File desFile) {
		if (srcFile == null) {
			return false;
		}
		if (desFile == null) {
			return false;
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			// 先创建目标文件所在文件夹，创建失败，不直接返回false，让函数执行报错，以便于分析
			File dir = desFile.getParentFile();
			if (dir != null) {
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(desFile);
			int byteread = 0;
			byte[] buffer = new byte[4096];
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			logger.error("Copy file to otherFile:", e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					logger.error("Copy file to otherFile:", ioe);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					logger.error("Copy file to otherFile:", ioe);
				}
			}
		}
		return true;
	}

	/**
	 * 复制文件到另一文件中
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param desPath
	 *            目标文件路径
	 * 
	 * @exception IOException
	 *                如果读入或者写出流关闭异常
	 */
	public static boolean copyFileToOtherFile(String srcPath, String desPath) {
		if (srcPath == null) {
			return false;
		}
		if (desPath == null) {
			return false;
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			// 先创建目标文件所在文件夹，创建失败，不直接返回false，让函数执行报错，以便于分析
			File desFile = new File(desPath);
			File dir = desFile.getParentFile();
			if (dir != null) {
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			in = new FileInputStream(srcPath);
			out = new FileOutputStream(desPath);
			int byteread = 0;
			byte[] buffer = new byte[4096];
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			logger.error("Copy file to otherFile:", e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					logger.error("Copy file to otherFile:", ioe);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					logger.error("Copy file to otherFile:", ioe);
				}
			}
		}
		return true;
	}

	/**
	 * 复制文件或文件夹到指定的文件夹，保持源目录结构
	 * 
	 * @param srcPath
	 *            源文件或文件夹路径
	 * @param desPath
	 *            目标文件夹路径
	 * @param filter
	 *            文件后缀集合，若为空，则复制所有文件
	 */
	public static boolean copyFileOrFolder(String srcPath, String desPath,
			List<String> filter) {
		if (srcPath == null) {
			return false;
		}
		if (desPath == null) {
			return false;
		}
		srcPath = processFileSeparator(srcPath);
		desPath = processFileSeparator(desPath);
		try {
			File srcFile = new File(srcPath);
			File desDir = new File(desPath);
			if (!srcFile.exists()) {
				return false;
			}
			// 若文件不存在，必须先创建后判断
			if (!desDir.exists()) {
				if (!desDir.mkdirs()) {
					return false;
				}
			}
			if (desDir.isFile()) {
				return false;
			}
			// 目标文件夹是源文件夹的子文件夹不允许复制
			if (isChildFolder(srcFile, desDir)) {
				return false;
			}
			if (srcFile.isFile()) {
				if (filterResult(srcFile, filter)) {
					int byteread = 0;
					String fileName = srcFile.getName();
					InputStream inStream = new FileInputStream(srcPath);
					FileOutputStream fos = new FileOutputStream(desPath
							+ File.separator + fileName);
					byte[] buffer = new byte[4096];
					while ((byteread = inStream.read(buffer)) != -1) {
						fos.write(buffer, 0, byteread);
					}
					inStream.close();
					fos.close();
				}
			} else if (srcFile.isDirectory()) {
				String[] file = srcFile.list();
				if (file.length > 0) {
					File temp = null;
					for (int i = 0; i < file.length; i++) {
						temp = new File(srcPath + File.separator + file[i]);
						if (temp.isFile()) {
							if (filterResult(temp, filter)) {
								if (!copyFileOrFolder(
										temp.getAbsolutePath(),
										desPath + File.separator
												+ srcFile.getName(), filter)) {
									return false;
								}
							}
						}
						if (temp.isDirectory()) {
							if (!copyFileOrFolder(
									temp.getAbsolutePath(),
									desPath + File.separator
											+ srcFile.getName(), filter)) {
								return false;
							}
						}
					}
				} else {
					File desDir2 = new File(desPath + File.separator
							+ srcFile.getName());
					if (!desDir2.exists()) {
						return desDir2.mkdirs();
					}
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("CopyFileOrFolder:", e);
			return false;
		}
	}

	/**************************************** 剪切文件或文件夹 *************************************************/

	/**
	 * 剪切文件或文件夹到指定文件夹
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean moveFileOrFolder(String oldPath, String newPath) {
		if (copyFileOrFolder(oldPath, newPath, null)) {
			return delFileOrFolder(oldPath);
		} else {
			return false;
		}
	}

	/**************************************** 获取文件内容同时进行部分处理 *************************************************/

	/**
	 * 将读取的文件内容以字符串形式返回，若keepFormat取值为true，则保持原有行格式，否则将文件内容做为一条字符串返回
	 * 
	 * @param file
	 * @param keepFormat
	 * @return
	 */
	public static String readContent(File file, boolean keepFormat) {
		return readContent(file.getAbsolutePath(), keepFormat);
	}

	/**
	 * 将读取的文件内容以字符串形式返回，若keepFormat取值为true，则保持原有行格式，否则将文件内容做为一条字符串返回
	 * 
	 * @param filePathAndName
	 * @param keepFormat
	 * @return
	 */
	public static String readContent(String filePathAndName, boolean keepFormat) {
		StringBuffer stringBuffer = new StringBuffer();
		File myFile = new File(filePathAndName);
		if (!myFile.exists()) {
			return null;
		}
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(myFile));
			String line = fileReader.readLine();
			while (line != null) {
				if (keepFormat) {
					stringBuffer.append(line).append(
							System.getProperty("line.separator"));
				} else {
					stringBuffer.append(line);
				}
				line = fileReader.readLine();
			}
		} catch (Exception e) {
			logger.error("ReadContent:", e);
			return null;
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("ReadContent:", e);
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 该方法对于某些特殊格式的文件内容实现行排序,若ascend取值为true，则按升序排序，否则按降序排序
	 * 
	 * @param filePathAndName
	 * @param ascend
	 * @return
	 */
	public static String orderContentByLine(String filePathAndName,
			boolean ascend) {
		StringBuffer stringBuffer = new StringBuffer();
		List<String> list = new ArrayList<String>();
		File myFile = new File(filePathAndName);
		if (!myFile.exists()) {
			return "File is not exist!";
		}
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(myFile));
			String line = fileReader.readLine();
			while (line != null) {
				list.add(line);
				line = fileReader.readLine();
			}
			if (ascend) {
				Collections.sort(list);
			} else {
				Collections.sort(list, new Comparator<String>() {
					public int compare(String o1, String o2) {
						if (o1.compareTo(o2) == 0) {
							return 0;
						} else if (o1.compareTo(o2) > 0) {
							return -1;
						} else {
							return 1;
						}
					}
				});
			}
			for (int i = 0; i < list.size(); i++) {
				stringBuffer.append(list.get(i)).append('\n');
			}
		} catch (Exception e) {
			logger.error("OrderContentByLine:", e);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("OrderContentByLine:", e);
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 该方法针对某些特殊格式的文件，实现按特殊字符或字符串分割文件内容，并将结果按行格式排列
	 * 
	 * @param filePathAndName
	 * @param splitStr
	 * @return
	 */
	public static String splitContentByStr(String filePathAndName,
			String splitStr) {
		String content = readContent(filePathAndName, false);
		String content2 = content.replace(splitStr, String.valueOf('\n'));
		return content2;
	}

	/**
	 * 获取文件的字节内容
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getBytes(File file) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileInputStream in = null;
		int length = 0;
		byte[] sendBytes = null;
		try {
			in = new FileInputStream(file);
			sendBytes = new byte[1024 * 4];
			while ((length = in.read(sendBytes, 0, sendBytes.length)) > 0) {
				out.write(sendBytes, 0, length);
			}
		} catch (IOException e) {
			logger.error("GetBytes:", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("GetBytes:", e);
				}
			}
		}
		return out.toByteArray();
	}

	/**
	 * 以字节数组形式返回网络文件内容
	 * 
	 * @param urlStr
	 *            网络文件地址
	 * @return
	 */
	public static byte[] getContentByBytes(String urlStr) {
		byte[] data = null;
		DataInputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			is = new DataInputStream(connection.getInputStream());
			List<byte[]> chunks = new ArrayList<byte[]>();
			byte[] buffer = new byte[1024 * 1000];
			int read = -1;
			int size = 0;
			while ((read = is.read(buffer)) != -1) {
				if (read > 0) {
					byte[] chunk = new byte[read];
					System.arraycopy(buffer, 0, chunk, 0, read);
					chunks.add(chunk);
					size += chunk.length;
				}
			}
			if (size > 0) {
				bos = new ByteArrayOutputStream(size);
				for (Iterator<byte[]> itr = chunks.iterator(); itr.hasNext();) {
					byte[] chunk = (byte[]) itr.next();
					bos.write(chunk);
				}
				data = bos.toByteArray();
			}
		} catch (Exception e) {
			logger.error("Get content by bytes:", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("Get content by bytes:", e);
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("Get content by bytes:", e);
				}
			}
		}
		return data;
	}

	/**
	 * 以指定编码形式返回网络文件内容
	 * 
	 * @param urlStr
	 *            网络文件地址
	 * @param encoding
	 *            指定编码
	 * @return
	 */
	public static String getContentStringByEncoding(String urlStr,
			String encoding) {
		byte[] data = getContentByBytes(urlStr);
		if (data != null) {
			try {
				return new String(data, encoding);
			} catch (UnsupportedEncodingException e) {
				logger.error("Get content by encode:", e);
				return null;
			}
		} else {
			return null;
		}
	}

	/**************************************** 获取文件夹大小 *************************************************/

	/**
	 * 得到一个目录或者文件的大小，以字节为单位
	 * 
	 * @param directory
	 * @return
	 */
	private static long getDirSize(File directory) {
		if (directory == null || !directory.exists()) {
			return -1;
		}
		long size = 0;
		if (directory.isFile()) {
			size += directory.length();
		} else if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				size += getDirSize(files[i]);
			}
		}
		return size;
	}

	/**
	 * 得到一个目录或者文件的大小，默认按字节单位返回
	 * 
	 * @param directory
	 * @param type
	 * @return
	 */
	public static float getDirSize(File directory, int type) {
		NumberFormat formatter = new DecimalFormat("#.##");
		long bytes = getDirSize(directory);
		switch (type) {
		case BYTE:
			return bytes;
		case K:
			return Float.parseFloat(formatter.format(bytes / 1024.0));
		case M:
			return Float.parseFloat(formatter.format(bytes / 1024.0 / 1024.0));
		case G:
			return Float.parseFloat(formatter
					.format(bytes / 1024.0 / 1024.0 / 1024.0));
		case T:
			return Float.parseFloat(formatter.format(bytes / 1024.0 / 1024.0
					/ 1024.0 / 1024.0));
		}
		return bytes;
	}

	/**************************************** 判断文件夹之间的关系 *************************************************/

	/**
	 * 判断目标文件夹是否是源文件夹的子文件夹，该方法适合用于单机应用程序
	 * 
	 * @param srcPath
	 *            源文件夹路径
	 * @param desPath
	 *            目标文件夹路径
	 * @return
	 */
	public static boolean isChildFolder(String srcPath, String desPath) {
		return isChildFolder(new File(srcPath), new File(desPath));
	}

	/**
	 * 判断目标文件夹是否是源文件夹的子文件夹，该方法适合用于单机应用程序
	 * 
	 * @param src
	 *            源文件夹
	 * @param dest
	 *            目标文件夹
	 * @return
	 */
	public static boolean isChildFolder(File src, File dest) {
		if (src != null && dest != null && src.isDirectory()
				&& dest.isDirectory()) {
			File[] files = src.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					if (dest.getAbsolutePath().equals(file.getAbsolutePath())
							|| isChildFolder(file, dest))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 遍历文件夹中的文件，将路径加入对应集合中
	 * 
	 * @param folderPath
	 * @param fileList
	 * @return
	 */
	public static ArrayList<String> refreshFileList(String folderPath,
			ArrayList<String> fileList) {
		File dir = new File(folderPath);
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				refreshFileList(files[i].getAbsolutePath(), fileList);
			} else {
				fileList.add(files[i].getAbsolutePath().toLowerCase());
			}
		}
		return fileList;
	}

	/**************************************** 主方法用于测试 *************************************************/
	public static void main(String[] args) throws IOException {
		
	}
}
