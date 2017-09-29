package sk.berops.android.vehiculum.gui.tyres;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.TyreChangeEntry;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.vehiculum.gui.common.GuiUtils;

public class ActivityTyreChange extends ActivityEntryGenericAdd {

	Cost laborCost;
	Cost extraMaterialCost;
	Cost tyresCost;
	
	private Car car;
	protected TyreChangeEntry tyreChangeEntry;
	protected EditText editTextTyresCost;
	protected EditText editTextLaborCost;
	protected EditText editTextSmallPartsCost;
	
	protected static final int REQUEST_CODE_SCHEME = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		car = MainActivity.garage.getActiveCar();
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry();
		}
		tyreChangeEntry.setCar(car);
		
		entry = tyreChangeEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_tyre_change);
	}

	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		editTextTyresCost = (EditText) findViewById(R.id.activity_tyre_change_tyres_cost);
		editTextLaborCost = (EditText) findViewById(R.id.activity_tyre_change_labor_cost);
		editTextSmallPartsCost = (EditText) findViewById(R.id.activity_tyre_change_small_parts_cost);
		editTextCost = (EditText) findViewById(R.id.activity_tyre_change_total_cost);
		editTextMileage = (EditText) findViewById(R.id.activity_tyre_change_mileage);
		editTextComment = (EditText) findViewById(R.id.activity_tyre_change_comment);

		textViewDistanceUnit = (TextView) findViewById(R.id.activity_tyre_change_distance_unit);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_tyre_change_total_cost_currency);

		listEditTexts.add(editTextTyresCost);
		listEditTexts.add(editTextLaborCost);
		listEditTexts.add(editTextSmallPartsCost);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
	}

	@Override
	public void afterTextChanged(Editable s) {
		super.afterTextChanged(s);
		if (s == editTextTyresCost.getText()
				|| s == editTextLaborCost.getText()
				|| s == editTextSmallPartsCost.getText()) {
			updateTotalCost();
		}
	}

	/**
	 * Method to update the cost of the tyres. If the editTextTyresCost field is empty this method
	 * will read the boughtTyres list and calculate and populate the tyreEntry.tyresCost field
	 * accordingly.
	 * @param forceUpdate Forces the update of the cost of the tyres by reading the cost of the bought
	 *                    tyres.
	 */
	protected void updateTyresCost(boolean forceUpdate) {
		Cost cost = new Cost();
		if (forceUpdate
				|| editTextTyresCost.getText().toString().equals("")
				|| GuiUtils.extractDouble(editTextTyresCost) == 0.0) {
			for (Tyre t : tyreChangeEntry.getBoughtTyres()) {
				cost = Cost.add(cost, t.getCost());
			}
			tyreChangeEntry.setTyresCost(cost);
			editTextTyresCost.setText(cost.getPreferredValue().toString() + " " + cost.getPreferredUnit().getSymbol());
		}

		updateTotalCost();
	}

	/**
	 * Method to calculate the total cost of the entry (laborCost + extraMaterials + tyresCost).
	 * @return Total cost
	 */
	private Cost getTotalCosts() {
		Cost cost = new Cost();
		Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());

		try {
			double value = GuiUtils.extractDouble(editTextLaborCost);
			laborCost = new Cost(value, currency);
			tyreChangeEntry.setLaborCost(laborCost);
			cost = Cost.add(cost, laborCost);
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_labor_cost_hint));
		}

		try {
			double value = GuiUtils.extractDouble(editTextSmallPartsCost);
			extraMaterialCost = new Cost(value, currency);
			tyreChangeEntry.setExtraMaterialCost(extraMaterialCost);
			cost = Cost.add(cost, extraMaterialCost);
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}

		try {
			double value = GuiUtils.extractDouble(editTextTyresCost);
			tyresCost = new Cost(value, currency);
			tyreChangeEntry.setTyresCost(tyresCost);
			cost = Cost.add(cost, tyresCost);
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_tyres_cost_hint));
		}

		return cost;
	}

	/**
	 * Updates the editTextCost field by the overall cost calculated from the entry.
	 */
	protected void updateTotalCost() {
		Cost totalCost = getTotalCosts();
		editTextCost.setText(totalCost.getPreferredValue().toString());
		spinnerCurrency.setSelection(totalCost.getRecordedUnit().getId());
	}

	/**
	 * Method to extract the cost of the "small parts/extra materials"
	 */
	private void updateExtraMaterialsCost() {
		double value = 0.0;
		Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
		try {
			 value = Double.parseDouble(editTextSmallPartsCost.getText().toString());
		} catch (NumberFormatException ex) {
			// This is not a mandatory field
		}

		tyreChangeEntry.setExtraMaterialCost(new Cost(value, currency));
	}

	/**
	 * Method to extract the cost of the labor within the tyre change entry
	 */
	private void updateLaborCost() {
		double value = 0.0;
		Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
		try {
			value = Double.parseDouble(editTextLaborCost.getText().toString());
		} catch (NumberFormatException ex) {
			// This is not a mandatory field
		}

		tyreChangeEntry.setLaborCost(new Cost(value, currency));
	}

	@Override
	protected void reloadReferences() {
		entry = tyreChangeEntry;
		super.reloadReferences();
	}

	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateTyresCost(false);
		updateExtraMaterialsCost();
		updateLaborCost();
	}

	/**
	 * Method to handle the clicks on the buttons in the activity
	 * @param view as passed by activity
	 */
	@Override
	public boolean onClick(View view) {
		if (super.onClick(view)) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}

		switch (view.getId()) {
			case R.id.activity_tyre_change_button_advanced:
				Intent i = new Intent(this, ActivityTyreChangeScheme.class);
				if (tyreChangeEntry != null) {
					// Ugly hack:
					// We need to remove Consumption object as it contains elements from external libraries which can't be serialized
					tyreChangeEntry.setConsumption(null);
					i.putExtra(ActivityTyreChangeScheme.INTENT_TYRE_ENTRY, tyreChangeEntry);
				}
				startActivityForResult(i, REQUEST_CODE_SCHEME);
				return true;
		}

		return false;
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
		case REQUEST_CODE_SCHEME:
			// We're coming back from ActivityTyreChangeScheme
			if (resultCode == RESULT_OK) {
				tyreChangeEntry = (TyreChangeEntry) data.getExtras().getSerializable(ActivityTyreChangeScheme.INTENT_TYRE_ENTRY);
				// After a deserialization tyreChangeEntry is a new object. Updating the reference.
				reloadReferences();
				updateTyresCost(true);
				setUpdateOngoing(true);
			} else if (resultCode == RESULT_CANCELED) {
				// nothing needed to be done if the scheme addition was cancelled
			}
			break;
		}
	}
}
