/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.96
 * Generated at: 2019-10-01 02:40:41 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta charset=\"UTF-8\">\n");
      out.write("<title>Chat Test</title>\n");
      out.write("\n");
      out.write("\n");
      out.write("<link rel=\"stylesheet\" href=\"//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">\n");
      out.write("<script src=\"//code.jquery.com/jquery.min.js\"></script>\n");
      out.write("<script src=\"//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js\"></script>\n");
      out.write("\n");
      out.write("<link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css\">\n");
      out.write("<script src=\"//code.jquery.com/jquery.min.js\"></script>\n");
      out.write("<script src=\"//code.jquery.com/ui/1.11.4/jquery-ui.min.js\"></script>\n");
      out.write("\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("<!-- 메시지 표시 영역 -->\n");
      out.write("<div>\n");
      out.write("\t<textarea id=\"messageTextArea\" readonly=\"readonly\" rows=\"10\" cols=\"45\"></textarea>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<select id=\"sel\"></select>\n");
      out.write("\n");
      out.write("<!-- 송신 메시지 텍스트박스 -->\n");
      out.write("<input type=\"text\" id=\"messageText\" size=\"50\" />\n");
      out.write("\n");
      out.write("<!-- 송신 버튼 -->\n");
      out.write("<input type=\"button\" value=\"Send\" onclick=\"sendMessage()\" />\n");
      out.write("\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("\n");
      out.write("\n");
      out.write("//웹소켓 초기화\n");
      out.write("var webSocket = new WebSocket(\"ws://localhost:8080/WebSocketEx/broadsocket\");\n");
      out.write("var messageTextArea = document.getElementById(\"messageTextArea\");\n");
      out.write("\n");
      out.write("//웹 소켓이 연결되었을 때 호출되는 이벤트\n");
      out.write("webSocket.onopen = function(message){\n");
      out.write("\tmessageTextArea.value += \"Server connect...\\n\";\n");
      out.write("};\n");
      out.write("\n");
      out.write("//웹 소켓이 닫혔을 때 호출되는 이벤트\n");
      out.write("webSocket.onclose = function(message){\n");
      out.write("\tmessageTextArea.value += \"Server Disconnect...\\n\";\n");
      out.write("};\n");
      out.write("\n");
      out.write("//웹 소켓이 에러가 났을 때 호출되는 이벤트\n");
      out.write("webSocket.onerror = function(message){\n");
      out.write("\tmessageTextArea.value += \"error...\\n\";\n");
      out.write("};\n");
      out.write("\n");
      out.write("\n");
      out.write("//메시지가 오면 messageTextArea요소에 메시지를 추가한다.\n");
      out.write("webSocket.onmessage = function processMessge(message){\n");
      out.write("\t\n");
      out.write("\t//Json 풀기\n");
      out.write("\tvar jsonDataOri = JSON.parse(message.data);\n");
      out.write("\tvar jsonData = jsonDataOri.chat;\n");
      out.write("\tconsole.log(jsonDataOri);\n");
      out.write("\t\n");
      out.write("\t\tif(jsonData.message != null) {\n");
      out.write("\t\t\tvar name = jsonData.username;\n");
      out.write("\t\t\tvar me = jsonData.me;\n");
      out.write("\t\t\tvar msg = jsonData.message;\n");
      out.write("\t\t\tvar allMembers = JSON.parse(jsonData.allMembers);\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t$('#sel').empty();\n");
      out.write("\t\t\tfor(var count = 0; count < allMembers.length; count++){                \n");
      out.write("                var option = $(\"<option>\"+ allMembers[count]+\"</option>\");\n");
      out.write("                $('#sel').append(option);\n");
      out.write("            }\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t\n");
      out.write("\t\t\tmessageTextArea.value += name + \" : \"+ msg + \"\\n\"\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t\n");
      out.write("\t\t};\n");
      out.write("}\n");
      out.write("\n");
      out.write("\n");
      out.write("//메시지 보내기\n");
      out.write("function sendMessage(){\n");
      out.write("\tvar messageText = document.getElementById(\"messageText\");\n");
      out.write("\twebSocket.send(messageText.value);\n");
      out.write("\tmessageText.value = \"\";\n");
      out.write("}\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
