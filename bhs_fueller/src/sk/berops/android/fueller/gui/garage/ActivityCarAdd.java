package sk.berops.android.fueller.gui.garage;

import org.w3c.dom.NamedNodeMap;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddRecord;
import sk.berops.android.fueller.gui.common.pictures.CameraHandler;
import sk.berops.android.fueller.gui.fuelling.ActivityRefuel;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class ActivityCarAdd extends ActivityAddRecord {
	
	protected Car car;
	protected boolean editMode;
	private View addCarView;
	
	protected Button buttonCommit;
	protected Button buttonAddPhoto;
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
		setContentView(R.layout.activity_car_add);
		if (car == null) {
			car = new Car();
		}
		super.record = (Record) this.car;
		super.onCreate(savedInstanceState);
		attachGuiObjects();
	}

	@Override
	protected void attachGuiObjects() {
		buttonCommit = (Button)findViewById(R.id.activity_car_add_button_commit);
		buttonAddPhoto = (Button)findViewById(R.id.activity_car_add_button_get_photo);
		if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			System.out.println("TRUE TRUE");
			buttonAddPhoto.setVisibility(View.VISIBLE);
		} else {
			System.out.println("FALSE FALSE");
			buttonAddPhoto.setVisibility(View.GONE);
		}
		editTextBrand = (EditText)findViewById(R.id.activity_car_add_brand);
		editTextModel = (EditText)findViewById(R.id.activity_car_add_model);
		editTextLicensePlate = (EditText)findViewById(R.id.activity_car_add_license_plate);
		editTextMileage = (EditText)findViewById(R.id.activity_car_add_mileage);
		editTextModelYear = (EditText)findViewById(R.id.activity_car_add_model_year);
		editTextNickname = (EditText)findViewById(R.id.activity_car_add_nickname);
		
		spinnerDistanceUnit = (Spinner) findViewById(R.id.activity_car_add_distance_unit);
		ArrayAdapter<CharSequence> adapterDistanceUnit = ArrayAdapter
				.createFromResource(this, R.array.activity_car_add_distance_units,
						android.R.layout.simple_spinner_item);
		adapterDistanceUnit
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDistanceUnit.setAdapter(adapterDistanceUnit);
		
		spinnerVolumeUnit = (Spinner) findViewById(R.id.activity_car_add_volume_unit);
		ArrayAdapter<CharSequence> adapterVolumeUnit = ArrayAdapter
				.createFromResource(this, R.array.activity_car_add_volume_units,
						android.R.layout.simple_spinner_item);
		adapterDistanceUnit
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVolumeUnit.setAdapter(adapterVolumeUnit);
	}
	
	@Override
	protected void styleGuiObjects() {
		editTextBrand.setHintTextColor(Colors.LIGHT_GREEN);
		editTextModel.setHintTextColor(Colors.LIGHT_GREEN);
		editTextLicensePlate.setHintTextColor(Colors.LIGHT_GREEN);
		editTextMileage.setHintTextColor(Colors.LIGHT_GREEN);
		editTextModelYear.setHintTextColor(Colors.LIGHT_GREEN);
		editTextNickname.setHintTextColor(Colors.LIGHT_GREEN);
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
		car.setDistanceUnit(Car.DistanceUnit.getDistanceUnit(spinnerDistanceUnit.getSelectedItemPosition()));
		car.setVolumeUnit(Car.VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition()));
		
		if (!editMode) {
			MainActivity.garage.addCar(car);
		}
		super.saveFieldsAndPersist(view);
	}
	
	public void onClick(View view) {
		saveEntry(view);
		switch(view.getId()) {
		case R.id.activity_car_add_button_commit:
			startActivity(new Intent(this, MainActivity.class));
			break;
		case R.id.activity_car_add_button_get_photo:
			startActivity(new Intent(this, CameraHandler.class));
			break;
		}
	}
}
