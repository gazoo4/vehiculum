package sk.berops.android.fueller.gui.garage;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.R;
import android.content.Intent;
import android.os.Bundle;

public class ActivityCarEdit extends ActivityCarAdd {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", -1);
		car = MainActivity.garage.getCars().get(position);
		super.car = car;
		super.onCreate(savedInstanceState);
		editMode = true;
		initializeGuiObjects();
	}

	private void initializeGuiObjects() {
		buttonCommit.setText(R.string.activity_car_add_button_edit);
		editTextBrand.setText(car.getBrand());
		editTextModel.setText(car.getModel());
		editTextLicensePlate.setText(car.getLicensePlate());
		editTextMileage.setText(Double.valueOf(car.getCurrentMileage()).toString());
		editTextModelYear.setText(Integer.valueOf(car.getModelYear()).toString());
		editTextNickname.setText(car.getNickname());
		spinnerDistanceUnit.setSelection(car.getDistanceUnit().getId());
		spinnerVolumeUnit.setSelection(car.getVolumeUnit().getId());
	}
}
