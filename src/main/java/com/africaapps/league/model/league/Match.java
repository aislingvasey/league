package com.africaapps.league.model.league;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name = "match", uniqueConstraints = { @UniqueConstraint(columnNames = { "match_id", "league_season_id" }) })
public class Match extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Integer matchId; // external id
	private LeagueSeason leagueSeason;
	private Date startDateTime;
	private String finalScore;
	private Team team1;
	private Team team2;
	private MatchProcessingStatus status;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Match: ");
		builder.append(" id:").append(id);
		builder.append(" matchId:").append(matchId);
		builder.append(" leagueSeason:").append(leagueSeason);
		builder.append(" startDateTime:").append(startDateTime);
		builder.append(" finalScore:").append(finalScore);
		builder.append(" team1:").append(team1 == null ? "" : team1.getClubName());
		builder.append(" team2:").append(team2 == null ? "" : team2.getClubName());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Match)) {
			return false;
		} else {
			Match m = (Match) o;
			if (m.getId().equals(id)) {
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
	@SequenceGenerator(name = "match_seq", sequenceName = "match_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq")
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Index(name = "match_id_index", columnNames = "match_id")
	@Column(name = "match_id", nullable = false)
	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	@ManyToOne
	@JoinColumn(name = "league_season_id", nullable = false)
	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	@NotNull
	@Column(name = "start_date_time", nullable = false)
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Size(min = 1, max = 50, message = "{validate.finalscore.range}")
	@Column(name = "final_score", length = 50, nullable = true)
	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	@ManyToOne
	@JoinColumn(name = "team1_id", nullable = false)
	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	@ManyToOne
	@JoinColumn(name = "team2_id", nullable = false)
	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	@Column(name = "match_processing_status", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	public MatchProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(MatchProcessingStatus status) {
		this.status = status;
	}
}
