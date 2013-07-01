package com.africaapps.league.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_player_price")
public class PlayerPrice extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;	
	private Integer price;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PlayerPrice: ");
		builder.append(" id:").append(id);
		builder.append(" firstName").append(firstName);
		builder.append(" lastName:").append(lastName);
		builder.append(" price:").append(price);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PlayerPrice)) {
			return false;
		} else {
			PlayerPrice p = (PlayerPrice) o;
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
  @SequenceGenerator(name="player_price_seq", sequenceName="player_price_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="player_price_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
}
