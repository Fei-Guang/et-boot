package com.web.service.etender;

import java.util.List;

import com.web.model.etender.Trade;

public interface ITradeService {

	List<Trade> loadTrade(String userid);

	int addTrade(Trade trade);

	boolean deleteTrade(Integer tradeID);

	int editTrade(Trade trade);

	int getTradeCount4User(String userid);

	void initTrade4User(String userid);

	boolean batchDeleteTrade(List<String> ids);

	Trade loadTrade(int tradeID);

}
