package me.topeestla.tictactoe.message;

import me.topeestla.tictactoe.entities.Game;

/**
 * @author TopeEstLa
 */
public class GameUpdateMessage {

    private Long gameId;
    private String board;
    private String player1;
    private String player2;
    private String nextPlayer;

    public GameUpdateMessage(Game game) {
        this.gameId = game.getId();
        this.board = game.getBoard();
        this.player1 = game.getPlayer1();
        this.player2 = game.getPlayer2();
        this.nextPlayer = game.getNextPlayer();
    }

    public GameUpdateMessage(Long gameId, String board, String player1, String player2, String nextPlayer) {
        this.gameId = gameId;
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.nextPlayer = nextPlayer;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getBoard() {
        return board;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }
}
