package sk.berops.android.fueller.engine.calculation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import android.text.format.DateFormat;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class FuellingCalculator {
	//TODO: add to settings including the checks (see below the if clausules)
	public static final int movingPrecision = 6;
	public static final int floatingPrecision = 6;
	
	public static void calculate(LinkedList<FuellingEntry> fuellingEntries) {
		FuellingEntry entryFirst = null;
		FuellingEntry entryLast = new FuellingEntry();

		double totalVolume = 0.0;
		int fuellingCount = 0;
		double totalFuelCost = 0.0;
		double averageFuelPrice = 0.0;
		
		boolean movingEligible = true;
		boolean floatingEligible = true;
		
		TreeMap<FuelType, FuellingEntry> entryFirstPerFuelType = new TreeMap<FuelType, FuellingEntry>();
		TreeMap<FuelType, FuellingEntry> entryLastPerFuelType = new TreeMap<FuelType, FuellingEntry>();
		
		TreeMap<FuelType, Double> distanceSum = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> volumeSum = new TreeMap<FuelType, Double>();
		
		TreeMap<FuelType, LinkedList<Double>> movingVolume = new TreeMap<FuelType, LinkedList<Double>>();
		TreeMap<FuelType, LinkedList<Double>> movingDistance = new TreeMap<FuelType, LinkedList<Double>>();
		
		TreeMap<FuelType, ArrayList<Double>> floatingValues = new TreeMap<FuelType, ArrayList<Double>>();
		
		TreeMap<FuelType, Double> totalVolumePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Integer> fuellingCountPerFuelType = new TreeMap<FuelType, Integer>();
		TreeMap<FuelType, Double> averagePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> movingAveragePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> floatingAveragePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> totalFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> averageFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> costSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> averageFuelPricePerFuelType = new TreeMap<FuelType, Double>();
		
		if (movingPrecision < 0) {
			//TODO: these checks to be moved to the settings
			movingEligible = false;
		}
		
		if (movingPrecision < 4) {
			floatingEligible = false;
		}
		
		for (FuellingEntry e : fuellingEntries) {
			FuelType fuelType = e.getFuelType();
			
			if (fuellingCountPerFuelType.get(fuelType) == null) {
				totalVolumePerFuelType.put(fuelType, 0.0);
				fuellingCountPerFuelType.put(fuelType, 0);
				averagePerFuelType.put(fuelType, 0.0);
				movingAveragePerFuelType.put(fuelType, 0.0);
				floatingAveragePerFuelType.put(fuelType, 0.0);
				mileageSinceFirstRefuelPerFuelType.put(fuelType, 0.0);
				mileageSinceLastRefuelPerFuelType.put(fuelType, 0.0);
				totalFuelCostPerFuelType.put(fuelType, 0.0);
				totalVolumePerFuelType.put(fuelType, 0.0);
				averageFuelCostPerFuelType.put(fuelType, 0.0);
				costSinceLastRefuelPerFuelType.put(fuelType, 0.0);
				averageFuelPricePerFuelType.put(fuelType, 0.0);
				distanceSum.put(fuelType, 0.0);
				volumeSum.put(fuelType, 0.0);
				movingVolume.put(fuelType,new LinkedList<Double>());
				movingDistance.put(fuelType,new LinkedList<Double>());
				floatingValues.put(fuelType, new ArrayList<Double>());
			}
			
			
			FuelConsumption consumption = (FuelConsumption) e.getConsumption();
					
			consumption.setLastRefuelType(fuelType);
			
			totalVolume += e.getFuelVolumeSI();
			consumption.setTotalVolume(totalVolume);
			
			double typeTotal = 0.0;
			typeTotal = totalVolumePerFuelType.get(fuelType);
			typeTotal += e.getFuelVolumeSI();
			totalVolumePerFuelType.put(fuelType, typeTotal);
			TreeMap<FuelType, Double> totalVolumePerFuelTypeCopy = new TreeMap<FuelType, Double>(totalVolumePerFuelType);
			consumption.setTotalVolumePerFuelType(totalVolumePerFuelTypeCopy);
			
			consumption.setFuellingCount(++fuellingCount);
			
			int typeCount = fuellingCountPerFuelType.get(fuelType);
			fuellingCountPerFuelType.put(fuelType, ++typeCount);
			TreeMap<FuelType, Integer> fuellingCountPerFuelTypeCopy = new TreeMap<FuelType, Integer>(fuellingCountPerFuelType);
			consumption.setFuellingCountPerFuelType(fuellingCountPerFuelTypeCopy);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				consumption.setAverageSinceLast(0.0);
			} else {
				double mileageLast = entryLastPerFuelType.get(fuelType).getMileageSI();
				double averageSinceLast = e.getFuelVolume() / (e.getMileageSI() - mileageLast) * 100;
				consumption.setAverageSinceLast(averageSinceLast);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averagePerFuelType.put(fuelType, 0.0);
			} else {
				double volume = consumption.getTotalVolumePerFuelType().get(fuelType) - entryFirstPerFuelType.get(fuelType).getFuelVolumeSI();
				double distance = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				averagePerFuelType.put(fuelType, volume / distance * 100);
			}
			TreeMap<FuelType, Double> averagePerFuelTypeCopy = new TreeMap<FuelType, Double>(averagePerFuelType);
			consumption.setAveragePerFuelType(averagePerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setGrandAverage(0.0);
			} else {
				double grandAverage = 0.0;
				for (FuelType t : averagePerFuelType.keySet()) {
					grandAverage += averagePerFuelType.get(t);
				}
				consumption.setGrandAverage(grandAverage);
			}
			
			if (movingEligible) {
				if (movingVolume.get(fuelType).size() != 0) {
					volumeSum.put(fuelType, volumeSum.get(fuelType) + e.getFuelVolumeSI());
				}
				
				movingVolume.get(fuelType).add(Double.valueOf(e.getFuelVolumeSI()));
				movingDistance.get(fuelType).add(Double.valueOf(e.getMileageSI()));
				
				if (movingVolume.get(fuelType).size() > (movingPrecision + 1)) {
					volumeSum.put(fuelType, volumeSum.get(fuelType) - movingVolume.get(fuelType).getFirst().doubleValue());
					movingVolume.get(fuelType).removeFirst();
					movingDistance.get(fuelType).removeFirst();
				}
				
				if (movingVolume.get(fuelType).size() > 1) {
					double distance = movingDistance.get(fuelType).getLast().doubleValue() - movingDistance.get(fuelType).getFirst().doubleValue();
					distanceSum.put(fuelType, distance);
					double movingAverageType = volumeSum.get(fuelType) / distanceSum.get(fuelType) * 100;
					movingAveragePerFuelType.put(fuelType, movingAverageType);
				}
				TreeMap<FuelType, Double> movingAveragePerFuelTypeCopy = new TreeMap<FuelType, Double>(movingAveragePerFuelType);
				consumption.setMovingAveragePerFuelType(movingAveragePerFuelTypeCopy);
			}
			
			if (floatingEligible) {
				ArrayList<Double> values = floatingValues.get(fuelType);
				values.add(Double.valueOf(consumption.getAverageSinceLast()));
				if (values.size() > floatingPrecision + 1) {
					values.remove(0);
				}
				values = new ArrayList<Double>(values); // when cutting min/max, not to overwrite the array...
				
				if (values.size() >= 4) {
					//cut min & max
					double min = Double.MAX_VALUE;
					double max = Double.MIN_VALUE;
					int minIndex = -1;
					int maxIndex = -1;
					for (int i = 0; i < values.size() - 1; i++) {
						if (values.get(i).doubleValue() >= max) {
							max = values.get(i).doubleValue();
							maxIndex = i;
						}
						if (values.get(i).doubleValue() < min) {
							min = values.get(i).doubleValue();
							minIndex = i;
						}
					}
					if (minIndex >= 0) {
						values.remove(minIndex);
					}
					if (maxIndex >= 0) {
						values.remove(maxIndex);
					}
				}
				
				double sum = 0.0;
				for (Double d : values) {
					sum += d.doubleValue();
				}
				floatingAveragePerFuelType.put(fuelType, sum / values.size());
				
				TreeMap<FuelType, Double> floatingAveragePerFuelTypeCopy = new TreeMap<FuelType, Double>(floatingAveragePerFuelType);
				consumption.setFloatingAveragePerFuelType(floatingAveragePerFuelTypeCopy);
			}
			
			if (fuellingCount == 1) {
				consumption.setMileageSinceFirstRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryFirst.getMileageSI();
				consumption.setMileageSinceFirstRefuel(mileage);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				mileageSinceFirstRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				mileageSinceFirstRefuelPerFuelType.put(fuelType, mileage);
			}
			TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(mileageSinceFirstRefuelPerFuelType);
			consumption.setMileageSinceFirstRefuelPerFuelType(mileageSinceFirstRefuelPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setMileageSinceLastRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryLast.getMileageSI();
				consumption.setMileageSinceLastRefuel(mileage);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				mileageSinceLastRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryLastPerFuelType.get(fuelType).getMileageSI();
				mileageSinceLastRefuelPerFuelType.put(fuelType, mileage);
			}
			TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(mileageSinceLastRefuelPerFuelType);
			consumption.setMileageSinceLastRefuelPerFuelType(mileageSinceLastRefuelPerFuelTypeCopy);
			
			totalFuelCost += e.getCost();
			consumption.setTotalFuelCost(totalFuelCost);
			
			double totalFuelCostType = totalFuelCostPerFuelType.get(fuelType);
			totalFuelCostType += e.getCost();
			totalFuelCostPerFuelType.put(fuelType, totalFuelCostType);
			TreeMap<FuelType, Double> totalFuelCostPerFuelTypeCopy = new TreeMap<FuelType, Double>(totalFuelCostPerFuelType);
			consumption.setTotalFuelCostPerFuelType(totalFuelCostPerFuelTypeCopy);
			
			double averageFuelCost;
			if (fuellingCount == 1) {
				averageFuelCost = 0.0;
			} else {
				double fuelCost = totalFuelCost - entryFirst.getCost();
				double mileage = e.getMileageSI() - entryFirst.getMileageSI();
				averageFuelCost = fuelCost / mileage;
			}
			consumption.setAverageFuelCost(averageFuelCost);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averageFuelCostPerFuelType.put(fuelType, 0.0);
			} else {
				double fuelCost = totalFuelCostPerFuelType.get(fuelType) - entryFirstPerFuelType.get(fuelType).getCost();
				double mileage = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				double averageFuelCostType = fuelCost / mileage;
				averageFuelCostPerFuelType.put(fuelType, averageFuelCostType);
			}
			TreeMap<FuelType, Double> averageFuelCostPerFuelTypeCopy = new TreeMap<FuelType, Double>(averageFuelCostPerFuelType);
			consumption.setAverageFuelCostPerFuelType(averageFuelCostPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setCostSinceLastRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryLast.getMileageSI();
				double costSinceLastRefuel = e.getCost() / mileage;
				consumption.setCostSinceLastRefuel(costSinceLastRefuel);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				costSinceLastRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryLastPerFuelType.get(fuelType).getMileageSI();
				double costSinceLastRefuelType = e.getCost() / mileage;
				costSinceLastRefuelPerFuelType.put(fuelType, costSinceLastRefuelType);
			}
			TreeMap<FuelType, Double> costSinceLastRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(costSinceLastRefuelPerFuelType);
			consumption.setCostSinceLastRefuelPerFuelType(costSinceLastRefuelPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				averageFuelPrice = e.getFuelPrice();
			} else {
				double totalPrice = averageFuelPrice * (fuellingCount - 1);
				averageFuelPrice = (totalPrice + e.getFuelPrice()) / fuellingCount;
			}
			consumption.setAverageFuelPrice(averageFuelPrice);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averageFuelPricePerFuelType.put(fuelType, e.getFuelPrice());
			} else {
				double totalPriceType = averageFuelPricePerFuelType.get(fuelType) * (fuellingCountPerFuelType.get(fuelType) - 1);
				double averageFuelPriceType = (totalPriceType + e.getFuelPrice()) / fuellingCountPerFuelType.get(fuelType);
				averageFuelPricePerFuelType.put(fuelType, averageFuelPriceType);
			}
			TreeMap<FuelType, Double> averageFuelPricePerFuelTypeCopy = new TreeMap<FuelType, Double>(averageFuelPricePerFuelType);
			consumption.setAverageFuelPricePerFuelType(averageFuelPricePerFuelTypeCopy);
			
			if (entryFirst == null) {
				entryFirst = e;
			}
			if (entryFirstPerFuelType.get(fuelType) == null) {
				entryFirstPerFuelType.put(fuelType, e);
			}
			entryLast = e;
			entryLastPerFuelType.put(fuelType, e);
		}
	}
}
