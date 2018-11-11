package org.strangeforest.tcb.stats.model;

import java.util.*;

import org.strangeforest.tcb.stats.model.core.*;

import static java.util.Collections.*;
import static java.util.Comparator.*;
import static java.util.Comparator.reverseOrder;

public class TournamentLevelTimeline {

	private final Map<Integer, List<TournamentItem>> seasonsTournaments = new TreeMap<>(reverseOrder());
	private final Map<Integer, List<TournamentLevelTimelineItem>> seasonsEvents = new HashMap<>();
	private final Map<String, Map<Integer, Integer>> playerWins = new HashMap<>();

	public Set<Integer> getSeasons() {
		return seasonsTournaments.keySet();
	}

	public boolean areSameTournaments(int season) {
		Optional<Integer> nextSeason = getNextSeason(season);
		return nextSeason.isPresent() && startsWith(seasonsTournaments.get(nextSeason.get()), seasonsTournaments.get(season));
	}

	public List<TournamentItem> getTournaments(int season) {
		return seasonsTournaments.get(season);
	}

	public List<TournamentLevelTimelineItem> getSeasonEvents(int season) {
		return seasonsEvents.get(season);
	}

	public int getWins(String level, int playerId) {
		return playerWins.getOrDefault(level, emptyMap()).get(playerId);
	}

	public void addItem(TournamentLevelTimelineItem item) {
		addSeasonTournament(item);
		addSeasonEvent(item);
		updatePlayerWins(item);
	}

	private void addSeasonTournament(TournamentLevelTimelineItem item) {
        TournamentItem tournamentItem = new TournamentItem(item.getTournamentId(), item.getName(), item.getChineseName(), item.getLevel());
		seasonsTournaments.computeIfAbsent(item.getSeason(), s -> new ArrayList<>()).add(tournamentItem);
	}

	private void addSeasonEvent(TournamentLevelTimelineItem item) {
		seasonsEvents.computeIfAbsent(item.getSeason(), s -> new ArrayList<>()).add(item);
	}

	private void updatePlayerWins(TournamentLevelTimelineItem item) {
		PlayerRow winner = item.getWinner();
		if (winner == null)
			return;
		item.setPlayerWins(playerWins.computeIfAbsent(item.getLevel(), l -> new HashMap<>()).compute(winner.getPlayerId(), (p, w) -> w != null ? w + 1 : 1));
	}

	public void addMissingSeasonLastTournaments() {
		for (int season : getSeasons()) {
			Optional<Integer> optionalPrevSeason = getPrevSeason(season);
			if (!optionalPrevSeason.isPresent())
				continue;
			int prevSeason = optionalPrevSeason.get();
			List<TournamentItem> seasonTournaments = seasonsTournaments.get(season);
			List<TournamentItem> prevSeasonTournaments = seasonsTournaments.get(prevSeason);
			if (startsWith(prevSeasonTournaments, seasonTournaments)) {
				List<TournamentLevelTimelineItem> prevSeasonEvents = seasonsEvents.get(prevSeason);
				for (int index = seasonTournaments.size(); index < prevSeasonEvents.size(); index++) {
					TournamentLevelTimelineItem item = prevSeasonEvents.get(index);
                    addItem(new TournamentLevelTimelineItem(item.getTournamentId(), item.getName(), item.getChineseName(), season, 0, null, item.getLevel(), null));
				}
			}
		}
	}


	// Util

	private Optional<Integer> getPrevSeason(int season) {
		Set<Integer> seasons = getSeasons();
		if (seasons.stream().sorted(naturalOrder()).findFirst().get() == season)
			return Optional.empty();
		else {
			for (int prevSeason = season - 1; true; prevSeason--) {
				if (seasons.contains(prevSeason))
					return Optional.of(prevSeason);
			}
		}
	}

	private Optional<Integer> getNextSeason(int season) {
		Set<Integer> seasons = getSeasons();
		if (seasons.stream().findFirst().get() == season)
			return Optional.empty();
		else {
			for (int nextSeason = season + 1; true; nextSeason++) {
				if (seasons.contains(nextSeason))
					return Optional.of(nextSeason);
			}
		}
	}

	private static <T> boolean startsWith(List<T> list, List<T> subList) {
		return subList.size() <= list.size() && list.subList(0, subList.size()).equals(subList);
	}
}
