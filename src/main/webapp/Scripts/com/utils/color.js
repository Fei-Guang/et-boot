function zero_fill_hex(num, digits) {
	var s = num.toString(16);
	while (s.length < digits)
		s = "0" + s;
	return s;
}
function rgb2hex(rgb) {
	if (rgb.charAt(0) == '#')
		return rgb;
	var ds = rgb.split(/\D+/);
	var decimal = Number(ds[1]) * 65536 + Number(ds[2]) * 256 + Number(ds[3]);
	return ("#" + zero_fill_hex(decimal, 6)).toUpperCase();
}
function rgbToHex(r, g, b) {
	return ("#" + ((r << 16) | (g << 8) | b).toString(16)).toUpperCase();
}