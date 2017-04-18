package com.web.service.etender;

import com.web.model.etender.User;

public interface IUserService {

	boolean saveUser(User user);

	/**
	 * 验证TBQ登录用户
	 * 
	 * @param email
	 * @param password
	 * @param access_token
	 * @return
	 */
	String verifyTBQUser(String email, String password, String access_token);

	/**
	 * 授权用户登录
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int grant(User user) throws Exception;

	/**
	 * 获取邮箱对应用户信息，广联云用户登录
	 * 
	 * @param email
	 * @return
	 */
	public User loadByEmail4Glodon(String email);

	/**
	 * 验证用户邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public int validEmail(String email);

	/**
	 * 注册新用户
	 * 
	 * @param email
	 * @param password
	 * @param name
	 * @param phone
	 * @param company
	 * @param address
	 * @param website
	 * @param captcha
	 * @param captchaKey
	 * @param returnTo
	 * @return
	 */
	int register(String email, String password, String name, String phone,
			String company, String address, String website, String captcha,
			String captchaKey, String returnTo);

	/**
	 * 完善个人信息
	 * 
	 * @param userid
	 * @param name
	 * @param phone
	 * @param company
	 * @param address
	 * @param website
	 * @return
	 */
	int supplyPersonalInfo(String userid, String name, String phone,
			String company, String address, String website);

	/**
	 * 修改个人信息
	 * 
	 * @param user
	 * @param origPsw
	 * @param newPsw
	 * @param name
	 * @param phone
	 * @param company
	 * @param address
	 * @param website
	 * @return
	 */
	int modifyPersonalInfo(User user, String origPsw, String newPsw,
			String name, String phone, String company, String address,
			String website);

}
