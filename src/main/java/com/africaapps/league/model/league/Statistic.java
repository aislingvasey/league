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

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="statistic", uniqueConstraints={@UniqueConstraint(columnNames={"league_type_id", "stats_id"})})
public class Statistic extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private LeagueType leagueType;
	private Long statsId;
	private String description;
	private Integer points;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Statistic: ");
		builder.append(" id:").append(id);
		builder.append(" statsId:").append(statsId);
		builder.append(" description:").append(description);
		builder.append(" points:").append(points);
		builder.append(" type:").append(leagueType);		
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Statistic)) {
			return false;
		} else {
			Statistic s = (Statistic) o;
			if (s.getId().equals(id)) {
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
	@SequenceGenerator(name="statistic_seq", sequenceName="statistic_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="statistic_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
  @JoinColumn(name = "league_type_id")
	public LeagueType getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(LeagueType leagueType) {
		this.leagueType = leagueType;
	}

	@NotNull
	@Column(name="stats_id", nullable=false)
	public Long getStatsId() {
		return statsId;
	}

	public void setStatsId(Long statsId) {
		this.statsId = statsId;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.description.range}")
	@Column(name="description", length=200, nullable=false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Column(name="points", nullable=false)
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
