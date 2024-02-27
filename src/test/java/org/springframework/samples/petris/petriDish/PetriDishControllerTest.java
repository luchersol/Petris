package org.springframework.samples.petris.petriDish;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petris.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.samples.petris.match.Match;

import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;


/**
 * Test class for the {@link PetriDishController}
 */
@WebMvcTest(controllers = PetriDishController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PetriDishControllerTest {
    private static final Integer TEST_PETRI_DISH_ID = 1;
    private static final Integer TEST_INDEX = 0;
    private static final Integer TEST_CHIPS_PLAYER_BLUE = 0;
    private static final Integer TEST_CHIPS_PLAYER_RED = 0;

    private static final Integer TEST_MATCH_ID = 1;
    private static final String TEST_MATCH_NAME = "Partida 1";
    private static final String TEST_CODE = "code1";
    
    private static final String BASE_URL = "/petris/petriDish";

    @SuppressWarnings("unused")
    @Autowired
    private PetriDishController petriDishController;

    @MockBean
    private PetriDishService petriDishService;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private PetriDish petriDish;
    private Match match;

    @BeforeEach
    void setup(){
        match = new Match();
        match.setId(TEST_MATCH_ID);
        match.setName(TEST_MATCH_NAME);
        match.setCode(TEST_CODE);

        petriDish = new PetriDish();
        petriDish.setId(TEST_PETRI_DISH_ID);
        petriDish.setIndex(TEST_INDEX);
        petriDish.setChipsPlayerBlue(TEST_CHIPS_PLAYER_BLUE);
        petriDish.setChipsPlayerRed(TEST_CHIPS_PLAYER_RED);

    }

    @Test
	@WithMockUser("admin")
    void shouldSavePetriDish() throws Exception{
        PetriDish petriDishNew1 = new PetriDish();
        PetriDish petriDishNew2 = new PetriDish();
        petriDishNew1.setChipsPlayerBlue(2);
        petriDishNew1.setChipsPlayerRed(1);
        petriDishNew1.setChipsPlayerBlue(3);
        petriDishNew1.setChipsPlayerRed(2);
        mockMvc.perform(post(BASE_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(List.of(petriDishNew1,petriDishNew2)))).andExpect(status().isCreated())		;


    }
    
}
