package sk.berops.android.fueller.gui.burreaucratic;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.fueller.dataModel.expense.BurreaucraticEntry;
import sk.berops.android.fueller.gui.MainActivity;

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
}
