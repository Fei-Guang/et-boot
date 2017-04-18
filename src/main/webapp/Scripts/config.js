//rootFolder is empty for ROOT CONTEXT
var rootFolder = '';
window.ARTDIALOG_HOME_URL = rootFolder+'/Styles/thirdparty/artDialog/';
window.DATEPICKER_HOME_URL = rootFolder+'/Styles/thirdparty/datePicker/';
window.UEDITOR_HOME_URL = rootFolder+'/Scripts/thirdparty/ueditor/';
window.SITE_URL = rootFolder+'/Styles/jquery/treeTable/';
var baseUrl = getBaseUrl();
function getBaseUrl() {
	var baseUrl = 'http://'+location.host+rootFolder;
	return baseUrl;
}
