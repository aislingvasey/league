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

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="position", uniqueConstraints = {@UniqueConstraint(columnNames={"position_number", "league_type_id"})})
public class Position extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private int positionNumber;
	private PositionType positionType;
	private LeagueType leagueType;
	
	@Override
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="position_seq", sequenceName="position_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="position_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	@NotNull
  @Column(name="position_number", nullable=false)
	public int getPositionNumber() {
		return positionNumber;
	}

	public void setPositionNumber(int positionNumber) {
		this.positionNumber = positionNumber;
	}

	@NotNull
	@Column(name="position_type", nullable=false)
	@Enumerated(EnumType.STRING)
	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}

	@ManyToOne
	@JoinColumn(name = "league_type_id")
	public LeagueType getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(LeagueType leagueType) {
		this.leagueType = leagueType;
	}
}
