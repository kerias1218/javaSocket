package com.community;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ServerEndpoint("/broadsocket3")
public class broadsocket3 {

	BufferedOutputStream bos;
    final String PATH = "/Users/naya/Documents/coforward/javasocket/WebSocketEx/WebContent/resources";

	private SocketSession session = null;
	private Bridge bridge = null;
	
	@OnMessage
	public void handleMessage(Session userSession, String jsonMessage) throws IOException {
		
		Map resultMap = bridge.distributeMessage2(userSession, session, jsonMessage);
		
		String func = (String) resultMap.get("func");
		String result = (String) resultMap.get("resultJson");
	
		
		if( func.equals("Chat") ) {
			
			sendChatMessage(userSession, result);	
			
			
		}
		else sendMessage(userSession, result);
	 
	}
	
	public void sendMessage(Session userSession, String jsonData) throws IOException {
		userSession.getBasicRemote().sendText(jsonData);
	}
	
	private void sendChatMessage(Session userSession, String resultJson) throws IOException {
		
		Boolean whispers = false;
		
		//String sendTo = getSendToJsonParsing(resultJson);
		String sendTo = getJsonParsing(resultJson, "sendTo");
		
		dd("sendChatMessage() : resultJson >> " + resultJson);
		
		
		String me = (String) userSession.getUserProperties().get("sender");
	
		Iterator<Session> iterator = session.getSessionUsers().iterator();
		
		// 귓속말이면  
		if( sendTo.length()>=1 ) {
			while(iterator.hasNext()){
				Session sess = iterator.next();
				
				String sender = (String) sess.getUserProperties().get("sender");
				if( sender.equals(sendTo) || sender.equals(me) ) {
					sess.getBasicRemote().sendText(resultJson);
				}
			}
		}
		else { // 전체 메세지  
			while(iterator.hasNext()){
				Session sess = iterator.next();
				
				String sender = (String) sess.getUserProperties().get("sender");
				sess.getBasicRemote().sendText(resultJson);
			}
		}
		
		
	}
	
	@OnOpen
	public void handleOpen(Session userSession) throws IOException{
		System.out.println("OnOpen");
		
		session = new SocketSession();
		session.add(userSession);
		
		bridge = new Bridge();
		String resultJson = bridge.chatOpen(userSession, session);
		sendChatMessage(userSession, resultJson);
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
		
		String resultJson = bridge.chatOut(userSession, session);
		sendChatMessage(userSession, resultJson);
	}
	
	// 바이너리 데이터가 오게되면 호출된다.
    @OnMessage
    public void processUpload(ByteBuffer msg, boolean last, Session session) {
        
    	System.out.println("-- processUpload --");
    	
    	
        while(msg.hasRemaining()){
            try {
                bos.write(msg.get());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        
    }
	
	
	private String getJsonParsing(String jsonMessage, String field) {
		JsonElement jelement = new JsonParser().parse(jsonMessage);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("data");
	    
	    return jobject.getAsJsonObject().get(field).getAsString();
	}
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}

}
