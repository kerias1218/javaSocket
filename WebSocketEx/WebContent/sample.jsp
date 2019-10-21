<!-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat Test</title>


<!-- index modify 3333 -->

<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>

<style>
.box {
	width:100px;
	height:200px;
	border:1px solid #eee;
	text-align:center;
	float:left;
	padding: 10px 10px;
	margin: 10px 10px;
}

</style>
</head>

<body>






<div id="section1" class="box">
	<button onclick="sendMessage('section1')">섹션1</button><br>
	파일업로드 <br>
	<div id="section1_area"></div>
</div>

<div id="section2" class="box">
	<button onclick="sendMessage('section2')">섹션2</button><br>
	게시판 글쓰기4444<br>
	<div id="section2_area"></div>
</div>



<div>
	<textarea id="messageTextArea" readonly="readonly" rows="10" cols="45"></textarea>
</div>
<select id="sel" name="sendTo"></select>
<input type="text" id="messageText" size="50" />
<input type="button" value="Send" onclick="sendMessage('chat')" />


<script src="js/websocketUrl.js"></script>

<script type="text/javascript">

var me;
var funcMap = {
	section1_res(obj) {
		$("#section1_area").html(obj.data.subType);
	},
	section2_res(obj) {
		$("#section2_area").html(obj.data.subType);
	},
	chat_res(obj) {
		var data = obj.data;
		var sender = data.sender;
		var msg = data.message;
		var allMembers = JSON.parse(data.members);
		
		messageTextArea.value += sender + " : " + msg + "\n"
		
		$('#sel').empty();
		var option = "<option value='' selected>전 체";
		for(var count = 0; count < allMembers.length; count++){                
	           option += "<option value='"+allMembers[count]+"'>"+ allMembers[count]+"</option>";
	    }
		$('#sel').append(option);
		
	}
};
	
var messageTextArea = document.getElementById("messageTextArea");

//웹 소켓이 연결되었을 때 호출되는 이벤트
webSocket.onopen = function(message){
	console.log('onopen');
	messageTextArea.value += "Server connect...\n";
	
	sendMessage('section1');
	//sendMessage('section2');
	//ßsendMessage('chat');
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
	var jsonData = JSON.parse(message.data);
	console.log(jsonData);

	var func = jsonData.func;
	funcMap[func](jsonData);
}


var sendMessage = function(type){

	console.log("me>>"+me);
	
	switch(type) {
		case "section1" :   j = section1();
			
			break;
			
		case "section2" : j = section2();
			
			break;
		
		case "chat" : j = chat(); 
			
			break;
	}
	
	var jsonMsg = JSON.stringify(j);
	webSocket.send(jsonMsg);
	messageText.value = "";
}


var section1 = function() {
	var j = {
		func: "Section1",
		data: {
			firstName: "na",
			lastName: "ya",
			hp: "+82-10-9090-9090",
		}
	};

	return j;
}

var section2 = function() {
	var j = new Object();
	j.func = "Section2";
	
	var obj = new Object();
	obj.firstName = "na2";
	obj.lasttName = "ya2";
	obj.hp = "+82-10-2222-2222";
	j.data = obj;
	
	return j;
}

var chat = function() {

	var j = {
		func: "Chat",
		data: {
			roomId: "room-101",
			sendTo: ($("#sel option:selected").val())?$("#sel option:selected").val():'',
			message: $("#messageText").val(),
			etc: "",
		}
	};
	return j;
		
	/*
	var j = new Object();
	j.func = "Chat22";
	j.roomId = "room-101";
	j.sendTo = ($("#sel option:selected").val())?$("#sel option:selected").val():'';
	j.message = $("#messageText").val();
	j.etc = "";
	return j;
	*/
}

/*
var me;

//웹소켓 초기화
//var webSocket = new WebSocket("ws://localhost:8080/WebSocketEx/broadsocket");
var webSocket = new WebSocket("ws://localhost:8080/WebSocketEx/broadsocket2");
var messageTextArea = document.getElementById("messageTextArea");

//웹 소켓이 연결되었을 때 호출되는 이벤트
webSocket.onopen = function(message){
	console.log('onopen');
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
	var jsonDataOri = JSON.parse(message.data);
	var jsonData = jsonDataOri.chat;
	var mainData = jsonDataOri.other;
	
	console.log(jsonDataOri);

	var type = jsonDataOri.type;
	var section1 = jsonDataOri.section1;
	var section2 = jsonDataOri.section2;
	var chat = jsonDataOri.chat;
	
	if(type == "all") {
		var msg = chat.message;
		$("#section1_area").append(section1.subType);
		$("#section2_area").append(section2.subType);
		messageTextArea.value += msg + "\n"
		me = chat.sender;
	}
	else {
		var subType = jsonDataOri.subType;
		
		switch(subType) {
			case "section1" :   
				console.log('11111');
				
				break;
			
			case "section2" : 
				console.log('22222');
				
				break;
		
			case "chat" : 
				var chat = jsonDataOri.content;
				
				var sender = chat.sender;
				var sendTo = chat.sendTo;
				var msg = chat.message;
				var allMembers = JSON.parse(chat.members);
				
				$('#sel').empty();
				var option = "<option value='' selected>전 체";
				for(var count = 0; count < allMembers.length; count++){                
			           option += "<option value='"+allMembers[count]+"'>"+ allMembers[count]+"</option>";
			    }
				$('#sel').append(option);
				
				
				if(me == sendTo) var whispers = "(귓속말) ";
				else var whispers = "";
				
				if(me == sender) var matchMe = "(나) "; 
				else var matchMe = "";
				
				messageTextArea.value += whispers + matchMe + sender + " : "+ msg + "\n"
				
				break;
		}
	}
			
}

//메시지 보내기
function sendMessage(type){

	console.log("me>>"+me);
	
	switch(type) {
		case "section1" :   
			var j = new Object();
			j.type = type;
			j.data = "section1 datadata";
			
			break;
			
		case "section2" : 
			var j = new Object();
			j.type = type;
			j.data = "section2 datadata";
			
			break;
		
		case "chat" : 
			var j = new Object();
			j.type = type;
			j.roomId = "room-101";
			j.sendTo = ($("#sel option:selected").val())?$("#sel option:selected").val():'';
			j.message = $("#messageText").val();
			j.etc = "";
			
			break;
	}
	
	var jsonMsg = JSON.stringify(j);
	webSocket.send(jsonMsg);
	messageText.value = "";
}

*/

</script>


</body>
</html>