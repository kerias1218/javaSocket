package com.community;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/broadsocket2")
public class broadsocket2 {
	
	private SocketSession session = null;
	private Bridge bridge = null;
	
	@OnMessage
	public void handleMessage(Session userSession, String jsonMessage) throws IOException {
		
		Map resultMap = bridge.distributeMsg(userSession, session, jsonMessage);
		
		String type = (String) resultMap.get("type");
		String sendTo = (String) resultMap.get("sendTo");
		String result = (String) resultMap.get("result");
		
		if( type.equals("chat")) {
			sendChatMessage(userSession, result, sendTo);
		}
		else {
			sendMessage(userSession, result);	
		}
	}
	
	public void sendMessage(Session userSession, String jsonData) throws IOException {
		userSession.getBasicRemote().sendText(jsonData);
	}
		
	private void sendChatMessage(Session userSession, String jsonData, String sendTo) throws IOException {
	
		Boolean whispers = false;
		
		String me = (String) userSession.getUserProperties().get("sender");
	
		Iterator<Session> iterator = session.getSessionUsers().iterator();
		
		if( sendTo.length()>=1 ) {
			
			while(iterator.hasNext()){
				Session sess = iterator.next();
				
				String sender = (String) sess.getUserProperties().get("sender");
				if( sender.equals(sendTo) || sender.equals(me) ) {
					sess.getBasicRemote().sendText(jsonData);
				}
			}
		}
		else {
			
			while(iterator.hasNext()){
				Session sess = iterator.next();
				sess.getBasicRemote().sendText(jsonData);
			}
		}
	}
	
	@OnOpen
	public void handleOpen(Session userSession) throws IOException{
		System.out.println("OnOpen");
		
		session = new SocketSession();
		session.add(userSession);
		
		bridge = new Bridge();
		String result = bridge.distributeOpen(userSession, session);
		sendMessage(userSession, result);
		//sendChatMessage(userSession, result, "");
	}
	
	@OnError
	public void handleError(Throwable t){
		System.out.println("OnError");	
		t.printStackTrace();
	}
	
	@OnClose
	public void handleClose(Session userSession) throws IOException{
		System.out.println("OnClose");		
		session.remove(userSession);
	}
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}

}
