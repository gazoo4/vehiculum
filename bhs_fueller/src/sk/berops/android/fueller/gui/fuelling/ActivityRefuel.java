package sk.berops.android.fueller.gui.fuelling;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.UnitConstants;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityRefuel extends ActivityAddEventGeneric {

	class PriceCalculateListener implements TextWatcher, OnItemSelectedListener{

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

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			refreshPrice();		
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}

	private TextView textViewPrice;
	private TextView textViewDistanceUnit;
	protected EditText editTextVolume;
	protected Spinner spinnerFuelType;
	protected Spinner spinnerVolumeUnit;

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
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_refuel_distance_unit);
		editTextCost = (EditText) findViewById(R.id.activity_refuel_cost);
		editTextComment = (EditText) findViewById(R.id.activity_refuel_comment);
		textViewPrice = (TextView) findViewById(R.id.activity_refuel_price_text);
		editTextVolume = (EditText) findViewById(R.id.activity_refuel_volume);
		textViewDisplayDate = (TextView) findViewById(R.id.activity_refuel_date_text);

		spinnerFuelType = (Spinner) findViewById(R.id.activity_refuel_fuel_type);
		ArrayAdapter<CharSequence> adapterFuelType = ArrayAdapter
				.createFromResource(this, R.array.activity_refuel_fuel_type,
						R.layout.spinner_white);
		adapterFuelType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFuelType.setAdapter(adapterFuelType);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_refuel_currency);
		ArrayAdapter<CharSequence> adapterCurrency = ArrayAdapter
				.createFromResource(this, R.array.activity_refuel_currency, 
						R.layout.spinner_white);
		adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCurrency.setAdapter(adapterCurrency);
		
		spinnerVolumeUnit = (Spinner) findViewById(R.id.activity_refuel_volume_unit);
		ArrayAdapter<CharSequence> adapterVolumeUnit = ArrayAdapter
				.createFromResource(this, R.array.activity_refuel_volume_unit, 
						R.layout.spinner_white);
		adapterVolumeUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVolumeUnit.setAdapter(adapterVolumeUnit);
		
		PriceCalculateListener priceCalculator = new PriceCalculateListener();
		editTextCost.addTextChangedListener(priceCalculator);
		editTextVolume.addTextChangedListener(priceCalculator);
		spinnerCurrency.setOnItemSelectedListener(priceCalculator);
		spinnerVolumeUnit.setOnItemSelectedListener(priceCalculator);
	}
	
	@Override
	protected void styleGuiObjects() {
		editTextMileage.setHintTextColor(Colors.LIGHT_GREEN);
		editTextCost.setHintTextColor(Colors.LIGHT_GREEN);
		editTextVolume.setHintTextColor(Colors.LIGHT_GREEN);
		editTextComment.setHintTextColor(Colors.LIGHT_GREEN);
		
		//textViewDisplayDate.setTypeface(Fonts.getFontBook(this));
		//textViewPrice.setTypeface(Fonts.getFontBook(this));
		//editTextMileage.setTypeface(Fonts.getFontPort(this));
		//editTextCost.setTypeface(Fonts.getFontPort(this));
		//editTextVolume.setTypeface(Fonts.getFontPort(this));
		//editTextComment.setTypeface(Fonts.getFontPort(this));
	}
	
	@Override
	protected void initializeGuiObjects() {
		Car car = MainActivity.garage.getActiveCar();
		Currency.Unit currency;
		try {
			currency = car.getHistory().getEntries().getLast().getCurrency();
		} catch (NoSuchElementException e) {
			currency = Preferences.getInstance().getCurrency();
		}
		spinnerCurrency.setSelection(currency.getId());
		
		FuelType fuelType;
		try {
			fuelType = car.getHistory().getFuellingEntries().getLast().getFuelType();
		} catch (NoSuchElementException e) {
			fuelType = FuelType.getFuelType(0);
		}
		spinnerFuelType.setSelection(fuelType.getId());
		
		UnitConstants.VolumeUnit volumeUnit;
		volumeUnit = car.getVolumeUnit();
		spinnerVolumeUnit.setSelection(volumeUnit.getId());
		
		textViewDistanceUnit.setText(car.getDistanceUnit().getUnit());
	}
	
	protected void refreshPrice() {
		double volume;
		double cost;
		double price;
		Currency.Unit currency;
		UnitConstants.VolumeUnit volumeUnit = UnitConstants.VolumeUnit.getVolumeUnit(0);

		try {
			//TODO: here we should reflect the units we've bought in here
			volume = GuiUtils.extractDouble(editTextVolume);
			cost = GuiUtils.extractDouble(editTextCost);
			price = cost / volume;

			String formattedPrice;
			String unit;
			DecimalFormat df = new DecimalFormat("##.###");
			formattedPrice = df.format(price);

			currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
			volumeUnit = UnitConstants.VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition());
			unit = ""+ currency.getUnit() +"/"+ volumeUnit.getUnit(); 
			textViewPrice.setText(formattedPrice +" "+ unit);
		} catch (NumberFormatException e) {
			// Both the fields need to be filled-in (volume, cost)
		}
	}

	private void updateFuelVolume() {
		double volume = 0;
		UnitConstants.VolumeUnit volumeUnit;
		volumeUnit = UnitConstants.VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition());
		try {
			volume = GuiUtils.extractDouble(editTextVolume);
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(
					R.string.activity_refuel_volume_hint));
		}
		fuellingEntry.setFuelVolume(volume, volumeUnit);
	}

	private void updateFuelType() {
		fuellingEntry.setFuelType(FuellingEntry.FuelType
				.getFuelType(spinnerFuelType.getSelectedItemPosition()));
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_refuel_button_commit:
			entryOK = true;
			saveEntry();
			if (entryOK) {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			}
			break;
		}
	}
	
	public void saveEntry() {
		updateFuelVolume();
		updateFuelType();
	}
}
