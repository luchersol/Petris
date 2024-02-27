package org.springframework.samples.petris.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

@DataJpaTest()
public class CommentRepositoryTest {

    @Autowired
    CommentRepository cr;


    @Test
    public void findAllByUserId(){
        List<Comment> res = cr.findAllCommentsByUserId(1);
        assertEquals(res.size(),3);
    }

    @Test
    public void findAllByMatchId(){
        List<Comment> res = cr.findAllCommentsByMatchId(3, Sort.unsorted());
        assertEquals(res.size(),1);
    }

    @Test
    public void findAllByUserIdNull(){
        List<Comment> res = cr.findAllCommentsByUserId(null);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findAllByIncorrectMatchId(){
        List<Comment> res = cr.findAllCommentsByMatchId(33, Sort.unsorted());
        assertTrue(res.isEmpty());
    }
}
