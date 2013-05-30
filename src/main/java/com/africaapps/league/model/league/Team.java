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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="team", uniqueConstraints = {@UniqueConstraint(columnNames={"team_id", "league_season_id"})})
public class Team extends BaseDataModel {

	private static final long serialVersionUID = 1L;
	
	private int teamId; //external id
	private String clubName;
	private String teamName;
	private String city;
	private LeagueSeason leagueSeason;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Team: ");
		builder.append(" id:").append(id);
		builder.append(" teamId:").append(teamId);
		builder.append(" clubName:").append(clubName);
		builder.append(" teamName:").append(teamName);
		builder.append(" city:").append(city);
		builder.append(" leagueSeason:").append(leagueSeason);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Team)) {
			return false;
		} else {
			Team t = (Team) o;
			if (t.getId().equals(id)) {
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
	@SequenceGenerator(name="team_seq", sequenceName="team_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="team_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Index(name="team_id_index", columnNames = "team_id")
  @Column(name="team_id", nullable=false)
	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@NotNull
	@Size(min=1, max=500, message="{validate.clubname.range}")
  @Column(name="club_name", length=500, nullable=false)
	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	@Size(min=1, max=500, message="{validate.teamname.range}")
  @Column(name="team_name", length=500, nullable=true)
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	@Size(min=1, max=500, message="{validate.city.range}")
  @Column(name="city", length=500, nullable=true)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@ManyToOne
	@JoinColumn(name="league_season_id", nullable=false)
	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}
}