package com.web.service.etender;

import java.util.List;
import java.util.Set;

import com.web.model.etender.Supplier;
import com.web.model.etender.User;

public interface ISupplierService {

	/**
	 * 获取供应商集合，按照专业和资质进行过滤，同时专业和资质要记录到设置信息中
	 * 
	 * @param userid
	 * @param trade
	 * @param level
	 * @return
	 */
	List<Supplier> loadSupplier(String userid, String trade, String level);

	Supplier loadSupplier(int supplierid);

	int addSupplier(Supplier supplier);

	int deleteSupplierByLogic(String userid, Integer supplierID)
			throws Exception;

	int editSupplier(String currentUserid, Supplier supplier);

	int batchDeleteSupplierByLogic(String userid, List<String> ids);

	String importSupplier(String attachPath, User user);

	Set<String> loadFilter(String userid, String key);

}
