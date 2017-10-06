package sk.berops.android.vehiculum.gui.common;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.NoSuchElementException;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Expense;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;

public abstract class ActivityExpenseAdd extends ActivityRecordAdd {

	protected EditText editTextCost;
	protected Spinner spinnerCurrency;
	
	protected Expense expense;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.record = expense;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		mapSpinners.put(Currency.Unit.extractCodesAndSymbols(), spinnerCurrency);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();

		Currency.Unit currency;
		try {
			currency = car.getHistory().getEntries().getLast().getCost().getRecordedUnit();
		} catch (NoSuchElementException e) {
			currency = Preferences.getInstance().getCurrency();
		}
		spinnerCurrency.setSelection(currency.getId());
	}

	/**
	 * This method is used usually after deserialization of the Expense object so that the link to
	 * the Record (parent object) in the parent ActivityRecordAd  points correctly to this Expense
	 */
	protected void reloadReferences() {
		record = expense;
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateCost();
	}
	
	private void updateCost() throws FieldEmptyException {
		try {
			Currency.Unit unit = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
			double value = GuiUtils.extractDouble(editTextCost);
			expense.setCost(new Cost(value, unit));
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_expense_add_cost_hint);
		}
	}
}
