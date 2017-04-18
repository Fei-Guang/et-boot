<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<table class="data">
	<tbody>
		<c:forEach items="${trades}" var="trade" varStatus="status">
			<tr class="item">
				<td class="box"><c:choose>
						<c:when test="${fn:contains(ts,trade.code)}">
							<input type="checkbox" class="chk"
								id="chbox4Trade${status.count}" checked="checked" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" class="chk"
								id="chbox4Trade${status.count}" />
						</c:otherwise>
					</c:choose> <label for="chbox4Trade${status.count}"
					onclick="changeCheckBox(this);selectTrade();return false;"></label></td>
				<td class="td3 unedit"><input type="text" value="${trade.code}"
					title="${trade.code}" readonly="readonly"
					onfocus="this.blur();" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
