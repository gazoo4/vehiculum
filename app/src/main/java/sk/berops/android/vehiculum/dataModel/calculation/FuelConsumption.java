package sk.berops.android.vehiculum.dataModel.calculation;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry.FuelType;

public class FuelConsumption extends Consumption {
	//TODO: create also MaintenanceConsumption, FeeConsumption, etc...
	private FuelType lastRefuelType;
	private Double totalQuantity;												//total fuel quantity bought
	private TreeMap<FuelType, Double> totalQuantityPerFuelType;				//total fuel type quantity bought
	private int fuellingCount;												//amount of fuelling counts
	private TreeMap<FuelType, Integer> fuellingCountPerFuelType;			//amount of fuelling counts per fuel type
	private double averageSinceLast;										//average consumption since last refuelling
	private double grandAverage;											//overall average consumption combined across fuels
	private TreeMap<FuelType, Double> averagePerFuelType;					//average consumption per fuel type
	private TreeMap<FuelType, Double> movingAveragePerFuelType;				//moving average per fuel type
	private TreeMap<FuelType, Double> floatingAveragePerFuelType;			//floating average per fuel type
	private double mileageSinceFirstRefuel;									//mileage driven since the first refuel
	private TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelType;	//mileage driven since the first refuel of a certain fuel type
	private double mileageSinceLastRefuel;									//mileage driven since the last refuel
	private TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelType;	//mileage driven since the last refuel of a certain fuel type
	private double totalFuelCost;											//total fuel costs
	private TreeMap<FuelType, Double> totalFuelCostPerFuelType;				//total fuel costs of a certain fuel type
	private double averageFuelCost;											//average fuel cost per distance
	private TreeMap<FuelType, Double> averageFuelCostPerFuelType;			//average fuel type cost per distance
	private double costSinceLastRefuel;										//cost since last refuel per distance
	private TreeMap<FuelType, Double> costSinceLastRefuelPerFuelType;		//fuel type cost since last refuel per distance
	private double averageFuelPrice;										//average unit price
	private TreeMap<FuelType, Double> averageFuelPricePerFuelType;			//average unit price per fuel type
	
	public FuelConsumption() {
		super();
		lastRefuelType = FuelType.GASOLINE;
		totalQuantity = 0.0;
		totalQuantityPerFuelType = new TreeMap<FuelType, Double>();
		fuellingCount = 0;
		fuellingCountPerFuelType = new TreeMap<FuelType, Integer>();
		averageSinceLast = 0.0;
		grandAverage = 0.0;
		averagePerFuelType = new TreeMap<FuelType, Double>();
		movingAveragePerFuelType = new TreeMap<FuelType, Double>();
		floatingAveragePerFuelType = new TreeMap<FuelType, Double>();
		mileageSinceFirstRefuel = 0.0;
		mileageSinceFirstRefuelPerFuelType = new TreeMap<FuelType, Double>();
		mileageSinceLastRefuel = 0.0;
		mileageSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		totalFuelCost = 0.0;
		totalFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		averageFuelCost = 0.0;								
		averageFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		costSinceLastRefuel = 0.0;
		costSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		averageFuelPrice = 0.0;							
		averageFuelPricePerFuelType = new TreeMap<FuelType, Double>();
	}

	@Override
	public void reloadChartData(int data) {
		// We're on level 1. For anything smaller than level 1, we should call our parent class for the data generation instead
		if (data < 1) {
			super.reloadChartData(data);
			return;
		}
		pieChartLegend = new ArrayList<>();
		pieChartVals = new ArrayList<>();
		pieChartColors = new ArrayList<>();

		for (FuelType t : FuelType.values()) {
			if (getTotalFuelCostPerFuelType().get(t) == null) {
				continue;
			}
			double value = getTotalFuelCostPerFuelType().get(t);
			pieChartLegend.add(t.getType());
			pieChartVals.add(new Entry((float) value, t.getId()));
			pieChartColors.add(t.getColor());
		}
	}
	
	public FuelType getLastRefuelType() {
		return lastRefuelType;
	}

	public void setLastRefuelType(FuelType lastRefuelType) {
		this.lastRefuelType = lastRefuelType;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public TreeMap<FuelType, Double> getTotalQuantityPerFuelType() {
		return totalQuantityPerFuelType;
	}

	public void setTotalQuantityPerFuelType(TreeMap<FuelType, Double> totalQuantityPerFuelType) {
		this.totalQuantityPerFuelType = totalQuantityPerFuelType;
	}

	public int getFuellingCount() {
		return fuellingCount;
	}

	public void setFuellingCount(int fuellingCount) {
		this.fuellingCount = fuellingCount;
	}

	public TreeMap<FuelType,Integer> getFuellingCountPerFuelType() {
		return fuellingCountPerFuelType;
	}

	public void setFuellingCountPerFuelType(TreeMap<FuelType,Integer> fuellingCountPerFuelType) {
		this.fuellingCountPerFuelType = fuellingCountPerFuelType;
	}

	public double getAverageSinceLast() {
		return averageSinceLast;
	}

	public void setAverageSinceLast(double averageSinceLast) {
		this.averageSinceLast = averageSinceLast;
	}

	public double getGrandAverage() {
		return grandAverage;
	}

	public void setGrandAverage(double grandAverage) {
		this.grandAverage = grandAverage;
	}

	public TreeMap<FuelType, Double> getAveragePerFuelType() {
		return averagePerFuelType;
	}

	public void setAveragePerFuelType(TreeMap<FuelType, Double> averagePerFuelType) {
		this.averagePerFuelType = averagePerFuelType;
	}

	public double getMovingAverage() {
		double i = 0.0;
		for (FuelType t : movingAveragePerFuelType.keySet()) {
			i += movingAveragePerFuelType.get(t).doubleValue();
		}
		return i;
	}

	public TreeMap<FuelType, Double> getMovingAveragePerFuelType() {
		return movingAveragePerFuelType;
	}

	public void setMovingAveragePerFuelType(
			TreeMap<FuelType, Double> movingAveragePerFuelType) {
		this.movingAveragePerFuelType = movingAveragePerFuelType;
	}

	public double getFloatingAverage() {
		double i = 0.0;
		for (FuelType t : floatingAveragePerFuelType.keySet()) {
			i += floatingAveragePerFuelType.get(t).doubleValue();
		}
		return i;
	}

	public TreeMap<FuelType, Double> getFloatingAveragePerFuelType() {
		return floatingAveragePerFuelType;
	}

	public void setFloatingAveragePerFuelType(
			TreeMap<FuelType, Double> floatingAveragePerFuelType) {
		this.floatingAveragePerFuelType = floatingAveragePerFuelType;
	}

	public double getMileageSinceFirstRefuel() {
		return mileageSinceFirstRefuel;
	}

	public void setMileageSinceFirstRefuel(double mileageSinceFirstRefuel) {
		this.mileageSinceFirstRefuel = mileageSinceFirstRefuel;
	}

	public TreeMap<FuelType, Double> getMileageSinceFirstRefuelPerFuelType() {
		return mileageSinceFirstRefuelPerFuelType;
	}

	public void setMileageSinceFirstRefuelPerFuelType(
			TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelType) {
		this.mileageSinceFirstRefuelPerFuelType = mileageSinceFirstRefuelPerFuelType;
	}

	public double getMileageSinceLastRefuel() {
		return mileageSinceLastRefuel;
	}

	public void setMileageSinceLastRefuel(double mileageSinceLastRefuel) {
		this.mileageSinceLastRefuel = mileageSinceLastRefuel;
	}

	public TreeMap<FuelType, Double> getMileageSinceLastRefuelPerFuelType() {
		return mileageSinceLastRefuelPerFuelType;
	}

	public void setMileageSinceLastRefuelPerFuelType(
			TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelType) {
		this.mileageSinceLastRefuelPerFuelType = mileageSinceLastRefuelPerFuelType;
	}

	public double getTotalFuelCost() {
		return totalFuelCost;
	}

	public void setTotalFuelCost(double totalFuelCost) {
		this.totalFuelCost = totalFuelCost;
	}

	public TreeMap<FuelType, Double> getTotalFuelCostPerFuelType() {
		return totalFuelCostPerFuelType;
	}

	public void setTotalFuelCostPerFuelType(TreeMap<FuelType, Double> totalFuelCostPerFuelType) {
		this.totalFuelCostPerFuelType = totalFuelCostPerFuelType;
	}

	public double getAverageFuelCost() {
		return averageFuelCost;
	}

	public void setAverageFuelCost(double averageFuelCost) {
		this.averageFuelCost = averageFuelCost;
	}

	public TreeMap<FuelType, Double> getAverageFuelCostPerFuelType() {
		return averageFuelCostPerFuelType;
	}

	public void setAverageFuelCostPerFuelType(TreeMap<FuelType, Double> averageFuelCostPerFuelType) {
		this.averageFuelCostPerFuelType = averageFuelCostPerFuelType;
	}

	public double getCostSinceLastRefuel() {
		return costSinceLastRefuel;
	}

	public void setCostSinceLastRefuel(double costSinceLastRefuel) {
		this.costSinceLastRefuel = costSinceLastRefuel;
	}

	public TreeMap<FuelType, Double> getCostSinceLastRefuelPerFuelType() {
		return costSinceLastRefuelPerFuelType;
	}

	public void setCostSinceLastRefuelPerFuelType(
			TreeMap<FuelType, Double> costSinceLastRefuelPerFuelType) {
		this.costSinceLastRefuelPerFuelType = costSinceLastRefuelPerFuelType;
	}

	public double getAverageFuelPrice() {
		return averageFuelPrice;
	}

	public void setAverageFuelPrice(double averageFuelPrice) {
		this.averageFuelPrice = averageFuelPrice;
	}

	public TreeMap<FuelType, Double> getAverageFuelPricePerFuelType() {
		return averageFuelPricePerFuelType;
	}

	public void setAverageFuelPricePerFuelType(TreeMap<FuelType, Double> averageFuelPricePerFuelType) {
		this.averageFuelPricePerFuelType = averageFuelPricePerFuelType;
	}
	
	public Set<FuelType> getFuelTypes() {
		return getFuellingCountPerFuelType().keySet();
	}

}
