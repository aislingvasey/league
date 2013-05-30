package com.africaapps.league.service.team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamDao teamDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
	
	@WriteTransaction
	@Override
	public void saveTeam(Team team) throws LeagueException {
		team.setId(teamDao.getIdBySeasonandTeamId(team.getLeagueSeason().getId(), team.getTeamId()));
		logger.debug("Saving team:"+team);
		teamDao.saveOrUpdate(team);
		logger.debug("Saved team:"+team);
	}

	@ReadTransaction
	@Override
	public Team getTeam(long leagueSeasonId, int teamId) throws LeagueException {
		return teamDao.getBySeasonandTeamId(leagueSeasonId, teamId);
	}
}
