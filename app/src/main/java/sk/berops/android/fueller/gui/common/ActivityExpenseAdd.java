package sk.berops.android.fueller.gui.common;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.NoSuchElementException;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.expense.Expense;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;

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
	protected void initializeGuiObjects() {
		Currency.Unit currency;
		try {
			currency = car.getHistory().getEntries().getLast().getCurrency();
		} catch (NoSuchElementException e) {
			currency = Preferences.getInstance().getCurrency();
		}
		spinnerCurrency.setSelection(currency.getId());
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateCost();
	}
	
	private void updateCost() throws FieldsEmptyException {
		try {
			Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
			expense.setCost(GuiUtils.extractDouble(editTextCost), currency);
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_expense_add_cost_hint);
		}
	}
}
