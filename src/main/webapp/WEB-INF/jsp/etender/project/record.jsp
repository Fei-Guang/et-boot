<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="project main">
	<div class="projectRecord">
		<div class="control">
			<div class="menu">
				<span class="menuItem" id="back" onclick="backProject();"
					title='<spring:message
				code="project.record.back" />'><spring:message
						code="project.record.back" /></span>
			</div>
		</div>
		<table id="recordTable" class="head">
			<thead>
				<tr>
					<th class="sn"><spring:message code="project.record.sn" /></th>
					<th class="td3"><spring:message code="project.record.project" /></th>
					<th class="td4"><spring:message code="project.record.email" /></th>
					<th class="td5"><spring:message code="project.record.sentTime" /></th>
					<th class="td6"><spring:message
							code="project.record.receivedTime" /></th>
					<th class="td7"><spring:message
							code="project.record.startTime" /></th>
					<th class="td8"><spring:message code="project.record.endTime" /></th>
					<th class="td9"><spring:message
							code="project.record.reviewedAt" /></th>
					<th class="td10"><spring:message code="project.record.dueTime" /></th>
					<th class="td11"><spring:message
							code="project.record.timeZone" /></th>
				</tr>
			</thead>
		</table>
		<div class="autoScroll">
			<table class="data">
				<tbody>
					<c:forEach items="${records}" var="record" varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>${record.projectName }</td>
							<td class="name">${record.email }</td>
							<td><fmt:formatDate value="${record.sendTime}" type="both"
									pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${record.receiveTime}"
									type="both" pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${record.startQuoteTime}"
									type="both" pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${record.endQuoteTime}"
									type="both" pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${record.quoteDueTime}"
									type="both" pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${record.viewTime}" type="both"
									pattern="yyyy/MM/dd HH:mm:ss" /></td>
							<td>${record.timeZone}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="lastRow"></div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/record.js'/>"></script>