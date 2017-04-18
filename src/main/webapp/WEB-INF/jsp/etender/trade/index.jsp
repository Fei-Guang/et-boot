<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<form id="tradeForm">
	<table class="data">
		<tbody>
			<c:forEach items="${trades}" var="trade" varStatus="status">
				<tr class="item" ondblclick="editOneRow(this);"
					onclick="selectOneRow(this);">
					<td class="sn unedit"><input type="text"
						value="${status.count}" readonly="readonly" onfocus="this.blur();" /><input
						type="hidden" value="${trade.tradeid}" /></td>
					<td class="td3 edit" onclick="selectOneRows(this);"><input type="text" title="${trade.code}"
						value="${trade.code}" readonly="readonly"
						onkeypress="keyPressTrade(event,this);"
						onblur="submitTradeModify(this);"
						class="validate[required,maxSize[30]]"
						data-errormessage-value-missing="* Trade cannot be null." /></td>
					<td class="td4 edit"><input type="text"
						title="${trade.description}" value="${trade.description}"
						readonly="readonly" onkeypress="keyPressTrade(event,this);"
						onblur="submitTradeModify(this);" class="validate[maxSize[100]]" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<div id="trade_end" style="height:0px; overflow:hidden"></div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/trade/index.js'/>"></script>