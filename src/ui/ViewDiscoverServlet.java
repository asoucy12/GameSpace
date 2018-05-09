package ui;

import datalayer.GameDao;
import datalayer.LikeGameDao;
import datalayer.StoryDao;
import datalayer.UserDao;
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

public class ViewDiscoverServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);
        String likeButtonName = getButtonNameGivenValue(request, "Like");
        String unlikeButtonName = getButtonNameGivenValue(request, "Unlike");
        String searchText=request.getParameter("searchText");
        String buttonValue = request.getParameter("submitButton");

        if (likeButtonName != null){
            int gameID = Integer.parseInt(likeButtonName);
            LikeGameDao.saveLike(gameID, user.getUsername());
        }
        if (unlikeButtonName != null){
            int gameID = Integer.parseInt(unlikeButtonName);
            LikeGameDao.deleteLike(gameID, user.getUsername());
        }

        if (buttonValue != null && buttonValue.equals("Submit")){
            UserModel u = UserDao.getUser(searchText);
            if (u != null) {
                handleGoToUser(request, response, u, user);
            }
        }

        // Load any data we need on the page into the request.
        request.getSession().setAttribute("user", user);
        loadUsersIntoRequest(request);
        //loadRecommendedUsersIntoRequest(request);
        loadGamesIntoRequest(request);

        // Show the page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdiscover.jsp");
        dispatcher.forward(request, response);

    }

    private void handleGoToUser(HttpServletRequest request, HttpServletResponse response, UserModel postUser, UserModel user) throws ServletException, IOException {
        request.setAttribute("user", user);
        request.getSession().setAttribute("postUser", postUser);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewuser.jsp");
        dispatcher.forward(request, response);
    }

    private UserModel loadUserFromRequest(HttpServletRequest request) {
        String username = (String)request.getSession().getAttribute("username");
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
        //loadRecommendedUsersIntoRequest(request);
        loadGamesIntoRequest(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewdiscover.jsp");
        dispatcher.forward(request, response);
    }

    private void loadUsersIntoRequest(HttpServletRequest request) {
        ArrayList<UserModel> usersList = UserDao.getUsers();

        // We're going to convert the array list to an array because it works better in the JSP.
        UserModel[] users = usersList.toArray(new UserModel[usersList.size()]);
        request.setAttribute("users", users);
    }

    private void loadRecommendedUsersIntoRequest(HttpServletRequest request) {
        UserModel user = loadUserFromRequest(request);
        ArrayList<UserModel> userList = UserDao.getRecommendedUsers(user);

        UserModel[] recommendedUsers = userList.toArray(new UserModel[userList.size()]);
        request.setAttribute("recommendedUsers", recommendedUsers);
    }

    private void loadGamesIntoRequest(HttpServletRequest request) {
        ArrayList<GameModel> gamesList = GameDao.getGames();
        GameModel[] allGames = gamesList.toArray(new GameModel[gamesList.size()]);
        request.setAttribute("allGames", allGames);
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
