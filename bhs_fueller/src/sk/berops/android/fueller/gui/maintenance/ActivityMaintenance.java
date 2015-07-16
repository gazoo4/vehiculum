package sk.berops.android.fueller.gui.maintenance;

import java.util.NoSuchElementException;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityMaintenance extends ActivityAddEventGeneric {

	EditText editTextLaborCost;
	
	protected MaintenanceEntry maintenanceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_maintenance);
		
		if (maintenanceEntry == null) {
			maintenanceEntry = new MaintenanceEntry();
			maintenanceEntry.setExpenseType(ExpenseType.MAINTENANCE);
		}
		
		super.entry = (Entry) this.maintenanceEntry;
		super.editMode = editMode;
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDisplayDate = (TextView) findViewById(R.id.activity_maintenance_date_text);
		editTextMileage = (EditText) findViewById(R.id.activity_maintenance_mileage);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_maintenance_distance_unit);
		editTextLaborCost = (EditText) findViewById(R.id.activity_maintenance_labor_cost);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_maintenance_labor_cost_currency);
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		editTextLaborCost.setHintTextColor(Colors.LIGHT_GREEN);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_maintenance_button_add:
			startActivity(new Intent(this, ActivityPartAdd.class));
			break;
		case R.id.activity_maintenance_button_delete:
			break;
		case R.id.activity_maintenance_button_commit:
			break;
		}
	}
}