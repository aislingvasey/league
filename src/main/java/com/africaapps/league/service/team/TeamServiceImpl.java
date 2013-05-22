package com.africaapps.league.service.team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Team;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamDao teamDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
	
	@Transactional(readOnly=false, rollbackFor=LeagueException.class)
	@Override
	public void saveTeam(Team team) throws LeagueException {
		logger.debug("Saving team:"+team);
		teamDao.saveOrUpdate(team);
		logger.debug("Saved team:"+team);
	}
}
