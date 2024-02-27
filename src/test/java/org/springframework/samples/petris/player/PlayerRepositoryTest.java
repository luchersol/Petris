package org.springframework.samples.petris.player;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest()
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository pr;

    @Test
    public void findByUserId() {
        Optional<Player> res = pr.findPlayerByUserId(2);
        assumeTrue(res.isPresent());

    }

    @Test
    public void findByUsername() {
        Optional<Player> res = pr.findPlayerByUsername("player2");
        assumeTrue(res.isPresent());
    }

    @Test
    public void findFriendsById() {
        List<Player> res = pr.findFriendsById(1);
        assertTrue(!res.isEmpty());
        assertEquals(res.size(), 1);
    }

    @Test
    public void findFriendsByPlayer() {
        Optional<Player> player = pr.findPlayerByUserId(3);
        List<Player> res = pr.findFriendsByPlayer(player.get());
        assertTrue(!res.isEmpty());
        assertEquals(res.size(), 1);
    }

    @Test
    public void findByUserIdIncorrect() {
        Optional<Player> res = pr.findPlayerByUserId(45);
        assertTrue(res.isEmpty());

    }

    @Test
    public void findByUsernameIncorrect() {
        Optional<Player> res = pr.findPlayerByUsername("jugador2");
        assertTrue(res.isEmpty());
    }

    @Test
    public void findFriendsByIdIncorrect() {
        List<Player> res = pr.findFriendsById(10);
        assertTrue(res.isEmpty());
    }

}
