package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.Date;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Record;

public class Expense extends Record {

	@Element(name="eventDate", required=false)
	private Date eventDate;
	@Element(name="cost")
	private double cost;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	
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
}
