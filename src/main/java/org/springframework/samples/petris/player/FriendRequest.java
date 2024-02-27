package org.springframework.samples.petris.player;

import org.springframework.samples.petris.model.Notification;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "friend_request")
public class FriendRequest extends Notification{

    
}