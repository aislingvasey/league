package com.africaapps.league.model.league;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="league_season")
public class LeagueSeason extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private LeagueSeasonStatus status;
	private Date startDate;
	private Date endDate;
	private League league;
	private Set<TeamLeagueSeason> teamLeagueSeasons;
	
	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="league_season_seq", sequenceName="league_season_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="league_season_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Size(min=1, max=100, message="{validate.name.message}")
	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(message="{validate.status.required}")
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	public LeagueSeasonStatus getStatus() {
		return status;
	}

	public void setStatus(LeagueSeasonStatus status) {
		this.status = status;
	}

	@NotNull(message="validate.startdate.required")
	@Column(name="start_date", nullable=false)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@NotNull(message="{validate.enddate.required}")
	@Column(name="end_date", nullable=false)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@NotNull(message="{validate.league.required}")
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
  @JoinColumn(name = "league_id")
	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	@OneToMany(mappedBy = "leagueSeason")
	public Set<TeamLeagueSeason> getTeamLeagueSeasons() {
		return teamLeagueSeasons;
	}

	public void setTeamLeagueSeasons(Set<TeamLeagueSeason> teamLeagueSeasons) {
		this.teamLeagueSeasons = teamLeagueSeasons;
	}
}
