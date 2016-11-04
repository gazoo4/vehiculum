package sk.berops.android.fueller.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.gui.bureaucratic.ActivityBureaucraticAdd;
import sk.berops.android.fueller.gui.fuelling.ActivityRefuel;
import sk.berops.android.fueller.gui.insurance.ActivityInsuranceAdd;
import sk.berops.android.fueller.gui.maintenance.ActivityMaintenanceAdd;
import sk.berops.android.fueller.gui.maintenance.ActivityTyreChange;
import sk.berops.android.fueller.gui.service.ActivityServiceAdd;
import sk.berops.android.fueller.gui.toll.ActivityTollAdd;

public class ActivityEntryAdd extends DefaultActivity {

	Button buttonRefuel;
	Button buttonTyres;
	Button buttonMaintenance;
	Button buttonService;
	Button buttonToll;
	Button buttonInsurance;
	Button buttonBureaucratic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_entry_add);
	}

	@Override
	protected void attachGuiObjects() {
		buttonRefuel = (Button) findViewById(R.id.activity_entry_add_button_refuel);
		buttonTyres = (Button) findViewById(R.id.activity_entry_add_button_tyres_change);
		buttonMaintenance = (Button) findViewById(R.id.activity_entry_add_button_maintenance);
		buttonService = (Button) findViewById(R.id.activity_entry_add_button_service);
		buttonToll = (Button) findViewById(R.id.activity_entry_add_button_toll);
		buttonInsurance = (Button) findViewById(R.id.activity_entry_add_button_insurance);
		buttonBureaucratic = (Button) findViewById(R.id.activity_entry_add_button_bureaucratic);

		listButtons.add(buttonRefuel);
		listButtons.add(buttonTyres);
		listButtons.add(buttonMaintenance);
		listButtons.add(buttonService);
		listButtons.add(buttonToll);
		listButtons.add(buttonInsurance);
		listButtons.add(buttonBureaucratic);

	}

	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_entry_add_button_refuel:
			startActivity(new Intent(this, ActivityRefuel.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_tyres_change:
			startActivity(new Intent(this, ActivityTyreChange.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_maintenance:
			startActivity(new Intent(this, ActivityMaintenanceAdd.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_service:
			startActivity(new Intent(this, ActivityServiceAdd.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_toll:
			startActivity(new Intent(this, ActivityTollAdd.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_insurance:
			startActivity(new Intent(this, ActivityInsuranceAdd.class)); //this or getApplicationContext()
			break;
		case R.id.activity_entry_add_button_bureaucratic:
			startActivity(new Intent(this, ActivityBureaucraticAdd.class)); //this or getApplicationContext()
			break;
		}
	}
}