update tournament_event set chinese_name = (select chinese_name from tournament where tournament_id = tournament_event.original_tournament_id);

COMMIT;