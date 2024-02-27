package org.springframework.samples.petris.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FriendRequestRepositoryTest {

    @Autowired
    FriendRequestRepository frp;

    @Test
    public void findFriendRequestByPlayers(){
        Optional<FriendRequest> res = frp.findFriendRequestByPlayers(1, 2);
        assumeTrue(res.isPresent());
        assertEquals(res.get().getIsAccepted(),true);
    }


    @Test
    public void findFriendRequestByReceiver(){
        List<FriendRequest> res = frp.findFriendRequestByReceiver(2);
        assertTrue(!res.isEmpty());
        assertEquals(res.size(), 1);

    }

    @Test
    public void findFriendsByIdAuthor() {
        List<Player> res = frp.findFriendsByIdAuthor(1);
        assertTrue(!res.isEmpty());
        assertEquals(res.size(), 1);
    }

    @Test
    public void findFriendsByIdReceiver() {
        List<Player> res = frp.findFriendsByIdReceiver(2);
        assertTrue(!res.isEmpty());
        assertEquals(res.size(), 1);
    }
    
}
