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

import org.hibernate.annotations.Index;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="statistic", uniqueConstraints={@UniqueConstraint(columnNames={"league_type_id", "external_id", "block_type"})})
public class Statistic extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private LeagueType leagueType;
	private Integer externalId;
	private String name;
	private Double points;
	private BlockType block;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Statistic: ");
		builder.append(" id:").append(id);
		builder.append(" externalId:").append(externalId);
		builder.append(" name:").append(name);
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
		} else if (!(o instanceof Statistic)) {
			return false;
		} else {
			Statistic s = (Statistic) o;
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
	@SequenceGenerator(name="stat_seq", sequenceName="stat_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="stat_seq")
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
	@Index(name="external_stat_id_index", columnNames = "external_id")
	@Column(name="external_id", nullable=false)
	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	@NotNull
	@Column(name="name", length=200, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(name="points", nullable=false)
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	@Column(name="block_type", nullable=false)
	@Enumerated(EnumType.STRING)
	public BlockType getBlock() {
		return block;
	}

	public void setBlock(BlockType block) {
		this.block = block;
	}
}
