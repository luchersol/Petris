package org.springframework.samples.petris.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    
    CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Comment> findAllCommentsByUserId(int userId){
        return commentRepository.findAllCommentsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllCommentsByMatchId(int matchId){
        List<Comment> res = commentRepository.findAllCommentsByMatchId(matchId, Sort.by("commentDate"));
        return res;
    }

    @Transactional
    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }
    
}
