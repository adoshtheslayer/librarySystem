<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ page import="uz.pdp.bookshop.model.User" %>
<%@ page import="uz.pdp.bookshop.service.UserService" %>
<%@ page import="uz.pdp.bookshop.service.BookService" %>
<%@ page import="uz.pdp.bookshop.model.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<html>
<head>
    <title>EDIT BOOK</title>
</head>
<body>
<style>

    body {
        margin-top: 20px;
    }

    .avatar {
        width: 200px;
        height: 200px;
    }
</style>

<div class="container bootstrap snippets bootdey">
    <h1 class="text-primary">Edit Book</h1>
    <hr>
    <div class="row">
        <!-- left column -->
        <c:forEach items="${bookList}" var="book">
        <div class="col-md-3">
            <div class="text-center">
                <img src="files/${book.getImgUrl()}" class="avatar img-circle img-thumbnail" alt="${book.getTitle()}">
            </div>
        </div>

        <!-- edit form column -->
        <div class="col-md-9 personal-info">
            <div class="alert alert-info alert-dismissable">
            </div>
            <h3>Book info</h3>
            <form action="/edit-book?id=${book.getId()}" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label class="col-lg-3 control-label">Title:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="title" type="text" value="${book.getTitle()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-3 control-label">Quantity:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="quantity" type="text" value="${book.getQuantity()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-3 control-label">Isbn:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="isbn" type="text" value="${book.getIsbn()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-3 control-label">Date:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="year" type="text" value="${book.getDate()}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="categoryId">Category:</label>
                    <select required class="form-control" id="categoryId" name="categoryId">
                        <c:forEach items="${categoryList}" var="category">
                            <c:if test="${book.getCategoryId()==category.getId()}"><option selected value="${book.getCategoryId()}">${category.getName()} </option></c:if>
                            <option value="${category.getId()}">${category.getName()}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="authorsIds">Authors:</label>
                    <select id="authorsIds"
                            class="selectpicker form-control"
                            multiple
                            aria-label="Please select authors"
                            data-live-search="true"
                            name="authorsIds">
                        <c:forEach items="${authorList}" var="author">
                            <option value="${author.getId()}">${author.getFullName()}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="image">Upload cover image:</label>
                    <input class="form-control-file" id="image" type="file" name="image">
                </div>
                </c:forEach>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
