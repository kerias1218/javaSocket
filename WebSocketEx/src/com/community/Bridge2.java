package com.community;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Bridge2 {

	private SocketSession2 peers = null;
	private Mysql db = null;
	
	private final static String PACK  = "com.community.";
	
	public Bridge2()  {
		System.out.println("== bridge() ==");
	}
	
	public  Map<String, String> distributeMessage2(Session userSession, String jsonMessage) throws ClassNotFoundException, SQLException {
		
		// 소켓 세션 저장 
		setSocketSession(userSession, jsonMessage);
		
		
		
		Map<String,String> resultMap = new HashMap<String, String>();
		
		dd("distributeMessage2() 1 : " + jsonMessage);

		String func = getFunc(jsonMessage);
		String umethod = getMethod(jsonMessage);

		dd("distributeMessage2() 2: " + func);

		
		String resultJson = null;
		Gson gson = new Gson();
		JsonObject result = new JsonObject();
		
		
		try {
			
			Class<?> myClass = Class.forName(PACK+func);
			Constructor myConstuctor = myClass.getConstructor( new Class[] { Session.class });
			Object myObj = myConstuctor.newInstance(userSession);
			
			Class[] methodParamClass = new Class[] { String.class, SocketSession2.class };
			Object[] methodParamObject = new Object[] { jsonMessage, peers };

			
			Method method = myClass.getMethod("getData", methodParamClass);
			JsonElement ele = (JsonElement) method.invoke(myObj, methodParamObject);

			dd("distributeMessage2() 3 >>>"+ele);
			
			result.addProperty("function", func);
			result.addProperty("method", umethod);
			//result.addProperty("sessionId", userSession.getId());
			result.add("data", ele);
			
			//참고 url : https://kaspyx.tistory.com/80
			
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
	
	private String getFunc(String jsonMessage) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		return element.getAsJsonObject().get("function").getAsString();
	}
	
	private String getMethod(String jsonMessage) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonMessage);
		return element.getAsJsonObject().get("method").getAsString();
	}
	
	public void setSocketSession(Session userSession, String jsonMessage) {
	
		String userId = getJsonDataParsiong(jsonMessage, "userId");
		dd("setSocketSession() userId:"+userId);
		
		
		peers = new SocketSession2();
		peers.setProperties(userSession, "userId", userId);
		peers.add(userSession);
		peers.getAllPeers();
		
		
		db = new Mysql();
		
		try {
			db.openUpdate(userSession, userId);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getJsonDataParsiong(String jsonMessage, String field) {
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	    
	    return  jobject.getAsJsonObject().get(field).getAsString();
	    
	}
	
	public void remove(Session userSession) throws ClassNotFoundException, SQLException {
		peers.remove(userSession);
		
		Mysql db = new Mysql();
		//db.chatRoomDelete(userSession.getId());
		db.userTableCloseUpdate(userSession.getId());
		
	}
	
	
	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}
	
}
