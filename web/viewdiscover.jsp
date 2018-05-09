<%@ page import="models.UserModel" %>
<%@ page import="models.GameModel" %>
<%@ page import="datalayer.FollowDao" %>
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

        UserModel allUsers[] = (UserModel[]) request.getAttribute("users");
        if (allUsers == null) {
            allUsers = new UserModel[0];
        }

        GameModel allGames[] = (GameModel[]) request.getAttribute("allGames");
    %>
<p></p>
<p></p>

    <div class="container">

        <form action="viewDiscover" method="post">

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
                            <li class="active"><a href="viewDiscover">Discover</a></li>
                            <li class="inactive"><a href="viewMe"><!-- %=user.getUsername()% -->Me</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="welcome"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Display the jumbotron -->
            <div class="jumbotron">
                <h1>Find new friends and games!</h1>
            </div>

            <!-- Display a list of users -->
            <div class="container">
                <div class="row">
                    <div class="well well-sm">
                        <h3><p class="text-primary">Users</h3>
                        <div class="pre-scrollable">
                            <ul class="list-group">
                                <%
                                    for (int i = allUsers.length - 1; i >= 0; i--) {
                                %>
                                <li class="list-group-item">
                                    <%=allUsers[i].getUsername()%>
                                </li>
                                <%
                                    }
                                %>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Display a list of games -->
            <div class="container">
                <div class="row">
                    <div class="well well-sm">
                        <h3><p class="text-primary">Games</h3>
                        <div class="pre-scrollable">
                            <ul class="list-group">
                                <%
                                    for (int i = allGames.length - 1; i >= 0; i--) {
                                %>
                                <li class="list-group-item">
                                    <% if (LikeGameDao.didUserLikeGame(allGames[i].getGameId(), user.getUsername())){ %>
                                        <input type="submit" class="btn btn-info" name="<%=allGames[i].getGameId()%>" value="Unlike"> <%=allGames[i].getName()%>
                                    <% } else { %>
                                        <input type="submit" class="btn btn-info" name="<%=allGames[i].getGameId()%>" value="Like"> <%=allGames[i].getName()%>
                                    <% } %>
                                </li>
                                <%
                                    }
                                %>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Input for a new story -->
            <div class="container">
                <div class="row">
                    <div class="well well-sm">
                        <div class="form-group">
                            <label for="searchText">Search: </label>
                            <div class="form-group">
                                <input type="text" class="form-control" id="searchText" name="searchText"
                                       placeholder="Enter username to search for">
                            </div>
                            <!-- Button -->
                            <input type="submit" class="btn btn-info" name="submitButton" value="Submit">
                        </div>
                    </div>
                </div>
            </div>


            <!-- This is a screet input to the post!  Acts as if the user
                 had an input field with the username.
             -->
            <input type="hidden" name="username" value="<%=user.getUsername()%>">

        </form>
    </div>
</body>
</html>
