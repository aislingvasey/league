package com.africaapps.league.service.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.MatchDao;
import com.africaapps.league.dao.league.PlayerMatchDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchDao matchDao;
	@Autowired
	private PlayerMatchDao playerMatchDao;
	
	private static Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);
	
	@ReadTransaction
	@Override
	public boolean isProcessedMatch(long leageaSeasonId, int matchId) throws LeagueException {
		Match match = matchDao.getByLeagueSeasonAndMatchId(leageaSeasonId, matchId);
		if (match != null) {
			//TODO change this later
			if (MatchProcessingStatus./*COMPLETE*/SAVED.equals(match.getStatus())) {
				return true;
			}
		}
		return false;
	}

	@WriteTransaction
	@Override
	public void saveMatch(Match match) throws LeagueException {
		if (match != null) {
			match.setId(matchDao.getIdByMatchId(match.getLeagueSeason().getId(), match.getMatchId()));
			logger.info("Saving match: "+match);
			matchDao.saveOrUpdate(match);
			logger.debug("Saved match: "+match);
		}
	}

	@WriteTransaction
	@Override
	public void savePlayerMatch(PlayerMatch playerMatch) throws LeagueException {
		if (playerMatch != null) {
			playerMatch.setId(playerMatchDao.getIdByIds(playerMatch.getMatch().getId(), playerMatch.getPlayer().getId()));
			logger.debug("Saving playerMatch: "+playerMatch);			
			playerMatchDao.saveOrUpdate(playerMatch);
			logger.debug("Saved playerMatch: "+playerMatch);
		}
	}

	@WriteTransaction
	@Override
	public void calculatePlayerScores(Match match) throws LeagueException {
		if (match != null) {
			logger.info("Calculating players scores...");
			matchDao.calculatePlayerScores(match.getId());
		}
	}
}