package org.strangeforest.tcb.stats.model.records.details;

public abstract class BaseSeasonRankingDiffRecordDetail<T> extends BaseRankingDiffRecordDetail<T> implements SeasonRecordDetail<T> {

	private final int season;

    protected BaseSeasonRankingDiffRecordDetail(T value, int playerId2, String name2, String chineseName2, String countryId2, Boolean active2, int value1, int value2, int season) {
        super(value, playerId2, name2, chineseName2, countryId2, active2, value1, value2);
		this.season = season;
	}

	@Override public int getSeason() {
		return season;
	}

	@Override public String toDetailString() {
		return String.valueOf(season);
	}
}
