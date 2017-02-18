package sk.berops.android.vehiculum.gui.service;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;
import sk.berops.android.vehiculum.gui.MainActivity;

public class ActivityServiceEdit extends ActivityServiceAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		serviceEntry = (ServiceEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		entry = serviceEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		spinnerServiceType.setSelection(serviceEntry.getType().getId());
	}
}
