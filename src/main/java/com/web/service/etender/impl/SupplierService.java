package com.web.service.etender.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.ArrayUtil;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.utils.office.ExcelUtil;
import com.web.common.ConstantData;
import com.web.model.etender.Supplier;
import com.web.model.etender.User;
import com.web.service.etender.ISupplierService;
import com.web.utils.ServletContextUtil;
import com.web.utils.WebUtil;

@Service("supplierService")
public class SupplierService extends BaseService implements ISupplierService {

	/**
	 * 保存记录
	 * 
	 * @param userid
	 * @param jo
	 * @return
	 */
	private synchronized boolean saveRecord(String userid, JSONObject jo) {
		String recordPath = WebUtil.getTempPath() + "/inquire/" + userid
				+ "/filter.json";
		return FileUtil.newFile(recordPath, jo.toJSONString());
	}

	/**
	 * 获取记录
	 * 
	 * @param userid
	 * @return
	 */
	private synchronized JSONObject getRecord(String userid) {
		String recordPath = WebUtil.getTempPath() + "/inquire/" + userid
				+ "/filter.json";
		String json = FileUtil.readContent(recordPath, true);
		if (StringUtil.isNull(json)) {
			return new JSONObject();
		} else {
			return JSON.parseObject(json);
		}
	}

	/**
	 * 保存过滤条件
	 * 
	 * @param userid
	 * @param ks
	 * @param key
	 */
	private boolean saveFilter(String userid, List<String> ks, String key) {
		JSONObject jo = getRecord(userid);
		JSONObject filter = jo.getJSONObject(key);
		if (filter == null) {
			filter = new JSONObject();
			jo.put(key, filter);
		}
		for (String k : ks) {
			filter.put(k, "");
		}
		return saveRecord(userid, jo);
	}

	@Override
	public Set<String> loadFilter(String userid, String key) {
		JSONObject jo = getRecord(userid);
		JSONObject filter = jo.getJSONObject(key);
		if (filter != null) {
			return filter.keySet();
		} else {
			return null;
		}
	}

	@Override
	public List<Supplier> loadSupplier(String userid, String trade, String level) {
		List<Supplier> suppliers = supplierMapper.loadSupplier(userid);
		int size = suppliers.size();
		if (size > 0) {
			if (!StringUtil.isNull(trade)) {
				List<String> tl = StringUtil.getListByDescription(
						trade.toLowerCase(), ",");
				for (int i = size - 1; i >= 0; i--) {
					// 分包商的专业
					String t = suppliers.get(i).getTrade();
					if (!StringUtil.isNull(t)) {
						// 不为空进行判断
						List<String> stl = StringUtil.getListByDescription(
								t.toLowerCase(), ",");
						if (ArrayUtil.getIntersection(tl, stl).size() != tl
								.size()) {
							// 不完全包含所选专业，就移除
							suppliers.remove(i);
						}
					} else {
						// 为空，直接去除
						suppliers.remove(i);
					}
				}
				// TODO 保存失败怎么办，这里未来需要考虑一下
				saveFilter(userid, tl, "trade");
			}
		}
		size = suppliers.size();
		if (size > 0) {
			if (!StringUtil.isNull(level)) {
				List<String> ll = StringUtil.getListByDescription(
						level.toLowerCase(), ",");
				for (int i = size - 1; i >= 0; i--) {
					// 分包商的资质
					String l = suppliers.get(i).getLevel();
					if (!StringUtil.isNull(l)) {
						// 不为空进行判断
						List<String> sll = StringUtil.getListByDescription(
								l.toLowerCase(), ",");
						if (ArrayUtil.getIntersection(ll, sll).size() != ll
								.size()) {
							// 不完全包含所选资质，就移除
							suppliers.remove(i);
						}
					} else {
						// 为空，直接去除
						suppliers.remove(i);
					}
				}
				// TODO 保存失败怎么办，这里未来需要考虑一下
				saveFilter(userid, ll, "level");
			}
		}
		return suppliers;
	}

	@Override
	public Supplier loadSupplier(int supplierid) {
		return supplierMapper.selectByPrimaryKey(supplierid);
	}

	@Override
	public int addSupplier(Supplier supplier) {
		String name = supplier.getName();
		String email = supplier.getEmail();
		String userid = supplier.getUserid();
		List<String> names = supplierMapper.selectNameByUserID(userid, -1);
		for (String n : names) {
			if (name.equalsIgnoreCase(n)) {
				return ConstantData.NameEXISTED;
			}
		}
		List<String> emails = supplierMapper.selectEmailByUserID(userid, -1);
		for (String e : emails) {
			if (email.equalsIgnoreCase(e)) {
				return ConstantData.EmailEXISTED;
			}
		}
		int supplierid = ConstantData.ERROR;
		if (supplierMapper.insertSelective(supplier) == 1) {
			supplierid = supplier.getSupplierid();
		}
		return supplierid;
	}

	@Override
	public int deleteSupplierByLogic(String userid, Integer supplierID)
			throws Exception {
		int ret = ConstantData.OK;
		if (supplierMapper.deleteSupplierByLogic(supplierID) != 1) {
			ret = ConstantData.FAILED;
		}
		return ret;
	}

	@Override
	public int batchDeleteSupplierByLogic(String userid, List<String> ids) {
		int ret = ConstantData.OK;
		if (!supplierMapper.batchDeleteSupplierByLogic(ids)) {
			ret = ConstantData.FAILED;
		}
		return ret;
	}

	@Override
	public int editSupplier(String currentUserid, Supplier supplier) {
		String name = supplier.getName();
		String email = supplier.getEmail();
		String userid = supplier.getUserid();
		List<String> names = supplierMapper.selectNameByUserID(userid,
				supplier.getSupplierid());
		for (String n : names) {
			if (name.equalsIgnoreCase(n)) {
				return ConstantData.NameEXISTED;
			}
		}
		List<String> emails = supplierMapper.selectEmailByUserID(userid,
				supplier.getSupplierid());
		for (String e : emails) {
			if (email.equalsIgnoreCase(e)) {
				return ConstantData.EmailEXISTED;
			}
		}
		supplierMapper.updateByPrimaryKeySelective(supplier);
		return supplier.getSupplierid();
	}

	@Override
	public String importSupplier(String attachPath, User user) {
		String filePath = ServletContextUtil.getWebRootPath() + attachPath;
		Workbook workbook = null;
		try {
			workbook = ExcelUtil.getWorkbook(filePath);
			Sheet sheet = workbook.getSheetAt(0);
			boolean valid = true;
			int firstRowIndex = sheet.getFirstRowNum();
			Row headRow = sheet.getRow(firstRowIndex);// 当前行
			for (int i = 0; i < 7; i++) {
				if (!valid) {
					break;
				}
				Cell currentCell = headRow.getCell(i);// 当前单元格
				String currentCellValue = ExcelUtil.getCellValue(currentCell,
						true).trim();// 当前单元格的值
				switch (i) {
				case 0:
					valid = currentCellValue.equalsIgnoreCase("S/N");
					break;
				case 1:
					valid = currentCellValue.equalsIgnoreCase("Sub-Con Name");
					break;
				case 2:
					valid = currentCellValue.equalsIgnoreCase("Email");
					break;
				case 3:
					valid = currentCellValue.equalsIgnoreCase("Trade");
					break;
				case 4:
					valid = currentCellValue.equalsIgnoreCase("Level");
					break;
				case 5:
					valid = currentCellValue.equalsIgnoreCase("Contacts");
					break;
				case 6:
					valid = currentCellValue.equalsIgnoreCase("Phone");
					break;
				case 7:
					valid = currentCellValue.equalsIgnoreCase("Address");
					break;
				default:
					break;
				}
			}
			if (!valid) {
				return "The excel format is invalid.";
			}
			int lastRowIndex = sheet.getLastRowNum();
			for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
				Row currentRow = sheet.getRow(rowIndex);// 当前行
				if ( currentRow == null )
					continue;
				String name = ExcelUtil.getCellValue(currentRow.getCell(1),
						true);
				if (StringUtil.isNull(name)) {
					continue;
				} else if (StringUtil.getLength(name) > 255) {
					continue;
				} else if (StringUtil.isContainIllegalChars(name,
						ConstantData.InvalidChars)) {
					continue;
				}
				String email = ExcelUtil.getCellValue(currentRow.getCell(2),
						true);
				if (StringUtil.isNull(email)) {
					continue;
				} else if (!StringUtil.isEmail(email)) {
					continue;
				}
				String trade = ExcelUtil.getCellValue(currentRow.getCell(3),
						true);
				String level = ExcelUtil.getCellValue(currentRow.getCell(4),
						true);
				if (!StringUtil.isNull(level)
						&& StringUtil.getLength(level) > 10) {
					continue;
				}
				String contacts = ExcelUtil.getCellValue(currentRow.getCell(5),
						true);
				if (!StringUtil.isNull(contacts)
						&& StringUtil.getLength(contacts) > 50) {
					continue;
				}
				String telephone = ExcelUtil.getCellValue(
						currentRow.getCell(6), true);
				if (!StringUtil.isNull(telephone)
						&& StringUtil.getLength(telephone) > 30) {
					continue;
				}
				String address = ExcelUtil.getCellValue(currentRow.getCell(7),
						true);
				if (!StringUtil.isNull(address)
						&& StringUtil.getLength(address) > 255) {
					continue;
				}
				Supplier supplier = new Supplier();
				supplier.setName(name);
				supplier.setEmail(email);
				supplier.setTrade(trade);
				supplier.setLevel(level);
				supplier.setContacts(contacts);
				supplier.setTelephone(telephone);
				supplier.setAddress(address);
				supplier.setUserid(user.getUserid());
				supplier.setCreateby(user.getNickname());
				// TODO 部分用户添加失败的处理，这里还未考虑
				addSupplier(supplier);
			}
		} catch (Exception e) {
			logger.error("ImportSupplier:", e);
			return "Import supplier has error.";
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("Close:", e);
				}
			}
		}
		return ConstantData.Successed;
	}

}
