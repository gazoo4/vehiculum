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
	 * Size of a set for calculation of the {@link NewGenFuelConsumption#movingConsumptionBySubstance}
	 */
	public static final int MOVING_AVG_COUNT = 5;
	/**
	 * Size of a set for calculation of the {@link NewGenFuelConsumption#floatingConsumptionBySubstance}
	 */
	public static final int FLOATING_AVG_COUNT = 7;
	/**
	 * Number of MAX and MIN extremes removed from calculation of the {@link NewGenFuelConsumption#floatingConsumptionBySubstance}
	 */
	public static final int FLOATING_AVG_CUT = 2;
	/**
	 * Average cost per distance unit since last refuel
	 */
	private Cost costLastRefuel;
	/**
	 * Total volume of fuel bought split into the substance categories (i.e. liquid = liter, gas = kg, electricity = kWh)
	 */
	private HashMap<UnitConstants.Substance, Double> totalVolumeBySubstance;
	/**
	 * Total volume of fuel bought per fuel type
	 */
	private HashMap<FuellingEntry.FuelType, Double> totalVolumeByType;
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
	 * Moving consumption for each fuel substance
	 * (average consumption per distance across {@link NewGenFuelConsumption#MOVING_AVG_COUNT} refuellings)
	 */
	private HashMap<UnitConstants.Substance, Double> movingConsumptionBySubstance;
	/**
	 * Floating consumption for each fuel substance
	 * (arithmetically calculated average consumption across {@link NewGenFuelConsumption#FLOATING_AVG_COUNT} refuellings
	 * cutting out {@link NewGenFuelConsumption#FLOATING_AVG_CUT} of max and min values from the set)
	 */
	private HashMap<UnitConstants.Substance, Double> floatingConsumptionBySubstance;
	/**
	 * Moving consumption for each fuel type
	 * (average consumption per distance across {@link NewGenFuelConsumption#MOVING_AVG_COUNT} refuellings)
	 */
	private HashMap<FuellingEntry.FuelType, Double> movingConsumptionByType;
	/**
	 * Floating consumption for each fuel type
	 * (arithmetically calculated average consumption across {@link NewGenFuelConsumption#FLOATING_AVG_COUNT} refuellings
	 * cutting out {@link NewGenFuelConsumption#FLOATING_AVG_CUT} of max and min values from the set)
	 */
	private HashMap<FuellingEntry.FuelType, Double> floatingConsumptionByType;

	public Cost getCostLastRefuel() {
		return costLastRefuel;
	}

	public void setCostLastRefuel(Cost costLastRefuel) {
		this.costLastRefuel = costLastRefuel;
	}

	public HashMap<UnitConstants.Substance, Double> getTotalVolumeBySubstance() {
		return totalVolumeBySubstance;
	}

	public void setTotalVolumeBySubstance(HashMap<UnitConstants.Substance, Double> totalVolumeBySubstance) {
		this.totalVolumeBySubstance = totalVolumeBySubstance;
	}

	public HashMap<FuellingEntry.FuelType, Double> getTotalVolumeByType() {
		return totalVolumeByType;
	}

	public void setTotalVolumeByType(HashMap<FuellingEntry.FuelType, Double> totalVolumeByType) {
		this.totalVolumeByType = totalVolumeByType;
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

	public HashMap<UnitConstants.Substance, Double> getMovingConsumptionBySubstance() {
		return movingConsumptionBySubstance;
	}

	public void setMovingConsumptionBySubstance(HashMap<UnitConstants.Substance, Double> movingConsumptionBySubstance) {
		this.movingConsumptionBySubstance = movingConsumptionBySubstance;
	}

	public HashMap<UnitConstants.Substance, Double> getFloatingConsumptionBySubstance() {
		return floatingConsumptionBySubstance;
	}

	public void setFloatingConsumptionBySubstance(HashMap<UnitConstants.Substance, Double> floatingConsumptionBySubstance) {
		this.floatingConsumptionBySubstance = floatingConsumptionBySubstance;
	}

	public HashMap<FuellingEntry.FuelType, Double> getMovingConsumptionByType() {
		return movingConsumptionByType;
	}

	public void setMovingConsumptionByType(HashMap<FuellingEntry.FuelType, Double> movingConsumptionByType) {
		this.movingConsumptionByType = movingConsumptionByType;
	}

	public HashMap<FuellingEntry.FuelType, Double> getFloatingConsumptionByType() {
		return floatingConsumptionByType;
	}

	public void setFloatingConsumptionByType(HashMap<FuellingEntry.FuelType, Double> floatingConsumptionByType) {
		this.floatingConsumptionByType = floatingConsumptionByType;
	}
}
