package ui;

import datalayer.StoryDao;
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

public class ViewMeServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);

        String addButtonName = getButtonNameGivenValue(request, "Add");

        if (addButtonName != null){
            if (addButtonName.equalsIgnoreCase("addGame")){
                handleAddGameButton(request, response, user);
            }
            else if (addButtonName.equalsIgnoreCase("addPlatform")){
                handleAddPlatformButton(request, response, user);
            }
        }

        // Load any data we need on the page into the request.
        request.getSession().setAttribute("user", user);
        loadUsersIntoRequest(request);

        // Show the page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewme.jsp");
        dispatcher.forward(request, response);

    }

    private void handleAddPlatformButton(HttpServletRequest request, HttpServletResponse response, UserModel user) throws ServletException, IOException {
        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewaddplatform.jsp");
        dispatcher.forward(request, response);
    }

    private void handleAddGameButton(HttpServletRequest request, HttpServletResponse response, UserModel user) throws ServletException, IOException {
        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewaddgame.jsp");
        dispatcher.forward(request, response);
    }

    private UserModel loadUserFromRequest(HttpServletRequest request) {
        String username=(String)request.getSession().getAttribute("username");
        UserModel user = UserDao.getUser(username);

        // If there is no user for some weird reason, just use anonymous.
//        if (user == null) {
//            user = new UserModel();
//            user.setUsername("anonymous");
//        }

        return user;
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // Before we go the page to display the stories, we need to get the stories.
        // And then shove the stories in to the request.

        loadUsersIntoRequest(request);
        loadStoriesIntoRequest(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewme.jsp");
        dispatcher.forward(request, response);
    }

    private void loadUsersIntoRequest(HttpServletRequest request) {
        ArrayList<UserModel> usersList = UserDao.getUsers();

        // We're going to convert the array list to an array because it works better in the JSP.
        UserModel[] users = usersList.toArray(new UserModel[usersList.size()]);
        request.setAttribute("users", users);
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

    private void logRequestParameters(javax.servlet.http.HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }

    private String getButtonNameGivenValue(HttpServletRequest request, String buttonValue){
        Enumeration<String> params = request.getParameterNames();

        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if(paramValue.equals(buttonValue)){
                return paramName;
            }
        }

        return null;
    }

}

