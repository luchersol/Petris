package org.springframework.samples.petris.match.invitation;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petris.configuration.SecurityConfiguration;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.player.Player;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.FilterType;

/**
 * Test class for the {@link MatchInvitationController}
 */
@WebMvcTest(controllers = MatchInvitationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class MatchInvitationControllerTest {
    private static final String BASE_URL = "/petris/matchInvitation";

    private static final Integer TEST_ID_INVITATION = 1;
    private static final Boolean TEST_IS_ACCEPTED = Boolean.TRUE;

    private static final Integer TEST_MATCH_ID = 1;
    private static final String TEST_MATCH_NAME = "Partida 1";
    private static final Integer TEST_PLAYER_BLUE_ID = 1;
    private static final Integer TEST_PLAYER_RED_ID = 2;
    private static final String TEST_CODE = "code1";

    @SuppressWarnings("unused")
    @Autowired
    private MatchInvitationController matchInvitationController;

    @MockBean
    private MatchInvitationService matchInvitationService;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private MatchInvitation matchInvitation;
    private Match match;
    private Player player1;
    private Player player2;


    @BeforeEach
    void setup(){
        player1 = new Player();
        player1.setId(TEST_PLAYER_BLUE_ID);
        player2 = new Player();
        player2.setId(TEST_PLAYER_RED_ID);

        match = new Match();
        match.setId(TEST_MATCH_ID);
        match.setName(TEST_MATCH_NAME);
        match.setCode(TEST_CODE);
        match.setPlayer(player1);

        matchInvitation = new MatchInvitation();
        matchInvitation.setId(TEST_ID_INVITATION);
        matchInvitation.setIsAccepted(TEST_IS_ACCEPTED);
        matchInvitation.setMatch(match);
        matchInvitation.setAuthor(player1);
        matchInvitation.setReceiver(player2);
        match.setPlayer(player2);
    }

    @Test
    @WithMockUser("admin")
    void shouldFindMatchInvitationByReceiverId() throws Exception{
        
        when(this.matchInvitationService.findMatchInvitationByReceiverId(player2.getId()))
            .thenReturn(List.of(matchInvitation));
            
        mockMvc.perform(get(BASE_URL + "?receiverId={receiverId}", player2.getId())).andExpect(status().isOk())
                                        .andExpect(jsonPath("$.size()").value(1));

    }




    
    
}
