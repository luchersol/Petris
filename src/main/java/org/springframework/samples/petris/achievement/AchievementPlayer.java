package org.springframework.samples.petris.achievement;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.samples.petris.model.BaseEntity;
import org.springframework.samples.petris.player.Player;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievement_player")
public class AchievementPlayer extends BaseEntity{
    
    @NotNull
    @PastOrPresent
    private LocalDateTime achievementDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;

}
