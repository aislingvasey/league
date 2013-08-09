package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Statistic;

@Repository
public class StatisticDaoImpl extends BaseHibernateDao implements StatisticDao {

	private static Logger logger = LoggerFactory.getLogger(StatisticDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Statistic> get(long leagueTypeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Statistic.class); 
		criteria.createAlias("leagueType", "type").add(Restrictions.eq("type.id", leagueTypeId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Statistic get(long leagueTypeId, int externalId, BlockType block) {
		logger.info("get for externalId: "+externalId+" leagueTypeId: "+leagueTypeId+" block:"+block);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Statistic.class);
		criteria.add(Restrictions.eq("externalId", externalId));
		criteria.createAlias("leagueType", "t").add(Restrictions.eq("t.id", leagueTypeId));
		if (block != null) {
			criteria.add(Restrictions.eq("block", block));
		} else {
			criteria.add(Restrictions.isNull("block"));
		}
		List<Statistic> e = criteria.list();
		logger.info("Got stats: "+e.size());
		if (e.size() == 1) {
			return e.get(0);
		}
		return null;
	}

}
