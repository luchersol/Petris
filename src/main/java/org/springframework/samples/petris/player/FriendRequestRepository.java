package org.springframework.samples.petris.player;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
    
	// @Modifying
	// @Query("DELETE FROM FriendRequest fr WHERE fr.author.id = :idAuthor AND fr.receiver.id = :idReceiver")
	// void deleteByAuthorAndReceiver(Integer idAuthor, Integer idReceiver);

    @Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE (friendRequest.author.id = :idPlayer1 AND friendRequest.receiver.id = :idPlayer2) OR (friendRequest.author.id = :idPlayer2 AND friendRequest.receiver.id = :idPlayer1)")
    Optional<FriendRequest> findFriendRequestByPlayers(Integer idPlayer1, Integer idPlayer2);

	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.receiver.id = :reciberId")
    List<FriendRequest> findFriendRequestByReceiver(Integer reciberId);

	@Query("SELECT fr.receiver FROM FriendRequest fr WHERE fr.isAccepted = true AND fr.author.id = :id")
    List<Player> findFriendsByIdAuthor(Integer id);

    @Query("SELECT fr.author FROM FriendRequest fr WHERE fr.isAccepted = true AND fr.receiver.id = :id")
    List<Player> findFriendsByIdReceiver(Integer id);
}
