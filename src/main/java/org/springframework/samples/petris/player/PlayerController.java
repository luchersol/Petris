package org.springframework.samples.petris.player;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.achievement.AchievementService;
import org.springframework.samples.petris.auth.payload.response.MessageResponse;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchService;
import org.springframework.samples.petris.stats.Stats;
import org.springframework.samples.petris.stats.StatsService;
import org.springframework.samples.petris.util.RestPreconditions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/petris/players")
@SecurityRequirement(name = "bearerAuth")
public class PlayerController {

    PlayerService playerService;
    StatsService statsService;
    MatchService matchService;
    AchievementService achievementService;

    @Autowired
    public PlayerController(PlayerService playerService, StatsService statsService, MatchService matchService,
            AchievementService achievementService) {
        this.playerService = playerService;
        this.statsService = statsService;
        this.matchService = matchService;
        this.achievementService = achievementService;
    }

    @Operation(summary = "Retrives all players", description = "Get a list of players objects", tags = { "players",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player List retrieved", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping
    public ResponseEntity<List<Player>> findAllPlayers() {
        List<Player> body = playerService.findAllPlayers();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrieves a player", description = "Retrieves a player given its id", tags = { "players",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player retrieved", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Player> findPlayerById(@PathVariable("id") Integer id) {
        Player body = playerService.findPlayerById(id);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieves a player", description = "Retrieves a player given its user id", tags = { "players",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player retrieved", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Player> findPlayerByUserId(@PathVariable("userId") Integer id) {
        Player body = playerService.findPlayerByUserId(id);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrives the stats of a player", description = "Gets the stats of a player given its id", tags = {
            "players",
            "get", "stats", "matches" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stats retrieved", content = {
                    @Content(schema = @Schema(implementation = Stats.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}/stats")
    public ResponseEntity<Stats> findStatsByPlayerId(@PathVariable("id") Integer id) {
        Stats body = statsService.findStatsByPlayerId(id);
        if (body == null)
            throw new ResourceNotFoundException("There is no player with id: " + id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrieves a list of Matches", description = "Retrieves all finished matches that has the player with the id given", tags = {
            "players", "get", "matches" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matches retrieved", content = {
                    @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}/matches")
    public ResponseEntity<List<Match>> findPlayedMatchesByPlayerId(@PathVariable("id") Integer playerId) {
        List<Match> body = matchService.findPlayedMatchesByPlayerId(playerId);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrieves a list of Matches", description = "Retrieves all finished matches that has the player with the userId given", tags = {
        "players", "get", "matches" })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matches retrieved", content = {
                @Content(schema = @Schema(implementation = Match.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "404", description = "Player not found", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema()) }),
})
@GetMapping("/user/{id}/matches")
public ResponseEntity<List<Match>> findPlayedMatchesByUserId(@PathVariable("id") Integer id) {
    List<Match> body = matchService.findPlayedMatchesByUserId(id);
    return ResponseEntity.ok(body);
}

    @Operation(summary = "Retrieves a player", description = "Retrieves a player given its username", tags = {
            "players", "get", "users" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player retrieved", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/user/username/{username}")
    public ResponseEntity<Player> findPlayerByUsername(@PathVariable("username") String username) {
        Player body = playerService.findPlayerByUsername(username);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieves the friends of a player", description = "Retrieves a list of players who are friends of the player whose id is given", tags = {
            "players", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Players retrieved", content = {
                    @Content(schema = @Schema(implementation = FriendDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/beFriends/{id}")
    public ResponseEntity<List<FriendDTO>> findFriendsByPlayerId(@PathVariable("id") Integer playerId) {
        List<Player> oldF = playerService.findFriendsByPlayerId(playerId);
        List<FriendDTO> body = oldF.stream().map(f -> new FriendDTO(f)).toList();
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();

    }
       
    @Operation(summary = "Retrieves the friends of a player and the players that are friends with that player", description = "Retrieves a list of players who are friends of the player whose id is given and the players that considere the player whose id is given a friend", tags = {
            "players", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Players retrieved", content = {
                    @Content(schema = @Schema(implementation = FriendDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/allFriends/{id}")
    public ResponseEntity<List<FriendDTO>> findFriendsByPlayer(@PathVariable("id") Integer playerId) {
        Player player = playerService.findPlayerById(playerId);
        Set<Player> oldF = playerService.findFriendsByPlayer(player);
        List<FriendDTO> body = oldF.stream().map(f -> new FriendDTO(f)).toList();
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

        @Operation(summary = "Edits a player or his friend", description = "Edits a player given its id or his friend, depends on who considers who a friend", tags = { "players", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player edited", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{playerId}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Player> updateFriends(@PathVariable("playerId") Integer playerId,
            @PathVariable("friendId") Integer friendId, @RequestBody @Valid Player player) {
        RestPreconditions.checkNotNull(playerService.findPlayerById(playerId), "Player", "id", playerId);
        return new ResponseEntity<>(this.playerService.updateFriends(player, playerId, friendId), HttpStatus.OK);
    }

    @Operation(summary = "Edits a player", description = "Edits a player given its id", tags = { "players", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player edited", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Player not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Player> update(@PathVariable("playerId") Integer id, @RequestBody @Valid Player player) {
        RestPreconditions.checkNotNull(playerService.findPlayerById(id), "Player", "id", id);
        return new ResponseEntity<>(this.playerService.updatePlayer(player, id), HttpStatus.OK);
    }

    @Operation(summary = "Find top 5 players", description = "Finds the top 5 best players in the game", tags = {
            "players", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Players retrieved", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/podium")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Player>> findTop5Players() {
        List<Player> body = statsService.getBestPlayers();
        return ResponseEntity.ok(body);
    }


    @Operation(summary = "Deletes a player", description = "Deletes the player whose id is the given", tags = {
        "players", "delete" })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Player deleted", content = {
                @Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "404", description = "Friend request not found", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema()) }),
})
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteByUserId(@RequestParam Integer userId) {
        playerService.deletePlayerByUser(userId);
        MessageResponse body = new MessageResponse("Player deleted!");
        return ResponseEntity.ok(body);
    }

    // FRIEND REQUEST THINGS

    @Operation(summary = "Updates a friend request", description = "Updates a friend request given its id", tags = {
            "players", "put", "FriendRequests" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request updated", content = {
                    @Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Friend request not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/friendRequest/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FriendRequest> acceptFriendRequest(@PathVariable("id") Integer id) {
        FriendRequest friendRequest = playerService.findFriendRequestById(id);
        RestPreconditions.checkNotNull(friendRequest, "FriendRequest", "ID", id);
        FriendRequest body = this.playerService.acceptFriendRequest(friendRequest);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Creates a friend request", description = "Creates a friend request", tags = { "players",
            "post", "FriendRequests" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request created", content = {
                    @Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping("/friendRequest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FriendRequest> createFriendRequest(@RequestBody @Valid FriendRequest friendRequest) {
        FriendRequest fr = this.playerService.findFriendRequestByPlayers(friendRequest.getAuthor().getId(),
                friendRequest.getReceiver().getId());
        if (friendRequest.getReceiver().getId() == friendRequest.getAuthor().getId()) 
            throw new ResourceAccessException("You can not send a friend request to your self");
        
        if (fr != null) {
            throw new ResourceAccessException(
                    "You already send a friend request to that player or you are already his friend");
        } else {
            FriendRequest body = playerService.saveFriendRequest(friendRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }
    }

    @Operation(summary = "Deletes a friend request", description = "Deletes a friend request given its id", tags = {
            "players", "delete", "FriendRequests" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request deleted", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Friend request not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/friendRequest/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> declineFriendRequest(@PathVariable("id") Integer id) {
        playerService.deleteFriendRequest(id);
        MessageResponse body = new MessageResponse("Friend request deleted!");
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Deletes a friend request", description = "Deletes a friend request given author's and reciver's ids", tags = {
            "players", "delete", "FriendRequests" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request deleted", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Friend request not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/friendRequest/player1/{idPlayer1}/player2/{idPlayer2}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> deleteFriendRequestByAuthorAndReceiver(
            @PathVariable("idPlayer1") Integer idPlayer1, @PathVariable("idPlayer2") Integer idPlayer2) {
        FriendRequest p = playerService.findFriendRequestByPlayers(idPlayer1, idPlayer2);
        if (p != null) {
            playerService.deleteFriendRequest(p.getId());
        }
        MessageResponse body = new MessageResponse("Friend request deleted!");
        return ResponseEntity.ok(body);
    }

        @Operation(summary = "Retrieves a friend request by the receiver", description = "Retrieves a friend request given the id of the receiver", tags = { "players", "post", "FriendRequests" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request retrieved", content = {
                    @Content(schema = @Schema(implementation = FriendRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/friendRequest")
    public ResponseEntity<List<FriendRequest>> findFriendRequestByReceiver(@RequestParam Integer receiverId) {
        List<FriendRequest> body = playerService.findFriendRequestByReceiver(receiverId);
        return ResponseEntity.ok(body);
    }

}
