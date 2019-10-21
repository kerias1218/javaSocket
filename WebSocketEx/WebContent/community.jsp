<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>

<%
request.setCharacterEncoding("UTF-8");


/*
	//out.println("<h1>MySQL DB 연결 성공</h1>");

	PreparedStatement pstmt = null;
	ResultSet rs = null;
*/	
	
	String userId = request.getParameter("userid").trim();
	String userPw = request.getParameter("pw").trim();
	
	//out.println("id:"+nickName);

/*
	pstmt = conn.prepareStatement("select * from user where userid='"+userId+"'");

	rs = pstmt.executeQuery();
	
	//out.println("<h1>-------------</h1>");

	if(rs.next()) {
		//out.println(rs.getString("nickname"));
		//out.println(rs.getString("profile"));
		//out.println(rs.getString("is_login"));
	}
*/
	
	/*
	String sql = "update user set is_login=?, socket=? where nickname=?";
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, 1); 
	pstmt.setString(2, "a@df@fdv1");
	pstmt.setString(3, userId);

	int result = pstmt.executeUpdate();
	System.out.println(result + "<-- result:"+userId);
	*/
	
	
	
	
	
	

	session.setAttribute("userId", userId); //세션등록 
	String sessionWebId = (String)session.getAttribute("userId"); //세션얻기  
	//out.println("<br>세션아이디:"+sessionWebId);
	//session.invalidate(); //모든 세션 삭제 
	
	
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>



<!DOCTYPE html>
<html class="no-js">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Layout</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="css/style.css" type="text/css" />
</head>

<body>

    <!-- 
    <script>
        var userId = "<%=userId%>";
    </script>
	-->

    <section class="PAGE" data-side="open">
        <header class="PAGE_HEADER">
            <h1>유진투자선물 트레이딩커뮤니티</h1>
        </header>
        <nav class="NAVI">
            <div id="current_user" class="user_profile" data-userId="<%=sessionWebId%>" data-socket="">
                <img src="" alt="" />
                <span class="userId"><%=userId%></span>
            </div>
        </nav>
        <main class="PAGE_MAIN">
            <div class="window_row one">
                <div class="window one">

                    <% out.println("세션아이디:"+userId); %>

                </div>
                <div class="window two"></div>
            </div>
            <div class="window_row two">
                <div class="window three"></div>
                <div class="window four"></div>
            </div>
        </main>
        <div class="SIDE_BAR">
            <section id="Chatting">
                <header class="title_bar">
                    <h1>커뮤니티 체팅</h1>
                    <h2>체팅방 리스트</h2>
                    <p class="title_desc"><span class="cnt">0</span>개의 체팅방이 있습니다.</p>
                </header>

                <div id="Chatting_form_div" class="Chatting_div">
                    <form id="Chatting_reg_form" class="normal_form">
                        <div class="form_item">
                            <label for="Chatting_reg_title">제목</label>
                            <input type="text" name="title" value="room_7" id="Chatting_reg_title"
                                placeholder="체팅방 제목을 입력하세요" required="required" />
                        </div>
                        <div class="form_item">
                            <label for="Chatting_reg_desc">설명</label>
                            <textarea id="Chatting_reg_desc" name="desc"
                                placeholder="체팅방에 대한 설명을 입력하세요">room7_desc</textarea>
                        </div>
                        <div class="btn_div">
                            <button type="submit" class="btn_Chatting_reg_submit">확 인</button>
                            <button type="button" class="btn_Chatting_reg_cancel btn_cancel">취 소</button>
                        </div>
                    </form>
                </div><!-- End of #Chatting_form_div-->

                <div id="Chatting_list_div" class="Chatting_div">
                    <ul class="Chatting_list">

                    </ul>

                    <div class="desc nodata">
                        아직 개설된 체팅방이 없습니다.
                    </div>


                    <div class="btn_div">
                        <button type="button" class="btn_open_chatting">체팅방 개설하기</button>
                    </div>
                </div><!-- End of #Chatting_list-->

                <div id="Chatting_room_div" class="Chatting_div" data-room_idx="">
                    <div class="Chatting_room_content_warper">
                        <div class="chatting_room_content">

                        </div>
                    </div>
                    <div class="Chatting_send_form_div">
                        <form class="Chatting_send_form">
                            <input type="text" id="Chatting_send_message" placeholder="메세지를 입력하세요" />
                            <button type="submit" class="btn_Chatting_send_submit">확인</button>
                        </form>
                    </div>
                </div><!-- End of #Chatting_room_div -->
            </section>
        </div>
        <footer class="PAGE_FOOTER">
            <span class="copyright">Copyright 2019 EUGENE INVESTMENT & FUTRUES Co. Ltd All right reserved.</span>
        </footer>

    </section>
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/common.js" type="text/javascript"></script>
    <script src="js/websocketUrl.js" type="text/javascript"></script>
    <script src="js/websocket.js" type="text/javascript"></script>
    <script src="js/init.js" type="text/javascript"></script>
    <script src="js/layout.js" type="text/javascript"></script>
    <script src="js/side_bar.js" type="text/javascript"></script>
    <script src="js/chatting.js" type="text/javascript"></script>






    <script>
        $("#roomCreate").click(function () {
            var chatting_reg_title = $("#Chatting_reg_title").val();
            var chatting_reg_desc = $("#Chatting_reg_desc").val();


            var j = {
                function: "Chat",
                method: "create",
                data: {
                    userId: userId,
                    title: chatting_reg_title,
                    desc: chatting_reg_desc,
                    owner: userId,
                }
            };

            var jsonMsg = JSON.stringify(j);
            SOCKET.send(jsonMsg);
        });

        // 채팅방 참여 
        var join = function (roomId) {
            var j = {
                function: "Chat",
                method: "join",
                data: {
                    userId: userId,
                    roomId: roomId,
                }
            };

            var jsonMsg = JSON.stringify(j);
            SOCKET.send(jsonMsg);
        }

        // 채팅방 나가기
        var leave = function (roomId) {
            var j = {
                function: "Chat",
                method: "leave",
                data: {
                    userId: userId,
                    roomId: roomId,
                }
            };

            var jsonMsg = JSON.stringify(j);
            SOCKET.send(jsonMsg);
        }

    </script>


</body>

</html>