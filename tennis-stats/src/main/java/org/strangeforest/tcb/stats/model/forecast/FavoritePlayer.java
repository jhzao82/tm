package org.strangeforest.tcb.stats.model.forecast;

import org.strangeforest.tcb.stats.model.*;
import org.strangeforest.tcb.stats.model.price.*;

import static org.strangeforest.tcb.stats.model.price.PriceUtil.*;

public class FavoritePlayer extends PlayerRow {

	private final double probability;
	private String price;

    public FavoritePlayer(int favorite, int playerId, String name, String chineseName, String countryId, double probability) {
        super(favorite, playerId, name, chineseName, countryId, null);
		this.probability = probability;
	}

    public FavoritePlayer(int favorite, int playerId, String name, String chineseName, String countryId, double probability, PriceFormat priceFormat) {
        this(favorite, playerId, name, chineseName, countryId, probability);
		price = priceFormat != null ? priceFormat.format(toPrice(probability)) : null;
	}

	public double getProbability() {
		return probability;
	}

	public String getPrice() {
		return price;
	}

	public String getPrice(String format) {
		return PriceFormat.valueOf(format).format(toPrice(probability));
	}
}
