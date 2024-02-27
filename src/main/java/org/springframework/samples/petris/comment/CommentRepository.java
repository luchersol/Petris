package org.springframework.samples.petris.comment;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer>{
    
    @Query("SELECT c FROM Comment c WHERE c.sentBy.id = :id")
    List<Comment> findAllCommentsByUserId(Integer id);

    @Query("SELECT c FROM Comment c WHERE c.sentIn.id = :id")
    List<Comment> findAllCommentsByMatchId(int id, Sort sort);
}
