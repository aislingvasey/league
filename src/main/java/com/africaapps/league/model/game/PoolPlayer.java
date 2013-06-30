package com.africaapps.league.model.game;

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
import com.africaapps.league.model.league.Player;
import com.africaapps.league.validation.UpdateGroup;

@Entity
@Table(name="game_pool_player")
public class PoolPlayer extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private Player player; 
	private Pool pool;
	private long playerPrice;	
	private int playerCurrentScore;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[PoolPlayer: ");
		builder.append(" id:").append(id);
		builder.append(" player:").append(player);
		builder.append(" pool:").append(pool);
		builder.append(" playerPrice:").append(playerPrice);
		builder.append(" playerCurrentScore:").append(playerCurrentScore);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof PoolPlayer)) {
			return false;
		} else {
			PoolPlayer p = (PoolPlayer) o;
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
  @SequenceGenerator(name="pool_player_seq", sequenceName="pool_player_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pool_player_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="pool_id", nullable=false)
	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name="player_id", nullable=false)
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@NotNull
	@Column(name="player_price", nullable=false)
	public long getPlayerPrice() {
		return playerPrice;
	}

	public void setPlayerPrice(long playerPrice) {
		this.playerPrice = playerPrice;
	}

	@NotNull
	@Column(name="player_current_score", nullable=false)
	public int getPlayerCurrentScore() {
		return playerCurrentScore;
	}

	public void setPlayerCurrentScore(int playerCurrentScore) {
		this.playerCurrentScore = playerCurrentScore;
	}
}
