package com.africaapps.league.dao.game.hibernate;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PlayingWeekDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PlayingWeek;

@Repository
public class PlayingWeekDaoImpl extends BaseHibernateDao implements PlayingWeekDao {

	@Override
	public PlayingWeek get(Long leagueSeasonId, Date matchDateTime) {
		// TODO
		return null;
	}
}
