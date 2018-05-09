package models;

import java.io.Serializable;

public class LikeModel implements Serializable {
    private int storyId; // ID of story that was liked
    private String username; // Name of person that liked story

    public LikeModel(int storyId, String username) {
        this.storyId = storyId;
        this.username = username;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

