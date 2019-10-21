package com.community;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.websocket.Session;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Chat {
	
	private SocketSession2 peers;
	private Session userSession;
	
	
	public Chat(Session userSession) {
		this.userSession = userSession;
	}
	
	public JsonElement getData(String jsonMessage, SocketSession2 peers) throws ClassNotFoundException, SQLException {
		JsonObject result = new JsonObject();
		
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		String method = element.getAsJsonObject().get("method").getAsString();
		
	    dd(">>>>method:"+method);

	    switch(method) {
	    	case "list" : result = (JsonObject) roomList(); break;
	    	case "create" : result = (JsonObject) create(jsonMessage, peers); break;
	    	case "join" : result =  (JsonObject) join(jsonMessage, peers); break;
	    	case "leave" :  result = (JsonObject) leave(jsonMessage, peers); break;
	    	//case "chatting" : result =  (JsonObject) chatting(jsonMessage, session); break; 
		    //case "chattingFileUpload" : result = (JsonObject) chattingFileUpload(jsonMessage, session); break;
	    }
		
		return result;
	}
	
	private JsonElement create(String jsonMessage, SocketSession2 peers) throws ClassNotFoundException, SQLException {
		
		
		/*
		 * DB roomId 가 있는지 확인
		 * 있으면 json 에러 메세지
		 * 없으면 db insert 하구 json return 
		 */
		
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	   
	    String roomId = jobject.getAsJsonObject().get("title").getAsString();
	    String owner = jobject.getAsJsonObject().get("owner").getAsString();
	    String desc = jobject.getAsJsonObject().get("desc").getAsString();
	    
		// peer 룸이름 저장
		peers.setProperties(userSession, "roomId", roomId);
	    peers.getAllPeers();

	
	    Gson gson = new Gson();
	    JsonObject top = new JsonObject();
	    JsonObject room1 = new JsonObject();

		top.addProperty("roomId",  roomId);
		
		
	    Mysql db = new Mysql();
	    if( db.chatRoomExistsCheck(roomId) ) {
	    	dd("채팅방 있습니다. ");
	    	
	    	
	    	top.addProperty("desc", desc);
			top.addProperty("owner", owner);
	    	top.addProperty("status", "fail");
	    	
	    	JsonObject errorMsg = new JsonObject();
	    	errorMsg.addProperty("msg", "이미 개설된 채팅방이 있습니다.");
	    	
	    	top.add("error", errorMsg);
	    }
	    else {
	    	dd("이미 채팅방 생성 ");
	    	try {
				db.userInsert(roomId, desc, owner, userSession.getId());
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JsonArray children = new JsonArray();
			
	    	String sql2 = "SELECT * from user  WHERE userid = '"+owner+"'";
			List<Map<String,Object>> list = db.user(sql2);
			
			// 테스트 출력
	        for( Map<String, Object> map1 : list ){
	            System.out.println("=============create=========================");
	            Iterator<String> it = map1.keySet().iterator();
	            
	            JsonObject child1 = new JsonObject();
	            
	            while(it.hasNext()){
	                String key        = it.next();
	                String value    = (String)map1.get(key);
	                
	                System.out.println(key + ":::" + "\t\t\t" + value);
	                
	    			child1.addProperty(key, value);
	    				
	            }
	            
	            children.add(child1);
	            System.out.println("==============!create========================");
	        }
	        
	        room1.add("roomMembers", children);
	        top.add("ownerInfo", room1);
			top.addProperty("desc", desc);
			top.addProperty("owner", owner);
			//top.addProperty("totalUsers", peers.getToalUsers());
			//top.addProperty("totalMembers", peers.getAllMembers());
			top.addProperty("totalMembers", db.getAllMembers());
			top.addProperty("status", "success");
			
	    }
	    
		
		return top;
	} 
	
	

	private JsonElement join(String jsonMessage, SocketSession2 peers) throws ClassNotFoundException, SQLException {
		
		
		/*
		 
  	 	채팅방(room_7) 입장하기
		
		.peers 세션에 채팅방 기록 
	    	소켓 id 로 peers 의 userSession 찾은다음 
	    	
	    	기록 
	    	this.userSession.getUserProperties().put("sender", getUserName());
		
		.db chat_user insert

		INSERT INTO chat_user (room_idx, user_idx, state) 
		VALUES 
		(
		  (SELECT idx FROM chat_room WHERE title="room_7"),
			(SELECT idx FROM user WHERE nickname='나야나'),
			''
		)

		
		
	    .방정보 + 멤버 리스트 얻어오기
	
	    SELECT A.room_idx, A.user_idx, B.nickname, B.profile, C.title, C.title_desc, C.owner, C.save 
		FROM chat_user as A 
		LEFT JOIN user as B
		ON A.user_idx = B.idx
		LEFT JOIN chat_room as C
		ON A.room_idx = C.idx
		WHERE A.room_idx=(SELECT idx FROM chat_room WHERE title='room_8' )
		
		*/
		
		
		
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	   
	    String roomId = jobject.getAsJsonObject().get("roomId").getAsString();
	    String userId = jobject.getAsJsonObject().get("userId").getAsString();
	    
	    String roomOwner = null;
	    String roomDesc = null;
	    String roomIdx = null;
	    
	    
	    // peers 해당 소켓 찾은후 roomId 주입  
  		Session sess = peers.findOtherSession(userSession);
  		dd("--join():"+sess);
  		dd("--userId:"+peers.getProperties(sess, "userId"));
  		peers.setProperties(sess, "roomId", roomId);
  		dd("--roomId:"+peers.getProperties(sess, "roomId"));
  		
	    
	    Gson gson = new Gson();
	    JsonObject top = new JsonObject();
	    
	    JsonObject room1 = new JsonObject();

	    
		top.addProperty("roomId",  roomId);
		top.addProperty("status", "fail");
		

		JsonArray children = new JsonArray();

		
	    Mysql db = new Mysql();
 	    
	    // 방 리스트에 insert
	    db.chatUserInsert(roomId, userId);
 	    
 	    // 방 정보 + 멤버 리스트 얻어오기
	    String sql2 = "SELECT A.room_idx, A.user_idx, B.userid, B.profile, C.title, C.title_desc, C.owner, C.save FROM"
	    		+ " chat_user as A "
	    		+ "LEFT JOIN user as B "
	    		+ "ON A.user_idx = B.idx "
	    		+ "LEFT JOIN chat_room as C"
	    		+ " ON A.room_idx = C.idx "
	    		+ "WHERE A.room_idx=(SELECT idx FROM chat_room WHERE title='"+roomId+"' )";
	    
    	List<Map<String,Object>> list = db.roomUserLists(sql2);
		
		
		// 테스트 출력
        for( Map<String, Object> map1 : list ){
            System.out.println("=============join()=========================");
            Iterator<String> it = map1.keySet().iterator();
            
            JsonObject child1 = new JsonObject();
            
            while(it.hasNext()){
                String key        = it.next();
                String value    = (String)map1.get(key);
                
                System.out.println(key + ":::" + "\t\t\t" + value);
                
                if(key.equals("owner")) roomOwner = value;
                if(key.equals("title_desc")) roomDesc = value;
                if(key.equals("room_idx")) roomIdx = value;
                
                
    			child1.addProperty(key, value);
    				
            }
            
            children.add(child1);
            System.out.println("==============!join()========================");
        }
        
        room1.add("roomMembers", children);
        room1.addProperty("roomOwner", roomOwner);
        room1.addProperty("roomDesc", roomDesc);
        room1.addProperty("roomIdx", roomIdx);
        
        
        top.add("roomInfo", room1);
		//top.addProperty("desc", desc);
		//top.addProperty("owner", owner);
		//top.addProperty("totalUsers", peers.getToalUsers());
		//top.addProperty("totalMembers", peers.getAllMembers());
		top.addProperty("totalMembers", db.getAllMembers());
		top.addProperty("status", "success");
		
		
		return top;
	} 
	
	private JsonElement leave(String jsonMessage, SocketSession2 peers) throws ClassNotFoundException, SQLException {
		
		/*
		 * 채팅방 나가기 & onClose 될
		  .peers 세션 찾아서 roomId 삭제
		  
		  .방장인지 체크
		  
		     .내가 방장이면
		        다른 사림이 있는지 체크 
		        있으면 
		            chat_room owner 변경하기(다른사람에게 방장 넘김)
		        없으면 방삭제 
		      
		     .내가 방장아니면 채팅방
		  
		  .채팅방 나가기
		     chat_user 테이블에서 user_idx 찾아서 삭제
		     DELETE from chat_user WHERE user_idx=(SELECT idx FROM user WHERE userid='나야나')    
		 */
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	   
	    String roomId = jobject.getAsJsonObject().get("roomId").getAsString();
	    String userId = jobject.getAsJsonObject().get("userId").getAsString();

		 
		// peers 해당 소켓 찾은후 roomId 삭제   
  		Session sess = peers.findOtherSession(userSession);
  		
  		//기존꺼 확인 
  		dd("--leave():"+sess);
  		dd("--userId:"+peers.getProperties(sess, "userId"));
  		peers.setProperties(sess, "roomId", roomId);
  		dd("--roomId:"+peers.getProperties(sess, "roomId"));
		
  		// roomId 삭제 
  		peers.setProperties(sess, "roomId", "");
  		dd("--roomId 삭제확인 :"+peers.getProperties(sess, "roomId"));
  		
  		Mysql db = new Mysql();
	    if( db.chatRoomOwnerCheck(userId) ) {
	    	dd("leave() : "+roomId+" 주인입니다. "+userId);
	    	//다른사람이 있는지 체크한후 
	    	// 있으면 방장 다른사람에게 넘김
	    	// 없으면 방 삭제 
	    }
	    else {
	    	dd("leave() : "+roomId+" 게스트 입니다. "+userId);
	    	// 게스트 이면 채팅방 나가기 
	    }
  		
	    
	    // 채팅방 나가기 
	    db.chatUserDelete(userId);
	    
	    
		JsonObject jo = new JsonObject();
		return jo;
		
	}
	
	public JsonElement roomList() throws ClassNotFoundException, SQLException {
		
		JsonObject jo = new JsonObject();
		
		jo.addProperty("subType", "Open");
		
		JsonArray rooms = new JsonArray();
			
		//채팅방 리스트
		Mysql db = new Mysql();
		String sql = "select * from chat_room";
		Map<String,String> userIdMap = db.chatRoomUserIds();
		
		Iterator<String> keys = userIdMap.keySet().iterator();
		while(keys.hasNext()) {
			String roomId = keys.next();
			String userIds = userIdMap.get(roomId);
			
			System.out.println( String.format("룸이름 : %s, 닉넴 : %s", roomId, userIds) );
			
			JsonObject room1 = new JsonObject();
			room1.addProperty("roomId", roomId);
			
			JsonArray children = new JsonArray();
			
			String[] words = userIds.split("\\|");
			
			for(String val : words) {
				String sql2 = "SELECT * from user  WHERE userid = '"+val+"'";
				
				List<Map<String,Object>> list = db.user(sql2);
				
				// 테스트 출력
		        for( Map<String, Object> map1 : list ){
		            System.out.println("==============!@===========================");
		            Iterator<String> it = map1.keySet().iterator();
		            
		            JsonObject child1 = new JsonObject();
		            
		            while(it.hasNext()){
		                String key        = it.next();
		                String value    = (String)map1.get(key);
		                
		                System.out.println(key + ":::" + "\t\t\t" + value);
		                
		    			child1.addProperty(key, value);
		    				
		            }
		            
		            children.add(child1);
		            System.out.println("==============!@===========================");
		        }
			}
			room1.add("roomMembers", children);
			rooms.add(room1);
		}

		jo.add("roomInfo", rooms);
		
		
			
		/*
		JsonObject jo = new JsonObject();
		
		jo.addProperty("sessionId", userSession.getId());
		jo.addProperty("subType", "Open");
		
		JsonArray children = new JsonArray();
		
		//채팅방 리스트
		Mysql db = new Mysql();
		String sql = "select * from chat_room";
		Map<String,String> nicknamesMap = db.chatRoomNicknames();
		
		Iterator<String> keys = nicknamesMap.keySet().iterator();
		while(keys.hasNext()) {
			String roomId = keys.next();
			String nicknames = nicknamesMap.get(roomId);
			jo.addProperty("subType", "Open");
			System.out.println( String.format("룸이름 : %s, 닉넴 : %s", roomId, nicknames) );
		
			
			jo.addProperty("roomId", roomId);
			
			
			String[] words = nicknames.split("\\|");
			
			
			for(String val : words) {
				String sql2 = "SELECT * from user  WHERE nickname = '"+val+"'";
				
				List<Map<String,Object>> list = db.user(sql2);
				
				// 테스트 출력
		        for( Map<String, Object> map1 : list ){
		            System.out.println("==============!@===========================");
		            Iterator<String> it = map1.keySet().iterator();
		            
		            
		            JsonObject child1 = new JsonObject();
		            
		            while(it.hasNext()){
		                String key        = it.next();
		                String value    = (String)map1.get(key);
		                
		                System.out.println(key + ":::" + "\t\t\t" + value);
		            
		                
		    			child1.addProperty(key, value);
		    				
		            }
		            children.add(child1);
		            
		            System.out.println("==============!@===========================");
		        }
		        
			}
		}

		jo.add("roomMembers", children);
		*/
		
		return jo;
	}
	
	
	
	public void basicJson() {
		JsonObject jo = new JsonObject();
		jo.addProperty("subType", "section1");
		
			JsonArray rooms = new JsonArray();
				
				JsonObject room1 = new JsonObject();
				room1.addProperty("roomId", "romm-7");
							
					JsonArray children = new JsonArray();
					
					JsonObject child1 = new JsonObject();
					child1.addProperty("name", "mozzi");
					child1.addProperty("age", "5");
					children.add(child1);
					
					JsonObject child2 = new JsonObject();
					child2.addProperty("name", "mozza");
					child2.addProperty("age", "3");
					children.add(child2); 
					
				
				room1.add("roomMembers", children);
			
			rooms.add(room1);
			
		jo.add("roomInfo", rooms);
	}
	
	
	/*
	 * 
	public void setUserName() {
		this.userSession.getUserProperties().put("sender", getUserName());
	}
	
	public String getUserName() {
		return "손님_" + this.userSession.getId();
	}
	
	public JsonElement welcome(SocketSession session) {
		String hello = getUserName() + " 님 입장 하셨습니다";
		
		JsonObject object = new JsonObject();
		object.addProperty("roomId",  "");
		object.addProperty("sender",  "시스템");
		object.addProperty("message", hello);
		object.addProperty("sendTo", "");
		object.addProperty("totalUsers", session.getToalUsers());
		object.addProperty("members", session.getAllSessionId());
	
		return object;
		
	}
	
	public JsonElement out(SocketSession session) {
		String hello = getUserName() + " 님 퇴장 하셨습니다";
		
		JsonObject object = new JsonObject();
		object.addProperty("roomId",  "");
		object.addProperty("sender",  "시스템");
		object.addProperty("message", hello);
		object.addProperty("sendTo", "");
		object.addProperty("totalUsers", session.getToalUsers());
		object.addProperty("members", session.getAllSessionId());
	
		return object;
		
	}
	
	public JsonElement getData(String jsonMessage, SocketSession session) {
		JsonObject result = new JsonObject();
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	    
	    String type = jobject.getAsJsonObject().get("type").getAsString();
	  
	    dd("getData() 1 :"+type);
	    
	    switch(type) {
		    case "chatting" : result =  (JsonObject) chatting(jsonMessage, session); break; 
		    case "chattingFileUpload" : result = (JsonObject) chattingFileUpload(jsonMessage, session); break;
	    }
	    
	    return result;
		
	}

	private JsonElement chatting(String jsonMessage, SocketSession session) {
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	   
	    String roomId = jobject.getAsJsonObject().get("roomId").getAsString();
	    String sendTo = jobject.getAsJsonObject().get("sendTo").getAsString();
	    String message = jobject.getAsJsonObject().get("message").getAsString();
	    
		//String json = null;
		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		top.addProperty("sessionId", userSession.getId());
		top.addProperty("subType", "chatting");
		top.addProperty("roomId",  roomId);
		top.addProperty("sender",  this.getUserName());
		top.addProperty("message", message);
		top.addProperty("sendTo", sendTo);
		top.addProperty("totalUsers", session.getToalUsers());
		top.addProperty("members", session.getAllSessionId());
		
		dd("chatting() top:"+top);
		
		return top;
	}
	
	private JsonElement chattingFileUpload(String jsonMessage, SocketSession session) {
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	   
	    dd("chatFileUpload() jobject:"+jobject);
	    
	    String action = jobject.getAsJsonObject().get("action").getAsString();
	    
	    String roomId = jobject.getAsJsonObject().get("roomId").getAsString();
	    String sendTo = jobject.getAsJsonObject().get("sendTo").getAsString();
	    //String message = jobject.getAsJsonObject().get("message").getAsString();

		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		top.addProperty("sessionId", userSession.getId());
		top.addProperty("subType", "chattingFileUpload");
		top.addProperty("action", action);
		top.addProperty("roomId",  roomId);
		top.addProperty("sender",  this.getUserName());
		top.addProperty("message", "");
		top.addProperty("sendTo", sendTo);
		top.addProperty("totalUsers", session.getToalUsers());
		top.addProperty("members", session.getAllSessionId());
		
		dd("chatFileUpload() top:"+top);
		
		return top;
	    
	}
	
	*/
	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}
	
}










/*
public class Chat {
	private SocketSession session;
	private Session userSession;
	
	public Chat(Session userSession, SocketSession session) {
		this.session = session;
		this.userSession = userSession;
		
		setUserName();
		
		System.out.println("chat start...");
		System.out.println(session.getToalUsers());
		
	}
	
	public void setUserName() {
		this.userSession.getUserProperties().put("sender", getUserName());
	}
	
	public String getUserName() {
		return "손님_" + this.userSession.getId();
	}
	
	public JsonElement welcome() {
		String hello = getUserName() + " 님 입장 하셨습니다";
		
		JsonObject object = new JsonObject();
		object.addProperty("roomId",  "");
		object.addProperty("sender",  "시스템");
		object.addProperty("message", hello);
		object.addProperty("sendTo", "");
		object.addProperty("totalUsers", session.getToalUsers());
		object.addProperty("members", session.getAllSessionId());
	
		return object;
		
	}
	
	public JsonElement out() {
		String hello = getUserName() + " 님 퇴장 하셨습니다";
		
		JsonObject object = new JsonObject();
		object.addProperty("roomId",  "");
		object.addProperty("sender",  "시스템");
		object.addProperty("message", hello);
		object.addProperty("sendTo", "");
		object.addProperty("totalUsers", session.getToalUsers());
		object.addProperty("members", session.getAllSessionId());
	
		return object;
		
	}
	
	public JsonElement getData(String jsonMessage) {
		
		
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	    
	    String roomId = jobject.getAsJsonObject().get("roomId").getAsString();
	    String sendTo = jobject.getAsJsonObject().get("sendTo").getAsString();
	    String message = jobject.getAsJsonObject().get("message").getAsString();
	    
		//String json = null;
		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		top.addProperty("sessionId", userSession.getId());
		top.addProperty("subType", "chat");
		top.addProperty("roomId",  roomId);
		top.addProperty("sender",  this.getUserName());
		top.addProperty("message", message);
		top.addProperty("sendTo", sendTo);
		top.addProperty("totalUsers", session.getToalUsers());
		top.addProperty("members", session.getAllSessionId());
		
		return top;
		
		//json = gson.toJson(top);
		//System.out.println(json);
		//return json;
		
	}

}

*/















/*
public class Chat {

	private SocketSession session;
	private Session userSession;

	public Chat(Session userSession, SocketSession session) {
		this.session = session;
		this.userSession = userSession;
		
		setUserName();
		
		System.out.println("chat start...");
	}
	
	public JsonElement open() {

		JsonObject jo = new JsonObject();
		
		jo.addProperty("sender", getUserName());
		jo.addProperty("sessionId", userSession.getId());
		jo.addProperty("message", welcome());
		jo.addProperty("totalUsers", session.getToalUsers());
		jo.addProperty("members", session.getAllSessionId());
		
		return jo;
		
		//Gson gson = new Gson();
		//return gson.toJson(jo);
	
	}
	
	public String welcome() {
		return getUserName() + " 님 입장 하셨습니다";
	}
	
	public void setUserName() {
		this.userSession.getUserProperties().put("sender", getUserName());
	}
	
	public String getUserName() {
		return "손님_" + this.userSession.getId();
	}
	
	public JsonElement getData(String jsonMessage) {
		
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		
		String roomId = element.getAsJsonObject()
								.get("roomId")
								.getAsString();

		String sendTo = element.getAsJsonObject()
								.get("sendTo")
								.getAsString();
	
		String message = element.getAsJsonObject()
								.get("message")
								.getAsString();
		
		
		//String json = null;
		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		top.addProperty("sessionId", userSession.getId());
		top.addProperty("subType", "chat");
		
		JsonObject object = new JsonObject();
		object.addProperty("sessionId",  userSession.getId());
		object.addProperty("mode",  "R");
		object.addProperty("roomId",  roomId);
		object.addProperty("sender",  this.getUserName());
		object.addProperty("message", message);
		object.addProperty("sendTo", sendTo);
		object.addProperty("totalUsers", session.getToalUsers());
		object.addProperty("members", session.getAllSessionId());
	
		top.add("content", object);
		return top;
		
		
		//json = gson.toJson(top);
		//System.out.println(json);
		//return json;
		
	}
*/	
	
	
	
	
	
	
	/*
	Session userSession;	
	
	public Chat(Session userSession) {
		this.userSession = userSession;
		System.out.println("client is now connected...");
		System.out.println(userSession);
	}
	
	public String initChat(String username) {
		return username + " 님 입장 하셨습니다";
	}
	
	public String destroyChat(String username) {
		return username + " 님이 나갔습니다.";
	}
	
	public void putSession(String username) {
		this.userSession.getUserProperties().put("username", username);
	}
	
	public String getUserName(Session userSession) {
		return (String)userSession.getUserProperties().get("username");
	}
	
	public String makeRandomStr() {
		Random rnd = new Random();
		StringBuffer buf =new StringBuffer();
		for(int i=0;i<5;i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10)));
		    }
		}
		
		return buf.toString();
	}
	
	public void testPrint(Session userSession) {
		System.out.println("i am chat.java class. "+userSession);
	}
	*/
	

