package sk.berops.android.vehiculum.dataModel.charting;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieEntry;

import sk.berops.android.vehiculum.engine.calculation.NewGenTyreConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class TyreCharter extends PieCharter {

	private NewGenTyreConsumption tConsumption;

	public TyreCharter(NewGenTyreConsumption c) {
		tConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		if (! tConsumption.getLaborCost().isZero()) {
			colors.add(Color.LTGRAY);
			vals.add(new PieEntry(tConsumption.getLaborCost().getPreferredValue().floatValue()));
		}

		if (! tConsumption.getMaterialCost().isZero()) {
			colors.add(Color.GRAY);
			vals.add(new PieEntry(tConsumption.getMaterialCost().getPreferredValue().floatValue()));
		}

		tConsumption.getTyreCost().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> tConsumption.getTyreCost().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}
