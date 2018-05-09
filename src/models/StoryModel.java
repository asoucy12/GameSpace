package models;

import java.io.Serializable;

public class StoryModel implements Serializable {
    private int storyId = 0;
    private int likeCount = 0;
    private String story;
    private String username;
    private int commentOnStoryID;

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCommentOnStoryID() {
        return commentOnStoryID;
    }

    public void setCommentOnStoryID(int commentOnStoryID) {
        this.commentOnStoryID = commentOnStoryID;
    }
}