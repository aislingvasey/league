package com.africaapps.league.service.feed;

import java.util.ArrayList;
import java.util.List;

import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;

@Service
public class FeedParsingServiceImpl implements FeedParsingService {

	public List<Match> parseMatchFilActionStruct(List<MatchFilActionStruct> structs) throws LeagueException {
		List<Match> matches = new ArrayList<Match>();
		//TODO check match has not already been successfully processed previously
		//TODO convert struct to match  
		return matches;
	}
}
