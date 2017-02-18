package sk.berops.android.vehiculum.gui.insurance;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;
import sk.berops.android.vehiculum.gui.MainActivity;

public class ActivityInsuranceEdit extends ActivityInsuranceAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		insuranceEntry = (InsuranceEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		entry = insuranceEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		spinnerInsuranceType.setSelection(insuranceEntry.getType().getId());
	}
}
