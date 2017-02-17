package sk.berops.android.caramel.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.caramel.dataModel.UnitConstants.VolumeUnit;
import sk.berops.android.caramel.dataModel.calculation.FuelConsumption;

public class FuellingEntry extends Entry {
	@Element(name="fuelVolume")
	private double fuelVolume;
	private double fuelVolumeSI;
	@Element(name="volumeUnit", required=false)
	private VolumeUnit volumeUnit;
	@Element(name="fuelType")
	private FuelType fuelType;
	@Element(name="fuelPrice", required=false)
	private double fuelPrice;
	public enum FuelType{
		GASOLINE(0, "gasoline", 0xFFDA3BB0),
		LPG(1, "lpg", 0xFFDA680A),
		DIESEL(2, "diesel", 0xFF4C4C4C),
		CNG(3, "cng", 0xFF2C712C),
		ELECTRICITY(4, "electricity", 0xFF02A9BC);
		private int id;
		private String type;	
		private int color;

		FuelType(int id, String fuelType) {
			this(id, fuelType, (int) (Math.random() * Integer.MAX_VALUE));
		}

		FuelType(int id, String type, int color) {
			this.setId(id);
			this.setType(type);
			this.setColor(color);
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
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
	}
	
	@Override
	public void generateSI() {
		super.generateSI();
		setFuelVolume(getFuelVolume(), getVolumeUnit());
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
	public void setFuelVolume(double fuelVolume, VolumeUnit unit) {
		this.fuelVolume = fuelVolume;
		this.volumeUnit = unit;
		setFuelVolumeSI(fuelVolume * unit.getCoef());
	}
	public double getFuelPrice() {
		if (fuelPrice == 0.0) {
			return getCostSI()/getFuelVolumeSI();
		} else {
			return fuelPrice;
		}
	}
	public void setFuelPrice(double fuelPrice) {
		this.fuelPrice = fuelPrice;
	}
	public double getFuelVolumeSI() {
		return fuelVolumeSI;
	}
	public void setFuelVolumeSI(double fuelVolumeSI) {
		this.fuelVolumeSI = fuelVolumeSI;
	}
	public VolumeUnit getVolumeUnit() {
		if (volumeUnit == null) volumeUnit = VolumeUnit.LITER;
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	public FuelConsumption getFuelConsumption() {
		return (FuelConsumption) this.getConsumption();
	}
}