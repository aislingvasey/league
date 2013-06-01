package com.africaapps.league.model.league;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="player")
public class Player extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Integer playerId; //external id
	private String firstName;
	private String lastName;
	private String nickName;
	private Integer shirtNumber;
	private Position position;
	private BlockType block;
	private Team team;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Player: ");
		builder.append(" id:").append(id);
		builder.append(" playerId:").append(playerId);
		builder.append(" firstName:").append(firstName);
		builder.append(" lastName:").append(lastName);
		builder.append(" nickName:").append(nickName);
		builder.append(" position:").append(position);
		builder.append(" block:").append(block);
		builder.append(" team:").append(team == null ? "" : team.getClubName());
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Player)) {
			return false;
		} else {
			Player p = (Player) o;
			if (p.getId().equals(id)) {
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
	@SequenceGenerator(name="player_seq", sequenceName="player_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Index(name="player_id_index", columnNames = "player_id")
  @Column(name="player_id", nullable=false)
	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.firstname.range}")
  @Column(name="first_name", length=200, nullable=true)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.lastname.range}")
  @Column(name="last_name", length=200, nullable=true)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotNull
	@Size(min=1, max=200, message="{validate.nickname.range}")
  @Column(name="nick_name", length=200, nullable=true)
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

	@ManyToOne
	@JoinColumn(name="position_id", nullable=true)
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Column(name="block", nullable=true)
	@Enumerated(EnumType.STRING)
	public BlockType getBlock() {
		return block;
	}

	public void setBlock(BlockType block) {
		this.block = block;
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
