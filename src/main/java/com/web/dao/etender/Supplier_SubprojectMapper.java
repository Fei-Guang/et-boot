package com.web.dao.etender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Supplier_Subproject;

public interface Supplier_SubprojectMapper {
	int deleteByPrimaryKey(Integer suppliersubprojectid);

	int insert(Supplier_Subproject record);

	int insertSelective(Supplier_Subproject record);

	Supplier_Subproject selectByPrimaryKey(Integer suppliersubprojectid);

	int updateByPrimaryKeySelective(Supplier_Subproject record);

	int updateByPrimaryKey(Supplier_Subproject record);

	List<Supplier_Subproject> loadSupplierSubProject(
			HashMap<String, Object> selectCondition);

	/**
	 * 获取分包工程最后发送给分包商的记录
	 * 
	 * @param selectCondition
	 * @return
	 */
	Supplier_Subproject loadLastSupplierSubProject(
			HashMap<String, Object> selectCondition);

	/**
	 * 获取有效的用户待报价分包工程标识集合
	 * 
	 * @param email
	 * @return
	 */
	List<Integer> loadSubProjectIDs(String email);

	/**
	 * 获取供应商邮箱集合
	 * @param subprojectid
	 * @param endtime
	 * @return
	 */
	List<String> loadSupplierEmails(
			@Param("subprojectid") Integer subprojectid,
			@Param("endtime") Date endtime);

	/**
	 * 逻辑删除分包工程与供应商之间的关系记录
	 * 
	 * @param selectCondition
	 * @return
	 */
	int deleteSupplierSubProjectByLogic(HashMap<String, Object> selectCondition);

	/**
	 * 更新供应商接收分包工程时间
	 * 
	 * @param selectCondition
	 * @return
	 */
	int updateSupplierReceivedTime(HashMap<String, Object> selectCondition);

	/**
	 * 更新询价商第一次查看报价时间
	 * 
	 * @param subProjectIDs
	 * @return
	 */
	int updateMainConReviewTime(List<Integer> subProjectIDs);

	/**
	 * 更新供应商第一次提交报价时间
	 * 
	 * @param email
	 * @param subProjectID
	 */
	void updateSupplierSubmitTime(@Param("email") String email,
			@Param("subprojectid") Integer subprojectid);

	/**
	 * 更新供应商第一次保存报价时间
	 * 
	 * @param email
	 * @param subprojectid
	 */
	void updateSupplierFirstSaveTime(@Param("email") String email,
			@Param("subprojectid") String subprojectid);

	/**
	 * 获取分包工程的发送记录数量
	 * 
	 * @param subprojectid
	 * @return
	 */
	int loadSendRecordCount(Integer subprojectid);

	/**
	 * 获取分包工程的查看记录数量
	 * 
	 * @param subprojectid
	 * @return
	 */
	int loadReviewTimeCount(Integer subprojectid);
}