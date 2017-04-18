<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ul>
	<c:forEach items="${attachMents}" var="attachMent" varStatus="status">
		<li class="left">${attachMent.fileName};<span
			class="deleteAttachItem"
			onclick="deleteSupplierAttachRecord(this,'${attachMent.subproject}','${attachMent.subcontractorId}','${attachMent.attachPath}');"></span>
		</li>
	</c:forEach>
	 <div class="attachButton">
	<span class="plus"></span>
	<input type="button" value="Attach" class="addAttach" onclick="hideAttach();againLoadAttach();">
	</div>
</ul>
