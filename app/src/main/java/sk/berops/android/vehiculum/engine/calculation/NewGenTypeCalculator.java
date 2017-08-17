package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 8/14/17
 */

public class NewGenTypeCalculator extends NewGenCalculator {

	@Override
	public void calculateNext(Entry entry) {
		if (entry == null) {
			Log.w(this.getClass().toString(), "Can't run calculation for null entry");
			return;
		}

		NewGenConsumption prevC = (previous == null) ? null : previous.getConsumption();
		NewGenConsumption nextC = entry.getConsumption();

		// Total type cost
		Cost total = (prevC == null) ? new Cost() : prevC.getTotalTypeCost();
		total = Cost.add(total, entry.getCost());
		nextC.setTotalTypeCost(total);

		// Average type cost
		if (initial == null) {
			nextC.setAverageTypeCost(new Cost());
		} else {
			Double mileage = entry.getMileageSI() - initial.getMileageSI();
			Cost average = Cost.divide(total, mileage);
			nextC.setAverageTypeCost(average);
		}

		// Type count
		int count = (prevC == null) ? 0 : prevC.getTypeCount();
		nextC.setTypeCount(count + 1);
	}
}
