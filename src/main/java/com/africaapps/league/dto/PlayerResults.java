package com.africaapps.league.dto;

import java.util.ArrayList;
import java.util.List;

public class PlayerResults extends BaseDto {

	private static final long serialVersionUID = 1L;

	private List<PlayerSummary> players;
	private int pagesCount;
	private int page;	
	private int pageSize;
	
	public PlayerResults() {
		this.pagesCount = -1;
		this.players = new ArrayList<PlayerSummary>();
	}

	public boolean getLessThanAFullPage() {
		if (players.size() < pageSize) {
			return true;
		} else {
			return false;
		}
	}

	public List<PlayerSummary> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerSummary> players) {
		this.players = players;
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

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}	
}
