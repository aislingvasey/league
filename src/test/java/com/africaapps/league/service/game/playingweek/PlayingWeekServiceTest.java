package com.africaapps.league.service.game.playingweek;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.model.game.User;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.game.UserTeamScoreHistory;
import com.africaapps.league.model.league.League;
import com.africaapps.league.service.game.team.UserTeamService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.user.UserService;

public class PlayingWeekServiceTest extends BaseSpringDbUnitTest {

	@Autowired
	private PlayingWeekService playingWeekService;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserTeamService userTeamService;

	@Test
	public void completeCurrentPlayingWeek() throws Exception {
		League league = leagueService.getLeague("ABSA Soccer League");
		assertNotNull(league);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_WEEK, -3);
		int endDay = cal.get(Calendar.DAY_OF_WEEK);

		User user = userService.getUser(Long.valueOf("-1"));
		assertNotNull(user);

		long userTeamId = -1;
		UserTeam userTeam = userTeamService.getTeam(userTeamId);
		assertEquals(100, userTeam.getCurrentScore());
		List<UserTeamScoreHistory> history = userTeamService.getScoreHistory(-1, -1);
		assertEquals(1, history.size());
		UserTeamScoreHistory h1 = history.get(0);
		assertEquals(userTeamId, h1.getUserTeam().getId(), 0);
		assertEquals(10, h1.getPlayerPoints(), 0);
		assertEquals(-1, h1.getPoolPlayer().getId(), 0);

		// Run the end of week
		playingWeekService.completeCurrentPlayingWeek(league, endDay);

		// Check that the user team has added a new substitute player
		userTeam = userTeamService.getTeam(userTeamId);
		assertEquals(120, userTeam.getCurrentScore());
		history = userTeamService.getScoreHistory(-1, -1);
		assertEquals(3, history.size());

		for (UserTeamScoreHistory h : history) {
			if (h.getId().equals(Long.valueOf("-1"))) {
				assertEquals(userTeamId, h.getUserTeam().getId(), 0);
				assertEquals(10, h.getPlayerPoints(), 0);
				assertEquals(-1, h.getPoolPlayer().getId(), 0);
			} else if (h.getPoolPlayer().getId().equals(Long.valueOf(-2))) {
				assertEquals(userTeamId, h.getUserTeam().getId(), 0);
				assertEquals(15, h.getPlayerPoints(), 0);
				assertEquals(-2, h.getPoolPlayer().getId(), 0);
			} else if (h.getPoolPlayer().getId().equals(Long.valueOf(-3))) {
				assertEquals(userTeamId, h.getUserTeam().getId(), 0);
				assertEquals(5, h.getPlayerPoints(), 0);
				assertEquals(-3, h.getPoolPlayer().getId(), 0);
			} else {
				fail(h.toString());
			}
		}
	}
}
