package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.TollEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenTollCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof TollEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-Toll entry");
			return;
		}

		TollEntry tEntry = (TollEntry) entry;

		NewGenTollConsumption prevC = (previous == null) ? null : (NewGenTollConsumption) previous.getConsumption();
		NewGenTollConsumption nextC = tEntry.getTollConsumption();

		nextC.setTotalTTypeCost(calculateTotalTTypeCost(prevC, tEntry));
	}

	private HashMap<TollEntry.Type, Cost> calculateTotalTTypeCost(NewGenTollConsumption prevC, TollEntry entry) {
		HashMap<TollEntry.Type, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalTTypeCost());

		TollEntry.Type type = entry.getType();
		Cost cost = new Cost(result.get(type));
		result.put(type, Cost.add(cost, entry.getCost()));

		return result;
	}
}
