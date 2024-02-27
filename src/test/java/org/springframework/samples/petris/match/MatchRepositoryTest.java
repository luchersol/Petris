package org.springframework.samples.petris.match;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class MatchRepositoryTest {

    @Autowired
    MatchRepository mr;

    @Test
    public void findAllPlayerMatchesNull(){
        List<Match> res = mr.findAllPlayerMatches(null);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findAllPlayerMatches(){
        List<Match> res = mr.findAllPlayerMatches(1);
        assertEquals(res.size(), 5);
    }

    @Test
     public void findMatchByCode(){
        Match res = mr.findMatchByCode("code1");
        assertEquals(res.getId(), 1);

    }

    @Test
    public void findMatchByIncorretCode(){
       Match res = mr.findMatchByCode("codigo33");
       assertNull(res);
   }



    @Test
    public void findCurrentMatches(){
        List<Match> res = mr.findCurrentMatches();
        assertEquals(res.size(), 1);
    }

    @Test
    public void findMatchesWithoutStarts(){
        List<Match> res = mr.findMatchesWithoutStarts(2);
        assertTrue(res.isEmpty());
    }


    @Test
    public void findMatchSelection(){
        List<Match> res = mr.findMatchSelection(2);
        assertTrue(res.isEmpty());

    }



    @Test
    public void findActualMatchByPlayerId(){
        List<Match> res = mr.findActualMatchByPlayerId(2);
        assertTrue(res.isEmpty());
    }


    @Test
    public void findActualMatchByIncorrectPlayerId(){
        List<Match> res = mr.findActualMatchByPlayerId(12);
        assertTrue(res.isEmpty());
    }


    
}
