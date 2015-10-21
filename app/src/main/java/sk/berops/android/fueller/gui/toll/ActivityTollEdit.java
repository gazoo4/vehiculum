package sk.berops.android.fueller.gui.toll;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.fueller.dataModel.expense.TollEntry;
import sk.berops.android.fueller.gui.MainActivity;

public class ActivityTollEdit extends ActivityTollAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		tollEntry = (TollEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		entry = tollEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextMileage.setText(Double.valueOf(tollEntry.getMileage()).toString());
		editTextCost.setText(Double.valueOf(tollEntry.getCost()).toString());
		editTextComment.setText(tollEntry.getComment());
		
		spinnerCurrency.setSelection(tollEntry.getCurrency().getId());
		spinnerTollType.setSelection(tollEntry.getType().getId());
	}
}
