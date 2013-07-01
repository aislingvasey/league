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
@Table(name = "game_user_team_score_history")
public class UserTeamScoreHistory extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private UserTeam userTeam;
	private Match match;
	private PoolPlayer poolPlayer;
	private int playerPoints;
	private Date addedDateTime;
	
	public UserTeamScoreHistory() {
		this.addedDateTime = new Date();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[UserTeamScoreHistory: ");
		builder.append(" id:").append(id);
		builder.append(" addedDateTime:").append(addedDateTime);
		builder.append(" playerPoints:").append(playerPoints);
		if (poolPlayer != null) {
			builder.append(" poolPlayer:").append(poolPlayer.getId());
		}
		if (userTeam != null) {
			builder.append(" userTeam:").append(userTeam.getId());
		}
		if (match != null) {
			builder.append(" match:").append(match.getId());
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof UserTeamScoreHistory)) {
			return false;
		} else {
			UserTeamScoreHistory h = (UserTeamScoreHistory) o;
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
	@SequenceGenerator(name = "user_team_score_history_seq", sequenceName = "user_team_score_history_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_team_score_history_seq")
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_team_id", nullable=false)
	public UserTeam getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(UserTeam userTeam) {
		this.userTeam = userTeam;
	}

	@ManyToOne
	@JoinColumn(name = "pool_player_id", nullable=false)
	public PoolPlayer getPoolPlayer() {
		return poolPlayer;
	}

	public void setPoolPlayer(PoolPlayer poolPlayer) {
		this.poolPlayer = poolPlayer;
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
	@JoinColumn(name="match_id", nullable=false)
	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}
}
