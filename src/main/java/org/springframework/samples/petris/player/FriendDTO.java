package org.springframework.samples.petris.player;

import java.util.List;

import org.springframework.samples.petris.model.BaseEntity;
import org.springframework.samples.petris.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDTO extends BaseEntity{
	
    @NotNull
	User user;

    @JsonIgnore
    List<Player> beFriend;

    public FriendDTO(){}

	public FriendDTO(Player p){
		this.id = p.getId();
		this.user = p.getUser();
	}
}

