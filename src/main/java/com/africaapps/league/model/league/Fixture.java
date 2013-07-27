package com.africaapps.league.model.league;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="fixture")
public class Fixture extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private LeagueSeason leagueSeason;
	private Date startDate;
	private Date endDate;	
	private String description;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Fixture: ");
		builder.append(" id:").append(id);
		builder.append(" startDate:").append(startDate);
		builder.append(" endDate:").append(endDate);
		builder.append(" description:").append(description);
		builder.append(" leagueSeason:").append(leagueSeason);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Fixture)) {
			return false;
		} else {
			Fixture f = (Fixture) o;
			if (f.getId().equals(id)) {
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
  @SequenceGenerator(name="fixture_seq", sequenceName="fixture_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fixture_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name="league_season_id", nullable=false)
	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	@Column(name="start_datetime", nullable=false)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name="end_datetime", nullable=false)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="description", nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
