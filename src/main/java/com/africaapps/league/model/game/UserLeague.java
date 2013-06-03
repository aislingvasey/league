package com.africaapps.league.model.game;

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

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_user_league")
public class UserLeague extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private UserLeagueStatus status;	
	private UserLeagueType type;
	private User owner;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[UserLeague: ");
		builder.append(" id:").append(id);
		builder.append(" name:").append(name);
		builder.append(" description:").append(description);
		builder.append(" status:").append(status);
		builder.append(" type:").append(type);
		builder.append(" owner:").append(owner);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof UserLeague)) {
			return false;
		} else {
			UserLeague u = (UserLeague) o;
			if (u.getId().equals(id)) {
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
  @SequenceGenerator(name="user_league_seq", sequenceName="user_league_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_league_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@Size(min=1, max=100, message="{validate.name.range}")
  @Column(name="name", length=100, nullable=false)
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

  @Column(name="status", length=100, nullable=false)
  @Enumerated(EnumType.STRING)
	public UserLeagueStatus getStatus() {
		return status;
	}

	public void setStatus(UserLeagueStatus status) {
		this.status = status;
	}

	@Column(name="type", length=100, nullable=false)
  @Enumerated(EnumType.STRING)
	public UserLeagueType getType() {
		return type;
	}

	public void setType(UserLeagueType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name="owner_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
}
