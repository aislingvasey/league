package com.africaapps.league.model.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="player")
public class Player extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String nickName;
	private Integer shirtNumber;
	private Position position;
	private Team team;
	
	@NotNull(groups={UpdateGroup.class})
	@Id
  @Column(name="id", nullable=false)
	@Override
	public Long getId() {
		return id;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.firstname.range}")
  @Column(name="first_name", length=200, nullable=false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.lastname.range}")
  @Column(name="last_name", length=200, nullable=false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.nickname.range}")
  @Column(name="nick_name", length=200, nullable=false)
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@NotNull
  @Column(name="shirt_number", nullable=false)
	public Integer getShirtNumber() {
		return shirtNumber;
	}

	public void setShirtNumber(Integer shirtNumber) {
		this.shirtNumber = shirtNumber;
	}

	@NotNull
	@ManyToOne(optional=false)
	@JoinColumn(name="position_id")
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@NotNull
	@ManyToOne(optional=false)
	@JoinColumn(name="team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
