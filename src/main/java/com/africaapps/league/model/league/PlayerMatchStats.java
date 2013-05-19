package com.africaapps.league.model.league;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

public class PlayerMatchStats extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private PlayerMatch playerMatch;
	private Statistic statistic;
	private Date dateTime;
	
	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="player_match_stats_seq", sequenceName="player_match_stats_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_match_stats_seq")
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

	@NotNull
	@Column(name="date_time", nullable=false)
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
