<%@page import="org.kettle.ext.utils.StringEscapeHelper"%>
<%@page import="org.kettle.ext.utils.ExceptionUtils" %>
<%@page pageEncoding="utf-8" %>
<%
	Exception e = (Exception) request.getAttribute("exception");
	String str = ExceptionUtils.toString(e);
	str = StringEscapeHelper.encode(str);
	response.getWriter().write(str);
	
%>