package sk.berops.android.fueller.dataModel;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
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
	private double initialMileageSI;
	@Element(name="currentMileage")
	private double currentMileage;
	private double currentMileageSI;
	@Element(name="volumeUnit", required=false)
	private VolumeUnit volumeUnit;
	@Element(name="distanceUnit", required=false)
	private DistanceUnit distanceUnit;
	@Element(name="consumptionUnit", required=false)
	private ConsumptionUnit consumptionUnit;
	@ElementList(inline=true, required=false)
	private LinkedList<Axle> axles;
	@Element(name="type", required=false)
	private CarType type;
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
	
	public enum CarType{
		SEDAN(0, "sedan"), 
		WAGON(1, "wagon"),
		CITY_CAR(2, "city car"),
		COUPE(2, "coupe"),
		ROADSTER(2, "roadster"),
		HATCHBACK(2, "hatchback"),
		SUV(2, "SUV"),
		VAN(2, "VAN"),
		PICKUP(2, "pickup"),
		TRUCK(2, "truck"),
		TRACTOR(2, "tractor"),
		TRUCK_TRACTOR(2, "truck tractor"),
		SEMI_TRAILER(2, "semi-trailer"),
		MOTORBIKE(2, "motorbike"),
		THREE_WHEELER(2, "3-wheeler");
		private int id;
		private String type;	
		CarType(int id, String type) {
			this.setId(id);
			this.setType(type);
		}
		
		private static Map<Integer, CarType> idToTypeMapping;

		public static CarType getCarType(int id) {
			if (idToTypeMapping == null) {
				initMapping();
			}
			
			CarType result = null;
			result = idToTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToTypeMapping = new HashMap<Integer, CarType>();
			for (CarType type : values()) {
				idToTypeMapping.put(type.id, type);
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
	}
	
	public Car(CarType type) {
		super();
		setHistory(new History());
		this.setDistanceUnit(DistanceUnit.getDistanceUnit(0));
		this.setVolumeUnit(VolumeUnit.getVolumeUnit(0));
		this.setConsumptionUnit(ConsumptionUnit.getConsumptionUnit(0));
		this.setType(type);
		this.axles = createAxles(getType());
	}
	
	public Car() {
		this(CarType.SEDAN);
	}
	
	public Car(CarType type, String nickname) {
		this(type);
		this.setNickname(nickname);
	}
	
	public Car(CarType type, String nickname, DistanceUnit du, VolumeUnit vu) {
		this(type, nickname);
		if (du != null) {
			this.setDistanceUnit(du);
		}
		if (vu != null) {
			this.setVolumeUnit(vu);
		}
	}
	
	public void initAfterLoad() {
		double coef = 0;
		switch (getDistanceUnit()) {
		case KILOMETER: coef = 1;
			break;
		case MILE: coef = UnitConstants.MILE;
			break;
		default:
			System.out.println("Not expected program branch reached");
			break;
		}
		if (getInitialMileageSI() == 0 && getInitialMileage() != 0) {
			setInitialMileageSI(getInitialMileage() * coef);
		}
		if (getCurrentMileageSI() == 0 && getCurrentMileage() != 0) {
			setCurrentMileageSI(getCurrentMileage() * coef); 
		}
		
		history.initAfterLoad(this);
	}

	private LinkedList<Axle> createAxles(CarType type) {
		LinkedList<Axle> axles = new LinkedList<Axle>();
		
		switch (type) {
		case CITY_CAR:
		case COUPE:
		case HATCHBACK:
		case PICKUP:
		case ROADSTER:
		case SEDAN:
		case SUV:
		case TRACTOR:
		case VAN:
		case WAGON:
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case MOTORBIKE:
			axles.add(new Axle(Axle.AxleType.SINGLE));
			axles.add(new Axle(Axle.AxleType.SINGLE));
			break;
		case SEMI_TRAILER:
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case THREE_WHEELER:
			axles.add(new Axle(Axle.AxleType.SINGLE));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case TRUCK:
		case TRUCK_TRACTOR:
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.TANDEM));
			axles.add(new Axle(Axle.AxleType.TANDEM));
			break;
		default: System.out.println("wrong car type specified");
			break;
		}
		return axles;
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
		if (volumeUnit == null) return VolumeUnit.getVolumeUnit(0);
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public DistanceUnit getDistanceUnit() {
		if (distanceUnit == null) return DistanceUnit.getDistanceUnit(0);
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
		if (consumptionUnit == null) return ConsumptionUnit.getConsumptionUnit(0);
		return consumptionUnit;
	}

	public void setConsumptionUnit(ConsumptionUnit consumptionUnit) {
		this.consumptionUnit = consumptionUnit;
	}

	public CarType getType() {
		return type;
	}

	public void setType(CarType type) {
		this.type = type;
	}

	public LinkedList<Axle> getAxles() {
		return axles;
	}

	public void setAxles(LinkedList<Axle> axles) {
		this.axles = axles;
	}
}
