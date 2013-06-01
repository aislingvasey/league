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
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="player_match_event")
public class PlayerMatchEvent extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private PlayerMatch playerMatch;
	private Event event;
	private String matchTime;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PlayerMatchEvent: ");
		builder.append(" playerMatch:").append(playerMatch);
		builder.append(" event:").append(event);
		builder.append(" matchTime:").append(matchTime);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PlayerMatchEvent)) {
			return false;
		} else {
			PlayerMatchEvent s = (PlayerMatchEvent) o;
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
  @SequenceGenerator(name="player_match_stats_seq", sequenceName="player_match_stats_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_match_stats_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name="player_match_id")
	public PlayerMatch getPlayerMatch() {
		return playerMatch;
	}

	public void setPlayerMatch(PlayerMatch playerMatch) {
		this.playerMatch = playerMatch;
	}

	@ManyToOne
	@JoinColumn(name="event_id")
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@NotNull
	@Column(name="match_time", nullable=false)
	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
}
