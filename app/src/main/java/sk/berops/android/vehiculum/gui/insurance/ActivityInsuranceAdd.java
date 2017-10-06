package sk.berops.android.vehiculum.gui.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Currency;
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

		super.entry = this.insuranceEntry;
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
		mapSpinners.put(Currency.Unit.extractCodesAndSymbols(), spinnerCurrency);
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

	@Override
	public boolean onClick(View view) {
		if (super.onClick(view)) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}

		return false;
	}
}