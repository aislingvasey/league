package com.africaapps.league.model.game;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "game_user_team")
public class UserTeam extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private int currentScore;
	private long availableMoney;
	private TeamFormat currentFormat;
	private User user;
	private UserLeague userLeague;
	private Set<UserPlayer> userPlayers;
	private UserTeamStatus status;
	
	public UserTeam() {
		this.status = UserTeamStatus.INCOMPLETE;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[UserTeam: ");
		builder.append(" id:").append(id);
		builder.append(" name:").append(name);
		builder.append(" currentScore:").append(currentScore);
		builder.append(" availableMoney:").append(availableMoney);
		builder.append(" currentFormat:").append(currentFormat);
		builder.append(" user:").append(user);
		builder.append(" userLeague:").append(userLeague);
		builder.append(" status:").append(status);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof UserTeam)) {
			return false;
		} else {
			UserTeam u = (UserTeam) o;
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

	@NotNull(groups = { UpdateGroup.class })
	@Id
	@SequenceGenerator(name = "user_team_seq", sequenceName = "user_team_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_team_seq")
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Size(min = 1, max = 100, message = "{validate.name.range}")
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(name = "current_score", nullable = false)
	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	@NotNull
	@Column(name = "available_money", nullable = false)
	public long getAvailableMoney() {
		return availableMoney;
	}

	public void setAvailableMoney(long availableMoney) {
		this.availableMoney = availableMoney;
	}

	@ManyToOne
	@JoinColumn(name = "team_format_id")
	public TeamFormat getCurrentFormat() {
		return currentFormat;
	}

	public void setCurrentFormat(TeamFormat currentFormat) {
		this.currentFormat = currentFormat;
	}

	@ManyToOne
	@JoinColumn(name = "user_details_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name="user_league_id", nullable=false)
	public UserLeague getUserLeague() {
		return userLeague;
	}

	public void setUserLeague(UserLeague userLeague) {
		this.userLeague = userLeague;
	}

	@OneToMany(mappedBy="userTeam")
	public Set<UserPlayer> getUserPlayers() {
		return userPlayers;
	}

	public void setUserPlayers(Set<UserPlayer> userPlayers) {
		this.userPlayers = userPlayers;
	}

	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	public UserTeamStatus getStatus() {
		return status;
	}

	public void setStatus(UserTeamStatus status) {
		this.status = status;
	}
}
