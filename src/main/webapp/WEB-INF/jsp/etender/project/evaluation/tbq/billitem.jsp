<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<span class="menuShow" onclick="menuShow();"></span>
<div class="doubleTip">
	<span class="star"></span>
	<p>Click "Double click to edit" to switch to "Double click to
		adopt"</p>
	<span class="dele" onclick="hideTip();"></span>
</div>
<div class="control">
	<div class="menu">
		<%-- <span class="bt3" onclick="showOpenIcon();"> <spring:message
				code="project.evaluation.tbq.filter" />
		</span> --%>
		<c:choose>
			<c:when test="${sessionScope.evaluation_changed=='1'}">
				<span class="bt4 bt4Active" onclick="showChangedItem(this);">
					Show Changed Item </span>
			</c:when>
			<c:otherwise>
				<span class="bt4" onclick="showChangedItem(this);"> Show
					Changed Item </span>
			</c:otherwise>
		</c:choose>
		<span class="bt5" onclick="exportExcel();"> <spring:message
				code="project.evaluation.tbq.export" />
		</span> <span class="bt6" onclick="displaySetting();"> Display
			Settings </span> <span class="bt7" id="bQh"> Double
			click to edit </span> <input type="hidden" name="accuracy" id="accuracy"
			value="${accuracy}" />
	</div>
</div>
<div class="content">
	<p class="switch" onclick="showSub();"></p>
	<div class="popUp descriptPopup" tabindex="0"
		onblur="hideDescriptPopup();">
		<div class="searchBox">
			<input type="text" value="" class="inputBox" id="descriptKey"
				onblur="hideDescriptPopup();" /> <span class="searchIcon"
				onclick="searchDescript(this);"></span>
		</div>
		<div class="choose">
			<table class="descript">
				<c:forEach items="${descriptFilterMap}" var="descriptFilter"
					varStatus="status">
					<tr>
						<td class="box"><c:choose>
								<c:when test="${descriptFilter.value}">
									<input type="checkbox" class="chk"
										name="descript4${descriptFilter.key}"
										id="descriptBox${status.count}" checked="checked"
										choose="true" />
									<label for="descriptBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label>
								</c:when>
								<c:otherwise>
									<input type="checkbox" class="chk"
										name="descript4${descriptFilter.key}"
										id="descriptBox${status.count}" />
									<label for="descriptBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label>
								</c:otherwise>
							</c:choose></td>
						<td class="kw">${descriptFilter.key}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="okCancel">
			<input type="button" class="yes" value="OK"
				onclick="filterDescript();" /> <input type="button" class="cancel"
				value="Cancel" onclick="cancelDescript();" />
		</div>
	</div>
	<table class="head" id="dragTable">
		<tbody>
			<tr class="standard">
				<td class="td1"></td>
				<td class="td2"></td>
				<td class="td3"></td>
				<!-- 	<td class="td4"></td> -->
				<td class="td5"></td>
				<td class="td6"></td>
				<td class="td7"></td>
				<td class="net"></td>
				<td class="s"></td>
				<td class="amount1"></td>
				<td class="remark"></td>
				<td class="net"></td>
				<td class="s"></td>
				<td class="amount1"></td>
				<td class="remark"></td>
				<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
					<td class="net"></td>
					<td class="s"></td>
					<td class="amount1"></td>
					<td class="remark"></td>
				</c:forEach>
				<td class="scrollbar"></td>
			</tr>
			<tr class="tr1">
				<td colspan="6" rowspan="3"></td>
				<td colspan="4" rowspan="3" class="adopted">ADOPTED</td>
				<td colspan="4" rowspan="3" class="adopted">Pre-Tender
					Estimate(PTE)</td>
				<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
					<td colspan="2" class="white" name='mark'
						supplierID='${quoteInfo.userid}'>${quoteInfo.mark}</td>
					<td class="white hiddenPrice" colspan="2"
						hiddenPrice='${quoteInfo.hiddenPrice4SubProject}'
						fixAmount='${quoteInfo.fixAmount}'
						supplierID='${quoteInfo.userid}'>${quoteInfo.name}</td>
				</c:forEach>
			</tr>
			<tr class="tr2">
				<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
					<td colspan="2" class="white" name="discount">Discount(%)</td>
					<td class="white disnumber" colspan="2"><input type='text'
						supplierID='${quoteInfo.userid}' readonly="readonly"
						class="validate[custom[number]] edit"
						originalValue="${quoteInfo.discountPercent}"
						value="${quoteInfo.discountPercent}" name='discountPercent' /></td>
				</c:forEach>
			</tr>
			<tr class="tr3">
				<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
					<td colspan="2" class="white" name="discount">Discount</td>
					<td class="white disnumber" colspan="2"><input type='text'
						supplierID='${quoteInfo.userid}' readonly="readonly"
						class="validate[custom[number]] edit"
						originalValue="${quoteInfo.discount}"
						value="${quoteInfo.discount}" name='discountAmount' /></td>
				</c:forEach>
			</tr>
			<tr class="tr4">
				<td class="td1">S/N</td>
				<td class="td2"><span class="des">Description</span><span
					class="evalFilterIcon" onclick="showDescriptPopup();"></span></td>
				<td class="td3">Trade<span class="tradePopuIcon"
					onclick="showTradePopup();"></span>
            <!-- 专业弹出框 -->
					<div class="popUp tradePopup" tabindex="0"
						onblur="hideTradePopup();">
						<div class="searchBox">
							<input type="text" value="" class="inputBox" id="tradeKey"
								onblur="hideTradePopup();" /> <div class="searchIcon"
								onclick="searchTrade(this);"></div>
						</div>
						<div class="choose">
							<table class="trade">
								<c:forEach items="${tradeFilterMap}" var="tradeFilter"
									varStatus="status">
									<tr>
										<td class="box"><c:choose>
												<c:when test="${tradeFilter.value}">
													<input type="checkbox" class="chk"
														name="trade4${tradeFilter.key}"
														id="tradeBox${status.count}" checked="checked"
														choose="true" />
													<label for="tradeBox${status.count}"
														onclick="changeCheckBox(this);return false;"></label>
												</c:when>
												<c:otherwise>
													<input type="checkbox" class="chk"
														name="trade4${tradeFilter.key}"
														id="tradeBox${status.count}" />
													<label for="tradeBox${status.count}"
														onclick="changeCheckBox(this);return false;"></label>
												</c:otherwise>
											</c:choose></td>
										<td class="kw">${tradeFilter.key}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
						<div class="okCancel">
							<input type="button" class="yes" value="OK"
								onclick="filterTrade();" /> <input type="button" class="cancel"
								value="Cancel" onclick="cancelTrade();" />
						</div>
					</div>
				</td>
				<!-- <td class="td4">Element</td> -->
				<td class="td5">Unit<span class="unitPopuIcon"
					onclick="showUnitPopup();"></span>
					<div class="popUp unitPopup" tabindex="0" onblur="hideUnitPopup();">
		<div class="searchBox">
			<input type="text" value="" class="inputBox" id="unitKey"
				onblur="hideUnitPopup();" /> <div class="searchIcon"
				onclick="searchUnit(this);"></div>
		</div>
		<div class="choose">
			<table class="unit">
				<c:forEach items="${unitFilterMap}" var="unitFilter"
					varStatus="status">
					<tr>
						<td class="box"><c:choose>
								<c:when test="${unitFilter.value}">
									<input type="checkbox" class="chk"
										name="unit4${unitFilter.key}" id="unitBox${status.count}"
										checked="checked" choose="true" />
									<label for="unitBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label>
								</c:when>
								<c:otherwise>
									<input type="checkbox" class="chk"
										name="unit4${unitFilter.key}" id="unitBox${status.count}" />
									<label for="unitBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label>
								</c:otherwise>
							</c:choose></td>
						<td class="kw">${unitFilter.key}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="okCancel">
			<input type="button" class="yes" value="OK" onclick="filterUnit();" />
			<input type="button" class="cancel" value="Cancel"
				onclick="cancelUnit();" />
		</div>
	</div>
				</td>
				<td class="td6">Type</td>
				<td class="td7">Qty</td>
				<td class="net">Net rate</td>
				<td class="s">S</td>
				<td class="amount1">Amount</td>
				<td class="remark">Remarks</td>
				<td class="net">Net rate</td>
				<td class="s"></td>
				<td class="amount1">Amount</td>
				<td class="remark">Remarks</td>
				<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
					<td class="net">Net rate</td>
					<td class="s"></td>
					<td class="amount1">Amount</td>
					<td class="remark">Remarks</td>
				</c:forEach>
				<td class="scrollbar"></td>
			</tr>
		</tbody>
	</table>
	<div class="autoScroll">
		<table class="data evalData" id="treeTable">
			<tbody>
				<tr class="standard">
					<td class="td1"></td>
					<td class="td2"></td>
					<td class="td3"></td>
					<!-- <td class="td4"></td> -->
					<td class="td5"></td>
					<td class="td6"></td>
					<td class="td7"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="amount1"></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="amount1"></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="amount1"></td>
						<td class="remark"></td>
					</c:forEach>
					<td class="scrollbar"></td>
				</tr>
				<c:forEach items="${items}" var="item" varStatus="itemStatus">
					<tr class="item" id="${item.billitemid}" pId="${item.billitempid}"
						priceType="${item.pricetype}" type="${item.type}">
						<td class="td1"><c:choose>
								<c:when test="${item.pricetype==2 and item.type==2}">
									<b>RO</b>
								</c:when>
								<c:when test="${item.pricetype==3 and item.type==2}">
									<b>FA</b>
								</c:when>
								<c:when test="${item.pricetype==4 and item.type==2}">
									<b> FR</b>
								</c:when>
								<c:when test="${item.pricetype==5 and item.type==2}">
									<b> EP</b>
								</c:when>
								<c:when test="${item.pricetype==6 and item.type==2}">
									<b>AO</b>
								</c:when>
								<c:otherwise>				
              ${itemStatus.count}
            </c:otherwise>
							</c:choose></td>
						<td class="td2 treeControl" title='${item.description}'><div>${item.description}</div></td>
						<td class="td3" title='${item.trade}'>${item.trade}</td>
						<%-- <td class="td4" title='${item.elementName}'>${item.elementName}</td> --%>
						<td class="td5" title='${item.unit}'>${item.unit}</td>
						<td class="td6"><c:choose>
								<c:when test="${item.type==1}">Heading</c:when>
								<c:when test="${item.type==2}">Bill Item</c:when>
								<c:when test="${item.type==3}">Rate Item</c:when>
								<c:when test="${item.type==4}">Note</c:when>
								<c:otherwise></c:otherwise>
							</c:choose></td>
						<td class="td7"><c:choose>
								<c:when test="${item.qtyShow}">
									<input type="text" name="qty" title='${item.qty}'
										value="<fmt:formatNumber value='${item.qty}' maxFractionDigits='8'/>"
										readonly="readonly" onfocus="this.blur();" />
								</c:when>
								<c:otherwise>
									<input type="hidden" name="qty" value="" readonly="readonly"
										onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<!-- 采纳信息 -->
						<td class="net showTitle"><c:choose>
								<c:when test="${item.netrateShow}">
									<c:choose>
										<c:when test="${item.netrateEdit}">
											<input type="text" name="netRate"
												originalValue="${item.adoptedNetrate}"
												setValue="${item.adoptedNetrate}" supplierID='-1'
												value="<fmt:formatNumber value='${item.adoptedNetrate}' maxFractionDigits='8'/>"
												readonly="readonly" class="validate[custom[number]] edit" />
										</c:when>
										<c:otherwise>
											<input type="text" name="netRate"
												originalValue="${item.adoptedNetrate}"
												setValue="${item.adoptedNetrate}" supplierID='-1'
												value="<fmt:formatNumber value='${item.adoptedNetrate}' maxFractionDigits='8'/>"
												readonly="readonly" onfocus="this.blur();" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="netRate" originalValue=""
										setValue="" value="" supplierID='-1' readonly="readonly"
										onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<!-- 采纳标识 -->
						<td class="s showTitle" supplierID='-1'><c:choose>
								<c:when test="${item.netrateShow or item.netamountShow}">
									<input type="text" name="adoptedUserMark" supplierID='-1'
										adoptedUserID='${item.adoptedUserID}'
										value="${item.adoptedUserMark}" readonly="readonly"
										onfocus="this.blur();" />
								</c:when>
								<c:otherwise>
									<!--BUG 431 要求打开-->
									<input type="text" name="adoptedUserMark" supplierID='-1'
										adoptedUserID='${item.adoptedUserID}' value=""
										readonly="readonly" onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<td class="showTitle"><c:choose>
								<c:when test="${item.netamountShow}">
									<c:choose>
										<c:when test="${item.netamountEdit}">
											<input type="text" name="netAmount" supplierID='-1'
												originalValue="${item.adoptedNetamount}"
												setValue="${item.adoptedNetamount}"
												value="<fmt:formatNumber value='${item.adoptedNetamount}' maxFractionDigits='8'/>"
												readonly="readonly" class="validate[custom[number]] edit" />
										</c:when>
										<c:otherwise>
											<input type="text" name="netAmount" supplierID='-1'
												originalValue="${item.adoptedNetamount}"
												setValue="${item.adoptedNetamount}"
												value="<fmt:formatNumber value='${item.adoptedNetamount}' maxFractionDigits='8'/>"
												readonly="readonly" onfocus="this.blur();" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="netAmount" originalValue=""
										setValue="" value="" supplierID='-1' readonly="readonly"
										onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<!-- 采纳备注，如果不为空，就要带上价格标识，如果为空，就保持空 -->
						<td class="remark showTitle"><c:choose>
								<c:when test="${item.pricetype==1}">
									<input type="text" name="remark"
										originalValue="${item.adoptedRemark}"
										value="${item.adoptedRemark}" supplierID='-1' class="edit"
										readonly="readonly" />
								</c:when>
								<c:when test="${item.pricetype==2}">
									<c:choose>
										<c:when test="${item.adoptedRemark==null}">
											<input type="text" name="remark" originalValue="Rate Only"
												value="Rate Only" supplierID='-1' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.adoptedRemark}"
												value="${item.adoptedRemark}" supplierID='-1'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==3}">
									<c:choose>
										<c:when test="${item.adoptedRemark==null}">
											<input type="text" name="remark" originalValue="Fixed Amount"
												value="Fixed Amount" supplierID='-1' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.adoptedRemark}"
												value="${item.adoptedRemark}" supplierID='-1'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==4}">
									<c:choose>
										<c:when test="${item.adoptedRemark==null}">
											<input type="text" name="remark" originalValue="Fixed Rate"
												value="Fixed Rate" supplierID='-1' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.adoptedRemark}"
												value="${item.adoptedRemark}" supplierID='-1'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==5}">
									<c:choose>
										<c:when test="${item.adoptedRemark==null}">
											<input type="text" name="remark" originalValue="Empty Price"
												value="Empty Price" supplierID='-1' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.adoptedRemark}"
												value="${item.adoptedRemark}" supplierID='-1'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==6}">
									<c:choose>
										<c:when test="${item.adoptedRemark==null}">
											<input type="text" name="remark" originalValue="Amount Only"
												value="Amount Only" supplierID='-1' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.adoptedRemark}"
												value="${item.adoptedRemark}" supplierID='-1'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose></td>
						<!-- 标底信息 -->
						<td class="net showTitle adopt" supplierID='0'><c:choose>
								<c:when test="${item.netrateShow}">
									<c:choose>
										<c:when test="${item.netrateEdit}">
											<input type="text" name="netRate"
												originalValue="${item.adjustnetrate}"
												setValue="${item.netrate}" supplierID='0'
												value="<fmt:formatNumber value='${item.adjustnetrate}' maxFractionDigits='8'/>"
												readonly="readonly" class="validate[custom[number]] edit" />
										</c:when>
										<c:otherwise>
											<input type="text" name="netRate"
												originalValue="${item.adjustnetrate}"
												setValue="${item.netrate}" supplierID='0'
												value="<fmt:formatNumber value='${item.adjustnetrate}' maxFractionDigits='8'/>"
												readonly="readonly" onfocus="this.blur();" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="netRate" originalValue=""
										setValue="" value="" supplierID='0' readonly="readonly"
										onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<td class="s adopt" supplierID='0'></td>
						<td class="showTitle adopt" supplierID='0'><c:choose>
								<c:when test="${item.netamountShow}">
									<c:choose>
										<c:when test="${item.netamountEdit}">
											<input type="text" name="netAmount" supplierID='0'
												originalValue="${item.adjustnetamount}"
												setValue="${item.netamount}"
												value="<fmt:formatNumber value='${item.adjustnetamount}' maxFractionDigits='8'/>"
												readonly="readonly" class="validate[custom[number]] edit" />
										</c:when>
										<c:otherwise>
											<input type="text" name="netAmount" supplierID='0'
												originalValue="${item.adjustnetamount}"
												setValue="${item.netamount}"
												value="<fmt:formatNumber value='${item.adjustnetamount}' maxFractionDigits='8'/>"
												readonly="readonly" onfocus="this.blur();" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="netAmount" originalValue=""
										setValue="" value="" supplierID='0' readonly="readonly"
										onfocus="this.blur();" />
								</c:otherwise>
							</c:choose></td>
						<td class="remark showTitle adopt" supplierID='0'><c:choose>
								<c:when test="${item.pricetype==1}">
									<input type="text" name="remark" originalValue="${item.remark}"
										value="${item.remark}" supplierID='0' class="edit"
										readonly="readonly" />
								</c:when>
								<c:when test="${item.pricetype==2}">
									<c:choose>
										<c:when test="${item.remark==null}">
											<input type="text" name="remark" originalValue="Rate Only"
												value="Rate Only" supplierID='0' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.remark}"
												value="${item.remark}" supplierID='0'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==3}">
									<c:choose>
										<c:when test="${item.remark==null}">
											<input type="text" name="remark" originalValue="Fixed Amount"
												value="Fixed Amount" supplierID='0' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.remark}"
												value="${item.remark}" supplierID='0'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==4}">
									<c:choose>
										<c:when test="${item.remark==null}">
											<input type="text" name="remark" originalValue="Fixed Rate"
												value="Fixed Rate" supplierID='0' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.remark}"
												value="${item.remark}" supplierID='0'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==5}">
									<c:choose>
										<c:when test="${item.remark==null}">
											<input type="text" name="remark" originalValue="Empty Price"
												value="Empty Price" supplierID='0' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.remark}"
												value="${item.remark}" supplierID='0'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${item.pricetype==6}">
									<c:choose>
										<c:when test="${item.remark==null}">
											<input type="text" name="remark" originalValue="Amount Only"
												value="Amount Only" supplierID='0' class="edit"
												readonly="readonly" />
										</c:when>
										<c:otherwise>
											<input type="text" name="remark"
												originalValue="${item.remark}"
												value="${item.remark}" supplierID='0'
												class="edit" readonly="readonly" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose></td>
						<!-- 各个供应商的报价信息 -->
						<c:forEach items="${quoteInfos}" var="userQuoteInfo"
							varStatus="quoteStatus">
							<td class="net showTitle adopt"
								supplierID='${userQuoteInfo.userid}'><c:choose>
									<c:when test="${item.netrateShow}">
										<c:choose>
											<c:when test="${item.netrateEdit}">
												<input type="text" name="netRate"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetrate}'
													setValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].netrate}"
													value="<fmt:formatNumber value='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetrate}' maxFractionDigits='8'/>"
													readonly="readonly" class="validate[custom[number]] edit" />
											</c:when>
											<c:otherwise>
												<input type="text" name="netRate"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetrate}'
													setValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].netrate}"
													value="<fmt:formatNumber value='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetrate}' maxFractionDigits='8'/>"
													readonly="readonly" onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="netRate"
											billItemID="${item.billitemid}"
											supplierID='${userQuoteInfo.userid}' originalValue=""
											setValue="" value="" readonly="readonly"
											onfocus="this.blur();" />
									</c:otherwise>
								</c:choose></td>
							<td class="s adopt" supplierID='${userQuoteInfo.userid}'></td>
							<td class="showTitle adopt" supplierID='${userQuoteInfo.userid}'><c:choose>
									<c:when test="${item.netamountShow}">
										<c:choose>
											<c:when test="${item.netamountEdit}">
												<input type="text" name="netAmount"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetamount}'
													setValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].netamount}"
													value="<fmt:formatNumber value='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetamount}' maxFractionDigits='8'/>"
													readonly="readonly" class="validate[custom[number]] edit" />
											</c:when>
											<c:otherwise>
												<input type="text" name="netAmount"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetamount}'
													setValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].netamount}"
													value="<fmt:formatNumber value='${userQuoteInfo.quoteInfo[itemStatus.count-1].adjustnetamount}' maxFractionDigits='8'/>"
													readonly="readonly" onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="netAmount"
											billItemID="${item.billitemid}"
											supplierID='${userQuoteInfo.userid}' originalValue=""
											setValue="" value="" readonly="readonly"
											onfocus="this.blur();" />
									</c:otherwise>
								</c:choose></td>
							<!-- 报价商备注信息 -->
							<td class="remark showTitle adopt"
								supplierID='${userQuoteInfo.userid}'><c:choose>
									<c:when test="${item.pricetype==1}">
										<input type="text" name="remark"
											billItemID="${item.billitemid}"
											supplierID='${userQuoteInfo.userid}'
											originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
											value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
											class="edit" readonly="readonly" />
									</c:when>
									<c:when test="${item.pricetype==2}">
										<c:choose>
											<c:when
												test="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark==null}">
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="Rate Only" value="Rate Only" class="edit"
													readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													class="edit" readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==3}">
										<c:choose>
											<c:when
												test="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark==null}">
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="Fixed Amount" value="Fixed Amount"
													class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													class="edit" readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==4}">
										<c:choose>
											<c:when
												test="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark==null}">
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="Fixed Rate" value="Fixed Rate" class="edit"
													readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													class="edit" readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==5}">
										<c:choose>
											<c:when
												test="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark==null}">
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="Empty Price" value="Empty Price"
													class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													class="edit" readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==6}">
										<c:choose>
											<c:when
												test="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark==null}">
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="Amount Only" value="Amount Only"
													class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													billItemID="${item.billitemid}"
													supplierID='${userQuoteInfo.userid}'
													originalValue="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													value="${userQuoteInfo.quoteInfo[itemStatus.count-1].remark}"
													class="edit" readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose></td>
						</c:forEach>
						<!-- <td class="scrollbar"></td> -->
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="summary">
		<a><span class="open"></span></a>
		<table class="bottom" id="bottomTable">
			<tbody>
				<tr class="standard">
					<td class="td1"></td>
					<td class="td2"></td>
					<td class="td3"></td>
					<!-- <td class="td4"></td> -->
					<td class="td5"></td>
					<td class="td6"></td>
					<td class="td7"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="amount1"></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="amount1"></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="amount1"></td>
						<td class="remark"></td>
					</c:forEach>
					<td class="scrollbar"></td>
				</tr>
				<tr class="bb">
					<td></td>
					<td colspan="5" class="collection">Collection</td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text" name='collection'
						originalValue='' supplierID='-1' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text" name='collection'
						originalValue='' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text" name='collection'
							originalValue='' supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" class="number"
						title="Number of Unpriced Bill Items">Number of Unpriced Bill
						Items</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='numberOfUnpriced' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							name="numberOfUnpriced" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" class="rate"
						title="Number of Unpriced Bill Item (Rate Only)">Number of
						Unpriced Bill Item (Rate Only)</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='numberOfUnpricedRO' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							name="numberOfUnpricedRO" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" class="common pu" title="Common Total Amounts">Common
						Total Amounts<span class="a ab"></span>
					</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='commonTotalAmount' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							name="commonTotalAmount" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" title="Balance Amount" class="pu">Balance
						Amount<span class="b ab"></span>
					</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text" name='balanceAmount'
						supplierID='0' readonly="readonly" onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text" name="balanceAmount"
							supplierID='${quoteInfo.userid}' readonly="readonly"
							onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" title="Maximum Makeup Amount" class="pu">Maximum
						Makeup Amount<span class="c ab"></span>
					</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='maxMakeupAmount' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							name="maxMakeupAmount" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden maxTotalAmount">
					<td colspan="6" class="green pu" title="Maximum Total Amount">Maximum
						Total Amount<span class="abc ab"></span>
					</td>
					<td class="net unpriced">
						<!-- <span class="apply"></span> -->
					</td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s" supplierID='0'></td>
					<td class="showTitle"><input type="text" name='maxTotalAmount'
						supplierID='0' readonly="readonly" onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s" supplierID='${quoteInfo.userid}'></td>
						<td class="showTitle"><input type="text"
							name="maxTotalAmount" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" title="Minimum Makeup Amount" class="pu">Minimum
						Makeup Amount<span class="d ab"></span>
					</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='minMakeupAmount' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							name="minMakeupAmount" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden minTotalAmount">
					<td colspan="6" class="pink pu" title="Minimum Total Amount">Minimum
						Total Amount<span class="abd ab"></span>
					</td>
					<td class="net unpriced"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s" supplierID='0'></td>
					<td class="showTitle"><input type="text" name='minTotalAmount'
						supplierID='0' readonly="readonly" onfocus="this.blur();" /></td>
					<td class="remark"></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s" supplierID='${quoteInfo.userid}'></td>
						<td class="showTitle"><input type="text"
							name="minTotalAmount" supplierID='${quoteInfo.userid}'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="remark"></td>
					</c:forEach>
				</tr>
				<tr class="trHidden">
					<td colspan="6" title="Difference Against Adopted Amount">Difference
						Against Adopted Amount</td>
					<td class="net"></td>
					<td class="s"></td>
					<td></td>
					<td class="remark"></td>
					<td class="net"></td>
					<td class="s"></td>
					<td class="showTitle"><input type="text"
						name='differenceAmount' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="showTitle remark"><input type="text"
						name='differencePercent' supplierID='0' readonly="readonly"
						onfocus="this.blur();" /></td>
					<c:forEach items="${quoteInfos}" var="quoteInfo" varStatus="status">
						<td class="net"></td>
						<td class="s"></td>
						<td class="showTitle"><input type="text"
							supplierID='${quoteInfo.userid}' name='differenceAmount'
							readonly="readonly" onfocus="this.blur();" /></td>
						<td class="showTitle remark"><input type="text"
							supplierID='${quoteInfo.userid}' name='differencePercent'
							readonly="readonly" onfocus="this.blur();" /></td>
					</c:forEach>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</div>
