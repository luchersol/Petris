package org.springframework.samples.petris.stats;

import org.springframework.samples.petris.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stats")
public class Stats extends BaseEntity{

	@NotNull
	@PositiveOrZero
	private Integer totalBacterium;

	@NotNull
	@PositiveOrZero
	private Integer totalSarcinas;

	@NotNull
	@PositiveOrZero
	private Integer victories;

	@NotNull
	@PositiveOrZero
	private Integer losses;

	public static Stats startStats() {
		Stats stats = new Stats();
		stats.setLosses(0);
		stats.setTotalBacterium(0);
		stats.setVictories(0);
		stats.setTotalSarcinas(0);
		return stats;
	}

	public Integer countMatches(){
		return this.victories + this.losses;
	}

	public static Stats initialStats(){
		Stats stats = new Stats();
		stats.setLosses(0);
		stats.setVictories(0);
		stats.setTotalBacterium(0);
		stats.setTotalSarcinas(0);
		return stats;
	}

}
