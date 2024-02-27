package org.springframework.samples.petris.stats;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petris.player.Player;

@DataJpaTest
public class StatsRepositoryTest {

    @Autowired
    StatsRepository sr;
    

    @Test
    public void findByUserId(){
        Stats res = sr.findStatsByPlayerId(2);
        assertEquals(res.getTotalBacterium(),12);
        assertEquals(res.getTotalSarcinas(),3);
    }


    @Test
    public void findByUserIdIncorrect(){
        Stats res = sr.findStatsByPlayerId(0);
        assertNull(res);
    }




    @Test
    public void findBestPlayers(){
        List<Player> res = sr.findBestPlayers();
        assertNotNull(res);
        assertEquals(res.size(),2);
    }
}
