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
@Table(name="team_league_season", uniqueConstraints = {@UniqueConstraint(columnNames={"league_season_id", "team_id"})})
public class TeamLeagueSeason extends BaseDataModel {

	private static final long serialVersionUID = 1L;

  private LeagueSeason leagueSeason;
  private Team team;
  private Integer currentPosition;
  private int matchesPlayed;
  private int matchesWon;
  private int matchesDrawn;
  private int matchesLost;

  @ManyToOne
  @JoinColumn(name = "league_season_id")
	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	@ManyToOne
  @JoinColumn(name = "team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Column(name = "current_position", nullable=true)
	public Integer getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Integer currentPosition) {
		this.currentPosition = currentPosition;
	}

	@Column(name = "matches_player", nullable=false)
	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	@Column(name = "matches_won", nullable=false)
	public int getMatchesWon() {
		return matchesWon;
	}

	public void setMatchesWon(int matchesWon) {
		this.matchesWon = matchesWon;
	}

	@Column(name = "matches_drawn", nullable=false)
	public int getMatchesDrawn() {
		return matchesDrawn;
	}

	public void setMatchesDrawn(int matchesDrawn) {
		this.matchesDrawn = matchesDrawn;
	}

	@Column(name = "matches_lost", nullable=false)
	public int getMatchesLost() {
		return matchesLost;
	}

	public void setMatchesLost(int matchesLost) {
		this.matchesLost = matchesLost;
	}

	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="team_league_season_seq", sequenceName="team_league_season_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="team_league_season_seq")
	public Long getId() {
		return id;
	}
}
