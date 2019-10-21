package com.community;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.Session;

import com.google.gson.Gson;

public class SocketSession {
		
	static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<Session>());
		
	public SocketSession() {
		System.out.println("..SocketSession..");
	}
	
	public List<Session> getSessionUsers() {
		return this.sessionUsers;
	}
	
	public void add(Session userSession) {
		sessionUsers.add(userSession);
	}
	
	public void remove(Session userSession) {
		sessionUsers.remove(userSession);
	}
	
	public int getToalUsers() {
		return sessionUsers.size();
	}
	
	public void getAllSessions() { 
		int max = sessionUsers.size();
		
		for(Session sess : sessionUsers) {			
			System.out.println(sess);
		}
	}
	
	public String getAllSessionId() {
		List<String> allUsersRoom = new ArrayList<String>();
		for(Session sess : sessionUsers) {
			String name = (String)sess.getUserProperties().get("sender");
			//allUsersRoom.add(sess.getId());
			allUsersRoom.add(name);
		}
		Gson gson = new Gson();
		return gson.toJson(allUsersRoom);
	}
	
}
