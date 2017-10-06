package sk.berops.android.vehiculum.gui.toll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.TollEntry;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;

public class ActivityTollAdd extends ActivityEntryGenericAdd {
	protected Spinner spinnerTollType;
	
	protected TollEntry tollEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (tollEntry == null) {
			tollEntry = new TollEntry();
		}

		entry = tollEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_toll);
	}
	
	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_toll_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_toll_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_toll_cost);
		editTextComment = (EditText) findViewById(R.id.activity_toll_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_toll_currency);
		spinnerTollType = (Spinner) findViewById(R.id.activity_toll_type);

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(Currency.Unit.extractCodesAndSymbols(), spinnerCurrency);
		mapSpinners.put(R.array.activity_toll_type, spinnerTollType);
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateType();
	}
	
	private void updateType() {
		TollEntry.Type type;
		
		type = TollEntry.Type.getType(spinnerTollType.getSelectedItemPosition());
		tollEntry.setType(type);
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