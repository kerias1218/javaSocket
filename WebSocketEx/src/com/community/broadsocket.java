package com.community;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@ServerEndpoint("/broadsocket")
public class broadsocket {
	
	//유저 집합 리스트	
	static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<Session>());
	
	Section1 s1;
	Section2 s2;
	Chat chat;
	
	/**
	* 웹 소켓이 접속되면 유저리스트에 세션을 넣는다.
	* @param userSession 웹 소켓 세션
	 * @throws IOException 
	*/
	@OnOpen
	public void handleOpen(Session userSession) throws IOException{
		
		sessionUsers.add(userSession);
		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		
		result.addProperty("type", "all");
		result.addProperty("sessionId", userSession.getId());
		
		// section1 
		Section1 section1 = new Section1(userSession);
		this.s1 = section1;
		result.add("section1", section1.getData());

		// section2 
		Section2 section2 = new Section2(userSession);
		this.s2 = section2;
		result.add("section2", section2.getData());
		
		// chat
		Chat chat = new Chat(userSession, getToalUsers());
		this.chat = chat;
		result.add("chat", chat.open(getAllSessionId()));
		
		resultJson = gson.toJson(result);
		dd(resultJson);

		sendOpenMessage(userSession, resultJson);
		
	}
	
	private void sendOpenMessage(Session userSession, String jsonMessage) throws IOException {
	
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		
		String type = element.getAsJsonObject()
								.get("type")
								.getAsString();
		String sessionId = element.getAsJsonObject()
								.get("sessionId")
								.getAsString();
		
		Object section1 = element.getAsJsonObject()
								.get("section1");
		
		Object section2 = element.getAsJsonObject()
								.get("section2");
							
		Object chat = element.getAsJsonObject()
								.get("chat");
								

		/*
		String message = element.getAsJsonObject()
								.get("message")
								.getAsString();
		*/
	
		userSession.getBasicRemote()
			.sendText(buildJsonDataOpen(type,sessionId,section1,section2,chat));
		
		/*
		Iterator<Session> iterator = sessionUsers.iterator();
			
		while(iterator.hasNext()){
			Session sess = iterator.next();
			//sess.getId();
			sess.getBasicRemote().sendText(buildJsonDataOpen(type,sessionId,section1,section2,chat));
		}
		*/
		
			
	}
	
	
	/**
	* 웹 소켓으로부터 메시지가 오면 호출한다.
	* @param message 메시지
	* @param userSession
	* @throws IOException
	*/

	@OnMessage
	public void handleMessage(String jsonMessage, Session userSession) throws IOException {

		JsonElement jsonEle;
		String message;
		
		System.out.println(">>"+jsonMessage);
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		
		String type = element.getAsJsonObject()
								.get("type")
								.getAsString();
		
		Gson gson = new Gson();
		switch(type) {
			case "section1" : 
				jsonEle = this.s1.getData();
				message = gson.toJson(jsonEle);
				userSession.getBasicRemote().sendText(message);
			
				break;
			
			case "section2" : 
				jsonEle = this.s2.getData();
				message = gson.toJson(jsonEle);
				userSession.getBasicRemote().sendText(message);
				
				break;
				
			case "chat" : 
				dd("..chat..");
				sendChatMessage(userSession, jsonMessage);
				break;
		}
		
		//String username = this.chat.getUserName(userSession);
		//sendMessage(userSession, username, username, message);
	}

	private void sendChatMessage(Session userSession, String jsonMessage) throws IOException {
		
		Iterator<Session> iterator = sessionUsers.iterator();
		while(iterator.hasNext()){
			Session sess = iterator.next();
			sess.getBasicRemote()
				.sendText(buildJsonDataChat(sess.getId(), jsonMessage));
		}
		
	}
	
	private String buildJsonDataChat(String id, String jsonMessage) {
		
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
		
		
		String json = null;
		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		
		JsonObject object = new JsonObject();
		object.addProperty("sessionId",  id);
		object.addProperty("roomId",  roomId);
		object.addProperty("userName",  chat.getUserName());
		object.addProperty("message", message);
		object.addProperty("sendTo", sendTo);
		object.addProperty("totalUsers", getToalUsers());
		object.addProperty("members", getAllSessionId());
	
		top.add("chat", object);
		Other other = new Other();
		top.add("other", other.getOtherData());
		
		json = gson.toJson(top);
		System.out.println(json);
		return json;
	}
	
	
	
	@OnError
	public void handleError(Throwable t){
		t.printStackTrace();
	}

	/**
	* 웹소켓을 닫으면 해당 유저를 유저리스트에서 뺀다.
	* @param userSession
	 * @throws IOException 
	*/
	
	@OnClose
	public void handleClose(Session userSession) throws IOException{
		System.out.println("OnClose");				
		//String username = this.chat.getUserName(userSession);
		//String message = this.chat.destroyChat(username);
			
		sessionUsers.remove(userSession);
		
		//sendMessage(userSession, "System", username, message);
	}
		
	private String buildJsonDataOpen(String type, String id, Object section1, Object section2, Object chat) {
		
		String json = null;
		Gson gson = new Gson();
		
		JsonObject object = new JsonObject();
		object.addProperty("type",  type);
		object.addProperty("id",  id);
		object.add("section1",  (JsonElement) section1);
		object.add("section2",  (JsonElement) section2);
		object.add("chat",  (JsonElement) chat);
		
		json = gson.toJson(object);
		System.out.println(json);
		return json;
	}
	
	
	private String getAllSessionId() {
		List<String> allUsersRoom = new ArrayList<String>();
		for(Session sess : sessionUsers) {
			String name = (String)sess.getUserProperties().get("userName");
			//allUsersRoom.add(sess.getId());
			allUsersRoom.add(name);
		}
		Gson gson = new Gson();
		return gson.toJson(allUsersRoom);
	}
	
	
	
	
	
	
	
	
	
	
	

	private void sendMessage(Session userSession, String username, String me, String jsonMessage) throws IOException {

		/*
		String sendTo;
		
		System.out.println(message);
		String[] array = message.split("\\|");
		message = array[0];
		sendTo = (array.length == 2)?array[1]:"";
		*/
		
		System.out.println(jsonMessage);
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		
		String sectionID = element.getAsJsonObject()
								.get("sectionID")
								.getAsString();
		
		String roomID = element.getAsJsonObject()
								.get("roomID")
								.getAsString();

		String sendTo = element.getAsJsonObject()
								.get("sendTo")
								.getAsString();
	
		String message = element.getAsJsonObject()
								.get("message")
								.getAsString();
		
		Iterator<Session> iterator = sessionUsers.iterator();
		
		//System.out.println(userSession.getId());
		
		while(iterator.hasNext()){
			Session sess = iterator.next();
			
			
			sess.getBasicRemote().sendText(buildJsonDataChat(sess.getId(),username, me, message, sendTo));
		
		}
			
	}
	
	private String buildJsonDataChat_ori(String id, String username, String me, String message, String sendTo) {
						
		String json = null;
		Gson gson = new Gson();
		JsonObject top = new JsonObject();
		
		JsonObject object = new JsonObject();
		object.addProperty("id",  id);
		object.addProperty("username", username);
		object.addProperty("me",  me);
		object.addProperty("message", message);
		object.addProperty("sendTo", sendTo);
		object.addProperty("totalUsers", getToalUsers());
		object.addProperty("allMembers", getAllMembersRoom());
		
		
		top.add("chat", object);
		Other other = new Other();
		top.add("other", other.getOtherData());
		
		json = gson.toJson(top);
		System.out.println(json);
		return json;
	}
		
	private int getToalUsers() {
		return sessionUsers.size();
	}
	
	private String getAllMembersRoom() {
		List<String> allUsersRoom = new ArrayList<String>();
		for(Session sess : sessionUsers) {
			String name = (String)sess.getUserProperties().get("username");
			allUsersRoom.add(name);
		}
		Gson gson = new Gson();
		return gson.toJson(allUsersRoom);
	}

	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}
		
	private void getSessions() { 
		int max = sessionUsers.size();
		
		for(Session sess : sessionUsers) {			
			//System.out.println(sess);
		}
	}
	
	
	
}



