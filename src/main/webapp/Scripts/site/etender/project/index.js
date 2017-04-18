function showInquireProject() {
	$('div.tableChange span.inquireProject').css("border-bottom", "0px");
	$('div.tableChange span.quoteProject').css("border-bottom",
			"1px solid #dadde0");
	$('div.tableChange span.inquireProject span.topBorder').addClass('active');
	$('div.tableChange span.quoteProject span.topBorder').removeClass('active');
	$('#inquireProject').show();
	$('#quoteProject').hide();
}
function showQuoteProject() {
	$('div.tableChange span.quoteProject').css("border-bottom", "0px");
	$('div.tableChange span.inquireProject').css("border-bottom",
			"1px solid #dadde0");
	$('div.tableChange span.inquireProject span.topBorder').removeClass(
			'active');
	$('div.tableChange span.quoteProject span.topBorder').addClass('active');
	$('#inquireProject').hide();
	$('#quoteProject').show();
}
function loadProject(divId, url) {
	$.ajax({
		type : 'post',
		url : url,
		success : function(data) {
			$("#" + divId).html(data);
			var h = $(window).height();
			$('div.projectIndex div.autoScroll').css("height", h - 240);
		}
	});
}
$(function() {
	loadProject("inquireProject", rootFolder+'/project/inquireList?t='
			+ Math.random());
	loadProject("quoteProject", rootFolder+'/project/quoteList?t=' + Math.random());
	var projectWidth = $("div.project").width();
	var tableSpan = $("div.tableChange span").width();
	var w = tableSpan * 2 + 3;
	$("div.rightNone").width(projectWidth - w);
})