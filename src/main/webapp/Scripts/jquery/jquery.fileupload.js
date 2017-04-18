(function(e){if(typeof define==="function"&&define.amd){define(["jquery"],e)}else{e(jQuery)}})(function(e,t){var n=0,r=Array.prototype.slice,i=e.cleanData;e.cleanData=function(t){for(var n=0,r;(r=t[n])!=null;n++){try{e(r).triggerHandler("remove")}catch(s){}}i(t)};e.widget=function(t,n,r){var i,s,o,u,a={},f=t.split(".")[0];t=t.split(".")[1];i=f+"-"+t;if(!r){r=n;n=e.Widget}e.expr[":"][i.toLowerCase()]=function(t){return!!e.data(t,i)};e[f]=e[f]||{};s=e[f][t];o=e[f][t]=function(e,t){if(!this._createWidget){return new o(e,t)}if(arguments.length){this._createWidget(e,t)}};e.extend(o,s,{version:r.version,_proto:e.extend({},r),_childConstructors:[]});u=new n;u.options=e.widget.extend({},u.options);e.each(r,function(t,r){if(!e.isFunction(r)){a[t]=r;return}a[t]=function(){var e=function(){return n.prototype[t].apply(this,arguments)},i=function(e){return n.prototype[t].apply(this,e)};return function(){var t=this._super,n=this._superApply,s;this._super=e;this._superApply=i;s=r.apply(this,arguments);this._super=t;this._superApply=n;return s}}()});o.prototype=e.widget.extend(u,{widgetEventPrefix:s?u.widgetEventPrefix||t:t},a,{constructor:o,namespace:f,widgetName:t,widgetFullName:i});if(s){e.each(s._childConstructors,function(t,n){var r=n.prototype;e.widget(r.namespace+"."+r.widgetName,o,n._proto)});delete s._childConstructors}else{n._childConstructors.push(o)}e.widget.bridge(t,o)};e.widget.extend=function(n){var i=r.call(arguments,1),s=0,o=i.length,u,a;for(;s<o;s++){for(u in i[s]){a=i[s][u];if(i[s].hasOwnProperty(u)&&a!==t){if(e.isPlainObject(a)){n[u]=e.isPlainObject(n[u])?e.widget.extend({},n[u],a):e.widget.extend({},a)}else{n[u]=a}}}}return n};e.widget.bridge=function(n,i){var s=i.prototype.widgetFullName||n;e.fn[n]=function(o){var u=typeof o==="string",a=r.call(arguments,1),f=this;o=!u&&a.length?e.widget.extend.apply(null,[o].concat(a)):o;if(u){this.each(function(){var r,i=e.data(this,s);if(!i){return e.error("cannot call methods on "+n+" prior to initialization; "+"attempted to call method '"+o+"'")}if(!e.isFunction(i[o])||o.charAt(0)==="_"){return e.error("no such method '"+o+"' for "+n+" widget instance")}r=i[o].apply(i,a);if(r!==i&&r!==t){f=r&&r.jquery?f.pushStack(r.get()):r;return false}})}else{this.each(function(){var t=e.data(this,s);if(t){t.option(o||{})._init()}else{e.data(this,s,new i(o,this))}})}return f}};e.Widget=function(){};e.Widget._childConstructors=[];e.Widget.prototype={widgetName:"widget",widgetEventPrefix:"",defaultElement:"<div>",options:{disabled:false,create:null},_createWidget:function(t,r){r=e(r||this.defaultElement||this)[0];this.element=e(r);this.uuid=n++;this.eventNamespace="."+this.widgetName+this.uuid;this.options=e.widget.extend({},this.options,this._getCreateOptions(),t);this.bindings=e();this.hoverable=e();this.focusable=e();if(r!==this){e.data(r,this.widgetFullName,this);this._on(true,this.element,{remove:function(e){if(e.target===r){this.destroy()}}});this.document=e(r.style?r.ownerDocument:r.document||r);this.window=e(this.document[0].defaultView||this.document[0].parentWindow)}this._create();this._trigger("create",null,this._getCreateEventData());this._init()},_getCreateOptions:e.noop,_getCreateEventData:e.noop,_create:e.noop,_init:e.noop,destroy:function(){this._destroy();this.element.unbind(this.eventNamespace).removeData(this.widgetName).removeData(this.widgetFullName).removeData(e.camelCase(this.widgetFullName));this.widget().unbind(this.eventNamespace).removeAttr("aria-disabled").removeClass(this.widgetFullName+"-disabled "+"ui-state-disabled");this.bindings.unbind(this.eventNamespace);this.hoverable.removeClass("ui-state-hover");this.focusable.removeClass("ui-state-focus")},_destroy:e.noop,widget:function(){return this.element},option:function(n,r){var i=n,s,o,u;if(arguments.length===0){return e.widget.extend({},this.options)}if(typeof n==="string"){i={};s=n.split(".");n=s.shift();if(s.length){o=i[n]=e.widget.extend({},this.options[n]);for(u=0;u<s.length-1;u++){o[s[u]]=o[s[u]]||{};o=o[s[u]]}n=s.pop();if(arguments.length===1){return o[n]===t?null:o[n]}o[n]=r}else{if(arguments.length===1){return this.options[n]===t?null:this.options[n]}i[n]=r}}this._setOptions(i);return this},_setOptions:function(e){var t;for(t in e){this._setOption(t,e[t])}return this},_setOption:function(e,t){this.options[e]=t;if(e==="disabled"){this.widget().toggleClass(this.widgetFullName+"-disabled ui-state-disabled",!!t).attr("aria-disabled",t);this.hoverable.removeClass("ui-state-hover");this.focusable.removeClass("ui-state-focus")}return this},enable:function(){return this._setOption("disabled",false)},disable:function(){return this._setOption("disabled",true)},_on:function(t,n,r){var i,s=this;if(typeof t!=="boolean"){r=n;n=t;t=false}if(!r){r=n;n=this.element;i=this.widget()}else{n=i=e(n);this.bindings=this.bindings.add(n)}e.each(r,function(r,o){function u(){if(!t&&(s.options.disabled===true||e(this).hasClass("ui-state-disabled"))){return}return(typeof o==="string"?s[o]:o).apply(s,arguments)}if(typeof o!=="string"){u.guid=o.guid=o.guid||u.guid||e.guid++}var a=r.match(/^(\w+)\s*(.*)$/),f=a[1]+s.eventNamespace,l=a[2];if(l){i.delegate(l,f,u)}else{n.bind(f,u)}})},_off:function(e,t){t=(t||"").split(" ").join(this.eventNamespace+" ")+this.eventNamespace;e.unbind(t).undelegate(t)},_delay:function(e,t){function n(){return(typeof e==="string"?r[e]:e).apply(r,arguments)}var r=this;return setTimeout(n,t||0)},_hoverable:function(t){this.hoverable=this.hoverable.add(t);this._on(t,{mouseenter:function(t){e(t.currentTarget).addClass("ui-state-hover")},mouseleave:function(t){e(t.currentTarget).removeClass("ui-state-hover")}})},_focusable:function(t){this.focusable=this.focusable.add(t);this._on(t,{focusin:function(t){e(t.currentTarget).addClass("ui-state-focus")},focusout:function(t){e(t.currentTarget).removeClass("ui-state-focus")}})},_trigger:function(t,n,r){var i,s,o=this.options[t];r=r||{};n=e.Event(n);n.type=(t===this.widgetEventPrefix?t:this.widgetEventPrefix+t).toLowerCase();n.target=this.element[0];s=n.originalEvent;if(s){for(i in s){if(!(i in n)){n[i]=s[i]}}}this.element.trigger(n,r);return!(e.isFunction(o)&&o.apply(this.element[0],[n].concat(r))===false||n.isDefaultPrevented())}};e.each({show:"fadeIn",hide:"fadeOut"},function(t,n){e.Widget.prototype["_"+t]=function(r,i,s){if(typeof i==="string"){i={effect:i}}var o,u=!i?t:i===true||typeof i==="number"?n:i.effect||n;i=i||{};if(typeof i==="number"){i={duration:i}}o=!e.isEmptyObject(i);i.complete=s;if(i.delay){r.delay(i.delay)}if(o&&e.effects&&e.effects.effect[u]){r[t](i)}else if(u!==t&&r[u]){r[u](i.duration,i.easing,s)}else{r.queue(function(n){e(this)[t]();if(s){s.call(r[0])}n()})}}})});(function(e){"use strict";if(typeof define==="function"&&define.amd){define(["jquery"],e)}else{e(window.jQuery)}})(function(e){"use strict";var t=0;e.ajaxTransport("iframe",function(n){if(n.async){var r=n.initialIframeSrc||"javascript:false;",i,s,o;return{send:function(u,a){i=e('<form style="display:none;"></form>');i.attr("accept-charset",n.formAcceptCharset);o=/\?/.test(n.url)?"&":"?";if(n.type==="DELETE"){n.url=n.url+o+"_method=DELETE";n.type="POST"}else if(n.type==="PUT"){n.url=n.url+o+"_method=PUT";n.type="POST"}else if(n.type==="PATCH"){n.url=n.url+o+"_method=PATCH";n.type="POST"}t+=1;s=e('<iframe src="'+r+'" name="iframe-transport-'+t+'"></iframe>').bind("load",function(){var t,o=e.isArray(n.paramName)?n.paramName:[n.paramName];s.unbind("load").bind("load",function(){var t;try{t=s.contents();if(!t.length||!t[0].firstChild){throw new Error}}catch(n){t=undefined}a(200,"success",{iframe:t});e('<iframe src="'+r+'"></iframe>').appendTo(i);window.setTimeout(function(){i.remove()},0)});i.prop("target",s.prop("name")).prop("action",n.url).prop("method",n.type);if(n.formData){e.each(n.formData,function(t,n){e('<input type="hidden"/>').prop("name",n.name).val(n.value).appendTo(i)})}if(n.fileInput&&n.fileInput.length&&n.type==="POST"){t=n.fileInput.clone();n.fileInput.after(function(e){return t[e]});if(n.paramName){n.fileInput.each(function(t){e(this).prop("name",o[t]||n.paramName)})}i.append(n.fileInput).prop("enctype","multipart/form-data").prop("encoding","multipart/form-data");n.fileInput.removeAttr("form")}i.submit();if(t&&t.length){n.fileInput.each(function(n,r){var i=e(t[n]);e(r).prop("name",i.prop("name")).attr("form",i.attr("form"));i.replaceWith(r)})}});i.append(s).appendTo(document.body)},abort:function(){if(s){s.unbind("load").prop("src",r)}if(i){i.remove()}}}}});e.ajaxSetup({converters:{"iframe text":function(t){return t&&e(t[0].body).text()},"iframe json":function(t){return t&&e.parseJSON(e(t[0].body).text())},"iframe html":function(t){return t&&e(t[0].body).html()},"iframe xml":function(t){var n=t&&t[0];return n&&e.isXMLDoc(n)?n:e.parseXML(n.XMLDocument&&n.XMLDocument.xml||e(n.body).html())},"iframe script":function(t){return t&&e.globalEval(e(t[0].body).text())}}})});(function(e){"use strict";if(typeof define==="function"&&define.amd){define(["jquery","jquery.ui.widget"],e)}else{e(window.jQuery)}})(function(e){"use strict";e.support.fileInput=!((new RegExp("(Android (1\\.[0156]|2\\.[01]))"+"|(Windows Phone (OS 7|8\\.0))|(XBLWP)|(ZuneWP)|(WPDesktop)"+"|(w(eb)?OSBrowser)|(webOS)"+"|(Kindle/(1\\.0|2\\.[05]|3\\.0))")).test(window.navigator.userAgent)||e('<input type="file">').prop("disabled"));e.support.xhrFileUpload=!!(window.ProgressEvent&&window.FileReader);e.support.xhrFormDataFileUpload=!!window.FormData;e.support.blobSlice=window.Blob&&(Blob.prototype.slice||Blob.prototype.webkitSlice||Blob.prototype.mozSlice);e.widget("blueimp.fileupload",{options:{dropZone:e(document),pasteZone:e(document),fileInput:undefined,replaceFileInput:true,paramName:undefined,singleFileUploads:true,limitMultiFileUploads:undefined,limitMultiFileUploadSize:undefined,limitMultiFileUploadSizeOverhead:512,sequentialUploads:false,limitConcurrentUploads:undefined,forceIframeTransport:false,redirect:undefined,redirectParamName:undefined,postMessage:undefined,multipart:true,maxChunkSize:undefined,uploadedBytes:undefined,recalculateProgress:true,progressInterval:100,bitrateInterval:500,autoUpload:true,messages:{uploadedBytes:"Uploaded bytes exceed file size"},i18n:function(t,n){t=this.messages[t]||t.toString();if(n){e.each(n,function(e,n){t=t.replace("{"+e+"}",n)})}return t},formData:function(e){return e.serializeArray()},add:function(t,n){if(t.isDefaultPrevented()){return false}if(n.autoUpload||n.autoUpload!==false&&e(this).fileupload("option","autoUpload")){n.process().done(function(){n.submit()})}},processData:false,contentType:false,cache:false},_specialOptions:["fileInput","dropZone","pasteZone","multipart","forceIframeTransport"],_blobSlice:e.support.blobSlice&&function(){var e=this.slice||this.webkitSlice||this.mozSlice;return e.apply(this,arguments)},_BitrateTimer:function(){this.timestamp=Date.now?Date.now():(new Date).getTime();this.loaded=0;this.bitrate=0;this.getBitrate=function(e,t,n){var r=e-this.timestamp;if(!this.bitrate||!n||r>n){this.bitrate=(t-this.loaded)*(1e3/r)*8;this.loaded=t;this.timestamp=e}return this.bitrate}},_isXHRUpload:function(t){return!t.forceIframeTransport&&(!t.multipart&&e.support.xhrFileUpload||e.support.xhrFormDataFileUpload)},_getFormData:function(t){var n;if(e.type(t.formData)==="function"){return t.formData(t.form)}if(e.isArray(t.formData)){return t.formData}if(e.type(t.formData)==="object"){n=[];e.each(t.formData,function(e,t){n.push({name:e,value:t})});return n}return[]},_getTotal:function(t){var n=0;e.each(t,function(e,t){n+=t.size||1});return n},_initProgressObject:function(t){var n={loaded:0,total:0,bitrate:0};if(t._progress){e.extend(t._progress,n)}else{t._progress=n}},_initResponseObject:function(e){var t;if(e._response){for(t in e._response){if(e._response.hasOwnProperty(t)){delete e._response[t]}}}else{e._response={}}},_onProgress:function(t,n){if(t.lengthComputable){var r=Date.now?Date.now():(new Date).getTime(),i;if(n._time&&n.progressInterval&&r-n._time<n.progressInterval&&t.loaded!==t.total){return}n._time=r;i=Math.floor(t.loaded/t.total*(n.chunkSize||n._progress.total))+(n.uploadedBytes||0);this._progress.loaded+=i-n._progress.loaded;this._progress.bitrate=this._bitrateTimer.getBitrate(r,this._progress.loaded,n.bitrateInterval);n._progress.loaded=n.loaded=i;n._progress.bitrate=n.bitrate=n._bitrateTimer.getBitrate(r,i,n.bitrateInterval);this._trigger("progress",e.Event("progress",{delegatedEvent:t}),n);this._trigger("progressall",e.Event("progressall",{delegatedEvent:t}),this._progress)}},_initProgressListener:function(t){var n=this,r=t.xhr?t.xhr():e.ajaxSettings.xhr();if(r.upload){e(r.upload).bind("progress",function(e){var r=e.originalEvent;e.lengthComputable=r.lengthComputable;e.loaded=r.loaded;e.total=r.total;n._onProgress(e,t)});t.xhr=function(){return r}}},_isInstanceOf:function(e,t){return Object.prototype.toString.call(t)==="[object "+e+"]"},_initXHRData:function(t){var n=this,r,i=t.files[0],s=t.multipart||!e.support.xhrFileUpload,o=e.type(t.paramName)==="array"?t.paramName[0]:t.paramName;t.headers=e.extend({},t.headers);if(t.contentRange){t.headers["Content-Range"]=t.contentRange}if(!s||t.blob||!this._isInstanceOf("File",i)){t.headers["Content-Disposition"]='attachment; filename="'+encodeURI(i.name)+'"'}if(!s){t.contentType=i.type||"application/octet-stream";t.data=t.blob||i}else if(e.support.xhrFormDataFileUpload){if(t.postMessage){r=this._getFormData(t);if(t.blob){r.push({name:o,value:t.blob})}else{e.each(t.files,function(n,i){r.push({name:e.type(t.paramName)==="array"&&t.paramName[n]||o,value:i})})}}else{if(n._isInstanceOf("FormData",t.formData)){r=t.formData}else{r=new FormData;e.each(this._getFormData(t),function(e,t){r.append(t.name,t.value)})}if(t.blob){r.append(o,t.blob,i.name)}else{e.each(t.files,function(i,s){if(n._isInstanceOf("File",s)||n._isInstanceOf("Blob",s)){r.append(e.type(t.paramName)==="array"&&t.paramName[i]||o,s,s.uploadName||s.name)}})}}t.data=r}t.blob=null},_initIframeSettings:function(t){var n=e("<a></a>").prop("href",t.url).prop("host");t.dataType="iframe "+(t.dataType||"");t.formData=this._getFormData(t);if(t.redirect&&n&&n!==location.host){t.formData.push({name:t.redirectParamName||"redirect",value:t.redirect})}},_initDataSettings:function(e){if(this._isXHRUpload(e)){if(!this._chunkedUpload(e,true)){if(!e.data){this._initXHRData(e)}this._initProgressListener(e)}if(e.postMessage){e.dataType="postmessage "+(e.dataType||"")}}else{this._initIframeSettings(e)}},_getParamName:function(t){var n=e(t.fileInput),r=t.paramName;if(!r){r=[];n.each(function(){var t=e(this),n=t.prop("name")||"files[]",i=(t.prop("files")||[1]).length;while(i){r.push(n);i-=1}});if(!r.length){r=[n.prop("name")||"files[]"]}}else if(!e.isArray(r)){r=[r]}return r},_initFormSettings:function(t){if(!t.form||!t.form.length){t.form=e(t.fileInput.prop("form"));if(!t.form.length){t.form=e(this.options.fileInput.prop("form"))}}t.paramName=this._getParamName(t);if(!t.url){t.url=t.form.prop("action")||location.href}t.type=(t.type||e.type(t.form.prop("method"))==="string"&&t.form.prop("method")||"").toUpperCase();if(t.type!=="POST"&&t.type!=="PUT"&&t.type!=="PATCH"){t.type="POST"}if(!t.formAcceptCharset){t.formAcceptCharset=t.form.attr("accept-charset")}},_getAJAXSettings:function(t){var n=e.extend({},this.options,t);this._initFormSettings(n);this._initDataSettings(n);return n},_getDeferredState:function(e){if(e.state){return e.state()}if(e.isResolved()){return"resolved"}if(e.isRejected()){return"rejected"}return"pending"},_enhancePromise:function(e){e.success=e.done;e.error=e.fail;e.complete=e.always;return e},_getXHRPromise:function(t,n,r){var i=e.Deferred(),s=i.promise();n=n||this.options.context||s;if(t===true){i.resolveWith(n,r)}else if(t===false){i.rejectWith(n,r)}s.abort=i.promise;return this._enhancePromise(s)},_addConvenienceMethods:function(t,n){var r=this,i=function(t){return e.Deferred().resolveWith(r,t).promise()};n.process=function(t,s){if(t||s){n._processQueue=this._processQueue=(this._processQueue||i([this])).pipe(function(){if(n.errorThrown){return e.Deferred().rejectWith(r,[n]).promise()}return i(arguments)}).pipe(t,s)}return this._processQueue||i([this])};n.submit=function(){if(this.state()!=="pending"){n.jqXHR=this.jqXHR=r._trigger("submit",e.Event("submit",{delegatedEvent:t}),this)!==false&&r._onSend(t,this)}return this.jqXHR||r._getXHRPromise()};n.abort=function(){if(this.jqXHR){return this.jqXHR.abort()}this.errorThrown="abort";r._trigger("fail",null,this);return r._getXHRPromise(false)};n.state=function(){if(this.jqXHR){return r._getDeferredState(this.jqXHR)}if(this._processQueue){return r._getDeferredState(this._processQueue)}};n.processing=function(){return!this.jqXHR&&this._processQueue&&r._getDeferredState(this._processQueue)==="pending"};n.progress=function(){return this._progress};n.response=function(){return this._response}},_getUploadedBytes:function(e){var t=e.getResponseHeader("Range"),n=t&&t.split("-"),r=n&&n.length>1&&parseInt(n[1],10);return r&&r+1},_chunkedUpload:function(t,n){t.uploadedBytes=t.uploadedBytes||0;var r=this,i=t.files[0],s=i.size,o=t.uploadedBytes,u=t.maxChunkSize||s,a=this._blobSlice,f=e.Deferred(),l=f.promise(),c,h;if(!(this._isXHRUpload(t)&&a&&(o||u<s))||t.data){return false}if(n){return true}if(o>=s){i.error=t.i18n("uploadedBytes");return this._getXHRPromise(false,t.context,[null,"error",i.error])}h=function(){var n=e.extend({},t),l=n._progress.loaded;n.blob=a.call(i,o,o+u,i.type);n.chunkSize=n.blob.size;n.contentRange="bytes "+o+"-"+(o+n.chunkSize-1)+"/"+s;r._initXHRData(n);r._initProgressListener(n);c=(r._trigger("chunksend",null,n)!==false&&e.ajax(n)||r._getXHRPromise(false,n.context)).done(function(i,u,a){o=r._getUploadedBytes(a)||o+n.chunkSize;if(l+n.chunkSize-n._progress.loaded){r._onProgress(e.Event("progress",{lengthComputable:true,loaded:o-n.uploadedBytes,total:o-n.uploadedBytes}),n)}t.uploadedBytes=n.uploadedBytes=o;n.result=i;n.textStatus=u;n.jqXHR=a;r._trigger("chunkdone",null,n);r._trigger("chunkalways",null,n);if(o<s){h()}else{f.resolveWith(n.context,[i,u,a])}}).fail(function(e,t,i){n.jqXHR=e;n.textStatus=t;n.errorThrown=i;r._trigger("chunkfail",null,n);r._trigger("chunkalways",null,n);f.rejectWith(n.context,[e,t,i])})};this._enhancePromise(l);l.abort=function(){return c.abort()};h();return l},_beforeSend:function(e,t){if(this._active===0){this._trigger("start");this._bitrateTimer=new this._BitrateTimer;this._progress.loaded=this._progress.total=0;this._progress.bitrate=0}this._initResponseObject(t);this._initProgressObject(t);t._progress.loaded=t.loaded=t.uploadedBytes||0;t._progress.total=t.total=this._getTotal(t.files)||1;t._progress.bitrate=t.bitrate=0;this._active+=1;this._progress.loaded+=t.loaded;this._progress.total+=t.total},_onDone:function(t,n,r,i){var s=i._progress.total,o=i._response;if(i._progress.loaded<s){this._onProgress(e.Event("progress",{lengthComputable:true,loaded:s,total:s}),i)}o.result=i.result=t;o.textStatus=i.textStatus=n;o.jqXHR=i.jqXHR=r;this._trigger("done",null,i)},_onFail:function(e,t,n,r){var i=r._response;if(r.recalculateProgress){this._progress.loaded-=r._progress.loaded;this._progress.total-=r._progress.total}i.jqXHR=r.jqXHR=e;i.textStatus=r.textStatus=t;i.errorThrown=r.errorThrown=n;this._trigger("fail",null,r)},_onAlways:function(e,t,n,r){this._trigger("always",null,r)},_onSend:function(t,n){if(!n.submit){this._addConvenienceMethods(t,n)}var r=this,i,s,o,u,a=r._getAJAXSettings(n),f=function(){r._sending+=1;a._bitrateTimer=new r._BitrateTimer;i=i||((s||r._trigger("send",e.Event("send",{delegatedEvent:t}),a)===false)&&r._getXHRPromise(false,a.context,s)||r._chunkedUpload(a)||e.ajax(a)).done(function(e,t,n){r._onDone(e,t,n,a)}).fail(function(e,t,n){r._onFail(e,t,n,a)}).always(function(e,t,n){r._onAlways(e,t,n,a);r._sending-=1;r._active-=1;if(a.limitConcurrentUploads&&a.limitConcurrentUploads>r._sending){var i=r._slots.shift();while(i){if(r._getDeferredState(i)==="pending"){i.resolve();break}i=r._slots.shift()}}if(r._active===0){r._trigger("stop")}});return i};this._beforeSend(t,a);if(this.options.sequentialUploads||this.options.limitConcurrentUploads&&this.options.limitConcurrentUploads<=this._sending){if(this.options.limitConcurrentUploads>1){o=e.Deferred();this._slots.push(o);u=o.pipe(f)}else{this._sequence=this._sequence.pipe(f,f);u=this._sequence}u.abort=function(){s=[undefined,"abort","abort"];if(!i){if(o){o.rejectWith(a.context,s)}return f()}return i.abort()};return this._enhancePromise(u)}return f()},_onAdd:function(t,n){var r=this,i=true,s=e.extend({},this.options,n),o=n.files,u=o.length,a=s.limitMultiFileUploads,f=s.limitMultiFileUploadSize,l=s.limitMultiFileUploadSizeOverhead,c=0,h=this._getParamName(s),p,d,v,m,g=0;if(f&&(!u||o[0].size===undefined)){f=undefined}if(!(s.singleFileUploads||a||f)||!this._isXHRUpload(s)){v=[o];p=[h]}else if(!(s.singleFileUploads||f)&&a){v=[];p=[];for(m=0;m<u;m+=a){v.push(o.slice(m,m+a));d=h.slice(m,m+a);if(!d.length){d=h}p.push(d)}}else if(!s.singleFileUploads&&f){v=[];p=[];for(m=0;m<u;m=m+1){c+=o[m].size+l;if(m+1===u||c+o[m+1].size+l>f||a&&m+1-g>=a){v.push(o.slice(g,m+1));d=h.slice(g,m+1);if(!d.length){d=h}p.push(d);g=m+1;c=0}}}else{p=h}n.originalFiles=o;e.each(v||o,function(s,o){var u=e.extend({},n);u.files=v?o:[o];u.paramName=p[s];r._initResponseObject(u);r._initProgressObject(u);r._addConvenienceMethods(t,u);i=r._trigger("add",e.Event("add",{delegatedEvent:t}),u);return i});return i},_replaceFileInput:function(t){var n=t.clone(true);e("<form></form>").append(n)[0].reset();t.after(n).detach();e.cleanData(t.unbind("remove"));this.options.fileInput=this.options.fileInput.map(function(e,r){if(r===t[0]){return n[0]}return r});if(t[0]===this.element[0]){this.element=n}},_handleFileTreeEntry:function(t,n){var r=this,i=e.Deferred(),s=function(e){if(e&&!e.entry){e.entry=t}i.resolve([e])},o;n=n||"";if(t.isFile){if(t._file){t._file.relativePath=n;i.resolve(t._file)}else{t.file(function(e){e.relativePath=n;i.resolve(e)},s)}}else if(t.isDirectory){o=t.createReader();o.readEntries(function(e){r._handleFileTreeEntries(e,n+t.name+"/").done(function(e){i.resolve(e)}).fail(s)},s)}else{i.resolve([])}return i.promise()},_handleFileTreeEntries:function(t,n){var r=this;return e.when.apply(e,e.map(t,function(e){return r._handleFileTreeEntry(e,n)})).pipe(function(){return Array.prototype.concat.apply([],arguments)})},_getDroppedFiles:function(t){t=t||{};var n=t.items;if(n&&n.length&&(n[0].webkitGetAsEntry||n[0].getAsEntry)){return this._handleFileTreeEntries(e.map(n,function(e){var t;if(e.webkitGetAsEntry){t=e.webkitGetAsEntry();if(t){t._file=e.getAsFile()}return t}return e.getAsEntry()}))}return e.Deferred().resolve(e.makeArray(t.files)).promise()},_getSingleFileInputFiles:function(t){t=e(t);var n=t.prop("webkitEntries")||t.prop("entries"),r,i;if(n&&n.length){return this._handleFileTreeEntries(n)}r=e.makeArray(t.prop("files"));if(!r.length){i=t.prop("value");if(!i){return e.Deferred().resolve([]).promise()}r=[{name:i.replace(/^.*\\/,"")}]}else if(r[0].name===undefined&&r[0].fileName){e.each(r,function(e,t){t.name=t.fileName;t.size=t.fileSize})}return e.Deferred().resolve(r).promise()},_getFileInputFiles:function(t){if(!(t instanceof e)||t.length===1){return this._getSingleFileInputFiles(t)}return e.when.apply(e,e.map(t,this._getSingleFileInputFiles)).pipe(function(){return Array.prototype.concat.apply([],arguments)})},_onChange:function(t){var n=this,r={fileInput:e(t.target),form:e(t.target.form)};this._getFileInputFiles(r.fileInput).always(function(i){r.files=i;if(n.options.replaceFileInput){n._replaceFileInput(r.fileInput)}if(n._trigger("change",e.Event("change",{delegatedEvent:t}),r)!==false){n._onAdd(t,r)}})},_onPaste:function(t){var n=t.originalEvent&&t.originalEvent.clipboardData&&t.originalEvent.clipboardData.items,r={files:[]};if(n&&n.length){e.each(n,function(e,t){var n=t.getAsFile&&t.getAsFile();if(n){r.files.push(n)}});if(this._trigger("paste",e.Event("paste",{delegatedEvent:t}),r)!==false){this._onAdd(t,r)}}},_onDrop:function(t){t.dataTransfer=t.originalEvent&&t.originalEvent.dataTransfer;var n=this,r=t.dataTransfer,i={};if(r&&r.files&&r.files.length){t.preventDefault();this._getDroppedFiles(r).always(function(r){i.files=r;if(n._trigger("drop",e.Event("drop",{delegatedEvent:t}),i)!==false){n._onAdd(t,i)}})}},_onDragOver:function(t){t.dataTransfer=t.originalEvent&&t.originalEvent.dataTransfer;var n=t.dataTransfer;if(n&&e.inArray("Files",n.types)!==-1&&this._trigger("dragover",e.Event("dragover",{delegatedEvent:t}))!==false){t.preventDefault();n.dropEffect="copy"}},_initEventHandlers:function(){if(this._isXHRUpload(this.options)){this._on(this.options.dropZone,{dragover:this._onDragOver,drop:this._onDrop});this._on(this.options.pasteZone,{paste:this._onPaste})}if(e.support.fileInput){this._on(this.options.fileInput,{change:this._onChange})}},_destroyEventHandlers:function(){this._off(this.options.dropZone,"dragover drop");this._off(this.options.pasteZone,"paste");this._off(this.options.fileInput,"change")},_setOption:function(t,n){var r=e.inArray(t,this._specialOptions)!==-1;if(r){this._destroyEventHandlers()}this._super(t,n);if(r){this._initSpecialOptions();this._initEventHandlers()}},_initSpecialOptions:function(){var t=this.options;if(t.fileInput===undefined){t.fileInput=this.element.is('input[type="file"]')?this.element:this.element.find('input[type="file"]')}else if(!(t.fileInput instanceof e)){t.fileInput=e(t.fileInput)}if(!(t.dropZone instanceof e)){t.dropZone=e(t.dropZone)}if(!(t.pasteZone instanceof e)){t.pasteZone=e(t.pasteZone)}},_getRegExp:function(e){var t=e.split("/"),n=t.pop();t.shift();return new RegExp(t.join("/"),n)},_isRegExpOption:function(t,n){return t!=="url"&&e.type(n)==="string"&&/^\/.*\/[igm]{0,3}$/.test(n)},_initDataAttributes:function(){var t=this,n=this.options,r=e(this.element[0].cloneNode(false));e.each(r.data(),function(e,i){var s="data-"+e.replace(/([a-z])([A-Z])/g,"$1-$2").toLowerCase();if(r.attr(s)){if(t._isRegExpOption(e,i)){i=t._getRegExp(i)}n[e]=i}})},_create:function(){this._initDataAttributes();this._initSpecialOptions();this._slots=[];this._sequence=this._getXHRPromise(true);this._sending=this._active=0;this._initProgressObject(this);this._initEventHandlers()},active:function(){return this._active},progress:function(){return this._progress},add:function(t){var n=this;if(!t||this.options.disabled){return}if(t.fileInput&&!t.files){this._getFileInputFiles(t.fileInput).always(function(e){t.files=e;n._onAdd(null,t)})}else{t.files=e.makeArray(t.files);this._onAdd(null,t)}},send:function(t){if(t&&!this.options.disabled){if(t.fileInput&&!t.files){var n=this,r=e.Deferred(),i=r.promise(),s,o;i.abort=function(){o=true;if(s){return s.abort()}r.reject(null,"abort","abort");return i};this._getFileInputFiles(t.fileInput).always(function(e){if(o){return}if(!e.length){r.reject();return}t.files=e;s=n._onSend(null,t).then(function(e,t,n){r.resolve(e,t,n)},function(e,t,n){r.reject(e,t,n)})});return this._enhancePromise(i)}t.files=e.makeArray(t.files);if(t.files.length){return this._onSend(null,t)}}return this._getXHRPromise(false,t&&t.context)}})})