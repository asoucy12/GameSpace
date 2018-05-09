package models;

import java.io.Serializable;

public class LikeGameModel implements Serializable {
    private int gameId; // ID of game that was liked
    private String username; // Name of person that liked story

    public LikeGameModel(int storyId, String username) {
        this.gameId = storyId;
        this.username = username;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
