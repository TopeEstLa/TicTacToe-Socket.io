package me.topeestla.tictactoe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import me.topeestla.tictactoe.enums.GameStatus;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TopeEstLa
 */
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private GameStatus status;
    private Date created;
    private String board;


    private String player1;
    private String player2;


    private String lastPlayer;
    private String winner;


    public Game() {
    }

    public Game(Date created) {
        this.status = GameStatus.WAITING_FOR_PLAYER;
        this.created = created;
        this.board = "000;000;000";

        this.player1 = null;
        this.player2 = null;

        this.lastPlayer = null;
        this.winner = null;
    }

    @JsonIgnore
    public void updateBoard(int x, int y, String player) {
        String[] rows = board.split(";");
        rows[y] = rows[y].substring(0, x) + getPlayerIndex(player) + rows[y].substring(x + 1);
        board = String.join(";", rows);
        lastPlayer = player;
    }

    @JsonIgnore
    public boolean checkIfCanPLayHere(int x, int y) {
        String[] rows = board.split(";");
        return rows[y].charAt(x) == '0';
    }

    @JsonIgnore
    public boolean checkWin() {
        String[] rows = board.split(";");
        String[] columns = new String[3];
        columns[0] = rows[0].charAt(0) + "" + rows[1].charAt(0) + "" + rows[2].charAt(0);
        columns[1] = rows[0].charAt(1) + "" + rows[1].charAt(1) + "" + rows[2].charAt(1);
        columns[2] = rows[0].charAt(2) + "" + rows[1].charAt(2) + "" + rows[2].charAt(2);
        String[] diagonals = new String[2];
        diagonals[0] = rows[0].charAt(0) + "" + rows[1].charAt(1) + "" + rows[2].charAt(2);
        diagonals[1] = rows[0].charAt(2) + "" + rows[1].charAt(1) + "" + rows[2].charAt(0);
        for (String row : rows) {
            if (row.equals("111")) {
                winner = player1;
            } else if (row.equals("222")) {
                winner = player2;
            }
        }
        for (String column : columns) {
            if (column.equals("111")) {
                winner = player1;
            } else if (column.equals("222")) {
                winner = player2;
            }
        }
        for (String diagonal : diagonals) {
            if (diagonal.equals("111")) {
                winner = player1;
            } else if (diagonal.equals("222")) {
                winner = player2;
            }
        }

        if (winner != null) {
            status = GameStatus.FINISHED;
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isTurnOf(String player) {
        return this.getNextPlayer().equals(player);
    }

    @JsonIgnore
    public String getNextPlayer() {
        if (lastPlayer != null) {
            return lastPlayer.equals(player1) ? player2 : player1;
        } else {
            int random = ThreadLocalRandom.current().nextInt(2);
            this.lastPlayer = random == 0 ? player1 : player2;
            return this.getNextPlayer();
        }
    }

    @JsonIgnore
    public int getPlayerIndex(String player) {
        if (player.equals(player1)) {
            return 1;
        } else if (player.equals(player2)) {
            return 2;
        }
        return 0;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Long getId() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Date getCreated() {
        return created;
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

    public String getLastPlayer() {
        return lastPlayer;
    }

    public String getWinner() {
        return winner;
    }
}
