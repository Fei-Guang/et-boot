package com.web.controller.etender;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.StringUtil;
import com.web.common.ConstantData;
import com.web.controller.CommonController;
import com.web.model.etender.Trade;
import com.web.model.etender.User;
import com.web.service.etender.ITradeService;

@Controller
@RequestMapping("/trade")
public class TradeController extends CommonController {

	@Autowired
	protected ITradeService tradeService;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			List<Trade> trades = tradeService.loadTrade(user.getUserid());
			request.setAttribute("trades", trades);
		}
		return "etender/trade/index";
	}

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			String trade = request.getParameter("trade");
			if (!StringUtil.isNull(trade)) {
				String[] ts = trade.split(",");
				request.setAttribute("ts", Arrays.asList(ts));
			}
			List<Trade> trades = tradeService.loadTrade(user.getUserid());
			request.setAttribute("trades", trades);
		}
		return "etender/trade/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String code = request.getParameter("code");
			String description = request.getParameter("description");
			if (StringUtil.isNull(code)) {
				isPost = ConstantData.ERROR;
				msg = "Trade cannot be null.";
			} else {
				Trade trade = new Trade();
				trade.setCode(code);
				trade.setDescription(description);
				trade.setUserid(user.getUserid());
				try {
					int tradeID = tradeService.addTrade(trade);
					if (tradeID > 0) {
						jo.put("tradeID", tradeID);
					} else {
						if (tradeID == ConstantData.NameEXISTED) {
							isPost = ConstantData.NameEXISTED;
							msg = "The trade already exists.";
						} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
						}
					}
				} catch (Exception e) {
					logger.error("addTrade:", e);
					isPost = ConstantData.ERROR;
					msg = ConstantData.Error4Other;
				}
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/delete/{tradeID}")
	public void delete(@PathVariable Integer tradeID,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isPost = true;
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			isPost = tradeService.deleteTrade(tradeID);
		} else {
			isPost = false;
		}
		jo.put("isPost", isPost);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String userid = user.getUserid();
			String id = request.getParameter("id");
			String code = request.getParameter("code");
			String description = request.getParameter("description");
			if (StringUtil.isNull(code)) {
				isPost = ConstantData.ERROR;
				msg = "Trade cannot be null.";
			} else {
				Trade trade = new Trade();
				trade.setTradeid(Integer.parseInt(id));
				trade.setCode(code);
				trade.setDescription(description);
				trade.setUserid(userid);
				try {
					int tradeID = tradeService.editTrade(trade);
					if (tradeID > 0) {
						jo.put("tradeID", tradeID);
					} else {
						if (tradeID == ConstantData.NameEXISTED) {
							isPost = ConstantData.NameEXISTED;
							msg = "The trade already exists.";
						} else {
							isPost = ConstantData.ERROR;
							msg = ConstantData.Error4Other;
						}
					}
				} catch (Exception e) {
					logger.error("editTrade:", e);
					isPost = ConstantData.ERROR;
					msg = ConstantData.Error4Other;
				}
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/batchEdit", method = RequestMethod.POST)
	public void batchEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isPost = ConstantData.OK;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String userid = user.getUserid();
			String data = request.getParameter("data");
			StringBuffer sb = new StringBuffer();
			JSONArray ja = JSON.parseArray(data);
			int size = ja.size();
			for (int i = 0; i < size; i++) {
				JSONObject js = ja.getJSONObject(i);
				int id = js.getIntValue("tradeid");
				String code = js.getString("code");
				String description = js.getString("description");
				if (StringUtil.isNull(code)) {
					isPost = ConstantData.ERROR;
					msg = "Trade cannot be null.";
				} else {
					Trade trade = new Trade();
					trade.setCode(code);
					trade.setDescription(description);
					trade.setUserid(userid);
					try {
						int tradeID = 0;
						if (id > 0) {
							// 编辑
							trade.setTradeid(id);
							tradeID = tradeService.editTrade(trade);
						} else {
							// 添加
							tradeID = tradeService.addTrade(trade);
						}
						if (tradeID <= 0) {
							if (tradeID == ConstantData.NameEXISTED) {
								isPost = ConstantData.NameEXISTED;
								msg = "The trade already exists.";
							} else {
								isPost = ConstantData.ERROR;
								msg = ConstantData.Error4Other;
							}
						}
					} catch (Exception e) {
						logger.error("batchEdit:", e);
						isPost = ConstantData.ERROR;
						sb.append(code).append(",");
						msg = ConstantData.Error4Other;
					}
				}
			}
			if (sb.length() > 0) {
				// TODO 批量处理时，失败的专业代码
			}
		} else {
			isPost = ConstantData.ERROR;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public void batchDelete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPost = true;
		String msg = "";
		JSONObject jo = new JSONObject();
		User user = getCurrentUser(request);
		if (user != null) {
			String data = request.getParameter("data");
			if (!StringUtil.isNull(data)) {
				List<String> ids = Arrays.asList(data.split(","));
				isPost = tradeService.batchDeleteTrade(ids);
			}
		} else {
			isPost = false;
			msg = ConstantData.Error4Login;
		}
		jo.put("isPost", isPost);
		jo.put("msg", msg);
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(jo.toJSONString());
		printWriter.flush();
		printWriter.close();
	}
}
