package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Quoted_Element4tbq;

public interface Quoted_Element4tbqMapper {
	int deleteByPrimaryKey(Integer elementid);

	int insert(Quoted_Element4tbq record);

	int insertSelective(Quoted_Element4tbq record);

	Quoted_Element4tbq selectByPrimaryKey(Integer elementid);

	int updateByPrimaryKeySelective(Quoted_Element4tbq record);

	int updateByPrimaryKey(Quoted_Element4tbq record);

	List<Quoted_Element4tbq> loadTBQElement(
			@Param("subprojectid") int subprojectid, @Param("type") int type);

	List<Quoted_Element4tbq> loadTBQChildElement(
			@Param("subprojectid") int subprojectid,
			@Param("tbqelementid") int tbqelementid);

	/**
	 * 获得属于分包工程的指定类型的类目标识集合
	 * 
	 * @param subprojectid
	 * @param type
	 * @return
	 */
	List<Integer> loadTBQElementIDs(@Param("subprojectid") int subprojectid,
			@Param("type") int type);

	/**
	 * 获得属于分包工程的指定类目下的子类目标识集合
	 * 
	 * @param subprojectid
	 * @param tbqelementid
	 * @return
	 */
	List<Integer> loadTBQChildElementIDs(
			@Param("subprojectid") int subprojectid,
			@Param("tbqelementid") int tbqelementid);
}