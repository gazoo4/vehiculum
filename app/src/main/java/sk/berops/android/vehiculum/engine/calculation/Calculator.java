package sk.berops.android.vehiculum.engine.calculation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import sk.berops.android.vehiculum.dataModel.expense.Entry;

public class Calculator {
	public static void calculateAll(Collection<Entry> entries) {
		Entry oldEntry;
		Entry newEntry;
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

		// Serve the entries filtered by type into the calculation method
		for (Entry.ExpenseType type: catLists.keySet()) {
			newEntry = null;
			for (Entry e: catLists.get(type)) {
				oldEntry = newEntry;
				newEntry = e;
				calculateNext(oldEntry, newEntry);
			}
		}
	}

	public static void calculateNext(Entry previous, Entry next) {

		FOCUS ON THINGS HERE

		if (! previous.getExpenseType().equals(next.getExpenseType())) {
			return;
		}
	}
}
