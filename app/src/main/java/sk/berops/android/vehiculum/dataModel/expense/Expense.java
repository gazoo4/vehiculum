package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.Date;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.engine.synchronization.controllers.ExpenseController;

public abstract class Expense extends Record {

	@Element(name="eventDate", required=false)
	private Date eventDate;
	@Element(name="cost", required=true)
	private Cost cost;
	
	protected Car car;

	public Expense() {
		super();
		cost = new Cost();
	}

	/**
	 * Copy constructor
	 * @param expense
	 */
	public Expense(Expense expense) {
		super(expense);
		this.eventDate = expense.eventDate;
		this.cost = expense.cost;
		this.car = expense.car;
	}
	
	public void initAfterLoad(Car car) {
		setCar(car);
		generateSI();
	}

	public abstract void generateSI();
	
	public Date getEventDate() {
		if (eventDate == null) {
			return getCreationDate();
		}
		return eventDate;
	}
	public void setEventDate(Date date) {
		this.eventDate = date;
	}
	public Cost getCost() {
		return cost;
	}
	public void setCost(Cost cost) {
		this.cost = cost;
	}
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public ExpenseController getController() {
		return new ExpenseController(this);
	}
}
