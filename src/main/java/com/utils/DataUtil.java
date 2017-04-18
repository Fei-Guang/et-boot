package com.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.ConstantData;



/**
 * 数据操作的一些常用方法
 * 
 * @author liao.lh
 * 
 */
public class DataUtil {
	
	static Logger logger = LoggerFactory
			.getLogger(DataUtil.class);

	public static final float FLOAT_MIN_VALUE = -1 * Float.MAX_VALUE;

	public static final double DOUBLE_MIN_VALUE = -1 * Double.MAX_VALUE;

	// 默认公差
	private static final double DEFAULT_TOLERANCE = 0.00001;

	private static final DecimalFormat decimalFormat = new DecimalFormat(
			"0.00#");

	/**
	 * 判断是否约等于0，以默认公差为标准
	 * 
	 * @param d
	 * @return
	 */
	public static boolean approxZero(double d) {
		return approxEquals(d, 0);
	}

	/**
	 * 以默认公差为标准，判断两个数是否接近
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean approxEquals(double d1, double d2) {
		return approximate(d1, d2, DEFAULT_TOLERANCE);
	}

	/**
	 * 判断两个数是否近似，以指定的公差为标准
	 * 
	 * @param d1
	 * @param d2
	 * @param p
	 *            两个数之差是否小于p
	 * @return
	 */
	public static boolean approximate(double d1, double d2, double p) {
		return Math.abs(d1 - d2) < Math.abs(p);
	}

	/**
	 * 将 double 转为string，没有","分割，并且不采用科学计数法
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param p
	 *            最多保留几位小数，默认采取四舍五入的方式
	 * @return
	 */
	public static String double2String2(double d, int p) {
		NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
		// 如果不设置该参数，默认是保留3位
		
		nf.setMaximumFractionDigits(p);
		
		return nf.format(d).replaceAll(",", "");
	}
	
	
	/**
	 * 将 double 转为string，没有","分割，并且不采用科学计数法
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param p
	 *            最多保留几位小数，默认采取四舍五入的方式
	 * @return
	 */
	public static String double2String(double d, int p) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		if ( p != ConstantData.ACCURACY){
			if (bd.setScale(p,BigDecimal.ROUND_HALF_UP).doubleValue() != d)
				bd =bd.setScale(p,BigDecimal.ROUND_HALF_UP); 
		}
		if ( bd.longValue() == d ){
			logger.debug("doulbe to integer:{}",d);
			return Long.toString(bd.longValue());
		}		
		String ds = bd.toPlainString();
			
	    return ds;
	}
	
	
	/**
	 * 将 double 转为string，没有","分割，并且不采用科学计数法
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param p
	 *            最多保留几位小数，默认采取四舍五入的方式
	 * @return
	 */
	public static String double2String(String s, int p) {
		if ( s == null || s.equals("") )
			return s;
		try {
			double d = Double.parseDouble(s);					
		    return double2String(d, p);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("{} is not doulbe:{}", s, e.getMessage());
			return null;
		}
		
	}
	
	
	

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param p
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static float round(float f, int p) {
		if (p < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Float.toString(f));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, p, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param p
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double d, int p) {
		return round(d, p, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 同上，可采取不同的小数位处理方式
	 * 
	 * @param d
	 * @param p
	 * @param roundingMode
	 * @return
	 */
	public static double round(double d, int p, int roundingMode) {
		if (p < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		BigDecimal b = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, p, roundingMode).doubleValue();
	}

	/**
	 * 取值在指定的最小值与最大值之间，若超出，则取边界值
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static double truncate(double value, double min, double max) {
		return Math.min(max, Math.max(min, value));
	}

	/**
	 * 格式化数字，最多保留三位小数
	 * 
	 * @param number
	 * @return
	 */
	public static double format(double number) {
		return Double.parseDouble(decimalFormat.format(number));
	}

	/**
	 * 提供指定小数位的处理，采用截取方式，因此误差较大，该方法用在需要保证可接受最大值不超过实际最大值的情况下，对数值进行小数位处理
	 * 
	 * @param d
	 * @param p
	 * @return
	 */
	public static double cutMax(double d, int p) {
		if (d == Double.MAX_VALUE) {
			return d;
		} else if (d == Double.MIN_VALUE) {
			return 0;
		} else {
			String text = String.valueOf(d);
			int index = text.indexOf(".");
			if (index < 0) {// 没有小数点
				return Double.parseDouble(text);
			} else {
				if ((index + p) < text.length() - 1) {
					if (d >= 0) {
						return Double.parseDouble(text.substring(0, index + p
								+ 1));
					} else {
						return Double.parseDouble(String.valueOf(
								d - 1 / Math.pow(10, p)).substring(0,
								index + p + 1));
					}
				} else {
					return Double.parseDouble(text);
				}
			}
		}
	}

	/**
	 * 提供指定小数位的处理，采用截取方式，因此误差较大，该方法用在需要保证可接受最大值不超过实际最大值的情况下，对数值进行小数位处理
	 * 
	 * @param f
	 * @param p
	 * @return
	 */
	public static float cutMax(float f, int p) {
		if (f == Float.MAX_VALUE) {
			return f;
		} else if (f == Float.MIN_VALUE) {
			return 0;
		} else {
			String text = String.valueOf(f);
			int index = text.indexOf(".");
			if (index < 0) {// 没有小数点
				return Float.parseFloat(text);
			} else {
				if ((index + p) < text.length() - 1) {
					if (f >= 0) {
						return Float.parseFloat(text
								.substring(0, index + p + 1));
					} else {
						return Float.parseFloat(String.valueOf(
								f
										- Float.parseFloat(String
												.valueOf(1 / Math.pow(10, p))))
								.substring(0, index + p + 1));
					}
				} else {
					return Float.parseFloat(text);
				}
			}
		}
	}

	/**
	 * 提供指定小数位的处理，采用截取方式，因此误差较大，该方法用在需要保证可接受最小值不超过实际最小值的情况下，对数值进行小数位处理
	 * 
	 * @param d
	 * @param p
	 * @return
	 */
	public static double cutMin(double d, int p) {
		if (d == DOUBLE_MIN_VALUE) {
			return d;
		} else if (d == Double.MIN_VALUE) {
			return 0;
		} else {
			String text = String.valueOf(d);
			int index = text.indexOf(".");
			if (index < 0) {// 没有小数点
				return Double.parseDouble(text);
			} else {
				if ((index + p) < text.length() - 1) {
					if (d >= 0) {
						return cutMax(Double.parseDouble(text.substring(0,
								index + p + 1))
								+ 1 / Math.pow(10, p), p);
					} else {
						return Double.parseDouble(text.substring(0, index + p
								+ 1));
					}
				} else {
					return Double.parseDouble(text);
				}
			}
		}
	}

	/**
	 * 提供指定小数位的处理，采用截取方式，因此误差较大，该方法用在需要保证可接受最小值不超过实际最小值的情况下，对数值进行小数位处理
	 * 
	 * @param f
	 * @param p
	 * @return
	 */
	public static float cutMin(float f, int p) {
		if (f == FLOAT_MIN_VALUE) {
			return f;
		} else if (f == Float.MIN_VALUE) {
			return 0;
		} else {
			String text = String.valueOf(f);
			int index = text.indexOf(".");
			if (index < 0) {// 没有小数点
				return Float.parseFloat(text);
			} else {
				if ((index + p) < text.length() - 1) {
					if (f >= 0) {
						return cutMax(Float.parseFloat(text.substring(0, index
								+ p + 1))
								+ Float.parseFloat(String.valueOf(1 / Math.pow(
										10, p))), p);
					} else {
						return Float.parseFloat(text
								.substring(0, index + p + 1));
					}
				} else {
					return Float.parseFloat(text);
				}
			}
		}
	}

	/**
	 * 生成指定位数的随机数字
	 * 
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static long getRandomData(int length) throws Exception {
		if (length > 0) {
			return Math.round((Math.random() * 9 + 1)
					* Math.pow(10, length - 1));
		} else {
			throw new Exception("The length should be larger than 0!");
		}
	}

	/**
	 * 返回指定范围内的随机数，包含两边端点
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomData(int min, int max) {
		int minValue = Math.min(min, max);
		int maxValue = Math.max(min, max);
		Random rand = new Random();
		int randNumber = rand.nextInt(maxValue - minValue + 1) + minValue;
		return randNumber;
	}

	/**
	 * 获取指定范围内指定数量的随机数
	 * 
	 * @param num
	 * @param min
	 * @param max
	 * @return
	 */
	public static ArrayList<Integer> getRandomNum(int num, int min, int max) {
		int numCount = max - min + 1;
		ArrayList<Integer> randomNum = new ArrayList<Integer>();
		// 要获取随机数的数量小于等于总数量，才执行获取操作，以防止陷入死循环
		if (num < numCount) {
			int count = 0;
			while (count < num) {
				int number = getRandomData(min, max);
				if (!randomNum.contains(number)) {
					randomNum.add(number);
					count++;
				}
			}
		} else if (num == numCount) {
			for (int i = min; i <= max; i++) {
				randomNum.add(i);
			}
		}
		return randomNum;
	}

	/**
	 * 单精度转整型
	 * 
	 * @param f
	 * @return
	 */
	public static int floatToInt(float f) {
		if (f > 0)
			return (int) (f + 0.5);
		else if (f < 0)
			return (int) (f - 0.5);
		else
			return 0;
	}

	/**
	 * 双精度转整型
	 * 
	 * @param d
	 * @return
	 */
	public static int doubleToInt(double d) {
		if (d > 0)
			return (int) (d + 0.5);
		else if (d < 0)
			return (int) (d - 0.5);
		else
			return 0;
	}

	/**
	 * 单精度转换成双精度会出现精度丢失，该方法是为了防止精度丢失
	 * 
	 * @param value
	 * @return
	 */
	public static double floatToDouble(float value) {
		return Double.parseDouble(String.valueOf(value));
	}

	/**
	 * 将百分数解析为对应的float型小数
	 * 
	 * @param percent
	 * @return
	 */
	public static float parsePercent(String percent) {
		if (percent.endsWith("%")) {
			percent = percent.substring(0, percent.lastIndexOf("%"));
			return Integer.parseInt(percent) / 100.0f;
		} else {
			return 0.0f;
		}
	}

	/**
	 * 获取布尔值
	 * 
	 * @param str
	 * @return
	 */
	public static boolean getBoolean(String str) {
		return Boolean.parseBoolean(str);

	}

	/**
	 * 获取float值
	 * 
	 * @param str
	 * @return
	 */
	public static float getFloat(String str) {
		return Float.parseFloat(str);
	}

	/**
	 * 获取double值
	 * 
	 * @param str
	 * @return
	 */
	public static double getDouble(String str) {
		return Double.parseDouble(str);

	}

	/**
	 * 获取int值
	 * 
	 * @param str
	 * @return
	 */
	public static int getInteger(String str) {
		return Integer.parseInt(str);
	}

	/**
	 * 检测该字符串是否是不良的数值格式，指该字符串能够被数值类解析为对应数值，但是格式不良好 该方法主要用于数值输入组件的数值验证上
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isBadFormat(String text) {
		if (text.equals("-0")// 不良的整数格式 -0
				|| Pattern.matches("-?0\\d+", text)// 不良的整数格式，如：01;-01
				|| text.equals("-0.")// 不良的小数格式，如：-0.
				|| text.equals("0.")// 不良的小数格式，如：0.
				|| Pattern.matches("-?\\.\\d+", text)// 不良的小数格式，如：.01;-.01
				|| Pattern.matches("-?0\\d+\\.\\d*", text)//不良的小数格式，如：01.;-01.01
				|| Pattern.matches("-?\\d+\\.", text)// 不良的小数格式，如：11.;-11.
		) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否能转换成数值
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNumber(String text) {
		try {
			Double.valueOf(text);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断一个整数是否属于质数
	 * 
	 * @param n
	 * @return
	 */
	public static boolean isPrimes(int n) {
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	/************************************* 主方法用于测试 ***************************************/
	public static void main(String[] args) throws Exception {
		// String test = "15%";
		// System.out.println(parsePercent(test));
		// String test = "TRUE";
		// System.out.println(getBoolean(test));
		// System.out.println(format(1.4126));
		// System.out.println(cutMax(-1.41829, 3));
		// System.out.println(cutMin(2.2345f, 3));
		// System.out.println(getRandomData(4));
		System.out.println(isNumber("f1.23"));
	}

}
