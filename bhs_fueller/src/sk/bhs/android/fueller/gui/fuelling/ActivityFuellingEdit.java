package sk.bhs.android.fueller.gui.fuelling;

import java.text.DateFormat;

import sk.bhs.android.fueller.R;
import sk.bhs.android.fueller.dataModel.expense.FuellingEntry;
import sk.bhs.android.fueller.gui.ActivityEntryAdd;
import sk.bhs.android.fueller.gui.MainActivity;
import sk.bhs.android.fueller.gui.fuelling.ActivityRefuel.PriceCalculateListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityFuellingEdit extends ActivityRefuel {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", -1);
		fuellingEntry = (FuellingEntry) MainActivity.garage.getActiveCar().getHistory().getEntries().get(position);
		super.entry = fuellingEntry;
		super.editMode = true;
		super.onCreate(savedInstanceState);
		initializeGuiObjects();
	}
	
	private void initializeGuiObjects() {
		editTextMileage.setText(Double.valueOf(fuellingEntry.getMileage()).toString());
		editTextCost.setText(Double.valueOf(fuellingEntry.getCost()).toString());
		editTextComment.setText(fuellingEntry.getComment());
		editTextVolume.setText(Double.valueOf(fuellingEntry.getFuelVolume()).toString());
		textViewDisplayDate.setText(DateFormat.getDateInstance().format(fuellingEntry.getEventDate()));

		//PriceCalculateListener priceCalculator = new PriceCalculateListener();
		//editTextCost.addTextChangedListener(priceCalculator);
		//editTextVolume.addTextChangedListener(priceCalculator);

		spinnerFuelType.setSelection(fuellingEntry.getFuelType().getId());
		refreshPrice();
	}
}
