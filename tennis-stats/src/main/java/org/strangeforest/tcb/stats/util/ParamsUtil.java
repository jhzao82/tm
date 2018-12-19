package org.strangeforest.tcb.stats.util;

import java.sql.*;

import org.springframework.jdbc.core.namedparam.*;

import com.google.common.collect.*;

public abstract class ParamsUtil {

	public static MapSqlParameterSource params(String name, Object value) {
		return new MapSqlParameterSource(name, value);
	}

	public static <T extends Comparable> void addRangeParams(MapSqlParameterSource params, Range<T> range, String name) {
		if (range.hasLowerBound())
			params.addValue(name + "From", range.lowerEndpoint());
		if (range.hasUpperBound())
			params.addValue(name + "To", range.upperEndpoint());
	}

	public static void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
		if (value != null)
			ps.setInt(index, value);
		else
			ps.setNull(index, Types.INTEGER);
	}
}
