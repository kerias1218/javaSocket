/**
 * 유진선물 트레이딩 커뮤니티 체팅관련 기능
 * coForward
 * create : 2019.10.14
 * last modified : 2019.10.14 
 */

//jQuery(function () { CHAT.init(); });

var Chat = {
    init: function () {// 초기화
        console.log(`==Chat.ini()==`)
        //생성하기 버튼
        jQuery('#Chatting_list_div').on('click', 'button.btn_open_chatting', function () { Chat.reg_form() });
        //체팅방 생성 폼 기능
        jQuery('.PAGE').on('submit', 'form#Chatting_reg_form', function () { return Chat.reg_form_send(this); });
        jQuery('form#Chatting_reg_form').on('click', '.btn_Chatting_reg_cancel', function () { return Chat.create_cancel(this); });
        //리스트 페이지 초기화
        this.get_list_data();
    },
    view_chainge: function (section_id) {
        //초기 체팅리스트 표시
        console.log(`==console.log(${section_id})==`)
        jQuery('#Chatting .Chatting_div').addClass('hidden');
        jQuery(`#${section_id}`).removeClass('hidden');

    },
    get_list_data: function () {
        Side_Bar.sub_title('채팅방 리스트');
        var userId = jQuery('#current_user').attr('data-userid');
        var request_data = {
            function: 'Chat',
            method: 'list',
            data: {
                userId: userId
            }
        }
        CF_Socket.send(request_data);
    },
    list: function (data) {
        console.log('==Chat.list()==');
        var add_list = function (data) {
            console.log(data);
            if (!data) {
                console.log('개설된 방이 없음');
            } else {
                console.log('리스트 출력');
            }
        }
        //리스트를 생성 후 리스트 뷰 활성화 
        jQuery.when(add_list()).done(
            function () {
                Chat.view_chainge('Chatting_list_div');
            }
        )
    },
    reg_form: function () {
        Side_Bar.sub_title('새 채팅방 만들기');
        jQuery('form#Chatting_reg_form')[0].reset();
        this.view_chainge('Chatting_form_div')
    },
    reg_form_send: function (form) {// 방생성
        console.log(`==CHTAT.create==`);
        var data = commen.ajax_json(form);
        var userId = jQuery('#current_user').attr('data-userid');
        var socket_message = {
            function: 'Chat',
            method: 'create',
            data: {
                userId: userId,
                title: data.title,
                desc: data.desc,
                owner: userId
            }
        }
        CF_Socket.send(socket_message);
        return false;
    },
    create: function (data) {
        console.log('==Chat.create()==');
        console.log(data);
    },
    create_cancel: function () {
        jQuery('form#Chatting_reg_form')[0].reset();
        this.get_list_data();
    },
    join: function () {// 방 가입

    },
    out: function () {// 방 나가기

    },
    send: function () {//메세지 보내기

    },
    receive: function () {//메세지 수신

    }

}