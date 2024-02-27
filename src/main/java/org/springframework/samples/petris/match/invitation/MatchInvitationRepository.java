package org.springframework.samples.petris.match.invitation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchInvitationRepository extends CrudRepository<MatchInvitation, Integer>{

    @Query("SELECT invitation FROM MatchInvitation invitation WHERE invitation.receiver.id = :receiverId AND invitation.match.startDate IS NULL")
    List<MatchInvitation> findMatchInvitationByReceiverId(Integer receiverId);


}
