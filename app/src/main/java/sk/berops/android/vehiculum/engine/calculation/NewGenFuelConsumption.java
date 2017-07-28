package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import NewGenConsumption;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;


/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenFuelConsumption extends NewGenConsumption {
	private double totalQuantity;
	private HashMap<FuellingEntry.FuelType, Double> totalQuantityPerFuel;
	private int fuellingCount;
	private HashMap<FuellingEntry.FuelType, Integer> fuellingCountPerFuelType;
	private double averageSinceLast;
	private double grandAverage;
}
