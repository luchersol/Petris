package org.springframework.samples.petris.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petris.achievement.AchievementService;
import org.springframework.samples.petris.petriDish.PetriDish;
import org.springframework.samples.petris.petriDish.PetriDishRepository;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.samples.petris.stats.StatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

    MatchRepository matchRepository;
    PetriDishRepository petriDishRepository;
    AchievementService achievementService;
    StatsService statsService;
    PlayerService playerService;

    @Autowired
    public MatchService(MatchRepository matchRepository, AchievementService achievementService, StatsService statsService,
            PetriDishRepository petriDishRepository, PlayerService playerService) {
        this.matchRepository = matchRepository;
        this.achievementService = achievementService;
        this.playerService = playerService;
        this.statsService = statsService;
        this.petriDishRepository = petriDishRepository;
    }

    @Transactional(readOnly = true)
    public Match findMatchById(Integer matchId) {
        return matchRepository.findById(matchId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Match findActualMatchByPlayerId(Integer playerId){
        List<Match> matchs = matchRepository.findActualMatchByPlayerId(playerId);
        return matchs.isEmpty() ? null : matchs.get(0);
    }

    @Transactional(readOnly = true)
    public Match findMatchByCode(String matchCode) {
        return matchRepository.findMatchByCode(matchCode);
    }

    @Transactional(readOnly = true)
    public List<Match> findPlayedMatchesByPlayerId(Integer playerId) {
        return matchRepository.findAllPlayerMatches(playerId);
    }

    @Transactional(readOnly = true)
    public List<Match> findPlayedMatchesByUserId(Integer userId){
        Player player = playerService.findPlayerByUserId(userId);
        return matchRepository.findAllPlayerMatches(player.getId());
    }

    @Transactional(readOnly = true)
    public List<Match> findAllCurrentMatches() {
        return matchRepository.findCurrentMatches();
    }

    // esto te da la 1º partida pública sin empezar que encuentra
    @Transactional(readOnly = true)
    public Match findFirstMatchToPlay(Integer playerId) {
        List<Match> ls = matchRepository.findMatchesWithoutStarts(playerId);
        return ls.isEmpty() ? null : ls.get(0);
    }

    @Transactional(readOnly = true)
    public List<Match> findMatchesToPlay(Integer playerId) {
        return matchRepository.findMatchSelection(playerId);
    }

    @Transactional(readOnly = true)
    public List<Match> findAllMatches() {
        return (List<Match>) matchRepository.findAll();
    }

    @Transactional
    public Match saveNewMatch(Match match) {
        Player creator = match.getCreator();
        String name = match.getName();
        Boolean isPrivated = match.getIsPrivated();
        Match createdMatch = new Match();
        createdMatch.setNumTurn(1);
        createdMatch.setContaminationLevelBlue(0);
        createdMatch.setContaminationLevelRed(0);
        createdMatch.setStartDate(null);
        createdMatch.setEndDate(null);
        createdMatch.setIsPrivated(isPrivated);
        createdMatch.setName(name);
        createdMatch.setCreator(creator);
        createdMatch.setPlayer(null);
        createdMatch.setWinner(null);
        String matchCode = isPrivated ? createdMatch.generateMatchCode() : null;
        createdMatch.setCode(matchCode);
        createdMatch.setPetriDishes(new ArrayList<>());
        IntStream.range(0, 7).forEach(index -> {
            PetriDish p = index == 2 ? PetriDish.firstBlue() : 
                          index == 4 ? PetriDish.firstRed() : 
                          PetriDish.empty(index);
            this.petriDishRepository.save(p);
            createdMatch.getPetriDishes().add(p);
        });
        Match res = matchRepository.save(createdMatch);
        return res;
    }

    @Transactional
    public Match updateMatch(Integer id, Match match, Boolean finish) throws InvalidMatchCodeException {
        Match matchToUpdate = matchRepository.findById(id).get();
        if( !match.checkCorrectCode() ){
            throw new InvalidMatchCodeException();
        }
        BeanUtils.copyProperties(match, matchToUpdate, "id");
        matchToUpdate.getPetriDishes().forEach(petriDish -> {
            petriDishRepository.save(petriDish);
        });
        
        if(finish){ 
            LocalDateTime date = LocalDateTime.now();
            if(matchToUpdate.getStartDate() == null){
                matchToUpdate.setStartDate(date);
            }
            matchToUpdate.setEndDate(date);
        }
        matchRepository.save(matchToUpdate);
        if(finish){
            Consumer<Player> update = (player) -> {
                if(player != null){
                    statsService.updateStatsByPlayer(player.getId());
                    achievementService.saveNewCompletedAchievements(player.getId());
                }
            };
            Player playerBlue = match.getCreator();
            Player playerRed = match.getPlayer();
            update.accept(playerBlue);
            update.accept(playerRed);
        }
        return matchToUpdate;
    }

}
