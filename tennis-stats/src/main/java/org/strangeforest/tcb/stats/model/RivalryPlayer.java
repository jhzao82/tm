package org.strangeforest.tcb.stats.model;

import org.strangeforest.tcb.util.*;

public class RivalryPlayer implements Comparable<RivalryPlayer> {

	private final int playerId;
	private final String name;
    private final String chineseName;
	private final Country country;
	private final boolean active;
	private final int goatPoints;

    public RivalryPlayer(int playerId, String name, String chineseName, String countryId, boolean active, int goatPoints) {
		this.playerId = playerId;
		this.name = name;
        this.chineseName = chineseName;
		country = new Country(countryId);
		this.active = active;
		this.goatPoints = goatPoints;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

    public String getChineseName() {
        return chineseName;
    }

	public String getInitials() {
		StringBuilder sb = new StringBuilder();
		for (String word : name.split(" "))
			sb.append(word.charAt(0));
		return sb.toString();
	}

	public Country getCountry() {
		return country;
	}

	public boolean isActive() {
		return active;
	}

	public int getGoatPoints() {
		return goatPoints;
	}


	// Object methods

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RivalryPlayer)) return false;
		RivalryPlayer player = (RivalryPlayer)o;
		return playerId == player.playerId;
	}

	@Override public int hashCode() {
		return playerId;
	}

	@Override public int compareTo(RivalryPlayer player) {
		return Integer.compare(player.goatPoints, goatPoints);
	}
}
