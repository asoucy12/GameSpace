<%@ page import="models.StoryModel" %>
<%@ page import="models.UserModel" %>
<%@ page import="datalayer.LikeDao" %>
<%@ page import="datalayer.FollowDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<title>GameSpace</title>
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

</head>
<body>
<!-- Let's start by loading information we expect in the request.
     For any info missing, we'll just fake it.
  -->
<%
    UserModel user = (UserModel) request.getSession().getAttribute("user");
    if (user == null) {
        user = new UserModel();
        user.setUsername("anonymous");
    }

    StoryModel stories[] = (StoryModel[]) request.getAttribute("stories");
    if (stories == null) {
        stories = new StoryModel[0];
    }

    int allReviews = 0;
    for(int i = stories.length-1; i >= 0; i--){
        if(stories[i].getCommentOnStoryID() == 0){
            if (FollowDao.doesUserFollowUser(stories[i].getUsername(), user.getUsername()) || stories[i].getUsername().equalsIgnoreCase(user.getUsername())){
                allReviews++;
            }
        }
    }
%>
<p></p>
<p></p>
<div class="container">

    <form action="viewStories" method="post">

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
            <h1>Talk about your favorite games!</h1>
        </div>

        <!-- Input for a new story -->
        <div class="container">
            <div class="row">
                <div class="well well-sm">
                    <div class="form-group">
                        <label for="storyText">New Post:</label>
                        <div class="form-group">
                            <input type="text" class="form-control" id="storyText" name="storyText"
                                   placeholder="What's on your mind?">
                        </div>
                        <!-- Button -->
                        <input type="submit" class="btn btn-info" name="submitButton" value="Submit">
                    </div>
                </div>
            </div>
        </div>


        <!-- Display a list of stories -->
        <div class="container">
            <div class="row">
                <div class="well well-sm">
                    <h3><p class="text-primary"><%=allReviews%> Feed</h3>
                    <div class="pre-scrollable">
                        <ul class="list-group">
                            <%
                                for (int i = stories.length - 1; i >= 0; i--) {
                                    if (stories[i].getCommentOnStoryID() != 0){
                                        continue;
                                    }
                                    else{
                                        if (FollowDao.doesUserFollowUser(stories[i].getUsername(), user.getUsername()) || stories[i].getUsername().equalsIgnoreCase(user.getUsername())){
                            %>
                                <li class="list-group-item">
                                    <div class="row">
                                        <div class="col-12 col-md-9">
                                            <input type="submit" class="btn btn-info" name="<%=stories[i].getStoryId()%>" value="User">[<%=stories[i].getUsername()%>] - <%=stories[i].getStory()%>
                                        </div>
                                        <div class="col-6 col-md-3">
                                            <%=LikeDao.getNumberOfLikes(stories[i].getStoryId())%> Likes
                                            <div class="input-group">
                                                <% if (LikeDao.didUserLikeStory(stories[i].getStoryId(), user.getUsername())){ %>
                                                    <input type="submit" class="btn btn-info" name="<%=stories[i].getStoryId()%>" value="Unlike">
                                                <% } else { %>
                                                    <input type="submit" class="btn btn-info" name="<%=stories[i].getStoryId()%>" value="Like">
                                                <% } %>
                                                <input type="submit" class="btn btn-info" name="<%=stories[i].getStoryId()%>" value="View">
                                                <% if (user.getUsername().equalsIgnoreCase(stories[i].getUsername())){ %>
                                                <input type="submit" class="btn btn-info" name="<%=stories[i].getStoryId()%>" value="Delete">
                                                <% }%>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <% } %>
                            <% } %>
                            <%
                                }
                            %>
                        </ul>
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
