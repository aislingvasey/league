package com.africaapps.league.model.league;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="match")
public class Match extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Date startDateTime;
	private Date endDateTime;
	private String location;
	private String finalScore;
	private Team team1;
	private Team team2;
	private MatchProcessingStatus status;
	
	@NotNull(groups={UpdateGroup.class})
	@Id
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
  @Column(name="start_date_time", nullable=false)	
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	@NotNull
  @Column(name="end_date_time", nullable=true)	
	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Size(min=1, max=200, message="{validate.location.range}")
  @Column(name="location", length=200, nullable=true)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@NotNull
	@Size(min=1, max=50, message="{validate.finalscore.range}")
  @Column(name="final_score", length=50, nullable=true)	
	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	@ManyToOne
	@JoinColumn(name="team1_id", nullable=false)
	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	@ManyToOne
	@JoinColumn(name="team2_id", nullable=false)
	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	@Column(name="match_processing_status", length=50, nullable=false)
	@Enumerated(EnumType.STRING)
	public MatchProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(MatchProcessingStatus status) {
		this.status = status;
	}
}
