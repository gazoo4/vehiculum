package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import sk.berops.android.vehiculum.engine.calculation.NewGenInsuranceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class InsuranceCharter extends PieCharter {

	NewGenInsuranceConsumption iConsumption;

	public InsuranceCharter(NewGenInsuranceConsumption c) {
		iConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		iConsumption.getTotalITypeCost().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> iConsumption.getTotalITypeCost().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}
