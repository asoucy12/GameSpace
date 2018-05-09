<%@ page import="models.StoryModel" %>
<%@ page import="models.UserModel" %>
<%@ page import="datalayer.LikeDao" %>
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

    StoryModel story = (StoryModel) request.getAttribute("story");
    if (story == null) {
        story = new StoryModel();
        story.setStory("Unavailable.");
    }

    StoryModel comments[] = (StoryModel[]) request.getAttribute("storycomments");
    if (comments == null){
        comments = new StoryModel[0];
    }
%>
<p></p>
<p></p>
<div class="container">

    <form action="viewStory" method="post">

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
                        <li class="inactive"><a href="">Me</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="welcome"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Display a story -->
        <div class="container">
            <div class="well well-sm">
                <h3><p class="text-primary"><%=story.getUsername()%></h3>
                <div class="pre-scrollable">
                    <%=story.getStory()%>
                </div>
            </div>

            <!-- Comments on story -->
            <!-- Display a list of stories -->
            <div class="well well-sm">
                <%=LikeDao.getNumberOfLikes(story.getStoryId())%> Likes
                <h3><p class="text-primary"><%=comments.length%> Comments</h3>
                <div class="pre-scrollable">
                    <ul class="list-group">
                        <%
                            for (int i = comments.length - 1; i >= 0; i--)
                            {
                        %>
                        <li class="list-group-item">
                            <% if (comments[i].getUsername().equals("anonymous")) { %>
                            <span class="glyphicon glyphicon-user"></span>
                            <% } else { %>
                            <span class="glyphicon glyphicon-user" style="color:blue" ><%=comments[i].getUsername()%></span>
                            <% } %>
                            <%=comments[i].getStory()%>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Input for a new comment -->
        <div class="container">
            <div class="row">
                <div class="well well-sm">
                    <div class="form-group">
                        <label for="storyText">Comment</label>
                        <div class="form-group">
                            <input type="text" class="form-control" id="storyText" name="storyText" placeholder="Add a comment:">
                        </div>
                        <input type="submit" class="btn btn-info" name="submitButton" value="Submit">
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>
</body>
</html>
