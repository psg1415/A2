<%@ page contentType="text/html; charset=utf-8" %>
<%
	String method = (String)request.getAttribute("httpMethod");
	String requestURL = (String)request.getAttribute("requestURL");
%>
<h1>없는 페이지 입니다!!!</h1>
<h2><%=method%> / <%=requestURL%></h2>

<button type="button" onclick="history.back();">이전 페이지로 이동</button>