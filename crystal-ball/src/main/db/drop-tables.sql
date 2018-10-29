DROP TABLE visitor CASCADE;
DROP TABLE visitor_summary CASCADE;

DROP TABLE player_record CASCADE;
DROP TABLE active_player_record CASCADE;
DROP TABLE saved_record CASCADE;

DROP TABLE tournament_rank_points CASCADE;
DROP TABLE year_end_rank_goat_points CASCADE;
DROP TABLE best_rank_goat_points CASCADE;
DROP TABLE weeks_at_no1_goat_points CASCADE;
DROP TABLE weeks_at_elo_topn_goat_points CASCADE;
DROP TABLE best_elo_rating_goat_points CASCADE;
DROP TABLE best_surface_elo_rating_goat_points CASCADE;
DROP TABLE best_indoor_elo_rating_goat_points CASCADE;
DROP TABLE best_in_match_elo_rating_goat_points CASCADE;

DROP TABLE grand_slam_goat_points CASCADE;
DROP TABLE big_win_match_factor CASCADE;
DROP TABLE big_win_rank_factor CASCADE;
DROP TABLE h2h_rank_factor CASCADE;
DROP TABLE records_goat_points CASCADE;
DROP TABLE surface_records_goat_points CASCADE;
DROP TABLE best_season_goat_points CASCADE;
DROP TABLE greatest_rivalries_goat_points CASCADE;

DROP TABLE performance_goat_points CASCADE;
DROP TABLE performance_category CASCADE;
DROP TABLE statistics_goat_points CASCADE;
DROP TABLE statistics_category CASCADE;

DROP TABLE player_in_progress_result CASCADE;
DROP TABLE in_progress_match_stats CASCADE;
DROP TABLE in_progress_match CASCADE;
DROP TABLE in_progress_event CASCADE;
DROP TABLE team_tournament_event_winner CASCADE;
DROP TABLE match_price CASCADE;
DROP TABLE match_stats CASCADE;
DROP TABLE set_score CASCADE;
DROP TABLE match CASCADE;
DROP TABLE tournament_event_rank_factor CASCADE;
DROP TABLE tournament_event CASCADE;
DROP TABLE tournament_mapping CASCADE;
DROP TABLE tournament CASCADE;

DROP TABLE player_elo_ranking CASCADE;
DROP TABLE player_ranking CASCADE;
DROP TABLE player_alias CASCADE;
DROP TABLE player_mapping CASCADE;
DROP TABLE player CASCADE;
DROP FUNCTION full_name(TEXT, TEXT);