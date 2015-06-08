package sk.berops.android.fueller.dataModel;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class UnitConstants {
	// all the default units here are mapped against: kilometer, liter
	public static final double LITRE_PER_100KM = 1.0;
	public static final double KM_PER_LITRE = 0;
	public static final double MPG_IMPERIAL = 0;
	public static final double MPG_US = 0;
	
	public enum VolumeUnit{
		SI(-1,1.0, "ltrs", "liters"),
		LITER(0, 1.0, "ltrs", "liters"), 
		US_GALLON(1, 3.78541, "gal", "US gallons"),
		IMPERIAL_GALLON(2, 4.54609, "gal", "Imperial gallons");
		private int id;
		private double coef;
		private String unit;	
		private String longUnit;
		VolumeUnit(int id, double coef, String unit, String longUnit) {
			this.setId(id);
			this.setCoef(coef);
			this.setUnit(unit);
			this.setLongUnit(longUnit);
		}
		
		private static Map<Integer, VolumeUnit> idToUnitMapping;

		public static VolumeUnit getVolumeUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			VolumeUnit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, VolumeUnit>();
			for (VolumeUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}
	
		public double getCoef() {
			return coef;
		}
		public void setCoef(double coef) {
			this.coef = coef;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLongUnit() {
			return longUnit;
		}
		public void setLongUnit(String longUnit) {
			this.longUnit = longUnit;
		}
	}
	
	public enum DistanceUnit{
		SI(-1, 1.0, "km", "kilometers"),
		KILOMETER(0, 1.0, "km", "kilometers"), 
		MILE(1, 1.609344, "mil", "miles");
		private int id;
		private double coef;
		private String unit;	
		private String longUnit;
		DistanceUnit(int id, double coef, String unit, String longUnit) {
			this.setId(id);
			this.setCoef(coef);
			this.setUnit(unit);
			this.setLongUnit(longUnit);
		}
		
		private static Map<Integer, DistanceUnit> idToUnitMapping;

		public static DistanceUnit getDistanceUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			DistanceUnit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, DistanceUnit>();
			for (DistanceUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}
	
		public double getCoef() {
			return coef;
		}
		public void setCoef(double coef) {
			this.coef = coef;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLongUnit() {
			return longUnit;
		}
		public void setLongUnit(String longUnit) {
			this.longUnit = longUnit;
		}
	}
	
	public enum ConsumptionUnit{
		SI(-1, 1.0, "l/100 km"),
		LITRE_PER_100KM(0, 1.0, "l/100 km"), 
		KM_PER_LITRE(1, 1.0, "km/l"),
		MPG_US(2, 1.0, "mpg"),
		MPG_IMPERIAL(3, 1.0, "mpg");
		private int id;
		private double coef;
		private String unit;	
		ConsumptionUnit(int id, double coef, String unit) {
			this.setId(id);
			this.setCoef(coef);
			this.setUnit(unit);
		}
		
		private static Map<Integer, ConsumptionUnit> idToUnitMapping;

		public static ConsumptionUnit getConsumptionUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			ConsumptionUnit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, ConsumptionUnit>();
			for (ConsumptionUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}
	
		public double getCoef() {
			return coef;
		}
		public void setCoef(double coef) {
			this.coef = coef;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	public static double convertUnitConsumption(double fromValue, ConsumptionUnit fromUnit, ConsumptionUnit toUnit) {
		switch (fromUnit) {
		case KM_PER_LITRE:
			switch (toUnit) {
			case KM_PER_LITRE:
				return fromValue;
			case SI:
			case LITRE_PER_100KM:
				return (100/fromValue);
			case MPG_IMPERIAL:
				return (2.82481*fromValue);
			case MPG_US:
				return (2.35215*fromValue);
			default:
				Log.d("DEBUG", "Conversion method missing to: "+toUnit.getUnit());
				break;		
			}
			break;
		case SI:
		case LITRE_PER_100KM:
			switch (toUnit) {
			case KM_PER_LITRE:
				return (100/fromValue);
			case SI:
			case LITRE_PER_100KM:
				return fromValue;
			case MPG_IMPERIAL:
				return (282.481/fromValue);
			case MPG_US:
				return (235.215/fromValue);
			default:
				Log.d("DEBUG", "Conversion method missing to: "+toUnit.getUnit());
				break;		
			}
			break;
		case MPG_IMPERIAL:
			switch (toUnit) {
			case KM_PER_LITRE:
				return (fromValue/2.82481);
			case SI:
			case LITRE_PER_100KM:
				return (282.481/fromValue);
			case MPG_IMPERIAL:
				return fromValue;
			case MPG_US:
				return (fromValue/1.20095042);
			default:
				Log.d("DEBUG", "Conversion method missing to: "+toUnit.getUnit());
				break;	
			}
			break;
		case MPG_US:
			switch (toUnit) {
			case KM_PER_LITRE:
				return (fromValue/2.35215);
			case SI:
			case LITRE_PER_100KM:
				return (235.215/fromValue);
			case MPG_IMPERIAL:
				return (fromValue*1.20095042);
			case MPG_US:
				return fromValue;
			default:
				Log.d("DEBUG", "Conversion method missing to: "+toUnit.getUnit());
				break;	
			}
			break;
		default:
			Log.d("DEBUG", "Conversion method missing from: "+fromUnit.getUnit());
			break;
		}
		return 0.0;
	}
}