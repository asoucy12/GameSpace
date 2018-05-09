package ui;

import datalayer.*;
import models.GameModel;
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

public class ViewAddGameServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);
        String name=request.getParameter("gameName");
        String rating=request.getParameter( "gameRating");

        String addButtonName = getButtonNameGivenValue(request, "Add");
        if (addButtonName != null){
            handleAddGameButton(name, rating, request, response, user);
        }

        // Load any data we need on the page into the request.
        request.getSession().setAttribute("user", user);
        loadUsersIntoRequest(request);

        // Show the page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewaddgame.jsp");
        dispatcher.forward(request, response);

    }

    private void handleAddGameButton(String name, String rating, HttpServletRequest request, HttpServletResponse response, UserModel user) throws ServletException, IOException {
        //add the game to the user's likes
        //create the game - this will not create if the game already exists
        char r = rating.charAt(0);
        GameDao.saveGame(name, r);
        //add the like - this will check if the user already liked the game for us
        GameModel game = GameDao.getGame(name);
        LikeGameDao.saveLike(game.getGameId(), user.getUsername());

        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewme.jsp");
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before we go the page to display the stories, we need to get the stories.
        // And then shove the stories in to the request.

        loadUsersIntoRequest(request);
        loadStoriesIntoRequest(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewaddgame.jsp");
        dispatcher.forward(request, response);
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

    private void loadUsersIntoRequest(HttpServletRequest request) {
        ArrayList<UserModel> usersList = UserDao.getUsers();

        // We're going to convert the array list to an array because it works better in the JSP.
        UserModel[] users = usersList.toArray(new UserModel[usersList.size()]);
        request.setAttribute("users", users);
    }

    private void logRequestParameters(HttpServletRequest request) {
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

