package models;

import java.io.Serializable;
import java.util.ArrayList;

public class FollowersModel implements Serializable{
    private ArrayList<FollowModel> followers;

    public FollowersModel(ArrayList<FollowModel> followers) {
        this.followers = followers;
    }

    public ArrayList<FollowModel> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<FollowModel> followers) {
        this.followers = followers;
    }
}
