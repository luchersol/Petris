package org.springframework.samples.petris.achievement;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/petris/achievements")
@SecurityRequirement(name = "bearerAuth")
public class AchievementController {

    
    AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService){
        this.achievementService = achievementService;
    }

    @Operation(summary = "Retrives all achievements", description = "Gets a list of achievements objects", tags = {
            "achievements", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Achievement List retrieved", content = {
                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping
    public ResponseEntity<List<Achievement>> findAllAchievements() {
        List<Achievement> body = achievementService.findAllAchievements();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrieves a list of achievements", description = "Retrieves all the achievements of the player whose id is given", tags = {
                    "players", "get", "achievements" })
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Achiements retrieved", content = {
                                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                                    @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                                    @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                                    @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "404", description = "Player not found", content = {
                                    @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/player/{id}")
    public ResponseEntity<List<Achievement>> findAchievementByPlayerId(@PathVariable("id") Integer id) {
            List<Achievement> body = achievementService.findAchievementByPlayerId(id);
            return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrives an achievement", description = "Gets an achievement by its id", tags = {
            "achievements", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Achievement retrieved", content = {
                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Achievement> findAchievementById(@PathVariable("id") Integer id) {
        Achievement body = achievementService.findAchievementById(id);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Creates an achievement", description = "Creates a new achievement", tags = { "achievements",
            "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Achievement created", content = {
                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Achievement> saveAchievement(@RequestBody @Valid Achievement achievement) {
        Achievement body = achievementService.saveAchievement(achievement);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Edits an achievement", description = "Edits an achievement", tags = { "achievements", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Achievement edited", content = {
                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(@PathVariable Integer id, @RequestBody @Valid Achievement achievement) {
        Achievement body = achievementService.updateAchievement(id, achievement);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Deletes an achievement", description = "Deletes an achievement", tags = { "achievements",
            "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Achievement deleted", content = {
                    @Content(schema = @Schema(implementation = Achievement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/{id}")
    public void deleteAchievement(@PathVariable Integer id) {
        achievementService.deleteAchievement(id);
    }

}
