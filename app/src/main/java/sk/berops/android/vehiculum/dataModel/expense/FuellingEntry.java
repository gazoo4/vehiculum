package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.UnitConstants.QuantityUnit;
import sk.berops.android.vehiculum.dataModel.calculation.FuelConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.FuellingEntryController;

public class FuellingEntry extends Entry {
	@Element(name="fuelQuantity")
	private double fuelQuantity;
	private double fuelQuantitySI;
	@Element(name="quantityUnit", required=false)
	private UnitConstants.QuantityUnit quantityUnit;
	@Element(name="fuelType")
	private FuelType fuelType;
	@Element(name="fuelPrice", required=false)
	private Cost fuelPrice;

	public enum FuelType{
		GASOLINE(0, "gasoline", 0xFFDA3BB0, UnitConstants.Substance.LIQUID),
		LPG(1, "lpg", 0xFFDA680A, UnitConstants.Substance.LIQUID),
		DIESEL(2, "diesel", 0xFF4C4C4C, UnitConstants.Substance.LIQUID),
		CNG(3, "cng", 0xFF2C712C, UnitConstants.Substance.GAS),
		ELECTRICITY(4, "electricity", 0xFF02A9BC, UnitConstants.Substance.ELECTRIC);
		private int id;
		private String type;	
		private int color;
		private UnitConstants.Substance substance;

		FuelType(int id, String type, int color, UnitConstants.Substance substance) {
			this.setId(id);
			this.setType(type);
			this.setColor(color);
			this.setSubstance(substance);
		}
		
		private static Map<Integer, FuelType> idToTypeMapping;

		public static FuelType getFuelType(int id) {
			if (idToTypeMapping == null) {
				initMapping();
			}
			
			FuelType result = null;
			result = idToTypeMapping.get(id);
			return result;
		}

		private static void initMapping() {
			idToTypeMapping = new HashMap<Integer, FuellingEntry.FuelType>();
			for (FuelType ft : values()) {
				idToTypeMapping.put(ft.id, ft);
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
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
		public UnitConstants.Substance getSubstance() {
			return substance;
		}
		public void setSubstance(UnitConstants.Substance substance) {
			this.substance = substance;
		}
	}
	
	@Override
	public void generateSI() {
		super.generateSI();
		setFuelQuantity(getFuelQuantity(), getQuantityUnit());
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
	public double getFuelQuantity() {
		return fuelQuantity;
	}
	public void setFuelQuantity(double fuelQuantity, QuantityUnit unit) {
		this.fuelQuantity = fuelQuantity;
		this.quantityUnit = unit;
		setFuelQuantitySI(fuelQuantity * unit.getCoef());
	}
	public Cost getFuelPrice() {
		if (fuelPrice == null) {
			fuelPrice = new Cost();
			Double price;
			for (Currency.Unit unit: getCost().getCosts().keySet()) {
				price = getCost().getCosts().get(unit) / getFuelQuantitySI();
				fuelPrice.getCosts().put(unit, price);
			}
		}

		return fuelPrice;
	}
	public void setFuelPrice(Cost fuelPrice) {
		this.fuelPrice = fuelPrice;
	}
	public double getFuelQuantitySI() {
		return fuelQuantitySI;
	}
	public void setFuelQuantitySI(double fuelQuantitySI) {
		this.fuelQuantitySI = fuelQuantitySI;
	}
	public QuantityUnit getQuantityUnit() {
		if (quantityUnit == null) quantityUnit = QuantityUnit.LITER;
		return quantityUnit;
	}

	public void setQuantityUnit(QuantityUnit quantityUnit) {
		this.quantityUnit = quantityUnit;
		setFuelQuantitySI(fuelQuantity * quantityUnit.getCoef());
	}
	public FuelConsumption getFuelConsumption() {
		return (FuelConsumption) this.getConsumption();
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public FuellingEntryController getController() {
		return new FuellingEntryController(this);
	}
}