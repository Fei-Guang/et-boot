<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="control">
	<div class="menu">
		<span class="menuItem" id="deleteProject"
			onclick="deleteQuoteProject();"
			title='<spring:message
				code="project.quoteList.delete" />'><spring:message
				code="project.quoteList.delete" /></span> <span class="menuItem"
			id="recordProject" onclick="record4QuoteProject();"
			title='<spring:message
				code="project.quoteList.record" />'><spring:message
				code="project.quoteList.record" /></span>
	</div>
</div>
<table id="quoteTable" class="head">
	<thead>
		<tr>
			<th class="sn"><spring:message code="project.quoteList.sn" /></th>
			<th class="td3"><spring:message code="project.quoteList.name" /></th>
			<th class="td4"><spring:message
					code="project.quoteList.dateCreated" /></th>
			<th class="td5"><spring:message
					code="project.quoteList.mainContractor" /></th>
			<th class="td6"><spring:message
					code="project.quoteList.contacts" /></th>
			<th class="td7"><spring:message code="project.quoteList.phone" /></th>
			<th class="td8"><spring:message code="project.quoteList.dueDate" /></th>
			<th class="td9"><spring:message code="project.quoteList.status" /></th>
		</tr>
	</thead>
</table>
<div class="autoScroll">
	<table class="data">
		<tbody>
			<c:forEach items="${quoteProjects}" var="quoteProject"
				varStatus="status">
				<tr class="item" ondblclick="quote(this);"
					onclick="selectOneRow(this);">
					<td class="sn unedit"><input type="text"
						value="${status.count}" readonly="readonly" onfocus="this.blur();" /><input
						type="hidden" value="${quoteProject.queryprojectid}" /></td>
					<td class="td3 unedit" onclick="selectOneRows(this);"><input type="text"
						title="${quoteProject.name}" value="${quoteProject.name}"
						readonly="readonly" onfocus="this.blur();" /></td>
					<td class="td4 unedit"><input type="text"
						title="${quoteProject.createDate}"
						value="<fmt:formatDate value="${quoteProject.createDate}"
							type="date" pattern="yyyy-MM-dd HH:mm:ss" />"
						readonly="readonly" onfocus="this.blur();" /></td>
					<td class="td5 unedit"><input type="text"
						title="${quoteProject.mainCon}" value="${quoteProject.mainCon}"
						readonly="readonly" onfocus="this.blur();" /></td>
					<td class="td6 unedit"><input type="text"
						title="${quoteProject.contacts}" value="${quoteProject.contacts}"
						readonly="readonly" onfocus="this.blur();" /></td>
					<td class="td7 unedit"><input type="text"
						title="${quoteProject.telephone}"
						value="${quoteProject.telephone}" readonly="readonly"
						onfocus="this.blur();" /></td>
					<td class="td8 unedit"><input type="text"
						title='${quoteProject.timeZone} <fmt:formatDate value="${quoteProject.dueTime}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'
						value="<fmt:formatDate value="${quoteProject.dueTime}"
							type="date" pattern="yyyy-MM-dd" />"
						readonly="readonly" onfocus="this.blur();" /></td>
					<td class="td9 unedit"><input type="text"
						title="${quoteProject.status}" value="${quoteProject.status}"
						readonly="readonly" onfocus="this.blur();" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/list/quoteList.js'/>"></script>