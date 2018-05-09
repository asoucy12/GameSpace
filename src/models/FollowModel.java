package models;

import java.io.Serializable;

public class FollowModel implements Serializable{
    private String userFollowing;
    private String userBeingFollowed;

    public FollowModel(String userFollowing, String userBeingFollowed) {
        this.userFollowing = userFollowing;
        this.userBeingFollowed = userBeingFollowed;
    }

    public String getUserFollowing() {
        return userFollowing;
    }

    public void setUserFollowing(String userFollowing) {
        this.userFollowing = userFollowing;
    }

    public String getUserBeingFollowed() {
        return userBeingFollowed;
    }

    public void setUserBeingFollowed(String userBeingFollowed) {
        this.userBeingFollowed = userBeingFollowed;
    }
}
