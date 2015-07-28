package sk.berops.android.fueller.gui.toll;

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
import sk.berops.android.fueller.dataModel.expense.TollEntry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;

public class ActivityTollAdd extends ActivityAddEventGeneric {
	protected Spinner spinnerTollType;
	
	protected TollEntry tollEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_toll);

		if (tollEntry == null) {
			tollEntry = new TollEntry();
			tollEntry.setExpenseType(ExpenseType.TOLL);
		}

		super.entry = (Entry) this.tollEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDisplayDate = (TextView) findViewById(R.id.activity_toll_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_toll_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_toll_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_toll_cost);
		editTextComment = (EditText) findViewById(R.id.activity_toll_comment);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_toll_currency);
		spinnerTollType = (Spinner) findViewById(R.id.activity_toll_type);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		
		ArrayAdapter<CharSequence> adapterTollType = ArrayAdapter.createFromResource(this,
				R.array.activity_toll_type, R.layout.spinner_white);
		adapterTollType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTollType.setAdapter(adapterTollType);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_toll_button_commit:
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
		TollEntry.Type type;
		
		type = TollEntry.Type.getType(spinnerTollType.getSelectedItemPosition());
		tollEntry.setType(type);
	}
}