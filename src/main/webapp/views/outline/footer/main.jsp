<%@ page contentType="text/html; charset=utf-8" %>
<%
	String environment = (String)request.getAttribute("environment");
%>
	 <iframe <%=environment.equals("production")?" class='dn'":""%>name="ifrmHidden" width="100%" height="0" frameborder="0"></iframe>
 	</body>
</html>