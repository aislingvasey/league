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
	public TeamFormat getDefaultFormat(long leagueTypeId) throws LeagueException {
		return teamFormatDao.getDefault(leagueTypeId);
	}

	@ReadTransaction
	@Override
	public List<TeamFormat> getTeamFormats(long leagueTypeId) throws LeagueException {
		return teamFormatDao.getAll(leagueTypeId);
	}

	@Override
	public TeamFormat getTeamFormat(Long teamFormatId) throws LeagueException {
		if (teamFormatId != null) {
			return teamFormatDao.get(teamFormatId);
		} else {
			return null;
		}
	}
}
