package org.springframework.samples.petris.petriDish;

import org.hibernate.validator.constraints.Range;
import org.springframework.samples.petris.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "petrisDishes")
public class PetriDish extends BaseEntity{
    
    @NotNull
    @Range(min = 0, max = 6)
    private Integer index;
    @NotNull
    @Range(min = 0, max = 5)
    private Integer chipsPlayerBlue;
    @NotNull
    @Range(min = 0, max = 5)
    private Integer chipsPlayerRed;

    public PetriDish(){}

    private PetriDish(int index, int chipsPlayerBlue, int chipsPlayerRed){
        this.index = index;
        this.chipsPlayerBlue = chipsPlayerBlue;
        this.chipsPlayerRed = chipsPlayerRed;
    }

    public static PetriDish empty(int index){
        return new PetriDish(index, 0, 0);
    }

    public static PetriDish firstBlue(){
        return new PetriDish(2, 1, 0);
    }

    public static PetriDish firstRed(){
        return new PetriDish(4, 0, 1);
    }
    

}
