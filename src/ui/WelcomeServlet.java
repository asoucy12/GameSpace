package ui;

import datalayer.UserDao;
import models.UserModel;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class WelcomeServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * The post method is called by the browser when the user presses the button
     *
     * @param request The request has info on filled in fields and button presses.
     * @param response We use this to give the browser a response.
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        UserModel user = null;

        // Load data from the request
        String buttonValue = request.getParameter("button");
        String username=request.getParameter("username");
        String password=request.getParameter( "password");


        // Create an account
        if (buttonValue != null && buttonValue.equals("Create Account")){
            if (username != null && username.length() >= 0 && password != null && password.length() >= 0){
                user = new UserModel();
                user.setUsername(username);
                user.setPassword(password);
                UserDao.saveUser(user);
                RequestDispatcher dispatcher=request.getRequestDispatcher("/viewStories");
                dispatcher.forward(request, response);
            }
            else{
                String errorMessage = "Please fill in the username and password fields";
                request.setAttribute("errorMessage", errorMessage);
            }
        }

        // Or log in
        else if (buttonValue != null && buttonValue.equals("LogIn")){
            user = UserDao.getUser(username);
            if (user != null && username != null && username.length() > 0 && password != null && password.length() > 0){
                if(password.equals(user.getPassword())){
                    RequestDispatcher dispatcher=request.getRequestDispatcher("/viewStories");
                    dispatcher.forward(request, response);
                }
                else{
                    String errorMessage = "Incorrect password";
                    request.setAttribute("errorMessage", errorMessage);
                }
            }
            else{
                String errorMessage = "Incorrect username or password";
                request.setAttribute("errorMessage", errorMessage);
            }
            if(user != null){
                request.getSession().setAttribute("username", user.getUsername());
                request.getSession().setAttribute("user", user);
            }
//            if (user == null) {
//                String error = "Incorect username/password.";
//                request.setAttribute("errorMessage", error);
//                // We don't know who this is.
//                // We're going to stay on this page.
//                RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
//                dispatcher.forward(request, response);
//                return;
//            }
//            else {
//                if(password != user.getPassword()) {
//                    String error = "Incorect password.";
//                    request.setAttribute("errorMessage", error);
//                    RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
//                    dispatcher.forward(request, response);
//                    return;
//                }
//            }
        }

        // Load any data we need on the page into the request.
        RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * The get method is invoked when the user goes to the page by browser URI.
     */
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
        dispatcher.forward(request, response);
    }

}
