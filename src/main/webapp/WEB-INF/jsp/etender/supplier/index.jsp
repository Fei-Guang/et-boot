<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="supplier main">
	<div class="supplierIndex">
		<div class="top">
			<div class="menu ">
				<span class="menuItem" id="addSupplier" onclick="addSupplier();">New</span>
				<span class="menuItem" id="editSupplier" onclick="editSupplier();">Edit</span>
				<span class="menuItem" id="deleteSupplier"
					onclick="deleteSupplier();">Delete</span> <span class="menuItem"
					id="exportSupplier" onclick="downloadSupplier();">Export</span> <span
					class="menuItem" id="importSupplier">Import<input
					id="fileupload" type="file" name="files[]"
					title="Make sure to upload a excel file smaller than 5M." /></span> <span
					class="border"></span><span class="menuItem" id="tradeManagement"
					onclick="tradeManagement();">Trade Management</span>
			</div>
		</div>
		<div class="tableHead">
			<table class="head">
				<thead>
					<tr>
						<th class="sn">S/N</th>
						<th class="td3">Sub-Con Name<span class="star"></span></th>
						<th class="td4">Email<span class="star"></span></th>
						<th class="td5">Trade</th>
						<th class="td6">Level</th>
						<th class="td7">Contacts</th>
						<th class="td8">Phone</th>
						<th class="td9">Address</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="autoScroll">
			<form id="supplierForm">
				<table class="data">
					<tbody>
						<c:forEach items="${suppliers}" var="supplier" varStatus="status">
							<tr class="item" ondblclick="editOneRow(this);"
								onclick="selectOneRow(this);">
								<td class="sn unedit"><input type="text"
									value="${status.count}" readonly="readonly"
									onfocus="this.blur();" /><input type="hidden"
									value="${supplier.supplierid}" /></td>
								<td class="td3 edit" onclick="selectOneRows(this);"><input type="text"
									title="${supplier.name}" value="${supplier.name}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);"
									class="validate[required,maxSize[255],custom[specialCharacter]]"
									data-errormessage-value-missing="* Sub con names cannot be null." /></td>
								<td class="td4 edit"><input type="text"
									title="${supplier.email}" value="${supplier.email}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);"
									class="validate[required,custom[email4Null]]"
									data-errormessage-value-missing="* Emails cannot be null." /></td>
								<td class="td5 edit"><input type="text"
									title="${supplier.trade}" value="${supplier.trade}"
									readonly="readonly" onclick="showTradeList(this);"
									onkeypress="keyPress(event,this);"
									onblur="hideTradeList();submitModify(this);" /></td>
								<td class="td6 edit"><input type="text"
									title="${supplier.level}" value="${supplier.level}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);" class="validate[maxSize[10]]" /></td>
								<td class="td7 edit"><input type="text"
									title="${supplier.contacts}" value="${supplier.contacts}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);" class="validate[maxSize[50]]" /></td>
								<td class="td8 edit"><input type="text"
									title="${supplier.telephone}" value="${supplier.telephone}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);" class="validate[maxSize[30]]" /></td>
								<td class="td9 edit" class="more"><input type="text"
									title="${supplier.address}" value="${supplier.address}"
									readonly="readonly" onkeypress="keyPress(event,this);"
									onblur="submitModify(this);" class="validate[maxSize[255]]" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<div id="supplier_end" style="height:0px; overflow:hidden"></div>
			<div class="button">
				<input class="cancel" value="Back" type="button"
					onclick="cancel();return false;">
			</div>
		</div>
	</div>
</div>
<div class="tradeList popupMenu" tabindex="0" onblur="hideTradeList();">
	<div id="tradeList"></div>
</div>
<div id="tradeManagementDialog" class="dialog">
	<div class="back"></div>
	<div class="cnts">
		<div class="top">
			<span>Trade Management</span> <a class="close"
				href="javascript:void(0);" title="close dialog"></a>
		</div>
		<div class="content">
			<div class="menu">
				<span class="menuItem" id="addTrade" onclick="addTrade();">Add</span>
				<span class="menuItem" id="editTrade" onclick="editTrade();">Edit</span>
				<span class="menuItem" id="deleteTrade" onclick="deleteTrade();">Delete</span>
			</div>
			<div class="tableHead">
				<table class="head">
					<thead>
						<tr>
							<th class="sn">S/N</th>
							<th class="td3">Trade<span class="star"></span></th>
							<th class="td4">Description</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="autoScroll" id="tradeData"></div>
		</div>
		<div class="button">
			<div class="tip left">&nbsp;</div>
			<input value="Back" class="cancel" type="button" />
		</div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.nicescroll.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.fileupload.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine-en.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/StringBuilder.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/url.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/supplier/index.js'/>"></script>