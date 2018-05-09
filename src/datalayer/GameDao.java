package datalayer;

import models.GameModel;

import java.io.*;
import java.util.ArrayList;

public class GameDao {

    public static boolean doesGameExist(String name){
        ArrayList<GameModel> games = getGames();
        if (games.isEmpty() == false) {
            for (GameModel g : games) {
                if (g.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static GameModel getGame(String name){
        ArrayList<GameModel> games = getGames();
        for (GameModel g: games){
            if(g.getName().equalsIgnoreCase(name)){
                return g;
            }
        }
        return null;
    }

    /**
     * Given a game ID, return the story.
     */
    public static GameModel getGame(int gameId) {
        File file = new File(getFilePath(gameId));
        return getGame(file);
    }

    /*
     * Given a game ID, delete it from storage.
     */
    public static void deleteGame(int gameId) {
        File file = new File(getFilePath(gameId));
        file.delete();
    }

    /*
     * Save the given story model.  Make sure you've set
     * the ID in the story model.
     */
    public static void saveGame(GameModel gameModel){
        try {
            File file = new File(getFilePath(gameModel.getGameId()));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameModel);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a game ID and story text, make a story model
     * and save it.
     */
    public static void saveGame(String gameName, char rating) {
        if (doesGameExist(gameName)){
            return;
        }
        GameModel game = new GameModel();
        game.setName(gameName);
        game.setGameID(UniqueIdDao.getID());
        game.setRating(rating);
        saveGame(game);
    }

    /**
     * Return all saved games.
     */
    public static ArrayList<GameModel> getGames() {
        ArrayList<GameModel> games = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("game") &&
                    listOfFiles[i].getName().endsWith(".txt")){
                games.add(getGame(listOfFiles[i]));
            }
        }

        return games;
    }

    /**
     * Given a game ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath(int gameId) {
        return DaoUtils.storageDirectoryName() + File.separator + "game" + gameId + ".txt";
    }

    /*
     * Given a game filename, return the game that's saved in the file.
     */
    private static GameModel getGame(File file) {
        GameModel game = null;
        try {
            game = new GameModel();

            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                game = (GameModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return game;
    }

}
