package sk.berops.android.fueller.gui.tyres;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldEmptyException;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;

public class ActivityTyreChange extends ActivityEntryGenericAdd {
	
	class PriceCalculateListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			updateTotalCost();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	Double laborCost, extraMaterialCost, tyresCost;
	
	private Car car;
	protected TyreChangeEntry tyreChangeEntry;
	protected EditText editTextTyresCost;
	protected EditText editTextLaborCost;
	protected EditText editTextSmallPartsCost;
	
	protected static final int SCHEME = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		car = MainActivity.garage.getActiveCar();
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry();
		}
		tyreChangeEntry.setExpenseType(ExpenseType.TYRES);
		tyreChangeEntry.setCar(car);
		
		super.entry = this.tyreChangeEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_tyre_change);
	}

	@Override
	protected void attachGuiObjects() {
		editTextTyresCost = (EditText) findViewById(R.id.activity_tyre_change_tyres_cost);
		editTextLaborCost = (EditText) findViewById(R.id.activity_tyre_change_labor_cost);
		editTextSmallPartsCost = (EditText) findViewById(R.id.activity_tyre_change_small_parts_cost);
		editTextCost = (EditText) findViewById(R.id.activity_tyre_change_total_cost);
		editTextMileage = (EditText) findViewById(R.id.activity_tyre_change_mileage);
		editTextComment = (EditText) findViewById(R.id.activity_tyre_change_comment);

		textViewDistanceUnit = (TextView) findViewById(R.id.activity_tyre_change_distance_unit);

		buttonDate = (Button) findViewById(R.id.activity_tyre_change_date_button);
		buttonTagAdd = (Button) findViewById(R.id.activity_tyre_change_button_tag_add);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_tyre_change_total_cost_currency);

		listButtons.add(buttonDate);
		listButtons.add(buttonTagAdd);

		listEditTexts.add(editTextTyresCost);
		listEditTexts.add(editTextLaborCost);
		listEditTexts.add(editTextSmallPartsCost);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		initializeTags(R.id.activity_tyre_change_tags_recyclerview);
		PriceCalculateListener priceCalculator = new PriceCalculateListener();
		editTextTyresCost.addTextChangedListener(priceCalculator);
		editTextLaborCost.addTextChangedListener(priceCalculator);
		editTextSmallPartsCost.addTextChangedListener(priceCalculator);
	}

	protected void updateTyresCost() {
		double cost = 0.0;
		if (editTextTyresCost.getText().toString().equals("")
				|| GuiUtils.extractDouble(editTextTyresCost) == 0.0) {
			for (Tyre t : tyreChangeEntry.getBoughtTyres()) {
				cost += t.getCost();
			}
			tyreChangeEntry.setTyresCost(cost);
			editTextTyresCost.setText(((Double) tyreChangeEntry.getTyresCost()).toString());
		}
		updateTotalCost();
	}

	private double getTotalCosts() {
		double cost = 0.0;
		try {
			laborCost = GuiUtils.extractDouble(editTextLaborCost);
			tyreChangeEntry.setLaborCost(laborCost);
			cost += laborCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_labor_cost_hint));
		}
		try {
			extraMaterialCost = GuiUtils.extractDouble(editTextSmallPartsCost);
			tyreChangeEntry.setExtraMaterialCost(extraMaterialCost);
			cost += extraMaterialCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}
		try {
			tyresCost = GuiUtils.extractDouble(editTextTyresCost);
			tyreChangeEntry.setTyresCost(tyresCost);
			cost += tyresCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}

		return cost;
	}

	protected void updateTotalCost() {
		editTextCost.setText(Double.toString(getTotalCosts()));
	}

	private void updateExtraMaterialsCost() throws FieldEmptyException {
		double cost = 0.0;
		try {
			 cost = Double.parseDouble(editTextSmallPartsCost.getText().toString());
		} catch (NumberFormatException ex) {
			// This is not a mandatory field
		}

		tyreChangeEntry.setExtraMaterialCost(cost);
	}

	private void updateLaborCost() throws FieldEmptyException {
		double cost = 0.0;
		try {
			cost = Double.parseDouble(editTextLaborCost.getText().toString());
		} catch (NumberFormatException ex) {
			// This is not a mandatory field
		}

		tyreChangeEntry.setLaborCost(cost);
	}

	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateTyresCost();
		updateExtraMaterialsCost();
		updateLaborCost();
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_tyre_change_button_advanced:
			Intent i = new Intent(this, ActivityTyreChangeScheme.class);
			if (tyreChangeEntry != null) {
				// Ugly hack:
				// We need to remove Consumption object as it contains elements from external libraries which can't be serialized
				tyreChangeEntry.setConsumption(null);
				for (Entry e: tyreChangeEntry.getCar().getHistory().getEntries()) {
					if (e.getConsumption() != null) {
						e.getConsumption().setPieChartVals(null);
					}
				}
				i.putExtra(ActivityTyreChangeScheme.INTENT_TYRE_ENTRY, tyreChangeEntry);
			}
			startActivityForResult(i, SCHEME);
			break;
		case R.id.activity_tyre_change_button_commit:
			try {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}

	/**
	 * Callback method from follow-up activities
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCHEME:
			// We're coming back from ActivityTyreChangeScheme
			if (resultCode == RESULT_OK) {
				tyreChangeEntry = (TyreChangeEntry) data.getExtras().getSerializable(ActivityTyreChangeScheme.INTENT_TYRE_ENTRY);
				updateTyresCost();
			} else if (resultCode == RESULT_CANCELED) {
				// nothing needed to be done if the scheme addition was cancelled
			}
			break;
		}
	}
}
