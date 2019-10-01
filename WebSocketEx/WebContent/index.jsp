<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat Test</title>


<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>

</head>

<body>

<!-- 메시지 표시 영역 -->
<div>
	<textarea id="messageTextArea" readonly="readonly" rows="10" cols="45"></textarea>
</div>

<select id="sel"></select>

<!-- 송신 메시지 텍스트박스 -->
<input type="text" id="messageText" size="50" />

<!-- 송신 버튼 -->
<input type="button" value="Send" onclick="sendMessage()" />


<script type="text/javascript">


//웹소켓 초기화
var webSocket = new WebSocket("ws://localhost:8080/WebSocketEx/broadsocket");
var messageTextArea = document.getElementById("messageTextArea");

//웹 소켓이 연결되었을 때 호출되는 이벤트
webSocket.onopen = function(message){
	messageTextArea.value += "Server connect...\n";
};

//웹 소켓이 닫혔을 때 호출되는 이벤트
webSocket.onclose = function(message){
	messageTextArea.value += "Server Disconnect...\n";
};

//웹 소켓이 에러가 났을 때 호출되는 이벤트
webSocket.onerror = function(message){
	messageTextArea.value += "error...\n";
};


//메시지가 오면 messageTextArea요소에 메시지를 추가한다.
webSocket.onmessage = function processMessge(message){
	
	//Json 풀기
	var jsonData = JSON.parse(message.data);
	console.log(jsonData);
	
		if(jsonData.message != null) {
			var name = jsonData.username;
			var me = jsonData.me;
			var msg = jsonData.message;
			var allMembers = JSON.parse(jsonData.allMembers);
			
			$('#sel').empty();
			for(var count = 0; count < allMembers.length; count++){                
                var option = $("<option>"+ allMembers[count]+"</option>");
                $('#sel').append(option);
            }
			
			
			messageTextArea.value += name + " : "+ msg + "\n"
			
			
		};
}


//메시지 보내기
function sendMessage(){
	var messageText = document.getElementById("messageText");
	webSocket.send(messageText.value);
	messageText.value = "";
}



</script>


</body>
</html>