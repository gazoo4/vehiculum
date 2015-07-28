package sk.berops.android.fueller.gui.burreaucratic;

import sk.berops.android.fueller.dataModel.expense.BurreaucraticEntry;
import sk.berops.android.fueller.gui.MainActivity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityBurreaucraticEdit extends ActivityBurreaucraticAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		burreaucraticEntry = (BurreaucraticEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		entry = burreaucraticEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextMileage.setText(Double.valueOf(burreaucraticEntry.getMileage()).toString());
		editTextCost.setText(Double.valueOf(burreaucraticEntry.getCost()).toString());
		editTextComment.setText(burreaucraticEntry.getComment());
		
		spinnerCurrency.setSelection(burreaucraticEntry.getCurrency().getId());
	}
}
