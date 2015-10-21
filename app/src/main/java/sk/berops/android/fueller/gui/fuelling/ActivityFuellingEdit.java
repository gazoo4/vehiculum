package sk.berops.android.fueller.gui.fuelling;

import android.content.Intent;
import android.os.Bundle;

import java.text.DateFormat;

import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;

public class ActivityFuellingEdit extends ActivityRefuel {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		fuellingEntry = (FuellingEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		super.entry = fuellingEntry;
		super.editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextMileage.setText(Double.valueOf(fuellingEntry.getMileage()).toString());
		editTextCost.setText(Double.valueOf(fuellingEntry.getCost()).toString());
		editTextComment.setText(fuellingEntry.getComment());
		editTextVolume.setText(Double.valueOf(fuellingEntry.getFuelVolume()).toString());
		textViewDisplayDate.setText(DateFormat.getDateInstance().format(fuellingEntry.getEventDate()));

		spinnerFuelType.setSelection(fuellingEntry.getFuelType().getId());
		refreshPrice();
	}
}
