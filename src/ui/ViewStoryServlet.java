package ui;

import datalayer.StoryDao;
import datalayer.UniqueIdDao;
import datalayer.UserDao;
import models.StoryModel;
import models.UserModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewStoryServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * The post method is called by the browser when the user presses the button
     *
     * @param request The request has info on filled in fields and button presses.
     * @param response We use this to give the browser a response.
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request. This may or may not be there
        // depending on what the user did.
        UserModel user = loadUserFromRequest(request);
        String commentText = request.getParameter("storyText");
        String buttonValue = request.getParameter("submitButton");

        // We stored the id of the story being displayed in the session in ViewStoriesServlet.
        Integer storyIdAsInteger = (Integer) request.getSession().getAttribute("storyId");

        int storyBeingDisplayedId = 0;
        if (storyIdAsInteger != null) {
            storyBeingDisplayedId = storyIdAsInteger.intValue();
        }

        // Maybe the user submitted a comment.
        if (buttonValue != null && buttonValue.equals("Submit")){
            addStoryComment(user, storyBeingDisplayedId, commentText);
        }

        // Show the page
        showViewStoryPage(request, response, user, storyBeingDisplayedId);
    }

    private void showViewStoryPage(HttpServletRequest request, HttpServletResponse response, UserModel user, int storyId) throws ServletException, IOException {
        StoryModel story = StoryDao.getStory(storyId);

        // Load any data we need on the page into the request.
        request.getSession().setAttribute("user", user);
        request.setAttribute("story", story);


        loadCommentsOnStoryIntoRequest(request, storyId);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstory.jsp");
        dispatcher.forward(request, response);
    }

    private void loadCommentsOnStoryIntoRequest(HttpServletRequest request, int storyId) {
        ArrayList<StoryModel> storiesList = StoryDao.getStoriesThatAreComments(storyId);

        // We're going to convert the array list to an array because it works better in the JSP.
        StoryModel[] stories = storiesList.toArray(new StoryModel[storiesList.size()]);
        request.setAttribute("storycomments", stories);
    }

    /**
     * Grab the username from the request and create a user model.
     */
    private UserModel loadUserFromRequest(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        UserModel user = UserDao.getUser(username);

        // If there is no user for some weird reason, just use anonymous.
//        if (user == null) {
//            user = new UserModel();
//            user.setUsername("anonymous");
//        }

        return user;
    }

    /**
     * The get method is called if the user directly invokes the URI.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before we go the page to display the stories, we need to get the stories.
        // And then shove the stories in to the request.

        UserModel user = loadUserFromRequest(request);
        String storyIdAsString =request.getParameter("storyId");
        int storyBeingDisplayedId = 0;
        if (storyIdAsString != null) {
            storyBeingDisplayedId = Integer.parseInt(storyIdAsString);
        }

        showViewStoryPage(request, response, user, storyBeingDisplayedId);
    }

    /**
     * Retrieve all the stories and put them in the request.
     * We can then use then in the JSP file.
     *
     * @param request
     */
    private void loadStoriesIntoRequest(HttpServletRequest request) {
        ArrayList<StoryModel> storiesList = StoryDao.getStories();

        // We're going to convert the array list to an array because it works better in the JSP.
        StoryModel[] stories = storiesList.toArray(new StoryModel[storiesList.size()]);
        request.setAttribute("stories", stories);
    }

    /**
     * Save a story.
     */
    private void addStoryComment(UserModel user, int storyBeingCommentedOnId, String comment) {
        if (comment != null && comment.length() > 0 && user != null) {
            StoryDao.saveStory(UniqueIdDao.getID(), comment, user.getUsername(), storyBeingCommentedOnId);
        }
    }

    /**
     * This method is useful in debugging what you got back in the
     * response from the user.
     *
     * @param request
     */
    private void logRequestParameters(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            logger.info("Parameter Name - " + paramName + ", Value - " + request.getParameter(paramName));
        }

    }

    private String getButtonNameGivenValue(HttpServletRequest request, String buttonValue) {
        Enumeration<String> params = request.getParameterNames();

        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue.equals(buttonValue)) {
                return paramName;
            }
        }

        return null;
    }

}
