package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenEntryCalculator extends NewGenCalculator {
	public static HashMap<Entry.ExpenseType, NewGenCalculator> typeCalculators = new HashMap<>();

	public static void calculateAll(Collection<Entry> entries){
		NewGenEntryCalculator calculator = new NewGenEntryCalculator();
		for (Entry e: entries) {
			calculator.calculateNext(e);
			getCalculator(e.getExpenseType()).calculateNext(e);
		}
	}

	public static NewGenCalculator getCalculator(Entry.ExpenseType type) {
		if (typeCalculators.get(type) == null) {
			typeCalculators.put(type, getNewInstance(type));
		}

		return typeCalculators.get(type);
	}

	public static NewGenCalculator getNewInstance(Entry.ExpenseType type) {
		switch (type) {
			case FUEL: return new NewGenFuelCalculator();
			case MAINTENANCE:
				break;
			case SERVICE:
				break;
			case TYRES:
				break;
			case TOLL:
				break;
			case INSURANCE:
				break;
			case BUREAUCRATIC:
				break;
			case OTHER:
				break;
		}

		return null;
	}

	@Override
	public void calculateNext(Entry entry) {
		if (entry == null) {
			Log.w(this.getClass().toString(), "Can't run calculation for null entry");
			return;
		}

		NewGenConsumption prevC = (previous == null) ? null : previous.getConsumption();
		NewGenConsumption nextC = entry.getConsumption();

		// Total cost
		Cost total = (prevC == null) ? new Cost() : prevC.getTotalCost();
		total = Cost.add(total, entry.getCost());
		nextC.setTotalCost(total);

		// Average cost
		if (initial == null) {
			initial = entry;
			nextC.setAverageCost(new Cost());
		} else {
			Double mileage = entry.getMileageSI() - initial.getMileageSI();
			Cost average = Cost.divide(total, mileage);
			nextC.setAverageCost(average);
		}

		previous = entry;
	}
}
