package com.africaapps.league.model.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="statistic")
public class Statistic extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Integer externalStatsId;
	private String description;
	private Integer points;
	
	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="stats_seq", sequenceName="stats_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="stats_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Column(name="external_stats_id", nullable=false)
	public Integer getExternalStatsId() {
		return externalStatsId;
	}

	public void setExternalStatsId(Integer externalStatsId) {
		this.externalStatsId = externalStatsId;
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
