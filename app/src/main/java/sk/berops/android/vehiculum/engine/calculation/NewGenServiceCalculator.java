package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenServiceCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof ServiceEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-Service entry");
			return;
		}

		ServiceEntry sEntry = (ServiceEntry) entry;

		NewGenServiceConsumption prevC = (previous == null) ? null : (NewGenServiceConsumption) previous.getConsumption();
		NewGenServiceConsumption nextC = sEntry.getServiceConsumption();

		nextC.setTotalSTypeCost(calculateTotalSTypeCost(prevC, sEntry));
	}

	private HashMap<ServiceEntry.Type, Cost> calculateTotalSTypeCost(NewGenServiceConsumption prevC, ServiceEntry entry) {
		HashMap<ServiceEntry.Type, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalSTypeCost());

		ServiceEntry.Type type = entry.getType();
		Cost cost = new Cost(result.get(type));
		result.put(type, Cost.add(cost, entry.getCost()));

		return result;
	}
}
