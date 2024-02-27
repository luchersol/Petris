package org.springframework.samples.petris.match;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.samples.petris.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for the {@link MatchController}
 */
@WebMvcTest(controllers = MatchController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class MatchControllerTest {
    
    private static final Integer TEST_MATCH_ID = 1;
    private static final String TEST_MATCH_NAME = "Partida 1";
    private static final Integer TEST_WINNER_ID = 1;
    private static final Integer TEST_PLAYER_BLUE_ID = 1;
    private static final Integer TEST_PLAYER_RED_ID = 2;
    private static final String BASE_URL = "/petris/matches";
    private static final String TEST_CODE = "code1";


    @SuppressWarnings("unused")
    @Autowired
    private MatchController matchController;

    @MockBean
    private MatchService matchService;

    @MockBean
    private PlayerService playerService;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    private Match match;
    private Player player1;
    private Player player2;
    private User user;
    private Admin admin;


    @BeforeEach
    void setup(){
        player1 = new Player();
        player1.setId(TEST_PLAYER_BLUE_ID);
        player2 = new Player();
        player2.setId(TEST_PLAYER_RED_ID);

        match = new Match();
        match.setId(TEST_MATCH_ID);
        match.setName(TEST_MATCH_NAME);
        match.setWinner(player1);
        match.setPlayer(player1);
        match.setPlayer(player2);
        match.setCode(TEST_CODE);
        match.setStartDate(LocalDateTime.of(2023, 11, 25, 10, 02, 02));
        match.setStartDate(LocalDateTime.of(2023, 11, 25, 10, 22, 02));



    }

    @Test
	@WithMockUser("admin")
    void shouldFindMatchById() throws Exception{
        when(this.matchService.findMatchById(TEST_MATCH_ID)).thenReturn(match);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_MATCH_ID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_MATCH_ID))
				.andExpect(jsonPath("$.name").value(match.getName())) 
                .andExpect(jsonPath("$.winner").value(match.getWinner()));
	
    }

    @Test
	@WithMockUser("admin")
    void shouldFindMatchByCode() throws Exception{
        when(this.matchService.findMatchByCode(TEST_CODE)).thenReturn(match);
		mockMvc.perform(get(BASE_URL + "/searchCode/{matchCode}", TEST_CODE)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_MATCH_ID))
				.andExpect(jsonPath("$.name").value(match.getName())) 
                .andExpect(jsonPath("$.winner").value(match.getWinner()));
	
    }

    @Test
    @WithMockUser("admin")
    void shouldFindAllCurrentMatches() throws Exception{
        Match matchNew1 = new Match();
        matchNew1.setId(TEST_MATCH_ID+1);
        matchNew1.setName("partida nueva!!");
        matchNew1.setPlayer(player1);
        matchNew1.setPlayer(player2);
        matchNew1.setCode("code112");
        matchNew1.setStartDate(LocalDateTime.of(2023, 12, 25, 10, 02, 02));
        matchNew1.setEndDate(null);

        when(this.matchService.findAllCurrentMatches()).thenReturn(List.of(matchNew1));

        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("partida nueva!!")); 

    }


    @Test
    @WithMockUser("admin")
    void shouldFindMatchesToPlay() throws Exception{
        Match matchNew1 = new Match();
        matchNew1.setId(TEST_MATCH_ID+1);
        matchNew1.setName("La gran partida");
        matchNew1.setPlayer(player1);
        matchNew1.setPlayer(player2);
        matchNew1.setCode("code1212");
        matchNew1.setStartDate(null);
        matchNew1.setEndDate(null);

        when(this.matchService.findMatchesToPlay(TEST_PLAYER_BLUE_ID)).thenReturn(List.of(match,matchNew1));

        mockMvc.perform(get(BASE_URL + "/play")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(0));

    }

    @Test
    @WithMockUser("admin")
    void shouldFindAllMatches() throws Exception{
        Match matchNew1 = new Match();
        matchNew1.setId(TEST_MATCH_ID+1);
        matchNew1.setName("partida nueva!!");
        matchNew1.setPlayer(player1);
        matchNew1.setPlayer(player2);
        matchNew1.setCode("code112");

        when(this.matchService.findAllMatches()).thenReturn(List.of(match,matchNew1));

        mockMvc.perform(get(BASE_URL + "/all")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("Partida 1"))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("partida nueva!!")); 

    }



    @Test
	@WithMockUser("admin")
	void shouldUpdateMatch() throws Exception {
		match.setName("La partida del siglo");

		when(this.matchService.findMatchById(TEST_MATCH_ID)).thenReturn(match);
		when(this.matchService.updateMatch(TEST_MATCH_ID, match, false)).thenReturn(match);
		mockMvc.perform(put(BASE_URL + "/{matchId}", TEST_MATCH_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(match))).andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("La partida del siglo"));
				
	}











}
