package com.utils.ini;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 这个接口用于定义操作INI格式数据的对象需要实现的方法
 * 
 * @author liao.lh
 */
public interface INIData {

	/* ==========================载入数据============================== */

	/**
	 * 通过InputStream,重新加载数据
	 * 
	 * @param inputStream
	 * @throws Exception
	 */
	public void reload(InputStream inputStream) throws IOException;

	/**
	 * 通过reader,重新加载数据
	 * 
	 * @param reader
	 * @throws Exception
	 */
	public void reload(Reader reader) throws IOException;

	/**
	 * 通过file,重新加载数据
	 * 
	 * @param file
	 */
	public void reload(File file);

	/* ==========================获取节点值 ============================== */

	/**
	 * 获取指定section下指定key的字符串值,如果字串不存在返回null
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public String get(String section, String key);

	/**
	 * 获取指定section下指定key的字符串值,如果字串不存在返回缺省字串, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public String get(String section, String key, String defaultVal);

	/**
	 * 获取指定section下指定key的整形数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public int getInt(String section, String key, int defaultVal);

	/**
	 * 获取指定section下指定key的浮点数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public float getFloat(String section, String key, float defaultVal);

	/**
	 * 获取指定section下指定key的浮点数值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(String section, String key, double defaultVal);

	/**
	 * 获取指定section下指定key的布尔值, 如果值不存在返回缺省值, 即defaultVal
	 * 
	 * @param section
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public boolean getBoolean(String section, String key, boolean defaultVal);

	/* ==========================添加节点值 ============================== */

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
	public int add(String section, String key, String value, boolean overWrite);

	public int add(String section, String key, int value, boolean overWrite);

	public int add(String section, String key, float value, boolean overWrite);

	public int add(String section, String key, boolean value, boolean overWrite);

	/**
	 * 如果指定的Section不存在,添加这个Section,返回true;如果指定的Section存在,返回false
	 * 
	 * @param section
	 * @return
	 */
	public boolean addSection(String section);

	/* ==========================删除节点值 ============================== */

	/**
	 * 如果指定的Section不存在,返回-1; 如果指定的Section存在,Key不存在,返回0;
	 * 如果指定的Section存在,key存在,执行remove的功能,返回1
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public int remove(String section, String key);

	/**
	 * 删除指定的Section
	 * 
	 * @param section
	 * @return
	 */
	public boolean delSection(String section);

	/**
	 * 删除所有的Section
	 */
	public void delAllSection();

	/* ==========================保存节点值 ============================== */

	/**
	 * 把修改之后的数据存入到默认的文件当中
	 * 
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save() throws IOException;

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param filepath
	 *            保存的文件路径
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save(String filepath) throws IOException;

	/**
	 * 把修改之后的数据存入到指定的文件当中
	 * 
	 * @param file
	 *            保存的文件
	 * @exception IOException
	 *                如果把数据保存到文件失败,抛出IOException
	 */
	public void save(File file) throws IOException;

	/* ==========================其他方法 ============================== */

	/**
	 * 根据Section的名字, 获取Key-Value所在的Hashtable
	 * 
	 * @param name
	 * @return
	 */
	public Hashtable<String, String> getSection(String name);

	/**
	 * 枚举section
	 */
	public Enumeration<?> EnumSections();

	/**
	 * 这个方法用来打印所有的ini数据, 主要用于测试程序正确性
	 * 
	 * @return
	 */
	public String dumpObject();

}