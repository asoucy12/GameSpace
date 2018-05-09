package datalayer;

import models.FollowModel;
import models.GameModel;
import models.StoryModel;
import models.UserModel;

import java.io.*;
import java.util.ArrayList;

public class UserDao {

    /**
     * Given a username, return the user model.
     * Returns null, if no such user.
     */
    public static UserModel getUser(String username) {
        if (username == null) {
            return null;
        }

        File file = new File(getFilePath(username));
        return getUser(file);
    }

    /*
     * Given a user, delete it from storage.
     */
    public static void deleteUser(String username) {
        File file = new File(getFilePath(username));
        file.delete();
    }

    /*
     * Save the given user model.
     */
    public static void saveUser(UserModel userModel){
        try {
            File file = new File(getFilePath(userModel.getUsername()));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userModel);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a story ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath(String username) {
        return DaoUtils.storageDirectoryName() + File.separator + "user" + username + ".txt";
    }

    /*
     * Given a username, return the user that's saved in the file.
     * Returns null if not found.
     */
    private static UserModel getUser(File file) {
        UserModel user = null;
        try {
            user = new UserModel();

            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                user = (UserModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return user;
    }

    public static ArrayList<UserModel> getRecommendedUsers(UserModel user){
        ArrayList<GameModel> userLikedGames = LikeGameDao.getLikedGames(user);
        ArrayList<UserModel> allUsers = UserDao.getUsers();
        ArrayList<UserModel> userList = new ArrayList<>();

        for (UserModel u: allUsers){
            //get liked games for u
            ArrayList<GameModel> uLikes = LikeGameDao.getLikedGames(u);
            int gamesInCommon = 0;
            //compare each game in uLikes with each game in userLikedGames
            for (GameModel uGame: uLikes){
                for (GameModel userGame: userLikedGames){
                    //if they are the same, they have a game in common
                    if (uGame.getGameId() == userGame.getGameId()){
                        gamesInCommon++;
                    }
                }
            }

            //if they have games in common, add them to the userList
            if (gamesInCommon > 0){
                userList.add(u);
            }
        }

        return userList;
    }

    public static ArrayList<UserModel> getUsers() {
        ArrayList<UserModel> users = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("user") &&
                    listOfFiles[i].getName().endsWith(".txt")){
                users.add(getUser(listOfFiles[i]));
            }
        }

        return users;
    }

    /**
     * Unit test program.
     *
     * @param args
     */
    public static void main(String[] args) {
        testUserDao();
    }

    private static void testUserDao() {
        String username = "danny";
        UserDao dao = new UserDao();
        UserModel user = new UserModel();
        user.setUsername(username);
        dao.saveUser(user);

        user = dao.getUser(username);
        assert(user != null);
        assert(user.getUsername().compareTo(username) == 0);

        dao.deleteUser(username);
    }

}
