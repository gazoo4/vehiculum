package sk.berops.android.fueller.dataModel;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class Car extends Record implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1491264721816146235L;
	// TODO: add gallery, power, first registration, owner (2nd, 3rd...)
	// TODO: add engine object (power, construction,...)
	@Element(name="nickname", required=false)
	private String nickname;
	@Element(name="brand")
	private String brand;
	@Element(name="model")
	private String model;
	@Element(name="modelYear")
	private int modelYear;
	@Element(name="history")
	private History history;
	@Element(name="licensePlate")
	private String licensePlate;
	@Element(name="initialMileage")
	private double initialMileage;
	@Element(name="initialMileageSI")
	private double initialMileageSI;
	@Element(name="currentMileage")
	private double currentMileage;
	@Element(name="currentMileageSI")
	private double currentMileageSI;
	@Element(name="volumeUnit", required=false)
	private VolumeUnit volumeUnit;
	@Element(name="distanceUnit", required=false)
	private DistanceUnit distanceUnit;
	@Element (name="consumptionUnit", required=false)
	private ConsumptionUnit consumptionUnit;
	private Consumption consumption;
	private Consumption consumptionSI;
	
	public enum VolumeUnit{
		LITER(0, "liter"), 
		US_GALLON(1, "US gallon"),
		IMPERIAL_GALLON(2, "imperial gallon");
		private int id;
		private String unit;	
		VolumeUnit(int id, String unit) {
			this.setId(id);
			this.setUnit(unit);
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
			idToUnitMapping = new HashMap<Integer, Car.VolumeUnit>();
			for (VolumeUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
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
	
	public enum DistanceUnit{
		KILOMETER(0, "kilometer"), 
		MILE(1, "mile");
		private int id;
		private String unit;	
		DistanceUnit(int id, String unit) {
			this.setId(id);
			this.setUnit(unit);
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
			idToUnitMapping = new HashMap<Integer, Car.DistanceUnit>();
			for (DistanceUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
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
	
	public enum ConsumptionUnit{
		LITRE_PER_100KM(0, "l/100 km"), 
		KM_PER_LITRE(1, "km/l"),
		MPG_US(2, "mpg"),
		MPG_IMPERIAL(3, "mpg");
		private int id;
		private String unit;	
		ConsumptionUnit(int id, String unit) {
			this.setId(id);
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
			idToUnitMapping = new HashMap<Integer, Car.ConsumptionUnit>();
			for (ConsumptionUnit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
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
	
	public Car() {
		super();
		setHistory(new History());
		this.setDistanceUnit(DistanceUnit.getDistanceUnit(0));
		this.setVolumeUnit(VolumeUnit.getVolumeUnit(0));
	}

	public Car(String nickname) {
		this();
		this.setNickname(nickname);
	}
	
	public Car(String nickname, DistanceUnit du, VolumeUnit vu) {
		this(nickname);
		if (du != null) {
			this.setDistanceUnit(du);
		}
		if (vu != null) {
			this.setVolumeUnit(vu);
		}
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public String getNickname() {
		if (nickname == null) {
			return ""+ getBrand() +" "+ getModel() +" ("+ getModelYear() +")";
		}
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public double getInitialMileage() {
		return initialMileage;
	}

	public void setInitialMileage(double initialMileage) {
		this.initialMileage = initialMileage;
		switch (this.getDistanceUnit()) {
		case KILOMETER:
			setInitialMileageSI(initialMileage);
			break;
		case MILE:
			setInitialMileageSI(initialMileage * UnitConstants.MILE);
			break;
		default:
			break;
		}
	}

	public double getCurrentMileage() {
		return currentMileage;
	}

	public void setCurrentMileage(double currentMileage) {
		this.currentMileage = currentMileage;
		switch (this.getDistanceUnit()) {
		case KILOMETER:
			setCurrentMileageSI(currentMileage);
			break;
		case MILE:
			setCurrentMileageSI(currentMileage * UnitConstants.MILE);
			break;
		default:
			break;
		}
	}

	public VolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public DistanceUnit getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(DistanceUnit distanceUnit) {
		this.distanceUnit = distanceUnit;
	}
	
	public double getInitialMileageSI() {
		return initialMileageSI;
	}

	public void setInitialMileageSI(double initialMileageSI) {
		this.initialMileageSI = initialMileageSI;
	}

	public double getCurrentMileageSI() {
		return currentMileageSI;
	}

	public void setCurrentMileageSI(double currentMileageSI) {
		this.currentMileageSI = currentMileageSI;
	}
	
	public Consumption getConsumption() {
		return consumption;
	}

	public void setConsumption(Consumption consumption) {
		this.consumption = consumption;
	}
	public Consumption getConsumptionSI() {
		return consumptionSI;
	}

	public void setConsumptionSI(Consumption consumptionSI) {
		this.consumptionSI = consumptionSI;
	}

	public ConsumptionUnit getConsumptionUnit() {
		return consumptionUnit;
	}

	public void setConsumptionUnit(ConsumptionUnit consumptionUnit) {
		this.consumptionUnit = consumptionUnit;
	}
}
