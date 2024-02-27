package org.springframework.samples.petris.achievement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementService {
    
    AchievementRepository achievementRepository;
    AchievementPlayerRepository achievementPlayerRepository;
    PlayerService playerService;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository, AchievementPlayerRepository achievementPlayerRepository, PlayerService playerService){
        this.achievementRepository = achievementRepository;
        this.achievementPlayerRepository = achievementPlayerRepository;
        this.playerService = playerService;
    }

    @Transactional(readOnly = true)
    public List<Achievement> findAllAchievements(){
        return achievementRepository.findAll(Sort.by("meter", "numCondition"));
    }

    @Transactional(readOnly = true)
    public Achievement findAchievementById(Integer id){
        return achievementRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Achievement> findAchievementByPlayerId(Integer playerId){
        return achievementRepository.findAchievementByPlayerId(playerId, Sort.by("achievement.meter", "achievement.numCondition"));
    }

    @Transactional(readOnly = true)
    public List<Achievement> findAchievementByAdminId(Integer adminId){
        return achievementRepository.findAchievementByAdminId(adminId);
    }

    @Transactional
    public Achievement saveAchievement(Achievement achievement){
        Achievement newAchievement = new Achievement();
        BeanUtils.copyProperties(achievement, newAchievement, "id");
        achievement.selectBagImage();
        return achievementRepository.save(achievement);
    }

    @Transactional
    public void saveNewCompletedAchievements(Integer playerId){
        List<Achievement> uncompletedAchivements = achievementRepository.findUncompletedAchievementByPlayerId(playerId);
        Player player = playerService.findPlayerById(playerId);
        uncompletedAchivements.stream()
        .filter(achievement -> achievement.completedBy(player))
        .forEach(achivement -> {
            AchievementPlayer achievementPlayer = new AchievementPlayer();
            achievementPlayer.setAchievement(achivement);
            achievementPlayer.setPlayer(player);
            achievementPlayer.setAchievementDate(LocalDateTime.now());
            achievementPlayerRepository.save(achievementPlayer);
        });
    }

    @Transactional
    public Achievement updateAchievement(Integer id, Achievement achievement){
        Achievement toUpdate = achievementRepository.findById(id).orElse(null);
        BeanUtils.copyProperties(achievement, toUpdate, "id");
        toUpdate.selectBagImage();
        achievementRepository.save(toUpdate);
        return toUpdate;
    }

    @Transactional
    public void deleteAchievement(Integer achievementId){
        achievementRepository.deleteById(achievementId);
    }
}
