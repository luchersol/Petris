package org.springframework.samples.petris.achievement;

import org.springframework.samples.petris.admin.Admin;
import org.springframework.samples.petris.model.NamedEntity;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.stats.Stats;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements", 
        uniqueConstraints = {@UniqueConstraint(columnNames = {"meter","numCondition"})})
public class Achievement extends NamedEntity{

    private String badgeImage;
    
    @NotBlank
    private String description;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private Meter meter;

    @NotNull
    @Min(1)
    private Integer numCondition;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Admin creator;

    public Boolean completedBy(Player player){
        Stats stats = player.getStats();
        Integer statToCheck = switch(this.meter){
            case DEFEAT -> stats.getLosses();
            case MATCH -> stats.countMatches();
            case VICTORY -> stats.getVictories();
        };
        return statToCheck >= this.numCondition;
    }

    public void selectBagImage(){
        this.badgeImage = this.meter.getUri();
    }

}
