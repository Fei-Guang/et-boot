 //base64编码，需要引入base64.js
function encodeBase64(str) {
	var b = new Base64();
	return b.encode(str);
}
// base64解码，需要引入base64.js
function decodeBase64(str) {
	var b = new Base64();
	return b.decode(str);
}
// md5加密，需要引入md5.js
function encodeMD5(str) {
	return hex_md5(str);
}
// sha1加密，需要引入sha1.js
function encodeSHA1(str) {
	return hex_sha1(str);
}
// des加密，需要引入des.js
function encrypt(str,key1,key2,key3) {
	return strEnc(str,key1,key2,key3);
}
//des解密，需要引入des.js
function decrypt(str,key1,key2,key3) {
	return strDec(str,key1,key2,key3);
}