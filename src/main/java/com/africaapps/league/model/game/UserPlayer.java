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
import com.africaapps.league.model.league.Player;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_user_player")
public class UserPlayer extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Player player;
	private UserPlayerStatus status;
	private UserTeam team;	
	private Pool pool;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[UserPlayer: ");
		builder.append(" id:").append(id);
		builder.append(" player:").append(player);
		builder.append(" status:").append(status);
		builder.append(" team:").append(team);
		builder.append(" pool:").append(pool);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof UserPlayer)) {
			return false;
		} else {
			UserPlayer u = (UserPlayer) o;
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
	
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="user_player_seq", sequenceName="user_player_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_player_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name="player_id", nullable=false)
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@NotNull
	@Column(name="status", length=50, nullable=false)
	public UserPlayerStatus getStatus() {
		return status;
	}

	public void setStatus(UserPlayerStatus status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name="user_team_id", nullable=false)
	public UserTeam getTeam() {
		return team;
	}

	public void setTeam(UserTeam team) {
		this.team = team;
	}

	@ManyToOne
	@JoinColumn(name="pool_id", nullable=false)
	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}
}
