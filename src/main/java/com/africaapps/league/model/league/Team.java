package com.africaapps.league.model.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="team")
public class Team extends BaseDataModel {

	private static final long serialVersionUID = 1L;
	
	private String clubName;
	private String teamName;
	private String city;
	private String manager;
	private String coach;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Team: ");
		builder.append(" id:").append(id);
		builder.append(" clubName:").append(clubName);
		builder.append(" teamName:").append(teamName);
		builder.append(" city:").append(city);
		builder.append(" manager:").append(manager);
		builder.append(" coach:").append(coach);
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
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
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

	@Size(min=1, max=500, message="{validate.manager.range}")
  @Column(name="manager", length=500, nullable=true)
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Size(min=1, max=500, message="{validate.coach.range}")
  @Column(name="coach", length=500, nullable=true)
	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}
}