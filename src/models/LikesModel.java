package models;

import java.io.Serializable;
import java.util.ArrayList;

public class LikesModel implements Serializable {
    private ArrayList<LikeModel> likes;

    public LikesModel(ArrayList<LikeModel> likes) {
        this.likes = likes;
    }

    public ArrayList<LikeModel> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<LikeModel> likes) {
        this.likes = likes;
    }
}