package me.topeestla.tictactoe.message;

import me.topeestla.tictactoe.enums.JoinStatus;

/**
 * @author TopeEstLa
 */
public class JoinCallbackMessage {

    private Long gameId;
    private String playerName;
    private JoinStatus status;

    public JoinCallbackMessage() {
    }

    public JoinCallbackMessage(Long gameId, String playerName, JoinStatus status) {
        this.gameId = gameId;
        this.playerName = playerName;
        this.status = status;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public JoinStatus getStatus() {
        return status;
    }
}
