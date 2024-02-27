package org.springframework.samples.petris.achievement;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petris.admin.AdminService;

@SpringBootTest
public class AchievementServiceTest {

    @Autowired
    AchievementService achievementService;

    @Autowired
    AdminService adminService;

    @Test
    public void shouldFindAllAchievements(){
        List<Achievement> achievements = achievementService.findAllAchievements();
        assertNotNull(achievements);
        assertEquals(achievements.size(),10);
    }

    @Test
    public void shouldFindAchievementById(){
        Achievement achievement = achievementService.findAchievementById(5);
        assertTrue(achievement.getName().equals("Primera partida ganada"));

    }

    @Test
    public void shouldFindAchievementByIncorrectId(){
        Achievement achievement = achievementService.findAchievementById(15);
        assertNull(achievement);
    }

    @Test
    public void shouldFindAchievementsByPlayerId(){
        List<Achievement> achievements = achievementService.findAchievementByPlayerId(1);
        assertNotNull(achievements);
        assertTrue(achievements.size() == 6);
    }

    @Test
    public void shouldFindAchievementsByPlayerIdNull(){
        List<Achievement> achievements = achievementService.findAchievementByPlayerId(null);
        assertNotNull(achievements);
        assertTrue(achievements.isEmpty());
    }
    
    @Test
    public void shoulFindAchievementsByAdminId(){
        List<Achievement> achievements = achievementService.findAchievementByAdminId(1);
        assertNotNull(achievements);
        assertTrue(achievements.size() == 10);
    }

    @Test
    public void shoulFindAchievementsByIncorrectAdminId(){
        List<Achievement> achievements = achievementService.findAchievementByAdminId(4);
        assertNotNull(achievements);
        assertTrue(achievements.isEmpty());
    }

    @Test
    public void shouldCreateAchievement(){
        Integer count = this.achievementService.findAllAchievements().size();
        Achievement achievement = new Achievement();
        achievement.setId(12);
        achievement.setName("100 VICTORIAS");
        achievement.setDescription("Eres un experto en victorias");
        achievement.setMeter(Meter.VICTORY);
        achievement.setNumCondition(100);
        achievement.setCreator(adminService.findAdminByUserId(1));
        achievement.setBadgeImage("https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg");
        this.achievementService.saveAchievement(achievement);
        assertNotEquals(0, achievement.getId().longValue());
		assertNotNull(achievement.getId());

        Integer finalCount = this.achievementService.findAllAchievements().size();
		assertEquals(count + 1, finalCount);
        
    }

    @Test
    public void shouldUpdateAchievement(){
        String nombreActualizado = "NombreActualizado";
        Achievement achievement = achievementService.findAchievementById(1);
		achievement.setName(nombreActualizado);
        achievementService.updateAchievement(1, achievement);
		achievement = achievementService.findAchievementById(1);
		assertEquals(nombreActualizado, achievement.getName());
    }


  

    @Test
    public void shouldDeleteAchievement(){
        Integer count = this.achievementService.findAllAchievements().size();
        this.achievementService.deleteAchievement(achievementService.findAchievementById(1).getId());
        Integer lastCount = this.achievementService.findAllAchievements().size();
        assertEquals(count - 1, lastCount);
        
    }


   

        
}
