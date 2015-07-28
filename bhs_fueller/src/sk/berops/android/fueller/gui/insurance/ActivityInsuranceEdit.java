package sk.berops.android.fueller.gui.insurance;

import sk.berops.android.fueller.dataModel.expense.InsuranceEntry;
import sk.berops.android.fueller.gui.MainActivity;
import android.content.Intent;
import android.os.Bundle;

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
		editTextMileage.setText(Double.valueOf(insuranceEntry.getMileage()).toString());
		editTextCost.setText(Double.valueOf(insuranceEntry.getCost()).toString());
		editTextComment.setText(insuranceEntry.getComment());
		
		spinnerCurrency.setSelection(insuranceEntry.getCurrency().getId());
		spinnerInsuranceType.setSelection(insuranceEntry.getType().getId());
	}
}
