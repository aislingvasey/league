package com.africaapps.league.dto;

import java.util.List;

import com.africaapps.league.model.game.PoolPlayer;

public class PoolPlayersResults extends BaseDto {

	private static final long serialVersionUID = 1L;

	private List<PoolPlayer> poolPlayers;
	private int page;	
	private int pageSize;

	public List<PoolPlayer> getPoolPlayers() {
		return poolPlayers;
	}

	public void setPoolPlayers(List<PoolPlayer> poolPlayers) {
		this.poolPlayers = poolPlayers;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
