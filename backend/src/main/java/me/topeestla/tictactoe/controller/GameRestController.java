package me.topeestla.tictactoe.controller;

import me.topeestla.tictactoe.entities.Game;
import me.topeestla.tictactoe.enums.GameStatus;
import me.topeestla.tictactoe.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author TopeEstLa
 */
@Controller
@RequestMapping("/api/game")
public class GameRestController {

    @Autowired
    private GameRepository repository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<Game> createGame() {
        Game game = new Game(new Date());
        repository.save(game);
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/available")
    public ResponseEntity<List<Game>> getAvailableGames() {
        List<Game> games = repository.findAllByStatus(GameStatus.WAITING_FOR_PLAYER);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
