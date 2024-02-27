package org.springframework.samples.petris.player;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer>{

    @Query("SELECT player FROM Player player WHERE player.user.id = :id")
    Optional<Player> findPlayerByUserId(Integer id);
    
    @Query("SELECT player FROM Player player WHERE player.user.username = :username")
    Optional<Player> findPlayerByUsername(String username);

    @Query("SELECT p.beFriends FROM Player p where p.id = :id ")
    List<Player> findFriendsById(@Param("id") Integer id);

    @Query("SELECT p FROM Player p where :player MEMBER OF p.beFriends ")
    List<Player> findFriendsByPlayer(@Param("player") Player player);

}
