package com.web.dao.etender;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Quoted_Detail4tbq;

public interface Quoted_Detail4tbqMapper {
	int deleteByPrimaryKey(Integer detailid);

	int insert(Quoted_Detail4tbq record);

	int insertSelective(Quoted_Detail4tbq record);

	Quoted_Detail4tbq selectByPrimaryKey(Integer detailid);

	int updateByPrimaryKeySelective(Quoted_Detail4tbq record);

	int updateByPrimaryKey(Quoted_Detail4tbq record);

	Quoted_Detail4tbq selectBillitemQuoteDetail(
			@Param("billitemid") int billitemid, @Param("userid") String userid);

	List<Quoted_Detail4tbq> loadQuoteDetail(int billitemid);

	int getQuoteBillitemCount(HashMap<String, Object> condition);
}