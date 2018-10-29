-- tournament

CREATE TABLE tournament (
	tournament_id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	chinese_name TEXT,
	country_id TEXT,
	city TEXT,
	level tournament_level NOT NULL,
	surface surface,
	indoor BOOLEAN NOT NULL,
	linked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX ON tournament (level);
CREATE INDEX ON tournament (surface);


-- tournament_mapping

CREATE TABLE tournament_mapping (
	ext_tournament_id TEXT PRIMARY KEY,
	tournament_id INTEGER NOT NULL REFERENCES tournament (tournament_id) ON DELETE CASCADE
);

CREATE INDEX ON tournament_mapping (tournament_id);


-- tournament_event

CREATE TABLE tournament_event (
	tournament_event_id SERIAL PRIMARY KEY,
	tournament_id INTEGER NOT NULL REFERENCES tournament (tournament_id),
	original_tournament_id INTEGER NOT NULL REFERENCES tournament (tournament_id),
	season SMALLINT NOT NULL,
	date DATE NOT NULL,
	name TEXT NOT NULL,
	chinese_name TEXT,
	city TEXT,
	level tournament_level NOT NULL,
	surface surface,
	indoor BOOLEAN NOT NULL,
	draw_type draw_type,
	draw_size SMALLINT,
	rank_points INTEGER,
	map_properties JSON,
	UNIQUE (original_tournament_id, season)
);

CREATE INDEX ON tournament_event (tournament_id);
CREATE INDEX ON tournament_event (season);
CREATE INDEX ON tournament_event (level);
CREATE INDEX ON tournament_event (surface);


-- tournament_event_rank_factor

CREATE TABLE tournament_event_rank_factor (
	rank_from INTEGER NOT NULL PRIMARY KEY,
	rank_to INTEGER NOT NULL,
	rank_factor INTEGER
);


-- player

CREATE TABLE player (
	player_id SERIAL PRIMARY KEY,
	first_name TEXT,
	last_name TEXT,
	chinese_name TEXT,
	dob DATE,
	country_id TEXT NOT NULL,
	birthplace TEXT,
	residence TEXT,
	height SMALLINT,
	weight SMALLINT,
	hand player_hand,
	backhand player_backhand,
	active BOOLEAN,
	turned_pro SMALLINT,
	coach TEXT,
	prize_money TEXT,
	wikipedia TEXT,
	web_site TEXT,
	facebook TEXT,
	twitter TEXT,
	nicknames TEXT,
	UNIQUE (first_name, last_name, dob)
);

CREATE OR REPLACE FUNCTION full_name(
	p_first_name TEXT,
	p_last_name TEXT
) RETURNS TEXT IMMUTABLE AS $$
BEGIN
	IF p_first_name IS NULL THEN
		RETURN p_last_name;
	ELSIF p_last_name IS NULL THEN
		RETURN p_first_name;
	ELSE
		RETURN p_first_name || ' ' || p_last_name;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE INDEX player_name_idx ON player (full_name(first_name, last_name));
CREATE INDEX player_name_gin_idx ON player USING gin (full_name(first_name, last_name) gin_trgm_ops, nicknames gin_trgm_ops);
CREATE INDEX ON player (country_id);


-- player_mapping

CREATE TABLE player_mapping (
	ext_player_id INTEGER PRIMARY KEY,
	player_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE CASCADE
);

CREATE INDEX ON player_mapping (player_id);


-- player_alias

CREATE TABLE player_alias (
	alias TEXT PRIMARY KEY,
	name TEXT
);


-- player_ranking

CREATE TABLE player_ranking (
	rank_date DATE NOT NULL,
	player_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE CASCADE,
	rank INTEGER NOT NULL,
	rank_points INTEGER,
	PRIMARY KEY (rank_date, player_id)
);

CREATE INDEX ON player_ranking (player_id);


-- player_elo_ranking

CREATE TABLE player_elo_ranking (
	rank_date DATE NOT NULL,
	player_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE CASCADE,
	rank INTEGER NOT NULL,
	elo_rating INTEGER NOT NULL,
	recent_rank INTEGER,
	recent_elo_rating INTEGER,
	hard_rank INTEGER,
	hard_elo_rating INTEGER,
	clay_rank INTEGER,
	clay_elo_rating INTEGER,
	grass_rank INTEGER,
	grass_elo_rating INTEGER,
	carpet_rank INTEGER,
	carpet_elo_rating INTEGER,
	outdoor_rank INTEGER,
	outdoor_elo_rating INTEGER,
	indoor_rank INTEGER,
	indoor_elo_rating INTEGER,
	set_rank INTEGER,
	set_elo_rating INTEGER,
	game_rank INTEGER,
	game_elo_rating INTEGER,
	service_game_rank INTEGER,
	service_game_elo_rating INTEGER,
	return_game_rank INTEGER,
	return_game_elo_rating INTEGER,
	tie_break_rank INTEGER,
	tie_break_elo_rating INTEGER,
	PRIMARY KEY (rank_date, player_id)
);

CREATE INDEX ON player_elo_ranking (player_id);


-- match

CREATE TABLE match (
	match_id BIGSERIAL PRIMARY KEY,
	tournament_event_id INTEGER NOT NULL REFERENCES tournament_event (tournament_event_id) ON DELETE CASCADE,
	match_num SMALLINT NOT NULL,
	date DATE NOT NULL,
	surface surface,
	indoor BOOLEAN NOT NULL,
	round match_round NOT NULL,
	best_of SMALLINT NOT NULL,
	winner_id INTEGER NOT NULL REFERENCES player (player_id),
	winner_country_id TEXT NOT NULL,
	winner_seed SMALLINT,
	winner_entry tournament_entry,
	winner_rank INTEGER,
	winner_rank_points INTEGER,
	winner_elo_rating INTEGER,
	winner_next_elo_rating INTEGER,
	winner_age REAL,
	winner_height SMALLINT,
	loser_id INTEGER NOT NULL REFERENCES player (player_id),
	loser_country_id TEXT NOT NULL,
	loser_seed SMALLINT,
	loser_entry tournament_entry,
	loser_rank INTEGER,
	loser_rank_points INTEGER,
	loser_elo_rating INTEGER,
	loser_next_elo_rating INTEGER,
	loser_age REAL,
	loser_height SMALLINT,
	score TEXT,
	outcome match_outcome,
	w_sets SMALLINT,
	l_sets SMALLINT,
	w_games SMALLINT,
	l_games SMALLINT,
	w_tbs SMALLINT,
	l_tbs SMALLINT,
	has_stats BOOLEAN,
	UNIQUE (tournament_event_id, match_num)
);

CREATE INDEX ON match (tournament_event_id);
CREATE INDEX ON match (winner_id);
CREATE INDEX ON match (loser_id);


-- set_score

CREATE TABLE set_score (
	match_id BIGINT NOT NULL REFERENCES match (match_id) ON DELETE CASCADE,
	set SMALLINT NOT NULL,
	w_games SMALLINT NOT NULL,
	l_games SMALLINT NOT NULL,
	w_tb_pt SMALLINT,
	l_tb_pt SMALLINT,
	w_tbs SMALLINT,
	l_tbs SMALLINT,
	PRIMARY KEY (match_id, set)
);


-- match_stats

CREATE TABLE match_stats (
	match_id BIGINT NOT NULL REFERENCES match (match_id) ON DELETE CASCADE,
	set SMALLINT NOT NULL,
	minutes SMALLINT,
	w_ace SMALLINT,
	w_df SMALLINT,
	w_sv_pt SMALLINT,
	w_1st_in SMALLINT,
	w_1st_won SMALLINT,
	w_2nd_won SMALLINT,
	w_sv_gms SMALLINT,
	w_bp_sv SMALLINT,
	w_bp_fc SMALLINT,
	l_ace SMALLINT,
	l_df SMALLINT,
	l_sv_pt SMALLINT,
	l_1st_in SMALLINT,
	l_1st_won SMALLINT,
	l_2nd_won SMALLINT,
	l_sv_gms SMALLINT,
	l_bp_sv SMALLINT,
	l_bp_fc SMALLINT,
	PRIMARY KEY (match_id, set)
);


-- match_price

CREATE TABLE match_price (
	match_id BIGINT NOT NULL REFERENCES match (match_id) ON DELETE CASCADE,
	source TEXT NOT NULL,
	winner_price REAL NOT NULL,
	loser_price REAL NOT NULL,
	PRIMARY KEY (match_id, source)
);


-- team_tournament_event_winner

CREATE TABLE team_tournament_event_winner (
	season INTEGER NOT NULL,
	level tournament_level NOT NULL,
	winner_id TEXT NOT NULL,
	runner_up_id TEXT NOT NULL,
	score TEXT,
	PRIMARY KEY (season, level)
);


-- in_progress_event

CREATE TABLE in_progress_event (
	in_progress_event_id SERIAL PRIMARY KEY,
	tournament_id INTEGER NOT NULL REFERENCES tournament (tournament_id) ON DELETE CASCADE,
	date DATE NOT NULL,
	name TEXT NOT NULL,
	level tournament_level NOT NULL,
	surface surface,
	indoor BOOLEAN NOT NULL,
	draw_type draw_type,
	draw_size SMALLINT,
	completed BOOLEAN NOT NULL DEFAULT FALSE,
	matches_hash TEXT,
	UNIQUE (tournament_id)
);


-- in_progress_match

CREATE TABLE in_progress_match (
	in_progress_match_id BIGSERIAL PRIMARY KEY,
	in_progress_event_id INTEGER NOT NULL REFERENCES in_progress_event (in_progress_event_id) ON DELETE CASCADE,
	match_num SMALLINT NOT NULL,
	prev_match_num1 SMALLINT,
	prev_match_num2 SMALLINT,
	date DATE,
	surface surface,
	indoor BOOLEAN,
	round match_round NOT NULL,
	best_of SMALLINT,
	player1_id INTEGER REFERENCES player (player_id),
	player1_country_id TEXT,
	player1_seed SMALLINT,
	player1_entry tournament_entry,
	player1_rank INTEGER,
	player1_elo_rating INTEGER,
	player1_next_elo_rating INTEGER,
	player1_recent_elo_rating INTEGER,
	player1_next_recent_elo_rating INTEGER,
	player1_surface_elo_rating INTEGER,
	player1_next_surface_elo_rating INTEGER,
	player1_in_out_elo_rating INTEGER,
	player1_next_in_out_elo_rating INTEGER,
	player1_set_elo_rating INTEGER,
	player1_next_set_elo_rating INTEGER,
	player2_id INTEGER REFERENCES player (player_id),
	player2_country_id TEXT,
	player2_seed SMALLINT,
	player2_entry tournament_entry,
	player2_rank INTEGER,
	player2_elo_rating INTEGER,
	player2_next_elo_rating INTEGER,
	player2_recent_elo_rating INTEGER,
	player2_next_recent_elo_rating INTEGER,
	player2_surface_elo_rating INTEGER,
	player2_next_surface_elo_rating INTEGER,
	player2_in_out_elo_rating INTEGER,
	player2_next_in_out_elo_rating INTEGER,
	player2_set_elo_rating INTEGER,
	player2_next_set_elo_rating INTEGER,
	winner SMALLINT,
	score TEXT,
	outcome match_outcome,
	p1_sets SMALLINT,
	p2_sets SMALLINT,
	p1_games SMALLINT,
	p2_games SMALLINT,
	p1_tbs SMALLINT,
	p2_tbs SMALLINT,
	p1_set_games SMALLINT[],
	p2_set_games SMALLINT[],
	p1_set_tb_pt SMALLINT[],
	p2_set_tb_pt SMALLINT[],
	has_stats BOOLEAN,
	UNIQUE (in_progress_event_id, match_num)
);

CREATE INDEX ON in_progress_match (player1_id);
CREATE INDEX ON in_progress_match (player2_id);


-- in_progress_match_stats

CREATE TABLE in_progress_match_stats (
	in_progress_match_id BIGINT NOT NULL REFERENCES in_progress_match (in_progress_match_id) ON DELETE CASCADE,
	set SMALLINT NOT NULL,
	minutes SMALLINT,
	p1_ace SMALLINT,
	p1_df SMALLINT,
	p1_sv_pt SMALLINT,
	p1_1st_in SMALLINT,
	p1_1st_won SMALLINT,
	p1_2nd_won SMALLINT,
	p1_sv_gms SMALLINT,
	p1_bp_sv SMALLINT,
	p1_bp_fc SMALLINT,
	p2_ace SMALLINT,
	p2_df SMALLINT,
	p2_sv_pt SMALLINT,
	p2_1st_in SMALLINT,
	p2_1st_won SMALLINT,
	p2_2nd_won SMALLINT,
	p2_sv_gms SMALLINT,
	p2_bp_sv SMALLINT,
	p2_bp_fc SMALLINT,
	PRIMARY KEY (in_progress_match_id, set)
);


-- player_in_progress_result

CREATE TABLE player_in_progress_result (
	in_progress_event_id INTEGER NOT NULL REFERENCES in_progress_event (in_progress_event_id) ON DELETE CASCADE,
	player_id INTEGER REFERENCES player (player_id) ON DELETE CASCADE,
	base_result tournament_event_result,
	result tournament_event_result,
	probability REAL NOT NULL,
	avg_draw_probability REAL,
	no_draw_probability REAL,
	PRIMARY KEY (in_progress_event_id, player_id, base_result, result)
);


-- tournament_rank_points

CREATE TABLE tournament_rank_points (
	level tournament_level NOT NULL,
	draw_type draw_type NOT NULL,
	result tournament_event_result NOT NULL,
	rank_points INTEGER,
	rank_points_2008 INTEGER,
	goat_points INTEGER,
	additive BOOLEAN,
	PRIMARY KEY (level, draw_type, result)
);


-- year_end_rank_goat_points

CREATE TABLE year_end_rank_goat_points (
	year_end_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (year_end_rank, goat_points)
);


-- best_rank_goat_points

CREATE TABLE best_rank_goat_points (
	best_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (best_rank, goat_points)
);


-- weeks_at_no1_goat_points

CREATE TABLE weeks_at_no1_goat_points (
	weeks_for_point INTEGER NOT NULL PRIMARY KEY
);


-- weeks_at_elo_topn_goat_points

CREATE TABLE weeks_at_elo_topn_goat_points (
	rank INTEGER NOT NULL PRIMARY KEY,
	weeks_for_point INTEGER NOT NULL
);


-- best_elo_rating_goat_points

CREATE TABLE best_elo_rating_goat_points (
	best_elo_rating_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (best_elo_rating_rank, goat_points)
);


-- best_surface_elo_rating_goat_points

CREATE TABLE best_surface_elo_rating_goat_points (
	best_elo_rating_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (best_elo_rating_rank, goat_points)
);


-- best_indoor_elo_rating_goat_points

CREATE TABLE best_indoor_elo_rating_goat_points (
	best_elo_rating_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (best_elo_rating_rank, goat_points)
);


-- best_in_match_elo_rating_goat_points

CREATE TABLE best_in_match_elo_rating_goat_points (
	best_elo_rating_rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (best_elo_rating_rank, goat_points)
);


-- grand_slam_goat_points

CREATE TABLE grand_slam_goat_points (
	career_grand_slam INTEGER NOT NULL,
	season_grand_slam INTEGER NOT NULL,
	season_3_grand_slam INTEGER NOT NULL,
	grand_slam_holder INTEGER NOT NULL,
	consecutive_grand_slam_on_same_event INTEGER NOT NULL,
	grand_slam_on_same_event REAL NOT NULL
);


-- big_win_match_factor

CREATE TABLE big_win_match_factor (
	level tournament_level NOT NULL,
	round match_round NOT NULL,
	match_factor INTEGER,
	PRIMARY KEY (level, round)
);


-- big_win_rank_factor

CREATE TABLE big_win_rank_factor (
	rank_from INTEGER NOT NULL PRIMARY KEY,
	rank_to INTEGER NOT NULL,
	rank_factor INTEGER
);


-- h2h_rank_factor

CREATE TABLE h2h_rank_factor (
	rank_from INTEGER NOT NULL PRIMARY KEY,
	rank_to INTEGER NOT NULL,
	rank_factor INTEGER
);


-- records_goat_points

CREATE TABLE records_goat_points (
	record_id TEXT NOT NULL,
	rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (record_id, rank)
);


-- surface_records_goat_points

CREATE TABLE surface_records_goat_points (
	record_id TEXT NOT NULL,
	rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (record_id, rank)
);


-- best_season_goat_points

CREATE TABLE best_season_goat_points (
	season_rank INTEGER NOT NULL PRIMARY KEY,
	goat_points INTEGER NOT NULL
);


-- greatest_rivalries_goat_points

CREATE TABLE greatest_rivalries_goat_points (
	rivalry_rank INTEGER NOT NULL PRIMARY KEY,
	goat_points INTEGER NOT NULL
);


-- performance_category

CREATE TABLE performance_category (
	category_id TEXT NOT NULL PRIMARY KEY,
	name TEXT NOT NULL,
	min_entries INTEGER NOT NULL,
	sort_order INTEGER NOT NULL
);


-- performance_goat_points

CREATE TABLE performance_goat_points (
	category_id TEXT NOT NULL REFERENCES performance_category (category_id) ON DELETE CASCADE,
	rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (category_id, rank)
);


-- statistics_category

CREATE TABLE statistics_category (
	category_id TEXT NOT NULL PRIMARY KEY,
	name TEXT NOT NULL,
	min_entries INTEGER NOT NULL,
	sort_order INTEGER NOT NULL
);


-- statistics_goat_points

CREATE TABLE statistics_goat_points (
	category_id TEXT NOT NULL REFERENCES statistics_category (category_id) ON DELETE CASCADE,
	rank INTEGER NOT NULL,
	goat_points INTEGER NOT NULL,
	PRIMARY KEY (category_id, rank)
);


-- player_record

CREATE TABLE player_record (
	record_id TEXT NOT NULL,
	sort_order INTEGER NOT NULL,
	rank INTEGER NOT NULL,
	player_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE CASCADE,
	detail JSON NOT NULL,
	PRIMARY KEY (record_id, sort_order)
);

CREATE INDEX ON player_record (player_id);
CREATE INDEX ON player_record (rank);


-- active_player_record

CREATE TABLE active_player_record (
	record_id TEXT NOT NULL,
	sort_order INTEGER NOT NULL,
	rank INTEGER NOT NULL,
	player_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE CASCADE,
	detail JSON NOT NULL,
	PRIMARY KEY (record_id, sort_order)
);

CREATE INDEX ON active_player_record (player_id);


-- saved_record

CREATE TABLE saved_record (
	record_id TEXT NOT NULL,
	active_players BOOLEAN NOT NULL,
	infamous BOOLEAN NOT NULL,
	PRIMARY KEY (record_id, active_players)
);


-- visitor

CREATE TABLE visitor (
	visitor_id SERIAL PRIMARY KEY,
	ip_address TEXT NOT NULL,
	country_id TEXT,
	country TEXT,
	agent_type TEXT,
	hits INTEGER NOT NULL,
	active BOOLEAN NOT NULL DEFAULT TRUE,
	first_hit TIMESTAMP(3) NOT NULL DEFAULT now(),
	last_hit TIMESTAMP(3) NOT NULL
);

CREATE INDEX ON visitor (ip_address);
CREATE INDEX ON visitor (active);
CREATE INDEX ON visitor (last_hit);


-- visitor_summary

CREATE TABLE visitor_summary (
	date DATE NOT NULL,
	country_id TEXT,
	agent_type TEXT,
	visits BIGINT NOT NULL,
	hits BIGINT NOT NULL,
	visit_duration INTERVAL NOT NULL
);

CREATE UNIQUE INDEX ON visitor_summary (date, country_id, agent_type);