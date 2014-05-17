package sk.bhs.android.fueller.dataModel.expense;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

public class FuellingEntry extends Entry {
	@Element(name="fuelVolume")
	private double fuelVolume;
	@Element(name="fuelType")
	private FuelType fuelType;
	@Element(name="fuelPrice")
	private double fuelPrice;
	private double consumption;
	private double floatingConsumption;
	public enum FuelType{
		GASOLINE(0, "gasoline"), 
		LPG(1, "lpg"),
		DIESEL(2, "diesel"),
		CNG(3, "cng"),
		ELECTRICITY(4, "electricity");
		private int id;
		private String type;	
		FuelType(int id, String type) {
			this.setId(id);
			this.setType(type);
		}
		
		private static Map<Integer, FuelType> idToDescriptionMapping;

		public static FuelType getFuelType(int id) {
			if (idToDescriptionMapping == null) {
				initMapping();
			}
			
			FuelType result = null;
			result = idToDescriptionMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToDescriptionMapping = new HashMap<Integer, FuellingEntry.FuelType>();
			for (FuelType ft : values()) {
				idToDescriptionMapping.put(ft.id, ft);
			}
		}
	
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String toString() {
			return getType();
		}
	}
	public int compareTo(FuellingEntry e) {
		return super.compareTo(e);
	}
	public FuellingEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.FUEL);
	}
	public FuelType getFuelType() {
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	public double getFuelVolume() {
		return fuelVolume;
	}
	public void setFuelVolume(double fuelVolume) {
		this.fuelVolume = fuelVolume;
	}
	public double getFuelPrice() {
		return fuelPrice;
	}
	public void setFuelPrice(double fuelPrice) {
		this.fuelPrice = fuelPrice;
	}
	public double getConsumption() {
		return consumption;
	}
	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}
	public double getFloatingConsumption() {
		return floatingConsumption;
	}
	public void setFloatingConsumption(double floatingConsumption) {
		this.floatingConsumption = floatingConsumption;
	}
}