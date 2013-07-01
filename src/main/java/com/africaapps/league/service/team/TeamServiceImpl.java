package com.africaapps.league.service.team;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.game.team.UserTeamService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamDao teamDao;
	@Autowired
	private UserTeamService userTeamService;
	@Autowired
	private LeagueService leagueService;
	
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

	@ReadTransaction
	@Override
	public List<TeamSummary> getTeams(long userTeamId) throws LeagueException {
		List<TeamSummary> summaries = new ArrayList<TeamSummary>();
		UserTeam userTeam = userTeamService.getTeam(userTeamId);
		if (userTeam != null) {			
			LeagueSeason season = leagueService.getCurrentSeason(userTeam.getUserLeague().getLeague());
			if (season != null) {
				List<Team> teams = teamDao.getBySeasonId(season.getId());
				for(Team team : teams) {
					summaries.add(new TeamSummary(team.getId(), team.getClubName()));
				}
			} else {
				logger.error("No default leagueSeason found for userTeamId: "+userTeamId);
			}
		} else {
			logger.error("No UserTeam found for userteamId: "+userTeamId);
		}
		return summaries;
	}

	@Override
	public String getTeamName(long teamId) throws LeagueException {
		return teamDao.getName(teamId);
	}
}