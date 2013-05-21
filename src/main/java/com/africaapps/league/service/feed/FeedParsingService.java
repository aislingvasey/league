package com.africaapps.league.service.feed;

import java.util.List;

import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;

public interface FeedParsingService {

	public List<Match> parseMatchFilActionStruct(List<MatchFilActionStruct> structs) throws LeagueException;
	
}
