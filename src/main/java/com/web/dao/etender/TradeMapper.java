package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Trade;

public interface TradeMapper {
	int deleteByPrimaryKey(Integer tradeid);

	int insert(Trade record);

	int insertSelective(Trade record);

	Trade selectByPrimaryKey(Integer tradeid);

	int updateByPrimaryKeySelective(Trade record);

	int updateByPrimaryKey(Trade record);

	List<Trade> loadTrade(String userid);

	int getTradeCount4User(String userid);

	boolean batchDeleteTrade(List<String> ids);

	List<String> selectCodeByUserID(@Param("userid") String userid,
			@Param("tradeid") int tradeid);
}