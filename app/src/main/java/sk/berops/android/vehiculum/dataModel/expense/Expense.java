package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.Date;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.Record;

public class Expense extends Record {

	@Element(name="eventDate", required=false)
	private Date eventDate;
	@Element(name="cost")
	private double cost;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	/**
	 * Cost of the expense converted to EUR
	 */
	@Element(name="costEur", required=false)
	private double costEur;
	
	protected Car car;

	public Expense() {
		super();
	}

	/**
	 * Copy constructor
	 * @param expense
	 */
	public Expense(Expense expense) {
		super(expense);
		this.eventDate = expense.eventDate;
		this.cost = expense.cost;
		this.currency = expense.currency;
		this.car = expense.car;
	}
	
	public void initAfterLoad(Car car) {
		setCar(car);
		if (getCurrency() == null) {
			setCurrency(Currency.Unit.EURO);
		}
		generateSI();
	}

	/**
	 * Override method hashCode
	 * @return hashcode of this object
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		long temp;
		result = 31 * result + (eventDate != null ? eventDate.hashCode() : 0);
		temp = Double.doubleToLongBits(cost);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (currency != null ? currency.hashCode() : 0);
		return result;
	}

	/**
	 * Overriden method equals for comparing 2 Expenses
	 * @param obj
	 * @return true is objects are equal. Otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		// Basic checks
		if (obj == null) {
			return false;
		}
		if (!Expense.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}

		final Expense other = (Expense) obj;
		if ((this.eventDate == null) ? (other.eventDate != null) : !this.eventDate.equals(other.eventDate)) {
			return false;
		}
		if (this.cost != other.cost) {
			return false;
		}
		if ((this.currency == null) ? (other.currency != null) : !this.currency.equals(other.currency)) {
			return false;
		}
		return true;
	}
	
	public void generateSI() {
		setCost(getCost(), getCurrency());
	}
	
	public Date getEventDate() {
		if (eventDate == null) {
			return getCreationDate();
		}
		return eventDate;
	}
	public void setEventDate(Date date) {
		this.eventDate = date;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost, Currency.Unit currency) {
		this.cost = cost;
		this.currency = currency;
	}
	public double getCostSI() {
		return Currency.convertToSI(getCost(), getCurrency(), getEventDate());
	}

	public Currency.Unit getCurrency() {
		return currency;
	}

	public void setCurrency(Currency.Unit currency) {
		this.currency = currency;
	}
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void setCostEur(double costEur) {
		this.costEur = costEur;
	}

	public double getCostEur() {
		return this.costEur;
	}
}
