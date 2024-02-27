package org.springframework.samples.petris.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petris.achievement.AchievementService;
import org.springframework.samples.petris.configuration.SecurityConfiguration;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.match.MatchService;
import org.springframework.samples.petris.stats.Stats;
import org.springframework.samples.petris.stats.StatsService;
import org.springframework.samples.petris.user.Authorities;
import org.springframework.samples.petris.user.User;
import org.springframework.samples.petris.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for the {@link PlayerController}
 */
@WebMvcTest(controllers = PlayerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PlayerControllerTests {

    private static final Integer TEST_AUTH_ID = 2;
    private static final Integer TEST_USER_ID = 2;
    private static final String TEST_USERNAME = "player1";
    private static final Integer TEST_PLAYER_ID = 1;
    private static final Integer TEST_USER_FRIEND_ID = 3;
    private static final Integer TEST_FRIEND_ID = 2;
    private static final String BASE_URL = "/petris/players";

    @SuppressWarnings("unused")
    @Autowired
    private PlayerController playerController;

    @MockBean
    private StatsService statService;

    @MockBean
    private MatchService matchService;

    @MockBean
    private AchievementService achievementService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private UserService userService;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Player player;
    private User user;
    private Player friend;
    private User userFriend;
    private Stats stats;

    @BeforeEach
    void setup() {

        Authorities auth = new Authorities();
        auth.setId(TEST_AUTH_ID);
        auth.setAuthority("PLAYER");

        user = new User();
        user.setId(TEST_USER_ID);
        user.setUsername("player1");
        user.setPassword("password");
        user.setAuthority(auth);

        player = new Player();
        player.setId(TEST_PLAYER_ID);
        player.setUser(user);

        userFriend = new User();
        userFriend.setId(TEST_USER_FRIEND_ID);
        userFriend.setUsername("player2");
        userFriend.setPassword("password");
        userFriend.setAuthority(auth);

        friend = new Player();
        friend.setId(TEST_FRIEND_ID);
        friend.setUser(userFriend);

        stats = new Stats();
        stats.setId(1);
        stats.setTotalBacterium(10);
        stats.setTotalSarcinas(2);
    }

    @Test
    @WithMockUser("admin")
    void shouldFindAll() throws Exception {
        Player playerNew1 = new Player();
        User userNew1 = new User();
        userNew1.setUsername("Lucas");
        playerNew1.setId(3);
        playerNew1.setUser(userNew1);

        Player playerNew2 = new Player();
        User userNew2 = new User();
        userNew2.setUsername("Terelu");
        playerNew2.setId(4);

        when(this.playerService.findAllPlayers()).thenReturn(List.of(player, playerNew1, playerNew2));

        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(3)); 
    }

    @Test
	@WithMockUser("admin")
	void shouldReturnPlayer() throws Exception {
		when(this.playerService.findPlayerById(TEST_PLAYER_ID)).thenReturn(player);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_PLAYER_ID))
				.andExpect(jsonPath("$.user").value(player.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldReturnNotFoundPlayer() throws Exception {
		when(this.playerService.findPlayerById(TEST_PLAYER_ID)).thenThrow(ResourceNotFoundException.class);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_PLAYER_ID)).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser("admin")
	void shouldUpdatePlayer() throws Exception {

		when(this.playerService.findPlayerById(TEST_PLAYER_ID)).thenReturn(player);
		when(this.playerService.updatePlayer(any(Player.class), any(Integer.class))).thenReturn(player);

		mockMvc.perform(put(BASE_URL + "/{id}", TEST_PLAYER_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(player))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_PLAYER_ID))
				.andExpect(jsonPath("$.user").value(player.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldReturnNotFoundUpdatePlayer() throws Exception {

		when(this.playerService.findPlayerById(3)).thenThrow(ResourceNotFoundException.class);
		when(this.playerService.updatePlayer(any(Player.class), any(Integer.class))).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(put(BASE_URL + "/{id}", 3).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(player))).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser("admin")
	void shouldFindPlayerByUser() throws Exception {
		when(this.playerService.findPlayerByUserId(TEST_USER_ID)).thenReturn(player);

		mockMvc.perform(get(BASE_URL + "/user/{userId}", TEST_USER_ID)).andExpect(status().isOk())
        .andExpect(jsonPath("$.user").value(player.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldNotFindPlayerByUser() throws Exception {
		when(this.playerService.findPlayerByUserId(3)).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/user/{userId}", 3)).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser("admin")
	void shouldFindPlayerByUsername() throws Exception {
		when(this.playerService.findPlayerByUsername(TEST_USERNAME)).thenReturn(player);

		mockMvc.perform(get(BASE_URL + "/user/username/{username}", TEST_USERNAME)).andExpect(status().isOk())
        .andExpect(jsonPath("$.user").value(player.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldNotFindPlayerByUsername() throws Exception {
		when(this.playerService.findPlayerByUsername("player10")).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/user/username/{username}", "playerX")).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldFindFriendList() throws Exception {
		when(this.playerService.findFriendsByPlayerId(TEST_PLAYER_ID)).thenReturn(List.of(friend));

		mockMvc.perform(get(BASE_URL + "/beFriends/{id}", TEST_PLAYER_ID)).andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldNotFindFriendList() throws Exception {
		when(this.playerService.findFriendsByPlayerId(3)).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/beFriends/{id}", 3)).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shoulFindFriendListReverseEmpty() throws Exception {
		when(this.playerService.findFriendsByPlayerId(TEST_FRIEND_ID)).thenReturn(new ArrayList<>());

		mockMvc.perform(get(BASE_URL + "/beFriends/{id}", TEST_FRIEND_ID)).andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldFindAllFriendList() throws Exception {
        when(this.playerService.findPlayerById(TEST_PLAYER_ID)).thenReturn(player);
        when(this.playerService.findFriendsByPlayerId(TEST_PLAYER_ID)).thenReturn(List.of(friend));
		when(this.playerService.findFriendsByPlayer(player)).thenReturn(Set.of(friend));

		mockMvc.perform(get(BASE_URL + "/allFriends/{id}", TEST_PLAYER_ID)).andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldNotFindAllFriendList() throws Exception {
        Player playerNew1 = new Player();
        User userNew1 = new User();
        userNew1.setUsername("Lorenzo");
        playerNew1.setId(5);
        playerNew1.setUser(userNew1);

        when(this.playerService.findPlayerById(5)).thenThrow(ResourceNotFoundException.class);
        when(this.playerService.findFriendsByPlayerId(5)).thenThrow(ResourceNotFoundException.class);
		when(this.playerService.findFriendsByPlayer(playerNew1)).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/allFriends/{id}", 5)).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shoulFindAllFriendListReverse() throws Exception {
        when(this.playerService.findPlayerById(TEST_FRIEND_ID)).thenReturn(friend);
        when(this.playerService.findFriendsByPlayerId(TEST_FRIEND_ID)).thenReturn(List.of(player));
		when(this.playerService.findFriendsByPlayer(friend)).thenReturn(Set.of(player));

		mockMvc.perform(get(BASE_URL + "/allFriends/{id}", TEST_FRIEND_ID)).andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldFindStatsByPlayerId() throws Exception {
		when(this.statService.findStatsByPlayerId(TEST_PLAYER_ID)).thenReturn(stats);

		mockMvc.perform(get(BASE_URL + "/{id}/stats", TEST_PLAYER_ID)).andExpect(status().isOk())
        .andExpect(jsonPath("$.totalBacterium").value(10))
        .andExpect(jsonPath("$.totalSarcinas").value(2))
        .andExpect(jsonPath("$.id").value(1));
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldNotFindStatsByPlayerId() throws Exception {
		when(this.statService.findStatsByPlayerId(5)).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/{id}/stats", 5)).andExpect(status().isNotFound());
	}

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldFindAllPlayerMatches() throws Exception {
		when(this.matchService.findPlayedMatchesByPlayerId(TEST_PLAYER_ID)).thenReturn(new ArrayList<>());

		mockMvc.perform(get(BASE_URL + "/{id}/matches", TEST_PLAYER_ID)).andExpect(status().isOk());	
    }

    @Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldNotFindAllPlayerMatches() throws Exception {
		when(this.matchService.findPlayedMatchesByPlayerId(5)).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(get(BASE_URL + "/{id}/matches", 5)).andExpect(status().isNotFound());
    }

	@Test
	@WithMockUser(username = "player", authorities = "PLAYER")
	void shouldFindPlayedMatchesByUserId() throws Exception {
		when(this.matchService.findPlayedMatchesByUserId(TEST_USER_ID)).thenReturn(new ArrayList<>());

		mockMvc.perform(get(BASE_URL + "/user/{id}/matches", TEST_USER_ID)).andExpect(status().isOk());	
    }

    @Test
	@WithMockUser("admin")
	void shouldUpdatePlayerList() throws Exception {

		when(this.playerService.findPlayerById(TEST_PLAYER_ID)).thenReturn(player);
		when(this.playerService.updateFriends(any(Player.class), any(Integer.class), any(Integer.class))).thenReturn(player);

		mockMvc.perform(put(BASE_URL + "/{playerId}/{friendId}", TEST_PLAYER_ID, TEST_FRIEND_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(player))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_PLAYER_ID))
				.andExpect(jsonPath("$.user").value(player.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldUpdatePlayerListReverse() throws Exception {

		when(this.playerService.findPlayerById(TEST_FRIEND_ID)).thenReturn(friend);
		when(this.playerService.updateFriends(any(Player.class), any(Integer.class), any(Integer.class))).thenReturn(friend);

		mockMvc.perform(put(BASE_URL + "/{playerId}/{friendId}", TEST_FRIEND_ID, TEST_PLAYER_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(friend))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_FRIEND_ID))
				.andExpect(jsonPath("$.user").value(friend.getUser()));
	}

    @Test
	@WithMockUser("admin")
	void shouldReturnNotFoundUpdatePlayerList() throws Exception {

		when(this.playerService.findPlayerById(3)).thenThrow(ResourceNotFoundException.class);
		when(this.playerService.updateFriends(any(Player.class), any(Integer.class), any(Integer.class))).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(put(BASE_URL + "/{playerId}/{friendId}", 3, TEST_FRIEND_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(player))).andExpect(status().isNotFound());
	}

}
