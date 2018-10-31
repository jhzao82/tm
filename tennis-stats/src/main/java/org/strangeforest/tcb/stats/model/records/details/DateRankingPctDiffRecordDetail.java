package org.strangeforest.tcb.stats.model.records.details;

import java.time.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;

import static java.lang.String.*;

public class DateRankingPctDiffRecordDetail extends BaseDateRankingDiffRecordDetail<String> {

	public DateRankingPctDiffRecordDetail(
            @JsonProperty("value") double value,
            @JsonProperty("player_id2") int playerId2,
            @JsonProperty("name2") String name2,
            @JsonProperty("name2") String chineseName2,
            @JsonProperty("country_id2") String countryId2,
            @JsonProperty("active2") Boolean active2,
            @JsonProperty("value1") int value1,
            @JsonProperty("value2") int value2,
            @JsonProperty("date") @JsonDeserialize(using = LocalDateDeserializer.class) LocalDate date
	) {
        super(format("%1$.1f%%", value), playerId2, name2, chineseName2, countryId2, active2, value1, value2, date);
	}
}
