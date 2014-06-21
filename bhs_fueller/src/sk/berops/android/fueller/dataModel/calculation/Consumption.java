package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Car.ConsumptionUnit;
import sk.berops.android.fueller.dataModel.UnitConstants;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class Consumption {
	private Double total;
	private TreeMap<FuelType,Double> perType;
	
	public Consumption() {
		perType = new TreeMap<FuelType,Double>();
		total = (double) 0;
	}
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public TreeMap<FuelType, Double> getPerType() {
		return perType;
	}
	public void setPerType(TreeMap<FuelType, Double> perType) {
		this.perType = perType;
	}
	
	public class SinceLastRefuel {
		private FuelType fuelType;
		private Double consumption;
		
		public FuelType getFuelType() {
			return fuelType;
		}
		public void setFuelType(FuelType fuelType) {
			this.fuelType = fuelType;
		}
		public Double getConsumption() {
			return consumption;
		}
		public void setConsumption(Double consumption) {
			this.consumption = consumption;
		}
	}

	public Consumption getConsumptionNonSI(Car.ConsumptionUnit cu) {
		Consumption consumption = new Consumption();
		double value = 0;
		double coef = 0;
		
		switch (cu) {
		case KM_PER_LITRE:
			coef = UnitConstants.KM_PER_LITRE;
			break;
		case LITRE_PER_100KM:
			coef = UnitConstants.LITRE_PER_100KM;
			break;
		case MPG_IMPERIAL:
			coef = UnitConstants.MPG_IMPERIAL;
			break;
		case MPG_US:
			coef = UnitConstants.MPG_US;
			break;
		default:
			break;
		}
		
		for (FuelType type : this.getPerType().keySet()) {
			value = this.getPerType().get(type);
			consumption.getPerType().put(type, value*coef);
		}
		return consumption;
	}
}
