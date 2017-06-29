package sk.berops.android.vehiculum.dataModel.expense;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.engine.synchronization.controllers.CostController;

/**
 * @author Bernard Halas
 * @date 6/27/17
 *
 * Class that holds all the cost values for various objects that need to record expenses.
 * This class records also the prices in other currencies (if a conversion is done).
 */

public class Cost extends Record {

	/**
	 * Method to provide a sum of costs across multiple Cost objects. The sum is reported only in the currencies,
	 * which are recorded in all Cost objects. E.g. if there's a Cost object that doesn't have the cost listed
	 * in USD, the result will not contain the cost sum in USD either.
	 * @param input collection of Costs across which the sum should be made
	 * @return sum across all the relevant currencies
	 */
	public static Cost sum(Collection<Cost> input) {
		Cost result = new Cost();

		if ((input == null)
				|| (input.size() == 0)) {
			return result;
		}

		// Take the first Cost item in the collection, look at its the currencies and take them as a base.
		input.iterator().next().getValues().keySet()
				.stream()
				.forEach(unit -> result.addCost(unit, 0.0));

		TreeMap<Currency.Unit, Double> values = result.getValues();
		Iterator<Currency.Unit> i = values.keySet().iterator();
		// Iterate through the currencies from the first cost object and sum the costs across all the Cost objects.
		while (i.hasNext()) {
			Currency.Unit unit = i.next();
			try {
				input.stream()
						.forEach(cost -> {
							values.put(unit, values.get(unit) + cost.getValues().get(unit));
						});
			} catch (NullPointerException ex) {
				// If a Cost object doesn't have a record in this specific currency, we can't report the sum for this whole currency.
				// It's OK to remove the key from the keySet, as this is backed by the map and this means that the key with the
				// respective element is removed from the map itself as well.
				i.remove();
			}
		}

		return result;
	}

	/**
	 * Map that holds value in various currencies (after conversion has been done)
	 */
	@ElementMap(inline = true, required = false)
	private TreeMap<Currency.Unit, Double> values;

	/**
	 * Unit that indicates in which currency the cost was recorded by the client
	 */
	@Element(name = "recordUnit", required = false)
	private Currency.Unit recordUnit;

	public Cost() {
		values = new TreeMap<>();
	}

	public Cost(Double value, Currency.Unit unit) {
		this();
		setRecordUnit(unit);
		values.put(unit, value);
	}

	public Double getCost(Currency.Unit unit) {
		if (! values.containsKey(unit)) {
			Log.w(this.getClass().toString(), "Required unit value hasn't been recorded yet.");
		}
		return values.get(unit);
	}

	public void addCost(Currency.Unit unit, Double value) {
		if (values.get(unit) != value) {
			Log.i(this.getClass().toString(), "Updating an existing value. Discarding all previously recorded values.");
			values = new TreeMap<>();
		}

		values.put(unit, value);
	}

	public TreeMap<Currency.Unit, Double> getValues() {
		return values;
	}

	public void setValues(TreeMap<Currency.Unit, Double> values) {
		this.values = values;
	}

	public Currency.Unit getRecordUnit() {
		if (recordUnit == null) {
			recordUnit = Currency.Unit.EUR;
		}
		return recordUnit;
	}

	public void setRecordUnit(Currency.Unit recordUnit) {
		this.recordUnit = recordUnit;
	}

	/**
	 * Override method hashCode
	 * @return hashcode of this object
	 */
	@Override
	public int hashCode() {
		int result = 197 * recordUnit.getId();
		result += values.hashCode();

		return result;
	}

	/**
	 * Overriden method equals for comparing 2 Records
	 * @param obj
	 * @return true is objects are equal. Otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!Cost.class.isAssignableFrom(obj.getClass())) {
			return false;
		}

		final Cost other = (Cost) obj;
		if ((this.values == null) ? (other.values != null) : (! this.values.equals(other.values))) {
			return false;
		}
		if ((this.recordUnit == null) ? (other.recordUnit != null) : (! this.recordUnit.equals(other.recordUnit))) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public int compareTo(Cost other) {
		HashSet<Currency.Unit> units = new HashSet<>();
		units.add(Currency.Unit.EUR);
		units.add(recordUnit);
		units.add(other.recordUnit);

		for (Currency.Unit u: units) {
			Double value1 = values.get(u);
			Double value2 = other.values.get(u);
			if ((value1 != null)
				&& (value2 != null)) {
				return value1.compareTo(value2);
			}
		}

		Log.w(this.getClass().toString(), "Cost comparison failure");
		throw new NullPointerException("Unable to find non-null values for comparison");
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public CostController getController() {
		return new CostController(this);
	}
}