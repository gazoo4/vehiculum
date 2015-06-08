package sk.berops.android.fueller.dataModel.expense;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.UnitConstants.VolumeUnit;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.UnitConstants;
import android.graphics.Color;

public class FuellingEntry extends Entry {
	@Element(name="fuelVolume")
	private double fuelVolume;
	private double fuelVolumeSI;
	@Element(name="volumeUnit", required=false)
	private VolumeUnit volumeUnit;
	@Element(name="fuelType")
	private FuelType fuelType;
	@Element(name="fuelPrice")
	private double fuelPrice;
	public enum FuelType{
		GASOLINE(0, "gasoline", Color.MAGENTA), 
		LPG(1, "lpg", 0xFFFF8000), //Color.ORANGE
		DIESEL(2, "diesel", Color.GRAY),
		CNG(3, "cng", Color.GREEN),
		ELECTRICITY(4, "electricity", Color.BLUE);
		private int id;
		private String type;	
		private int color;
		FuelType(int id, String type, int color) {
			this.setId(id);
			this.setType(type);
			this.setColor(color);
		}
		
		private static Map<Integer, FuelType> idToDescriptionMapping;
		private static Map<FuelType, Integer> typeToColorMapping;

		public static FuelType getFuelType(int id) {
			if (idToDescriptionMapping == null) {
				initMapping();
			}
			
			FuelType result = null;
			result = idToDescriptionMapping.get(id);
			return result;
		}
		/*
		public static int getColor(int id) {
			if (typeToColorMapping == null) {
				initMapping();
			}
			
			int result = -1;
			result = typeToColorMapping.get(id);
			return result;
		}
		*/
		private static void initMapping() {
			idToDescriptionMapping = new HashMap<Integer, FuellingEntry.FuelType>();
			typeToColorMapping = new HashMap<FuellingEntry.FuelType, Integer>();
			for (FuelType ft : values()) {
				idToDescriptionMapping.put(ft.id, ft);
				typeToColorMapping.put(ft, ft.color);
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
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
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
		return fuelPrice;
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