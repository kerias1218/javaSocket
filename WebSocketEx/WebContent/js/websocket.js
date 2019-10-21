/**
 * 유진선물 트레이딩 커뮤니티 소켓관련 기능
 * coForward
 * create : 2019.10.14
 * last modified : 2019.10.14 
 */

jQuery(function () {
    CF_Socket.init();
})

var SOCKET = false;

var CF_Socket = {
    init: function () { //웹소켓 연결 초기화
        console.log(`==WebSocket.init()==`);
        SOCKET = new WebSocket(SOCKET_URL);
        SOCKET.onopen = function (message) { CF_Socket.open(message) }
        SOCKET.onclose = function (message) { CF_Socket.close(message) }
        SOCKET.onerror = function (message) { CF_Socket.error(message) }
        SOCKET.onmessage = function (message) { CF_Socket.recive(message) }
    },
    open: function (message) { //웹소켓 연결시
        console.log(`==CF_Socket.open()==`);
        console.log(message);
        var userId = jQuery('.PAGE .user_profile').attr('data-userId');

        var init_message = {
            function: "Open",
            method: "init",
            data: {
                userId: userId,
            }
        };
        this.send(init_message);
        // SIDE_BAR.init();
    },
    close: function (message) { //웹소켓이 끊어졌을시
        console.log(`==CF_Socket.close()==`);
        console.log(message);
    },
    error: function (message) {// 에러 시
        console.log(`==CF_Socket.error()==`);
        console.log(message);
    },
    recive: function (message) {// 메세지 수신시
        console.log(`==CF_Socket.recive()==`);
        var recive_data = JSON.parse(message.data);
        console.log(recive_data);
        if (window[recive_data.function][recive_data.method]) {
            window[recive_data.function][recive_data.method](recive_data.data);
        } else {
            //실행 기능 미등록 시 메세지
        }
    },
    send: function (message_json) {
        console.log(`==CF_Socket.send()==`);
        console.log(message_json);

        var message = JSON.stringify(message_json);
        SOCKET.send(message);
    }
}//End of {CF_Socket}




