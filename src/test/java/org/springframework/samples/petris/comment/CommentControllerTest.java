package org.springframework.samples.petris.comment;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petris.configuration.SecurityConfiguration;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchService;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for the {@link CommentController}
 */
@WebMvcTest(controllers = CommentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CommentControllerTest {

    private static final String BASE_URL = "/petris/comment";

    private static final Integer TEST_MATCH_ID = 1;
    private static final String TEST_MATCH_NAME = "Partida 1";
    private static final Integer TEST_WINNER_ID = 1;
    private static final Integer TEST_PLAYER_BLUE_ID = 1;
    private static final Integer TEST_PLAYER_RED_ID = 2;
    private static final String TEST_CODE = "code1";

    private static final Integer TEST_COMMENT_ID = 1;
    private static final String TEST_MESSAGE_COMMENT = "VOY A GANAR";
    private static final LocalDateTime TEST_COMMENT_DATE = LocalDateTime.of(2023, 01, 01, 16, 30);

    @SuppressWarnings("unused")
    @Autowired
    private CommentController commentController;

    @MockBean
    private CommentService commentService;

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
    private Comment comment;


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

        comment = new Comment();
        comment.setId(TEST_COMMENT_ID);
        comment.setMessage(TEST_MESSAGE_COMMENT);
        comment.setCommentDate(TEST_COMMENT_DATE);
        comment.setSentBy(player1);
        comment.setSentIn(match);

    }

    @Test
    @WithMockUser("admin")
    void shouldGetCommentsByMatchId() throws Exception{
        Comment commentNew1 = new Comment();
        commentNew1.setId(TEST_COMMENT_ID + 1);
        commentNew1.setMessage("buena suerte!!");
        commentNew1.setCommentDate(LocalDateTime.of(2023, 01, 01, 16, 32));
        commentNew1.setSentBy(player2);
        commentNew1.setSentIn(match);

        when(this.commentService.findAllCommentsByMatchId(TEST_MATCH_ID)).thenReturn(Arrays.asList(comment,commentNew1));

        mockMvc.perform(get(BASE_URL + "?match_id={matchId}", TEST_MATCH_ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].message").value(TEST_MESSAGE_COMMENT))
                .andExpect(jsonPath("$[?(@.id == 2)].message").value("buena suerte!!")); 

    }




    

    
}
