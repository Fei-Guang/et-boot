<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<span class="menuShow" onclick="menuShow();"></span>
<div class="control">
	<div class="menu">
		<%--<span class="bt1" onclick="backProjectList();"> <spring:message
				code="project.quote.tbq.projectList" /> 
		</span>--%>
		<%-- <span class="bt2"> <spring:message
				code="project.quote.tbq.find" />
		</span> --%>
		<%-- <span class="bt3"> <spring:message
				code="project.quote.tbq.filter" />
		</span> <span class="bt4" onclick="fold();"> <spring:message
				code="project.quote.tbq.collapseAll" />
		</span><span class="bt5" onclick="expand();"> <spring:message
				code="project.quote.tbq.expandAll" />
		</span><span class="bt6" onclick="refresh();"> <spring:message
				code="project.quote.tbq.refresh" /> 
		</span> 
		<span class="bt7" onclick="saveQuote();"> <spring:message
				code="project.quote.tbq.save" />
		</span>
		<span class="bt8" onclick="checkInOut(1);" id="checkOut"> <spring:message
				code="project.quote.tbq.checkOut" />
		</span> <span class="bt9" style="display:none;" onclick="checkInOut(0);"
			id="checkIn"> <spring:message code="project.quote.tbq.checkIn" />
		</span> --%>
		<span class="bt10" onclick="showDownloadDialog();"><spring:message
				code="project.quote.tbq.download" /></span> <span class="bt11"
			onclick="exportExcel();"> <spring:message
				code="project.quote.tbq.export" />
		</span> <input type="hidden" name="accuracy" id="accuracy"
			value="${accuracy}" />
	</div>
</div>
<table class="head" id="dragTable">
	<thead>
		<tr>
			<th class="sn"><spring:message code="project.quote.tbq.nub" /></th>
			<th class="td3"><spring:message
					code="project.quote.tbq.description" /><span class="quoteFilterIcon" onclick="showDescriptPopup();"></span></th>
			<th class="td4"><spring:message code="project.quote.tbq.trade" /></th>
			<th class="td5"><spring:message code="project.quote.tbq.unit" /></th>
			<th class="td6"><spring:message code="project.quote.tbq.type" /></th>
			<th class="td7"><spring:message code="project.quote.tbq.qty" /></th>
			<th class="td8"><spring:message code="project.quote.tbq.netRate" /></th>
			<th class="td9"><spring:message
					code="project.quote.tbq.netAmount" /></th>
			<th class="td10"><spring:message
					code="project.quote.tbq.remarks" /></th>
		</tr>
	</thead>
</table>
<!-- 报价描述过滤框 -->
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

<div class="quoteAutoScroll">
	<table class="data quoteData" id="treeTable">
		<tbody>
			<c:forEach items="${items}" var="item" varStatus="status">
				<tr class="item" id="${item.billitemid}" pId="${item.billitempid}"
					priceType="${item.pricetype}" type="${item.type}">
					<td class="sn"><c:choose>
							<c:when test="${item.pricetype==2 and item.type==2}">
								<b>RO</b>
							</c:when>
							<c:when test="${item.pricetype==3 and item.type==2}">
								<b>FA</b>
							</c:when>
							<c:when test="${item.pricetype==4 and item.type==2}">
								<b>FR</b>
							</c:when>
							<c:when test="${item.pricetype==5 and item.type==2}">
								<b>EP</b>
							</c:when>
							<c:when test="${item.pricetype==6 and item.type==2}">
								<b>AO</b>
							</c:when>
							<c:otherwise>
							${status.count}
						</c:otherwise>
						</c:choose></td>
					<td class="td3 treeControl" title='${item.description}'><div
							class="desc">${item.description}</div></td>
					<td class="td4" title='${item.trade}'>${item.trade}</td>
					<td class="td5" title='${item.unit}'>${item.unit}</td>
					<td class="td6"><c:choose>
							<c:when test="${item.type==1}">Heading</c:when>
							<c:when test="${item.type==2}">Bill Item</c:when>
							<c:when test="${item.type==3}">Rate Item</c:when>
							<c:when test="${item.type==4}">Note</c:when>
							<c:otherwise></c:otherwise>
						</c:choose></td>
					<td class="td7 showTitle"><c:choose>
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
					<td class="td8 showTitle"><c:choose>
							<c:when test="${item.netrateShow}">
								<c:choose>
									<c:when test="${item.netrateEdit and !isSubmit}">
										<input type="text" name="netRate"
											originalValue="${item.netrate}"
											value="<fmt:formatNumber value='${item.netrate}' maxFractionDigits='8'/>"
											readonly="readonly" class="validate[custom[number]] edit" />
									</c:when>
									<c:otherwise>
										<input type="text" name="netRate"
											originalValue="${item.netrate}"
											value="<fmt:formatNumber value='${item.netrate}' maxFractionDigits='8'/>"
											readonly="readonly" onfocus="this.blur();" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="netRate" originalValue="" value=""
									readonly="readonly" onfocus="this.blur();" />
							</c:otherwise>
						</c:choose></td>
					<td class="td9 showTitle"><c:choose>
							<c:when test="${item.netamountShow}">
								<c:choose>
									<c:when test="${item.netamountEdit and !isSubmit}">
										<input type="text" name="netAmount"
											originalValue="${item.netamount}"
											value="<fmt:formatNumber value='${item.netamount}' maxFractionDigits='8'/>"
											readonly="readonly" class="validate[custom[number]] edit" />
									</c:when>
									<c:otherwise>
										<input type="text" name="netAmount"
											originalValue="${item.netamount}"
											value="<fmt:formatNumber value='${item.netamount}' maxFractionDigits='8'/>"
											readonly="readonly" onfocus="this.blur();" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="netAmount" originalValue="" value=""
									readonly="readonly" onfocus="this.blur();" />
							</c:otherwise>
						</c:choose></td>
					<td class="td10 showTitle">
						<input type="text" name="remark" value="${item.remark}" readonly="readonly" onfocus="this.blur();" />
					<!--
					<c:choose>
							<c:when test="${isSubmit}">
								<c:choose>
									<c:when test="${item.pricetype==1}">
										<input type="text" name="remark" value="${item.remark}" readonly="readonly" onfocus="this.blur();" />
									</c:when>
									<c:when test="${item.pricetype==2 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" value="Rate Only"
													readonly="readonly" onfocus="this.blur();" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													value="${item.remark}" readonly="readonly"
													onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==3 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" value="Fixed Amount"
													readonly="readonly" onfocus="this.blur();" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													value="${item.remark}" readonly="readonly"
													onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==4 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" value="Fixed Rate"
													readonly="readonly" onfocus="this.blur();" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													value="${item.remark}" readonly="readonly"
													onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==5 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" value="Empty Price"
													readonly="readonly" onfocus="this.blur();" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													value="${item.remark}" readonly="readonly"
													onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==6 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" value="Amount Only"
													readonly="readonly" onfocus="this.blur();" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													value="${item.remark}" readonly="readonly"
													onfocus="this.blur();" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${item.pricetype==1}">
										<input type="text" name="remark"
											originalValue="${item.remark}" value="${item.remark}"
											class="edit" readonly="readonly" />
									</c:when>
									<c:when test="${item.pricetype==2 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" originalValue="Rate Only"
													value="Rate Only" class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													originalValue="${item.remark}"
													value="${item.remark}" class="edit"
													readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==3 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark"
													originalValue="Fixed Amount" value="Fixed Amount"
													class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													originalValue="${item.remark}"
													value="${item.remark}" class="edit"
													readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==4 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" originalValue="Fixed Rate"
													value="Fixed Rate" class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													originalValue="${item.remark}"
													value="${item.remark}" class="edit"
													readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==5 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" originalValue="Empty Price"
													value="Empty Price" class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													originalValue="${item.remark}"
													value="${item.remark}" class="edit"
													readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${item.pricetype==6 and item.type==2}">
										<c:choose>
											<c:when test="${item.remark==null}">
												<input type="text" name="remark" originalValue="Amount Only"
													value="Amount Only" class="edit" readonly="readonly" />
											</c:when>
											<c:otherwise>
												<input type="text" name="remark"
													originalValue="${item.remark}"
													value="${item.remark}" class="edit"
													readonly="readonly" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						-->
						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="summary">
	<b>Final Summary</b><span
		class="totalNum" id="totalAmount"><fmt:formatNumber
			value='${totalAmount}' maxFractionDigits='8' /></span>
	<c:if test="${!isSubmit}">
		<a class="submit" onclick="submitQuote();" style="cursor: pointer;">
			<spring:message code="project.quote.tbq.submit" />
		</a>
	</c:if>
</div>
<div class="downLoad" id="downLoadBox">
	<table class="tipHead">
		<thead>
			<tr class="one">
				<th class="fileOne">File name</th>
				<th class="size">Size</th>
				<th class="speed"></th>
			</tr>
		</thead>
	</table>
	<div class="downTable">
		<table class="data">
			<c:forEach items="${attachMents}" var="attachMent" varStatus="status">
				<tr>
					<td class="tdOne">${attachMent.fileName}</td>
					<td class="tdTwo">${attachMent.fileSize}</td>
					<td class="tdThree"><a
						href="<c:url value='/download?filePath=${attachMent.attachPath}&fileName=${attachMent.fileName}'/>"></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>