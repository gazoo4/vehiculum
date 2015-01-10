package sk.berops.android.fueller.gui.fuelling;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.Fonts;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;
import sk.berops.android.fueller.gui.common.FragmentDatePicker;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.R.id;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityRefuel extends ActivityAddEventGeneric {

	class PriceCalculateListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			refreshPrice();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	private TextView textViewPrice;
	protected EditText editTextVolume;
	protected Spinner spinnerFuelType;

	protected FuellingEntry fuellingEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_refuel);
		if (fuellingEntry == null) {
			fuellingEntry = new FuellingEntry();
		}
		fuellingEntry.setExpenseType(Entry.ExpenseType.FUEL);
		super.entry = (Entry) this.fuellingEntry;
		super.editMode = editMode;
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		editTextMileage = (EditText) findViewById(R.id.activity_refuel_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_refuel_cost);
		editTextComment = (EditText) findViewById(R.id.activity_refuel_comment);
		textViewPrice = (TextView) findViewById(R.id.activity_refuel_price_text);
		editTextVolume = (EditText) findViewById(R.id.activity_refuel_volume);
		textViewDisplayDate = (TextView) findViewById(R.id.activity_refuel_date_text);

		PriceCalculateListener priceCalculator = new PriceCalculateListener();
		editTextCost.addTextChangedListener(priceCalculator);
		editTextVolume.addTextChangedListener(priceCalculator);

		spinnerFuelType = (Spinner) findViewById(R.id.activity_refuel_fuel_type);
		ArrayAdapter<CharSequence> adapterFuelType = ArrayAdapter
				.createFromResource(this, R.array.activity_refuel_fuel_type,
						android.R.layout.simple_spinner_item);
		adapterFuelType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFuelType.setAdapter(adapterFuelType);
	}
	
	@Override
	protected void styleGuiObjects() {
		editTextMileage.setHintTextColor(Colors.LIGHT_GREEN);
		editTextCost.setHintTextColor(Colors.LIGHT_GREEN);
		editTextVolume.setHintTextColor(Colors.LIGHT_GREEN);
		editTextComment.setHintTextColor(Colors.LIGHT_GREEN);
		
		textViewDisplayDate.setTypeface(Fonts.getFontBook(this));
		textViewPrice.setTypeface(Fonts.getFontBook(this));
		editTextMileage.setTypeface(Fonts.getFontPort(this));
		editTextCost.setTypeface(Fonts.getFontPort(this));
		editTextVolume.setTypeface(Fonts.getFontPort(this));
		editTextComment.setTypeface(Fonts.getFontPort(this));
	}
	
	protected void refreshPrice() {
		double volume;
		double cost;
		double price;

		try {
			volume = GuiUtils.extractDouble(editTextVolume);
			cost = GuiUtils.extractDouble(editTextCost);
			price = cost / volume;

			String formattedPrice;
			DecimalFormat df = new DecimalFormat("##.###");
			formattedPrice = df.format(price);

			textViewPrice.setText(formattedPrice);
		} catch (NumberFormatException e) {
			// Both the fields need to be filled-in (volume, cost)
		}
	}

	private void updateFuelVolume() {
		double volume = 0;
		try {
			volume = GuiUtils.extractDouble(editTextVolume);
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(
					R.string.activity_refuel_volume_hint));
		}
		switch (MainActivity.garage.getActiveCar().getVolumeUnit()) {
			case LITER: fuellingEntry.setFuelVolume(volume); break;
			case IMPERIAL_GALLON: fuellingEntry.setFuelVolume(volume*4.54609); break;
			case US_GALLON: fuellingEntry.setFuelVolume(volume*3.78541); break;
			default: System.out.println("Very weird volume unit");
		}
	}

	private void updateFuelType() {
		fuellingEntry.setFuelType(FuellingEntry.FuelType
				.getFuelType(spinnerFuelType.getSelectedItemPosition()));
	}

	private void updateFuelPrice() {
		double volume;
		double cost;
		double price;

		try {
			volume = GuiUtils.extractDouble(editTextVolume);
			cost = GuiUtils.extractDouble(editTextCost);
			price = cost / volume;
			fuellingEntry.setFuelPrice(price);
		} catch (NumberFormatException ex) {
			System.out.println("Cannot compute the price when not both Cost + Volume are filled-in");
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_refuel_button_commit:
			entryOK = true;
			saveEntry(view);
			if (entryOK) {
			super.saveFieldsAndPersist(view);
			startActivity(new Intent(this, MainActivity.class));
			}
			break;
		}
	}
	
	public void saveEntry(View view) {
		updateFuelVolume();
		updateFuelType();
		updateFuelPrice();
	}
}
