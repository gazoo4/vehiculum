package sk.berops.android.vehiculum.dataModel.expense;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.BiFunction;

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
	 * @return a new Cost object that holds the sum across all the relevant currencies
	 */
	public static Cost sum(Collection<Cost> input) {
		Cost result = new Cost();

		if (input == null
				|| input.size() == 0) {
			return result;
		}

		// Take the first Cost item in the collection, look at its the currencies and take them as a base.
		for (Currency.Unit unit: input.iterator().next().getValues().keySet()) {
				result.addCost(unit, 0.0);
		}

		Iterator<Cost> i = input.iterator();
		while (i.hasNext()) {
			result = add(result, i.next());
		}

		return result;
	}

	/**
	 * Method to add two Cost objects together and return the result in a new Cost object.
	 * RecordedUnit gets preserved if it's identical in both Cost objects.
	 * @param c1
	 * @param c2
	 * @return a new Cost object
	 */
	public static Cost add(Cost c1, Cost c2) {
		if (c1 == null && c2 == null) return new Cost();

		if (c1 == null || c1.isZero()) return new Cost(c2);
		if (c2 == null || c2.isZero()) return new Cost(c1);

		return executeBinary(c1, c2, (a, b) -> a + b);
	}

	/**
	 * Method to subtract two Cost objects from each other and return the result in a new Cost object.
	 * RecordedUnit gets preserved if it's identical in both Cost objects.
	 * @param c1
	 * @param c2
	 * @return a new Cost object
	 */
	public static Cost subtract(Cost c1, Cost c2) {
		if (c1 == null || c1.isZero()) return multiply(c2, -1.0);
		if (c2 == null || c2.isZero()) return c1;

		return executeBinary(c1, c2, (a, b) -> a - b);
	}

	/**
	 * Method to multiply the Cost object by double number and return that in a new Cost object
	 * @param cost
	 * @param multiplier
	 * @return a new Cost object
	 */
	public static Cost multiply(Cost cost, double multiplier) {
		if (cost == null) return new Cost();

		return executeUnary(cost, multiplier, (a, b) -> a * b);
	}

	/**
	 * Method to divide the Cost object by double number and return that in a new Cost object
	 * @param cost
	 * @param divisor
	 * @return a new Cost object
	 */
	public static Cost divide(Cost cost, double divisor) {
		if (cost == null) return new Cost();

		return executeUnary(cost, divisor, (a, b) -> {
			if (b == 0) {
				return Double.NaN;
			} else {
				return a / b;
			}
		});
	}

	public static Cost executeBinary(Cost c1, Cost c2, BiFunction<Double, Double, Double> operation) {
		Cost result = new Cost();
		if (c1.getRecordUnit().equals(c2.getRecordUnit())) {
			result.setRecordUnit(c1.getRecordUnit());
		}

		// If the cost is completely empty, initialize it to zeroes and match the other cost currencies
		if (c1.getValues().keySet().size() == 0) {
			for (Currency.Unit unit: c2.getValues().keySet()) {
				c1.addCost(unit, 0.0);
			}
		}

		c1.getValues().keySet().stream()
				// Proceed only if Cost c2 has a record for the same currency
				.filter(unit -> c2.getValues().keySet().contains(unit))
				.forEach(unit -> result.addCost(unit, operation.apply(c1.getCost(unit), c2.getCost(unit))));

		return result;
	}

	public static Cost executeUnary(Cost cost, double x, BiFunction<Double, Double, Double> operation) {
		Cost result = new Cost();
		result.setRecordUnit(cost.getRecordUnit());

		cost.getValues().keySet().stream()
				.forEach(unit -> result.addCost(unit, operation.apply(cost.getCost(unit), x)));

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

	public Cost(Cost cost) {
		this();

		if (cost == null) {
			return;
		}

		setRecordUnit(cost.getRecordUnit());
		for (Currency.Unit u: cost.getValues().keySet()) {
			values.put(u, cost.getCost(u));
		}
	}

	/**
	 * Method to check if this Cost instance is of 0 value
	 * @return true if it's of 0 value for all units  or if it has no recorded values for any unit
	 */
	public boolean isZero() {
		if (values.keySet().size() == 0) return true;

		for (Currency.Unit unit: values.keySet()) {
			if (values.get(unit) != 0.0) {
				// Values contain non-zero entry
				return false;
			}
		}

		return true;
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