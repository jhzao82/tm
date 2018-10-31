package org.strangeforest.tcb.stats.model;

import java.time.*;

public class PlayerDiffRankingsRow extends PlayerRankingsRow {

	private final Integer rankDiff;
	private final Integer pointsDiff;
	private final int bestPoints;

    public PlayerDiffRankingsRow(int rank, int playerId, String name, String chineseName, String countryId, int points, int bestRank, LocalDate bestRankDate, Integer rankDiff, Integer pointsDiff, int bestPoints) {
        super(rank, playerId, name, chineseName, countryId, null, points, bestRank, bestRankDate);
		this.rankDiff = rankDiff;
		this.pointsDiff = pointsDiff;
		this.bestPoints = bestPoints;
	}

	public Integer getRankDiff() {
		return rankDiff;
	}

	public Integer getPointsDiff() {
		return pointsDiff;
	}

	public int getBestPoints() {
		return bestPoints;
	}
}
