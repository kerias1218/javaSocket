package com.community;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.community.UniversalDataService;


public class Open extends Common {
	
	Session userSession;
	
	public Open(Session userSession) {
		this.userSession = userSession;
		System.out.println("Open() start ....."+userSession);
	}
	
	public JsonElement getData(String jsonMessage, SocketSession2 peers) throws ClassNotFoundException, SQLException {
		
		
		/*
		UniversalDataService svc = new UniversalDataService();
		dd(">>>>>"+svc);
		*/
		
		
		// 소켓에 정상적으로 userId 저장되었는지 peers 에서 소켓 찾은다음 userId 추출
		Session sess = peers.findOtherSession(userSession);
		String userId = peers.getProperties(sess, "userId");
		
		JsonObject jo = new JsonObject();
		jo.addProperty("socketId", userSession.getId());
		jo.addProperty("message", "소켓 접속 성공..");
		
		
		if(debug) {
			JsonObject debug = new JsonObject();
			debug.addProperty("userId",userId);
			jo.add("debug",debug);	
		}
		
		return jo;
		
		
		
		/*
		// 채팅 영역 접근시 
		Chat chat = new Chat(userSession);
		return chat.roomList();
		*/
		
		
	}
	
	public void init() {
		dd("init();");
	}
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}
	
}
