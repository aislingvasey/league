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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="event", uniqueConstraints={@UniqueConstraint(columnNames={"league_type_id", "event_id", "block_type"})})
public class Event extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private LeagueType leagueType;
	private Integer eventId; //external id
	private String description;
	private Integer points;
	private BlockType block;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Event: ");
		builder.append(" id:").append(id);
		builder.append(" eventId:").append(eventId);
		builder.append(" description:").append(description);
		builder.append(" points:").append(points);
		builder.append(" type:").append(leagueType);		
		builder.append(" block:").append(block);		
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Event)) {
			return false;
		} else {
			Event s = (Event) o;
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
	@SequenceGenerator(name="event_seq", sequenceName="event_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="event_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
  @JoinColumn(name = "league_type_id")
	public LeagueType getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(LeagueType leagueType) {
		this.leagueType = leagueType;
	}

	@NotNull
	@Index(name="event_id_index", columnNames = "event_id")
	@Column(name="event_id", nullable=false)
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
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

	@Column(name="block_type", nullable=true)
	@Enumerated(EnumType.STRING)
	public BlockType getBlock() {
		return block;
	}

	public void setBlock(BlockType block) {
		this.block = block;
	}
}
