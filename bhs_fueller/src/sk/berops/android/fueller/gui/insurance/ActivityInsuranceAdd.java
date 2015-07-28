package sk.berops.android.fueller.gui.insurance;

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
import sk.berops.android.fueller.dataModel.expense.InsuranceEntry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;

public class ActivityInsuranceAdd extends ActivityAddEventGeneric {
	protected Spinner spinnerInsuranceType;
	
	protected InsuranceEntry insuranceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance);

		if (insuranceEntry == null) {
			insuranceEntry = new InsuranceEntry();
			insuranceEntry.setExpenseType(ExpenseType.INSURANCE);
		}

		super.entry = (Entry) this.insuranceEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDisplayDate = (TextView) findViewById(R.id.activity_insurance_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_insurance_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_insurance_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_insurance_cost);
		editTextComment = (EditText) findViewById(R.id.activity_insurance_comment);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_insurance_currency);
		spinnerInsuranceType = (Spinner) findViewById(R.id.activity_insurance_type);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		
		ArrayAdapter<CharSequence> adapterInsuranceType = ArrayAdapter.createFromResource(this,
				R.array.activity_insurance_type, R.layout.spinner_white);
		adapterInsuranceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerInsuranceType.setAdapter(adapterInsuranceType);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_insurance_button_commit:
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
		InsuranceEntry.Type type;
		
		type = InsuranceEntry.Type.getType(spinnerInsuranceType.getSelectedItemPosition());
		insuranceEntry.setType(type);
	}
}