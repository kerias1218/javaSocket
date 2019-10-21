package com.community;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Mysql {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/eujin?useSSL=false&useUnicode=true&characterEncoding=utf8";

    static final String USERNAME = "eujin";
    static final String PASSWORD = "eujin@^00pwZZ";
	    
	Connection connection = null;
    Statement st = null;
    
    public Mysql() {
    	
    }
  	
	public void select() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        String sql;
        sql = "select * FROM user;";

        ResultSet rs = st.executeQuery(sql);

        if(rs.next()) {
    		dd(rs.getString("userid"));
    		dd(rs.getString("nickname"));
    		dd(rs.getString("profile"));
    		dd(rs.getString("is_login"));
    	}
        
        st.close();
        connection.close();
	}
	
	public void userInsert(String roomId, String desc, String owner, String socketId) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
        String sql1 = "insert into chat_room (title,title_desc,owner,type,save,socket) values (?,?,?,?,?,?) ";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql1);
    	 
    	pstmt.setString(1, roomId);
    	pstmt.setString(2, desc);
    	pstmt.setString(3, owner);
    	pstmt.setString(4, "type");
    	pstmt.setInt(5, 0);
    	pstmt.setString(6, socketId);
    	
    	int result = pstmt.executeUpdate();
    	dd("userInsert() : "+result);
        
        st.close();
        connection.close();
	}

	public void openUpdate(Session userSession, String userId) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
        String sql1 = "update user set is_login=?, socket=? where userid=?";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql1);
    	pstmt.setInt(1, 1); 
    	pstmt.setString(2, userSession.getId());
    	pstmt.setString(3, userId);

    	int result = pstmt.executeUpdate();
        
        st.close();
        connection.close();
	}
	
	public void closeUpdate(Session userSession, String userId) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
        String sql1 = "update user set is_login=?, socket=? where userid=?";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql1);
    	pstmt.setInt(1, 0); 
    	pstmt.setString(2, "");
    	pstmt.setString(3, userId);

    	int result = pstmt.executeUpdate();
        
        st.close();
        connection.close();
	}
	
	public void chatRoomDelete(String socketId) throws ClassNotFoundException, SQLException {
		
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
        String sql1 = "delete from chat_room where socket=?";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql1);
    	pstmt.setString(1, socketId);

    	pstmt.execute();
        
        st.close();
        connection.close();
        
	}
	
	public void userTableCloseUpdate(String socketId) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
        String sql1 = "update user set is_login=?, socket=? where socket=?";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql1);
    	pstmt.setInt(1, 0); 
    	pstmt.setString(2, "");
    	pstmt.setString(3, socketId);

    	int result = pstmt.executeUpdate();
        
        st.close();
        connection.close();
	}
	
	public String getAllMembers() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        String sql;
        sql = "select * FROM user WHERE is_login='1'";

        ResultSet rs = st.executeQuery(sql);
        
        List<String> totalUsers = new ArrayList<String>();
        
        while(rs.next()) { 
    		String userId = rs.getString("userid");
    		totalUsers.add(userId);
    	}

        st.close();
        connection.close();
        
        Gson gson = new Gson();
		return gson.toJson(totalUsers);
		
	}
	
	public String getRoomMembers() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        String sql;
        sql = "select * FROM user WHERE is_login='1'";

        ResultSet rs = st.executeQuery(sql);
        
        List<String> totalUsers = new ArrayList<String>();
        
        while(rs.next()) { 
    		String userId = rs.getString("userid");
    		totalUsers.add(userId);
    	}

        st.close();
        connection.close();
        
        Gson gson = new Gson();
		return gson.toJson(totalUsers);
		
	}
	
	
	public Map<String, String> chatRoomUserIds() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        String sql;
        sql = "SELECT A.title, GROUP_CONCAT( A.owner SEPARATOR  '|' ) as userids FROM chat_room as A GROUP BY A.title";

        ResultSet rs = st.executeQuery(sql);
        
        Map<String,String> resultMap = new HashMap<String, String>();
        while(rs.next()) { 
        	String roomId = rs.getString("title");
    		String userIds = rs.getString("userids");
    		resultMap.put(roomId, userIds);
    	}

        st.close();
        connection.close();
        
        return resultMap;
	}


	public List<Map<String,Object>> user(String sql) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		st = (Statement) connection.createStatement();
        
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        
        
        // 각 행을 읽어 리스트에 저장한다.
        int sizeOfcolumn = metaData.getColumnCount();
        String column;
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map;
        
        while(rs.next()){
            map = new HashMap<String,Object>();
            
            for(int indexOfcolumn=0; indexOfcolumn<sizeOfcolumn; indexOfcolumn++){
                column = metaData.getColumnName(indexOfcolumn + 1);
                map.put(column, rs.getString(column));
            }
            list.add(map);
        }
        
        
        /*
        // 테스트 출력
        for( Map<String, Object> map1 : list ){
            System.out.println("=========================================");
            Iterator<String> it = map1.keySet().iterator();
            while(it.hasNext()){
                String key        = it.next();
                String value    = (String)map1.get(key);
                
                System.out.println(key + ":::" + "\t\t\t" + value);
            }
            System.out.println("=========================================");
        }
        */



        st.close();
        connection.close();
        
        return list;
	}

	public List<Map<String,Object>> roomUserLists(String sql) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		st = (Statement) connection.createStatement();
        
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        
        
        // 각 행을 읽어 리스트에 저장한다.
        int sizeOfcolumn = metaData.getColumnCount();
        String column;
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map;
        
        while(rs.next()){
            map = new HashMap<String,Object>();
            
            for(int indexOfcolumn=0; indexOfcolumn<sizeOfcolumn; indexOfcolumn++){
                column = metaData.getColumnName(indexOfcolumn + 1);
                map.put(column, rs.getString(column));
            }
            list.add(map);
        }
        
        
        /*
        // 테스트 출력
        for( Map<String, Object> map1 : list ){
            System.out.println("============roomUserLists()============================");
            Iterator<String> it = map1.keySet().iterator();
            while(it.hasNext()){
                String key        = it.next();
                String value    = (String)map1.get(key);
                
                System.out.println(key + ":::" + "\t\t\t" + value);
            }
            System.out.println("============//roomUserLists()==========================");
        }
        */

        st.close();
        connection.close();
        
        return list;
	}
	
	public void chatUserInsert(String roomId, String userId) throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
        
    	String sql = "INSERT INTO chat_user (room_idx, user_idx, state) " + 
	    		"		VALUES " + 
	    		"		(" + 
	    		"		  (SELECT idx FROM chat_room WHERE title=?)," + 
	    		"		  (SELECT idx FROM user WHERE userid=?)," + 
	    		"		  ''" + 
	    		"		)";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql);
    	 
    	pstmt.setString(1, roomId);
    	pstmt.setString(2, userId);
    	
    	int result = pstmt.executeUpdate();
    	dd("chatUserInsert() : "+result);
        
        st.close();
        connection.close();
	}
	
	public Boolean chatRoomExistsCheck(String roomId) throws SQLException, ClassNotFoundException {
		
		Boolean flag = false;
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        st = (Statement) connection.createStatement();
        
        String sql;
        sql = "SELECT * FROM chat_room WHERE title='"+roomId+"'";

        ResultSet rs = st.executeQuery(sql);

        if(rs.isBeforeFirst()){
            flag = true;
        }
        else {
            flag = false;
        } 
        
        rs.close();
        st.close();
        connection.close();
        
        return flag;
	}
	
	public Boolean chatRoomOwnerCheck(String userId) throws SQLException, ClassNotFoundException {
		
		Boolean flag = false;
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        st = (Statement) connection.createStatement();
        
        String sql;
        sql = "SELECT * FROM chat_room WHERE owner='"+userId+"'";
        
        ResultSet rs = st.executeQuery(sql);

        if(rs.isBeforeFirst()){
            flag = true;
        }
        else {
            flag = false;
        } 
        
        rs.close();
        st.close();
        connection.close();
        
        return flag;
	}
	
	// 채팅방 나가기 
	public void chatUserDelete(String userId) throws ClassNotFoundException, SQLException {
		
		Class.forName(JDBC_DRIVER);
		connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		st = (Statement) connection.createStatement();
        
        PreparedStatement pstmt = null;
    	ResultSet rs1 = null;
    	
    	String sql = "DELETE from chat_user WHERE user_idx=(SELECT idx FROM user WHERE userid=?)";
    	pstmt = (PreparedStatement) connection.prepareStatement(sql);
    	pstmt.setString(1, userId);

    	pstmt.execute();
        
        st.close();
        connection.close();
        
	}
	
	
	
	
	private void dd(String msg) {
		System.out.println(msg);
	}
	
	private void dd(Object msg) {
		System.out.println(msg);
	}
}
