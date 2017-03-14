package sk.berops.android.vehiculum.dataModel;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.vehiculum.gui.MainActivity;

public class UnitConstants {
	public static class ConsumptionUnit {
		private ConsumptionScheme cs;
		private QuantityUnit qu;
		private DistanceUnit du;

		public ConsumptionUnit(ConsumptionScheme cs, QuantityUnit qu, DistanceUnit du) {
			this.cs = cs;
			this.qu = qu;
			this.du = du;
		}

		public ConsumptionScheme getConsumptionScheme() {
			return cs;
		}

		public QuantityUnit getQuantityUnit() {
			return qu;
		}

		public DistanceUnit getDistanceUnit() {
			return du;
		}

		public String toUnitLong() {
			return toConsumptionUnitLong(cs, du, qu);
		}

		public String toUnitShort() {
			return toConsumptionUnitShort(cs, du, qu);
		}

		@Override
		public boolean equals(Object obj) {
			// Basic checks
			if (obj == null) {
				return false;
			}
			if (!ConsumptionUnit.class.isAssignableFrom(obj.getClass())) {
				return false;
			}


			final ConsumptionUnit other = (ConsumptionUnit) obj;
			// Here it's faster and safer to compare enums via == or !=
			// (3rd expression evaluation)
			if ((this.cs == null) ? (other.cs != null) : (this.cs != other.cs)) {
				return false;
			}
			if ((this.du == null) ? (other.du != null) : (this.du != other.du)) {
				return false;
			}
			if ((this.qu == null) ? (other.qu != null) : (this.qu != other.qu)) {
				return false;
			}
			return true;
		}
	}

	// all the default units here are mapped against: kilometer, liter
	static Preferences preferences = Preferences.getInstance();

	public enum Substance{
		LIQUID(0),
		GAS(1),
		ELECTRIC(2);

		private int id;

		Substance(int id) {
			this.id = id;
		}

		private static Map<Integer, Substance> idToSubstanceMapping;

		public static Substance getConsistency(int id) {
			if (idToSubstanceMapping == null) {
				initMapping();
			}

			return idToSubstanceMapping.get(id);
		}

		private static void initMapping() {
			idToSubstanceMapping = new HashMap<>();
			for (Substance substance: Substance.values()) {
				idToSubstanceMapping.put(substance.getId(), substance);
			}
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	
	public enum QuantityUnit {
		// Need to keep this in sync with preference_units.xml value arrays:
		// preferences_units_gas_weight_values
		// preferences_units_volume_values
		// Alternatively we could change this to strings and pull it from preferences via "getString"
		LITER(
				0,
				1.0,
				true,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_liter_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_liter_long),
				Substance.LIQUID),
		US_GALLON(
				1,
				3.78541,
				false,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_gallon_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_gallon_us_long),
				Substance.LIQUID),
		IMPERIAL_GALLON(
				2,
				4.54609,
				false,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_gallon_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_gallon_imperial_long),
				Substance.LIQUID),
		KILOGRAM(
				3,
				1.0,
				true,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_kilogram_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_kilogram_long),
				Substance.GAS),
		POUND(
				4,
				0.453592,
				false,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_pound_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_pound_long),
				Substance.GAS),
		KILOWATT_HOUR(
				5,
				1.0,
				true,
				MainActivity.getContext().getString(R.string.generic_unit_quantity_kilowatthour_short),
				MainActivity.getContext().getString(R.string.generic_unit_quantity_kilowatthour_long),
				Substance.ELECTRIC);
		
		private int id;
		private double coef;
		private boolean si;
		private String unit;	
		private String longUnit;
		private Substance substance;

		QuantityUnit(int id, double coef, boolean si, String unit, String longUnit, Substance substance) {
			this.setId(id);
			this.setCoef(coef);
			this.setSi(si);
			this.setUnit(unit);
			this.setLongUnit(longUnit);
			this.setSubstance(substance);
		}

		public static QuantityUnit getDefaultQuantityUnit(FuelType type) {
			switch(type.getSubstance()) {
				case LIQUID:
					return LITER;
				case GAS:
					return KILOGRAM;
				case ELECTRIC:
					return KILOWATT_HOUR;
				default:
					Log.d("ERROR", "Unknown substance type. Silently changing to Liter");
					return LITER;
			}
		}
		
		private static Map<Integer, QuantityUnit> idToUnitMapping;

		public static QuantityUnit getQuantityUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			QuantityUnit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, QuantityUnit>();
			for (QuantityUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}
	
		public double getCoef() {
			return coef;
		}
		public void setCoef(double coef) {
			this.coef = coef;
		}
		public boolean isSI() {
			return si;
		}
		public void setSi(boolean si) {
			this.si = si;
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
		public Substance getSubstance() {
			return substance;
		}
		public void setSubstance(Substance substance) {
			this.substance = substance;
		}
	}
	
	public enum DistanceUnit{
		KILOMETER(
				0,
				1.0,
				true,
				MainActivity.getContext().getString(R.string.generic_unit_distance_kilometer_short),
				MainActivity.getContext().getString(R.string.generic_unit_distance_kilometer_short)),
		MILE(
				1,
				1.609344,
				false,
				MainActivity.getContext().getString(R.string.generic_unit_distance_mile_short),
				MainActivity.getContext().getString(R.string.generic_unit_distance_mile_long));

		private int id;
		private double coef;
		private boolean si;
		private String unit;	
		private String longUnit;
		DistanceUnit(int id, double coef, boolean si, String unit, String longUnit) {
			this.setId(id);
			this.setCoef(coef);
			this.setSi(si);
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
		public boolean isSi() {
			return si;
		}
		public void setSi(boolean si) {
			this.si = si;
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

	public enum ConsumptionScheme {
		FUEL_PER_DISTANCE(0, 1.0, "/"),
		DISTANCE_PER_FUEL(1, 1.0, "/"),
		FUEL_PER_100DISTANCE(2, 1.0, "/100");

		private int id;
		private double coef;
		private boolean si;
		private String scheme;

		ConsumptionScheme(int id, double coef, String scheme) {
			this.setId(id);
			this.setCoef(coef);
			this.setScheme(scheme);
		}

		private static Map<Integer, ConsumptionScheme> idToSchemeMapping;

		public static ConsumptionScheme getConsumptionScheme(int id) {
			if (idToSchemeMapping == null) {
				initMapping();
			}

			ConsumptionScheme result = null;
			result = idToSchemeMapping.get(id);
			return result;
		}

		private static void initMapping() {
			idToSchemeMapping = new HashMap<Integer, ConsumptionScheme>();
			for (ConsumptionScheme scheme : values()) {
				idToSchemeMapping.put(scheme.id, scheme);
			}
		}

		public double getCoef() {
			return coef;
		}
		public void setCoef(double coef) {
			this.coef = coef;
		}
		public String getScheme() {
			return scheme;
		}
		public void setScheme(String unit) {
			this.scheme = scheme;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	
	public enum CostUnit{
		COST_PER_DISTANCE(0, 1.0, "/"),
		COST_PER_100_DISTANCE(1, 100.0, "/100 "),
		DISTANCE_PER_COST(2, 1.0, "/"),
		DISTANCE_PER_100_COST(3, 100.0, "/100 ");
		
		private int id;
		private double coef;
		private String unit;	
		CostUnit(int id, double coef, String unit) {
			this.setId(id);
			this.setCoef(coef);
			this.setUnit(unit);
		}
		
		private static Map<Integer, CostUnit> idToUnitMapping;

		public static CostUnit getCostUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			CostUnit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, CostUnit>();
			for (CostUnit unit : values()) {
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
			DistanceUnit distanceUnit = preferences.getDistanceUnit();
			Currency.Unit currency = preferences.getCurrency();
			
			switch (this) {
			case COST_PER_100_DISTANCE:
			case COST_PER_DISTANCE:
				return ""+ currency.getUnit() + unit + distanceUnit.getUnit();
			case DISTANCE_PER_100_COST:
			case DISTANCE_PER_COST:
				return ""+ distanceUnit.getUnit() + unit + currency.getUnit();
			default:
				Log.d("DEBUG", "Unexpected CostUnit value");
			}
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

	public static String toConsumptionUnitLong(ConsumptionScheme scheme, DistanceUnit distance, QuantityUnit quantity) {
		if ((scheme == ConsumptionScheme.DISTANCE_PER_FUEL)
			&& (distance == DistanceUnit.MILE)) {
			if (quantity == QuantityUnit.IMPERIAL_GALLON) {
				return MainActivity.getContext().getString(R.string.generic_unit_consumption_mpg_imperial_long);
			} else if (quantity == QuantityUnit.US_GALLON) {
				return MainActivity.getContext().getString(R.string.generic_unit_consumption_mpg_us_long);
			}
		}

		switch(scheme) {
			case FUEL_PER_DISTANCE:
				return quantity.getLongUnit() + " per " + distance.getLongUnit();
			case DISTANCE_PER_FUEL:
				return  distance.getLongUnit() + " per " + quantity.getLongUnit();
			case FUEL_PER_100DISTANCE:
				return quantity.getLongUnit() + " per 100 " + distance.getLongUnit();
			default:
				Log.w("WARN", "Unknown consumption scheme: "+ scheme.getScheme() +". Silently ignoring.");
				return "";
		}
	}

	public static String toConsumptionUnitShort(ConsumptionScheme scheme, DistanceUnit distance, QuantityUnit quantity) {
		if ((scheme == ConsumptionScheme.DISTANCE_PER_FUEL)
			&& (distance == DistanceUnit.MILE)) {
			if ((quantity == QuantityUnit.IMPERIAL_GALLON)
				|| (quantity == QuantityUnit.US_GALLON)) {
				return MainActivity.getContext().getString(R.string.generic_unit_consumption_mpg_short);
			}
		}

		switch(scheme) {
			case FUEL_PER_DISTANCE:
				return quantity.getUnit() + "/" + distance.getUnit();
			case DISTANCE_PER_FUEL:
				return  distance.getUnit() + "/" + quantity.getUnit();
			case FUEL_PER_100DISTANCE:
				return quantity.getUnit() + "/100 " + distance.getUnit();
			default:
				Log.w("WARN", "Unknown consumption scheme: "+ scheme.getScheme() +". Silently ignoring.");
				return "";
		}
	}
	
	public static double convertUnitConsumptionFromSI(FuelType type, double fromValue) {
		return convertUnitConsumptionFromSI(type, fromValue, preferences.getConsumptionUnit(type));
	}

	public static double convertUnitConsumptionFromSI(FuelType type, double fromValue,
	                                                  ConsumptionUnit to) {
		ConsumptionUnit from = new ConsumptionUnit(
				ConsumptionScheme.FUEL_PER_100DISTANCE,
				QuantityUnit.getDefaultQuantityUnit(type),
				DistanceUnit.KILOMETER);
		return convertUnitConsumption(fromValue, from, to);
	}

	public static double convertUnitConsumption(double fromValue,
	                                            ConsumptionUnit from,
	                                            ConsumptionUnit to) {
		return convertUnitConsumption(
				fromValue,
				from.getQuantityUnit(),
				from.getConsumptionScheme(),
				from.getDistanceUnit(),
				to.getQuantityUnit(),
				to.getConsumptionScheme(),
				to.getDistanceUnit());
	}

	public static double convertUnitConsumption(double fromValue,
	                                            QuantityUnit fromQuantity,
	                                            ConsumptionScheme fromConsumption,
	                                            DistanceUnit fromDistance,
	                                            QuantityUnit toQuantity,
	                                            ConsumptionScheme toConsumption,
	                                            DistanceUnit toDistance) {

		if (fromQuantity.getSubstance() != toQuantity.getSubstance()) {
			Log.d("ERROR", "We can't convert between volume/mass/energy units. Silently exiting with zero return");
			return 0.0;
		}

		double value = fromValue;

		// First we need to convert towards the SI units
		switch(fromConsumption) {
			case FUEL_PER_DISTANCE:
				value *= 100.0;
				break;
			case DISTANCE_PER_FUEL:
				value = 1.0/value;
				value *= 100.0;
				break;
			case FUEL_PER_100DISTANCE:
				break;
		}

		value *= fromQuantity.getCoef();
		value /= fromDistance.getCoef();

		// Now in value variable we should have the SI values.
		// Starting to convert towards desired consumption unit.
		value /= toQuantity.getCoef();
		value *= toDistance.getCoef();

		switch(toConsumption) {
			case FUEL_PER_DISTANCE:
				value /= 100.0;
				break;
			case DISTANCE_PER_FUEL:
				value /= 100.0;
				value = 1.0/value;
				break;
			case FUEL_PER_100DISTANCE:
				break;
		}

		return value;
	}

	public static double convertUnitCost(double fromValue) {
		return convertUnitCost(fromValue, null, null);
	}
	
	public static double convertUnitCost(double fromValue, CostUnit fromUnit, CostUnit toUnit) {
		if (fromUnit == null) {
			fromUnit = CostUnit.COST_PER_DISTANCE;
		}
		if (toUnit == null) {
			toUnit = preferences.getCostUnit();
		}
		
		double distanceCoef = preferences.getDistanceUnit().getCoef();
		
		int coefFrom = 1;
		int coefTo = 1;
		
		switch (fromUnit) {
		case COST_PER_100_DISTANCE:
			coefFrom = 100;
		case COST_PER_DISTANCE:
			switch (toUnit) {
			case COST_PER_100_DISTANCE:
				coefTo = 100;
			case COST_PER_DISTANCE:
				return coefTo*(fromValue*distanceCoef/coefFrom);
			case DISTANCE_PER_100_COST:
				coefTo = 100;
			case DISTANCE_PER_COST:
				return coefTo/(fromValue*distanceCoef/coefFrom);
			default:
				Log.d("DEBUG", "Conversion method missing to: "+fromUnit.getUnit());
				break;
			}
			break;
		case DISTANCE_PER_100_COST:
			coefFrom = 100;
		case DISTANCE_PER_COST:
			switch (toUnit) {
			case COST_PER_100_DISTANCE:
				coefTo = 100;
			case COST_PER_DISTANCE:
				return coefTo/(fromValue*distanceCoef/coefFrom);
			case DISTANCE_PER_100_COST:
				coefTo = 100;
			case DISTANCE_PER_COST:
				return coefTo*(fromValue*distanceCoef/coefFrom);
			default:
				Log.d("DEBUG", "Conversion method missing to: "+fromUnit.getUnit());
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