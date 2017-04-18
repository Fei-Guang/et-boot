package com.web.service.etender.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.common.ConstantData;
import com.web.model.etender.Trade;
import com.web.service.etender.ITradeService;

@Service("tradeService")
public class TradeService extends BaseService implements ITradeService {

	@Override
	public List<Trade> loadTrade(String userid) {
		return tradeMapper.loadTrade(userid);
	}

	@Override
	public boolean deleteTrade(Integer tradeID) {
		return tradeMapper.deleteByPrimaryKey(tradeID) == 1;
	}

	@Override
	public int editTrade(Trade trade) {
		String code = trade.getCode();
		String userid = trade.getUserid();
		List<String> codes = tradeMapper.selectCodeByUserID(userid,
				trade.getTradeid());
		for (String c : codes) {
			if (code.equalsIgnoreCase(c)) {
				return ConstantData.NameEXISTED;
			}
		}
		tradeMapper.updateByPrimaryKeySelective(trade);
		return trade.getTradeid();
	}

	@Override
	public boolean batchDeleteTrade(List<String> ids) {
		return tradeMapper.batchDeleteTrade(ids);
	}

	@Override
	public Trade loadTrade(int tradeID) {
		return tradeMapper.selectByPrimaryKey(tradeID);
	}
}
