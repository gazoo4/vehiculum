package sk.berops.android.vehiculum.gui.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.Vehiculum;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenConsumption;
import sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption;
import sk.berops.android.vehiculum.gui.MainActivity;

/**
 * @author Bernard Halas
 * @date 8/17/17
 */

public class StatsWriter {
	public static Garage garage = MainActivity.garage;
	public static Context context = Vehiculum.context;
	public static Preferences preferences = Preferences.getInstance();

	public static void generateRowCarSummary(TableLayout layout) {
		if (garage != null && garage.getActiveCar() != null) {
			String description = context.getString(R.string.activity_main_car);
			String nickname = garage.getActiveCar().getNickname();
			layout.addView(createStatRow(description, nickname));
		} else {
			layout.addView(createStatRow(context.getString(R.string.activity_main_garage_empty), ""));
		}
	}

	public static void generateRowTotalCosts(TableLayout layout) {
		NewGenConsumption c = garage.getActiveCar().getConsumption();
		if (c == null) {
			String message = context.getString(R.string.activity_main_no_expenses_message);
			layout.addView(createStatRow(message, null));
			return;
		}

		String description = context.getString(R.string.activity_main_total_costs);
		double value = c.getTotalCost().getPreferredValue();
		String unit = c.getTotalCost().getPreferredUnit().getUnitIsoCode();

		layout.addView(createStatRow(description, value, unit));
	}

	public static void generateRowAverageConsumption(TableLayout layout) {
		double avgConsumption;
		NewGenFuelConsumption c = garage.getActiveCar().getFuelConsumption();
		if (c == null) return;

		String description;
		double valueSI;
		double valueReport;
		UnitConstants.ConsumptionUnit unit;
		unit = preferences.getConsumptionUnit(garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType());

		// we should buy at least 2 different fuels in order to display the stats separately
		HashMap<FuellingEntry.FuelType, Double> map = new HashMap<>();
		Set<UnitConstants.Substance> substances = new HashSet<>();
		for (FuellingEntry.FuelType t : FuellingEntry.FuelType.values()) {
			avgConsumption = c.getAverageTypeConsumption().get(t);
			if (avgConsumption != 0.0) {
				map.put(t, avgConsumption);
				substances.add(t.getSubstance());
			}
		}

		if (map.size() > 1) {
			for (FuellingEntry.FuelType t : map.keySet()) {
				description = context.getString(R.string.activity_main_average);
				description += " ";
				description += t.getType();
				valueSI = map.get(t);
				valueReport = UnitConstants.convertUnitConsumptionFromSI(t, valueSI);
				unit = preferences.getConsumptionUnit(t);
				layout.addView(createStatRow(description, valueReport, unit.toUnitShort()));
			}
		}

		if (substances.size() == 1) {
			description = context.getString(R.string.activity_main_average_consumption);
		} else {
			description = context.getString(R.string.activity_main_combined_average);
		}

		for (UnitConstants.Substance s: substances) {
			valueSI = c.getAverageConsumption().get(s);
			valueReport = UnitConstants.convertUnitConsumptionFromSI(s, valueSI);
			unit = preferences.getConsumptionUnit(s);
			layout.addView(createStatRow(description + " " + s.name(), valueReport, unit.toUnitShort()));
		}
	}

	public static void generateRowTotalRelativeCosts(TableLayout layout) {
		NewGenConsumption c = garage.getActiveCar().getConsumption();
		if (c == null) return;

		String description = context.getString(R.string.activity_main_relative_costs);
		double valueSI = c.getAverageCost().getPreferredValue();
		UnitConstants.CostUnit unit = preferences.getCostUnit();

		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}

	public static void generateRowRelativeCosts(TableLayout layout) {
		FuellingEntry.FuelType t = garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType();
		NewGenFuelConsumption c = garage.getActiveCar().getFuelConsumption();

		String description = context.getString(R.string.activity_main_relative_costs_fuel);
		description += " ";
		description += t.toString();
		double valueSI = c.getAverageTypeConsumption().get(t);
		UnitConstants.CostUnit unit = preferences.getCostUnit();

		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}

	public static void generateRowLastCosts(TableLayout layout) {
		Car activeCar = garage.getActiveCar();
		LinkedList<FuellingEntry> entries = activeCar.getHistory().getFuellingEntries();

		if (entries.size() <= 1) {
			// We can't calculate the consumption if just one refuelling has been made
			return;
		}

		FuellingEntry e = entries.getLast();
		NewGenFuelConsumption c = activeCar.getFuelConsumption();
		FuellingEntry.FuelType type = e.getFuelType();

		if (activeCar.getHistory().getFuellingEntriesFiltered(e.getFuelType()).size() <= 1) {
			// We can't calculate the consumption if just one refuelling of this type has been made
			return;
		}

		try {
			double avgCost = c.getAverageCostByType().get(type).getPreferredValue();
			double lastCost = c.getLastCost().getPreferredValue();
			double relativeChange = (lastCost / avgCost - 0.8) / 0.4;
			int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange);

			String description = context.getString(R.string.activity_main_relative_since_last_refuel);
			UnitConstants.CostUnit unit = preferences.getCostUnit();

			double lastCostReport = UnitConstants.convertUnitCost(lastCost);
			layout.addView(createStatRow(description, lastCostReport, unit.getUnit(), color));
		} catch (NoSuchElementException ex) {
			Log.d("DEBUG", "Not enough history to generate stats");
		}
	}

	public static void generateRowLastConsumption(TableLayout layout) {
		Car activeCar = garage.getActiveCar();
		LinkedList<FuellingEntry> entries = activeCar.getHistory().getFuellingEntries();

		if (entries.size() <= 1) {
			// We can't calculate the consumption if just one refuelling has been made
			return;
		}

		FuellingEntry e = entries.getLast();
		NewGenFuelConsumption c = activeCar.getFuelConsumption();
		FuellingEntry.FuelType type = e.getFuelType();

		if (activeCar.getHistory().getFuellingEntriesFiltered(e.getFuelType()).size() <= 1) {
			// We can't calculate the consumption if just one refuelling of this type has been made
			return;
		}

		double avgConsumption = c.getAverageTypeConsumption().get(type);
		double lastConsumption = c.getLastConsumption();
		double relativeChange = (lastConsumption / avgConsumption - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange); //orange in the middle

		String description = context.getString(R.string.activity_main_since_last_refuel);
		UnitConstants.ConsumptionUnit unit = preferences.getConsumptionUnit(type);

		double lastConsumptionReport = UnitConstants.convertUnitConsumptionFromSI(type, lastConsumption);
		layout.addView(createStatRow(description, lastConsumptionReport, unit.toUnitShort(), color));
	}

	private static TableRow createStatRow(String description, double value) {
		String textValue = TextFormatter.format(value, "######.##");

		return createStatRow(description, textValue, null);
	}

	private static TableRow createStatRow(String description, double value, String unit) {
		String textValue = TextFormatter.format(value, "######.##");

		return createStatRow(description, textValue, unit);
	}

	private static TableRow createStatRow(String description, double value, int valueColor) {
		String textValue = TextFormatter.format(value, "######.##");

		return createStatRow(description, textValue, null, valueColor);
	}

	private static TableRow createStatRow(String description, double value, String unit, int valueColor) {
		String textValue = TextFormatter.format(value, "######.##");

		return createStatRow(description, textValue, unit, valueColor);
	}

	private static TableRow createStatRow(String description, String value) {
		return createStatRow(description, value, null, 0xFFFFFFFF);
	}

	private static TableRow createStatRow(String description, String value, String unit) {
		return createStatRow(description, value, unit, 0xFFFFFFFF);
	}

	private static TableRow createStatRow(String description, String value, String unit, int valueColor) {
		if (description == null) {
			return null;
		}
		TableRow row = new TableRow(context);
		TextView descriptionView = new TextView(context);
		TextView valueView = new TextView(context);
		TextView unitView = new TextView(context);
		valueView.setGravity(Gravity.END);
		unitView.setGravity(Gravity.START);

		// setTextAppearance(context, resID)
		// got deprecated in SDK version 23 in favor of
		// setTextAppearance(resID)
		if (Build.VERSION.SDK_INT < 23) {
			TextViewCompat.setTextAppearance(descriptionView, R.style.plain_text);
			TextViewCompat.setTextAppearance(valueView, R.style.plain_text_big);
			TextViewCompat.setTextAppearance(unitView, R.style.plain_text);
		} else {
			descriptionView.setTextAppearance(R.style.plain_text);
			valueView.setTextAppearance(R.style.plain_text_big);
			unitView.setTextAppearance(R.style.plain_text);
		}

		descriptionView.setText(description);
		valueView.setText(value);
		unitView.setText(unit);

		valueView.setTextColor(valueColor);
		valueView.setShadowLayer(15, 0, 0, valueColor);

		row.addView(descriptionView);

		TableRow.LayoutParams params;

		if (value == null) {
			// We just want to show the description
			// and we want to ignore value and unit
			params = (TableRow.LayoutParams) descriptionView.getLayoutParams();
			params.span = 3;
			descriptionView.setLayoutParams(params);
		} else if (unit == null) {
			// If value is non-zero, let's display it,
			// but ignore units
			valueView.setPadding(3, 3, 3, 3);
			row.addView(valueView);

			params = (TableRow.LayoutParams) valueView.getLayoutParams();
			params.span = 2;
			valueView.setLayoutParams(params);
		} else {
			// Show description, value and unit
			row.addView(valueView);
			unitView.setPadding(3, 3, 3, 3);
			row.addView(unitView);
		}

		return row;
	}
}