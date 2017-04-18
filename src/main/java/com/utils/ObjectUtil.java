package com.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 该类实现了对象的一些便捷操作方法
 * 
 * @author liaolh
 * 
 */
public class ObjectUtil {

	/**
	 * 将父类所有的属性COPY到子类中。 类定义中child一定要extends father;
	 * 而且child和father一定为严格javabean写法,比如属性为deleteDate,方法为getDeleteDate
	 * 
	 * @param father
	 * @param child
	 * @throws Exception
	 */
	public static void fatherToChild(Object father, Object child)
			throws Exception {
		if (!(child.getClass().getSuperclass() == father.getClass())) {
			throw new Exception("child is not extend father!");
		}
		Class<? extends Object> fatherClass = father.getClass();
		Field ff[] = fatherClass.getDeclaredFields();
		for (int i = 0; i < ff.length; i++) {
			Field f = ff[i];// 取出每一个属性,如deleteDate
			Method m = fatherClass
					.getMethod("get" + upperHeadChar(f.getName()));// 方法getDeleteDate
			Object obj = m.invoke(father);// 取出属性值
			f.set(child, obj);
		}
	}

	/**
	 * 首字母大写，比如 in:deleteDate，out:DeleteDate
	 * 
	 * @param in
	 * @return
	 */
	private static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}
}
