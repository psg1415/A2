<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.HashSet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
	    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
    <meta name="viewport"
      content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="${rootURL}/resources/css/style.css">
    <link rel="stylesheet" href="${rootURL}/resources/css/sg.css">
    <link rel="stylesheet" href="${rootURL}/resources/css/sy.css">
		<meta charset='utf-8'>
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
		<link rel="stylesheet" type="text/css" href="${rootURL}/resources/css/style.css${version}" />
		<c:forEach var="css" items="${addCss}">
		<link rel="stylesheet" type="text/css" href="${rootURL}/resources/css/${css}.css${version}" />
		</c:forEach>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
		<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
		<script type="text/javascript" src="${rootURL}/resources/js/layer.js${version}"></script>
		<script type="text/javascript" src="${rootURL}/resources/js/common.js${version}"></script>
		<c:forEach var="script" items="${addScripts}">
		<script type="text/javascript" src="${rootURL}/resources/js/${script}.js${version}"></script>
		</c:forEach>
		<title><c:out value="${pageTitle}" /></title>
	</head>
	<body class="${bodyClass} layout_width">
