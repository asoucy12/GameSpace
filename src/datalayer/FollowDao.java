package datalayer;

import models.FollowModel;
import models.UserModel;

import java.io.*;
import java.util.ArrayList;

public class FollowDao {

    // doesUserFollowUser
    public static boolean doesUserFollowUser(String userBeingFollowed, String userFollowing){
        ArrayList<FollowModel> allFollowers = getAllFollowers();
        for (FollowModel f: allFollowers){
            if (f.getUserBeingFollowed().equalsIgnoreCase(userBeingFollowed)){
                if (f.getUserFollowing().equalsIgnoreCase(userFollowing)){
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<String> getFollowers (UserModel user){
        ArrayList<String> followers = new ArrayList<String>();
        ArrayList<FollowModel> allFollowers = getAllFollowers();
        for (FollowModel f: allFollowers){
            if (f.getUserBeingFollowed().equalsIgnoreCase(user.getUsername()))
                followers.add(f.getUserFollowing());
        }
        return followers;
    }

    public static ArrayList<String> getFollowing (UserModel user){
        ArrayList<String> following = new ArrayList<String>();
        ArrayList<FollowModel> allFollowers = getAllFollowers();
        for (FollowModel f: allFollowers){
            if (f.getUserFollowing().equalsIgnoreCase(user.getUsername()))
                following.add(f.getUserBeingFollowed());
        }
        return following;
    }

    public static void saveFollow(String userBeingFollowed, UserModel userFollowing) {
        if (doesUserFollowUser(userBeingFollowed, userFollowing.getUsername())) {
            return;
        }

        ArrayList<FollowModel> followers = getAllFollowers();
        followers.add(new FollowModel(userFollowing.getUsername(), userBeingFollowed));
        saveFollowers(followers);
    }

    public static void deleteFollow(String userBeingFollowed, UserModel userFollowing){
        ArrayList<FollowModel> allFollowers = getAllFollowers();
        for (int i = 0; i < allFollowers.size()-1; i++){
            if (allFollowers.get(i).getUserBeingFollowed().equalsIgnoreCase(userBeingFollowed)){
                if (allFollowers.get(i).getUserFollowing().equalsIgnoreCase(userFollowing.getUsername())){
                    allFollowers.remove(i);
                }
            }
        }
        saveFollowers(allFollowers);
    }

    public static ArrayList<FollowModel> getAllFollowers() {
        File file = new File(getFilePath());
        return getAllFollowers(file);
    }

    /*
     * Given a username, return the user that's saved in the file.
     * Returns null if not found.
     */
    private static ArrayList<FollowModel> getAllFollowers(File file) {
        ArrayList<FollowModel> followers = null;
        try {
            followers = new ArrayList<FollowModel>();

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                followers = (ArrayList<FollowModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return followers;
    }

    /*
     * Save the given user model.
     */
    public static void saveFollowers(ArrayList<FollowModel> followers){
        try {
            File file = new File(getFilePath());
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(followers);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a story ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath() {
        return DaoUtils.storageDirectoryName() + File.separator + "followers" + ".txt";
    }
}
