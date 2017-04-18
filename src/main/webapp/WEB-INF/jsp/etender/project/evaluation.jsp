<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="cell projectEvaluation">
	<div class="tab tableLayout left" id="navigation"></div>
	<div class="tab_page" id="evaluationPage"></div>
	<div id="evalDisplaySettingDialog" class="dialog">
		<div class="back"></div>
		<div class="cnts">
			<div class="top">
				<span>Display Settings</span> <a class="close" title="close dialog" href="javascript:void(0);"></a>
			</div>
			<div class="tableHead">
				<table class="head">
					<thead>
						<tr>
							<th class="s">S/N</th>
							<th class="td3">Column</th>
							<th class="td4">Display</th>
						</tr>
						
					</thead>
				</table>
			</div>
			<div class="autoScroll">
				<table class="data">
					<tr class="item">
						<td class="sn">1</td>
						<td class="td3">Trade</td>
						<td class="dis">
						<input id="disOne" class="chk" type="checkbox">
						<label for="disOne" onclick="changeCheckBox(this);selectTdName(this);return false;"></label>				
					</td>		
					</tr>
					<tr class="item">
						<td class="sn">2</td>
						<td class="td5">Unit</td>
						<td class="dis"><input id="disTwo" class="chk" type="checkbox">
						<label for="disTwo" onclick="changeCheckBox(this);selectTdName(this);return false;"></label></td>
					
					</tr>
					<tr class="item">
						<td class="sn">3</td>
						<td class="td6">Type</td>
						<td class="dis"><input id="disThree" class="chk" type="checkbox">
						<label for="disThree" onclick="changeCheckBox(this);selectTdName(this);return false;"></label></td>
					
					</tr>
					<tr class="item">
						<td class="sn">4</td>
						<td class="s">s</td>
						<td class="dis"><input id="disFour" class="chk" type="checkbox">
						<label for="disFour" onclick="changeCheckBox(this);selectTdName(this);return false;"></label></td>
					
					</tr>
					<tr class="item">
						<td class="sn">5</td>
						<td class="remark">Remarks</td>
						<td class="dis"><input id="disFive" class="chk" type="checkbox">
						<label for="disFive" onclick="changeCheckBox(this);selectTdName(this);return false;"></label></td>
					
					</tr>
					
				</table>
			</div>
			<div class="button">		
			<input type="button" value="OK" class="ok" onclick="hideDisplay();">
			
			</div>
		</div>

	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/validation/jquery.validationEngine-en.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/jquery/jquery.treeTable.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/Array.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/objectUtil.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/dataUtil.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/com/utils/StringBuilder.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/billitem.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/evaluation/tbq/element.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/evaluation/tbq/billitem.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/Scripts/site/etender/project/evaluation.js'/>"></script>