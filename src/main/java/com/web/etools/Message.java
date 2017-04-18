package com.web.etools;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.Coder;
import com.web.controller.CommonController;

/**
 * 数据报文，该报文一旦创建，就不再允许修改，该报文的格式参考http协议设计，具体组成如下：
 * 
 * 2位起始位 + 报文头/正文JSON数据 + 2位结束位
 * 
 * 2位起始位: $S
 * 
 * 报文头/正文JSON数据：需加密，加密方法采用BASE64算法，由header和body/response两部分组成。
 * header信息必填；body/response数据遵循接口方法对应的业务要求 { header:{
 * datetime:"",//请求|响应时间，格式yyyy-MM-dd HH:mm:ss version:""//版本号 } body/response:{
 * 请求体格式根据实际业务来约定} }
 * 
 * 2位结束位: $E
 * 
 * @author bigbird
 *
 */
public class Message {

	public static final String VERSION = "1.0";

	public static final String startMark = "$S";
	private JSONObject jsonData;
	public static final String endMark = "$E";

	private String des;
	
	static Logger logger = LoggerFactory
			.getLogger(Message.class);

	public Message(JSONObject jsonData) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException {
		this.jsonData = jsonData;
		this.des = Coder.encodeBase64(jsonData.toJSONString());
	}

	public Message(String msg) throws Exception {
		parse(msg);
	}

	/**
	 * 按照约定的报文格式，解析消息，构造报文
	 * 
	 * @param msg
	 * @throws Exception
	 */
	private void parse(String msg) throws Exception {
		if (msg.startsWith(startMark)) {
			if (msg.endsWith(endMark)) {
				int desStart = startMark.length();
				int endMarkStart = msg.length() - endMark.length();
				des = msg.substring(desStart, endMarkStart).trim();
				des = des.replaceAll(" ", "+");
				String json = Coder.decodeBase64(des);
				jsonData = JSON.parseObject(json);
			} else {
				throw new Exception("Bad message:" + msg);
			}
		} else {
			throw new Exception("Bad message:" + msg);
		}
	}

	/**
	 * 该方法用于获取报文里面的数据，如果利用该方法给报文里面的数据进行修改，会导致报文无效
	 * 
	 * @return
	 */
	public JSONObject getJsonData() {
		return jsonData;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(startMark);
		// 0A=new line
		//sb.append(des.replaceAll("0A", ""));
		sb.append(des);
		sb.append(endMark);
		//String sResult = sb.toString();		
		return sb.toString();
	}

}
