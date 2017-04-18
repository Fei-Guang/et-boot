package com.web.dao.etender;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Quoted_Discount4tbq;

public interface Quoted_Discount4tbqMapper {
	int deleteByPrimaryKey(Integer discountid);

	int insert(Quoted_Discount4tbq record);

	int insertSelective(Quoted_Discount4tbq record);

	Quoted_Discount4tbq selectByPrimaryKey(Integer discountid);

	int updateByPrimaryKeySelective(Quoted_Discount4tbq record);

	int updateByPrimaryKey(Quoted_Discount4tbq record);

	Quoted_Discount4tbq selectBillitemQuoteDiscount(
			@Param("userid") String userid,
			@Param("subprojectid") int subProjectID);
}