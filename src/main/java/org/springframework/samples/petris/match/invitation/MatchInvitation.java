package org.springframework.samples.petris.match.invitation;

import org.springframework.samples.petris.match.Match;
import org.springframework.samples.petris.model.Notification;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "matchInvitations", uniqueConstraints = {@UniqueConstraint(columnNames = {"match_id", "author", "receiver"})})
public class MatchInvitation extends Notification{
	
    @ManyToOne
	@JoinColumn(name = "match_id")
	private Match match;

}
