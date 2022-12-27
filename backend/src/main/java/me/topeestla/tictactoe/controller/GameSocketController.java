package me.topeestla.tictactoe.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import me.topeestla.tictactoe.entities.Game;
import me.topeestla.tictactoe.enums.GameStatus;
import me.topeestla.tictactoe.enums.JoinStatus;
import me.topeestla.tictactoe.enums.MoveStatus;
import me.topeestla.tictactoe.message.*;
import me.topeestla.tictactoe.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private ScheduledExecutorService ex;

    @PostConstruct
    private void init() {
        logger = Logger.getLogger(GameSocketController.class.getName());
        ex = Executors.newScheduledThreadPool(5);

        server.start();

        server.addConnectListener(client -> {
            logger.info("Client connected: " + client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            logger.info("Client disconnected: " + client.getSessionId());
        });

        server.addEventListener("join_game", JoinCallbackMessage.class, (client, data, ackSender) -> {
            Optional<Game> game = gameRepository.findById(data.getGameId());

            if (game.isPresent()) {
                this.clientJoinGame(client, game.get(), data.getPlayerName());
            } else {
                client.sendEvent("join_callback", new JoinCallbackMessage(game.get().getId(), data.getPlayerName(), JoinStatus.GAME_NOT_FOUND));
            }
        });

        /**
        server.addEventListener("join_game", JoinCallbackMessage.class, (client, data, ackRequest) -> {
            Optional<Game> gameOpt = gameRepository.findById(data.getGameId());

            if (gameOpt.isPresent()) {
                Game game = gameOpt.get();

                if (game.getPlayer1() != null) {
                    if (game.getPlayer2() == null) {
                        game.setPlayer2(data.getPlayerName());
                        game.setStatus(GameStatus.IN_PROGRESS);
                        gameRepository.save(game);
                        client.joinRoom(data.getGameId().toString());
                        client.sendEvent("join_success", data.getGameId());

                        sendPreStart(game);

                        ex.schedule(() -> sendGameStart(game), 10, TimeUnit.SECONDS);
                    } else {
                        // Game is full
                    }
                } else {
                    game.setPlayer1(data.getPlayerName());
                    gameRepository.save(game);
                    client.joinRoom(data.getGameId().toString());
                    client.sendEvent("join_success", data.getGameId());
                }
            }
        }); */


        server.addEventListener("game_move", GameMoveMessage.class, ((client, data, ackSender) -> {
            Optional<Game> game = gameRepository.findById(data.getGameId());

            if (game.isPresent()) {
                this.clientPlayMove(client, game.get(), data.getPlayer(), data.getX(), data.getY());
            } else {
                client.sendEvent("move_callback", new MoveCallbackMessage(data.getGameId(), data.getPlayer(), MoveStatus.GAME_NOT_FOUND));
            }
        }));

        /**
        server.addEventListener("game_move", GameMoveMessage.class, (client, data, ackRequest) -> {
            Optional<Game> gameOpt = gameRepository.findById(data.getGameId());

            if (gameOpt.isPresent()) {
                Game game = gameOpt.get();

                if (game.getStatus().equals(GameStatus.IN_PROGRESS)) {
                    if (game.isTurnOf(data.getPlayer())) {
                        if (game.checkIfCanPLayHere(data.getX(), data.getY())) {

                            game.updateBoard(data.getX(), data.getY(), data.getPlayer());

                            sendGameUpdate(game);

                            if (game.checkWin()) {
                                sendGameWin(game);
                            }

                            gameRepository.save(game);
                        }
                    }
                }
            }
        }); */
    }

    public void clientJoinGame(SocketIOClient client, Game game, String playerName) {
        if (game.getPlayer1() == null) {
            if (game.getPlayer2() != null) {
                if (playerName.equals(game.getPlayer2())) {
                    client.sendEvent("join_callback", new JoinCallbackMessage(game.getId(), playerName, JoinStatus.GAME_NOT_FOUND));
                    return;
                }
            }
            game.setPlayer1(playerName);
            client.joinRoom(game.getId().toString());
            client.sendEvent("join_callback", new JoinCallbackMessage(game.getId(), playerName, JoinStatus.SUCCESS));
            gameRepository.save(game);
            this.testStart(game);
        } else if (game.getPlayer2() == null) {
            if (game.getPlayer1() != null) {
                if (game.getPlayer1().equals(playerName)) {
                    client.sendEvent("join_callback", new JoinCallbackMessage(game.getId(), playerName, JoinStatus.GAME_NOT_FOUND));
                    return;
                }
            }
            game.setPlayer2(playerName);
            client.joinRoom(game.getId().toString());
            client.sendEvent("join_callback", new JoinCallbackMessage(game.getId(), playerName, JoinStatus.SUCCESS));
            gameRepository.save(game);
            this.testStart(game);
        } else {
            client.sendEvent("join_callback", new JoinCallbackMessage(game.getId(), playerName, JoinStatus.GAME_FULL));
        }
    }

    public void clientPlayMove(SocketIOClient client, Game game, String playerName, int x, int y) {
        if (game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            if (game.isTurnOf(playerName)) {
                if (game.checkIfCanPLayHere(x, y)) {
                    game.updateBoard(x, y, playerName);

                    server.getRoomOperations(game.getId().toString()).sendEvent("game_update", new GameUpdateMessage(game));

                    if (game.checkWin()) {
                        game.setStatus(GameStatus.FINISHED);
                        server.getRoomOperations(game.getId().toString()).sendEvent("game_win", new GameWinMessage(game));
                    }

                    gameRepository.save(game);
                } else {
                    client.sendEvent("move_callback", new MoveCallbackMessage(game.getId(), playerName, MoveStatus.INVALID_MOVE));
                }
            } else {
                client.sendEvent("move_callback", new MoveCallbackMessage(game.getId(), playerName, MoveStatus.INVALID_PLAYER));
            }
        } else {
            client.sendEvent("move_callback", new MoveCallbackMessage(game.getId(), playerName, MoveStatus.INVALID_GAME_STATE));
        }
    }

    public void testStart(Game game) {
        if ((game.getPlayer1() != null) && (game.getPlayer2() != null)) {
            server.getRoomOperations(game.getId().toString()).sendEvent("game_soon_start", game.getId());

            ex.schedule(() -> {
                game.setStatus(GameStatus.IN_PROGRESS);
                server.getRoomOperations(game.getId().toString()).sendEvent("game_start", new GameStartMessage(game));
                gameRepository.save(game);
            }, 10, TimeUnit.SECONDS);
        }
    }
}
