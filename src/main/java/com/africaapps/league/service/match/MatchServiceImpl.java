package com.africaapps.league.service.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.dao.league.MatchDao;
import com.africaapps.league.dao.league.PlayerMatchDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.PlayerMatch;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchDao matchDao;
	@Autowired
	private PlayerMatchDao playerMatchDao;
	
	private static Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);
	
	@Transactional(readOnly=true)
	@Override
	public boolean isProcessedMatch(int matchId) throws LeagueException {
		Match match = matchDao.getByMatchId(matchId);
		if (match != null) {
			if (MatchProcessingStatus.COMPLETE.equals(match.getStatus())) {
				logger.info("Match previously processed: "+matchId);
				return true;
			} else {
				logger.info("Match not processed yet: "+matchId);
			}
		} else {
			logger.info("Unknown match: "+matchId);
		}
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void saveMatch(Match match) throws LeagueException {
		if (match != null) {
			Match existingMatch = matchDao.getByMatchId(match.getMatchId());
			if (existingMatch != null) {
				match.setId(existingMatch.getId());
			}
			logger.info("Saving match: "+match);
			matchDao.saveOrUpdate(match);
			logger.debug("Saved match: "+match);
		}
	}

	@Override
	public void savePlayerMatch(PlayerMatch playerMatch) throws LeagueException {
		if (playerMatch != null) {
			logger.debug("Saving playerMatch: "+playerMatch);
			playerMatchDao.saveOrUpdate(playerMatch);
			logger.debug("Saved playerMatch: "+playerMatch);
		}
	}
}