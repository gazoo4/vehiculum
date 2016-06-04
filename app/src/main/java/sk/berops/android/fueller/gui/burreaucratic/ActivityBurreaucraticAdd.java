package sk.berops.android.fueller.gui.burreaucratic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.BurreaucraticEntry;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;

public class ActivityBurreaucraticAdd extends ActivityEntryGenericAdd {
	
	protected BurreaucraticEntry burreaucraticEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_burreaucratic);

		if (burreaucraticEntry == null) {
			burreaucraticEntry = new BurreaucraticEntry();
			burreaucraticEntry.setExpenseType(ExpenseType.BURREAUCRATIC);
		}

		super.entry = (Entry) this.burreaucraticEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void attachGuiObjects() {
		textViewDisplayDate = (TextView) findViewById(R.id.activity_burreaucratic_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_burreaucratic_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_burreaucratic_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_burreaucratic_cost);
		editTextComment = (EditText) findViewById(R.id.activity_burreaucratic_comment);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_burreaucratic_currency);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		// To be updated once more fields are added to BurreaucraticEntry
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_burreaucratic_button_commit:
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