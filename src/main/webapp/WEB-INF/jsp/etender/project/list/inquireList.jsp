<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="control">
	<div class="menu">
		<%-- <span class="menuItem" id="createProject" onclick="createProject();"
			title='<spring:message
				code="project.inquireList.new" />'><spring:message
				code="project.inquireList.new" /></span> --%> <span class="menuItem"
			id="editProject" onclick="editProject();"
			title='<spring:message
				code="project.inquireList.edit" />'><spring:message
				code="project.inquireList.edit" /> </span> <span class="menuItem"
			id="deleteProject" onclick="deleteInquireProject();"
			title='<spring:message
				code="project.inquireList.delete" />'><spring:message
				code="project.inquireList.delete" /></span>
		<%-- <span class="menuItem"
			id="passwordProject" onclick="password4InquireProject();"
			title='<spring:message
				code="project.inquireList.password" />'><spring:message
				code="project.inquireList.password" /></span> --%>
		<span class="menuItem" id="recordProject"
			onclick="record4InquireProject();"
			title='<spring:message
				code="project.inquireList.record" />'><spring:message
				code="project.inquireList.record" /></span> <span class="menuItem"
			id="inquireProject" onclick="send4InquireProject();"
			title='<spring:message
				code="project.inquireList.inquire" />'><spring:message
				code="project.inquireList.inquire" /></span>
	</div>
</div>
<table id="inquireTable" class="head">
	<thead>
		<tr>
			<th class="sn"><spring:message code="project.inquireList.sn" /></th>
			<th class="td3"><spring:message code="project.inquireList.name" /></th>
			<th class="td4"><spring:message
					code="project.inquireList.dateCreated" /></th>
			<th class="td5"><spring:message
					code="project.inquireList.status" /></th>
			<%-- <th class="td6"><spring:message
					code="project.inquireList.amount" /></th> --%>
		</tr>
	</thead>
</table>
<div class="autoScroll">
	<form id="projectForm">
		<table class="data">
			<tbody>
				<c:forEach items="${queryProjects}" var="queryProject"
					varStatus="status">
					<tr class="item" ondblclick="inquire(this);"
						onclick="selectOneRow(this);">
						<td class="sn unedit"><input type="text"
							value="${status.count}" onfocus="this.blur();" /><input
							type="hidden" value="${queryProject.queryprojectid}" /></td>
						<td class="td3 edit" onclick="selectOneRows(this);"><input
							type="text" title="${queryProject.name}"
							value="${queryProject.name}" readonly="readonly"
							onkeypress="keyPress(event,this);" onblur="submitModify(this);"
							class="validate[required,maxSize[255],custom[specialCharacter]]"
							data-errormessage-value-missing="* Project names cannot be null." /></td>
						<td class="td4 unedit"><input type="text"
							value="<fmt:formatDate value="${queryProject.createtime}"
							type="date" pattern="yyyy-MM-dd HH:mm:ss" />"
							onfocus="this.blur();" /></td>
						<td class="td5 unedit"><c:choose>
								<c:when test="${queryProject.status==0}">
									<input type="text"
										value="<spring:message code="project.inquireList.notInquired" />"
										onfocus="this.blur();" />
								</c:when>
								<c:when test="${queryProject.status==1}">
									<input type="text"
										value="<spring:message code="project.inquireList.notInquired" />"
										onfocus="this.blur();" />
								</c:when>
								<c:when test="${queryProject.status==2}">
									<input type="text"
										value="<spring:message code="project.inquireList.inquired" />"
										onfocus="this.blur();" />
								</c:when>
								<c:when test="${queryProject.status==3}">
									<input type="text"
										value="<spring:message code="project.inquireList.evaluating" />"
										onfocus="this.blur();" />
								</c:when>
								<c:when test="${queryProject.status==4}">
									<input type="text"
										value="<spring:message code="project.inquireList.evaluated" />"
										onfocus="this.blur();" />
								</c:when>
								<c:otherwise></c:otherwise>
							</c:choose></td>
						<%-- <td class="td6 unedit"><input type="text"
							title="${queryProject.totalprice}"
							value="${queryProject.totalprice}" onfocus="this.blur();" /></td> --%>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	<div id="queryProject_end" style="height:0px; overflow:hidden"></div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine-en.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/Array.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/objectUtil.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/list/inquireList.js'/>"></script>
