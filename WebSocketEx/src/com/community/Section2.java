package com.community;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Section2 {

	Session userSession;
	
	public Section2(Session userSession) {
		this.userSession = userSession;
		System.out.println("section2 start ....."+userSession);
	}
	
	public JsonElement getData(String jsonMessage, SocketSession session) {

		JsonObject jo = new JsonObject();
		
		jo.addProperty("sessionId", userSession.getId());
		jo.addProperty("subType", "section2");
		
		JsonArray phones = new JsonArray();
		phones.add("010-1234-5678");
		phones.add("031-123-4567");
		jo.add("subContent", phones);
		
		JsonArray children = new JsonArray();
		
		JsonObject child1 = new JsonObject();
		child1.addProperty("name", "mozzi");
		child1.addProperty("age", "5");
		children.add(child1);
		
		JsonObject child2 = new JsonObject();
		child2.addProperty("name", "mozza");
		child2.addProperty("age", "3");
		children.add(child2);
		
		jo.add("content", children);
		
		return jo;
		
		//Gson gson = new Gson();
		//return gson.toJson(jo);
	
	}
}
