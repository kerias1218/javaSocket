
import java.util.Random;

import javax.websocket.Session;

public class Chat {

	Session userSession;	
	
	public Chat(Session userSession) {
		this.userSession = userSession;
		System.out.println("client is now connected...");
		System.out.println(userSession);
	}
	
	public String initChat(String username) {
		return username + " 님 입장 하셨습니다";
	}
	
	public String destroyChat(String username) {
		return username + " 님이 나갔습니다.";
	}
	
	public void putSession(String username) {
		this.userSession.getUserProperties().put("username", username);
	}
	
	public String getUserName(Session userSession) {
		return (String)userSession.getUserProperties().get("username");
	}
	
	public String makeRandomStr() {
		Random rnd = new Random();
		StringBuffer buf =new StringBuffer();
		for(int i=0;i<5;i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10)));
		    }
		}
		
		return buf.toString();
	}
	
	public void testPrint(Session userSession) {
		System.out.println("i am chat.java class. "+userSession);
	}
}
