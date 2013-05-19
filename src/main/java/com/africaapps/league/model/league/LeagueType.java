package com.africaapps.league.model.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="league_type")
public class LeagueType extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private LeagueTypeStatus status;
	
	public LeagueType() {
		this.name = "";
		this.description = "";
		this.status = LeagueTypeStatus.ACTIVE;
	}
	
	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="league_type_seq", sequenceName="league_type_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="league_type_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@Size(min=1, max=20, message="{validate.name.range}")
  @Column(name="name", length=20, nullable=false)
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
	
	@NotNull
	@Column(name="league_type_status", nullable=false)
	@Enumerated(EnumType.STRING) 
	public LeagueTypeStatus getStatus() {
		return status;
	}
	
	public void setStatus(LeagueTypeStatus status) {
		this.status = status;
	}
}
