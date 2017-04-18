package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期操作的一些常用方法
 * 
 * @author liaolinghao
 * 
 */
public class DateUtil {

	// hh 代表12小时制 ，HH 代表24小时制，mm 代表分钟 ，MM代表月份。
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		// 解决部分系统时间差8小时问题
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * 取得当前时间表示的字符串,该字符串用于需要时间戳的应用中
	 * 
	 * @param needTimeZone
	 *            是否需要时区标志，需要时区标志时，字符串显示形式如：&TimeStamp=2011-12-23
	 *            13:38:52&GMT=08:00
	 * @return
	 */
	public static String getTimeStampGMT(boolean needTimeZone) {
		String str = null;
		Date currentTime = new Date();
		str = sdf.format(currentTime);
		if (needTimeZone) {
			str = "&TimeStamp=" + str;
			int GMT = TimeZone.getDefault().getRawOffset();
			float gmt = GMT / 3600000;
			int g = (int) gmt;
			int m = (int) ((gmt - (int) gmt) * 100);
			if (g < 10)
				str = str + "&GMT=0" + g + ":";
			else
				str = str + "&GMT=" + g + ":";
			if (m < 10)
				str = str + "0" + m;
			else
				str = str + m;
		}
		return str;
	}

	/**
	 * 检验日期的有效性
	 * 
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @param d
	 *            日
	 * @param h
	 *            时
	 * @param s
	 *            分
	 * @return
	 */
	public static boolean validateDateTime(int y, int m, int d, int h, int s) {
		if (y > 10000 || y < 1 || m < 1 || m > 12 || d > 31 || d < 1 || h < 0
				|| h > 23 || s < 0 || s > 59) {
			return false;
		} else {
			// 检验月份和日期的关系
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				if (d > 30) {// 小月，大于30，非法
					return false;
				}
			}
			// 2月份的日期检验
			if (m == 2) {
				if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) {// 闰年
					if (d > 29) {
						return false;
					}
				} else {// 平年
					if (d > 28) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 将字符串转换成日期对象
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException ex) {
			return null;
		}
		return date;
	}

	/**
	 * 将日期转换成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String parseDateString(Date date) {
		return sdf.format(date);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	public static int getThisYear() {
		String str = null;
		Date currentTime = new Date();
		str = sdf.format(currentTime);
		return Integer.parseInt(str.substring(0, str.indexOf("-")));
	}

	/**
	 * 给时间增加指定的月份数
	 * 
	 * @param dt
	 * @param months
	 * @return
	 */
	public static Date addMonths(Date dt, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 给时间增加指定的天数
	 * 
	 * @param dt
	 * @param days
	 * @return
	 */
	public static Date addDays(Date dt, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 给时间增加指定的小时数
	 * 
	 * @param dt
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date dt, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}

	/**
	 * 给时间增加指定的分钟数
	 * 
	 * @param dt
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date dt, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	/**
	 * 给时间增加指定的秒数
	 * 
	 * @param dt
	 * @param seconds
	 * @return
	 */
	public static Date addSeconds(Date dt, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * java的Date.getTime()转换成C#的Datetime.ticks
	 * 
	 * @param epochStr
	 *            指定毫秒数
	 * @return
	 */
	public static long GetTicks(long epochStr) {
		// convert the target-epoch time to a well-format string
		String date = new java.text.SimpleDateFormat("yyyy/MM/dd/HH/mm/ss")
				.format(new Date(epochStr));
		String[] ds = date.split("/");

		// start of the ticks time,从1月1号起时间上会差两天，因此采用1月3号开始计算，据说历法的转换，导致"丢失的2天"
		Calendar calStart = Calendar.getInstance();
		calStart.set(1, 1, 3, 0, 0, 0);

		// the target time
		Calendar calEnd = Calendar.getInstance();
		calEnd.set(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]),
				Integer.parseInt(ds[2]), Integer.parseInt(ds[3]),
				Integer.parseInt(ds[4]), Integer.parseInt(ds[5]));

		// epoch time of the ticks-start time
		long epochStart = calStart.getTime().getTime();
		// epoch time of the target time
		long epochEnd = calEnd.getTime().getTime();

		// get the sum of epoch time, from the target time to the ticks-start
		// time
		long all = epochEnd - epochStart;
		// convert epoch time to ticks time
		long ticks = ((all / 1000) * 1000000) * 10;

		return ticks;
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) {
		// System.out.println(getTimeStampGMT(true));
		// System.out.println(GetTicks(System.currentTimeMillis()));
		// System.out.println(getThisYear());
		System.out.println(parseDateString(addDays(new Date(), 10)));
	}
}
