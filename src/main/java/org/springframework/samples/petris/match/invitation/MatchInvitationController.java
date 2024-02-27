package org.springframework.samples.petris.match.invitation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.auth.payload.response.MessageResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/petris/matchInvitation")
@SecurityRequirement(name = "bearerAuth")
public class MatchInvitationController {
    
    MatchInvitationService matchInvitationService;

    @Autowired
    public MatchInvitationController(MatchInvitationService matchInvitationService){
        this.matchInvitationService = matchInvitationService;
    }


        @Operation(summary = "Retrives a list of match invitations", description = "Gets a list of match invitations whose receiverId is the given", tags = {
            "matches", "get", "matchInvitations" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Match inviations retrieved", content = {
                    @Content(schema = @Schema(implementation = MatchInvitation.class), mediaType = "application/json") }),
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
    public ResponseEntity<List<MatchInvitation>> findMatchInvitationByReceiverId(@RequestParam Integer receiverId){
        List<MatchInvitation> body = matchInvitationService.findMatchInvitationByReceiverId(receiverId);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Creates a match invitations", description = "Creates a match invitation whose reciever is the player whose username is the given", tags = {
        "matches", "post", "matchInvitations" })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "MatchInvitation retrieved", content = {
                @Content(schema = @Schema(implementation = MatchInvitation.class), mediaType = "application/json") }),
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
    public ResponseEntity<MatchInvitation> saveMatchInvitation(@RequestParam Integer matchId, @RequestParam String username){
        MatchInvitation body = matchInvitationService.saveMatchInvitation(matchId, username);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Deletes a match invitations", description = "Deletes the match invitation whose id is the given", tags = {
        "matches", "delete", "matchInvitations" })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Invitacion rechazada", content = {
                @Content(schema = @Schema(implementation = MatchInvitation.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(schema = @Schema()) }),    
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema()) }),
})
    @DeleteMapping("/{id}/reject")
    public ResponseEntity<MessageResponse> deleteMatchInvitation(@PathVariable Integer id){
        matchInvitationService.deleteMatchInvitation(id);
        MessageResponse body = new MessageResponse("Invitacion rechazada");
        return ResponseEntity.ok(body);
    }
}
