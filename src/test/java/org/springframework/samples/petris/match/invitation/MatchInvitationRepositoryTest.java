package org.springframework.samples.petris.match.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MatchInvitationRepositoryTest {

    @Autowired
    MatchInvitationRepository mir;

    @Test
    public void findMatchInvitationByReceiverIdNull(){
        List<MatchInvitation> res = mir.findMatchInvitationByReceiverId(null);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findMatchInvitationByIncorrectReceiverId(){
        List<MatchInvitation> res = mir.findMatchInvitationByReceiverId(21);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findMatchInvitationByReceiverId(){
        List<MatchInvitation> res = mir.findMatchInvitationByReceiverId(2);
        assertTrue(res.isEmpty());
    }
    
}
