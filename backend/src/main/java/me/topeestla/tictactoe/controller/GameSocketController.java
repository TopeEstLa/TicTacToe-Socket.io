package me.topeestla.tictactoe.controller;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import me.topeestla.tictactoe.entities.Game;
import me.topeestla.tictactoe.enums.GameStatus;
import me.topeestla.tictactoe.message.*;
import me.topeestla.tictactoe.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author TopeEstLa
 */
@Component
public class GameSocketController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SocketIOServer server;

    private Logger logger;

    @PostConstruct
    private void init() {
        logger = Logger.getLogger(GameSocketController.class.getName());
        server.start();
        server.addConnectListener(client -> {
            logger.info("Client connected: " + client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            logger.info("Client disconnected: " + client.getSessionId());
        });

        server.addEventListener("join", JoinGameMessage.class, (client, data, ackRequest) -> {
            Optional<Game> gameOpt = gameRepository.findById(data.getGameId());

            if (gameOpt.isPresent()) {
                Game game = gameOpt.get();



                if (game.getPlayer1() != null) {
                    if (game.getPlayer2() == null) {
                        game.setPlayer2(data.getPlayerName());
                        game.setStatus(GameStatus.IN_PROGRESS);
                        gameRepository.save(game);
                        client.joinRoom(data.getGameId().toString());
                        sendGameStart(game);
                    } else {
                        // Game is full
                    }
                } else {
                    game.setPlayer1(data.getPlayerName());
                    gameRepository.save(game);
                    client.joinRoom(data.getGameId().toString());
                    sendGameWaiting(game);
                }
            }
        });

            server.addEventListener("game_move", GameMoveMessage.class, (client, data, ackRequest) -> {
            Optional<Game> gameOpt = gameRepository.findById(data.getGameId());

            if (gameOpt.isPresent()) {
                Game game = gameOpt.get();

                if (game.getStatus().equals(GameStatus.IN_PROGRESS)) {
                    if (game.isTurnOf(data.getPlayer())) {
                        if (game.checkIfCanPLayHere(data.getX(), data.getY())) {
                            game.updateBoard(data.getX(), data.getY(), data.getPlayer());
                            if (game.checkWin()) {
                                gameRepository.save(game);
                                sendGameWin(game);
                                return;
                            }
                            gameRepository.save(game);
                            sendGameUpdate(game);
                        }
                    }
                }
            }
        });
    }

    public void sendGameStart(Game game) {
        server.getRoomOperations(game.getId().toString()).sendEvent("game_start", new GameStartMessage(game));
    }

    public void sendGameWaiting(Game game) {
        server.getRoomOperations(game.getId().toString()).sendEvent("game_waiting", game);
    }

    public void sendGameUpdate(Game game) {
        server.getRoomOperations(game.getId().toString()).sendEvent("game_update", new GameUpdateMessage(game));
    }

    public void sendGameWin(Game game) {
        server.getRoomOperations(game.getId().toString()).sendEvent("game_win", new GameWinMessage(game));
    }
}
