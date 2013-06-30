package com.africaapps.league.dao.game.hibernate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

public class PlayingWeekDaoTest {
	
	private static final String PREFIX = "INSERT INTO game_playing_week(id, start_date_time, end_date_time, league_season_id)\n VALUES ((select nextval('pool_seq')), '";
  private static final String SUFFIX = "', (select id from league_season where status = 'CURRENT' and league_id = (select id from league where name = 'ABSA Premier Soccer League')));";	

	@Test
	public void generatePlayingWeeks() {
		//-- Saturday to Friday
//		select min(start_date_time) from match  --8/10/2012 8:00:27 PM
//		select max(start_date_time) from match --5/18/2013 3:00:54 PM
		
		//1999-01-08 04:05:06.789
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		
		Calendar start = Calendar.getInstance();
		start.set(Calendar.YEAR, 2012);
		start.set(Calendar.MONTH, 7);
		start.set(Calendar.DAY_OF_MONTH, 4);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2012);
		end.set(Calendar.MONTH, 7);
		end.set(Calendar.DAY_OF_MONTH, 10);
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 999);
		
		for(int i=0;i<42;i++) {
			System.out.println(PREFIX + sdf.format(start.getTime()) +"', '"+sdf.format(end.getTime())+SUFFIX);
			start.add(Calendar.WEEK_OF_YEAR, 1);
			end.add(Calendar.WEEK_OF_YEAR, 1);
		}
	}
	
}
