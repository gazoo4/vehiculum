package sk.berops.android.fueller.gui.toll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.TollEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;

public class ActivityTollAdd extends ActivityEntryGenericAdd {
	protected Spinner spinnerTollType;
	
	protected TollEntry tollEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (tollEntry == null) {
			tollEntry = new TollEntry();
			tollEntry.setExpenseType(ExpenseType.TOLL);
		}

		super.entry = (Entry) this.tollEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_toll);
	}
	
	@Override
	protected void attachGuiObjects() {
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_toll_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_toll_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_toll_cost);
		editTextComment = (EditText) findViewById(R.id.activity_toll_comment);

		buttonDate = (Button) findViewById(R.id.activity_toll_date_button);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_toll_currency);
		spinnerTollType = (Spinner) findViewById(R.id.activity_toll_type);

		listButtons.add(buttonDate);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
		mapSpinners.put(R.array.activity_toll_type, spinnerTollType);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		initializeTags(R.id.activity_toll_tags_recyclerview);
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateType();
	}
	
	private void updateType() {
		TollEntry.Type type;
		
		type = TollEntry.Type.getType(spinnerTollType.getSelectedItemPosition());
		tollEntry.setType(type);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_toll_button_commit:
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