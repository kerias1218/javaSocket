/**
 * 유진선물 트레이딩 커뮤니티  기능 초기화
 * coForward
 * create : 2019.10.21
 * last modified : 2019.10.21 
 */
var Open = {
    init: function (data) {
        console.log('==Open.init()==')
        console.log(data);
        jQuery('#current_user').attr('data-socket', data.socketId);
        Side_Bar.init();
    }
}