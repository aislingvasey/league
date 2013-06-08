package com.africaapps.league.service.game.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.TeamFormatDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class TeamFormatServiceImpl implements TeamFormatService {

	@Autowired
	private TeamFormatDao teamFormatDao;
	
	@ReadTransaction
	@Override
	public TeamFormat getDefaultFormat() throws LeagueException {
		return teamFormatDao.getDefault();
	}

	@ReadTransaction
	@Override
	public List<TeamFormat> getTeamFormats() throws LeagueException {
		return teamFormatDao.getAll();
	}
}
