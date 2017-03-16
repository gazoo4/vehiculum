package sk.berops.android.vehiculum.gui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;

public class ActivityServiceAdd extends ActivityEntryGenericAdd {
	protected Spinner spinnerServiceType;
	
	protected ServiceEntry serviceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (serviceEntry == null) {
			serviceEntry = new ServiceEntry();
		}

		entry = serviceEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_service);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_service_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_service_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_service_cost);
		editTextComment = (EditText) findViewById(R.id.activity_service_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_service_currency);
		spinnerServiceType = (Spinner) findViewById(R.id.activity_service_type);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_service_type, spinnerServiceType);
		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateType();
	}
	
	private void updateType() {
		ServiceEntry.Type type;
		
		type = ServiceEntry.Type.getType(spinnerServiceType.getSelectedItemPosition());
		serviceEntry.setType(type);
	}

	@Override
	public boolean onClick(View view) {
		if (super.onClick(view)) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}

		return false;
	}
}