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
import sk.berops.android.vehiculum.engine.calculation.NewGenCalculator;
import sk.berops.android.vehiculum.engine.synchronization.controllers.MainController;
import sk.berops.android.vehiculum.gui.common.GuiUtils;
import sk.berops.android.vehiculum.gui.common.StatsWriter;
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
			NewGenCalculator.calculateAll(garage.getActiveCar());
		}
	}

	private void generateStatTable() {
		statsTable.removeAllViews();

		//TODO: this view shall be generated based on the settings

		StatsWriter.generateRowCarSummary(statsTable);
		if ((garage != null) && (garage.getActiveCar() != null)) {
			StatsWriter.generateRowTotalCosts(statsTable);
			StatsWriter.generateRowTotalRelativeCosts(statsTable);
			StatsWriter.generateRowAverageConsumption(statsTable);
			// StatsWriter.generateRowRelativeCosts(statsTable);
			StatsWriter.generateRowLastCosts(statsTable);
			StatsWriter.generateRowLastConsumption(statsTable);
		}
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