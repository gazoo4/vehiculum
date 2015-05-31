package sk.berops.android.fueller.gui;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.fueller.configuration.UnitSettings;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.engine.calculation.CalculationException;
import sk.berops.android.fueller.engine.calculation.Calculator;
import sk.berops.android.fueller.engine.calculation.FloatingConsumption;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TextFormatter;
import sk.berops.android.fueller.gui.fuelling.ActivityRefuel;
import sk.berops.android.fueller.gui.garage.ActivityGarageManagement;
import sk.berops.android.fueller.gui.preferences.PreferenceWithHeaders;
import sk.berops.android.fueller.gui.report.ActivityStatsShow;
import sk.berops.android.fueller.io.DataHandler;
import sk.berops.android.fueller.io.xml.XMLHandler;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.R.id;
import sk.berops.android.fueller.R.layout;
import sk.berops.android.fueller.R.menu;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.TableLayout.LayoutParams;

public class MainActivity extends Activity {
	
	public static Garage garage;
	public static DataHandler dataHandler;
	private static UnitSettings settings = UnitSettings.getInstance();
	
	private TableLayout statsTable;
	private TextView textViewHeader;
	private TextView textViewCarNickname;
	private TextView textViewSinceLast;
	private TextView textViewSinceLastUnit;
	private TextView textViewConsumptionCombined;
	private TextView textViewConsumptionCombinedUnit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dataHandler = new XMLHandler();
		attachGuiObjects();
		styleGuiObjects();
		
		if (garage == null) {
			try {
				garage = dataHandler.loadGarage();
				Log.d("DEBUG", garage.getActiveCar().getNickname());
				Toast.makeText(getApplicationContext(), "Loaded car: "+ garage.getActiveCar().getNickname(), Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {
				Log.d("DEBUG", "Old garage.xml file not found, is this the first run? Building new garage");
				throwAlertCreateGarage();
			} catch (NullPointerException e) {
				if (garage == null) {
					Log.d("DEBUG", "Garage failed to load");
				} else if (garage.getActiveCar() == null) {
					Log.d("DEBUG", "No car loaded");
				}
			}
		}
		
		refreshStats();
		generateStatTable();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshStats();
		generateStatTable();
	}
	
	public void throwAlertCreateGarage() {
		AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
		alertDialog.setMessage(getResources().getString(R.string.activity_main_create_garage_alert));
		alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "Creating garage... ", Toast.LENGTH_LONG).show();
				garage = new Garage();
				startActivity(new Intent(MainActivity.this, ActivityGarageManagement.class));
			}
		});
		
		alertDialog.show();
	}
	
	public void attachGuiObjects() {
		statsTable = (TableLayout) findViewById(R.id.activity_main_stats_table);
		textViewHeader = (TextView) findViewById(R.id.activity_main_header);
		textViewCarNickname = (TextView) findViewById(R.id.activity_main_car_nickname);
		textViewSinceLast = (TextView) findViewById(R.id.activity_main_since_last_consumption_value);
		textViewSinceLastUnit = (TextView) findViewById(R.id.activity_main_since_last_consumption_unit);
		textViewConsumptionCombined = (TextView) findViewById(R.id.activity_main_consumption_combined_value);
		textViewConsumptionCombinedUnit = (TextView) findViewById(R.id.activity_main_consumption_combined_unit);
	}
	
	public void styleGuiObjects() {
		textViewHeader.setTypeface(Fonts.getGomariceFont(this));
	}
	
	private void refreshStats() {
		if ((garage != null) && (garage.getActiveCar() != null)) {
			Calculator.calculateAll(garage.getActiveCar().getHistory());
		}
	}
	
	private void generateStatTable() {
		statsTable.removeAllViews();
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		
		//TODO: this view shall be generated based on the settings
		
		generateRowCarSummary(statsTable);
		if ((garage != null) && (garage.getActiveCar() != null)) {
			generateRowTotalCosts(statsTable);
			generateRowTotalRelativeCosts(statsTable);
			generateRowAverageConsumption(statsTable);
			//generateRowRelativeCosts(statsTable);
			//generateRowLastCosts(statsTable);
			generateRowLastConsumption(statsTable);
		}
	}
	
	private void generateRowCarSummary(TableLayout layout) {
		
		if (garage != null && garage.getActiveCar() != null) {
			String description = getString(R.string.activity_main_car);
			String nickname = garage.getActiveCar().getNickname();
			layout.addView(createStatRow(description, nickname));
		} else {
			layout.addView(createStatRow(getString(R.string.activity_main_garage_empty),""));
		}
	}
	
	private void generateRowTotalCosts(TableLayout layout) {
		Consumption c = garage.getActiveCar().getConsumption();
		if (c == null) return;
		
		String description = getString(R.string.activity_main_total_costs);
		double value = c.getTotalCost();
		String unit = settings.getCurrency().getUnit();
		
		layout.addView(createStatRow(description, value, unit));
	}
	
	private void generateRowAverageConsumption(TableLayout layout) {
		double avgConsumption = 0;
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		if (c == null) return;
		
		String description;
		double value;
		String unit;
		
		// we should buy at least 2 different fuels in order to display the stats separately
		HashMap<FuelType, Double> map = new HashMap<FuelType, Double>();
		for (FuelType t: c.getFuelTypes()) {
			avgConsumption = c.getAveragePerFuelType().get(t).doubleValue();
			if (avgConsumption != 0.0) {
				map.put(t,  avgConsumption);
			}
		}
		
		if (map.size() >= 2) {
			for (FuelType t: map.keySet()) {
				description = getString(R.string.activity_main_average);
				description += " ";
				description += t.getType().toString();
				value = map.get(t);
				unit = settings.getConsumptionUnit().getUnit();
					
				layout.addView(createStatRow(description, value, unit));
			}
		}
		
		description = getString(R.string.activity_main_average_combined);
		value = c.getGrandAverage();
		unit = settings.getConsumptionUnit().getUnit();
		
		layout.addView(createStatRow(description, value, unit));
	}
	
	private void generateRowTotalRelativeCosts(TableLayout layout) {
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		if (c == null) return;
		
		String description = getString(R.string.activity_main_relative_costs);
		double value = c.getAverageFuelCost();
		String unit = settings.getCurrency().getUnit();
		unit += "/100 ";
		unit += settings.getDistanceUnit().getUnit();
		
		layout.addView(createStatRow(description, value, unit));
	}
	
	private void generateRowRelativeCosts(TableLayout layout) {
		FuelType t = garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		
		String description = getString(R.string.activity_main_relative_costs_fuel);
		description += " ";
		description += t.toString();
		double value = c.getAverageFuelCostPerFuelType().get(t).doubleValue();
		String unit = settings.getCurrency().getUnit();
		unit += "/100 ";
		unit += settings.getDistanceUnit();
		
		layout.addView(createStatRow(description, value, unit));
	}
	
	private void generateRowLastCosts(TableLayout layout) {
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgCost = c.getAverageFuelCostPerFuelType().get(type);
		double lastCost = c.getCostSinceLastRefuel();
		double relativeChange = (lastCost / avgCost - 0.8) / 0.4;
		System.out.println("Relative Distance: "+ relativeChange);
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange);
		
		String description = getString(R.string.activity_main_relative_since_last_refuel);
		String unit = settings.getCurrency().getUnit();
		unit += "/100 ";
		unit += settings.getDistanceUnit().getUnit();
		
		layout.addView(createStatRow(description, lastCost, unit, color));
	}
	
	private void generateRowLastConsumption(TableLayout layout) {
		if (garage.getActiveCar().getHistory().getFuellingEntries().size() == 0) return;
		
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgConsumption = c.getAveragePerFuelType().get(type);
		double lastConsumption = c.getAverageSinceLast();
		double relativeChange = (lastConsumption / avgConsumption - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange); //orange in the middle
		
		String description = getString(R.string.activity_main_since_last_refuel);
		String unit = settings.getConsumptionUnit().getUnit();
		
		layout.addView(createStatRow(description, lastConsumption, unit, color));
	}
	
	private TableRow createStatRow(String description, double value) {
		String textValue = TextFormatter.format(value, "######.##");
		
		return createStatRow(description, textValue, null);
	}
	
	private TableRow createStatRow(String description, double value, String unit) {
		String textValue = TextFormatter.format(value, "######.##");
		
		return createStatRow(description, textValue, unit);
	}
	
	private TableRow createStatRow(String description, double value, int valueColor) {
		String textValue = TextFormatter.format(value, "######.##");
		
		return createStatRow(description, textValue, null, valueColor);
	}
	
	private TableRow createStatRow(String description, double value, String unit, int valueColor) {
		String textValue = TextFormatter.format(value, "######.##");
		
		return createStatRow(description, textValue, unit, valueColor);
	}
	
	private TableRow createStatRow(String description, String value) {
		return createStatRow(description, value, null, 0xffffffff);
	}
	
	private TableRow createStatRow(String description, String value, String unit) {
		return createStatRow(description, value, unit, 0xffffffff);
	}
	
	private TableRow createStatRow(String description, String value, String unit, int valueColor) {
		TableRow row = new TableRow(this);
		TextView descriptionView = new TextView(this);
		TextView valueView = new TextView(this);
		TextView unitView = new TextView(this);
		valueView.setGravity(Gravity.END);
		unitView.setGravity(Gravity.START);
	
		if (unit == null) {
			unit = "";
		}
		
		descriptionView.setText(description);
		valueView.setText(value);
		unitView.setText(unit);
		
		descriptionView.setTextAppearance(this, R.style.plain_text);
		valueView.setTextAppearance(this, R.style.plain_text_big);
		unitView.setTextAppearance(this, R.style.plain_text_small);
		
		valueView.setTextColor(valueColor);
		valueView.setShadowLayer(15, 0, 0, valueColor);
		
		unitView.setPadding(3, 3, 3, 3);
		
		row.addView(descriptionView);
		row.addView(valueView);
		row.addView(unitView);
		return row;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_main_action_settings:
			startActivity(new Intent(this, PreferenceWithHeaders.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_main_button_entry_add:
			startActivity(new Intent(this, ActivityEntryAdd.class));
			break;
		case R.id.activity_main_button_stats_view:
			startActivity(new Intent(this, ActivityStatsShow.class));
			break;
		case R.id.activity_main_button_garage_enter:
			startActivity(new Intent(this, ActivityGarageManagement.class));
			break;
		}
	}
}