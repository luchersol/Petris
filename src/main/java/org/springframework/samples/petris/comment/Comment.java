package org.springframework.samples.petris.comment;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.model.BaseEntity;
import org.springframework.samples.petris.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment extends BaseEntity{
	
	@NotBlank
	private String message;

	@NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime commentDate;
	
	@ManyToOne
	@JoinColumn(name="player_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Player sentBy;
	
	@ManyToOne
	@JoinColumn(name="match_id")
	private Match sentIn;
	

}
