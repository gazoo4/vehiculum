package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;

import static sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption.FLOATING_AVG_COUNT;
import static sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption.FLOATING_AVG_CUT;
import static sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption.MOVING_AVG_COUNT;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenFuelCalculator extends NewGenTypeCalculator {
	private HashMap<UnitConstants.Substance, FuellingEntry> initialBySubstance = new HashMap<>();
	private HashMap<FuellingEntry.FuelType, FuellingEntry> initialByFuelType = new HashMap<>();
	private HashMap<UnitConstants.Substance, FuellingEntry> previousBySubstance = new HashMap<>();
	private HashMap<FuellingEntry.FuelType, FuellingEntry> previousByFuelType = new HashMap<>();

	private HashMap<UnitConstants.Substance, LinkedList<FuellingEntry>> movingEntriesBySubstance = new HashMap<>();
	private HashMap<FuellingEntry.FuelType, LinkedList<FuellingEntry>> movingEntriesByType = new HashMap<>();
	private HashMap<UnitConstants.Substance, LinkedList<FuellingEntry>> floatingEntriesBySubstance = new HashMap<>();
	private HashMap<FuellingEntry.FuelType, LinkedList<FuellingEntry>> floatingEntriesByType = new HashMap<>();

	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (! (entry instanceof FuellingEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-fuelling entry");
			return;
		}

		FuellingEntry fEntry = (FuellingEntry) entry;

		NewGenFuelConsumption prevC = (previous == null) ? null : (NewGenFuelConsumption) previous.getConsumption();
		NewGenFuelConsumption nextC = fEntry.getFuelConsumption();

		nextC.setLastCost(calculateCostLastRefuel(fEntry));

		nextC.setTotalVolumeBySubstance(calculateTotalSubstanceVolume(prevC, fEntry));
		nextC.setTotalVolumeByType(calculateTotalTypeVolume(prevC, fEntry));

		nextC.setTotalCostBySubstance(calculateTotalSubstanceCost(prevC, fEntry));
		nextC.setTotalCostByType(calculateTotalTypeCost(prevC, fEntry));

		// Here we need the already calculated total costs (in the step above)
		nextC.setAverageCostBySubstance(calculateAverageCostBySubstance(nextC, fEntry));
		nextC.setAverageCostByType(calculateAverageCostByType(nextC, fEntry));

		nextC.setAverageConsumptionBySubstance(calculateAverageConsumptionBySubstance(prevC, fEntry));
		nextC.setLastConsumption(calculateLastConsumption(fEntry));

		nextC.setAverageConsumptionByType(calculateAverageConsumptionByType(prevC, fEntry));

		slideBySubstance(movingEntriesBySubstance, fEntry, MOVING_AVG_COUNT);
		slideByType(movingEntriesByType, fEntry, MOVING_AVG_COUNT);
		slideBySubstance(floatingEntriesBySubstance, fEntry, FLOATING_AVG_COUNT);
		slideByType(floatingEntriesByType, fEntry, FLOATING_AVG_COUNT);

		nextC.setMovingConsumptionBySubstance(calculateMovingConsumptionBySubstance(prevC, fEntry));
		nextC.setMovingConsumptionByType(calculateMovingConsumptionByType(prevC, fEntry));
		nextC.setFloatingConsumptionBySubstance(calculateFloatingConsumptionBySubstance(prevC, fEntry));
		nextC.setFloatingConsumptionByType(calculateFloatingConsumptionByType(prevC, fEntry));
	}

	@Override
	public void shiftPointers(Entry e) {
		super.shiftPointers(e);

		FuellingEntry f = (FuellingEntry) e;
		FuellingEntry.FuelType type = f.getFuelType();
		UnitConstants.Substance substance = f.getFuelType().getSubstance();

		initialByFuelType.putIfAbsent(type, f);
		initialBySubstance.putIfAbsent(substance, f);

		previousByFuelType.put(type, f);
		previousBySubstance.put(substance, f);
	}

	private Cost calculateCostLastRefuel(FuellingEntry entry) {
		Cost result = new Cost();
		FuellingEntry.FuelType type = entry.getFuelType();
		if (previousByFuelType.get(type) != null) {
			double mileage = entry.getMileageSI() - previousByFuelType.get(type).getMileageSI();
			result = Cost.divide(entry.getCost(), mileage);
			result = Cost.multiply(result, 100);
		}

		return result;
	}

	private HashMap<UnitConstants.Substance, Double> calculateTotalSubstanceVolume(NewGenFuelConsumption prevC, FuellingEntry entry) {
		// Total volume
		HashMap<UnitConstants.Substance, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalVolumeBySubstance());

		// Fuel substance needs to be taken into the account (as we can't combine liquids, solid, electricity,...)
		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		double volume = (result.get(substance) == null) ? 0 : result.get(substance);
		result.put(substance, volume + entry.getFuelQuantitySI());

		return result;
	}

	private HashMap<FuellingEntry.FuelType, Double> calculateTotalTypeVolume(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalVolumeByType());

		FuellingEntry.FuelType type = entry.getFuelType();
		double volume = (result.get(type) == null) ? 0 : result.get(type);
		result.put(type, volume + entry.getFuelQuantitySI());

		return result;
	}

	private HashMap<UnitConstants.Substance, Cost> calculateTotalSubstanceCost(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<UnitConstants.Substance, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalCostBySubstance());

		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		Cost cost = (result.get(substance) == null) ? new Cost() : result.get(substance);
		result.put(substance, Cost.add(cost, entry.getCost()));

		return result;
	}

	private HashMap<FuellingEntry.FuelType, Cost> calculateTotalTypeCost(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Cost> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getTotalCostByType());

		FuellingEntry.FuelType type = entry.getFuelType();
		Cost cost = (result.get(type) == null) ? new Cost() : result.get(type);
		result.put(type, Cost.add(cost, entry.getCost()));

		return result;
	}

	private HashMap<UnitConstants.Substance, Cost> calculateAverageCostBySubstance(NewGenFuelConsumption nextC, FuellingEntry entry) {
		HashMap<UnitConstants.Substance, Cost> result = new HashMap<>();
		HashMap<UnitConstants.Substance, Cost> tCosts = nextC.getTotalCostBySubstance();

		for (UnitConstants.Substance s: tCosts.keySet()) {
			FuellingEntry initial = initialBySubstance.get(s);
			if (initial == null) {
				continue;
			}

			double mileage = entry.getMileageSI() - initial.getMileageSI();
			if (mileage == 0.0) {
				continue;
			}

			// We need to subtract the initial refuelling from calculating average per distance
			Cost cost = Cost.subtract(tCosts.get(s), initial.getCost());
			cost = Cost.divide(cost, mileage);
			cost = Cost.multiply(cost, 100);
			result.put(s, cost);
		}

		return result;
	}

	private HashMap<FuellingEntry.FuelType, Cost> calculateAverageCostByType(NewGenFuelConsumption nextC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Cost> result = new HashMap<>();
		HashMap<FuellingEntry.FuelType, Cost> tCosts = nextC.getTotalCostByType();

		for (FuellingEntry.FuelType t: tCosts.keySet()) {
			FuellingEntry initial = initialByFuelType.get(t);
			if (initial == null) {
				continue;
			}

			double mileage = entry.getMileageSI() - initial.getMileageSI();
			if (mileage == 0.0) {
				continue;
			}

			// We need to subtract the initial refuelling from calculating average per distance
			Cost cost = Cost.subtract(tCosts.get(t), initial.getCost());
			cost = Cost.divide(cost, mileage);
			cost = Cost.multiply(cost, 100);
			result.put(t, cost);
		}

		return result;
	}

	private HashMap<UnitConstants.Substance, Double> calculateAverageConsumptionBySubstance(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<UnitConstants.Substance, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getAverageConsumptionBySubstance());

		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		FuellingEntry initial = initialBySubstance.get(substance);
		if (initial == null) {
			result.put(substance, 0.0);
		} else {
			// At this moment the totalVolume needs to be calculated
			double mileage = entry.getMileageSI() - initial.getMileageSI();
			double volume = ((NewGenFuelConsumption) entry.getConsumption()).getTotalVolumeBySubstance().get(substance);
			volume -= initialBySubstance.get(substance).getFuelQuantitySI();
			result.put(substance, 100 * volume / mileage); // Default unit: volume/100 * distance
		}
		return result;
	}

	private double calculateLastConsumption(FuellingEntry entry) {
		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		FuellingEntry previous = previousBySubstance.get(substance);
		if (previous == null) {
			return 0.0;
		} else {
			double mileage = entry.getMileageSI() - previous.getMileageSI();
			double volume = entry.getFuelQuantitySI();
			return 100 * volume / mileage; // Default unit: volume/100*distance
		}
	}

	private HashMap<FuellingEntry.FuelType, Double> calculateAverageConsumptionByType(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getAverageConsumptionByType());

		FuellingEntry.FuelType type = entry.getFuelType();
		FuellingEntry initial = initialByFuelType.get(type);
		if (initial == null) {
			result.put(type, 0.0);
		} else {
			// At this moment the totalVolume needs to be calculated
			double mileage = entry.getMileageSI() - initial.getMileageSI();
			double volume = ((NewGenFuelConsumption) entry.getConsumption()).getTotalVolumeByType().get(type);
			volume -= initialByFuelType.get(type).getFuelQuantitySI();
			result.put(type, 100 * volume / mileage); // Default unit is volume/100*distance
		}
		return result;
	}

	private HashMap<UnitConstants.Substance, Double> calculateMovingConsumptionBySubstance(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<UnitConstants.Substance, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getMovingConsumptionBySubstance());

		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		LinkedList<FuellingEntry> queue = movingEntriesBySubstance.get(substance);
		if (queue == null || queue.size() <= 1) {
			result.put(substance, 0.0);
		} else {
			double mileage = queue.getLast().getMileageSI() - queue.getFirst().getMileageSI();
			double volume = 0.0;
			for (FuellingEntry e : queue) {
				volume += e.getFuelQuantitySI();
			}
			volume -= queue.getFirst().getFuelQuantitySI();

			double consumption = volume / (mileage * 100);
			result.put(substance, consumption);
		}

		return result;
	}

	private HashMap<UnitConstants.Substance, Double> calculateFloatingConsumptionBySubstance(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<UnitConstants.Substance, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getFloatingConsumptionBySubstance());

		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		LinkedList<FuellingEntry> queue = floatingEntriesBySubstance.get(substance);
		int minLength = (FLOATING_AVG_CUT * 2) + 1;
		if (queue == null || queue.size() <= minLength) {
			result.put(substance, 0.0);
		} else {
			LinkedList<Double> values = new LinkedList<>();
			// Load all the values
			for (FuellingEntry e: queue) {
				values.add(e.getFuelConsumption().getLastConsumption());
			}
			// Sort all the values
			values.sort((Double d1, Double d2) -> Double.compare(d1, d2));

			// Cut min & max values
			for (int i = 0; i < FLOATING_AVG_CUT; i++) {
				values.removeFirst();
				values.removeLast();
			}

			double consumption = 0.0;
			// Calculate the average consumption
			for (Double d: values) {
				consumption += d;
			}
			consumption /= values.size();

			result.put(substance, consumption);
		}

		return result;
	}

	private HashMap<FuellingEntry.FuelType, Double> calculateMovingConsumptionByType(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getMovingConsumptionByType());

		FuellingEntry.FuelType type = entry.getFuelType();
		LinkedList<FuellingEntry> queue = movingEntriesBySubstance.get(type);
		if (queue == null || queue.size() <= 1) {
			result.put(type, 0.0);
		} else {
			double mileage = queue.getLast().getMileageSI() - queue.getFirst().getMileageSI();
			double volume = 0.0;
			for (FuellingEntry e : queue) {
				volume += e.getFuelQuantitySI();
			}
			volume -= queue.getFirst().getFuelQuantitySI();

			double consumption = volume / (mileage * 100);
			result.put(type, consumption);
		}

		return result;
	}

	private HashMap<FuellingEntry.FuelType, Double> calculateFloatingConsumptionByType(NewGenFuelConsumption prevC, FuellingEntry entry) {
		HashMap<FuellingEntry.FuelType, Double> result;
		result = (prevC == null) ? new HashMap<>() : new HashMap<>(prevC.getFloatingConsumptionByType());

		FuellingEntry.FuelType type = entry.getFuelType();
		LinkedList<FuellingEntry> queue = floatingEntriesBySubstance.get(type);
		int minLength = (FLOATING_AVG_CUT * 2) + 1;
		if (queue == null || queue.size() <= minLength) {
			result.put(type, 0.0);
		} else {
			LinkedList<Double> values = new LinkedList<>();
			// Load all the values
			for (FuellingEntry e: queue) {
				values.add(e.getFuelConsumption().getLastConsumption());
			}
			// Sort all the values
			values.sort((Double d1, Double d2) -> Double.compare(d1, d2));

			// Cut min & max values
			for (int i = 0; i < FLOATING_AVG_CUT; i++) {
				values.removeFirst();
				values.removeLast();
			}

			double consumption = 0.0;
			// Calculate the average consumption
			for (Double d: values) {
				consumption += d;
			}
			consumption /= values.size();

			result.put(type, consumption);
		}

		return result;
	}

	private void slideBySubstance(HashMap<UnitConstants.Substance, LinkedList<FuellingEntry>> entries, FuellingEntry entry, int length) {
		UnitConstants.Substance substance = entry.getFuelType().getSubstance();
		LinkedList<FuellingEntry> queue = entries.get(substance);

		if (queue == null) {
			queue = new LinkedList<>();
			entries.put(substance, queue);
		}

		queue.add(entry);
		if (queue.size() > length) {
			queue.removeFirst();
		}
	}

	private void slideByType(HashMap<FuellingEntry.FuelType, LinkedList<FuellingEntry>> entries, FuellingEntry entry, int length) {
		FuellingEntry.FuelType type = entry.getFuelType();
		LinkedList<FuellingEntry> queue = entries.get(type);

		if (queue == null) {
			queue = new LinkedList<>();
			entries.put(type, queue);
		}

		queue.add(entry);
		if (queue.size() > length) {
			queue.removeFirst();
		}
	}
}