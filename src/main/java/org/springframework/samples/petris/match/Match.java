package org.springframework.samples.petris.match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.springframework.samples.petris.model.NamedEntity;
import org.springframework.samples.petris.petriDish.PetriDish;
import org.springframework.samples.petris.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "matches")
public class Match extends NamedEntity {

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private String code;

	@Min(1)
	@NotNull
	private Integer numTurn;

	@Range(min = 0, max = 8)
	@NotNull
	private Integer contaminationLevelBlue;

	@Range(min = 0, max = 8)
	@NotNull
	private Integer contaminationLevelRed;

	@NotNull
	private Boolean isPrivated;

	@ManyToOne
	@JoinColumn(name = "winner_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Player winner;

	@ManyToOne
	@JoinColumn(name = "player_blue_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Player creator;

	@ManyToOne
	@JoinColumn(name = "player_red_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Player player;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "match_id")
	private List<PetriDish> petriDishes;

	public Integer countBacteriumPlayerBlue() {
		return petriDishes.stream().map(p -> p.getChipsPlayerBlue())
				.filter(value -> value != 5)
				.mapToInt(v -> v)
				.sum();
	}

	public Integer countBacteriumPlayerRed() {
		return petriDishes.stream().map(p -> p.getChipsPlayerRed())
				.filter(value -> value != 5)
				.mapToInt(v -> v)
				.sum();
	}

	public Integer countSarcinPlayerBlue() {
		return (int) petriDishes.stream().map(p -> p.getChipsPlayerBlue())
				.filter(value -> value == 5)
				.mapToInt(v -> v)
				.count();
	}

	public Integer countSarcinPlayerRed() {
		return (int) petriDishes.stream().map(p -> p.getChipsPlayerRed())
				.filter(value -> value == 5)
				.mapToInt(v -> v)
				.count();
	}

	public Boolean checkCorrectCode() {
		return isPrivated ? code != null : code == null;
	}

	public String generateMatchCode() {
		String res = "";

		String options = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			Integer index = random.nextInt(options.length());
			res += options.charAt(index);
		}
		return res;
	}
}
