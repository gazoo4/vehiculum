package sk.berops.android.vehiculum.gui.tyres;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.vehiculum.dataModel.expense.TyreChangeEntry;
import sk.berops.android.vehiculum.gui.MainActivity;

/**
 * @author Bernard Halas
 * @date 1/12/17
 */

public class ActivityTyreChangeEdit extends ActivityTyreChange {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		tyreChangeEntry = (TyreChangeEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		super.entry = tyreChangeEntry;
		super.editMode = true;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextLaborCost.setText(Double.toString(tyreChangeEntry.getLaborCost().getRecordedValue()));
		editTextSmallPartsCost.setText(Double.toString(tyreChangeEntry.getExtraMaterialCost().getRecordedValue()));
		editTextTyresCost.setText(Double.toString(tyreChangeEntry.getTyresCost().getRecordedValue()));
		spinnerCurrency.setSelection(tyreChangeEntry.getCost().getRecordedUnit().getId());
		updateTotalCost();
	}
}
