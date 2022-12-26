package me.topeestla.tictactoe.message;

/**
 * @author TopeEstLa
 */
public class GameMoveMessage {

    private Long gameId;
    private String player;
    private int x, y;

    public GameMoveMessage() {
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
