package com.africaapps.league.model.game;

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
@Table(name="game_team_format")
public class TeamFormat extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private int defenderCount;
	private int midfielderCount;	
	private int strikerCount;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[TeamFormat: ");
		builder.append(" id:").append(id);
		builder.append(" name:").append(name);
		builder.append(" description:").append(description);
		builder.append(" defenderCount:").append(defenderCount);
		builder.append(" midfielderCount:").append(midfielderCount);
		builder.append(" strikerCount:").append(strikerCount);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof TeamFormat)) {
			return false;
		} else {
			TeamFormat f = (TeamFormat) o;
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
  @SequenceGenerator(name="team_format_seq", sequenceName="team_format_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="team_format_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@Size(min=1, max=100, message="{validate.name.range}")
  @Column(name="username", length=100, nullable=false, unique=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Size(min=1, max=250, message="{validate.description.range}")
  @Column(name="description", length=250, nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
  @Column(name="defender_count", nullable=false)
	public int getDefenderCount() {
		return defenderCount;
	}

	public void setDefenderCount(int defenderCount) {
		this.defenderCount = defenderCount;
	}

	@NotNull
  @Column(name="midfielder_count", nullable=false)
	public int getMidfielderCount() {
		return midfielderCount;
	}

	public void setMidfielderCount(int midfielderCount) {
		this.midfielderCount = midfielderCount;
	}

	@NotNull
  @Column(name="striker_count", nullable=false)
	public int getStrikerCount() {
		return strikerCount;
	}

	public void setStrikerCount(int strikerCount) {
		this.strikerCount = strikerCount;
	}
}
