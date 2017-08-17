package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;

/**
 * @author Bernard Halas
 * @date 8/15/17
 */

public class NewGenMaintenanceCalculator extends NewGenTypeCalculator {

	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof MaintenanceEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-maintenance entry");
			return;
		}

		MaintenanceEntry mEntry = (MaintenanceEntry) entry;

		NewGenMaintenanceConsumption prevC = (previous == null) ? null : (NewGenMaintenanceConsumption) previous.getConsumption();
		NewGenMaintenanceConsumption nextC = mEntry.getMaintenanceConsumption();

		nextC.setTotalMTypeCost(calculateTotalMTypeCost(prevC, mEntry));
		nextC.setTotalLaborCost(calculateTotalLaborCost(prevC, mEntry));
		nextC.setTotalPartsCost(calculateTotalPartsCost(prevC, mEntry));
	}

	private HashMap<MaintenanceEntry.Type, Cost> calculateTotalMTypeCost(NewGenMaintenanceConsumption prevC, MaintenanceEntry entry) {
		HashMap<MaintenanceEntry.Type, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalMTypeCost());

		MaintenanceEntry.Type type = entry.getType();
		Cost cost = new Cost(result.get(type));
		cost = Cost.add(cost, entry.getCost());
		result.put(type, cost);

		return result;
	}

	private Cost calculateTotalLaborCost(NewGenMaintenanceConsumption prevC, MaintenanceEntry entry) {
		Cost cost = (prevC == null) ? new Cost() : prevC.getTotalLaborCost();
		cost = Cost.add(cost, entry.getLaborCost());
		return cost;
	}

	private Cost calculateTotalPartsCost(NewGenMaintenanceConsumption prevC, MaintenanceEntry entry) {
		Cost cost = (prevC == null) ? new Cost() : prevC.getTotalPartsCost();
		cost = Cost.add(cost, entry.getPartsCost());
		return cost;
	}
}
