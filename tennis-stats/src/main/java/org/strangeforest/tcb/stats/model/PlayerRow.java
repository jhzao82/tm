package org.strangeforest.tcb.stats.model;

import java.util.regex.*;

import org.strangeforest.tcb.util.*;

public class PlayerRow {

	private final int rank;
	private final int playerId;
	private final String name;
    private final String chineseName;
	private final Country country;
	private final Boolean active;

    public PlayerRow(int rank, int playerId, String name, String chineseName, String countryId, Boolean active) {
		this.rank = rank;
		this.playerId = playerId;
		this.name = name;
        this.chineseName = chineseName;
		country = new Country(countryId);
		this.active = active;
	}

	public PlayerRow(PlayerRow player) {
		rank = player.rank;
		playerId = player.playerId;
		name = player.name;
        chineseName = player.chineseName;
		country = player.country;
		active = player.active;
	}

	public int getRank() {
		return rank;
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

	public Country getCountry() {
		return country;
	}

	public Boolean getActive() {
		return active;
	}

	private static final Pattern SHORT_NAME_PATTERN = Pattern.compile("[^A-Z\\s]+(?=\\s+[A-Z]+[^A-Z\\s]+)");

	public String shortName() {
		return SHORT_NAME_PATTERN.matcher(name).replaceAll(".");
	}
}
