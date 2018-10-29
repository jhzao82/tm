package org.strangeforest.tcb.dataload

import java.time.*

import org.jsoup.nodes.*
import org.jsoup.select.*
import org.springframework.jdbc.core.namedparam.*
import org.strangeforest.tcb.stats.model.core.*
import org.strangeforest.tcb.stats.model.forecast.*
import org.strangeforest.tcb.stats.service.*

import com.google.common.base.*
import com.google.common.hash.*
import groovy.sql.*

import static com.google.common.base.Strings.*
import static org.strangeforest.tcb.dataload.BaseXMLLoader.*
import static org.strangeforest.tcb.dataload.LoadParams.*
import static org.strangeforest.tcb.dataload.LoaderUtil.*

class ATPWorldTourInProgressTournamentLoader extends BaseATPWorldTourTournamentLoader {

	boolean forceForecast
	EloSurfaceFactors eloSurfaceFactors

	static final String LOAD_EVENT_SQL = //language=SQL
		'{call load_in_progress_event(' +
			':ext_tournament_id, :date, :name, :tournament_level, :surface, :indoor, :draw_type, :draw_size' +
		')}'

	static final String FETCH_EVENT_HASH_SQL = //language=SQL
		'SELECT matches_hash FROM in_progress_event\n' +
		'INNER JOIN tournament_mapping USING (tournament_id)\n' +
		'WHERE ext_tournament_id = :extId'

	static final String LOAD_MATCH_SQL = //language=SQL
		'{call load_in_progress_match(' +
			':ext_tournament_id, :match_num, :prev_match_num1, :prev_match_num2, :date, :surface, :indoor, :round, :best_of, ' +
			':player1_name, :player1_country_id, :player1_seed, :player1_entry, ' +
			':player2_name, :player2_country_id, :player2_seed, :player2_entry, ' +
			':winner, :score, :outcome, :p1_sets, :p2_sets, :p1_games, :p2_games, :p1_tbs, :p2_tbs, :p1_set_games, :p2_set_games, :p1_set_tb_pt, :p2_set_tb_pt, :minutes, ' +
			':p1_ace, :p1_df, :p1_sv_pt, :p1_1st_in, :p1_1st_won, :p1_2nd_won, :p1_sv_gms, :p1_bp_sv, :p1_bp_fc, ' +
			':p2_ace, :p2_df, :p2_sv_pt, :p2_1st_in, :p2_1st_won, :p2_2nd_won, :p2_sv_gms, :p2_bp_sv, :p2_bp_fc' +
		')}'

	static final String UPDATE_EVENT_HASH_SQL = //language=SQL
		'UPDATE in_progress_event SET matches_hash = :matchesHash\n' +
		'WHERE tournament_id = (SELECT tournament_id FROM tournament_mapping WHERE ext_tournament_id = :extId)'

	static final String FETCH_MATCHES_SQL = //language=SQL
		'SELECT m.*, e.tournament_id, e.level, e.draw_type FROM in_progress_match m\n' +
		'INNER JOIN in_progress_event e USING (in_progress_event_id)\n' +
		'INNER JOIN tournament_mapping tm USING (tournament_id)\n' +
		'WHERE tm.ext_tournament_id = :extId\n' +
		'ORDER BY in_progress_event_id, round, match_num'

	static final String UPDATE_MATCH_ELO_RATINGS_SQL = //language=SQL
		'UPDATE in_progress_match\n' +
		'SET player1_elo_rating = :player1_elo_rating, player1_next_elo_rating = :player1_next_elo_rating,\n' +
		'    player1_recent_elo_rating = :player1_recent_elo_rating, player1_next_recent_elo_rating = :player1_next_recent_elo_rating,\n' +
		'    player1_surface_elo_rating = :player1_surface_elo_rating, player1_next_surface_elo_rating = :player1_next_surface_elo_rating,\n' +
		'    player1_in_out_elo_rating = :player1_in_out_elo_rating, player1_next_in_out_elo_rating = :player1_next_in_out_elo_rating,\n' +
		'    player1_set_elo_rating = :player1_set_elo_rating, player1_next_set_elo_rating = :player1_next_set_elo_rating,\n' +
		'    player2_elo_rating = :player2_elo_rating, player2_next_elo_rating = :player2_next_elo_rating,\n' +
		'    player2_recent_elo_rating = :player2_recent_elo_rating, player2_next_recent_elo_rating = :player2_next_recent_elo_rating,\n' +
		'    player2_surface_elo_rating = :player2_surface_elo_rating, player2_next_surface_elo_rating = :player2_next_surface_elo_rating,\n' +
		'    player2_in_out_elo_rating = :player2_in_out_elo_rating, player2_next_in_out_elo_rating = :player2_next_in_out_elo_rating,\n' +
		'    player2_set_elo_rating = :player2_set_elo_rating, player2_next_set_elo_rating = :player2_next_set_elo_rating\n' +
		'WHERE in_progress_match_id = :in_progress_match_id'

	static final String LOAD_PLAYER_RESULT_SQL = //language=SQL
		'{call load_player_in_progress_result(' +
			':in_progress_event_id, :player_id, :base_result, :result, :probability, :avg_draw_probability::REAL, :no_draw_probability::REAL' +
		')}'

	static final String DELETE_PLAYER_PROGRESS_RESULTS_SQL = //language=SQL
		'DELETE FROM player_in_progress_result\n' +
		'WHERE in_progress_event_id = :inProgressEventId'

	static final String SELECT_EVENT_EXT_IDS_SQL = //language=SQL
		'SELECT ext_tournament_id FROM in_progress_event\n' +
		'INNER JOIN tournament_mapping USING (tournament_id)\n' +
		'WHERE NOT completed\n' +
		'ORDER BY ext_tournament_id'

	static final String COMPLETE_EVENT_SQL = //language=SQL
		'UPDATE in_progress_event SET completed = TRUE\n' +
		'WHERE tournament_id = (SELECT tournament_id FROM tournament_mapping WHERE ext_tournament_id = :extId)'


	ATPWorldTourInProgressTournamentLoader(Sql sql) {
		super(sql)
		forceForecast = getBooleanProperty(FORCE_FORECAST_PROPERTY, FORCE_FORECAST_DEFAULT)
		eloSurfaceFactors = new EloSurfaceFactors(sql, LocalDate.now().year - 1)
	}

	def loadAndForecastTournament(String urlId, extId, Integer season = null, String level = null, String surface = null, boolean verbose = false) {
		try {
			if (loadTournament(urlId, extId, season, level, surface, verbose))
				forecastTournament(extId, verbose)
			return true
		}
		catch (Exception ex) {
			System.err.println "Error loading and forecasting in-progress tournament: $urlId/$extId"
			ex.printStackTrace()
		}
		false
	}

	def loadTournament(String urlId, extId, Integer season, String level, String surface, boolean verbose) {
		def stopwatch = Stopwatch.createStarted()
		def url = tournamentUrl(urlId, extId, season)
		println "Fetching in-progress tournament URL '$url'"
		def doc = retriedGetDoc(url)
		def dates = doc.select('.tourney-dates').text()
		def atpLevel = extract(doc.select('.tourney-badge-wrapper > img:nth-child(1)').attr("src"), '_', 1)
		if (!atpLevel || atpLevel == 'itf') {
			println "Skipping tournament at '$url', unsupported level: $atpLevel"
			return 0
		}
		level = level ?: mapLevel(atpLevel, urlId)
		season = season ?: LocalDate.now().year
		def name = getName(doc, level, season)
		def atpSurface = doc.select('td.tourney-details:nth-child(2) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)').text()
		surface = surface ?: mapSurface(atpSurface)
		def indoor = mapIndoor(surface, name, season)
		def drawType = mapDrawType(level)
		if (drawType != 'KO') {
			println "Skipping tournament at '$url', unsupported drawType: $drawType"
			return 0
		}
		def drawSize = doc.select('a.not-in-system:nth-child(1) > span:nth-child(1)').text()
		def bestOf = smallint(mapBestOf(level))

		def matches = [:]
		short matchNum = 0
		def startDate = date extractStartDate(dates)
		def seedEntries = [:]

		def params = [:]
		params.ext_tournament_id = string extId
		params.date = startDate
		params.name = name
		params.tournament_level = level
		params.surface = surface
		params.indoor = indoor
		params.draw_type = drawType
		params.draw_size = smallint drawSize
		saveEvent(params)

		def drawTable = doc.select('#scoresDrawTable')
		def rounds = drawTable.select('thead > tr > th').findAll().collect { round -> round.text() }
		rounds = rounds.collect { r -> mapRound(r, rounds)}
		def entryRound = rounds[0]
		if (verbose) {
			println "Rounds $rounds"
			println '\n' + entryRound
		}

		// Processing entry round
		Elements entryMatches = drawTable.select('table.scores-draw-entry-box-table')
		entryMatches.each { entryMatch ->
			matchNum += 1
			def name1 = extractEntryPlayer(entryMatch, 1)
			def name2 = extractEntryPlayer(entryMatch, 2)
			def seedEntry1 = extractSeedEntry entryMatch.select('tbody > tr:nth-child(1) > td:nth-child(2)').text()
			def seedEntry2 = extractSeedEntry entryMatch.select('tbody > tr:nth-child(2) > td:nth-child(2)').text()
			if (isQualifier(name1)) {
				name1 = null
				if (!seedEntry1)
					seedEntry1 = 'Q'
			}
			if (isQualifier(name2)) {
				name2 = null
				if (!seedEntry2)
					seedEntry2 = 'Q'
			}
			def isSeed1 = allDigits seedEntry1
			def isSeed2 = allDigits seedEntry2
			def seed1 = isSeed1 ? smallint(seedEntry1) : null
			def seed2 = isSeed2 ? smallint(seedEntry2) : null
			def entry1 = !isSeed1 ? mapEntry(string(seedEntry1)) : null
			def entry2 = !isSeed2 ? mapEntry(string(seedEntry2)) : null
			seedEntries[name1] = [seed: seed1, entry: entry1]
			seedEntries[name2] = [seed: seed2, entry: entry2]

			params = [:]
			params.ext_tournament_id = string extId
			params.match_num = matchNum
			params.date = startDate
			params.surface = surface
			params.indoor = indoor
			params.round = entryRound
			params.best_of = bestOf

			params.player1_name = name1
			params.player1_seed = seed1
			params.player1_entry = entry1

			params.player2_name = name2
			params.player2_seed = seed2
			params.player2_entry = entry2

			setScoreParams(params)

			matches[matchNum] = params

			if (verbose)
				println "$seedEntry1 $name1 vs $seedEntry2 $name2"
		}

		// Processing other rounds
		def drawRowSpan = 1
		short prevMatchNumOffset = 1
		rounds.findAll { round -> round != entryRound }.each { round ->
			if (verbose)
				println '\n' + round
			short matchNumOffset = matchNum + 1
			Elements roundPlayers = drawTable.select("tbody > tr > td[rowspan=$drawRowSpan]")
			if (!roundPlayers.select('div.scores-draw-entry-box').find { e -> e.text() })
				return
			if (round == 'Winner' && roundPlayers.size() == 2) // Workaround ATP website bug
				round = 'F'
			if (round == 'Winner') {
				def winner = roundPlayers.get(0)
				def winnerName = player winner.select('a.scores-draw-entry-box-players-item').text()
				def finalScoreElem = winner.select('a.scores-draw-entry-box-score')
				setScoreParams(matches[prevMatchNumOffset], finalScoreElem, winnerName)
			}
			else {
				def name1
				short prevMatchNum1
				roundPlayers.eachWithIndex { roundPlayer, index ->
					if (index % 2 == 0) {
						prevMatchNum1 = prevMatchNumOffset + index
						name1 = extractPlayer(roundPlayer)
						def prevScoreElem = roundPlayer.select('a.scores-draw-entry-box-score')
						setScoreParams(matches[prevMatchNum1], prevScoreElem, name1)
					}
					else {
						matchNum += 1
						short prevMatchNum2 = prevMatchNumOffset + index
						def name2 = extractPlayer(roundPlayer)
						def prevScoreElem = roundPlayer.select('a.scores-draw-entry-box-score')
						setScoreParams(matches[prevMatchNum2], prevScoreElem, name2)
						def seedEntry1 = seedEntries[name1]
						def seedEntry2 = seedEntries[name2]
						def seed1 = seedEntry1?.seed
						def seed2 = seedEntry2?.seed
						def entry1 = seedEntry1?.entry
						def entry2 = seedEntry2?.entry

						params = [:]
						params.ext_tournament_id = string extId
						params.match_num = matchNum
						params.prev_match_num1 = prevMatchNum1
						params.prev_match_num2 = prevMatchNum2
						params.date = startDate
						params.surface = surface
						params.indoor = indoor
						params.round = round
						params.best_of = bestOf

						params.player1_name = name1
						params.player1_seed = seed1
						params.player1_entry = entry1

						params.player2_name = name2
						params.player2_seed = seed2
						params.player2_entry = entry2

						setScoreParams(params)

						matches[matchNum] = params

						if (verbose)
							println "${seed1 ?: ''}${entry1 ?: ''} $name1 vs ${seed2 ?: ''}${entry2 ?: ''} $name2"
					}
				}
			}
			drawRowSpan *= 2
			prevMatchNumOffset = matchNumOffset
		}

		loadStats(matches.values(), 'p1_', 'p2_')

		def matchCount = 0
		if (matches) {
			matchCount = saveMatches(extId, matches)
			if (matchCount > 0)
				println "${matches.size()} matches loaded in $stopwatch"
			else
				println 'Matches not changed'
		}
		else
			println 'No matches found'

		sql.commit()
		matchCount
	}

	static extractEntryPlayer(Element entryMatch, int index) {
		def playerBox = entryMatch.select("tbody > tr:nth-child($index) > td:nth-child(3)")
		def name = playerBox.select("*.scores-draw-entry-box-players-item").text()
		if (!name) {
			name = playerBox.text()
			if (isBye(name))
				return null
		}
		emptyToNull(player(name))
	}

	static extractPlayer(Element roundPlayer) {
		emptyToNull(player(roundPlayer.select('*.scores-draw-entry-box-players-item').text()))
	}

	def setScoreParams(Map params, Elements scoreElem = null, String winnerName = null) {
		if (scoreElem) {
			def score = fitScore scoreElem.html().replace('<sup>', '(').replace('</sup>', ')')
			def matchScore = MatchScoreParser.parse(score)
			if (winnerName == params.player1_name) {
				params.winner = (short) 1
				if (matchScore) {
					def conn = sql.connection
					params.p1_sets = matchScore.w_sets
					params.p2_sets = matchScore.l_sets
					params.p1_games = matchScore.w_games
					params.p2_games = matchScore.l_games
					params.p1_tbs = matchScore.w_tbs
					params.p2_tbs = matchScore.l_tbs
					params.p1_set_games = shortArray(conn, matchScore.w_set_games)
					params.p2_set_games = shortArray(conn, matchScore.l_set_games)
					params.p1_set_tb_pt = shortArray(conn, matchScore.w_set_tb_pt)
					params.p2_set_tb_pt = shortArray(conn, matchScore.l_set_tb_pt)
				}
			}
			else if (winnerName == params.player2_name) {
				params.winner = (short) 2
				if (matchScore) {
					def conn = sql.connection
					params.p1_sets = matchScore.l_sets
					params.p2_sets = matchScore.w_sets
					params.p1_games = matchScore.l_games
					params.p2_games = matchScore.w_games
					params.p1_tbs = matchScore.l_tbs
					params.p2_tbs = matchScore.w_tbs
					params.p1_set_games = shortArray(conn, matchScore.l_set_games)
					params.p2_set_games = shortArray(conn, matchScore.w_set_games)
					params.p1_set_tb_pt = shortArray(conn, matchScore.l_set_tb_pt)
					params.p2_set_tb_pt = shortArray(conn, matchScore.w_set_tb_pt)
				}
			}
			params.score = matchScore?.toString()
			params.outcome = matchScore?.outcome
			params.statsUrl = matchStatsUrl(scoreElem.first().attr('href'))
		}
	}

	static tournamentUrl(String urlId, extId, Integer season) {
		def type = !season ? 'current' : 'archive'
		def seasonStr = !season ? '' : "/$season"
		"http://www.atpworldtour.com/en/scores/$type/$urlId/$extId$seasonStr/draws"
	}

	static mapRound(String round, List<String> rounds) {
		switch (round) {
			case '1st Rd':
			case '2nd Rd':
				int pos = rounds.indexOf(round)
				for (int i = pos + 1; i < rounds.size(); i++) {
					int j = KOResult.values().findIndexOf { v -> v.name() == rounds[i]}
					if (j >= 0)
						return KOResult.values()[j].offset(pos - i).name()
				}
		}
		return round
	}

	
	// Tournament Forecast

	def forecastTournament(extId, boolean verbose) {
		if (verbose)
			println '\nStarting tournament forecast'
		def stopwatch = Stopwatch.createStarted()
		def matches = fetchMatches(extId)

		// Set qualifier ids as different negative numbers
		int qualifierIndex
		matches.each { match ->
			if (!match.player1_id && match.player1_entry == 'Q')
				match.player1_id = -(++qualifierIndex)
			if (!match.player2_id && match.player2_entry == 'Q')
				match.player2_id = -(++qualifierIndex)
		}

		def firstMatch = matches[0]
		def inProgressEventId = firstMatch.in_progress_event_id
		def today = LocalDate.now()
		def surface = Surface.decode(firstMatch.surface)
		def level = TournamentLevel.decode(firstMatch.level)
		def tournamentId = firstMatch.tournament_id
		def bestOf = firstMatch.best_of
		def drawType = firstMatch.draw_type
		def entryResult = KOResult.valueOf(matches[0].round)

		MatchPredictionService predictionService = new MatchPredictionService(new NamedParameterJdbcTemplate(SqlPool.dataSource()))
		TournamentMatchPredictor predictor = new TournamentMatchPredictor(predictionService, today, tournamentId, inProgressEventId, surface, false, level, bestOf)

		def resultCount = 0
		def tournamentForecaster
		if (drawType == 'KO') {

			// Current state forecast
			if (verbose)
				println 'Current'
			tournamentForecaster = new KOTournamentForecaster(predictor, inProgressEventId, matches, entryResult, true, false, verbose)
			tournamentForecaster.calculateEloRatings(eloSurfaceFactors)
			saveEloRatings(matches)
			sql.commit()

			deleteResults(inProgressEventId)
			def results = tournamentForecaster.forecast()
			saveResults(results)
			resultCount += results.size()

			// Each round state forecast
			for (baseResult in KOResult.values().findAll { r -> r >= entryResult && r < KOResult.W }) {
				if (verbose)
					println baseResult
				def selectedMatches = matches.findAll { match -> KOResult.valueOf(match.round) >= baseResult }
				tournamentForecaster = new KOTournamentForecaster(predictor, inProgressEventId, selectedMatches, baseResult, false, baseResult == entryResult, verbose)
				results = tournamentForecaster.forecast()
				saveResults(results)
				resultCount += results.size()
				if (selectedMatches.find { match -> KOResult.valueOf(match.round) == baseResult && !match.winner })
					break
			}
		}
		else
			throw new UnsupportedOperationException("Draw type $drawType is not supported.")

		println "Tournament forecast: ${resultCount} results loaded in $stopwatch"
	}

	def saveEvent(Map params) {
		sql.executeUpdate(params, LOAD_EVENT_SQL)
	}

	def saveMatches(extId, matches) {
		def matchesHash = Hashing.murmur3_128().newHasher().putString(matches.toString(), Charsets.UTF_8).hash().toString()
		def oldMatchesHash = forceForecast ? null : sql.firstRow([extId: string(extId)], FETCH_EVENT_HASH_SQL).matches_hash
		if (matchesHash != oldMatchesHash) {
			sql.withBatch(LOAD_MATCH_SQL) { ps ->
				matches.values().each { match ->
					ps.addBatch(match)
				}
			}
			sql.executeUpdate([extId: string(extId), matchesHash: matchesHash], UPDATE_EVENT_HASH_SQL)
			sql.commit()
			matches.size()
		}
		else
			0
	}

	def fetchMatches(extId) {
		sql.rows([extId: string(extId)], FETCH_MATCHES_SQL)
	}

	def saveEloRatings(matches) {
		sql.withBatch(UPDATE_MATCH_ELO_RATINGS_SQL) { ps ->
			matches.each { match ->
				ps.addBatch(match)
			}
		}
	}

	def deleteResults(int inProgressEventId) {
		sql.execute([inProgressEventId: inProgressEventId], DELETE_PLAYER_PROGRESS_RESULTS_SQL)
	}

	def saveResults(results) {
		sql.withBatch(LOAD_PLAYER_RESULT_SQL) { ps ->
			results.each { result ->
				ps.addBatch(result)
			}
		}
	}


	// Maintenance

	def findInProgressEventExtIds() {
		sql.rows(SELECT_EVENT_EXT_IDS_SQL).collect { row -> row.ext_tournament_id }
	}

	def completeInProgressEventExtIds(Collection extIds) {
		sql.withBatch(COMPLETE_EVENT_SQL) { ps ->
			extIds.each { extId ->
				ps.addBatch([extId: extId])
			}
		}
	}
}