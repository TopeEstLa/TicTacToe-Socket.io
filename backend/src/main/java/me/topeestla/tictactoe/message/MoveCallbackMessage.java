package me.topeestla.tictactoe.message;

import me.topeestla.tictactoe.enums.MoveStatus;

/**
 * @author TopeEstLa
 */
public class MoveCallbackMessage {

    private Long gameId;
    private String player;
    private MoveStatus status;

    public MoveCallbackMessage(Long gameId, String player, MoveStatus status) {
        this.gameId = gameId;
        this.player = player;
        this.status = status;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPlayer() {
        return player;
    }

    public MoveStatus getStatus() {
        return status;
    }
}
