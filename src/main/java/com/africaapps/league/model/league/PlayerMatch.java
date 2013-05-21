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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="player_match", uniqueConstraints = {@UniqueConstraint(columnNames={"match_id", "player_id"})})
public class PlayerMatch extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Match match;
	private Player player;
	private Integer playerScore;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PlayerMatch: ");
		builder.append(" id:").append(id);
		builder.append(" match:").append(match == null ? "" : match.getId());
		builder.append(" player:").append(player == null ? "" : player.getId());
		builder.append(" playerScore:").append(playerScore);		
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PlayerMatch)) {
			return false;
		} else {
			PlayerMatch p = (PlayerMatch) o;
			if (p.getId().equals(id)) {
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
  @SequenceGenerator(name="player_match_seq", sequenceName="player_match_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_match_seq")
	public Long getId() {
		return id;
	}

	@ManyToOne
  @JoinColumn(name = "match_id")
	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	@ManyToOne
  @JoinColumn(name = "player_id")
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Column(name="player_score", nullable=true)
	public Integer getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(Integer playerScore) {
		this.playerScore = playerScore;
	}
}
