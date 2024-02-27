package org.springframework.samples.petris.stats;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petris.player.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends CrudRepository<Stats, Integer>{

    @Query("SELECT p.stats FROM Player p WHERE p.id = :id")
    Stats findStatsByPlayerId(Integer id);


    @Query("SELECT p FROM Player p WHERE (p.stats.victories + p.stats.losses) > 0 ORDER BY p.stats.victories DESC, (p.stats.victories / (p.stats.victories + p.stats.losses)) DESC LIMIT 5")
    List<Player> findBestPlayers();
    
}
