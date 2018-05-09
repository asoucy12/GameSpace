<%@ page import="models.UserModel" %>
<%@ page import="models.GameModel" %>
<%@ page import="models.StoryModel" %>
<%@ page import="datalayer.LikeDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="datalayer.FollowDao" %>
<%@ page import="datalayer.LikeGameDao" %>
<%@ page import="datalayer.StoryDao" %>
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

    UserModel postUser = (UserModel) request.getSession().getAttribute("postUser");

    ArrayList<StoryModel> pUserStories = StoryDao.getStoriesByUser(postUser);
    StoryModel[] postUserStories = pUserStories.toArray(new StoryModel[pUserStories.size()]);

    ArrayList<GameModel> gamesList = LikeGameDao.getLikedGames(postUser);
    //Convert to array
    GameModel[] likedGames = gamesList.toArray(new GameModel[gamesList.size()]);

    StoryModel stories[] = (StoryModel[]) request.getAttribute("stories");
    if (stories == null) {
        stories = new StoryModel[0];
    }


%>
<p></p>
<p></p>

<div class="container">

    <form action="viewUser" method="post">

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
                        <li class="active"><a href="viewStories">Feed</a></li>
                        <li class="inactive"><a href="viewDiscover">Discover</a></li>
                        <li class="inactive"><a href="viewMe">Me</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="welcome"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Display the jumbotron -->
        <div class="jumbotron">
            <h1><%=postUser.getUsername()%></h1>
            <% if (!(postUser.getUsername().equalsIgnoreCase(user.getUsername()))){ %>
                <% if(FollowDao.doesUserFollowUser(postUser.getUsername(), user.getUsername())){ %>
                <input type="submit" class="btn btn-info" name="<%=postUser.getUsername()%>" value="Unfollow">
                <% } else { %>
                <input type="submit" class="btn btn-info" name="<%=postUser.getUsername()%>" value="Follow">
                <% }
            }%>
            <h3>Followers: <%=FollowDao.getFollowers(postUser).size()%></h3>
            <h3>Follwing: <%=FollowDao.getFollowing(postUser).size()%></h3>
        </div>

        <!-- Display user info and options -->
        <div class="container">
            <div class="row">
                <div class="col-sm-3"><h2>Games</h2>
                    <!-- display a list of user's liked games-->
                    <% for (int i = likedGames.length-1; i >= 0; i--){ %>
                        <li class="list-group-item"><%=likedGames[i].getName()%></li>
                    <% } %>
                </div>
                <div class="col-sm-6"><h2>Posts</h2>
                    <% for (int i = postUserStories.length - 1; i >= 0; i--) { %>
                    <li class="list-group-item">[<%=postUserStories[i].getUsername()%>] - <%=postUserStories[i].getStory()%>
                        Likes: <%=LikeDao.getNumberOfLikes(postUserStories[i].getStoryId())%>
                    </li>
                    <% } %>
                </div>
                <div class="col-sm-3"><h2>Platforms</h2>
                    N/A
                </div>
            </div>
        </div>

        <input type="hidden" name="username" value="<%=user.getUsername()%>">

    </form>
</div>
</body>
</html>

