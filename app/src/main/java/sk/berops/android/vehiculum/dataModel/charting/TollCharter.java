package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import sk.berops.android.vehiculum.engine.calculation.NewGenTollConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class TollCharter extends Charter {

	private NewGenTollConsumption tConsumption;

	public TollCharter(NewGenTollConsumption c) {
		tConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		tConsumption.getTotalTTypeCost().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> tConsumption.getTotalTTypeCost().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}
