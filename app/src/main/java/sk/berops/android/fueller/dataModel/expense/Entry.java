package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.calculation.Consumption;

public abstract class Entry extends Expense implements Comparable<Entry> {
	private int dynamicId;
	private Consumption consumption;
	private Car car;
	
	@Element(name="mileage")
	private double mileage;
	private double mileageSI;
	@Element(name="expenseType")
	private ExpenseType expenseType;
	
	@Override
	public void generateSI() {
		// this is called just 8 times????
		super.generateSI();
		setMileage(getMileage());
		setCost(getCost(), getCurrency());
	}
	
	public int compareTo(Entry e) {
		return Double.valueOf(this.getMileage()).compareTo(Double.valueOf(e.getMileage()));
	}
	public int getDynamicId() {
		return dynamicId;
	}
	public void setDynamicId(int dynamicId) {
		this.dynamicId = dynamicId;
	}
	public Consumption getConsumption() {
		return consumption;
	}
	public void setConsumption(Consumption consumption) {
		this.consumption = consumption;
	}
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public double getMileage() {
		return mileage;
	}
	public void setMileage(double mileage) {
		this.mileage = mileage;
		setMileageSI(mileage * car.getDistanceUnit().getCoef());
	}
	public ExpenseType getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}
	public enum ExpenseType{
		FUEL(0, "fuel"), //fossil fuels, electricity
		MAINTENANCE(1, "maintenance"), //any maintenance action
		SERVICE(2, "service"), //tow service, replacement car service
		TYRES(3, "tyres"),
		TOLL(4, "toll"), //ferry fee, highway vignette...
		INSURANCE(5, "insurance"), //basic insurance or extended insurance
		BURREAUCRATIC(6, "burreaucratic"), //technical compliancy check fee (TUEV), import tax, eco class tax,...
		OTHER(Integer.MAX_VALUE, "other");
		
		private int id;
		private String expenseType;	
		ExpenseType(int id, String expenseType) {
			this.setId(id);
			this.setExpenseType(expenseType);
		}
		
		private static Map<Integer, ExpenseType> idToExpenseTypeMapping;

		public static ExpenseType getExpenseType(int id) {
			if (idToExpenseTypeMapping == null) {
				initMapping();
			}
			
			ExpenseType result = null;
			result = idToExpenseTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToExpenseTypeMapping = new HashMap<Integer, ExpenseType>();
			for (ExpenseType expenseType : values()) {
				idToExpenseTypeMapping.put(expenseType.id, expenseType);
			}
		}
	
		public String getExpenseType() {
			return expenseType;
		}
		public void setExpenseType(String expenseType) {
			this.expenseType = expenseType;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	public double getMileageSI() {
		return mileageSI;
	}
	public void setMileageSI(double mileageSI) {
		this.mileageSI = mileageSI;
	}
}
