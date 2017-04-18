package com.utils.office;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 操作excel的一些通用方法
 * 
 * @author liaolh
 *
 */
public class ExcelUtil {

	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_XLSX = "xlsx";

	/***
	 * 
	 * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类) xls:HSSFWorkbook
	 * xlsx：XSSFWorkbook，该方法不适合处理大数据量的文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * 
	 */
	public static Workbook getWorkbook(String filePath) throws IOException {
		Workbook workbook = null;
		InputStream is = new FileInputStream(filePath);
		if (filePath.endsWith(EXTENSION_XLS)) {
			workbook = new HSSFWorkbook(is);
		} else if (filePath.endsWith(EXTENSION_XLSX)) {
			workbook = new XSSFWorkbook(is);
		}
		return workbook;
	}

	/**
	 * 取单元格的值
	 * 
	 * @param cell
	 *            单元格对象
	 * @param treatAsStr
	 *            为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
	 * @return
	 */
	public static String getCellValue(Cell cell, boolean treatAsStr) {
		if (cell == null) {
			return "";
		}
		if (treatAsStr) {
			// 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
			// 加上下面这句，临时把它当做文本来读取
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}

	/**
	 * 用于将Excel表格中列号字母转成列索引，从1对应A开始
	 *
	 * @param column
	 *            列号
	 * @return 列索引
	 * @throws Exception
	 */
	public static int columnToIndex(String column) throws Exception {
		if (!column.matches("[A-Z]+")) {
			throw new Exception("Invalid parameter.");
		}
		int index = 0;
		char[] chars = column.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			index += ((int) chars[i] - (int) 'A' + 1)
					* (int) Math.pow(26, chars.length - i - 1);
		}
		return index;
	}

	/**
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 *
	 * @param index
	 *            列索引
	 * @return 列号
	 * @throws Exception
	 */
	public static String indexToColumn(int index) throws Exception {
		if (index <= 0) {
			throw new Exception("Invalid parameter.");
		}
		index--;
		String column = "";
		do {
			if (column.length() > 0) {
				index--;
			}
			column = ((char) (index % 26 + (int) 'A')) + column;
			index = (int) ((index - index % 26) / 26);
		} while (index > 0);
		return column;
	}

	/***************************************** 主方法用于测试 **************************************************/
	public static void main(String[] args) throws IOException {
		Workbook workbook = null;
		try {
			workbook = ExcelUtil.getWorkbook("f:/数据.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
