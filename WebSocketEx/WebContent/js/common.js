// JavaScript Document
// 공통 사용 코드 
var commen = {}

/**
 * 필수 입력 서식 체크 
 */
commen.form_check = function (form) {
	//필수 입력 항목 검증 
	var required = jQuery(form).find('*[required="required"]');
	for (var i = 0; i < required.length; i++) {
		var input = jQuery(required[i])
		var input_value = input.val();
		if (input_value.length < 2) {
			var label = jQuery(user_base.reg_form_id + ' label[for="' + input.attr('id') + '"]');
			label.find('span').remove();
			var label_caption = label.text();
			alert('"' + label_caption + '"는' + "\n" + '필수 입력 항목 입니다.');
			input.focus();
			return false;
		}
	}//End of for(var i=0;i<required.length;i++) 
	return true;
}//End of commem.form_check;


/**
 * console.log 단축
 */
function v(data) {
	console.log(data);
}//v(data)

/**
 * 공백제거
 */
String.prototype.trim = function () {
	return this.replace(/(^\s*)|(\s*$)/gi, "");
};

/**
 * object의 객체 크기를 반환함
 */
function obj_size(object) {
	var length = 0;
	for (i in object) {
		length++;
	}
	return length;
}//obj_size(object)


/**
 * URL정보를 object로 Return함
 * @string : url - url 경로 stirng 
 * @returns : 분류된 json obejct
 */
function url_parser(url) {
	try {
		var url_temp = url.split('?');
		var path_temp = url_temp[0];
		var query_temp = url_temp[1];
	} catch (e) {
		var path_temp = url;
		var query_temp = false;
	}
	console.log(path_temp);
	var path_temp = path_temp.split('/');
	var file = path_temp.pop();
	var path = path_temp.join('/');
	if (query_temp) {
		var query_array = query_temp.split('&');
		var query = {};
		for (i = 0; i < query_array.length; i++) {
			var item = query_array[i].split('=');
			query[item[0]] = item[1];
		}
	} else {
		var query = false;
	}
	var return_data = { 'path': path, 'file': file, 'query': query };
	return return_data;
}//End of url_parser(url);


/**
 *loading img 표시기 생성 
 */
function get_loading_img(target) {
	console.log('--get_loading_img--');
	if (!target) { target = 'body'; }
	var loading_img = new HTML('div');
	loading_img.id = 'loading_img_div';
	jQuery(target).addClass('loading_img_parent');
	jQuery(target).append(loading_img.toString());
	return false;
}//get_loading_img(target)

/**
 * loading img 표시기 삭제
 */
function remove_loading_img() {
	console.log('--remove_loading_img--');
	var loading_img = jQuery('#loading_img_div');
	var target = loading_img.parent('.loading_img_parent');
	loading_img.remove();
	jQuery(target).removeClass('loading_img_parent');
	return false;
}//remove_loading_img(target)


/**
 * HTML 태그를 위한 기능 생성
 */
function HTML(tag) {
	this.tag = tag;
	return this;
}//HTML(tag)

HTML.prototype.toString = function get_code() {
	var no_end_tag = { 'input': true, 'img': true, 'br': true, 'hr': true };
	var no_end_chek = false;
	var attr_text = '';
	for (attr in this) {
		switch (attr) {
			case 'tag':
				if (this[attr] in no_end_tag) {
					no_end_chek = true;
				}
				break;
			case 'toString':
				break;
			case 'content':
				if (typeof (this[attr]) == 'undefined') {
					var html_content = '';
				} else {
					var html_content = this[attr];
				}
				break;

			case 'html_class':
				attr_text = attr_text + ' ' + 'class' + '="' + this[attr] + '"'
				break;
			case 'html_for':
				attr_text = attr_text + ' ' + 'for' + '="' + this[attr] + '"'
				break;
			case 'html_for':
				attr_text = attr_text + ' ' + 'for' + '="' + this[attr] + '"'
				break;

			default:
				var data_attr = attr.split('data_');
				if (data_attr[1]) {
					attr_text = attr_text + ' data-' + data_attr[1] + '="' + this[attr] + '"';
				} else {
					attr_text = attr_text + ' ' + attr + '="' + this[attr] + '"';
				}
				break;
		}
	}

	if (typeof (html_content) == 'undefined') {
		var html_content = '';
	}

	if (no_end_chek != true) {
		var code = '<' + this.tag + ' ' + attr_text + '>' + html_content + '</' + this.tag + '>';
	} else {
		var code = '<' + this.tag + ' ' + attr_text + ' />';
	}
	return code;
}//HTML.prototype.toString=function get_code()

/**
 * 인풋요소를 위한 객체 생성
 */
function INPUT(type) {
	var code = new HTML('input');
	code.type = type;
	return code;
}//INPUT(type)

/**
 * TEXTAREA을 위한 객체 생성
 */
function TEXTAREA() {
	var code = new HTML('textarea');
	code.cols = 60;
	code.rows = 4;
	code.content = '';
	return code;
}//TEXTAREA()

/**
 * LABEL을 위한 객체 생성
 */
function LABEL(caption, input) {
	var code = new HTML('label');
	code.content = caption;
	if (typeof (input) == 'object') {
		code.html_for = input.id;
		if (input.required == 'required') {
			code.html_class = 'required_label';
			var required_span = new HTML('span');
			required_span.content = '필수';
			code.content += required_span;
		}
	}
	return code;
}//LABEL(caption,input)

/**
 * IMG의 객체 생성
 */
function IMG(src, alt) {
	var code = new HTML('img');
	code.src = src;
	if (alt) {
		code.alt = alt;
	}
	return code;
}//IMG(src,alt)

/**
 * COFORWARD의 이름표시객체 생성
 */
function COFORWARD() {
	var code = new HTML('em');
	code.html_class = 'coforward';

	var em_span = new HTML('span');
	em_span.content = 'Forward';

	code.content = 'co' + em_span;
	return code;
}//COFORWARD()





/**
 * localStorage의 기록및 삭제
 */
var local = {}
local.test = function () {//지원여부 확인
	if (typeof (Storage) === "undefined") {
		var error = '=localStorage를 지원하지 않음=';
		console.log(error);
		return false;
	} else {
		return true;
	}
}//End of local.test;
local.save = function (obj) {//저장하기
	if (!local.test) { return '=localStorage를 지원하지 않음='; }
	for (key in obj) {
		localStorage.setItem(key, obj[key]);
	}
}//End of local.save; 

local.load = function (key) {//키별로 읽기
	var return_obj = {};
	if (!local.test) { return '=localStorage를 지원하지 않음='; }
	if (localStorage[key]) {
		return localStorage[key];
	} else {
		return undefined;
	}
}//End of local.load

local.del = function (key) {//키별로 지우기
	var return_obj = {};
	if (!local.test) { return '=localStorage를 지원하지 않음='; }
	if (localStorage[key]) {
		localStorage.removeItem(key);
	}
}//End of local.del
local.clear = function () {//전체 지우기
	var return_obj = {};
	if (!local.test) { return '=localStorage를 지원하지 않음='; }
	localStorage.clear(key);
}//End of local.clear

/**
 * object관련 기능
 */
var obj = {}
/*객체의 갯수*/
obj.size = function (object) {
	var length = 0;
	for (i in object) {
		length++;
	}
	return length;
}//obj_size(object);

/*값의 존재여부*/
obj.has_key = function (object, key) {
	for (i in object) {
		if (i == key) { return true; }
	}
	return false;
}//obj.has_key;

/*값의 존재여부*/
obj.has_value = function (object, value) {
	for (i in object) {
		if (object[i] == value) { return true; }
	}
	return false;
}//obj.has_value;

/*특정키의 삭제*/
obj.del_key = function (object, key) {
	delete object[key];
}//obj.has_value;


/**
*javascript 동적 로딩
*/
commen.load_js = function (url, callback) {
	if (!url) { return false; }
	var script = document.createElement('script');
	script.type = 'text/javascript'
	script.charset = 'utf-8';
	if (callback) {
		script.onload = function () { callback(); }
	}
	script.src = url
	document.getElementsByTagName('head')[0].appendChild(script);
}//End of load_js(url,callback)

/**
 * Ajax처리를 위한 서식 값의 직열화
 * @param : form - 직열화할 서식요소 
 * @returns - ajax를 위해 직열화된 서식 요소 값
 */
commen.ajax_data = function (form) {
	var data = jQuery(form).serialize();
	data = data + '&ajax=true';
	return data;
}//End of ajax_form(form)

commen.ajax_json = function (form) {
	var data = {};
	var temp = new Array;
	var array_temp = jQuery(form).serializeArray();
	for (var i = 0; i < array_temp.length; i++) {
		temp[i] = '"' + array_temp[i].name + '":"' + array_temp[i].value + '"';
	}
	var json_string = '{' + temp.join(',') + '}';
	var json = jQuery.parseJSON(json_string);
	json.ajax = true;
	return json;
}//end of commen.ajax_json

/**
 * 정렬 가능 리스트의 이동버튼처리 
 */
commen.move_item = function (btn, list_selector, item_selector) {
	var list_selector = list_selector || 'ul';
	var item_selector = item_selector || 'li';

	var list = jQuery(btn).parents(list_selector);
	var list_id = jQuery(list[0]).attr('id');
	var item = jQuery(btn).parents(item_selector);
	jQuery(item[0]).addClass('moving_item');
	var prev_item = jQuery(item[0]).prev(item_selector)[0];
	var next_item = jQuery(item[0]).next(item_selector)[0];

	setTimeout(
		function () {
			if (jQuery(btn).hasClass('up')) {
				console.log('==up==');
				if (prev_item) {
					item.insertBefore(prev_item)
				}
			} else if (jQuery(btn).hasClass('down')) {
				console.log('==down==');
				if (next_item) {
					item.insertAfter(next_item)
				}
			}
			setTimeout(
				function () {
					jQuery(item[0]).removeClass('moving_item');
					jQuery('button[data-rel="' + list_id + '"]').slideDown(300);
				}
				, 500
			);
		}
		, 500
	);

}//End of commen.move_item

/**
 * 구글 맵의 생성
 */
function gmap(map_div_id) {
	var map_center_temp = jQuery('#' + map_div_id).attr('data-lat');
	if (!map_center_temp) {
		var map_lat = 37.57444430149635;
		var map_lng = 126.9767282213013;
	} else {
		var map_lat = jQuery('#' + map_div_id).attr('data-lat');
		var map_lng = jQuery('#' + map_div_id).attr('data-lng');
	}
	var mapOptions = {
		center: new google.maps.LatLng(map_lat, map_lng),
		zoom: 15,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(jQuery('#' + map_div_id)[0], mapOptions);
	map.div_id = map_div_id;
	console.log(map);
	return map
}//gmap(map_div_id)

/**
 * 구글 맵 마커 생성
 */
function set_map_marker(map, no_ani) {
	if (no_ani != false) {
		var marker_animation = google.maps.Animation.DROP;
	} else {
		var marker_animation = false
	}

	var marker = new google.maps.Marker({
		position: map.getCenter(),
		animation: marker_animation,
		icon: '/upload/map/icon/marker.png',
		draggable: false,
		map: map,
		title: ''
	});

	return marker
}//set_map_marker(map,no_ani)



/*공통 서식 form Ajax 처리=======================================================================*/
/* sns 덧글 기능 초기화 */
function ini_reply() {
	console.log('==ini_reply==');
	jQuery('form.reply_form').unbind('submit');
	jQuery('form.reply_form').submit(function () {
		if (!$(this).find('[name=reply_text]').val()) {
			alert('내용을 입력하여 주세요.');
			$(this).find('[name=reply_text]').focus()
			return false;
		}
		if ($(this).find(':input[name="submit_ajax"]').val() == 'true') {
			$.ajax({
				type: "POST",
				url: '/function/form_action.php',
				data: $(this).serialize(),
				cache: false,
				dataType: 'json',
				success: function (data) {
					console.log(data);
					// 댓글 데이터 체크후 생성
					var sns_item_article = jQuery('#' + data.ref + '_' + data.ref_idx);
					console.log(sns_item_article.length);
					if (sns_item_article.length > 0) {
						var no_reply_desc = jQuery('#' + data.ref + '_' + data.ref_idx + ' .no_reply_desc');
						var reply_desc = jQuery('#' + data.ref + '_' + data.ref_idx + ' .reply_desc');
						var list_section = jQuery('#' + data.ref + '_' + data.ref_idx + ' .reply_list_section');
					} else {
						var no_reply_desc = jQuery('.no_reply_desc');
						var reply_desc = jQuery('.reply_desc');
						var list_section = jQuery('.reply_list_section');
					}

					if (no_reply_desc.length > 0) {
						no_reply_desc.removeClass('no_reply_desc').addClass('reply_desc');
						no_reply_desc.html('<span class="reply_cnt">1</span> 개의 덧글이 있습니다.');
					} else {
						var reply_cnt = parseInt(reply_desc.find('.reply_cnt').html()) + 1;
						reply_desc.find('.reply_cnt').html(reply_cnt);
					}
					list_section.find('.reply_form').before(data.code);
					ini_del_idx(); //게시물 삭제
				}
			})
			this.reset();
			return false;
		}
	});

}

/* sns 추천하기 초기화 */
function ini_recommend() {
	console.log('==ini_sns_recommend==');
	jQuery('form.recommend_form').unbind('submit');
	jQuery('form.recommend_form').submit(function () {
		var owner = $(this);
		$.ajax({
			type: "POST",
			url: '/function/form_action.php',
			data: $(this).serialize(),
			cache: false,
			dataType: 'json',
			success: function (data) {
				console.log(data);
				alert(data.msg);
				if (data.locations) {
					location = data.locations;
				}
				if (data.state == true) {
					var sns_block = jQuery('#' + data.ref + '_' + data.idx);
					if (sns_block.length > 0) {
						var cnt_span = jQuery('#' + data.ref + '_' + data.idx + ' .btn_request span');
					} else {
						var cnt_span = jQuery('.btn_request span');
					}
					var old_cnt = cnt_span.text();
					cnt_span.text(parseInt(old_cnt) + 1);
				}
			}
		})
		return false;
	});
}//ini_sns_recommend

/* sns 스크랩하기 초기화 */
function ini_scrap() {
	jQuery('form.scrap_form').unbind('submit');
	jQuery('form.scrap_form').submit(function () {
		var owner = $(this);
		$.ajax({
			type: "POST",
			url: '/function/form_action.php',
			data: $(this).serialize(),
			cache: false,
			dataType: 'json',
			success: function (data) {
				console.log(data);
				alert(data.msg);
				if (data.locations) {
					location = data.locations;
				}
				if (data.state == true) {
					var sns_block = jQuery('#' + data.ref + '_' + data.idx);
					if (sns_block.length > 0) {
						var cnt_span = jQuery('#' + data.ref + '_' + data.idx + ' .btn_scrap span');
					} else {
						var cnt_span = jQuery('.btn_scrap span');
					}
					var old_cnt = cnt_span.text();
					if (data.method == 'reg') {
						jQuery('form.scrap_form input[name="method"]').val('del');
						jQuery('form.scrap_form button[type="submit"]').html('스크랩 해제');
						cnt_span.text(parseInt(old_cnt) + 1);
					} else if (data.method == 'del') {
						jQuery('form.scrap_form input[name="method"]').val('reg');
						cnt_span.text(parseInt(old_cnt) - 1);
						jQuery('form.scrap_form button[type="submit"]').text('스크랩 하기');
					}
					jQuery('form.scrap_form button[type="submit"]').prepend(cnt_span);
				}
			}
		})
		return false;
	});
}//ini_sns_scrap


/*게시물 삭제 ===================================================================================*/
function ini_del_idx() {
	console.log('===ini_del_idx===');
	jQuery('form.del_idx_form').unbind('submit');
	jQuery('form.del_idx_form').bind('submit', function () {

		if (!confirm('이글을 삭제합니다.' + "\n" + '삭제후에는 복구할 수 없습니다.')) {
			return false;
		};
		if ($(this).find(':input[name="submit_ajax"]').val() == 'true') {
			$.ajax({
				type: "POST",
				url: '/function/form_action.php',
				data: $(this).serialize(),
				cache: false,
				dataType: 'json',
				success: function (data) {
					console.log(data);
					var remove_target = jQuery('#' + data.table_name + '_' + data.del_idx);
					if (data.state == true) {
						switch (data.table_name) {
							case 'comment':
								var reply_section = remove_target.parents('.reply_list_section');
								console.log(reply_section.length);
								var reply_cnt = reply_section.find('.reply_cnt');
								if (reply_cnt.text() - 1 < 1) {
									var reply_desc = reply_section.find('.reply_desc');
									reply_desc.addClass('no_reply_desc');
									reply_desc.removeClass('reply_desc');
									reply_desc.text('아직 덧글이 없습니다. 이 글에 가장 먼저 덧글을 써주세요.');
								} else {
									reply_cnt.text(reply_cnt.text() - 1);
								}
								remove_target.remove();
								break;
						}

					}
				}//success: function(data)
			}); // $.ajax({
			return false;
		}
	});
}//ini_del_idx()================================================================================



/**
 * 지역-국가 선택 연동기능
 * @returns {Boolean}
 */
function ini_n_code_select() {
	var area_select = jQuery(jQuery('.ncode_area_select')[0]);
	if (area_select.length < 1) { return false; }
	area_option = jQuery('.ncode_area_select option');

	var nation_select = jQuery(jQuery('.ncode_nation_select')[0]);
	if (nation_select.length < 1) { return false; }
	nation_option = jQuery('.ncode_nation_select option');

	area_select.on('change', function (area_select) {
		var a_code = jQuery(this).val();
		nation_select = jQuery(jQuery('.ncode_nation_select')[0]);
		jQuery('.ncode_nation_select option').remove();

		for (var i = 0; i < nation_option.length; i++) {
			if (!a_code) {
				nation_select.append(nation_option[i]);
			} else if (nation_option[i].className == 'nation_all' || jQuery(nation_option[i]).hasClass(a_code)) {
				nation_select.append(nation_option[i]);
			}
		}

		nation_select.append(nation_option.find('option.nation_all'));
		nation_select.val('nation_all');
	});

	nation_select.on('change', function (nation_select) {
		var n_code = jQuery(this).val();
		var act_option = jQuery(this).find('option[value="' + n_code + '"]');
		var acode_temp = act_option[0].className;
		var acode = acode_temp.split(' ');
		area_select = jQuery(jQuery('.ncode_area_select')[0]);
		area_select.val(acode);
	});
}//End of ini_n_code_select();

/*이미지 상세보기*/
function ini_img_viewer(view_class) {
	//상세보기 링크 기능 초기화
	for (var i = 0; i < view_class.length; i++) {
		jQuery('.' + view_class[i]).bind('click',
			//이미지 보기 영역 생성
			function () {
				if (jQuery('#img_viewer_div').length > 0) { return false; }
				var img_viewer_div = new HTML('div');
				img_viewer_div.id = 'img_viewer_div';
				img_viewer_div.html_class = 'overlay_div';

				var view_fig = new HTML('figure');
				view_fig.style = "margin-top:" + (50 + window.pageYOffset) + "px";
				var large_img = new IMG(jQuery(this).attr('href'));
				large_img.html_class = jQuery(this).attr('data-aspect');
				view_fig.content = large_img;


				if (jQuery(this).attr('data-alt')) {
					var view_figcaption = new HTML('figcaption');
					view_figcaption.content = jQuery(this).attr('data-alt');
					view_fig.content += view_figcaption;
				}

				var btn_close = new HTML('button');
				btn_close.type = 'button';
				btn_close.html_class = 'btn_img_viewr_close';
				btn_close.content = '닫기';


				img_viewer_div.content = view_fig;
				img_viewer_div.content += btn_close;

				jQuery(jQuery('body')[0]).append(img_viewer_div.toString());
				jQuery(jQuery('body')[0]).addClass('loading_img_parent');
				//닫기 버튼 기능 초기화
				jQuery('#img_viewer_div').find('.btn_img_viewr_close').bind('click',
					function () {
						console.log('btn_img_viewr_close');
						jQuery('#img_viewer_div').remove();
						jQuery(jQuery('body')[0]).removeClass('loading_img_parent');
					});
				jQuery('#img_viewer_div').bind('click',
					function () {
						jQuery('#img_viewer_div').remove();
						jQuery(jQuery('body')[0]).removeClass('loading_img_parent');
					});
				return false;
			});
	}
}//ini_img_viewer(view_class)=====================================================================================

/**
 * 확인 레이어를 표시함
 * @param data - 표시할 문장
 */
var alert_box = {
	top: 150,
	id: 'alert_box_div',
	btn_close_text: '닫기!!',
	onclose_function: false
}

alert_box.show = function (data, btn) {
	var btn_activate_alert_box = jQuery(btn);
	var alert_div = new HTML('div');
	alert_div.id = alert_box.id;
	alert_div.html_class = 'overlay_div';
	var content_area = new HTML('div');
	content_area.html_class = 'alert_box_content';
	content_area.tabindex = 0;
	content_area.style = "margin-top:" + (alert_box.top + window.pageYOffset) + "px";

	var btn_div = new HTML('div');
	btn_div.html_class = 'btn_div';
	var botton = new HTML('button');
	botton.type = 'button';
	botton.html_class = 'btn btn_close_alert_box';
	botton.content = this.btn_close_text;
	btn_div.content = botton;
	content_area.content = data + btn_div;
	alert_div.content = content_area;
	jQuery(jQuery('body')[0]).addClass('loading_img_parent');
	jQuery(jQuery('body')[0]).append(alert_div.toString());
	jQuery('.alert_box_content').focus();
	jQuery('.btn_close_alert_box').on('click', function () {
		if (typeof alert_box.onclose_function == 'function') {
			alert_box.onclose_function();
		}
		alert_box.close(btn_activate_alert_box)
	});
	return alert_div.id;
}//End of alert_box(data);

//확인 레이어를 삭제함
alert_box.close = function (next_focuse) {
	jQuery('#' + alert_box.id).remove();
	jQuery(jQuery('body')[0]).removeClass('loading_img_parent');
	if (next_focuse) {
		next_focuse.focus();
	}
}//End of close_alert_box(next_focuse);


var func_alert = function (msg, targetId) {
	alert(msg);
	$('#' + targetId).focus();
	return false;
}