package org.springframework.samples.petris.comment;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchService;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/petris/comment")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {
    
    CommentService commentService;
    MatchService matchService;
    PlayerService playerService;
    
    @Autowired
    public CommentController(CommentService commentService, MatchService matchService, PlayerService playerService){
        this.commentService = commentService;
        this.matchService = matchService;
        this.playerService = playerService;
    }


    @Operation(summary = "Retrives a list of comments", description = "Gets the comments of a match given its id", tags = { "matches", "get", "comments" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments retrieved", content = {
                    @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByMatchId(@RequestParam Integer match_id){
        List<Comment> body = commentService.findAllCommentsByMatchId(match_id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Creates a comment", description = "Creates a comment and associates it with a match", tags = { "matches", "post", "comments", "users" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment created", content = {
                    @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Match not found or user not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Comment> createComment(@RequestBody @Valid Comment comment, @RequestParam Integer match_id, @RequestParam Integer user_id){
        Comment newComment = new Comment();
        Match match = matchService.findMatchById(match_id);
        Player player = playerService.findPlayerByUserId(user_id);
        BeanUtils.copyProperties(comment, newComment, "id");
        newComment.setSentIn(match);
        newComment.setSentBy(player);
        Comment body = commentService.saveComment(newComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
    
}
