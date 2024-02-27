package org.springframework.samples.petris.stats;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.samples.petris.user.AuthoritiesService;
import org.springframework.samples.petris.user.User;
import org.springframework.samples.petris.user.UserService;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
	private UserService userService;

    @Autowired
	private PlayerService playerService;

	@Autowired
	private AuthoritiesService authService;


    @Test
    public void shouldFindStatsById(){
        Stats stat = statsService.findStatsById(2);
        assertNotNull(stat);
        assertEquals(stat.getVictories(), 1);
        assertEquals(stat.getLosses(), 5);

    }


    @Test
    public void shouldFindStatsByIncorrectId(){
        Stats stat = statsService.findStatsById(14);
        assertNull(stat);
    }

    @Test
    public void shouldFindByPlayerId(){
        Stats stat = statsService.findStatsByPlayerId(2);
        assertNotNull(stat);
        assertEquals(stat.getTotalBacterium(),12);
        assertEquals(stat.getTotalSarcinas(),3);
    }

    @Test
    public void shouldFindByIncorrectPlayerId(){
        Stats stat = statsService.findStatsByPlayerId(12);
        assertNull(stat);

    }

    
    @Test
    @Transactional
    public void shouldCreateStats(){
         
        User user = new User();
		user.setUsername("Sam");
		user.setPassword("password23");
		user.setEmail("sam@gmail.com");
        user.setOnline(true);
		user.setAuthority(authService.findByAuthority("PLAYER"));

		this.userService.saveUser(user);
		assertNotEquals(0, user.getId().longValue());
		assertNotNull(user.getId());

        Stats stat = new Stats();
        stat.setTotalBacterium(10);
        stat.setTotalSarcinas(1);
        stat.setVictories(10);
        stat.setLosses(12);
        this.statsService.saveStats(stat);

        Player player = new Player();
        player.setUser(user);
        playerService.savePlayer(player);
        player.setStats(stat);

        Stats playerStats = statsService.findStatsByPlayerId(player.getId());
        Stats playerStats2 = playerService.findPlayerById(player.getId()).getStats();
        Stats playerStats3 = statsService.findStatsById(stat.getId());
        
        assertEquals(stat, playerStats);
        assertEquals(stat, playerStats2);
        assertEquals(stat, playerStats3);
    
        
    }

    

    @Test
    public void shouldUpdateByUserId(){
        Integer totalBacteriasActualizado = 13;
        Stats stat = statsService.findStatsByPlayerId(2);
		stat.setTotalBacterium(totalBacteriasActualizado);
        assertEquals(totalBacteriasActualizado, stat.getTotalBacterium());
    }


    @Test
    public void shouldGetBestPlayers(){
        List<Player> res = statsService.getBestPlayers();
        assertNotNull(res);
        assertEquals(res.size(),2);
        assertTrue(res.size() == 2);

    }
    



    
}
