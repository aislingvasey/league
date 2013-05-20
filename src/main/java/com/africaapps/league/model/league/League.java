package com.africaapps.league.model.league;

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
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="league")
public class League extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private LeagueType leagueType;
	private Set<LeagueSeason> leagueSeasons;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[League: ");
		builder.append(" id:").append(id);
		builder.append(" name:").append(name);
		builder.append(" description:").append(description);
		builder.append(" type:").append(leagueType);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof League)) {
			return false;
		} else {
			League l = (League) o;
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
  @SequenceGenerator(name="league_seq", sequenceName="league_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="league_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@Size(min=1, max=100, message="{validate.name.range}")
  @Column(name="name", length=100, nullable=false, unique=true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Size(min=1, max=256, message="{validate.description.range}")
  @Column(name="description", length=256, nullable=false)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
  @JoinColumn(name = "league_type_id")
	public LeagueType getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(LeagueType leagueType) {
		this.leagueType = leagueType;
	}

	@OneToMany(mappedBy = "league")
	public Set<LeagueSeason> getLeagueSeasons() {
		return leagueSeasons;
	}

	public void setLeagueSeasons(Set<LeagueSeason> leagueSeasons) {
		this.leagueSeasons = leagueSeasons;
	}
}
