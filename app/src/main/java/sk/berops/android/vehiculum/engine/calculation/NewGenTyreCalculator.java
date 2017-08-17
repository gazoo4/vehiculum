package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.TyreChangeEntry;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenTyreCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof TyreChangeEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-maintenance entry");
			return;
		}

		TyreChangeEntry tEntry = (TyreChangeEntry) entry;

		NewGenTyreConsumption prevC = (previous == null) ? null : (NewGenTyreConsumption) previous.getConsumption();
		NewGenTyreConsumption nextC = tEntry.getTyreConsumption();

		nextC.setTyreCost(calculateTyreCost(prevC, tEntry));
		nextC.setMaterialCost(calculateMaterialCost(prevC, tEntry));
		nextC.setLaborCost(calculateLaborCost(prevC, tEntry));
	}

	private HashMap<Tyre.Season, Cost> calculateTyreCost(NewGenTyreConsumption prevC, TyreChangeEntry entry) {
		HashMap<Tyre.Season, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTyreCost());

		for (Tyre t: entry.getBoughtTyres()) {
			Tyre.Season season = t.getSeason();
			Cost cost = (result.get(season) == null) ? new Cost() : result.get(season);
			result.put(season, Cost.add(cost, t.getCost()));
		}

		return result;
	}

	private Cost calculateMaterialCost(NewGenTyreConsumption prevC, TyreChangeEntry entry) {
		Cost cost = (prevC == null) ? new Cost() : prevC.getMaterialCost();
		return Cost.add(cost, entry.getExtraMaterialCost());
	}

	private Cost calculateLaborCost(NewGenTyreConsumption prevC, TyreChangeEntry entry) {
		Cost cost = (prevC == null) ? new Cost() : prevC.getLaborCost();
		return Cost.add(cost, entry.getLaborCost());
	}
}
