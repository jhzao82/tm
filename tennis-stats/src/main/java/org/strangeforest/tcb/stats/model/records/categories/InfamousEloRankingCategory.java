package org.strangeforest.tcb.stats.model.records.categories;

public class InfamousEloRankingCategory extends RankingCategory {

	private static final String ELO_DIFF_CONDITION = "r1.rank_date >= DATE '1968-07-01'";

	public InfamousEloRankingCategory() {
		super("Elo Ranking");
		register(leastPointsAsNo1("SmallestEloRating", "Smallest Elo Rating as Elo No. 1", "player_elo_ranking", "elo_rating", "elo_rating", "Elo Rating"));
		register(leastEndOfSeasonPointsAsNo1("SmallestEndOfSeasonEloRating", "Smallest End of Season Elo Rating as Elo No. 1", "player_year_end_elo_rank", "year_end_elo_rating", "Elo Rating"));
		register(pointsDifferenceBetweenNo1andNo2(
			"SmallestEloPointsNo1No2Difference", "Smallest Elo Rating Difference Between No. 1 and No. 2", "player_elo_ranking", "elo_rating", ELO_DIFF_CONDITION,
			"r1.elo_rating - r2.elo_rating", "r1.elo_rating", "r2.elo_rating", "r.value",
			"numeric", null, "Rating", "Rating Diff."
		));
	}
}