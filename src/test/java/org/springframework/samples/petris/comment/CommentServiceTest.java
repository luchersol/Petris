package org.springframework.samples.petris.comment;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.match.MatchService;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;


@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchService matchService;

    @Test
    public void shouldFindAllByUserId(){
        List<Comment> comments = commentService.findAllCommentsByUserId(2);
        assertNotNull(comments);
        assertTrue(comments.size() == 3);



    }

    @Test
    public void shouldNotFindAllByIncorrectUserId(){
        List<Comment> comments = commentService.findAllCommentsByUserId(6);
        assertTrue(comments.isEmpty());


    }

    @Test
    public void shouldFindAllByMatchId(){
        List<Comment> comments = commentService.findAllCommentsByMatchId(4);
        assertNotNull(comments);
        assertTrue(comments.size() == 1);

    }

    @Test
    public void shouldNotFindAllByIncorrectMatchId(){
        List<Comment> comments = commentService.findAllCommentsByMatchId(16);
        assertTrue(comments.isEmpty());


    }

    @Test
    public void shouldCreateComment(){
        DateTimeFormatter formatoFecha =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Player p1 = playerService.findPlayerById(1);
        Match m1 = matchService.findMatchById(1);
        Comment comment = new Comment();
        comment.setId(7);
        comment.setMessage("Voy ganando. Tomaaaa");
        comment.setCommentDate(LocalDateTime.parse("2023-01-01 16:45", formatoFecha));
        comment.setSentBy(p1);
        comment.setSentIn(m1);
        this.commentService.saveComment(comment);
        assertNotEquals(0, comment.getId().longValue());
		assertNotNull(comment.getId());



    }


    
}
