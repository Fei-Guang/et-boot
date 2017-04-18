package com.web.common;

/**
 * 定义系统中要利用的一些常量
 * 
 * @author liao.lh
 * 
 */
public interface ConstantData {

	public static final String HOST = "http://www.glodon.com.sg";
	public static final String DOMAIN = "http://www.glodon.com.sg/etender";

	public static final char[] InvalidChars = new char[] { '\\', '/', ':', '*',
			'?', '"', '<', '>', '|' };

	public static final int RETRYNUM = 5;

	// 精度控制
	public static final int ACCURACY = 8;

	public static final String CONST_CAS_ASSERTION = "currentUser";
	public static final String CACHE_URL = "_cache_url_";
	public static final String LOGOUT_URL = "_logout_url_";

	public static final String FirstKey = "Etender";
	public static final String SecondKey = "IBU";
	public static final String ThirdKey = "bigbird";

	public static final String CancelExport = "cancelExport"; // 取消导出
	public static final String CurrentProgress = "currentProgress"; // 当前进度
	public static final String TotalProgress = "totalProgress"; // 总进度

	public static final String Started = "Started!"; // 开始
	public static final String Canceled = "Canceled!"; // 取消
	public static final String Failed = "Failed!"; // 失败
	public static final String Successed = "Successed!"; // 成功

	public static final String Error4Login = "Login status expired, please login again.";// 登录失效的错误提示
	public static final String Error4Other = "Operation failed. Please try again later.";// 操作失败的错误提示
	public static final String Error4OverTime = "This submission is overdue, cannot submit.";// 过期失败的错误提示
	public static final String Error4OriginalPsw = "The password is incorrect.";// 密码不正确

	public static final String DefaultPassword = "etender";// 默认密码

	// 超级管理员
	public static final char SuperAdministrator = 'S';
	// 管理员
	public static final char Administrator = 'A';

	// 询价工程状态
	public static final int notInquired = 0;
	public static final int inquiring = 1;
	public static final int inquired = 2;
	public static final int evaluating = 3;
	public static final int evaluated = 4;

	// 分包工程来源
	public static final int TBQ = 0;
	public static final int EXCEL = 1;
	public static final int PDF = 2;

	// 章
	public static final int CHAPTER = 1;
	// 节
	public static final int SECTION = 2;

	// 清单类型
	// 标题
	public static final int Heading = 1;
	// 清单
	public static final int BillItem = 2;
	// 单价子项
	public static final int RateItem = 3;
	// 说明项
	public static final int Note = 4;

	// 清单报价类型
	// 正常
	public static final int Normal = 1;
	// 仅报单价
	public static final int OnlyRate = 2;
	// 固定总价
	public static final int FixAmount = 3;
	// 固定单价
	public static final int FixRate = 4;
	// 不报价
	public static final int LumpSumItem = 5;
	// 仅报总价
	public static final int OnlyAmount = 6;

	// 操作成功还是出错的公共标识
	// 成功
	public static final int OK = 0;
	// 失败
	public static final int FAILED = -9;
	// 发生错误
	public static final int ERROR = -1;
	// 名称重复
	public static final int NameEXISTED = -2;
	// 邮箱重复
	public static final int EmailEXISTED = -3;
	// 操作过期
	public static final int OverTime = -4;

	// 邮箱验证，结果在1到9之间
	// 邮箱已存在
	public static final int EXISTED_EMAIL = 1;
	// 无效邮箱
	public static final int INVALID_EMAIL = 2;
	// 账户未激活
	public static final int NAME_NOT_VERIFIED = 3;
	// 其他原因
	public static final int OTHER_EMAIL = 9;

	// 注册验证，结果在11到19之间
	// 授权失败
	public static final int REGISTER_FAILED = 11;
	// 其他原因
	public static final int REGISTER_OTHER = 19;

	// 登录验证，结果在21到29之间
	// 用户被冻结，不准登录
	public static final int LOGIN_FREEZE = 21;
	// 用户名或者密码无效
	public static final int LOGIN_INVALID = 22;
	public static final int EMAIL_INVALID = 23;
	public static final int PASSWORD_INVALID = 24;
	// 其他原因
	public static final int LOGIN_OTHER = 29;

}
