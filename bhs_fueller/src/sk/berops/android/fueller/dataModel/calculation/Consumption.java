package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class Consumption {
	private Double total;
	private TreeMap<FuelType,Double> perType;
	
	public Consumption() {
		perType = new TreeMap();
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
}
