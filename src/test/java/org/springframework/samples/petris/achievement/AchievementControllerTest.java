package org.springframework.samples.petris.achievement;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petris.admin.Admin;
import org.springframework.samples.petris.configuration.SecurityConfiguration;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.user.Authorities;
import org.springframework.samples.petris.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for the {@link AchievementController}
 */

@WebMvcTest(controllers = AchievementController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AchievementControllerTest {

    private static final Integer TEST_AUTH_ID = 2;
    private static final Integer TEST_PLAYER_ID = 1;
    private static final Integer TEST_ADMIN_ID = 1;
    private static final Integer TEST_ACHIEVEMENT_ID = 1;
    private static final Integer TEST_USER_ID = 2;
    private static final String BASE_URL = "/petris/achievements";

    @SuppressWarnings("unused")
    @Autowired
    private AchievementController achievementController;

    @MockBean
    private AchievementService achievementService;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Achievement achievement;
    private Player player;
    private User user;
    private Admin admin;

    @BeforeEach
    void setup(){
        Authorities auth = new Authorities();
        auth.setId(TEST_AUTH_ID);
        auth.setAuthority("ADMIN");

        user = new User();
        user.setId(TEST_USER_ID);
        user.setUsername("player1");
        user.setPassword("password");
        user.setAuthority(auth);

        admin = new Admin();
        admin.setId(TEST_ADMIN_ID);
        admin.setUser(user);

        achievement = new Achievement();
        achievement.setId(TEST_ACHIEVEMENT_ID);
        achievement.setName("Poker de victorias");
        achievement.setDescription("Cuatro victorias!!!!");
        achievement.setMeter(Meter.VICTORY);
        achievement.setNumCondition(4);
        achievement.setCreator(admin);

        player = new Player();
        player.setId(TEST_PLAYER_ID);
        player.setUser(user);

    }


    @Test
    @WithMockUser("admin")
    void shouldFindAll() throws Exception{
        Achievement achievNew1 = new Achievement();
        achievNew1.setId(2);
        achievNew1.setName("Second in a row");
        achievNew1.setDescription("Dos victorias seguidas!!");
        achievNew1.setMeter(Meter.VICTORY);
        achievNew1.setNumCondition(2);
        achievNew1.setCreator(admin);

        when(this.achievementService.findAllAchievements()).thenReturn(List.of(achievement,achievNew1));

        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("Poker de victorias"))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("Second in a row")); 

    }

    @Test
    @WithMockUser("admin")
    void shouldFindAchievementByPlayerId() throws Exception{
        when(this.achievementService.findAchievementByPlayerId(TEST_PLAYER_ID)).thenReturn(List.of(achievement));

        mockMvc.perform(get(BASE_URL + "/player/{id}", TEST_PLAYER_ID)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("Poker de victorias"));

        

    }

    @Test
	@WithMockUser("admin")
	void shouldFindAchievementById() throws Exception {
		when(this.achievementService.findAchievementById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_ACHIEVEMENT_ID))
				.andExpect(jsonPath("$.name").value(achievement.getName())) 
                .andExpect(jsonPath("$.description").value(achievement.getDescription())) 
				.andExpect(jsonPath("$.creator").value(achievement.getCreator()));
	}

     @Test
	@WithMockUser("admin")
	void shouldNotFindAchievementById() throws Exception {
		when(this.achievementService.findAchievementById(TEST_ACHIEVEMENT_ID)).thenThrow(ResourceNotFoundException.class);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID)).andExpect(status().isNotFound());
	}


    @Test
	@WithMockUser("admin")
	void shouldUpdateAchievement() throws Exception {
		achievement.setName("Four victories, my friend");

		when(this.achievementService.findAchievementById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);
		when(this.achievementService.updateAchievement(any(Integer.class), any(Achievement.class))).thenReturn(achievement);
		mockMvc.perform(put(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(achievement))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(achievement.getName())) 
                .andExpect(jsonPath("$.description").value(achievement.getDescription())) 
				.andExpect(jsonPath("$.creator").value(achievement.getCreator()));
	}


    @Test
	@WithMockUser("admin")
	void shouldDeleteAchievement() throws Exception {
		when(this.achievementService.findAchievementById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);
		doNothing().when(this.achievementService).deleteAchievement(TEST_ACHIEVEMENT_ID);;

		mockMvc.perform(delete(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID).with(csrf())).andExpect(status().isOk());
	}






    
}
