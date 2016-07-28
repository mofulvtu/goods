<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>left</title>
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>"
	type="text/css" media="all">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/left.css'/>">
<script language="javascript">
	/*
	 * 1.对象名必须与第一个参数相同
	 * 2.第二个参数是显示在菜单上的大标题
	 */
	var bar = new Q6MenuBar("bar", "集创网上书城");
	$(function() {
		bar.colorStyle = 4;//指定配色样式，一共0，1，2，3，4 这几种
		bar.config.imgDir = "<c:url value='/menu/img/'/>";//小工具所有图片的路径
		bar.config.radioButton = true;//是否排斥，多个一级分类是否排斥

		/*
		 1.程序设计：一级分类名称
		 2.Java JavaScript:二级分类名称
		 3./goods/jsps/book/list.jsp ： 点击二级分类后链接到的URL
		 4.body: 表示链接的内容在哪个框架页中显示
		 */
       /*  alert("进来了");
		alert("${parents}");
		alert("${params}"); */
		<c:forEach items="${parents}" var="parent">
			<c:forEach items="${parent.children}" var="child">
			bar.add("${parent.cname}", "${child.cname}", "/goods/jsps/book/list.jsp", "body");
			</c:forEach>
		</c:forEach>
		bar.add("程序设计", "JSP", "/goods/jsps/book/list.jsp", "body");
		bar.add("程序设计", "Java Javascript", "/goods/jsps/book/list.jsp", "body");
		bar.add("程序设计", "C C++ VC VC++", "/goods/jsps/book/list.jsp", "body");
		
		bar.add("办公室用书", "微软Office", "/goods/jsps/book/list.jsp", "body");
		bar.add("办公室用书", "计算机初级入门", "/goods/jsps/book/list.jsp", "body");
		
		bar.add("图形 图像 多媒体", "Photoshop", "/goods/jsps/book/list.jsp", "body");
		bar.add("图形 图像 多媒体", "3DS MAX", "/goods/jsps/book/list.jsp", "body");
		bar.add("图形 图像 多媒体", "网页设计", "/goods/jsps/book/list.jsp", "body");
		bar.add("图形 图像 多媒体", "Flush", "/goods/jsps/book/list.jsp", "body");
		
		bar.add("操作系统/系统开发", "Windows", "/goods/jsps/book/list.jsp", "body");
		bar.add("操作系统/系统开发", "Linux", "/goods/jsps/book/list.jsp", "body");
		bar.add("操作系统/系统开发", "系统开发", "/goods/jsps/book/list.jsp", "body");

		$("#menu").html(bar.toString());
	});
</script>
</head>

<body>
	<div id="menu"></div>
</body>
</html>
