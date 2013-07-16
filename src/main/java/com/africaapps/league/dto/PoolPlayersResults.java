package com.africaapps.league.dto;

import java.util.List;

public class PoolPlayersResults extends BaseDto {

	private static final long serialVersionUID = 1L;

	private List<PoolPlayerSummary> poolPlayers;
	private int page;	
	private int pageSize;

	public boolean getLessThanAFullPage() {
		if (poolPlayers.size() < pageSize) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<PoolPlayerSummary> getPoolPlayers() {
		return poolPlayers;
	}

	public void setPoolPlayers(List<PoolPlayerSummary> poolPlayers) {
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
