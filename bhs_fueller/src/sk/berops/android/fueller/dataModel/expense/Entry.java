package sk.berops.android.fueller.dataModel.expense;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Record;

public abstract class Entry extends Record implements Comparable<Entry> {
	private int dynamicId;
	
	@Element(name="mileage")
	private double mileage;
	@Element(name="mileageSI")
	private double mileageSI;
	@Element(name="eventDate")
	private Date eventDate;
	@Element(name="cost")
	private double cost;
	@Element(name="expenseType")
	private ExpenseType expenseType;
	
	public int compareTo(Entry e) {
		return Double.valueOf(this.getMileage()).compareTo(Double.valueOf(e.getMileage()));
	}
	public int getDynamicId() {
		return dynamicId;
	}
	public void setDynamicId(int dynamicId) {
		this.dynamicId = dynamicId;
	}
	public double getMileage() {
		return mileage;
	}
	public void setMileage(double mileage) {
		this.mileage = mileage;
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
	public void setCost(double cost) {
		this.cost = cost;
	}
	public ExpenseType getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}
	public enum ExpenseType{
		FUEL("fuel"), 
		MAINTENANCE("maintenance"), //any maintenance action
		SERVICE("service"), //tow service
		FEE("fee"); //highway fee, vignette...
		private String value;	
		ExpenseType(String value) {
			this.setValue(value);
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	public double getMileageSI() {
		return mileageSI;
	}
	public void setMileageSI(double mileageSI) {
		this.mileageSI = mileageSI;
	}
}
