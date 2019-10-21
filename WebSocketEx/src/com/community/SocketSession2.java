package com.community;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.google.gson.Gson;

public class SocketSession2 {
	
	static Map<String, Session> map = new HashMap<>();
	static Map<String, Session> peers = Collections.synchronizedMap(map);

	public SocketSession2() {
		System.out.println("== SocketSession ==");
	}
	
	public void add(Session userSession) {
		peers.put(userSession.getId(), userSession);
	}
	
	public void remove(Session userSession) {
		peers.remove(userSession.getId());
	}
	
	public static Session findOtherSessionById(Session userSession, String id) {
	    if ( peers.containsKey(userSession.getId()) ) {
	        return peers.get(userSession.getId());
	    }
	    else return null;
	}
	
	public static Session findOtherSession(Session userSession) {
		if ( peers.containsKey(userSession.getId()) ) {
	        return peers.get(userSession.getId());
	    }
	    else return null;
	}
	
	public int getToalUsers() {
		return peers.size();
	}
	
	public void setProperties(Session userSession, String field, String content) {
		userSession.getUserProperties().put(field, content);
	}
	
	public String getProperties(Session userSession, String field) {
		return (String) userSession.getUserProperties().get(field);	
	}

	public String getAllMembers() {
		List<String> allMembers = new ArrayList<String>();
		
		Iterator<String> keys = peers.keySet().iterator();
	    while( keys.hasNext() ){
	        String key = keys.next();
	        Session sess = peers.get(key);
	        String userId = getProperties(sess, "userId");
	        allMembers.add(userId);
	    }
		
		Gson gson = new Gson();
		return gson.toJson(allMembers);
	} 

	public void getAllPeers() {
		Iterator<String> keys = peers.keySet().iterator();
	    while( keys.hasNext() ){
	        String key = keys.next();
	        Session sess = peers.get(key);
	        String userId = getProperties(sess, "userId");
	        String roomId = getProperties(sess, "roomId");
	        System.out.println( String.format("키 : %s, 값 : %s, userId : %s, roomId : %s", key, sess, userId, roomId) );
	    }
	    
	}
	
}
