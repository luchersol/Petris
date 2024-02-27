package org.springframework.samples.petris.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest()
public class AchievementRepositoryTest {

    @Autowired
    AchievementRepository ar;
     
    @Test
    public void  findByAdminIdNull(){
        List<Achievement> res = ar.findAchievementByAdminId(null);
        assertTrue(res.isEmpty());
    }

    @Test
    public void  findByPlayerIdNull(){
        List<Achievement> res = ar.findAchievementByPlayerId(null, null);
        assertTrue(res.isEmpty());
    }

    @Test
     public void findByAdminId(){
        List<Achievement> res = ar.findAchievementByAdminId(1);
        assertEquals(res.size(),10);
    }

    @Test
    public void findByPlayerId(){
        List<Achievement> res = ar.findAchievementByPlayerId(2, null);
        assertEquals(res.size(),5);
    }


    @Test
    public void findUncompletedAchievementByPlayerId(){
        List<Achievement> res = ar.findUncompletedAchievementByPlayerId(2);
        assertEquals(res.size(),6);

    }

    @Test
    public void  findUncompletedAchievementByPlayerIdIncorrect(){
        List<Achievement> res = ar.findAchievementByAdminId(45);
        assertTrue(res.isEmpty());
    }


    
}
