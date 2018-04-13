<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<script type="text/javascript" src="${ctx}/resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>
 	<form name="myform" action="${ctx}/producerController/sendMessage" method="post">
 		messageId:<input type="text" name="messageId"><br/>
 		messageType:<input type="text" name="messageType" value="textMessage" readonly="readonly"><br/>
 		messageSendType:<select name="messageSendType">
 							<option value="1">点对点</option>
 							<option value="2">订阅</option>
 						</select><br/>
 		messageTitle:<input type="text" name="messageTitle"><br/>
 		messageContent:<input type="text" name="messageContent"><br/>
 		fromUser:<input type="text" name="fromUser"><br/>
 		toUser:<input type="text" name="toUser"><br/>
 		messageFeature.repeat:<input type="text" name="messageFeature.repeat"><br/>
 		messageFeature.period:<input type="text" name="messageFeature.period"><br/>
 		messageFeature.delay:<input type="text" name="messageFeature.delay"><br/>
 		 <input type="submit" value="Submit" />
 		<input type="reset" value="Rest"/>
 	</form>
</body>
</html>