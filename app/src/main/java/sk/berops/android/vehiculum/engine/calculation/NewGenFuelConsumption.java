package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;


/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenFuelConsumption extends NewGenConsumption {
	// TODO: Add these constants into the configuration options
	// After a configuration change a re-calculation of the consumptions must be done
	/**
	 * Size of a set for calculation of the {@link NewGenFuelConsumption#movingConsumption}
	 */
	public static final int MOVING_AVG_COUNT = 5;
	/**
	 * Size of a set for calculation of the {@link NewGenFuelConsumption#floatingConsumption}
	 */
	public static final int FLOATING_AVG_COUNT = 7;
	/**
	 * Number of MAX and MIN extremes removed from calculation of the {@link NewGenFuelConsumption#floatingConsumption}
	 */
	public static final int FLOATING_AVG_CUT = 2;
	/**
	 * Average cost per distance unit since last refuel
	 */
	private Cost costLastRefuel;
	/**
	 * Total volume of fuel bought split into the substance categories (i.e. liquid = liter, gas = kg, electricity = kWh)
	 */
	private HashMap<UnitConstants.Substance, Double> totalVolume;
	/**
	 * Total volume of fuel bought per fuel type
	 */
	private HashMap<FuellingEntry.FuelType, Double> totalTypeVolume;
	/**
	 * Average fuel consumption per distance unit
	 */
	private HashMap<UnitConstants.Substance, Double> averageConsumption;
	/**
	 * Average fuel consumption (per fuel type) per distance unit
	 */
	private HashMap<FuellingEntry.FuelType, Double> averageTypeConsumption;
	/**
	 * Average consumption from the last refuel
	 */
	private double lastConsumption;
	/**
	 * Moving consumption
	 * (average consumption per distance across {@link NewGenFuelConsumption#MOVING_AVG_COUNT} refuellings)
	 */
	private HashMap<UnitConstants.Substance, Double> movingConsumption;
	/**
	 * Floating consumption
	 * (arithmetically calculated average consumption across {@link NewGenFuelConsumption#FLOATING_AVG_COUNT} refuellings
	 * cutting out {@link NewGenFuelConsumption#FLOATING_AVG_CUT} of max and min values from the set)
	 */
	private HashMap<UnitConstants.Substance, Double> floatingConsumption;

	public Cost getCostLastRefuel() {
		return costLastRefuel;
	}

	public void setCostLastRefuel(Cost costLastRefuel) {
		this.costLastRefuel = costLastRefuel;
	}

	public HashMap<UnitConstants.Substance, Double> getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(HashMap<UnitConstants.Substance, Double> totalVolume) {
		this.totalVolume = totalVolume;
	}

	public HashMap<FuellingEntry.FuelType, Double> getTotalTypeVolume() {
		return totalTypeVolume;
	}

	public void setTotalTypeVolume(HashMap<FuellingEntry.FuelType, Double> totalTypeVolume) {
		this.totalTypeVolume = totalTypeVolume;
	}

	public HashMap<UnitConstants.Substance, Double> getAverageConsumption() {
		return averageConsumption;
	}

	public void setAverageConsumption(HashMap<UnitConstants.Substance, Double> averageConsumption) {
		this.averageConsumption = averageConsumption;
	}

	public HashMap<FuellingEntry.FuelType, Double> getAverageTypeConsumption() {
		return averageTypeConsumption;
	}

	public void setAverageTypeConsumption(HashMap<FuellingEntry.FuelType, Double> averageTypeConsumption) {
		this.averageTypeConsumption = averageTypeConsumption;
	}

	public double getLastConsumption() {
		return lastConsumption;
	}

	public void setLastConsumption(double lastConsumption) {
		this.lastConsumption = lastConsumption;
	}

	public HashMap<UnitConstants.Substance, Double> getMovingConsumption() {
		return movingConsumption;
	}

	public void setMovingConsumption(HashMap<UnitConstants.Substance, Double> movingConsumption) {
		this.movingConsumption = movingConsumption;
	}

	public HashMap<UnitConstants.Substance, Double> getFloatingConsumption() {
		return floatingConsumption;
	}

	public void setFloatingConsumption(HashMap<UnitConstants.Substance, Double> floatingConsumption) {
		this.floatingConsumption = floatingConsumption;
	}
}
