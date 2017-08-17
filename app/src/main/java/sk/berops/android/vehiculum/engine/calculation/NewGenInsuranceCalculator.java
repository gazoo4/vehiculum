package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenInsuranceCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof InsuranceEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-Insurance entry");
			return;
		}

		InsuranceEntry iEntry = (InsuranceEntry) entry;

		NewGenInsuranceConsumption prevC = (previous == null) ? null : (NewGenInsuranceConsumption) previous.getConsumption();
		NewGenInsuranceConsumption nextC = iEntry.getInsuranceConsumption();

		nextC.setTotalITypeCost(calculateTotalITypeCost(prevC, iEntry));
	}

	private HashMap<InsuranceEntry.Type, Cost> calculateTotalITypeCost(NewGenInsuranceConsumption prevC, InsuranceEntry entry) {
		HashMap<InsuranceEntry.Type, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalITypeCost());

		InsuranceEntry.Type type = entry.getType();
		Cost cost = new Cost(result.get(type));
		result.put(type, Cost.add(cost, entry.getCost()));

		return result;
	}
}
