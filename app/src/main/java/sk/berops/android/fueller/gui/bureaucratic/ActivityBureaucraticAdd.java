package sk.berops.android.fueller.gui.bureaucratic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.BureaucraticEntry;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;

public class ActivityBureaucraticAdd extends ActivityEntryGenericAdd {
	
	protected BureaucraticEntry bureaucraticEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (bureaucraticEntry == null) {
			bureaucraticEntry = new BureaucraticEntry();
			bureaucraticEntry.setExpenseType(ExpenseType.BUREAUCRATIC);
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
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_bureaucratic_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_bureaucratic_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_bureaucratic_cost);
		editTextComment = (EditText) findViewById(R.id.activity_bureaucratic_comment);

		buttonDate = (Button) findViewById(R.id.activity_bureaucratic_date_button);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_bureaucratic_currency);

		listButtons.add(buttonDate);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		initializeTags(R.id.activity_bureaucratic_tags_recyclerview);
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		// To be updated once more fields are added to BureaucraticEntry
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_bureaucratic_button_commit:
			try {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldsEmptyException ex) {
				ex.throwAlert();
			}
			break;
		}
	}
}