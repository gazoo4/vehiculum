package sk.berops.android.fueller.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.gui.burreaucratic.ActivityBurreaucraticAdd;
import sk.berops.android.fueller.gui.fuelling.ActivityRefuel;
import sk.berops.android.fueller.gui.insurance.ActivityInsuranceAdd;
import sk.berops.android.fueller.gui.maintenance.ActivityMaintenanceAdd;
import sk.berops.android.fueller.gui.maintenance.ActivityTyreChange;
import sk.berops.android.fueller.gui.service.ActivityServiceAdd;
import sk.berops.android.fueller.gui.toll.ActivityTollAdd;

public class ActivityEntryAdd extends Activity {

	Button buttonRefuel;
	Button buttonTyres;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry_add);

		attachGuiObjects();
		styleGuiObjects();
	}

	private void attachGuiObjects() {
		buttonRefuel = (Button) findViewById(R.id.activity_entry_add_button_refuel);
		buttonTyres = (Button) findViewById(R.id.activity_entry_add_button_tyres_change);
	}

	private void styleGuiObjects() {
		int drawableColor = 0xFFFFFFFF;
		buttonRefuel.getCompoundDrawables()[0].setTint(drawableColor);
		buttonTyres.getCompoundDrawables()[0].setTint(drawableColor);
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
		case R.id.activity_entry_add_button_burreaucratic:
			startActivity(new Intent(this, ActivityBurreaucraticAdd.class)); //this or getApplicationContext()
			break;
		}
	}
}

