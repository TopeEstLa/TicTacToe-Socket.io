package me.topeestla.tictactoe.repository;

import me.topeestla.tictactoe.entities.Game;
import me.topeestla.tictactoe.enums.GameStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TopeEstLa
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> findAllByStatus(GameStatus status);

}
