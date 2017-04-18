<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="project main sentPorject" style="position: relative;">
	<div class="projectInquire">
		<div id="line"></div>
		<form id="inquireForm">
			<div id="selectSubcontract">
				<input type="text" placeholder="Select Subcontract" id="subContract"
					onclick="showSubcontractList(id);" readonly="readonly" /><span
					class="triangle" onclick="showSubcontractList();"></span>
				<!-- <span
				id="importSubcontract"></span> -->
			</div>
			<div class="subcontractList popupMenu" tabindex="0"
				onblur="hideSubcontractList();">
				<div id="subcontractList">
					<c:forEach items="${subProjects}" var="subProject"
						varStatus="status">
						<div class="subcontractListItem">
							<span class="list"
								onclick="selectSubcontract('${subProject.name}','${subProject.subprojectid}');">${subProject.name}
							</span> <span class="deleteIcon"
								onclick="deleteSubItem(this,'${subProject.subprojectid}');"></span>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="title">
				<span class="dueIcon sentIcon"></span>Due Date
			</div>

			<div id="selectTime">
				<input type="text" placeholder="Select Time Zone" id="timeZone"
					onclick="showTimeZoneList(id);" readonly="readonly" /><span
					class="triangle timeZoneTriangle" onclick="showTimeZoneList();"></span>
				<input type="text" placeholder="Select Time" class="ui_timepicker"
					id="endTime" name="datetime" readonly="readonly" />
			</div>
			<div class="timeZoneList popupMenu" tabindex="0"
				onblur="hideTimeZoneList();">
				<div id="timeZoneList">
					<div class="predefine">
						<span class="list"
							onclick="selectTimeZone('(UTC+07:00) Bangkok, Hanoi, Jakarta');">(UTC+07:00)
							Bangkok, Hanoi, Jakarta</span> <span class="list"
							onclick="selectTimeZone('(UTC+08:00) Beijing, Chongqing, Hong kong, Urumqi');">(UTC+08:00)
							Beijing, Chongqing, Hong kong, Urumqi</span> <span class="list"
							onclick="selectTimeZone('(UTC+08:00) Kuala Lumpur, Singapore');">(UTC+08:00)
							Kuala Lumpur, Singapore</span>
						<div class="control">
							<span class="plus right" onclick="showAllTimeZone();"></span><span
								class="tip right" onclick="showAllTimeZone();">MORE</span>
						</div>
					</div>
					<div class="all">
						<c:forEach items="${applicationScope.timeZones}" var="timeZone"
							varStatus="status">
							<span class="list" onclick="selectTimeZone('${timeZone}');">${timeZone}</span>
						</c:forEach>
					</div>
				</div>
			</div>
			<div class="title">
				<span class="subcontIcon sentIcon"></span> Subcontractors <span
					class="menuItem" id="subConManagement"
					onclick="subConManagement();"><a>Sub-Con Management</a></span>
			</div>
			<div id="allSubCon" class="table">
				<div class="popUp tradePopup" tabindex="0"
					onblur="hideTradePopup();">
					<div class="searchBox">
						<input type="text" value="" class="inputBox" id="tradeKey"
							onblur="hideTradePopup();" /><span class="searchIcon"
							onclick="searchTrade(this);"></span>
					</div>
					<div class="choose">
						<table class="trade">
							<c:forEach items="${tradeFilters}" var="tradeFilter"
								varStatus="status">
								<tr>
									<td class="box"><input type="checkbox" class="chk"
										name="trade4${tradeFilter}" id="tradeBox${status.count}" /><label
										for="tradeBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label></td>
									<td class="kw">${tradeFilter}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
					<div class="okCancel">
						<input type="button" class="yes" value="OK"
							onclick="filterTrade();" /> <input type="button" class="cancel"
							value="Cancel" onclick="cancelTrade();" />
					</div>
				</div>
				<div class="popUp levelPopup" onblur="hideLevelPopup();"
					tabindex="0">
					<div class="searchBox">
						<input type="text" value="" class="inputBox" id="levelKey"
							onblur="hideLevelPopup();" /><span class="searchIcon"
							onclick="searchLevel(this);"></span>
					</div>
					<div class="choose">
						<table class="level">
							<c:forEach items="${levelFilters}" var="levelFilter"
								varStatus="status">
								<tr>
									<td class="box"><input type="checkbox" class="chk"
										name="level4${levelFilter}" id="levelBox${status.count}" /><label
										for="levelBox${status.count}"
										onclick="changeCheckBox(this);return false;"></label></td>
									<td class="kw">${levelFilter}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
					<div class="okCancel">
						<input type="button" class="yes" value="OK"
							onclick="filterLevel();" /> <input type="button" class="cancel"
							value="Cancel" onclick="cancelLevel();" />
					</div>
				</div>
				<div class="tableHead">
					<table class="head">
						<thead>
							<tr>
								<th class="box"></th>
								<th class="sn">S/N</th>
								<th class="td3">Sub-Con Name</th>
								<th class="td4">Email</th>
								<th class="td5">Trade<span class="pop-up"
									onclick="tradePopupShow();"></span></th>
								<th class="td6">Level<span class="pop-up"
									onclick="levelPopupShow();"></span></th>
								<th class="td7">Contacts</th>
								<th class="td8">Phone</th>
								<th class="td9">Address</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="autoScroll">
					<table class="data">
						<tbody>
							<c:forEach items="${suppliers}" var="supplier" varStatus="status">
								<tr class="item">
									<td class="box"><input type="checkbox" class="chk"
										name="select4${supplier.supplierid}"
										id="chbox4Supplier${status.count}" /> <label
										for="chbox4Supplier${status.count}"
										onclick="changeCheckBox(this);hideAttach();selectSupplier(this);return false;"></label>
										<input type="hidden" value="${supplier.supplierid}" /></td>
									<td class="sn unedit"><input type="text"
										value="${status.count}" readonly="readonly"
										onfocus="this.blur();" /></td>
									<td class="td3 unedit"><input type="text"
										title="${supplier.name}" value="${supplier.name}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td4 unedit"><input type="text"
										title="${supplier.email}" value="${supplier.email}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td5 unedit"><input type="text"
										title="${supplier.trade}" value="${supplier.trade}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td6 unedit"><input type="text"
										title="${supplier.level}" value="${supplier.level}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td7 unedit"><input type="text"
										title="${supplier.contacts}" value="${supplier.contacts}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td8 unedit"><input type="text"
										title="${supplier.telephone}" value="${supplier.telephone}"
										readonly="readonly" onfocus="this.blur();" /></td>
									<td class="td9 unedit" class="more"><input type="text"
										title="${supplier.address}" value="${supplier.address}"
										readonly="readonly" onfocus="this.blur();" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>
		<div class="title selectTitle">
			<span class="selectIcon sentIcon"></span>Selected
		</div>
	</div>
	<div id="selectedSubCon" class="table">
		<input type="hidden" name="subProject" id="subProject" value="" /> <input
			type="hidden" name="supplierID" id="supplierID" value="" />
		<div class="tableHead">
			<div class="arrowBottom">
				<div class="arrow1"></div>
				<div class="arrow2"></div>
			</div>
			<table class="head tip">
				<thead>
					<tr class="tip">
						<th class="box"></th>
						<th class="sn"></th>
						<th class="td3"></th>
						<th class="td4"></th>
						<th class="td5"></th>
						<th class="td6"></th>
						<th class="td7"></th>
						<th class="td8"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="tableHead">
			<table class="head">
				<thead>
					<tr class="head">
						<th class="box"></th>
						<th class="sn">S/N</th>
						<th class="td3">Subcontract</th>
						<th class="td4">Sub-Con Name</th>
						<th class="td5">Email</th>
						<th class="td6">Due Time</th>
						<th class="td7"><span class="uploadAttachControl"
							id="uploadAttach"><input id="fileupload" type="file"
								name="files[]" /></span></th>
						<th class="td8"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="autoScroll">
			<table class="data">
				<tbody>
					<c:forEach items="${subProject_Subcontractors}"
						var="subProject_Subcontractor" varStatus="status">
						<tr class="item"
							id="${subProject_Subcontractor.subproject}_${subProject_Subcontractor.subcontractorId}"
							sid="${subProject_Subcontractor.subcontractorId}">
							<td class="box"></td>
							<td class="sn unedit"><input type="text"
								value="${status.count}" readonly="readonly"
								onfocus="this.blur();" /></td>
							<td class="td3 unedit"><input type="text"
								title="${subProject_Subcontractor.subproject}"
								value="${subProject_Subcontractor.subproject}"
								readonly="readonly" onfocus="this.blur();" /></td>
							<td class="td4 unedit"><input type="text"
								title="${subProject_Subcontractor.subcontractor}"
								value="${subProject_Subcontractor.subcontractor}"
								readonly="readonly" onfocus="this.blur();" /></td>
							<td class="td5 unedit"><input type="text"
								title="${subProject_Subcontractor.email}"
								value="${subProject_Subcontractor.email}" readonly="readonly"
								onfocus="this.blur();" /></td>
							<td class="td6 unedit"><input type="text"
								title="${subProject_Subcontractor.datetime}"
								value="${subProject_Subcontractor.datetime}" readonly="readonly"
								onfocus="this.blur();" /></td>
							<td class="td7"><span class="uploadAttach controlItem"
								onclick="hideAttach();uploadAttach(this);"
								title="Make sure to upload a file smaller than 100M."></span>
								 <c:choose>
									<c:when test="${subProject_Subcontractor.hasAttach}">
										<span class="plusAttach showAttach"onclick="showAttach(this);"></span>
									</c:when>
									<c:otherwise>
										<span class="plusAttach"onclick="showAttach(this);"></span>
									</c:otherwise> 
								</c:choose> 
								<span class="deleteItem controlItem"
								onclick="deleteItem(this);"></span>
							</td>
							<td class="td8"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div class="button">
		<input class="submit" value="Send" type="submit"
			onclick="submit();return false;">
	</div>
	<div id="attachment">
		<span id="hideAttachment" onclick="hideAttach();"></span>
		<div class="arrowBottom">
			<div class="arrow1"></div>
			<div class="arrow2"></div>
		</div>
		<div class="data"></div>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/ui/jquery-ui-1.8.17.custom.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/ui/jquery-ui-timepicker-addon.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.fileupload.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine-en.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/StringBuilder.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/objectUtil.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/inquire.js'/>"></script>