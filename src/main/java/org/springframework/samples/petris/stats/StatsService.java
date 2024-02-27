package org.springframework.samples.petris.stats;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchRepository;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatsService {
    
    StatsRepository statsRepository;
    PlayerRepository playerRepository;
    MatchRepository matchRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository, PlayerRepository playerRepository, MatchRepository matchRepository){
        this.statsRepository = statsRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public Stats findStatsById(Integer id){
        return this.statsRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Stats findStatsByPlayerId(Integer id){
        Optional<Player> player= playerRepository.findById(id);
        return player.isEmpty()?null:statsRepository.findStatsByPlayerId(id);
    }

    @Transactional
    public Stats saveStats(Stats stats){
        return statsRepository.save(stats);
    }

    @Transactional
    public Stats updateStatsByPlayer(Integer playerId){
        
        Stats toUpdate = findStatsByPlayerId(playerId);
        Integer totalBacterium = 0, totalSarcinas = 0, victories = 0, losses = 0;
        List<Match> matches = matchRepository.findAllPlayerMatches(playerId);
        for(Match match: matches){
            if(match.getWinner() != null && match.getWinner().getId() == playerId) victories++;
            else losses++;
            Boolean isPlayerBlue = match.getCreator().getId() == playerId;
            totalBacterium += isPlayerBlue ? match.countBacteriumPlayerBlue() : match.countBacteriumPlayerRed();
            totalSarcinas += isPlayerBlue ? match.countSarcinPlayerBlue() : match.countSarcinPlayerRed();
        }
        toUpdate.setTotalBacterium(totalBacterium);
        toUpdate.setTotalSarcinas(totalSarcinas);
        toUpdate.setVictories(victories);
        toUpdate.setLosses(losses);
        return saveStats(toUpdate);
    }

    @Transactional(readOnly = true)
    public List<Player> getBestPlayers() {
        List<Player> res = statsRepository.findBestPlayers();
        return res;
    }

}
