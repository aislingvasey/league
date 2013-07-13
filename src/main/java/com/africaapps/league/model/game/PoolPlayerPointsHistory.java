package com.africaapps.league.model.game;

import java.util.Date;

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
import com.africaapps.league.model.league.Match;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name = "game_pool_player_points_history")
public class PoolPlayerPointsHistory extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private PoolPlayer poolPlayer;
	private Match match;
	private int playerPoints;
	private Date addedDateTime;
	private PlayingWeek playingWeek;
	
	public PoolPlayerPointsHistory() {
		this.addedDateTime = new Date();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PoolPlayerPointsHistory: ");
		builder.append(" id:").append(id);
		builder.append(" addedDateTime:").append(addedDateTime);
		builder.append(" playerPoints:").append(playerPoints);
		builder.append(" poolPlayer:").append(poolPlayer);
		builder.append(" match:").append(match);
		builder.append(" playingWeek:").append(playingWeek);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PoolPlayerPointsHistory)) {
			return false;
		} else {
			PoolPlayerPointsHistory h = (PoolPlayerPointsHistory) o;
			if (h.getId().equals(id)) {
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

	@NotNull(groups = { UpdateGroup.class })
	@Id
	@SequenceGenerator(name = "pool_player_points_history_seq", sequenceName = "pool_player_points_history_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pool_player_points_history_seq")
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name = "pool_player_id", nullable=false)
	public PoolPlayer getPoolPlayer() {
		return poolPlayer;
	}
	
	public void setPoolPlayer(PoolPlayer poolPlayer) {
		this.poolPlayer = poolPlayer;
	}
	
	@ManyToOne
	@JoinColumn(name = "match_id", nullable=false)
	public Match getMatch() {
		return match;
	}
	
	public void setMatch(Match match) {
		this.match = match;
	}
	
	@Column(name="player_points", nullable=false)
	public int getPlayerPoints() {
		return playerPoints;
	}
	
	public void setPlayerPoints(int playerPoints) {
		this.playerPoints = playerPoints;
	}
	
	@Column(name="added_date_time", nullable=false)
	public Date getAddedDateTime() {
		return addedDateTime;
	}
	
	public void setAddedDateTime(Date addedDateTime) {
		this.addedDateTime = addedDateTime;
	}

	@ManyToOne
	@JoinColumn(name="playing_week_id", nullable=false)
	public PlayingWeek getPlayingWeek() {
		return playingWeek;
	}

	public void setPlayingWeek(PlayingWeek playingWeek) {
		this.playingWeek = playingWeek;
	}
}
