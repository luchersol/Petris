package org.springframework.samples.petris.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petris.user.User;

@SpringBootTest
public class PlayerServiceTests {
    
    @Autowired
    PlayerService playerService ;

    @Test
    public void shouldFindAllPlayers(){
        List<Player> players = playerService.findAllPlayers();
        assertNotNull(players);
        assertTrue(players.size() == 5);
    }

    @Test
    public void shouldFindPlayerByUser(){
        Player player = playerService.findPlayerByUserId(2);
        assertNotNull(player);
        assertTrue(player.getId() == 1);
    }

    @Test
    public void shouldNotFindPlayerByUser(){
        Player player = playerService.findPlayerByUserId(1);
        assertNull(player);
    }

    @Test
    public void shouldUpdatePlayer(){
        Player player = playerService.findPlayerById(1);
        User user = player.getUser();
		user.setUsername("Update");
        player.setUser(user);
        playerService.updatePlayer(player, 1);
		player = playerService.findPlayerById(1);
		assertEquals("Update", player.getUser().getUsername());
    }

    @Test
    public void shouldFindFriendsAuthor(){
        List<Player> players = playerService.findFriendsByIdAuthor(1);
        assertNotNull(players);
        Player player = playerService.findPlayerById(2);
        assertNotNull(player);
        assertTrue(players.contains(player));
    }

    @Test
    public void shouldFindFriendsReceiver(){
        List<Player> players = playerService.findFriendsByIdReceiver(2);
        assertNotNull(players);
        Player player = playerService.findPlayerById(1);
        assertNotNull(player);
        assertTrue(players.contains(player));
    }

    @Test
    public void shouldFindPlayerByUsername(){
        Player player = playerService.findPlayerByUsername("player1");
        assertNotNull(player);
        assertTrue(player.getId() == 1);
    }

    @Test
    public void shouldFindFriendList(){
        List<Player> players = playerService.findFriendsByPlayerId(1);
        assertNotNull(players);
        Player player = playerService.findPlayerById(2);
        assertNotNull(player);
        assertTrue(players.contains(player));
    }

    @Test
    public void shouldFindFriendListByPlayer(){
        Player player = playerService.findPlayerById(2);
        assertNotNull(player);
        Set<Player> players = playerService.findFriendsByPlayer(player);
        assertNotNull(players);
        Player player2 = playerService.findPlayerById(1);
        assertNotNull(player2);
        assertTrue(players.contains(player2));
    }

}
