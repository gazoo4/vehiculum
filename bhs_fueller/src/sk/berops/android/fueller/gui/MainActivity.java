package sk.berops.android.fueller.gui;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.engine.calculation.CalculationException;
import sk.berops.android.fueller.engine.calculation.FloatingConsumption;
import sk.berops.android.fueller.engine.calculation.SimpleConsumption;
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
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	
	public static Garage garage;
	public static DataHandler dataHandler;
	
	private TextView textQuickStat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dataHandler = new XMLHandler();
		attachGuiObjects();
		
		if (garage == null) {
			try {
				garage = dataHandler.loadGarage();
				Toast.makeText(getApplicationContext(), "Loaded car: "+ garage.getActiveCar().getNickname(), Toast.LENGTH_LONG).show();
				SimpleConsumption.calculateConsumption(garage.getActiveCar().getHistory());
				//TODO: set the following 5, true params in options
				FloatingConsumption.calculateConsumption(garage.getActiveCar().getHistory(), 5, true);
			} catch (FileNotFoundException e) {
				System.out.println("Old garage.xml file not found, is this the first run? Building new garage.....");
				throwAlertCreateGarage();
			} catch (CalculationException e) {
				e.getReason();
			}
		}
		
		generateQuickStat();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		generateQuickStat();
		if (garage != null && garage.getActiveCar() != null) {
			SimpleConsumption.calculateConsumption(garage.getActiveCar().getHistory());
		}
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
		textQuickStat = (TextView)findViewById(R.id.activity_main_quick_status);
	}
	
	public void generateQuickStat() {
		if (garage == null) return;
		
		Consumption c;
		Consumption.SinceLastRefuel s;
		double avgConsumption = 0;
		String stat = "";
		stat += "Car: "+ garage.getActiveCar().getNickname() +"\n";
		
		try {
			garage.getActiveCar().setConsumption(SimpleConsumption.getTotalAverage(garage.getActiveCar()));
		} catch (CalculationException e) {
			// TODO Generate a toast saying not enough refuellings done
			
			stat += e.getReason();
			textQuickStat.setText(stat);
			return;
		}
		
		String formattedVolume;
		DecimalFormat df = new DecimalFormat("##.###");
		
		for (FuelType t: FuelType.values()) {
			avgConsumption = garage.getActiveCar().getConsumption().getPerType().get(t);
			if (avgConsumption != 0) {
				formattedVolume = df.format(avgConsumption);
				stat += t.getType() +": "+ formattedVolume +" l/100 km\n";
			}
		}
		formattedVolume = df.format(garage.getActiveCar().getConsumption().getTotal());
		stat += "average: "+ formattedVolume +" l/100 km\n\n";
		
		try {
			s = SimpleConsumption.sinceLastRefuel(garage.getActiveCar().getHistory());
			formattedVolume = df.format(s.getConsumption());
			stat += "since last refuel: "+ formattedVolume +" l/100 km of "+ s.getFuelType().getType();
		} catch (CalculationException e) {
			// TODO Log an entry saying that not enough history of refuelling of a particular fuel type
		}
		textQuickStat.setText(stat);
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