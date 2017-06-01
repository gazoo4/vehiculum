package sk.berops.android.vehiculum.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.UnitConstants.CostUnit;
import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.calculation.FuelConsumption;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.vehiculum.engine.synchronization.controllers.MainController;
import sk.berops.android.vehiculum.engine.calculation.Calculator;
import sk.berops.android.vehiculum.gui.common.GuiUtils;
import sk.berops.android.vehiculum.gui.common.TextFormatter;
import sk.berops.android.vehiculum.gui.garage.ActivityGarageManagement;
import sk.berops.android.vehiculum.gui.preferences.PreferenceWithHeaders;
import sk.berops.android.vehiculum.gui.report.ActivityReportsNavigate;
import sk.berops.android.vehiculum.io.xml.ArchiveDataHandler;
import sk.berops.android.vehiculum.io.xml.DataHandler;
import sk.berops.android.vehiculum.io.xml.XMLHandler;

public class MainActivity extends DefaultActivity {

	public static Garage garage;
	public static DataHandler dataHandler;
	private static Preferences preferences = Preferences.getInstance();
	private static MainController controller = MainController.getInstance();

	private Button buttonRecordEvent;
	private Button buttonViewStats;
	private Button buttonEnterGarage;
	private View lineRecordEvent;
	private View lineViewStats;
	private View lineEnterGarage;
	private TableLayout statsTable;

	public final static int REQUEST_CODE_BACKUP = 1;
	public final static int REQUEST_CODE_RESTORE = 2;
	public final static int REQUEST_CODE_RESOLUTION = 3;
	public final static int REQUEST_CODE_CREATOR = 4;

	private static Context context;
	private final static String LOG_TAG = "General Error";

	public static void saveGarage(Activity activity) {
		String toast;
		try {
			getDataHandler(activity).saveGarage(garage);
			toast = activity.getResources().getString(R.string.activity_main_garage_saved_toast);
		} catch (IOException e) {
			toast = activity.getResources().getString(R.string.activity_main_garage_saving_error_toast);
		}
		Toast.makeText(activity.getApplication(), toast, Toast.LENGTH_SHORT).show();
	}

	public static Context getContext() {
		return context;
	}


	private static DataHandler getDataHandler(Activity activity) {
		if (dataHandler == null) {
			return new XMLHandler(activity);
		}

		return dataHandler;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getApplicationContext();

		attachGuiObjects();
		styleGuiObjects();

		dataHandler = getDataHandler(this);
		if (garage == null) {
			loadGarage();
		}

		refreshStats();
		generateStatTable();
	}

	@Override
	public void onResume() {
		super.onResume();

		initAfterLoad();
		refreshStats();
		generateStatTable();
	}

	private void loadGarage() {
		try {
			garage = dataHandler.loadGarage();
			initAfterLoad();
		} catch (IOException | NullPointerException e) {
			Log.d("DEBUG", "Could not load garage.xml");
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
			throwAlertCreateGarage();
		}
	}

	private void initAfterLoad() {
		if ((garage == null)
				|| garage.getActiveCar() == null) {
			buttonRecordEvent.setVisibility(View.GONE);
			lineRecordEvent.setVisibility(View.GONE);
			buttonViewStats.setVisibility(View.GONE);
			lineViewStats.setVisibility(View.GONE);
		} else if (garage.getActiveCar().getHistory().getEntries().size() == 0) {
			buttonRecordEvent.setVisibility(View.VISIBLE);
			lineRecordEvent.setVisibility(View.VISIBLE);
			buttonViewStats.setVisibility(View.GONE);
			lineViewStats.setVisibility(View.GONE);
		} else {
			garage.initAfterLoad();
			buttonRecordEvent.setVisibility(View.VISIBLE);
			lineRecordEvent.setVisibility(View.VISIBLE);
			buttonViewStats.setVisibility(View.VISIBLE);
			lineViewStats.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), "Garage initialized. Car loaded: " + garage.getActiveCar().getNickname(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void attachGuiObjects() {
		statsTable = (TableLayout) findViewById(R.id.activity_main_stats_table);
		buttonRecordEvent = (Button) findViewById(R.id.activity_main_button_entry_add);
		buttonViewStats = (Button) findViewById(R.id.activity_main_button_stats_view);
		buttonEnterGarage = (Button) findViewById(R.id.activity_main_button_garage_enter);

		lineRecordEvent = findViewById(R.id.activity_main_line_entry_add);
		lineViewStats = findViewById(R.id.activity_main_line_stats_view);
		lineEnterGarage = findViewById(R.id.activity_main_line_garage_enter);

		listButtons.add(buttonRecordEvent);
		listButtons.add(buttonViewStats);
		listButtons.add(buttonEnterGarage);
	}

	public void throwAlertCreateGarage() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
			generateRowLastCosts(statsTable);
			generateRowLastConsumption(statsTable);
		}
	}

	private void generateRowCarSummary(TableLayout layout) {

		if (garage != null && garage.getActiveCar() != null) {
			String description = getString(R.string.activity_main_car);
			String nickname = garage.getActiveCar().getNickname();
			layout.addView(createStatRow(description, nickname));
		} else {
			layout.addView(createStatRow(getString(R.string.activity_main_garage_empty), ""));
		}
	}

	private void generateRowTotalCosts(TableLayout layout) {
		Consumption c = garage.getActiveCar().getConsumption();
		if (c == null) {
			String message = getString(R.string.activity_main_no_expenses_message);
			layout.addView(createStatRow(message, null));
			return;
		}

		String description = getString(R.string.activity_main_total_costs);
		double value = c.getTotalCost();
		String unit = preferences.getCurrency().getUnitIsoCode();

		layout.addView(createStatRow(description, value, unit));
	}

	private void generateRowAverageConsumption(TableLayout layout) {
		double avgConsumption;
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();
		if (c == null) return;

		String description;
		double valueSI;
		double valueReport;
		UnitConstants.ConsumptionUnit unit;
		unit = preferences.getConsumptionUnit(c.getLastRefuelType());

		// we should buy at least 2 different fuels in order to display the stats separately
		HashMap<FuelType, Double> map = new HashMap<>();
		Set<UnitConstants.Substance> substances = new HashSet<>();
		for (FuelType t : c.getFuelTypes()) {
			avgConsumption = c.getAveragePerFuelType().get(t);
			if (avgConsumption != 0.0) {
				map.put(t, avgConsumption);
				substances.add(t.getSubstance());
			}
		}

		if (map.size() >= 2) {
			for (FuelType t : map.keySet()) {
				description = getString(R.string.activity_main_average);
				description += " ";
				description += t.getType();
				valueSI = map.get(t);
				valueReport = UnitConstants.convertUnitConsumptionFromSI(t, valueSI);
				unit = preferences.getConsumptionUnit(t);
				layout.addView(createStatRow(description, valueReport, unit.toUnitShort()));
			}
			description = getString(R.string.activity_main_combined_average);
		} else {
			description = getString(R.string.activity_main_average_consumption);
		}

		// If we use several fuels, but of different substances, we can't make a combined average
		if (substances.size() > 1) {
			return;
		}

		valueSI = c.getGrandAverage();
		valueReport = UnitConstants.convertUnitConsumptionFromSI(c.getFuelTypes().iterator().next(), valueSI);
		layout.addView(createStatRow(description, valueReport, unit.toUnitShort()));
	}

	private void generateRowTotalRelativeCosts(TableLayout layout) {
		Consumption c = garage.getActiveCar().getConsumption();
		if (c == null) return;

		String description = getString(R.string.activity_main_relative_costs);
		double valueSI = c.getAverageCost();
		CostUnit unit = preferences.getCostUnit();

		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}

	private void generateRowRelativeCosts(TableLayout layout) {
		FuelType t = garage.getActiveCar().getHistory().getFuellingEntries().getLast().getFuelType();
		FuelConsumption c = garage.getActiveCar().getFuelConsumption();

		String description = getString(R.string.activity_main_relative_costs_fuel);
		description += " ";
		description += t.toString();
		double valueSI = c.getAverageFuelCostPerFuelType().get(t);
		CostUnit unit = preferences.getCostUnit();

		double valueReport = UnitConstants.convertUnitCost(valueSI);
		layout.addView(createStatRow(description, valueReport, unit.getUnit()));
	}

	private void generateRowLastCosts(TableLayout layout) {
		Car activeCar = garage.getActiveCar();
		LinkedList<FuellingEntry> entries = activeCar.getHistory().getFuellingEntries();

		if (entries.size() <= 1) {
			// We can't calculate the consumption if just one refuelling has been made
			return;
		}

		FuellingEntry e = entries.getLast();
		FuelConsumption c = activeCar.getFuelConsumption();
		FuelType type = e.getFuelType();

		if (activeCar.getHistory().getFuellingEntriesFiltered(e.getFuelType()).size() <= 1) {
			// We can't calculate the consumption if just one refuelling of this type has been made
			return;
		}

		try {
			double avgCostSI = c.getAverageFuelCostPerFuelType().get(type);
			double lastCostSI = c.getCostSinceLastRefuelPerFuelType().get(type);
			double relativeChange = (lastCostSI / avgCostSI - 0.8) / 0.4;
			int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange);

			String description = getString(R.string.activity_main_relative_since_last_refuel);
			CostUnit unit = preferences.getCostUnit();

			double lastCostReport = UnitConstants.convertUnitCost(lastCostSI);
			layout.addView(createStatRow(description, lastCostReport, unit.getUnit(), color));
		} catch (NoSuchElementException ex) {
			Log.d("DEBUG", "Not enough history to generate stats");
		}
	}

	private void generateRowLastConsumption(TableLayout layout) {
		Car activeCar = garage.getActiveCar();
		LinkedList<FuellingEntry> entries = activeCar.getHistory().getFuellingEntries();

		if (entries.size() <= 1) {
			// We can't calculate the consumption if just one refuelling has been made
			return;
		}

		FuellingEntry e = entries.getLast();
		FuelConsumption c = activeCar.getFuelConsumption();
		FuelType type = e.getFuelType();

		if (activeCar.getHistory().getFuellingEntriesFiltered(e.getFuelType()).size() <= 1) {
			// We can't calculate the consumption if just one refuelling of this type has been made
			return;
		}

		double avgConsumptionSI = c.getAveragePerFuelType().get(type);
		double lastConsumptionSI = c.getAverageSinceLast();
		double relativeChange = (lastConsumptionSI / avgConsumptionSI - 0.8) / 0.4;
		int color = GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange); //orange in the middle

		String description = getString(R.string.activity_main_since_last_refuel);
		UnitConstants.ConsumptionUnit unit = preferences.getConsumptionUnit(type);

		double lastConsumptionReport = UnitConstants.convertUnitConsumptionFromSI(type, lastConsumptionSI);
		layout.addView(createStatRow(description, lastConsumptionReport, unit.toUnitShort(), color));
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
		return createStatRow(description, value, null, 0xFFFFFFFF);
	}

	private TableRow createStatRow(String description, String value, String unit) {
		return createStatRow(description, value, unit, 0xFFFFFFFF);
	}

	private TableRow createStatRow(String description, String value, String unit, int valueColor) {
		if (description == null) {
			return null;
		}
		TableRow row = new TableRow(this);
		TextView descriptionView = new TextView(this);
		TextView valueView = new TextView(this);
		TextView unitView = new TextView(this);
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
				// Open settings activity
				startActivity(new Intent(this, PreferenceWithHeaders.class));
				return true;
			case R.id.menu_main_action_export:
				// Export garage to a file
				ArchiveDataHandler archiveDataHandler = new ArchiveDataHandler(this);
				try {
					archiveDataHandler.saveToExternal(garage);
				} catch (IOException ex) {
					Log.d("ERROR", "Archiving failed: " + ex.getMessage());
				}
				return true;
			case R.id.menu_main_action_restore:
				// Restore garage from a file
				Intent intent = new Intent()
						.setType("application/zip")
						.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "select the file to load"), REQUEST_CODE_RESTORE);
				return true;
			case R.id.menu_main_action_feedback:
				// Send a feedback to the authors
				Intent i = new Intent(Intent.ACTION_SENDTO);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.menu_action_feedback_title));
				i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.menu_action_feedback_body));
				i.setData(Uri.parse("mailto:vehiculum@berops.com")); // or just "mailto:" for blank
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
				startActivity(i);
				return true;
			/*
			case R.id.menu_main_action_donate:
				AlertDialog dialog = new AlertDialog.Builder(this)
						.setTitle(R.string.fragment_donate_title)
						.setMessage(R.string.fragment_donate_message)
						.setNeutralButton(R.string.activity_generic_ok_hint, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								startActivity(new Intent(MainActivity.this, DonationActivity.class));
							}
						})
						.create();
				dialog.show();
				startActivity(new Intent(this, DonationActivity.class));
				return true;
				*/
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CODE_RESTORE:
				if (resultCode == RESULT_OK) {
					ArchiveDataHandler archiveDataHandler = new ArchiveDataHandler(this);
					try {
						garage = archiveDataHandler.restoreFrom(data.getData());
						initAfterLoad();
					} catch (IOException ex) {
						Log.e("ERROR", "Problem during restoring the archive: " + ex.getMessage());
					}
				}
				break;
			case REQUEST_CODE_CREATOR:
				// Called after a file is saved to GDrive
				if (resultCode == RESULT_OK) {
					Log.i(LOG_TAG, "Backup successfully saved to GDrive");
					//gDriveBackupHandler.setBackupRequested(false);
				}
				break;
			case REQUEST_CODE_RESOLUTION:
				if (resultCode == RESULT_OK) {
					Log.i(LOG_TAG, "GoogleApiClient authenticated successfully");
				}
				break;
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.activity_main_button_entry_add:
				startActivity(new Intent(this, ActivityEntryChoose.class));
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