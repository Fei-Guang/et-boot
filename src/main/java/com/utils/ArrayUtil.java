package com.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 操作数组的一些通用方法
 * 
 * @author liaolinghao
 * 
 */
public class ArrayUtil {

	private static Logger logger = LoggerFactory.getLogger(ArrayUtil.class);

	/**
	 * 获取两个字符串数组的交集
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static List<String> getIntersection(String[] array1, String[] array2) {
		List<String> list1 = Arrays.asList(array1);
		logger.debug(list1.toString());
		List<String> list2 = Arrays.asList(array2);
		logger.debug(list2.toString());
		return getIntersection(list1, list2);
	}

	/**
	 * 获取两个字符串集合的交集
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<String> getIntersection(List<String> list1,
			List<String> list2) {
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = list1.iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			if (list2.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}

	/**
	 * 判断两个整数数组是否相等
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean approxEquals(int[] a1, int[] a2) {
		if (a1 != null && a2 != null) {
			if (a1.length == a2.length) {
				for (int i = 0; i < a1.length; i++) {
					if (a1[i] != a2[i]) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return a1 == a2;
	}

	/**
	 * 判断两个数组是否近似相等，以默认公差0.00001为标准
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean approxEquals(double[] a1, double[] a2) {
		if (a1 != null && a2 != null) {
			if (a1.length == a2.length) {
				for (int i = 0; i < a1.length; i++) {
					if (!DataUtil.approxEquals(a1[i], a2[i])) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return a1 == a2;
	}

	/**
	 * 判断两个数组是否近似相等，以指定公差为标准
	 * 
	 * @param a1
	 * @param a2
	 * @param tolerance
	 * @return
	 */
	public static boolean approxEquals(double[] a1, double[] a2,
			double tolerance) {
		if (a1 != null && a2 != null) {
			if (a1.length == a2.length) {
				for (int i = 0; i < a1.length; i++) {
					if (!DataUtil.approximate(a1[i], a2[i], tolerance)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return a1 == a2;
	}

	/**
	 * 判断字符数组是否含有某一特定字符
	 * 
	 * @param chars
	 * @param escapeChar
	 * @return
	 */
	public static boolean contain(char[] chars, char escapeChar) {
		if (chars == null) {
			return false;
		}
		for (char c : chars) {
			if (c == escapeChar) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串数组是否含有某一特定字符串
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean contain(String[] strs, String str) {
		if (strs == null) {
			return false;
		}
		for (String string : strs) {
			if (string.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**************************************** 主方法用于测试 *************************************************/
	public static void main(String[] args) {
		// String[] array1 = "无,左一钉,右一钉,左两钉,右两钉".split(",");
		// String[] array2 = "无,左一钉,右一钉,左两钉,右两钉,上两钉,骑马钉".split(",");
		// System.out.println(getIntersection(array1, array2));

		double[] a1 = new double[] { 1.25, 1.24 };
		double[] a2 = new double[] { 1.24 };
		System.out.println(approxEquals(a1, a2));
	}
}
