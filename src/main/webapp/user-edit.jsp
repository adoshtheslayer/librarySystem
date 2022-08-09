<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ page import="uz.pdp.bookshop.model.User" %>
<%@ page import="uz.pdp.bookshop.service.UserService" %>
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
    <title>EDIT USER</title>
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
    <h1 class="text-primary">Edit User</h1>
    <hr>
    <div class="row">
        <!-- left column -->
        <div class="col-md-3">
            <div class="text-center">
                <img src="https://bootdey.com/img/Content/avatar/avatar7.png" class="avatar img-circle img-thumbnail"
                     alt="avatar">
            </div>
        </div>

        <!-- edit form column -->
        <div class="col-md-9 personal-info">
            <div class="alert alert-info alert-dismissable">
            </div>
            <h3>Personal info</h3>

            <c:forEach items="${userList}" var="user">
            <form action="/edit-user?id=${user.getId()}" method="post">
                <div class="form-group">
                    <label class="col-lg-3 control-label">Full name:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="fullname" type="text" value="${user.getFullName()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-3 control-label">Username:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="username" type="text" value="${user.getUsername()}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="roleId">Role:</label>
                    <select required class="form-control" id="roleId" name="roleId">
                        <option selected value="${user.getRoleId()}">${user.getRoleName()}</option>
                        <c:forEach items="${roleList}" var="role">
                            <option value="${role.getId()}">${role.getName()}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label class="col-lg-3 control-label">Password:</label>
                    <div class="col-lg-8">
                        <input class="form-control" name="password" type="text" value="${user.getPassword()}">
                    </div>
                </div>
                </c:forEach>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
        </div>
    </div>
</div>
<hr>

</body>
</html>
