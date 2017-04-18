package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Quoted_Adopt4tbq;

public interface Quoted_Adopt4tbqMapper {
	int deleteByPrimaryKey(Integer billitemid);

	int insert(Quoted_Adopt4tbq record);

	int insertSelective(Quoted_Adopt4tbq record);

	Quoted_Adopt4tbq selectByPrimaryKey(Integer billitemid);

	int updateByPrimaryKeySelective(Quoted_Adopt4tbq record);

	int updateByPrimaryKey(Quoted_Adopt4tbq record);

	/**
	 * 调整采纳单价
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateAdoptnetrate(@Param("billitemid") Integer billitemid,
			@Param("adoptnetrate") String data);

	/**
	 * 调整采纳合价
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateAdoptnetamount(@Param("billitemid") Integer billitemid,
			@Param("adoptnetamount") String data);

	/**
	 * 调整采纳备注
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateAdoptRemark(@Param("billitemid") Integer billitemid,
			@Param("adoptremark") String data);

	/**
	 * 获取清单集合中，已采纳的总条数
	 * 
	 * @param itemIDs
	 * @return
	 */
	int loadAdoptItemCount(List<Integer> itemIDs);
}