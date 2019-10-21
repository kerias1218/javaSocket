package com.community;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Bridge {
	
	public final static String PATH = "/Users/naya/Documents/coforward/javasocket/WebSocketEx/src/";

	Section1 s1;
	Section2 s2;
	//Chat chat;

	public Bridge() {
		System.out.println("..bridge()..");
	}

	public String chatOpen(Session userSession, SocketSession session) throws IOException {
		
		//Chat chat = new Chat(userSession, session);
		Chat chat = new Chat(userSession);
		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		result.addProperty("func", "Chat_res");
		result.add("data", chat.welcome(session));
		resultJson = gson.toJson(result);
		
		dd("chatOpen() : "+resultJson);
		
		return resultJson;
	}
	
	public String chatOut(Session userSession, SocketSession session) {
		
		//Chat chat = new Chat(userSession, session);
		Chat chat = new Chat(userSession);
		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		result.addProperty("func", "Chat_res");
		result.add("data", chat.out(session));
		resultJson = gson.toJson(result);
		
		return resultJson;
		
	}
	
	public  Map<String, String> distributeMessage2(Session userSession, SocketSession session, String jsonMessage) throws IOException {
		
		Map<String,String> resultMap = new HashMap<String, String>();
		
		
		dd("distributeMessage2() 1 : " + jsonMessage);
		String func = getFunc(jsonMessage);

		dd("distributeMessage2() 2: " + func);

		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		
		
		try {
			
			Class<?> myClass = Class.forName(func);
			Constructor myConstuctor = myClass.getConstructor( new Class[] { Session.class });
			Object myObj = myConstuctor.newInstance(userSession);
			
			Class[] methodParamClass = new Class[] { String.class, SocketSession.class };
			Object[] methodParamObject = new Object[] { jsonMessage, session };

			
			Method method = myClass.getMethod("getData", methodParamClass);
			JsonElement ele = (JsonElement) method.invoke(myObj, methodParamObject);

			dd("distributeMessage2() 3 >>>"+ele);
			
			result.addProperty("func", func+"_res");
			result.addProperty("sessionId", userSession.getId());
			result.add("data", ele);
			
			//참고 url : https://kaspyx.tistory.com/80
			
			/*
			 * 채팅 부분도 getData 에 넣어서 Section1 Section2 와 같이 한번에 가져와야 
			 */
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resultMap.put("func", func);
		
		resultJson = gson.toJson(result);
		resultMap.put("resultJson", resultJson);
		return resultMap;
		
	}
	
	/*
	public Map<String, String> distributeMessage(Session userSession, SocketSession session, String jsonMessage) throws IOException {
		
		Map<String,String> resultMap = new HashMap<String, String>();
		
		dd("distributeMessage() : " + jsonMessage);
		
		String func = getFunc(jsonMessage);
		
		dd("--"+func+"--");
			
		if(!isClass(func)) {
			throw new IllegalArgumentException("--클래스 없습니다. 생성해주세요--"+func);
			//dd("-----클래스 없습니다. -----" + func);
		}
		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		
		
		switch(func) {
			case "Section1" : 
				//section1 객체 생성
				s1 = new Section1(userSession);
				result.addProperty("func", "section1_res");
				result.addProperty("sessionId", userSession.getId());
				result.add("data", s1.getData());
				
				resultMap.put("func", func);
				
				break;
			
			case "Section2" : 
				//section2 객체 생성
				s2 = new Section2(userSession);
				result.addProperty("func", "section2_res");
				result.addProperty("sessionId", userSession.getId());
				result.add("data", s2.getData());
				
				resultMap.put("func", func);
				break;
				
			case "Chat" : 

				dd("--chat--");
				JsonElement jelement = new JsonParser().parse(jsonMessage);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    jobject = jobject.getAsJsonObject("data");
			    String message = jobject.getAsJsonObject().get("message").getAsString();
			    
			    if(!message.equals("")) {
					Chat chat = new Chat(userSession, session);
					result.addProperty("func", "chat_res");
					result.addProperty("sessionId", userSession.getId());
					result.add("data", chat.getData(jsonMessage));
			    }
			    
			    resultMap.put("func", func);
			    
				break;
		}
		
		resultJson = gson.toJson(result);
		resultMap.put("resultJson", resultJson);
		return resultMap;
		
		//return resultJson;
	}
	*/
	
	private Boolean isClass(String func) {
		File f = new File(PATH+func+".java");
		if(f.isFile()) return true;
		else return false;
	}
	
	private String getFunc(String jsonMessage) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		
		String func = element.getAsJsonObject()
								.get("func")
								.getAsString();
		return func;
		
	}
	
	
	
	/*
	
	public String distributeOpen(Session userSession, SocketSession session) throws IOException {
		
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
		//Chat chat = new Chat(userSession, session.getToalUsers());
		Chat chat = new Chat(userSession, session);
		this.chat = chat;
		result.add("chat", chat.open());
		
		resultJson = gson.toJson(result);
		dd(resultJson);

		return decomposition(userSession, resultJson);
		
	}
	
	private String decomposition(Session userSession, String jsonMessage) throws IOException {
		
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
							
		String jsonData = buildJsonOpen(type,sessionId,section1,section2,chat);
		return jsonData;
	}
	
	private String buildJsonOpen(String type, String id, Object section1, Object section2, Object chat) {
		
		String json = null;
		Gson gson = new Gson();
		
		JsonObject object = new JsonObject();
		object.addProperty("type",  type);
		object.addProperty("id",  id);
		object.add("section1",  (JsonElement) section1);
		object.add("section2",  (JsonElement) section2);
		object.add("chat",  (JsonElement) chat);
		
		json = gson.toJson(object);
		return json;
	}
	
	public Map distributeMsg(Session userSession, SocketSession session, String jsonMessage) throws IOException {
		
		Map<String,String> resultMap = new HashMap<String, String>();
		
		JsonElement jsonEle;
		String message;
		String result = null;
		
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
				result = gson.toJson(jsonEle);
				break;
			
			case "section2" : 
				jsonEle = this.s2.getData();
				result = gson.toJson(jsonEle);
				break;
				
			case "chat" : 
				dd("..chat..");
				String sendTo = element.getAsJsonObject().get("sendTo").getAsString();
				jsonEle = this.chat.getData(jsonMessage);
				result = gson.toJson(jsonEle);
				resultMap.put("sendTo", sendTo);	
				break;
		}
		
	
		
		resultMap.put("type", type);
		resultMap.put("result",  result);
		
		return resultMap;
		
		
	}
	
	*/
	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}

	
	
	
}
