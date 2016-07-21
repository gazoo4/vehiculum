package sk.berops.android.fueller.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import java.util.LinkedList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.gui.MainActivity;

public class ActivityMaintenanceEdit extends ActivityMaintenanceAdd {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		maintenanceEntry = (MaintenanceEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		if (maintenanceEntry.getParts() == null) {
			maintenanceEntry.setParts(new LinkedList<ReplacementPart>());
		}
		entry = maintenanceEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextLaborCost.setText(Double.toString(maintenanceEntry.getLaborCost()));
		spinnerLaborCostCurrency.setSelection(maintenanceEntry.getLaborCostCurrency().getId());

		RadioButton button;
		switch (maintenanceEntry.getType()) {
		case ACCIDENT_REPAIR:
			button = (RadioButton) findViewById(R.id.activity_maintenance_type_accident);
			break;
		case PLANNED:
			button = (RadioButton) findViewById(R.id.activity_maintenance_type_planned);
			break;
		case UNPLANNED:
			button = (RadioButton) findViewById(R.id.activity_maintenance_type_unplanned);
			break;
		default:
			button = (RadioButton) findViewById(R.id.activity_maintenance_type_planned);
			Log.d("WARN", "Unknown maintenanceType");
			break;
		}
		button.setChecked(true);
	}
}
