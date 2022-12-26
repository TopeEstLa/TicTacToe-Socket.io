package me.topeestla.tictactoe.message;

/**
 * @author TopeEstLa
 */
public class JoinGameMessage {

    private Long gameId;
    private String playerName;

    public JoinGameMessage() {
    }

    public JoinGameMessage(Long gameId, String playerName) {
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
