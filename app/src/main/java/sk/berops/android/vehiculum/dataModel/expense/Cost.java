package sk.berops.android.vehiculum.dataModel.expense;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.Currency;

/**
 * @author Bernard Halas
 * @date 6/27/17
 *
 * Class that holds all the cost values for various objects that need to record expenses.
 * This class records also the prices in other currencies (if a conversion is done).
 */

public class Cost {
	/**
	 * Map that holds value in various currencies (after conversion has been done)
	 */
	@ElementMap(inline = true, required = false)
	private TreeMap<Currency.Unit, Double> costs;

	/**
	 * Unit that indicates in which currency the cost was recorded by the client
	 */
	@Element(name = "recordUnit", required = false)
	private Currency.Unit recordUnit;

	public Cost() {
		costs = new TreeMap<>();
	}

	public Cost(Double value, Currency.Unit unit) {
		this();
		setRecordUnit(unit);
		getCosts().put(unit, value);
	}

	public Double getCost(Currency.Unit unit) {
		if (! costs.containsKey(unit)) {
			Log.w(this.getClass().toString(), "Required unit value hasn't been recorded yet.");
		}
		return costs.get(unit);
	}

	public void addCost(Double value, Currency.Unit unit) {
		if (costs.get(unit) != value) {
			Log.i(this.getClass().toString(), "Updating an existing value. Discarding all previously recorded values.");
			costs = new TreeMap<>();
		}

		costs.put(unit, value);
	}

	public TreeMap<Currency.Unit, Double> getCosts() {
		return costs;
	}

	public void setCosts(TreeMap<Currency.Unit, Double> costs) {
		this.costs = costs;
	}

	public Currency.Unit getRecordUnit() {
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
		result += costs.hashCode();

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
		if ((this.costs == null) ? (other.costs != null) : (! this.costs.equals(other.costs))) {
			return false;
		}
		if ((this.recordUnit == null) ? (other.recordUnit != null) : (! this.recordUnit.equals(other.recordUnit))) {
			return false;
		}
		return true;
	}

	public int compareTo(Cost other) {
		HashSet<Currency.Unit> units = new HashSet<>();
		units.add(Currency.Unit.EUR);
		units.add(recordUnit);
		units.add(other.recordUnit);

		for (Currency.Unit c: units) {
			if ()
		}
		return 0;
	}
}