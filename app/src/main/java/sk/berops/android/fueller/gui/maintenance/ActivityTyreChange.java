package sk.berops.android.fueller.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.UtilsActivity;

public class ActivityTyreChange extends ActivityEntryGenericAdd {
	
	class PriceCalculateListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			refreshCosts();
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
	
	Double cost = 0.0;
	Double laborCost, extraMaterialCost, tyresCost;
	
	private Car car;
	protected TyreChangeEntry tyreChangeEntry;
	protected EditText editTextTyresCost;
	protected EditText editTextLaborCost;
	protected EditText editTextSmallPartsCost;
	
	protected static final int SCHEME = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_change);
		car = MainActivity.garage.getActiveCar();
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry();
		}
		tyreChangeEntry.setExpenseType(ExpenseType.TYRES);
		
		super.entry = (Entry) this.tyreChangeEntry;
		super.onCreate(savedInstanceState);
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
		textViewDisplayDate = (TextView) findViewById(R.id.activity_tyre_change_date_text);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_tyre_change_total_cost_currency);
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		UtilsActivity.styleEditText(editTextTyresCost);
		UtilsActivity.styleEditText(editTextLaborCost);
		UtilsActivity.styleEditText(editTextSmallPartsCost);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		PriceCalculateListener priceCalculator = new PriceCalculateListener();
		editTextTyresCost.addTextChangedListener(priceCalculator);
		editTextLaborCost.addTextChangedListener(priceCalculator);
		editTextSmallPartsCost.addTextChangedListener(priceCalculator);
	}
	
	private void readCosts() {
		cost = 0.0;
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
	}
	
	protected void refreshCosts() {
		readCosts();
		editTextCost.setText(cost.toString());
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		calculateCost();
	}
	
	protected void calculateCost() {
		// TODO: normally updateCost is in ActivityAddExpense superclass 
		readCosts();
		//entry.setCost(cost);
	}
	
	private void reloadTyresCost() {
		editTextTyresCost.setText(((Double) tyreChangeEntry.getTyresCost()).toString());
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_tyre_change_button_advanced:
			Intent i = new Intent(this, ActivityTyreChangeScheme.class);
			if (tyreChangeEntry.getTyreScheme() != null) {
				i.putExtra("entry", tyreChangeEntry);
			}
			startActivityForResult(i, SCHEME);
			break;
		case R.id.activity_tyre_change_button_commit:
			try {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldsEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCHEME:
			if (resultCode == RESULT_OK) {
				tyreChangeEntry.setTyreScheme((TyreConfigurationScheme) data.getExtras().getSerializable("scheme"));
				tyreChangeEntry.setTyresCost(data.getExtras().getDouble(("tyres cost")));
				reloadTyresCost();
			} else if (resultCode == RESULT_CANCELED) {
				// nothing needed to be done if the scheme addition was cancelled
			}
			break;
		}
	}
}
