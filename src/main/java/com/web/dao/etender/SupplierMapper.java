package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Supplier;

public interface SupplierMapper {
	int deleteByPrimaryKey(Integer supplierid);

	int insert(Supplier record);

	int insertSelective(Supplier record);

	Supplier selectByPrimaryKey(Integer supplierid);
	
	List<Supplier> selectSubcontractByEmail(@Param("userid") String userid, @Param("email") String email);

	int updateByPrimaryKeySelective(Supplier record);

	int updateByPrimaryKey(Supplier record);

	List<Supplier> loadSupplier(String userid);

	int deleteSupplierByLogic(Integer supplierid);

	boolean batchDeleteSupplierByLogic(List<String> ids);

	List<String> selectNameByUserID(@Param("userid") String userid,
			@Param("supplierid") int supplierid);

	/**
	 * 总包商对应的分包商列表
	 * @param userid
	 * @param supplierid
	 * @return
	 */
	List<String> selectEmailByUserID(@Param("userid") String userid,
			@Param("supplierid") int supplierid);
}