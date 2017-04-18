package com.web.dao.etender;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Subproject;

public interface SubprojectMapper {
	int deleteByPrimaryKey(Integer subprojectid);

	int insert(Subproject record);

	int insertSelective(Subproject record);

	Subproject selectByPrimaryKey(Integer subprojectid);

	int updateByPrimaryKeySelective(Subproject record);

	int updateByPrimaryKey(Subproject record);

	List<Subproject> loadSubProject(Integer queryprojectid);

	/**
	 * 载入分包工程标识集合
	 * 
	 * @param queryprojectid
	 * @return
	 */
	List<Integer> loadSubProjectIDs(Integer queryprojectid);

	/**
	 * 获取时间到期，可评标的分包工程标识集合
	 * 
	 * @param queryprojectid
	 * @return
	 */
	List<Integer> loadEvaluationSubProjectIDs(Integer queryprojectid);

	/**
	 * 获取分包工程所属的询价工程标识集合
	 * 
	 * @param subProjectIDs
	 * @return
	 */
	List<Integer> loadQueryProjectIDs(List<Integer> subProjectIDs);

	/**
	 * 载入最后结束的分包工程信息
	 * 
	 * @param selectCondition
	 * @return
	 */
	Subproject loadLastEndSubProject(HashMap<String, Object> selectCondition);

	/**
	 * 获取用户待报价的分包工程标识集合
	 * 
	 * @param selectCondition
	 * @return
	 */
	List<Integer> loadQuoteSubProjectIDs(HashMap<String, Object> selectCondition);

	/**
	 * 获取用户待报价的分包工程集合
	 * 
	 * @param selectCondition
	 * @return
	 */
	List<Subproject> loadQuoteSubProject(HashMap<String, Object> selectCondition);

	/**
	 * 逻辑删除分包工程
	 * 
	 * @param subprojectid
	 * @return
	 */
	int deleteSubProjectByLogic(Integer subprojectid);

	/**
	 * 获取TBQ对应的分包工程信息
	 * 
	 * @param etenderProjectID
	 * @param tbqSubProjectID
	 * @return
	 */
	Subproject loadEtenderSubProject(
			@Param("queryProjectID") int etenderProjectID,
			@Param("tbqSubProjectID") int tbqSubProjectID);
}