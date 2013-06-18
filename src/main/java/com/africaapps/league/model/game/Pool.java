package com.africaapps.league.model.game;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_pool")
public class Pool extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private LeagueSeason leagueSeason;
	private Set<PoolPlayer> players;
	private Set<UserLeague> userLeagues;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Pool: ");
		builder.append(" id:").append(id);
		builder.append(" leagueSeason:").append(leagueSeason);		
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Pool)) {
			return false;
		} else {
			Pool p = (Pool) o;
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
  @SequenceGenerator(name="pool_seq", sequenceName="pool_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pool_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="league_season_id", nullable=false, unique=true)
	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	@OneToMany(mappedBy = "pool")
	public Set<PoolPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(Set<PoolPlayer> players) {
		this.players = players;
	}

	@OneToMany(mappedBy = "pool")
	public Set<UserLeague> getUserLeagues() {
		return userLeagues;
	}

	public void setUserLeagues(Set<UserLeague> userLeagues) {
		this.userLeagues = userLeagues;
	}
}
