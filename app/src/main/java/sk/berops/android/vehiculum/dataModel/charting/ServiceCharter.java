package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import sk.berops.android.vehiculum.engine.calculation.NewGenServiceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class ServiceCharter extends PieCharter {

	private NewGenServiceConsumption sConsumption;

	public ServiceCharter(NewGenServiceConsumption c) {
		sConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		sConsumption.getTotalSTypeCost().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> sConsumption.getTotalSTypeCost().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}
