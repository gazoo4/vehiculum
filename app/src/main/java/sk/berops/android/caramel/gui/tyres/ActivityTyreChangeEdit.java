package sk.berops.android.caramel.gui.tyres;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.caramel.dataModel.expense.TyreChangeEntry;
import sk.berops.android.caramel.gui.MainActivity;

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
		editTextLaborCost.setText(Double.toString(tyreChangeEntry.getLaborCost()));
		editTextSmallPartsCost.setText(Double.toString(tyreChangeEntry.getExtraMaterialCost()));
		editTextTyresCost.setText(Double.toString(tyreChangeEntry.getTyresCost()));
		spinnerCurrency.setSelection(tyreChangeEntry.getCurrency().getId());
		updateTotalCost();
	}
}
