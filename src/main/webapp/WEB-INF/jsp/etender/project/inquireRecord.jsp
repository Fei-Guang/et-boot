<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="project main">
	<div class="inquireRecord">
		<div class="control">
			<div class="menu">
				<span class="menuItem" id="back" onclick="backProject();"
					title='<spring:message
				code="project.inquireRecord.back" />'><spring:message
						code="project.inquireRecord.back" /></span>
			</div>
		</div>
		<table id="recordTable" class="head">
			<thead>
				<tr>
					<th class="sn noOrder"><spring:message code="project.inquireRecord.sn" /></th>
					<th class="td3"><span class="mess"><spring:message
							code="project.inquireRecord.project" /></span><span class="des arrowOrder"></span></th>
					<th class="td4"><span class="mess"><spring:message
							code="project.inquireRecord.name" /></span><span class="des arrowOrder"></span></th>
					<th class="td5 noOrder"><spring:message
							code="project.inquireRecord.email" /></th>
					<th class="td6"><span class="mess"><spring:message
							code="project.inquireRecord.sentTime" /></span><span class="des arrowOrder"></span></th>
					<th class="td7"><span class="mess"><spring:message
							code="project.inquireRecord.receivedTime" /></span><span class="des arrowOrder"></span></th>
					<th class="td8"><span class="mess"><spring:message
							code="project.inquireRecord.startTime" /></span><span class="des arrowOrder"></span></th>
					<th class="td9"><span class="mess"><spring:message
							code="project.inquireRecord.endTime" /></span><span class="des arrowOrder"></span></th>
					<th class="td10"><span class="mess"><spring:message
							code="project.inquireRecord.dueTime" /></span><span class="des arrowOrder"></span></th>
				</tr>
			</thead>
		</table>
		<div class="autoScroll">
			<table class="data" id="tableData">
				<tbody>
					<c:forEach items="${records}" var="record" varStatus="status">
						<tr>
							<td class="sn">${status.count}</td>
							<td class="td3">${record.subProjectName}</td>
							<td class="td4">${record.subConName}</td>
							<td class="td5">${record.subConEmail}</td>
							<td class="td6"
								title='<fmt:formatDate value="${record.timeSent}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'><fmt:formatDate
									value="${record.timeSent}" type="date" pattern="yyyy-MM-dd" /></td>
							<td class="td7"
								title='<fmt:formatDate value="${record.timeReceived}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'><fmt:formatDate
									value="${record.timeReceived}" type="date" pattern="yyyy-MM-dd" /></td>
							<td class="td8"
								title='<fmt:formatDate value="${record.startTime}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'><fmt:formatDate
									value="${record.startTime}" type="date" pattern="yyyy-MM-dd" /></td>
							<td class="td9"
								title='<fmt:formatDate value="${record.endTime}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'><fmt:formatDate
									value="${record.endTime}" type="date" pattern="yyyy-MM-dd" /></td>
							<td class="td10"
								title='${record.timeZone} <fmt:formatDate value="${record.dueTime}"
									type="date" pattern="yyyy-MM-dd HH:mm:ss" />'><fmt:formatDate
									value="${record.dueTime}" type="date" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/record.js'/>"></script>