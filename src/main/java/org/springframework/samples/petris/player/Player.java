package org.springframework.samples.petris.player;


import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.samples.petris.model.BaseEntity;
import org.springframework.samples.petris.stats.Stats;
import org.springframework.samples.petris.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends BaseEntity{
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name="player_friends",
		joinColumns = @JoinColumn(name = "id1", referencedColumnName = "id"),
    	inverseJoinColumns = @JoinColumn(name = "id2", referencedColumnName = "id"))
	@OnDelete(action = OnDeleteAction.CASCADE)
    private List<Player> beFriends;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "stats_id")
	private Stats stats;

	public void addFriend(Player friend){
		this.beFriends.add(friend);
	}

	public void deleteFriend(Player friend){
		this.beFriends.remove(friend);
	}

	public void clearFriend(){
		this.beFriends.clear();
	}

}
