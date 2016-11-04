package sk.berops.android.fueller.gui.garage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.UnitConstants.DistanceUnit;
import sk.berops.android.fueller.dataModel.UnitConstants.VolumeUnit;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityRecordAdd;

public class ActivityCarAdd extends ActivityRecordAdd {
	
	protected Car car;
	protected boolean editMode;
	private View addCarView;
	
	protected Button buttonCommit;
	protected EditText editTextBrand;
	protected EditText editTextModel;
	protected EditText editTextLicensePlate;
	protected EditText editTextMileage;
	protected EditText editTextModelYear;
	protected EditText editTextNickname;
	protected Spinner spinnerDistanceUnit;
	protected Spinner spinnerVolumeUnit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (car == null) {
			car = new Car();
		}
		super.record = (Record) this.car;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_car_add);
	}

	@Override
	protected void attachGuiObjects() {
		buttonCommit = (Button)findViewById(R.id.activity_car_add_button_commit);

		editTextBrand = (EditText)findViewById(R.id.activity_car_add_brand);
		editTextModel = (EditText)findViewById(R.id.activity_car_add_model);
		editTextLicensePlate = (EditText)findViewById(R.id.activity_car_add_license_plate);
		editTextMileage = (EditText)findViewById(R.id.activity_car_add_mileage);
		editTextModelYear = (EditText)findViewById(R.id.activity_car_add_model_year);
		editTextNickname = (EditText)findViewById(R.id.activity_car_add_nickname);
		
		spinnerDistanceUnit = (Spinner) findViewById(R.id.activity_car_add_distance_unit);
		spinnerVolumeUnit = (Spinner) findViewById(R.id.activity_car_add_volume_unit);

		listButtons.add(buttonCommit);

		listEditTexts.add(editTextBrand);
		listEditTexts.add(editTextModel);
		listEditTexts.add(editTextLicensePlate);
		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextModelYear);
		listEditTexts.add(editTextNickname);

		mapSpinners.put(R.array.activity_car_add_distance_units, spinnerDistanceUnit);
		mapSpinners.put(R.array.activity_car_add_volume_units, spinnerVolumeUnit);
	}

	public void saveEntry(View view) {
		car.setBrand(editTextBrand.getText().toString());
		car.setModel(editTextModel.getText().toString());
		car.setLicensePlate(editTextLicensePlate.getText().toString());
		car.setNickname(editTextNickname.getText().toString());
		try {
			Integer mileage = Integer.parseInt(editTextMileage.getText().toString());
			car.setCurrentMileage(mileage);
			car.setInitialMileage(mileage);
		} catch (NumberFormatException ex) {
			System.out.println("Mileage not defined.");
			ex.printStackTrace();
		}
		try {
			car.setModelYear(Integer.parseInt(editTextModelYear.getText().toString()));
		} catch (NumberFormatException ex) {
			System.out.println("ModelYear not defined.");
			ex.printStackTrace();
		}
		car.setDistanceUnit(DistanceUnit.getDistanceUnit(spinnerDistanceUnit.getSelectedItemPosition()));
		car.setVolumeUnit(VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition()));
		
		if (!editMode) {
			MainActivity.garage.addCar(car);
		}
		try {
			super.saveFieldsAndPersist(view);
		} catch (FieldsEmptyException e) {
			e.throwAlert();
		}
	}
	
	public void onClick(View view) {
		saveEntry(view);
		switch(view.getId()) {
		case R.id.activity_car_add_button_commit:
			startActivity(new Intent(this, MainActivity.class));
			break;
		}
	}
}
