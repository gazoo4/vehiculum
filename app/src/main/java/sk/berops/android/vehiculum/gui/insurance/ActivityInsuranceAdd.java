package sk.berops.android.vehiculum.gui.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;

public class ActivityInsuranceAdd extends ActivityEntryGenericAdd {
	protected Spinner spinnerInsuranceType;
	
	protected InsuranceEntry insuranceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (insuranceEntry == null) {
			insuranceEntry = new InsuranceEntry();
		}

		super.entry = (Entry) this.insuranceEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_insurance);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_insurance_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_insurance_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_insurance_cost);
		editTextComment = (EditText) findViewById(R.id.activity_insurance_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_insurance_currency);
		spinnerInsuranceType = (Spinner) findViewById(R.id.activity_insurance_type);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_insurance_type, spinnerInsuranceType);
		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateType();
	}
	
	private void updateType() {
		InsuranceEntry.Type type;
		
		type = InsuranceEntry.Type.getType(spinnerInsuranceType.getSelectedItemPosition());
		insuranceEntry.setType(type);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_insurance_button_commit:
			try {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldEmptyException ex) {
				ex.throwAlert();
			}
			break;
		}
	}
}