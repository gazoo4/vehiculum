package sk.berops.android.fueller.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.HashMap;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.UnitConstants;
import sk.berops.android.fueller.dataModel.UnitConstants.ConsumptionUnit;
import sk.berops.android.fueller.dataModel.UnitConstants.CostUnit;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.engine.calculation.Calculator;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TextFormatter;
import sk.berops.android.fueller.gui.garage.ActivityGarageManagement;
import sk.berops.android.fueller.gui.preferences.PreferenceWithHeaders;
import sk.berops.android.fueller.gui.report.ActivityReportsNavigate;
import sk.berops.android.fueller.io.DataHandler;
import sk.berops.android.fueller.io.xml.XMLHandler;

public class MainActivity extends Activity {
	
	public static Garage garage;
	public static DataHandler dataHandler;
	private static Preferences preferences = Preferences.getInstance();
	
	private TableLayout statsTable;
	private TextView textViewHeader;
	
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
		String unit = preferences.getCurrency().getUnit();
		
		layout.addView(createStatRow(description, value, unit));
	}
	
	private void generateRowAverageConsumption(TableLayout layout) {
		double avgConsumption = 0;
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		if (c == null) return;
		
		String description;
		double valueSI;
		double valueReport;
		ConsumptionUnit unit;
		unit = preferences.getConsumptionUnit();
		
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
				valueSI = map.get(t);
				valueReport = UnitConstants.convertUnitConsumption(valueSI);
					
				layout.addView(createStatRow(description, valueReport, unit.getUnit()));
			}
		}
		
		description = getString(R.string.activity_main_average_combined);
		valueSI = c.getGrandAverage();
		valueReport = UnitConstants.convertUnitConsumption(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}
	
	private void generateRowTotalRelativeCosts(TableLayout layout) {
		Consumption c = garage.getActiveCar().getConsumption();
		if (c == null) return;
		
		String description = getString(R.string.activity_main_relative_costs);
		double valueSI = c.getAverageCost();
		CostUnit unit = preferences.getCostUnit();
		System.out.println(unit.getUnit());
		
		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}
	
	private void generateRowRelativeCosts(TableLayout layout) {
		FuelType t = garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		
		String description = getString(R.string.activity_main_relative_costs_fuel);
		description += " ";
		description += t.toString();
		double valueSI = c.getAverageFuelCostPerFuelType().get(t).doubleValue();
		CostUnit unit = preferences.getCostUnit();
		
		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}
	
	private void generateRowLastCosts(TableLayout layout) {
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgCostSI = c.getAverageFuelCostPerFuelType().get(type);
		double lastCostSI = c.getCostSinceLastRefuel();
		double relativeChange = (lastCostSI / avgCostSI - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange);
		
		String description = getString(R.string.activity_main_relative_since_last_refuel);
		CostUnit unit = preferences.getCostUnit();
		
		double lastCostReport = UnitConstants.convertUnitCost(lastCostSI);
		layout.addView(createStatRow(description, lastCostReport, unit.getUnit(), color));
	}
	
	private void generateRowLastConsumption(TableLayout layout) {
		if (garage.getActiveCar().getHistory().getFuellingEntries().size() == 0) return;
		
		FuellingEntry e = garage.getActiveCar().getHistory().getFuellingEntries().getLast();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		FuelType type = e.getFuelType();
		double avgConsumptionSI = c.getAveragePerFuelType().get(type);
		double lastConsumptionSI = c.getAverageSinceLast();
		double relativeChange = (lastConsumptionSI / avgConsumptionSI - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange); //orange in the middle
		
		String description = getString(R.string.activity_main_since_last_refuel);
		String unit = preferences.getConsumptionUnit().getUnit();
		
		double lastConsumptionReport = UnitConstants.convertUnitConsumption(lastConsumptionSI);
		layout.addView(createStatRow(description, lastConsumptionReport, unit, color));
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
				
		descriptionView.setText(description);
		valueView.setText(value);
		if (unit != null) {
			unitView.setText(unit);
		}
		
		descriptionView.setTextAppearance(this, R.style.plain_text);
		valueView.setTextAppearance(this, R.style.plain_text_big);
		unitView.setTextAppearance(this, R.style.plain_text_small);
		
		valueView.setTextColor(valueColor);
		valueView.setShadowLayer(15, 0, 0, valueColor);
		
		row.addView(descriptionView);
		
		TableRow.LayoutParams params;
		
		if (unit == null) {
			valueView.setPadding(3, 3, 3, 3);
			row.addView(valueView);
			
			params = (TableRow.LayoutParams) valueView.getLayoutParams();
			params.span = 2;
			valueView.setLayoutParams(params);
			
		} else {
			row.addView(valueView);
			unitView.setPadding(3, 3, 3, 3);
			row.addView(unitView);
		}
		
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
			startActivity(new Intent(this, ActivityReportsNavigate.class));
			break;
		case R.id.activity_main_button_garage_enter:
			startActivity(new Intent(this, ActivityGarageManagement.class));
			break;
		}
	}
}