<%@ page import="models.UserModel" %>
<%@ page import="models.StoryModel" %>
<%@ page import="datalayer.LikeDao" %>
<%@ page import="datalayer.FollowDao" %>
<%@ page import="models.GameModel" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="datalayer.LikeGameDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="resources/style.css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
      integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
      integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
        crossorigin="anonymous"></script>

<head>
    <title>GameSpace</title>
</head>
<body>

<%
    UserModel user = (UserModel) request.getSession().getAttribute("user");
    if (user == null) {
        user = new UserModel();
        user.setUsername("anonymous");
    }

    StoryModel stories[] = (StoryModel[]) request.getAttribute("stories");
%>
<p></p>
<p></p>

<div class="container">

    <form action="viewAddGame" method="post">

        <!-- Navigation Bar -->
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li class="inactive"><a href="viewStories">Feed</a></li>
                        <li class="inactive"><a href="viewDiscover">Discover</a></li>
                        <li class="inactive"><a href="viewMe">Me</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="welcome"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Display user info and options -->
        <div class="container">
            <div class="form-group">
                <input type="text" name="gameName" class="form-control" id="gameName" placeholder="Enter game name:">
                <input type="text" name="gameRating" class="form-control" id="gameRating" placeholder="Enter game rating:">
            </div>
            <div class="container">
                <input type="submit" class="btn btn-info" name="addGame" value="Add">
            </div>
        </div>

        <!-- Input for a new story -->
        <%--<div class="container">--%>
            <%--<div class="row">--%>
                <%--<div class="well well-sm">--%>
                    <%--<div class="form-group">--%>
                        <%--<label for="searchText">Search: </label>--%>
                        <%--<div class="form-group">--%>
                            <%--<input type="text" class="form-control" id="searchText" name="searchText"--%>
                                   <%--placeholder="Enter username or game to search for">--%>
                        <%--</div>--%>
                        <%--<!-- Button -->--%>
                        <%--<input type="submit" class="btn btn-info" name="submitButton" value="Submit">--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>


        <!-- This is a screet input to the post!  Acts as if the user
             had an input field with the username.
         -->
        <input type="hidden" name="username" value="<%=user.getUsername()%>">

    </form>
</div>
</body>
</html>

