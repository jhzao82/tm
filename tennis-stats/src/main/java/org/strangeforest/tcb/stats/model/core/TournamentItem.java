package org.strangeforest.tcb.stats.model.core;

import java.util.*;

import static org.strangeforest.tcb.util.CompareUtil.*;

public class TournamentItem implements Comparable<TournamentItem> {

	private final int id;
	private final String name;
	private final String chineseName;
	private final String level;

	public TournamentItem(int id, String name, String chineseName, String level) {
		this.id = id;
		this.name = name;
		this.chineseName = chineseName;
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getChineseName() {
		return chineseName;
	}

	public String getLevel() {
		return level;
	}


	// Object methods

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TournamentItem tournament = (TournamentItem)o;
		return id == tournament.id && Objects.equals(name, tournament.name) && Objects.equals(level, tournament.level);
	}

	@Override public int hashCode() {
		return Objects.hash(id, name, level);
	}

	@Override public int compareTo(TournamentItem tournament) {
		return nullsLastCompare(name, tournament.name);
	}
}
