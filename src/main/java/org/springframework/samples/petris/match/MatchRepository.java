package org.springframework.samples.petris.match;

import java.util.List;

import org.springframework.data.jpa.repository.Query;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Integer>{

    //esto te da todas las partidas que ha jugado el player que quieres 
    @Query("SELECT match FROM Match match WHERE (match.creator.id =?1 OR match.player.id =?1) AND match.endDate IS NOT NULL AND match.startDate != match.endDate")
    List<Match> findAllPlayerMatches(Integer playerId);

    //esto te da las partidad que han empezado pero no han acabado
    @Query("SELECT match FROM Match match WHERE match.startDate IS NOT NULL AND match.endDate IS NULL")
    List<Match> findCurrentMatches();

    //esto te da todas las partidas públicas que no han empezado aún
    @Query("SELECT match FROM Match match WHERE match.creator.id != :playerId AND match.startDate IS NULL AND match.isPrivated=FALSE")
    List<Match> findMatchesWithoutStarts(Integer playerId);

    @Query("SELECT match FROM Match match WHERE match.creator.id != :playerId AND match.endDate IS NULL AND match.isPrivated=FALSE")
    List<Match> findMatchSelection(Integer playerId);

    @Query("SELECT match FROM Match match WHERE (match.creator.id = :id OR match.player.id = :id) AND match.endDate IS NULL ORDER BY match.startDate DESC")
    List<Match> findActualMatchByPlayerId(Integer id);

    @Query("SELECT match FROM Match match WHERE match.code =?1")
    Match findMatchByCode(String code);


}