package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Quoted_Billitem4tbq;

public interface Quoted_Billitem4tbqMapper {
	int deleteByPrimaryKey(Integer billitemid);

	int insert(Quoted_Billitem4tbq record);

	int insertSelective(Quoted_Billitem4tbq record);

	Quoted_Billitem4tbq selectByPrimaryKey(Integer billitemid);

	int updateByPrimaryKeySelective(Quoted_Billitem4tbq record);

	int updateByPrimaryKeyWithBLOBs(Quoted_Billitem4tbq record);

	int updateByPrimaryKey(Quoted_Billitem4tbq record);

	List<Quoted_Billitem4tbq> loadBillItem(int elementid);

	/**
	 * 获得所属类目标识的清单标识集合
	 * 
	 * @param elementIDs
	 * @return
	 */
	List<Integer> loadBillItemIDs(List<Integer> elementIDs);

	/**
	 * 调整标底单价
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateAdjustnetrate(@Param("billitemid") Integer billitemid,
			@Param("adjustnetrate") String data);

	/**
	 * 调整标底合价
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateAdjustnetamount(@Param("billitemid") Integer billitemid,
			@Param("adjustnetamount") String data);

	/**
	 * 调整备注
	 * 
	 * @param billitemid
	 * @param data
	 * @return
	 */
	int updateRemark(@Param("billitemid") Integer billitemid,
			@Param("remark") String data);

	/**
	 * 调整修改项标识
	 * 
	 * @param billitemid
	 * @param ischange
	 * @return
	 */
	int updateChangeStatus(@Param("billitemid") Integer billitemid,
			@Param("ischange") byte ischange);
}