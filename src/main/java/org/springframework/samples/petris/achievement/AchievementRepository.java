package org.springframework.samples.petris.achievement;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer>{

    List<Achievement> findAll(Sort sort);

    @Query("SELECT a FROM Achievement a WHERE a.creator.id = :id")
    List<Achievement> findAchievementByAdminId(Integer id);

    @Query("SELECT r.achievement FROM AchievementPlayer r WHERE r.player.id = :playerId")
    List<Achievement> findAchievementByPlayerId(Integer playerId, Sort sort);

    @Query("SELECT a FROM Achievement a WHERE a NOT IN (SELECT r.achievement FROM AchievementPlayer r WHERE r.player.id = :playerId)")
    List<Achievement> findUncompletedAchievementByPlayerId(Integer playerId);


}
