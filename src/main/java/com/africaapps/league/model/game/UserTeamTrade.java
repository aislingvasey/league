package com.africaapps.league.model.game;

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
@Table(name="game_user_team_trade")
public class UserTeamTrade extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private UserTeam userTeam;	
	private PoolPlayer poolPlayer;	
	private PlayingWeek playingWeek;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[UserTeamTrade: ");
		builder.append(" id:").append(id);
		builder.append(" userTeam:").append(userTeam);
		builder.append(" poolPlayer:").append(poolPlayer);
		builder.append(" playingWeek:").append(playingWeek);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof UserTeamTrade)) {
			return false;
		} else {
			UserTeamTrade u = (UserTeamTrade) o;
			if (u.getId().equals(id)) {
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
	@SequenceGenerator(name = "user_team_trade_seq", sequenceName = "user_team_trade_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_team_trade_seq")
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name="user_team_id", nullable=false)
	public UserTeam getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(UserTeam userTeam) {
		this.userTeam = userTeam;
	}

	@ManyToOne
	@JoinColumn(name="pool_player_id", nullable=false)
	public PoolPlayer getPoolPlayer() {
		return poolPlayer;
	}

	public void setPoolPlayer(PoolPlayer poolPlayer) {
		this.poolPlayer = poolPlayer;
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