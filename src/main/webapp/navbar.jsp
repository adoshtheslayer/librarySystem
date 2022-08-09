<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="uz.pdp.bookshop.service.BookService" %>
<%@ page import="uz.pdp.bookshop.model.Book" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
<html>
<head>
    <title>Title</title>
</head>
<body>
<style>
    .card {
        margin-top: 1%;
        margin-left: 6%;
    }
</style>

<%@include file="navigation.jsp" %>

<section class="container mt-4 ">
    <c:if test="${(total)>1}">

        <div>
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${total}" varStatus="loop">
                    <li class="page-item"><a class="page-link" href="/main?page=${loop.index}">${loop.index}</a></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <div class="row justify-content-around">

        <c:forEach items="${bookList}" var="book">

            <div class="card text-center shadow col-md-3" style="width: 18rem;">
                <img src="files/${book.getImgUrl()}" class="card-img-top" alt="...">
                <div class="card-body">
                    <h5 class="card-title">${book.getTitle()}</h5>
                    <c:forEach items="${book.getAuthors()}" var="author">
                        <p class="card-text">${author.getFullName()}</p>
                    </c:forEach>
                    <p>${book.getCategory().getName()}</p>
                </div>
            </div>
        </c:forEach>
    </div>
</section>


</body>
</html>
