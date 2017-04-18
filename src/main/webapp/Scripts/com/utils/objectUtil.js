/**
 * 判断对象是否存在
 * 
 * @param obj
 * @returns {Boolean}
 */
function isNullOrUndefined(obj) {
	if (typeof (obj) == "undefined" || obj == null) {
		return true;
	}
	return false;
}