package me.topeestla.tictactoe.message;

import me.topeestla.tictactoe.entities.Game;

/**
 * @author TopeEstLa
 */
public class GameWinMessage {

    private Long gameId;
    private String winner;

    public GameWinMessage(Game game) {
        this.gameId = game.getId();
        this.winner = game.getWinner();
    }

    public GameWinMessage(Long gameId, String winner) {
        this.gameId = gameId;
        this.winner = winner;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getWinner() {
        return winner;
    }
}
