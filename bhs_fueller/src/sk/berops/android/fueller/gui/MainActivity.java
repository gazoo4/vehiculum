package sk.berops.android.fueller.gui;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.LinkedList;

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
import sk.berops.android.fueller.gui.report.ActivityStatsShow;
import sk.berops.android.fueller.io.DataHandler;
import sk.berops.android.fueller.io.xml.XMLHandler;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.R.id;
import sk.berops.android.fueller.R.layout;
import sk.berops.android.fueller.R.menu;
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
	
	private TableLayout statsTable;
	private TextView textViewHeader;
	private TextView textViewCarNickname;
	private TextView textViewSinceLast;
	private TextView textViewConsumptionCombined;
	
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
				System.out.println("Old garage.xml file not found, is this the first run? Building new garage.....");
				throwAlertCreateGarage();
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
		textViewConsumptionCombined = (TextView) findViewById(R.id.activity_main_consumption_combined_value);
	}
	
	public void styleGuiObjects() {
		textViewHeader.setTypeface(Fonts.getGomariceFont(this));
	}
	
	private void refreshStats() {
		Calculator.calculateAll(garage.getActiveCar().getHistory());
	}
	
	private void generateStatTable() {
		statsTable.removeAllViews();
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		
		generateRowCarSummary(statsTable);
		generateRowTotalCosts(statsTable);
		generateRowAverageConsumption(statsTable);
		generateRowTotalRelativeCosts(statsTable);
		generateRowRelativeCosts(statsTable);
		generateRowLastCosts(statsTable);
		generateRowLastConsumption(statsTable);
	}
	
	private void generateRowCarSummary(TableLayout layout) {
		layout.addView(createStatRow(getString(R.string.activity_main_car), garage.getActiveCar().getNickname()));
	}
	
	private void generateRowTotalCosts(TableLayout layout) {
		Consumption c = garage.getActiveCar().getConsumption();
		
		layout.addView(createStatRow(getString(R.string.activity_main_total_costs), c.getTotalCost()));
	}
	
	private void generateRowAverageConsumption(TableLayout layout) {
		double avgConsumption = 0;
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		for (FuelType t: c.getFuelTypes()) {
			avgConsumption = c.getAveragePerFuelType().get(t).doubleValue();
			if (avgConsumption != 0.0) {
				layout.addView(createStatRow(getString(R.string.activity_main_average) +" "+ t.getType().toString(), avgConsumption));
			}
		}
		layout.addView(createStatRow(getString(R.string.activity_main_average_combined), c.getGrandAverage()));
	}
	
	private void generateRowTotalRelativeCosts(TableLayout layout) {
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();

		layout.addView(createStatRow(getString(R.string.activity_main_relative_costs), c.getAverageFuelCost()));
	}
	
	private void generateRowRelativeCosts(TableLayout layout) {
		FuelType t = garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		
		layout.addView(createStatRow(getString(R.string.activity_main_relative_costs_fuel) +" "+ t.toString(), c.getAverageFuelCostPerFuelType().get(t).doubleValue()));
	}
	
	private void generateRowLastCosts(TableLayout layout) {
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgCost = c.getAverageFuelCostPerFuelType().get(type);
		double lastCost = c.getCostSinceLastRefuel();
		double relativeChange = (lastCost / avgCost - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange);
		layout.addView(createStatRow(getString(R.string.activity_main_relative_since_last_refuel), lastCost, color));
	}
	
	private void generateRowLastConsumption(TableLayout layout) {
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgConsumption = c.getAveragePerFuelType().get(type);
		double lastConsumption = c.getAverageSinceLast();
		double relativeChange = (lastConsumption / avgConsumption - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange); //orange in the middle
		layout.addView(createStatRow(getString(R.string.activity_main_since_last_refuel), lastConsumption, color));
	}
	
	private TableRow createStatRow(String description, double value) {
		String textValue = TextFormatter.formatValue(value, "######.##");
		
		return createStatRow(description, textValue);
	}
	
	private TableRow createStatRow(String description, double value, int valueColor) {
		String textValue = TextFormatter.formatValue(value, "######.##");
		
		return createStatRow(description, textValue, valueColor);
	}
	
	private TableRow createStatRow(String description, String value) {
		return createStatRow(description, value, 0xffffffff);
	}
	
	private TableRow createStatRow(String description, String value, int valueColor) {
		TableRow row = new TableRow(this);
		TextView descriptionView = new TextView(this);
		TextView valueView = new TextView(this);
		valueView.setGravity(Gravity.RIGHT);
		
		descriptionView.setText(description);
		valueView.setText(value);
		
		descriptionView.setTextAppearance(this, R.style.plain_text);
		valueView.setTextAppearance(this, R.style.plain_text_big);
		
		valueView.setTextColor(valueColor);
		valueView.setShadowLayer(15, 0, 0, valueColor);
		
		row.addView(descriptionView);
		row.addView(valueView);
		return row;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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