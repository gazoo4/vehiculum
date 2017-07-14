package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

public class Calculator {
	private static Calculator instance;
	private Entry initEntry;
	private Entry previousEntry;
	private static Object mutex1 = new Object();

	private Calculator() {
		initEntry = null;
		previousEntry = null;
	}

	public static Calculator getInstance() {
		if (instance == null) {
			synchronized (mutex1) {
				if (instance == null) {
					instance = new Calculator();
				}
			}
		}

		return instance;
	}

	public void setInitEntry(Entry e) {
		initEntry = e;
	}

	public void setPreviousEntry(Entry e) {
		previousEntry = e;
	}

	public void calculateNext(Entry e) {
		if (e == null) {
			Log.e(this.getClass().toString(), "Can't run calculation for null entry");
			return;
		}

		Consumption prevC = (previousEntry == null) ? null : previousEntry.getConsumption();
		Consumption nextC = e.getConsumption();

		// Total cost
		Cost total = (prevC == null) ? new Cost() : prevC.getTotalCost();
		total = Cost.add(total, e.getCost());
		nextC.setTotalCost(total);

		// Average cost
		if (initEntry == null) {
			initEntry = e;
		} else {
			Double mileage = e.getMileageSI() - initEntry.getMileageSI();
			Cost average = Cost.divide(total, mileage);
			nextC.setAverageCost(average);
		}

		previousEntry = e;


	}
}
