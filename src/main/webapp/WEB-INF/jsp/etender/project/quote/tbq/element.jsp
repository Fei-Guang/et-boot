<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bird" uri="/WEB-INF/bird.tld"%>
<div id="bigMenuContainer">
	<span class="menuHide" onclick="menuHide();"></span>
	<div id="menu-container">
		<c:forEach items="${elements}" var="element" varStatus="status">
			<c:choose>
				<c:when test="${status.count==1 }">
					<div class="menu1 treeMenu selected"
						id="root${element.subprojectid}"
						subProjectID="${element.subprojectid}">
						<span class="open icon"
							onclick="extendFolder('root${element.subprojectid}',0);"></span>
						<c:choose>
							<c:when test="${bird:length(element.name) > 14}">
								<span title="${element.name}"
									onclick="loadBillItem(${element.subprojectid},${element.subprojectid},0);">${bird:substring(element.name, 14)}...</span>
							</c:when>
							<c:otherwise>
								<span title="${element.name}"
									onclick="loadBillItem(${element.subprojectid},${element.subprojectid},0);">${element.name}</span>
							</c:otherwise>
						</c:choose>
					</div>
					<c:forEach items="${element.elements}" var="branchElement">
						<div class="menu2 treeMenu" id="branch${branchElement.elementid}"
							pId="root${element.subprojectid}"
							sId="root${branchElement.subprojectid}"
							subProjectID="${branchElement.subprojectid}">
							<span class="open icon"
								onclick="extendFolder('branch${branchElement.elementid}',1);"></span>
							<c:choose>
								<c:when test="${bird:length(branchElement.name) > 12}">
									<span title="${branchElement.name}"
										onclick="loadBillItem(${branchElement.subprojectid},${branchElement.elementid},1);">${bird:substring(branchElement.name, 12)}...</span>
								</c:when>
								<c:otherwise>
									<span title="${branchElement.name}"
										onclick="loadBillItem(${branchElement.subprojectid},${branchElement.elementid},1);">${branchElement.name}</span>
								</c:otherwise>
							</c:choose>
						</div>
						<c:forEach items="${branchElement.childElements}"
							var="leafElement">
							<div class="menu3 treeMenu" id="leaf${leafElement.elementid}"
								pId="branch${branchElement.elementid}"
								sId="root${leafElement.subprojectid}"
								subProjectID="${leafElement.subprojectid}">
								<c:choose>
									<c:when test="${bird:length(leafElement.name) > 10}">
										<span title="${leafElement.name}"
											onclick="loadBillItem(${leafElement.subprojectid},${leafElement.elementid},2);">${bird:substring(leafElement.name, 10)}...</span>
									</c:when>
									<c:otherwise>
										<span title="${leafElement.name}"
											onclick="loadBillItem(${leafElement.subprojectid},${leafElement.elementid},2);">${leafElement.name}</span>
									</c:otherwise>
								</c:choose>
							</div>
						</c:forEach>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="menu1 treeMenu" id="root${element.subprojectid}"
						subProjectID="${element.subprojectid}">
						<span class="open icon"
							onclick="extendFolder('root${element.subprojectid}',0);"></span>
						<c:choose>
							<c:when test="${bird:length(element.name) > 14}">
								<span title="${element.name}"
									onclick="loadBillItem(${element.subprojectid},${element.subprojectid},0);">${bird:substring(element.name, 14)}...</span>
							</c:when>
							<c:otherwise>
								<span title="${element.name}"
									onclick="loadBillItem(${element.subprojectid},${element.subprojectid},0);">${element.name}</span>
							</c:otherwise>
						</c:choose>
					</div>
					<c:forEach items="${element.elements}" var="branchElement">
						<div class="menu2 treeMenu" id="branch${branchElement.elementid}"
							pId="root${element.subprojectid}"
							sId="root${branchElement.subprojectid}"
							subProjectID="${branchElement.subprojectid}">
							<span class="open icon"
								onclick="extendFolder('branch${branchElement.elementid}',1);"></span>
							<c:choose>
								<c:when test="${bird:length(branchElement.name) > 12}">
									<span title="${branchElement.name}"
										onclick="loadBillItem(${branchElement.subprojectid},${branchElement.elementid},1);">${bird:substring(branchElement.name, 12)}...</span>
								</c:when>
								<c:otherwise>
									<span title="${branchElement.name}"
										onclick="loadBillItem(${branchElement.subprojectid},${branchElement.elementid},1);">${branchElement.name}</span>
								</c:otherwise>
							</c:choose>
						</div>
						<c:forEach items="${branchElement.childElements}"
							var="leafElement">
							<div class="menu3 treeMenu" id="leaf${leafElement.elementid}"
								pId="branch${branchElement.elementid}"
								sId="root${leafElement.subprojectid}"
								subProjectID="${leafElement.subprojectid}">
								<c:choose>
									<c:when test="${bird:length(leafElement.name) > 10}">
										<span title="${leafElement.name}"
											onclick="loadBillItem(${leafElement.subprojectid},${leafElement.elementid},2);">${bird:substring(leafElement.name, 10)}...</span>
									</c:when>
									<c:otherwise>
										<span title="${leafElement.name}"
											onclick="loadBillItem(${leafElement.subprojectid},${leafElement.elementid},2);">${leafElement.name}</span>
									</c:otherwise>
								</c:choose>
							
							</div>
						</c:forEach>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<input type="hidden" id='keyword' />
	</div>
</div>
