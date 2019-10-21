/**
 * 유진선물 트레이딩 커뮤니티 사이드바 기능
 * coForward
 * create : 2019.10.14
 * last modified : 2019.10.14 
 */
var Side_Bar = {
    section: {
        'Chatting': 'Chat',
    },
    init: function (section) {
        var select_section = (!section) ? 'Chatting' : section;
        window[this.section[select_section]].init();
    },
    title: function (content) { //사이드바의 타이틀 변경
        jQuery('.SIDE_BAR .title_bar h1').html(content);
    },
    sub_title: function (content) {//사이드 바의 서브타이틀 변경
        jQuery('.SIDE_BAR .title_bar h2').html(content);
    },
    desc: function (content) {//사이드바 설 명문 변경
        jQuery('.SIDE_BAR .title_bar p').html(content);
    },
}//End of {SIDE_BAR} 