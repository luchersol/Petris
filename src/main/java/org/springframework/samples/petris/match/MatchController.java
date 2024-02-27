package org.springframework.samples.petris.match;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/petris/matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {

    MatchService matchService;
    PlayerService playerService;

    @Autowired
    public MatchController(MatchService matchService, PlayerService playerService) {
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @Operation(summary = "Retrives a match", description = "Gets a match given its id", tags = { "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{matchId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> findMatchById(@PathVariable("matchId") Integer matchId) {
        Match body = matchService.findMatchById(matchId);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrives a match", description = "Gets the actual match of the player  whose id is the given", tags = { "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/now")
    public ResponseEntity<Match> findActualMatchByPlayerId(@RequestParam Integer playerId) {
        Match body = matchService.findActualMatchByPlayerId(playerId);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrives a match", description = "Gets a match given its code. This is usefull for private matchmaking", tags = {
            "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/searchCode/{matchCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> findMatchByCode(@PathVariable("matchCode") String matchCode) {
        Match body = matchService.findMatchByCode(matchCode);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrives a match", description = "Joins the player chose id is the given into the private match whose code is the given", tags = {
        "matches", "put" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match retrieved", content = {
                @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "404", description = "Match not found", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema()) }),
})
    @PutMapping("/joinPrivate")
    public ResponseEntity<Match> joinPrivateMatch(@RequestParam String code, @RequestParam Integer playerId) throws InvalidMatchCodeException, NoSamePlayersException{
        Match match = matchService.findMatchByCode(code);
        return joinMatch(match.getId(), playerId);
    }

    @Operation(summary = "Retrives a list of matches", description = "Restrieves a list of matches which have started but have not finished", tags = {
            "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match list retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Match>> findAllCurrentMatches() {
        List<Match> body = matchService.findAllCurrentMatches();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrives the first public match that has not started yet", description = "Restrieves the first match which did not start yet and is public", tags = {
            "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match list retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/quickplay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> findFirstMatchToPlay(@RequestParam(defaultValue = "null") Integer playerId) throws NoMatchToPlayException {
        Match body = matchService.findFirstMatchToPlay(playerId);
        if (body == null)
            throw new NoMatchToPlayException();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrives a list of public matches that have not started yet", description = "Restrieves a list of matches which did not start yet and are public", tags = {
            "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match list retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/play")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Match>> findMatchesToPlay(@RequestParam(required = false) Integer playerId) throws NoMatchToPlayException {
        List<Match> body = matchService.findMatchesToPlay(playerId);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrives all matches", description = "Restrieves all matches that are in the database", tags = {
            "matches", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match list retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Match>> findAllMatches() {
        List<Match> body = matchService.findAllMatches();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Creates a match", description = "Creates a new match", tags = { "matches", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Match created", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Match> saveNewMatch(@RequestBody @Valid Match match) {
        Match body = matchService.saveNewMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Updates a match", description = "Edits a match", tags = { "matches", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match edited", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{matchId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> updateMatch(@RequestBody Match match, @PathVariable("matchId") Integer id,
            @RequestParam(defaultValue = "false") Boolean finish)
            throws InvalidMatchCodeException {
        Match oldMatch = matchService.findMatchById(id);
        if (oldMatch == null)
            throw new ResourceNotFoundException("There is no match with id: " + id);
        Match body = matchService.updateMatch(id, match, finish);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Joins a match", description = "Adds the player to a match which is waiting to be joined in", tags = {
            "matches", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match joined", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/join/{matchId}/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinMatch(@PathVariable("matchId") Integer id,
            @PathVariable("playerId") Integer playerId) throws InvalidMatchCodeException, NoSamePlayersException {
        Match joinableMatch = matchService.findMatchById(id);
        Player playerToJoin = playerService.findPlayerById(playerId);
        if (joinableMatch == null)
            throw new ResourceNotFoundException("There is no match with id: " + id);
        if (playerToJoin == null)
            throw new ResourceNotFoundException("There is no player with id: " + playerId);
        if (joinableMatch.getPlayer() != null)
            throw new ResourceNotFoundException("Not exist player ");
        if (joinableMatch.getCreator().getId() == playerId)
            throw new NoSamePlayersException();

        joinableMatch.setPlayer(playerToJoin);
        joinableMatch.setStartDate(LocalDateTime.now());
        Match matchJoined = matchService.updateMatch(id, joinableMatch, false);
        return ResponseEntity.ok(matchJoined);
    }

}
