package org.strangeforest.tcb.stats.model.prediction;

public enum RankingPredictionItem implements PredictionItem {

	RANK(0.0),
	RANK_POINTS(1.0),
	ELO(1.5),
	SURFACE_ELO(2.0);

	private volatile PredictionArea area;
	private volatile double weight;

	RankingPredictionItem(double weight) {
		this.weight = weight;
	}

	@Override public PredictionArea getArea() {
		return area;
	}

	@Override public void setArea(PredictionArea area) {
		this.area = area;
	}

	@Override public double getWeight() {
		return weight;
	}

	@Override public void setWeight(double weight) {
		this.weight = weight;
		area.calculateItemAdjustmentWeight();
	}

	@Override public String toString() {
		return area + "[" + super.toString() + "]";
	}
}
