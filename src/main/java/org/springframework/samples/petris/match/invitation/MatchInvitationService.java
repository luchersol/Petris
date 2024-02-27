package org.springframework.samples.petris.match.invitation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchRepository;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class MatchInvitationService {
    
    MatchInvitationRepository matchInvitationRepository;
    PlayerService playerService;
    MatchRepository matchRepository;

    @Autowired
    public MatchInvitationService(MatchInvitationRepository matchInvitationRepository, PlayerService playerService, MatchRepository matchRepository){
        this.matchInvitationRepository = matchInvitationRepository;
        this.playerService = playerService;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchInvitation> findMatchInvitationByReceiverId(Integer receiverId){
        return matchInvitationRepository.findMatchInvitationByReceiverId(receiverId);
    }

    @Transactional(rollbackFor = {IllegalAccessError.class})
    public MatchInvitation saveMatchInvitation(@RequestParam Integer matchId, @RequestParam String username){
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new IllegalAccessError());
        Player author = playerService.findPlayerById(match.getCreator().getId());
        Player receiver = playerService.findPlayerByUsername(username);
        MatchInvitation invitation = new MatchInvitation();
        invitation.setAuthor(author);
        invitation.setReceiver(receiver);
        invitation.setMatch(match);
        invitation.setIsAccepted(false);
        return matchInvitationRepository.save(invitation);
    }

    @Transactional
    public void deleteMatchInvitation(Integer id){
        matchInvitationRepository.deleteById(id);
    }
}
