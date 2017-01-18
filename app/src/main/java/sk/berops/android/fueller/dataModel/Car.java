package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import sk.berops.android.fueller.dataModel.UnitConstants.ConsumptionUnit;
import sk.berops.android.fueller.dataModel.UnitConstants.DistanceUnit;
import sk.berops.android.fueller.dataModel.UnitConstants.VolumeUnit;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;

public class Car extends Record implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1491264721816146235L;
	// TODO: add gallery, power, first registration, owner (2nd, 3rd...)
	// TODO: add VIN number
	// TODO: add engine object (power, construction,...)
	@Element(name = "nickname", required = false)
	private String nickname;
	@Element(name = "brand")
	private String brand;
	@Element(name = "model")
	private String model;
	@Element(name = "modelYear")
	private int modelYear;
	@Element(name = "history")
	private History history;
	@Element(name = "licensePlate", required = false)
	private String licensePlate;
	@Element(name = "initialMileage")
	private double initialMileage;
	private double initialMileageSI;
	@Element(name = "currentMileage")
	private double currentMileage;
	private double currentMileageSI;
	@Element(name = "volumeUnit", required = false)
	private VolumeUnit volumeUnit;
	@Element(name = "distanceUnit", required = false)
	private DistanceUnit distanceUnit;
	@Element(name = "consumptionUnit", required = false)
	private ConsumptionUnit consumptionUnit;
	@Element(name = "type", required = false)
	private CarType type;
	
	/**
	 * Car's initial tyreScheme. TODO: Not used yet. So far we assume that the
	 * initiall tyreScheme is always null (meaning no tyres installed).
	 */
	@Element(name = "initialTyreScheme", required = false)
	private TyreConfigurationScheme initialTyreScheme;

	/**
	 * Dynamically calculated tyreScheme. It should link to the last
	 * TyreChangeEntry's tyreScheme.
	 */
	private TyreConfigurationScheme currentTyreScheme;

	public enum CarType {
		SEDAN(0, "sedan"),
		STATION_WAGON(1, "station wagon"),
		CITY_CAR(2, "city car"),
		COUPE(2, "coupe"),
		ROADSTER(2, "roadster"), 
		HATCHBACK(2, "hatchback"),
		SUV(2, "SUV"), VAN(2, "VAN"),
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
		setInitialMileageSI(getInitialMileage() * getDistanceUnit().getCoef());
		setCurrentMileageSI(getCurrentMileage() * getDistanceUnit().getCoef());

		history.initAfterLoad(this);
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
			return "" + getBrand() + " " + getModel() + " (" + getModelYear() + ")";
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
		setInitialMileageSI(initialMileage * getDistanceUnit().getCoef());
	}

	public double getCurrentMileage() {
		return currentMileage;
	}

	public void setCurrentMileage(double currentMileage) {
		this.currentMileage = currentMileage;
		setCurrentMileageSI(currentMileage * getDistanceUnit().getCoef());
	}

	public VolumeUnit getVolumeUnit() {
		if (volumeUnit == null)
			return VolumeUnit.getVolumeUnit(0);
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public DistanceUnit getDistanceUnit() {
		if (distanceUnit == null)
			return DistanceUnit.getDistanceUnit(0);
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
		if (getHistory().getEntries().size() == 0)
			return null;
		return getHistory().getEntries().getLast().getConsumption();
	}

	public ConsumptionUnit getConsumptionUnit() {
		if (consumptionUnit == null)
			return ConsumptionUnit.getConsumptionUnit(0);
		return consumptionUnit;
	}

	public void setConsumptionUnit(ConsumptionUnit consumptionUnit) {
		this.consumptionUnit = consumptionUnit;
	}

	public FuelConsumption getFuelConsumption() {
		if (getHistory().getFuellingEntries().size() == 0)
			return null;
		return getHistory().getFuellingEntries().getLast().getFuelConsumption();
	}

	public CarType getType() {
		return type;
	}

	public void setType(CarType type) {
		this.type = type;
	}

	public double getDistanceDriven() {
		double distance = this.getCurrentMileage() - this.getHistory().getEntries().getFirst().getMileage();
		return distance;
	}

	public Set<FuelType> getFuelTypes() {
		return getHistory().getFuellingEntries().getLast().getFuelConsumption().getFuellingCountPerFuelType()
				.keySet();
	}

	public TyreConfigurationScheme getInitialTyreScheme() {
		if (initialTyreScheme == null)
			return new TyreConfigurationScheme(this);
		return initialTyreScheme;
	}

	public void setInitialTyreScheme(TyreConfigurationScheme initialTyreScheme) {
		this.initialTyreScheme = initialTyreScheme;
	}

	/**
	 * Method to provide the latest TyreConfigurationScheme
	 * @return TyreConfigurationScheme
	 */
	public TyreConfigurationScheme getCurrentTyreScheme() {
		return getTyreSchemeByDate(null);
	}

	/**
	 * Method to provide the TyreConfigurationScheme in a specific moment in the history.
	 * @param date in which you want to get the TyreConfigurationScheme
	 *             (if null, the method will provide the latest TyreConfigurationScheme)
	 * @return TyreConfigurationScheme
	 */
	public TyreConfigurationScheme getTyreSchemeByDate(Date date) {
		TyreConfigurationScheme scheme;

		LinkedList<TyreChangeEntry> entries = this.getHistory().getTyreChangeEntries();
		History.filterEntriesByDate(entries, null, date);
		if (entries.size() > 0) {
			scheme = entries.getLast().getTyreScheme();
		} else {
			scheme = getInitialTyreScheme();
		}
		return scheme;
	}
}
