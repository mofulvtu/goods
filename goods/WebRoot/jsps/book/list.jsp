<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>图书列表</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/book/list.css'/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/pager/pager.css'/>" />
<script type="text/javascript"
	src="<c:url value='/jsps/pager/pager.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jsps/js/book/list.js'/>"></script>
</head>

<body>

	<ul>

		<c:forEach items="${pb.beanList }" var="book">
			<li>
				<div class="inner">
					<a class="pic" href="<c:url value='/jsps/book/desc.jsp'/>"><img
						src="<c:url value='/${book.image_b }'/>" border="0" /></a>
					<p class="price">
						<span class="price_n">&yen;${book.currPrice }</span> <span
							class="price_r">&yen;${book.price }</span> (<span class="price_s">${book.discount }</span>)
					</p>
					<p>
						<a id="bookname" title="${book.bname }"
							href="<c:url value='/jsps/book/desc.jsp'/>">${book.bname }</a>
					</p>
					<p>
						<a href="<c:url value='/jsps/book/list.jsp'/>" name='P_zz'
							title='${book.author }'>${book.author }</a>
					</p>
					<p class="publishing">
						<span>出 版 社：</span><a href="<c:url value='/jsps/book/list.jsp'/>">${book.press }</a>
					</p>
					<p class="publishing_time">
						<span>出版时间：</span>${book.publishtime }
					</p>
				</div>
			</li>
		</c:forEach>




	</ul>

	<div style="float: left; width: 100%; text-align: center;">
		<hr />
		<br />
		<%@include file="/jsps/pager/pager.jsp"%>
	</div>

</body>

</html>

