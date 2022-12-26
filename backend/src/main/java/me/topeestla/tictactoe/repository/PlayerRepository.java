package me.topeestla.tictactoe.repository;

import me.topeestla.tictactoe.entities.Player;
import org.springframework.data.repository.CrudRepository;

/**
 * @author TopeEstLa
 */
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
