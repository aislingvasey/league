package com.africaapps.league.service.webservice.util;

import org.datacontract.schemas._2004._07.livemediastructs.ActorStruct;
import org.datacontract.schemas._2004._07.livemediastructs.ActorSubstitutionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.CIS;
import org.datacontract.schemas._2004._07.livemediastructs.DayRankingStruct;
import org.datacontract.schemas._2004._07.livemediastructs.DayScoringStruct;
import org.datacontract.schemas._2004._07.livemediastructs.DayTeamScoringStruct;
import org.datacontract.schemas._2004._07.livemediastructs.EventMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchDayScoringStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchStruct;
import org.datacontract.schemas._2004._07.livemediastructs.SDTS;
import org.datacontract.schemas._2004._07.livemediastructs.SOS;
import org.datacontract.schemas._2004._07.livemediastructs.SS;
import org.datacontract.schemas._2004._07.livemediastructs.ScorerStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamDayRankingStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLogUtil {

	private static Logger logger = LoggerFactory.getLogger(DataLogUtil.class);

	public static void logMatchFilStruct(MatchFilActionStruct matchStruct) {
		StringBuilder sb = new StringBuilder();		
		sb.append("[MatchFilActionStruct: ");		
		sb.append(" codeMatch:").append(matchStruct.getCodeMatch().getValue());
		sb.append(" competitionName:").append(matchStruct.getCompetitionName().getValue());
		sb.append(" dateTime:").append(matchStruct.getDateAndTime());
		sb.append(" day:").append(matchStruct.getDay().getValue());
		sb.append(" idMatch:").append(matchStruct.getIdMatch());
		
		for(TeamMatchFilActionStruct teamMatch : matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct()) {
			logTeamMatchFilActionStruct(sb, teamMatch);
		}
		
		for(EventMatchFilActionStruct eventMatch : matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct()) {
			logEventMatchFilActionStruct(sb, eventMatch);
		}
		
		sb.append("]");		
		logger.info(sb.toString());
	}
	
	private static void logTeamMatchFilActionStruct(StringBuilder sb, TeamMatchFilActionStruct teamMatch) {
		sb.append("\n\t[TeamMatchFilActionStruct:");
		sb.append(" idTeam:").append(teamMatch.getIdTeam());
		sb.append(" codeName:").append(teamMatch.getCodeName().getValue());
		sb.append(" teamName:").append(teamMatch.getTeamName().getValue());
		sb.append("]");
	}
	
	private static void logEventMatchFilActionStruct(StringBuilder sb, EventMatchFilActionStruct eventMatch) {
		sb.append("\n\t[EventMatchFilActionStruct:");
		sb.append(" idEvent:").append(eventMatch.getIdEvent());
		sb.append(" halfId:").append(eventMatch.getHalfId().getValue());
		sb.append(" halfTimeSubstition:").append(eventMatch.getIsHalfTimeSubstitution().getValue());
		sb.append(" phaseStartTime:").append(eventMatch.getPhaseStartTimeMs().getValue());
		sb.append(" scoreA:").append(eventMatch.getScoreA().getValue());
		sb.append(" scoreB:").append(eventMatch.getScoreB().getValue());
		sb.append(" timeMatch:").append(eventMatch.getTimeMatchStr().getValue());
		if (eventMatch.getTeam1() != null && eventMatch.getTeam1().getValue() != null) {
			sb.append(" team1:").append(eventMatch.getTeam1().getValue().getTeamName().getValue());
		}
		if (eventMatch.getTeam2() != null && eventMatch.getTeam2().getValue() != null) {
			sb.append(" team2:").append(eventMatch.getTeam2().getValue().getTeamName().getValue());		
		}
		if (eventMatch.getActor1().getValue() != null) {
			sb.append(" Player1: idActor:").append(eventMatch.getActor1().getValue().getIdActor())
			   .append(" nickName:").append(eventMatch.getActor1().getValue().getNickName().getValue());
		}
		if (eventMatch.getActor2().getValue() != null) {
			sb.append(" Player2: idActor:").append(eventMatch.getActor1().getValue().getIdActor())
			   .append(" nickName:").append(eventMatch.getActor1().getValue().getNickName().getValue());
		}
		sb.append("]");
	}
	
	public static void logMatchLightStruct(MatchLightStruct matchLightStruct) {
		StringBuilder sb = new StringBuilder();
		sb.append("[MatchLightStruct: ");
		sb.append(" idMatch:").append(matchLightStruct.getIdMatch());
		sb.append(" codeMatch:").append(matchLightStruct.getCodeMatch().getValue());
		sb.append(" competitionName:").append(matchLightStruct.getCompetitionName().getValue());
		sb.append(" dateTime:").append(matchLightStruct.getDateAndTime());
		sb.append(" idStatus:").append(matchLightStruct.getIdStatus().getValue());
		sb.append(" lstCIS:").append(matchLightStruct.getLstCIS());
		sb.append(" lastTeamLightStruct:").append(matchLightStruct.getLstTeamLightStruct());
		sb.append(" seasonEnd:").append(matchLightStruct.getSeasonEndDate());
		sb.append(" seasonStart:").append(matchLightStruct.getSeasonStartDate());
		sb.append(" teamA: ");
		logTeamLightStruct(sb, matchLightStruct.getTeamLightStructA().getValue());
		sb.append(" teamB: ");
		logTeamLightStruct(sb, matchLightStruct.getTeamLightStructB().getValue());
		logger.info(sb.toString());
	}
	
	public static void logMatchStruct(MatchStruct matchStruct) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[MatchStruct: ");		
		sb.append(" codeMatch:").append(matchStruct.getCodeMatch().getValue());
		sb.append(" competitionName:").append(matchStruct.getCompetitionName().getValue());
		sb.append(" currentHalfTime:").append(matchStruct.getCurrentHalf().getValue());
		sb.append(" dateTime:").append(matchStruct.getDateAndTime());
		sb.append(" day:").append(matchStruct.getDay().getValue());
		sb.append(" idCertificationState:").append(matchStruct.getIdCertificationState().getValue());
		sb.append(" idMatch:").append(matchStruct.getIdMatch());
		sb.append(" isStatus:").append(matchStruct.getIdStatus().getValue());
		sb.append(" source:").append(matchStruct.getSource().getValue());
		sb.append(" stadium:").append(matchStruct.getStadiumName().getValue());
		
		//TODO
//		sb.append(" ").append(matchStruct.getLstCIS());
//		sb.append(" ").append(matchStruct.getLstReferees());
//		sb.append(" ").append(matchStruct.getLstSDTS());
		
//		for(TeamStruct team : matchStruct.getLstTeamStruct().getValue().getTeamStruct()) {
//			logTeamStruct(sb, team);
//		}
		
		sb.append("]");
		logger.info(sb.toString());
	}
	
	private static void logTeamLightStruct(StringBuilder sb, TeamLightStruct team) {
		sb.append("\n [TeamLightStruct:");
		sb.append("").append(team.getAbbrevClubName().getValue());
		sb.append("").append(team.getCity().getValue());
		sb.append("").append(team.getClubName().getValue());
		sb.append("").append(team.getCodeName().getValue());
		sb.append("").append(team.getIdTeam());
		sb.append("").append(team.getMediaName().getValue());
		sb.append("").append(team.getTeamName().getValue());
		for(CIS cis: team.getLstCIS().getValue().getCIS()) {
			sb.append(" CIS: idClient:").append(cis.getIdClient().getValue()).append(" name:").append(cis.getName().getValue());
		}
		sb.append("]");
	}
	
	public static void logTeamStruct(StringBuilder sb, TeamStruct team) {
		sb.append("\n[TeamStruct:");
		sb.append(" abbrevClubName:").append(team.getAbbrevClubName().getValue());
		sb.append(" city:").append(team.getCity().getValue());
		sb.append(" clubName:").append(team.getClubName().getValue());
		sb.append(" codeName:").append(team.getCodeName().getValue());
		sb.append(" idTeam:").append(team.getIdTeam());
		sb.append(" mediaName:").append(team.getMediaName().getValue());
		sb.append(" teamName:").append(team.getTeamName().getValue());
		sb.append(" coach: idA:").append(team.getCoach().getValue().getIdA()).append(" FN:").append(team.getCoach().getValue().getFN().getValue());
		sb.append(" colorHexa:").append(team.getColorHexa().getValue());
		sb.append(" scoreEndOfficialTime:").append(team.getScoreEndOfficialTime().getValue());
		sb.append(" scoreFinalShot:").append(team.getScoreFinalShot().getValue());
		sb.append(" scoreHalfTime:").append(team.getScoreHafTime().getValue());
		sb.append(" scoreOverTime1:").append(team.getScoreOverTime1().getValue());
		sb.append(" scoreOverTime2:").append(team.getScoreOverTime2().getValue());
		
		//Players
		for(ActorStruct actor : team.getLstActorStruct().getValue().getActorStruct()) {
			logActor(sb, actor);
		}
		
		//Substitutions
//		for(ActorSubstitutionStruct sub : team.getLstActorSubstitutionStruct().getValue().getActorSubstitutionStruct()) {
//			sb.append("\n[Substitution:");
//			sb.append(" time:").append(sub.getTimeMatchStr().getValue());
//			sb.append(" halfId:").append(sub.getHalfId());
//			sb.append(" playerIn:").append(sub.getPlayerIn().getValue().getIdA());
//			sb.append(" playerOut:").append(sub.getPlayerOut().getValue().getIdA());
//			sb.append("]");
//		}		
		
		//Scorers
		for(ScorerStruct scorer : team.getLstScorerStruct().getValue().getScorerStruct()) {
			sb.append("\n[Scorer:");
			sb.append(" halfId:").append(scorer.getHalfId());
			sb.append(" idGoalType:").append(scorer.getIdGoalType());
			sb.append(" idA:").append(scorer.getPlayerScorer().getValue().getIdA());
			sb.append(" scoreA:").append(scorer.getScoreA().getValue());
			sb.append(" scoreB:").append(scorer.getScoreB().getValue());
			sb.append(" matchTime:").append(scorer.getTimeMatchStr().getValue());
			sb.append("]");
		}
		
		//CIS
//		for(CIS cis: team.getLstCIS().getValue().getCIS()) {
//			sb.append("\nCIS: idClient:").append(cis.getIdClient().getValue()).append(" name:").append(cis.getName().getValue());
//		}
		
		//STDS
//		for (SDTS sdts : team.getLstSDTS().getValue().getSDTS()) {
//			sb.append("\n[SDTS:");
//			sb.append(" idDT:").append(sdts.getIdDT());
//			sb.append(" name:").append(sdts.getName().getValue());
//			for(SS ss : sdts.getLstSS().getValue().getSS()) {
//				sb.append(" SS: id:").append(ss.getId()).append(" V:").append(ss.getV().getValue());
//				if (ss.getA2().getValue() != null) {
//					sb.append(" idA:").append(ss.getA2().getValue().getIdA());
//					sb.append(" FN:").append(ss.getA2().getValue().getFN().getValue());
//					sb.append(" NN:").append(ss.getA2().getValue().getNN().getValue());
//					sb.append(" SN:").append(ss.getA2().getValue().getSN().getValue());
//					sb.append(" UFN:").append(ss.getA2().getValue().getUFN().getValue());
//					for(CIS cis : ss.getA2().getValue().getLstCIS().getValue().getCIS()) {
//						sb.append(" CIS idClient:").append(cis.getIdClient().getValue());
//						sb.append(" name:").append(cis.getName().getValue());
//					}
//				}
//				for(SOS sos : ss.getLSOS().getValue().getSOS()) {
//					sb.append(" SOS:");
//					if (ss.getA2().getValue() != null) {
//						sb.append(" SOS's A2:").append(ss.getA2().getValue().getIdA());						
//					}
//					sb.append(" ETH:").append(sos.getETHstr().getValue());
//					sb.append(" ETM:").append(sos.getETMstr().getValue());
//					sb.append(" STH:").append(sos.getSTHstr().getValue());
//					sb.append(" STM:").append(sos.getSTMstr().getValue());
//				}
//			}
//			sb.append("]");
//		}
		
		sb.append("\n]");
	}

	public static void logActor(StringBuilder sb, ActorStruct actor) {
		sb.append("\n[Actor:");
		sb.append(" idActor:").append(actor.getIdActor());
		sb.append(" name:").append(actor.getFirstName().getValue()).append(" ").append(actor.getSecondName().getValue());			
		sb.append(" nickName:").append(actor.getNickName().getValue());
		sb.append(" shirtNumber:").append(actor.getShirtNumber().getValue());
		sb.append(" idBlock:").append(actor.getIdBlock().getValue());
		sb.append(" idPosition:").append(actor.getIdPosition().getValue());
//		for(SDTS sdts : actor.getLstSDTS().getValue().getSDTS()) {
//			sb.append("\n\t SDTS: idDT:").append(sdts.getIdDT())
//			.append(" name:").append(sdts.getName().getValue());
//			for(SS ss : sdts.getLstSS().getValue().getSS()) {
//				sb.append(" SS: id:").append(ss.getId());
//				if (ss.getLSOS() != null) {
//					for(SOS sos : ss.getLSOS().getValue().getSOS()) {
//						sb.append("\n\t\t SOS: ETH:").append(sos.getETHstr().getValue())
//						  .append(" ETM:").append(sos.getETMstr().getValue())
//						  .append(" STH:").append(sos.getSTHstr().getValue())
//						  .append(" STM:").append(sos.getSTMstr().getValue());							
//					}
//				}
//				sb.append(" idA:");
//				if (ss.getA2() != null && ss.getA2().getValue() != null) {
//					sb.append(ss.getA2().getValue().getIdA());
//				}
//			}
//		}
		sb.append("]");
	}
	
	public static void logDayRankingStruct(DayRankingStruct dayRankingStruct) {
		StringBuilder sb = new StringBuilder();
		sb.append("[DayRankingStruct:");
		sb.append(" competition:").append(
				dayRankingStruct.getCompetitionName().getValue());
		sb.append(" numDay:").append(dayRankingStruct.getNumDay());
		sb.append(" seasonStart:").append(
				dayRankingStruct.getSeasonStartDate().getValue().toString());
		sb.append(" seasonEnd:").append(
				dayRankingStruct.getSeasonEndDate().getValue().toString());
		for (TeamDayRankingStruct teamDayRankingStruct : dayRankingStruct
				.getLstTeamRankingStruct().getValue().getTeamDayRankingStruct()) {
			sb.append("\n TeamDayRanking: ");
			sb.append(" clubName:").append(
					teamDayRankingStruct.getClubName().getValue());
			sb.append(" teamName:").append(
					teamDayRankingStruct.getTeamName().getValue());
			sb.append(" rank:").append(teamDayRankingStruct.getRank());
			sb.append(" evolutionOfRank:").append(
					teamDayRankingStruct.getEvolutionOfRank().getValue());
			sb.append(" goalAgainst:").append(
					teamDayRankingStruct.getGoalAgainst());
			sb.append(" goalAverage:").append(
					teamDayRankingStruct.getGoalAverage());
			sb.append(" goalFor:").append(teamDayRankingStruct.getGoalFor());
			sb.append(" idTeam:").append(teamDayRankingStruct.getIdTeam());
			sb.append(" draw:").append(teamDayRankingStruct.getNbDraw());
			sb.append(" lost:").append(teamDayRankingStruct.getNbLost());
			sb.append(" matchesPlayed:").append(
					teamDayRankingStruct.getNbMatchesPlayed());
			sb.append(" points:").append(teamDayRankingStruct.getNbPoints());
			sb.append(" won:").append(teamDayRankingStruct.getNbWon());
			for (CIS cis : teamDayRankingStruct.getLstCIS().getValue().getCIS()) {
				sb.append(" CIS: ").append(cis.getIdClient().getValue())
						.append("=").append(cis.getName().getValue());
			}
		}
		sb.append("\n ]");
		logger.info(sb.toString());
	}

	public static void logDayScoringStruct(DayScoringStruct dayScoring) {
		StringBuilder sb = new StringBuilder();

		sb.append("[DayScoringStruct:");
		for (MatchDayScoringStruct matchDayScoring : dayScoring
				.getLstMatchsScoringStruct().getValue()
				.getMatchDayScoringStruct()) {
			sb.append("\n Match: codeMatch:").append(
					matchDayScoring.getCodeMatch().getValue());
			sb.append(" competitionName:").append(
					matchDayScoring.getCompetitionName().getValue());
			sb.append(" dateTime:").append(
					matchDayScoring.getDateAndTime().toString());
			sb.append(" idMatch:").append(matchDayScoring.getIdMatch());
			sb.append(" isStatus:").append(
					matchDayScoring.getIdStatus().getValue());
			for (DayTeamScoringStruct dayTeamScoring : matchDayScoring
					.getLstTeamScoringStruct().getValue()
					.getDayTeamScoringStruct()) {
				sb.append("\n DayTeamScoring:");
				sb.append(" idTeam:").append(
						dayTeamScoring.getIdTeam().getValue());
				sb.append(" scoreEnd:").append(
						dayTeamScoring.getScoreEndOfficialTime().getValue());
				sb.append(" team:").append(
						dayTeamScoring.getTeamName().getValue());
			}
		}
		sb.append("\n]");

		logger.info(sb.toString());
	}

	private DataLogUtil() {
		// no instances
	}
}
