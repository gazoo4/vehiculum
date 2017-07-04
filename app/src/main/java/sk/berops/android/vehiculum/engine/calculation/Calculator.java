package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

public class Calculator {
	public static void calculateAll(Collection<Entry> entries) {

		HashMap<Entry.ExpenseType, LinkedList<Entry>> catLists = new HashMap<>();
		Entry.ExpenseType key;

		for (Entry e: entries) {
			key = e.getExpenseType();
			// Initialize the lists
			if (catLists.get(key) == null) {
				catLists.put(key, new LinkedList<>());
			}
			// Sort entries based on the ExpenseType
			catLists.get(key).add(e);
		}

		Entry initEntry;
		Entry oldEntry;
		Entry newEntry;
		// Serve the entries filtered by type into the calculation method
		for (Entry.ExpenseType type: catLists.keySet()) {
			initEntry = catLists.get(type).getFirst();
			newEntry = null;
			for (Entry e: catLists.get(type)) {
				oldEntry = newEntry;
				newEntry = e;
				calculateNext(initEntry, oldEntry, newEntry);
			}
		}
	}

	/**
	 *
	 * @param previous
	 * @param next
	 */
	public static void calculateNext(Entry init, Entry previous, Entry next) {

		// Perform initial checks
		if (init == null
				|| next == null
				|| previous.getConsumption() == null
				|| ! init.getExpenseType().equals(next.getExpenseType())
				|| ! previous.getExpenseType().equals(next.getExpenseType())) {
			Log.d(Calculator.class.toString(), "Initial checks failed in the calculateNext method");
			return;
		}

		Consumption prevC;
		Consumption nextC;

		if (previous == null) {
			prevC = null;
		} else if (previous.getConsumption() == null) {
			Log.d(Calculator.class.toString(), "Detected first Entry in a History with no Consumption data.");
			prevC = null;
		} else {
			prevC = previous.getConsumption();
		}
		nextC = next.getConsumption();

		// Total Cost
		Cost previousCost = (previous == null) ? (null) : prevC.getTotalCost();
		Cost total = Cost.add(previousCost, next.getCost());
		nextC.setTotalCost(total);

		// Average Cost
		Cost average = Cost.subtract(total, init.getCost());
		nextC.setAverageCost(average);
	}
}
