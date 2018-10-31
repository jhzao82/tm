package org.strangeforest.tcb.stats.model;

import java.time.*;

import org.strangeforest.tcb.stats.model.core.*;

public class PlayerPeakEloRankingsRow extends PlayerRankingsRow {

	private final LocalDate pointsDate;
	private final TournamentEventItem tournamentEvent;

    public PlayerPeakEloRankingsRow(int rank, int playerId, String name, String chineseName, String countryId, Boolean active, int points, LocalDate pointsDate, int bestRank, LocalDate bestRankDate, TournamentEventItem tournamentEvent) {
        super(rank, playerId, name, chineseName, countryId, active, points, bestRank, bestRankDate);
		this.pointsDate = pointsDate;
		this.tournamentEvent = tournamentEvent;
	}

	public LocalDate getPointsDate() {
		return pointsDate;
	}

	public TournamentEventItem getTournamentEvent() {
		return tournamentEvent;
	}
}
