package com.web.service.etender.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.LoginUtil;
import com.thirdparty.glodon.yun.RegisterUtil;
import com.thirdparty.glodon.yun.UserUtil;
import com.thirdparty.glodon.yun.model.UserDomain;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.model.etender.User;
import com.web.service.etender.IUserService;
import com.web.utils.SqlUtil;
import com.web.utils.arithmetic.DES;

@Service("userService")
public class UserService extends BaseService implements IUserService {

	@Override
	public String verifyTBQUser(String email, String password,
			String access_token) {
		if (!StringUtil.isNull(access_token)) {
			// access_token获取login_token
			String login_token = LoginUtil.getLoginToken(access_token);
			if (!StringUtil.isNull(login_token)) {
				String userInfo = UserUtil.getUserInfo(access_token);
				if (!StringUtil.isNull(userInfo)) {
					JSONObject jo = JSON.parseObject(userInfo);
					String userid = jo.getString("id");
					User user = userMapper.selectByPrimaryKey(userid);
					if (user != null) {
						user.setEmail(email);
						DES des = new DES();
						user.setPassword(des.strEnc(password,
								ConstantData.FirstKey, ConstantData.SecondKey,
								ConstantData.ThirdKey));
						if (StringUtil.isNull(user.getNickname())) {
							user.setNickname(jo.getString("displayName"));
						}
						if (StringUtil.isNull(user.getTelephone())) {
							user.setTelephone(jo.getString("mobile"));
						}
						userMapper.updateByPrimaryKeySelective(user);
						return userid;
					} else {
						user = new User();
						user.setUserid(userid);
						user.setEmail(email);
						DES des = new DES();
						user.setPassword(des.strEnc(password,
								ConstantData.FirstKey, ConstantData.SecondKey,
								ConstantData.ThirdKey));
						user.setNickname(jo.getString("displayName"));
						user.setTelephone(jo.getString("mobile"));
						if (userMapper.insertSelective(user) == 1) {
							return userid;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public User loadByEmail4Glodon(String email) {
		return userMapper.loadByEmail4Glodon(email);
	}

	/**
	 * 广联云验证与第三方社交平台验证不一样的地方在于，第三方平台是通过用户id来标识对象，而用户id需要通过第三方平台授权验证才能获得，
	 * 而广联云可以通过用户邮箱为标识，因此可以先判断用户是否冻结，再让广联云进行授权验证，这样减少冻结用户与广联云授权验证的交互，提高速度
	 */
	@Override
	public int grant(final User user) throws Exception {
		final String email = user.getEmail();
		final String password = user.getPassword();
		final DES des = new DES();
		User u = loadByEmail4Glodon(SqlUtil.likeEscapeS(email, false));
		if (u != null) {
			copyUser(user, u);
			if (password.equals(des.strDec(u.getPassword(),
					ConstantData.FirstKey, ConstantData.SecondKey,
					ConstantData.ThirdKey))) {
				// 用户密码用加密方式保存在session中
				user.setPassword(des.strEnc(password, ConstantData.FirstKey,
						ConstantData.SecondKey, ConstantData.ThirdKey));
				// 本机验证成功
				return ConstantData.OK;
			}
		}
		// 以下开始执行广联云的验证，由于广联云服务器验证有时候会出错，影响用户体验，这种验证方式只用在网站本身验证失败的情况下再执行
		// 用户邮箱和密码获取access_token
		// 重复尝试指定次数，都获取为空，再返回错误
		String access_token_json = LoginUtil.grant(email, password);
		logger.info("Glodon response access token(" + email + "):"
				+ access_token_json);
		int count = 1;
		while (StringUtil.isNull(access_token_json)) {
			if (count >= ConstantData.RETRYNUM) {
				break;
			}
			Thread.sleep(1000);
			access_token_json = LoginUtil.grant(email, password);
			logger.info("Glodon response access token(" + email + "):"
					+ access_token_json);
			count++;
		}
		if (StringUtil.isNull(access_token_json)) {
			// 广联云服务器响应出现错误
			return ConstantData.ERROR;
		}

		JSONObject atj = JSON.parseObject(access_token_json);
		final String access_token = atj.getString("access_token");
		if (!StringUtil.isNull(access_token)) {
			// access_token获取login_token
			String login_token = LoginUtil.getLoginToken(access_token);
			if (!StringUtil.isNull(login_token)) {
				// TODO
				// 这里代表用户已经成功通过广联云的登录验证，利用广联云里面的用户信息完善系统中的用户信息
				String userInfo = UserUtil.getUserInfo(access_token);
				if (!StringUtil.isNull(userInfo)) {
					JSONObject jo = JSON.parseObject(userInfo);
					String userid = jo.getString("id");
					user.setUserid(userid);
					if (StringUtil.isNull(user.getNickname())
							|| user.getNickname().equalsIgnoreCase(email)) {
						// 用户昵称为空，那么以广联云用户昵称为标准
						String nickName = jo.getString("displayName");
						if (StringUtil.isNull(nickName)) {
							user.setNickname(email);
						} else {
							user.setNickname(nickName);
						}
					}
					if (StringUtil.isNull(user.getTelephone())) {
						user.setTelephone(jo.getString("mobile"));
					}
					user.setPassword(des.strEnc(password,
							ConstantData.FirstKey, ConstantData.SecondKey,
							ConstantData.ThirdKey));
					saveUser(user);
				}
				return ConstantData.OK;
			}
		} else {
			String error = atj.getString("error");
			if (error.equalsIgnoreCase("invalid_grant")) {
				return ConstantData.NAME_NOT_VERIFIED;
			} else if (error.equalsIgnoreCase("unauthorized")) {
				String error_description = atj.getString("error_description");
				if (error_description
						.equalsIgnoreCase("username not found at remote")) {
					// 代表邮箱在广联云不存在
					return ConstantData.EMAIL_INVALID;
				}
				return ConstantData.PASSWORD_INVALID;
			}
		}
		return ConstantData.LOGIN_OTHER;
	}

	/**
	 * 将一个用户信息复制到另一个用户对象中
	 * 
	 * @param to
	 * @param from
	 */
	private void copyUser(User to, User from) {
		to.setUserid(from.getUserid());
		to.setNickname(from.getNickname());
		to.setName(from.getName());
		to.setCompany(from.getCompany());
		to.setAddress(from.getAddress());
		to.setTelephone(from.getTelephone());
		to.setWebsite(from.getWebsite());
		to.setContactinfo(from.getContactinfo());
		to.setCreatetime(from.getCreatetime());
	}

	@Override
	public int register(String email, String password, String name,
			String phone, String company, String address, String website,
			String captcha, String captchaKey, String returnTo) {
		int r = validEmail(email);
		logger.info("validEmail return={}",r);
		if (r == ConstantData.OK) {
			UserDomain userDomain = new UserDomain();
			userDomain.setIdentity(email);
			userDomain.setPassword(password);
			userDomain.setCaptchaKey(captchaKey);
			userDomain.setCaptcha(captcha);
			userDomain.setReturnTo(returnTo);
			String res = RegisterUtil.create(userDomain);
			logger.info("res={}", res);
			JSONObject jo = JSON.parseObject(res);
			String result = jo.getString("ret");
			if (result.equals(RegisterUtil.RESULT_201)) {
				// 注册成功
				// 用户信息保存进数据库
				User user = new User();
				user.setUserid(jo.getString("id"));
				user.setNickname(name);
				user.setEmail(email);
				user.setName(name);
				user.setTelephone(phone);
				user.setCompany(company);
				user.setAddress(address);
				user.setWebsite(website);
				// 这里不判断是否保存成功可能存在用户信息丢失风险，不过即使用户信息丢失，到时可以在完善个人信息中再次完善
				saveUser(user);
				return ConstantData.OK;
			} else if (result.equals(RegisterUtil.RESULT_401)
					|| result.equals(RegisterUtil.RESULT_406)) {
				return ConstantData.REGISTER_FAILED;
			} else {
				return ConstantData.REGISTER_OTHER;
			}
		} else {
			// 邮箱无效
			return r;
		}
	}

	@Override
	public int supplyPersonalInfo(String userid, String name, String phone,
			String company, String address, String website) {
		User user = new User();
		user.setUserid(userid);
		user.setName(name);
		user.setTelephone(phone);
		user.setCompany(company);
		user.setAddress(address);
		user.setWebsite(website);
		userMapper.updateByPrimaryKeySelective(user);
		return ConstantData.OK;
	}

	@Override
	public int modifyPersonalInfo(User user, String origPsw, String newPsw,
			String name, String phone, String company, String address,
			String website) {
		User newUser = new User();
		if (!StringUtil.isNull(origPsw) && !StringUtil.isNull(newPsw)) {
			// 密码不为空，意味着需要修改密码，先执行第一层网站自身对原密码的验证
			if (!user.getPassword().equals(origPsw)) {
				return ConstantData.PASSWORD_INVALID;
			}
			newUser.setPassword(newPsw);
		}
		newUser.setUserid(user.getUserid());
		newUser.setName(name);
		newUser.setTelephone(phone);
		newUser.setCompany(company);
		newUser.setAddress(address);
		newUser.setWebsite(website);
		userMapper.updateByPrimaryKeySelective(newUser);
		if (!StringUtil.isNull(origPsw) && !StringUtil.isNull(newPsw)) {
			// 同步修改广联云密码
			DES des = new DES();
			String oldPsw = des.strDec(origPsw, ConstantData.FirstKey,
					ConstantData.SecondKey, ConstantData.ThirdKey);
			String access_token = "";
			String access_token_json = LoginUtil.getAccessTokenInfo(
					user.getEmail(), oldPsw);
			if (!StringUtil.isNull(access_token_json)) {
				logger.info("access_token_json={}", access_token_json);
				JSONObject atj = JSON.parseObject(access_token_json);
				access_token = atj.getString("access_token");
			}
			if (StringUtil.isNull(access_token)) {
				logger.info("ModifyPersonalInfo:", "Can not get access token.");
				throw new RuntimeException("Modify password failed!");
			} else {
				newPsw = des.strDec(newPsw, ConstantData.FirstKey,
						ConstantData.SecondKey, ConstantData.ThirdKey);
				String res = LoginUtil.modifyPsw(oldPsw, newPsw, access_token);
				if (!StringUtil.isNull(res)) {
					JSONObject jo = JSON.parseObject(res);
					if (!jo.getString("code").equalsIgnoreCase(
							RegisterUtil.RESULT_200)) {
						throw new RuntimeException("Modify password failed!");
					}
				}
			}
		}
		copyUser(user, newUser);
		// 更新系统的
		return ConstantData.OK;
	}

}
