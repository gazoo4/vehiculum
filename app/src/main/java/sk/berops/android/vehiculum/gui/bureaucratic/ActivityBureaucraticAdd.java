package sk.berops.android.vehiculum.gui.bureaucratic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.expense.BureaucraticEntry;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;

public class ActivityBureaucraticAdd extends ActivityEntryGenericAdd {
	
	protected BureaucraticEntry bureaucraticEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (bureaucraticEntry == null) {
			bureaucraticEntry = new BureaucraticEntry();
		}

		super.entry = (Entry) this.bureaucraticEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_bureaucratic);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_bureaucratic_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_bureaucratic_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_bureaucratic_cost);
		editTextComment = (EditText) findViewById(R.id.activity_bureaucratic_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_bureaucratic_currency);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		// To be updated once more fields are added to BureaucraticEntry
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_bureaucratic_button_commit:
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