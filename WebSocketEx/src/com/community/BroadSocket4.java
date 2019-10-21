package com.community;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/BroadSocket4")
public class BroadSocket4 {
	
	private Bridge2 bridge = null;
	
	@OnMessage
	public void handleMessage(Session userSession, String jsonMessage) throws IOException, ClassNotFoundException, SQLException {
		dd("handleMessage() userSession :"+userSession);
		
		Map resultMap = bridge.distributeMessage2(userSession, jsonMessage);
		
		String func = (String) resultMap.get("func");
		String result = (String) resultMap.get("resultJson");
	
		sendMessage(userSession, result);
		
		/*
		if( func.equals("Chat") ) {
			sendChatMessage(userSession, result);	
		}
		else sendMessage(userSession, result);
		*/
		
	}
	
	public void sendMessage(Session userSession, String jsonData) throws IOException {
		userSession.getBasicRemote().sendText(jsonData);
	}
	
	
	@OnOpen
	public void handleOpen(Session userSession) throws IOException, SQLException, ClassNotFoundException{
		
		dd("handleOpen() :"+userSession);
		bridge = new Bridge2();
	}
	
	@OnClose
	public void handleClose(Session userSession) throws IOException{
		
		/* 삭제하는거 임시로 삭제
		*/
		
		try {
			bridge.remove(userSession);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@OnError
	public void handleError(Throwable t){
		System.out.println("OnError");	
		t.printStackTrace();
	}

	
	

	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}

}
