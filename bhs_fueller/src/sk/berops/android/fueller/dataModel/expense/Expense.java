package sk.berops.android.fueller.dataModel.expense;

import java.util.Date;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Record;

public class Expense extends Record {

	@Element(name="eventDate")
	private Date eventDate;
	@Element(name="cost")
	private double cost;
	@Element(name="costSI", required=false)
	private double costSI;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	
	protected Car car;
	
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
		setCostSI(Currency.convertToSI(getCost(), getCurrency(), getEventDate()));
	}
	public double getCostSI() {
		return costSI;
	}

	public void setCostSI(double costSI) {
		this.costSI = costSI;
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
