package datalayer;

import models.LikeGameModel;
import models.GameModel;
import models.UserModel;
import java.io.*;
import java.util.ArrayList;

public class LikeGameDao {

    /**
     * Return likes
     */
    public static ArrayList<LikeGameModel> getLikes() {
        File file = new File(getFilePath());
        return getLikes(file);
    }

    public static void deleteLike(int gameId, String username){
        ArrayList<LikeGameModel> allLikes = getLikes();
        for (int i = 0; i < allLikes.size()-1; i++){
            if (allLikes.get(i).getGameId() == gameId){
                if (allLikes.get(i).getUsername().equalsIgnoreCase(username)){
                    allLikes.remove(i);
                }
            }
        }
        saveLikes(allLikes);
    }

    public static boolean didUserLikeGame(int gameId, String username) {
        ArrayList<LikeGameModel> likes = getLikes();
        if (likes.isEmpty() == false) {
            for (LikeGameModel like : likes) {
                if (like.getGameId() == gameId && like.getUsername().equalsIgnoreCase(username))
                    return true;
            }
        }
        return false;
    }

    /*
     * Given a username, return the user that's saved in the file.
     * Returns null if not found.
     */
    private static ArrayList<LikeGameModel> getLikes(File file) {
        ArrayList<LikeGameModel> likes = null;
        try {
            likes = new ArrayList<LikeGameModel>();

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                likes = (ArrayList<LikeGameModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return likes;
    }

    /*
     * Save the given user model.
     */
    public static void saveLikes(ArrayList<LikeGameModel> likes){
        try {
            File file = new File(getFilePath());
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(likes);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLike(int gameId, String username) {
        if (didUserLikeGame(gameId, username)) {
            return;
        }

        int nLikes = 0;
        ArrayList<LikeGameModel> likes = getLikes();
        likes.add(new LikeGameModel(gameId, username));
        saveLikes(likes);
    }

    /**
     * Gets all the games liked by a user returns them as an arraylist
     * @param user
     * @return
     */
    public static ArrayList<GameModel> getLikedGames(UserModel user){
        ArrayList<GameModel> userLikedGames = new ArrayList<>();
        ArrayList<LikeGameModel> allLikeGameModels = getLikes();
        for (LikeGameModel like: allLikeGameModels){
            //if like was from the user
            if (like.getUsername().equalsIgnoreCase(user.getUsername())){
                //get the game
                GameModel game = GameDao.getGame(like.getGameId());
                //add it to arraylist
                userLikedGames.add(game);
            }
        }
        return userLikedGames;
    }

    /*
     * Given a user, delete it from storage.
     */
    public static void deleteLikes() {
        File file = new File(getFilePath());
        file.delete();
    }

    public static int getNumberOfLikes(int gameId) {
        int nLikes = 0;
        ArrayList<LikeGameModel> likes = getLikes();

        for (LikeGameModel like: likes) {
            if (like.getGameId() == gameId)
                nLikes++;
        }

        return nLikes;
    }

    /**
     * Given a story ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath() {
        return DaoUtils.storageDirectoryName() + File.separator + "likeGame" + ".txt";
    }

}
