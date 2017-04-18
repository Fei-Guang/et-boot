$(function(){
	var email=$("div.tipcontent p.p1 span.username").text();
	var start=email.substr(0,1);
	var a=email.indexOf("@");
	var end=email.substr(a);
	var middle=email.substr(1,a-1);
	var xing="";
    for(var i=0;i<middle.length;i++){
    	xing+="*";
    }
	var newEmail=start+xing+end;
	$("div.tipcontent p.p1 span.username").text(newEmail);
})