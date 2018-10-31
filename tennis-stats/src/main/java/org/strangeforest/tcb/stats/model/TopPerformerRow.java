package org.strangeforest.tcb.stats.model;

import org.strangeforest.tcb.stats.model.core.*;

public class TopPerformerRow extends PlayerRow {

	private final WonLost wonLost;

    public TopPerformerRow(int rank, int playerId, String name, String chineseName, String countryId, Boolean active, WonLost wonLost) {
        super(rank, playerId, name, chineseName, countryId, active);
		this.wonLost = wonLost;
	}

	public String getWonLostPct() {
		return wonLost.getWonPctStr(2);
	}

	public int getWon() {
		return wonLost.getWon();
	}

	public int getLost() {
		return wonLost.getLost();
	}

	public int getPlayed() {
		return wonLost.getTotal();
	}
}
