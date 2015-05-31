package sk.berops.android.fueller.configuration;

import java.io.Serializable;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.Currency;
import sk.berops.android.fueller.dataModel.expense.Currency.Unit;

public class UnitSettings implements Serializable {
	
	private static final long serialVersionUID = 6578494917853630351L;

	private static UnitSettings instance = null;
	
	@Element(name="consumptionUnit", required=false)
	private Car.ConsumptionUnit consumptionUnit;
	@Element(name="volumeUnit", required=false)
	private Car.VolumeUnit volumeUnit;
	@Element(name="distanceUnit", required=false)
	private Car.DistanceUnit distanceUnit;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	
	private UnitSettings() {
		consumptionUnit = Car.ConsumptionUnit.LITRE_PER_100KM;
		volumeUnit = Car.VolumeUnit.LITER;
		distanceUnit = Car.DistanceUnit.KILOMETER;
		currency = Currency.Unit.EURO;
	}
	
	public static UnitSettings getInstance() {
		if (instance == null) {
			instance = new UnitSettings();
		}
		return instance;
	}

	public Car.ConsumptionUnit getConsumptionUnit() {
		return consumptionUnit;
	}

	public void setConsumptionUnit(Car.ConsumptionUnit consumptionUnit) {
		this.consumptionUnit = consumptionUnit;
	}

	public Car.VolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(Car.VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public Car.DistanceUnit getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(Car.DistanceUnit distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public Unit getCurrency() {
		return currency;
	}

	public void setCurrency(Unit currency) {
		this.currency = currency;
	}
}
