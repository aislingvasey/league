package com.africaapps.league.model.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="player_match_statistic")
public class PlayerMatchStatistic extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private PlayerMatch playerMatch;
	private Double value;
	private Double points;
	private Statistic statistic;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PlayerMatchStatistic: ");
		builder.append(" playerMatch:").append(playerMatch);
		builder.append(" value:").append(value);
		builder.append(" points:").append(points);
		builder.append(" statistic:").append(statistic);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PlayerMatchStatistic)) {
			return false;
		} else {
			PlayerMatchStatistic s = (PlayerMatchStatistic) o;
			if (s.getId().equals(id)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="player_match_stat_seq", sequenceName="player_match_stat_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_match_stat_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name="player_match_id")
	public PlayerMatch getPlayerMatch() {
		return playerMatch;
	}

	public void setPlayerMatch(PlayerMatch playerMatch) {
		this.playerMatch = playerMatch;
	}

	@ManyToOne
	@JoinColumn(name="statistic_id")
	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}

	@Column(name="value", nullable = false)
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Column(name="points", nullable = false)
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
}
