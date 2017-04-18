function record4QuoteProject() {
	if ($("div#quoteProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#quoteProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	id = strEnc(id, firstKey, secondKey, thirdKey);
	window.location.href = rootFolder+'/project/quoteRecord/' + id+ '?t=' + Math.random();
}
function deleteQuoteProject() {
	if ($("div#quoteProject tr.edit").length > 0) {
		return;
	}
	var tr = $("div#quoteProject tr.highlight");
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	if (id == undefined) {
		showSelectTip();
		return;
	}
	art.dialog({
		title : 'Tips',
		lock : true,
		background : '#353535', // 背景色
		opacity : 0.5, // 透明度
		content : 'Sure to delete the selected project?',
		okVal : 'OK',
		ok : function() {
			doDeleteQuoteProject(id);
			return true;
		},
		cancelVal : 'Cancel',
		cancel : true
	});
}
function doDeleteQuoteProject(id) {
	var projectID = strEnc(id, firstKey, secondKey, thirdKey);
	$.get(rootFolder+'/project/deleteQuoteProject/' + projectID + '?t=' + Math.random(), {

	}, function(data) {
		if (data.isPost) {
			window.location.reload();
		} else {
			showTip('Delete project fail.');
		}
	}, "json");
}
function quote(obj) {
	var tr = $(obj);
	var id = $($($(tr).find("td.sn")[0]).find("input")[1]).val();
	id = strEnc(id, firstKey, secondKey, thirdKey);
	window.location.href = rootFolder+'/project/quote/' + id;
}
$(function() {
	changeMenuItemStatus();
});