package com.africaapps.league.model.game;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_playing_week")
public class PlayingWeek extends BaseDataModel {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Date start;	
	private Date end;
	private Set<Match> matches;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PlayingWeek: ");
		builder.append(" id:").append(id);
		builder.append(" name:").append(name);
		builder.append(" start:").append(start);
		builder.append(" end:").append(end);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PlayingWeek)) {
			return false;
		} else {
			PlayingWeek w = (PlayingWeek) o;
			if (w.getId().equals(id)) {
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
  @SequenceGenerator(name="playing_week_seq", sequenceName="playing_week_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="playing_week_seq")
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

	@NotNull
  @Column(name="start_date_time", nullable=false)
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@NotNull
  @Column(name="end_date_time", nullable=false)
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@OneToMany(mappedBy="playingWeek")
	public Set<Match> getMatches() {
		return matches;
	}

	public void setMatches(Set<Match> matches) {
		this.matches = matches;
	}	
}
