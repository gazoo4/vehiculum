package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public abstract class NewGenCalculator {
	public static HashMap<Entry.ExpenseType, NewGenCalculator> typeCalculators = new HashMap<>();

	public static void calculateAll(Car car){
		NewGenEntryCalculator calculator = new NewGenEntryCalculator();
		for (Entry e: car.getHistory().getEntries()) {
			calculator.processNext(e);
			getCalculator(e.getExpenseType()).processNext(e);
		}
	}

	private static NewGenCalculator getCalculator(Entry.ExpenseType type) {
		if (typeCalculators.get(type) == null) {
			typeCalculators.put(type, getNewInstance(type));
		}

		return typeCalculators.get(type);
	}

	private static NewGenCalculator getNewInstance(Entry.ExpenseType type) {
		switch (type) {
			case FUEL: return new NewGenFuelCalculator();
			case MAINTENANCE: return new NewGenMaintenanceCalculator();
			case SERVICE: return new NewGenServiceCalculator();
			case TYRES: return new NewGenTyreCalculator();
			case TOLL: return new NewGenTollCalculator();
			case INSURANCE: return new NewGenInsuranceCalculator();
			case BUREAUCRATIC: return new NewGenBureaucraticCalculator();
			case OTHER: return new NewGenOtherCalculator();
			default:
				Log.w(NewGenEntryCalculator.class.toString(), "Unknown expense type for calculator instantiation");
		}

		return null;
	}

	/**
	 * Reference to the initial entry
	 * Usually necessary for calculating all-time averages
	 */
	protected Entry initial;
	/**
	 * Reference to the previous entry
	 * Typically necessary for incremental calculations
	 */
	protected Entry previous;

	/**
	 * Called for processing the consumption values for the next entry in the pipe
	 * @param entry next entry
	 */
	public void processNext(Entry entry) {
		// Calculate consumption for the entry
		calculateNext(entry);
		// Update the initial and previous pointers
		shiftPointers(entry);
	}

	/**
	 * Method used for calculating the consumption objects.
	 * This has to be overridden by specific entry type calculators (FuelCalculator for FuellingEntries)
	 * as each calculator type calculates individual aspects of the entries.
	 * @param entry
	 */
	public abstract void calculateNext(Entry entry);

	/**
	 * Method used to update the pointers to previous and initial entries for each calculator
	 * @param entry
	 */
	public void shiftPointers(Entry entry) {
		if (initial == null) {
			initial = entry;
		}
		previous = entry;
	}
}
