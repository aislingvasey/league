package com.africaapps.league.model.league;

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
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="league_data")
public class LeagueData extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private League league;
	private Date lastFeedRun;
	private int noTradeHours;
	private int squadSize;
	private int numberOfSubstitutes;
	private int numberOfGoalkeepers;
	private int initTeamMoney;
	private int userPointsPlayingWeek;	
	private TeamFormat defaultTeamFormat;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[LeagueData: ");
		builder.append(" id:").append(id);
		builder.append(" league:").append(league);
		builder.append(" lastFeedRun:").append(lastFeedRun);
		builder.append(" squadSize:").append(squadSize);
		builder.append(" numberOfSubstitutes:").append(numberOfSubstitutes);
		builder.append(" numberOfGoalkeepers:").append(numberOfGoalkeepers);
		builder.append(" initTeamMoney:").append(initTeamMoney);
		builder.append(" userPointsPlayingWeek:").append(userPointsPlayingWeek);
		builder.append(" defaultTeamFormat:").append(defaultTeamFormat);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof LeagueData)) {
			return false;
		} else {
			LeagueData l = (LeagueData) o;
			if (l.getId().equals(id)) {
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
  @SequenceGenerator(name="league_data_seq", sequenceName="league_data_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="league_data_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name="league_id", nullable = false)
  public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	@Column(name="last_feed_datetime")
	public Date getLastFeedRun() {
		return lastFeedRun;
	}

	public void setLastFeedRun(Date lastFeedRun) {
		this.lastFeedRun = lastFeedRun;
	}

	@Column(name="no_trade_hours", nullable=false)
	public int getNoTradeHours() {
		return noTradeHours;
	}

	public void setNoTradeHours(int noTradeHours) {
		this.noTradeHours = noTradeHours;
	}

	@Column(name="squad_size", nullable=false)
	public int getSquadSize() {
		return squadSize;
	}

	public void setSquadSize(int squadSize) {
		this.squadSize = squadSize;
	}

	@Column(name="substitutes_count", nullable=false)
	public int getNumberOfSubstitutes() {
		return numberOfSubstitutes;
	}

	public void setNumberOfSubstitutes(int numberOfSubstitutes) {
		this.numberOfSubstitutes = numberOfSubstitutes;
	}

	@Column(name="goalkeepers_count", nullable=false)
	public int getNumberOfGoalkeepers() {
		return numberOfGoalkeepers;
	}

	public void setNumberOfGoalkeepers(int numberOfGoalkeepers) {
		this.numberOfGoalkeepers = numberOfGoalkeepers;
	}

	@Column(name="init_team_money", nullable=false)
	public int getInitTeamMoney() {
		return initTeamMoney;
	}

	public void setInitTeamMoney(int initTeamMoney) {
		this.initTeamMoney = initTeamMoney;
	}
	
	@Column(name="user_points_playingweek", nullable=false)
	public int getUserPointsPlayingWeek() {
		return userPointsPlayingWeek;
	}

	public void setUserPointsPlayingWeek(int userPointsPlayingWeek) {
		this.userPointsPlayingWeek = userPointsPlayingWeek;
	}

	@ManyToOne
	@JoinColumn(name="team_format_id", nullable=false)
	public TeamFormat getDefaultTeamFormat() {
		return defaultTeamFormat;
	}

	public void setDefaultTeamFormat(TeamFormat defaultTeamFormat) {
		this.defaultTeamFormat = defaultTeamFormat;
	}
}
