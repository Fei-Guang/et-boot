package com.utils.ini;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.DataUtil;
import com.utils.StringUtil;

/**
 * 这个类用来分析INI文件, 可以对INI文件进行简单的访问,如果希望保存更为详细的信息,可以使用XML文件
 * 
 * @author liao.lh
 * 
 */
public class INIFileAccessor implements Serializable, INIData {

	private static final long serialVersionUID = -8003249168164547092L;

	private static Logger logger = LoggerFactory
			.getLogger(INIFileAccessor.class);

	private Vector<Section> infoList; // 存放Section集合
	private transient Section currentSection; // 当前Section
	private transient String iniFilePath; // 存放INI格式数据的文件路径
	private boolean trim = true; // 是否去掉key和value两头的空格

	/* ==========================构造方法============================== */

	public INIFileAccessor() {
		this(true);
	}

	public INIFileAccessor(boolean trim) {
		this.trim = trim;
		infoList = new Vector<Section>();
		currentSection = new Section();
	}

	/**
	 * 通过指定url来构造类的实例
	 * 
	 * @param url
	 */
	public INIFileAccessor(URL url) {
		this(url, false);
	}

	/**
	 * 通过指定url来构造类的实例,并根据local属性决定编码方式
	 * 
	 * @param url
	 * @param local
	 */
	public INIFileAccessor(URL url, boolean local) {
		this();
		try {
			if (!local) {
				load(url.openStream());
			} else {
				loadlocal(url.openStream());
			}
		} catch (Throwable t) {
			throw new IllegalStateException(t.toString());
		}
	}

	/**
	 * 通过指定url来构造类的实例,并采用指定的encode编码方式
	 * 
	 * @param url
	 * @param encode
	 */
	public INIFileAccessor(URL url, String encode) {
		this();
		try {
			load(url.openStream(), encode);
		} catch (Throwable t) {
			throw new IllegalStateException(t.toString());
		}
	}

	/**
	 * 通过指定文件来构造类的实例,如果文件不存在, 则创建文件
	 * 
	 * @param file
	 */
	public INIFileAccessor(File file) {
		this(file, true);
	}

	/**
	 * 通过指定文件来构造类的实例, 如果文件不存在,根据isCreate属性决定是否创建
	 * 
	 * @param fl
	 * @param isCreate
	 */
	public INIFileAccessor(File fl, boolean isCreate) {
		this(fl.getAbsolutePath(), isCreate);
	}

	/**
	 * 通过指定文件路径来构造类的实例,如果文件不存在, 则创建文件
	 * 
	 * @param filepath
	 */
	public INIFileAccessor(String filepath) {
		this(filepath, true);
	}

	/**
	 * 通过指定文件名来构造类的实例,如果文件不存在,根据isCreate属性决定是否创建
	 * 
	 * @param filepath
	 * @param isCreate
	 */
	public INIFileAccessor(String filepath, boolean isCreate) {
		this(filepath, isCreate, false);
	}

	/**
	 * 通过指定文件名来构造类的实例,如果文件不存在,根据isCreate属性决定是否创建,根据local属性决定是否采用本地编码
	 * 
	 * @param filepath
	 * @param isCreate
	 * @param local
	 */
	public INIFileAccessor(String filepath, boolean isCreate, boolean local) {
		this();
		iniFilePath = filepath;
		FileInputStream in = null;
		File iniFile = new File(iniFilePath);
		try {
			if (iniFile.exists()) {
				in = new FileInputStream(iniFilePath);
				if (!local) {
					load(in);
				} else {
					loadlocal(in);
				}
			} else {
				if (isCreate) {
					iniFile.createNewFile();
				}
			}
		} catch (Exception e) {
			logger.error("Create INIFileAccessor from file failed:", e);
		}
	}

	/**
	 * 通过指定INI文件Reader对象来构造类的实例
	 * 
	 * @param strReader
	 */
	public INIFileAccessor(Reader strReader) {
		this();
		try {
			load(strReader);
		} catch (Throwable t) {
			logger.error("Create INIFileAccessor from reader failed:", t);
		}
	}

	/* ==========================重载入数据方法============================== */

	/**
	 * 通过inputStream,重载入数据
	 * 
	 * @param inputStream
	 * @throws Exception
	 */
	public void reload(InputStream inputStream) throws IOException {
		infoList.removeAllElements();
		currentSection = new Section();
		load(inputStream);
	}

	/**
	 * 通过reader,重载入数据
	 * 
	 * @param reader
	 * @throws Exception
	 */
	public void reload(Reader reader) throws IOException {
		infoList.removeAllElements();
		currentSection = new Section();
		load(reader);
	}

	/**
	 * 通过file,重载入数据
	 * 
	 * @param file
	 */
	public void reload(File file) {
		reload(file, false);
	}

	/**
	 * 通过file,重新加载数据,如果文件不存在,根据isCreate属性决定是否创建
	 * 
	 * @param file
	 * @param isCreate
	 */
	public void reload(File file, boolean isCreate) {
		reload(file, isCreate, false);
	}

	/**
	 * 通过file,重新加载数据,如果文件不存在,根据isCreate属性决定是否创建,根据local属性决定是否采用本地编码
	 * 
	 * @param file
	 * @param isCreate
	 * @param local
	 */
	public void reload(File file, boolean isCreate, boolean local) {
		iniFilePath = file.getAbsolutePath();
		infoList.removeAllElements();
		currentSection = new Section();
		try {
			if (file.exists()) {
				if (!local) {
					load(new FileInputStream(file));
				} else {
					loadlocal(new FileInputStream(file));
				}
			} else {
				if (isCreate) {
					file.createNewFile();
				}
			}
		} catch (Exception e) {
			logger.error("Create INIFileAccessor from file failed:", e);
		}
	}

	/* ==========================载入数据方法============================== */

	/**
	 * 通过inStream,载入数据，默认采用utf-8编码
	 * 
	 * @param inStream
	 * @throws IOException
	 */
	protected void load(InputStream inStream) throws IOException {
		load(inStream, "utf-8");
	}

	/**
	 * 通过inStream为INIFileAccessor对象载入数据,encode决定采用的编码方式
	 * 
	 * @param inStream
	 * @param encode
	 * @throws IOException
	 */
	protected void load(InputStream inStream, String encode) throws IOException {
		BufferedReader br = StringUtil.isNull(encode) ? new BufferedReader(
				new InputStreamReader(inStream)) : new BufferedReader(
				new InputStreamReader(inStream, encode));
		load(br);
	}

	/**
	 * 通过使用本地字符集的inStream,载入数据
	 * 
	 * @param inStream
	 * @throws IOException
	 */
	protected void loadlocal(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream,
				Charset.forName(System.getProperty("file.encoding"))));
		load(br);
	}

	/**
	 * 通过reader,载入数据
	 * 
	 * @param reader
	 * @throws IOException
	 */
	public void load(Reader reader) throws IOException {
		BufferedReader br = new BufferedReader(reader);
		load(br);
	}

	/**
	 * 通过bufferedReader,载入数据
	 * 
	 * @param bufferedReader
	 * @throws IOException
	 */
	private synchronized void load(BufferedReader bufferedReader)
			throws IOException {
		String line;
		while (true) {
			line = bufferedReader.readLine();
			if (line == null) {
				break;
			}
			// 以#开头的为注释内容
			if (line.length() > 0 && line.charAt(0) != '#') {
				if (isSection(line)) {
					// 如果读入的第一句不为Section名称,将抛出NullPointException
					String name = trim ? line.trim() : line;
					String sectionName = getSectionName(name);
					currentSection = new Section(sectionName);
					infoList.addElement(currentSection);
				} else {
					String key = getKey(line);
					String value = getValue(line);
					currentSection.put(key, value);
				}
			}
		}
		bufferedReader.close();
	}

	/* ==========================获取节点值 方法============================== */

	/**
	 * 获取指定section下指定key的字符串值,如果字串不存在返回null
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public String get(String section, String key) {
		String value = null;
		Section tempSection = getSectionByName(section);
		if (tempSection != null) {
			value = (String) tempSection.get(key);
		}
		return value;
	}

	/**
	 * 获取指定section下指定key的字符串值,如果字串不存在返回缺省字串, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public String get(String section, String key, String defaultVal) {
		String value = get(section, key);
		return (value == null) ? defaultVal : value;
	}

	/**
	 * 获取指定section下指定key的整形数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public int getInt(String section, String key, int defaultVal) {
		String value = get(section, key);
		if (value == null) {
			return defaultVal;
		} else if (value.equalsIgnoreCase("MIN_VALUE")) {
			return Integer.MIN_VALUE;
		} else if (value.equalsIgnoreCase("MAX_VALUE")) {
			return Integer.MAX_VALUE;
		}
		return Integer.parseInt(value);
	}

	/**
	 * 获取指定section下指定key的浮点数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public float getFloat(String section, String key, float defaultVal) {
		String value = get(section, key);
		if (value == null) {
			return defaultVal;
		} else if (value.equalsIgnoreCase("MIN_VALUE")) {
			return DataUtil.FLOAT_MIN_VALUE;
		} else if (value.equalsIgnoreCase("MAX_VALUE")) {
			return Float.MAX_VALUE;
		}
		return Float.parseFloat(value);
	}

	/**
	 * 获取指定section下指定key的浮点数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(String section, String key, double defaultVal) {
		String value = get(section, key);
		if (value == null) {
			return defaultVal;
		} else if (value.equalsIgnoreCase("MIN_VALUE")) {
			return DataUtil.DOUBLE_MIN_VALUE;
		} else if (value.equalsIgnoreCase("MAX_VALUE")) {
			return Double.MAX_VALUE;
		}
		return Double.parseDouble(value);
	}

	/**
	 * 获取指定section下指定key的布尔值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public boolean getBoolean(String section, String key, boolean defaultVal) {
		String value = get(section, key);
		if (value == null) {
			return defaultVal;
		} else {
			return ("1".equals(value) || "true".equalsIgnoreCase(value));
		}
	}

	/* ==========================添加节点值 方法============================== */

	public int add(String section, String key, int value, boolean overWrite) {
		return add(section, key, String.valueOf(value), overWrite);
	}

	public int add(String section, String key, float value, boolean overWrite) {
		return add(section, key, String.valueOf(value), overWrite);
	}

	public int add(String section, String key, double value, boolean overWrite) {
		return add(section, key, String.valueOf(value), overWrite);
	}

	public int add(String section, String key, boolean value, boolean overWrite) {
		return add(section, key, String.valueOf(value), overWrite);
	}

	/**
	 * 如果指定的Section不存在,添加这个Section,返回0; 如果指定的Section存在,Key不存在,添加新的Key-value,返回1;
	 * 如果key已经存在,根据overWrite属性,若为false,则不添加,返回-1,若为true,则覆盖原值,返回2
	 * 
	 * @param section
	 * @param key
	 * @param value
	 * @param overWrite
	 * @return
	 */
	public int add(String section, String key, String value, boolean overWrite) {
		int nRet;
		Section tempSection = getSectionByName(section);
		if (tempSection == null) {
			currentSection = new Section(section);
			currentSection.put(key, value);
			infoList.addElement(currentSection);
			nRet = 0;
		} else {
			currentSection = tempSection;
			if (currentSection.containsKey(key)) {
				if (overWrite) {
					currentSection.put(key, value);
					nRet = 2;
				} else {
					nRet = -1;
				}
			} else {
				currentSection.put(key, value);
				nRet = 1;
			}
		}
		return nRet;
	}

	/**
	 * 如果指定的Section不存在,添加这个Section,返回true;如果指定的Section存在,返回false
	 * 
	 * @param section
	 * @return
	 */
	public boolean addSection(String section) {
		boolean bRet;
		Section tempSection = getSectionByName(section);
		if (tempSection == null) {
			currentSection = new Section(section);
			infoList.addElement(currentSection);
			bRet = true;
		} else {
			currentSection = tempSection;
			bRet = false;
		}
		return bRet;
	}

	/* ==========================删除节点值 方法============================== */

	/**
	 * 如果指定的Section不存在,返回-1; 如果指定的Section存在,Key不存在,返回0;
	 * 如果指定的Section存在,key存在,执行remove的功能,返回1
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public int remove(String section, String key) {
		int nRet;
		Section tmp = getSectionByName(section);
		if (tmp == null) {
			return -1;
		}
		currentSection = tmp;
		if (tmp.containsKey(key)) {
			tmp.remove(key);
			nRet = 1;
		} else
			nRet = 0;
		return nRet;
	}

	/**
	 * 删除指定的Section
	 * 
	 * @param section
	 * @return
	 */
	public boolean delSection(String section) {
		boolean bRet = true;
		Section tempSection = getSectionByName(section);
		if (tempSection != null) {
			bRet = infoList.removeElement(tempSection);
		} else {
			bRet = false;
		}
		return bRet;
	}

	/**
	 * 删除所有的Section
	 */
	public void delAllSection() {
		infoList.removeAllElements();
		currentSection = null;
	}

	/* ==========================保存节点值方法 ============================== */

	/**
	 * 把修改之后的数据存入到默认的文件当中
	 * 
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save() throws IOException {
		save(false);
	}

	/**
	 * 把修改之后的数据存入到默认的文件当中
	 * 
	 * @param local
	 *            是否采用本地编码，默认为utf-8
	 * @throws IOException
	 */
	public void save(boolean local) throws IOException {
		if (iniFilePath == null) {
			throw new IOException("Default Saved file is NULL");
		}
		save(iniFilePath, local);
	}

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param file
	 *            保存的文件
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save(File file) throws IOException {
		save(file.getAbsolutePath());
	}

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param filepath
	 *            保存的文件路径
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save(String filepath) throws IOException {
		save(filepath, false);
	}

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param filepath
	 *            保存的文件路径
	 * @param local
	 *            是否采用本地编码，默认为utf-8
	 * @throws IOException
	 *             如果把数据保存到文件失败,抛出IOException
	 */
	public void save(String filepath, boolean local) throws IOException {
		BufferedWriter bw = null;
		try {
			if (local) {
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(filepath), Charset.forName(System
								.getProperty("file.encoding"))));
			} else {
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(filepath), "utf-8"));
			}
			int count = infoList.size();
			for (int i = 0; i < count; i++) {
				writeSection(infoList.elementAt(i), bw);
			}
		} finally {
			bw.close();
		}
	}

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param filepath
	 *            保存的文件路径
	 * @param encode
	 *            编码
	 * @throws IOException
	 *             如果把数据保存到文件失败,抛出IOException
	 */
	public void save(String filepath, String encode) throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filepath), encode));
			int count = infoList.size();
			for (int i = 0; i < count; i++) {
				writeSection(infoList.elementAt(i), bw);
			}
		} finally {
			bw.close();
		}
	}

	/* ==========================其他方法 ============================== */

	/**
	 * 枚举section
	 */
	public Enumeration<Section> EnumSections() {
		return infoList.elements();
	}

	/**
	 * 获取INI数据结构段列表
	 * 
	 * @return
	 */
	public Vector<Section> getInfoList() {
		return infoList;
	}

	/**
	 * 这个方法用来打印所有的ini数据, 主要用于测试程序正确性
	 * 
	 * @return
	 */
	public String dumpObject() {
		StringBuffer buffer = new StringBuffer();
		int count = infoList.size();
		for (int i = 0; i < count; i++) {
			Section map = infoList.elementAt(i);
			buffer.append('[').append(map.getName()).append(']');
			buffer.append('\n');
			Enumeration<?> enumer = map.keys();
			while (enumer.hasMoreElements()) {
				Object obj = enumer.nextElement();
				buffer.append(obj).append('=').append(map.get(obj));
				buffer.append('\n');
			}
		}
		return buffer.toString();
	}

	/**
	 * 将一个节点写到指定文件
	 * 
	 * @param sec
	 * @param out
	 * @throws IOException
	 */
	private void writeSection(Section sec, Writer out) throws IOException {
		StringBuffer buf = new StringBuffer();
		buf.append('[').append(sec.getName()).append(']').append('\n');
		Enumeration<?> iterator = sec.keys();
		while (iterator.hasMoreElements()) {
			Object objKey = iterator.nextElement();
			Object objVal = sec.get(objKey);
			buf.append(objKey);
			if (objVal != null) {
				buf.append('=').append(objVal);
			}
			buf.append('\n');
		}
		out.write(buf.toString());
	}

	/**
	 * 判断是否为Section名称串
	 * 
	 * @param line
	 * @return
	 */
	private boolean isSection(String line) {
		boolean bRet = false;
		if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
			return bRet = true;
		}
		return bRet;
	}

	/**
	 * 获取Key值
	 * 
	 * @param line
	 * @return
	 */
	private String getKey(String line) {
		if (line != null && line.length() > 0) {
			String key = null;
			int index = line.indexOf('=');
			if (index == -1) {
				key = line;
			} else {
				key = line.substring(0, index);
			}
			return trim ? key.trim() : key;
		} else {
			return null;
		}
	}

	/**
	 * 获取Value值
	 * 
	 * @param line
	 * @return
	 */
	private String getValue(String line) {
		if (line != null && line.length() > 0) {
			int index = line.indexOf('=');
			String value = null;
			if (index == -1 || (index + 1 == line.length())) {
				value = "";
			} else {
				value = trim ? line.substring(index + 1).trim() : line
						.substring(index + 1);
			}
			return value;
		} else {
			return null;
		}
	}

	/**
	 * 获取Section名称
	 * 
	 * @param line
	 * @return
	 */
	private String getSectionName(String line) {
		String secName = line.substring(1, line.length() - 1);
		return trim ? secName.trim() : secName;
	}

	/**
	 * 根据Section的名字, 获取Key-Value所在的Hashtable
	 * 
	 * @param name
	 * @return
	 */
	public Hashtable<String, String> getSection(String name) {
		return getSectionByName(name);
	}

	/**
	 * 根据section名称,找出对应的Section对象,找不到返回null
	 * 
	 * @param section
	 * @return
	 */
	private Section getSectionByName(String section) {
		if (currentSection != null) {
			if (section.equals(currentSection.getName())) {
				return currentSection;
			}
		}
		int count = infoList.size();
		Section tempSection = null;
		for (int i = 0; i < count; i++) {
			Section temp = infoList.elementAt(i);
			if (section.equals(temp.getName())) {
				tempSection = temp;
				break;
			}
		}
		return tempSection;
	}

	/**
	 * 返回类的基本信息
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("INIFileAccessor from file: ").append(iniFilePath);
		return buf.toString();
	}

	/**
	 * 这是一个内部类,它是Hashtable的扩展,用来存储一个Section中的所有信息
	 * 它比一个Hashtable仅仅多了一个sectionName, 这个String用来标记Section的名称
	 * 该类没有重写判断类对象是否相等的方法，导致部分业务逻辑出现错误，该缺陷于2015-12-03在完善鹊桥用户需求时发现，因而得到修正。
	 * 
	 * @author liao.lh
	 * 
	 */
	public static class Section extends Hashtable<String, String> {

		private static final long serialVersionUID = 6202580781948917878L;

		String sectionName;

		public Section() {
			this("");
		}

		public Section(String name) {
			super();
			sectionName = name;
		}

		public String getName() {
			return sectionName;
		}

		public void setName(String name) {
			sectionName = name;
		}

		@Override
		public String toString() {
			return sectionName;
		}

		@Override
		public int hashCode() {
			return sectionName.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Section other = (Section) obj;
			if (sectionName == null) {
				if (other.getName() != null)
					return false;
			} else if (!sectionName.equals(other.getName()))
				return false;
			return true;
		}
	}

	/*************************************** 主方法用于测试 *************************************************/
	public static void main(String[] args) {
		INIFileAccessor iniFile = new INIFileAccessor(new File("d:/system.ini"));
		System.out.println(iniFile.dumpObject());
	}
}
