package org.springframework.samples.petris.match;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;


@SpringBootTest
@AutoConfigureTestDatabase
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Test
    public void shouldFindMatch() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Match m = new Match();
        Player p1 = playerService.findPlayerById(1);
        Player p2 = playerService.findPlayerById(2);
        m.setId(1);
        m.setName("Partida 1");
        m.setEndDate(LocalDateTime.parse("2023-01-01 16:55", formatoFecha));
        m.setStartDate(LocalDateTime.parse("2023-01-01 16:25", formatoFecha));
        m.setNumTurn(2);
        m.setContaminationLevelBlue(4);
        m.setContaminationLevelRed(5);
        m.setIsPrivated(true);
        m.setCode("code1");
        m.setWinner(p1);
        m.setCreator(p1);
        m.setPlayer(p2);
        Match matchSearched = this.matchService.findMatchById(1);
        assertEquals(m.toString(), matchSearched.toString());
    }

    @Test
    public void shouldFindActualMatchByPlayerId(){
        Match match = matchService.findActualMatchByPlayerId(3);
        assertNotNull(match);
        assertTrue(match.getName().equals("Partida 7"));


    }

    @Test
    public void shouldFindMatchByCode(){
        Match match = matchService.findMatchByCode("code5");
        assertNotNull(match);
        assertTrue(match.getName().equals("Partida 5"));


    }

    @Test
    public void shouldFindPlayedMatchesByPlayerId(){
        List<Match> matches = matchService.findPlayedMatchesByPlayerId(2);
        assertNotNull(matches);
        assertEquals(matches.size(), 6);

    }

    @Test
    public void shouldFindPlayedMatchesByUserId(){
        List<Match> matches = matchService.findPlayedMatchesByUserId(2);
        assertNotNull(matches);
        assertEquals(matches.size(), 5);

    }

    @Test
    public void shouldFindAllCurrentMatches(){
        List<Match> matches = matchService.findAllCurrentMatches();
        assertNotNull(matches);
        assertEquals(matches.size(), 1);
    }


    @Test
    public void shouldFindFirstMatchToPlay(){
        Match match = matchService.findFirstMatchToPlay(2);
        assertNull(match);
    }
    

    @Test
    public void shouldFindMatchesToPlay(){
        List<Match> matches = matchService.findMatchesToPlay(1);
        assertNotNull(matches);
        assertEquals(matches.size(), 0);

    }


    @Test
    public void shouldFindAllMatches(){
        List<Match> matches = matchService.findAllMatches();
        assertNotNull(matches);
        assertTrue(matches.size() == 7);
    }



    @Test
    void shouldUpdateMatch() throws InvalidMatchCodeException {
        Match match = this.matchService.findMatchById(7);
		match.setName("nombreCambiado");
        matchService.updateMatch(7, match, false);
		match = this.matchService.findMatchById(7);
		assertEquals("nombreCambiado", match.getName());
    }

}
