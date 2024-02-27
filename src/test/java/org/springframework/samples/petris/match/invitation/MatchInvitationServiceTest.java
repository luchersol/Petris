package org.springframework.samples.petris.match.invitation;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
public class MatchInvitationServiceTest {

    @Autowired
    private MatchInvitationService matchInvitationService;


    @Test
    void shouldFindMatchInvitationByReceiverId(){
        List<MatchInvitation> matchInvitations = this.matchInvitationService.findMatchInvitationByReceiverId(1);
        assertEquals(matchInvitations.size(), 0);

    }

    @Test
    public void shouldSaveMatchInvitation(){
        MatchInvitation matchInvitation = new MatchInvitation();
        matchInvitation.setId(7);
        matchInvitation.setIsAccepted(true);
        this.matchInvitationService.saveMatchInvitation(3, "player2");
		assertNotNull(matchInvitation.getId());


    }


    @Test
    public void shouldDeleteMatchInvitation(){
        this.matchInvitationService.deleteMatchInvitation(2);
    }

    



    
}
