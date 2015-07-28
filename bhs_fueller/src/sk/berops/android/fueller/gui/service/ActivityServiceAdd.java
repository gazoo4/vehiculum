package sk.berops.android.fueller.gui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.ServiceEntry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;

public class ActivityServiceAdd extends ActivityAddEventGeneric {
	protected Spinner spinnerServiceType;
	
	protected ServiceEntry serviceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_service);

		if (serviceEntry == null) {
			serviceEntry = new ServiceEntry();
			serviceEntry.setExpenseType(ExpenseType.SERVICE);
		}

		super.entry = (Entry) this.serviceEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDisplayDate = (TextView) findViewById(R.id.activity_service_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_service_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_service_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_service_cost);
		editTextComment = (EditText) findViewById(R.id.activity_service_comment);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_service_currency);
		spinnerServiceType = (Spinner) findViewById(R.id.activity_service_type);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		
		ArrayAdapter<CharSequence> adapterServiceType = ArrayAdapter.createFromResource(this,
				R.array.activity_service_type, R.layout.spinner_white);
		adapterServiceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerServiceType.setAdapter(adapterServiceType);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_service_button_commit:
			try {
				saveEntry();
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldsEmptyException ex) {
				ex.throwAlert();
			}
			break;
		}
	}
	
	private void saveEntry() {
		updateType();
	}
	
	private void updateType() {
		ServiceEntry.Type type;
		
		type = ServiceEntry.Type.getType(spinnerServiceType.getSelectedItemPosition());
		serviceEntry.setType(type);
	}
}