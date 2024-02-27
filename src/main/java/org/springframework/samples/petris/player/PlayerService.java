package org.springframework.samples.petris.player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Service
public class PlayerService {

    PlayerRepository playerRepository;
    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, FriendRequestRepository friendRequestRepository, UserRepository userRepository){
        this.playerRepository = playerRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Player savePlayer(@RequestBody @Valid Player player) {
        return playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public Player findPlayerById(Integer id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Player findPlayerByUserId(Integer id) {
        return playerRepository.findPlayerByUserId(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Player findPlayerByUsername(String username) {
        return playerRepository.findPlayerByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> findFriendsByPlayerId(Integer id) {
        return playerRepository.findFriendsById(id);
    }

    @Transactional(readOnly = true)
    public Set<Player> findFriendsByPlayer(Player player) {
        Set<Player> p = new HashSet<>();
        // DUDAS
        p.addAll(playerRepository.findFriendsByPlayer(player));
        p.addAll(playerRepository.findFriendsById(player.getId()));
        return p;
    }

    @Transactional(readOnly = true)
    public List<Player> findFriendsByIdAuthor(Integer id) {
        return friendRequestRepository.findFriendsByIdAuthor(id);
    }

    @Transactional(readOnly = true)
    public List<Player> findFriendsByIdReceiver(Integer id) {
        return friendRequestRepository.findFriendsByIdReceiver(id);
    }

    // FRIEND REQUEST THINGS

    // enviar peticion amistad
    @Transactional
    public FriendRequest saveFriendRequest(FriendRequest friendRequest) {
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);
        return savedFriendRequest;
    }

    // aceptar peticion amistad y amadir amigo
    @Transactional
    public FriendRequest acceptFriendRequest(FriendRequest friendRequest) {
        Player author = friendRequest.getAuthor(), receiver = friendRequest.getReceiver();
        author.addFriend(receiver);
        playerRepository.save(author);
        friendRequestRepository.delete(friendRequest);
        return friendRequest;
    }

    @Transactional(readOnly = true)
    public FriendRequest findFriendRequestByPlayers(Integer idPlayer1, Integer idPlayer2) {
        return friendRequestRepository.findFriendRequestByPlayers(idPlayer1, idPlayer2).orElse(null);
    }

    // rechazar peticion amistad
    @Transactional
    public void deleteFriendRequest(Integer id) {
        FriendRequest toDelete = findFriendRequestById(id);
		this.friendRequestRepository.delete(toDelete);
    }

    @Transactional(readOnly = true)
    public FriendRequest findFriendRequestById(Integer id) {
        return friendRequestRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> findFriendRequestByReceiver(Integer receiberId){
        return friendRequestRepository.findFriendRequestByReceiver(receiberId);
    }

    @Transactional
    public Player updatePlayer(Player player, Integer id) {
        Player toUpdate = playerRepository.findById(id).orElse(null);
        BeanUtils.copyProperties(player, toUpdate, "id");
        toUpdate.setUser(userRepository.save(toUpdate.getUser()));
        return playerRepository.save(toUpdate);
    }

    @Transactional
    public Player updateFriends(Player player, Integer playerId, Integer friendId) {
        List<Player> playerFriendList = playerRepository.findFriendsById(playerId);
        
        Player friend = playerRepository.findById(friendId).orElse(null);
        Player toUpdate = player;

        if(playerFriendList.contains(friend)){
            playerFriendList.remove(friend);
            player.setBeFriends(playerFriendList);
            toUpdate = updatePlayer(player, playerId);

        } else {
            List<Player> listFriends = playerRepository.findFriendsById(friendId);
            listFriends.remove(player);
            friend.setBeFriends(listFriends);
            updatePlayer(friend, friendId);
        }

        return toUpdate;
    }

    @Transactional
    public void deletePlayerByUser(Integer userId){
        Player toDeleted = findPlayerByUserId(userId);
        if(toDeleted == null)
            throw new ResourceNotFoundException("There is no player with a user with Id: " + userId);
        
        for(Player player: findFriendsByPlayer(toDeleted)){
            player.deleteFriend(toDeleted);
        }

        toDeleted.clearFriend();
        playerRepository.save(toDeleted);
        playerRepository.deleteById(toDeleted.getId());
        userRepository.deleteById(userId);
    }
}
