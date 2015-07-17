package sk.berops.android.fueller.gui.common;

import java.util.NoSuchElementException;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.gui.Colors;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ActivityAddExpense extends ActivityAddRecord {

	protected EditText editTextCost;
	protected Spinner spinnerCurrency;
	
	@Override
	protected void attachGuiObjects() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void styleGuiObjects() {
		editTextComment.setHintTextColor(Colors.LIGHT_GREEN);
		if (editTextCost != null) {
			//in some child views the cost is not used (and instead laborCost might be used)
			editTextCost.setHintTextColor(Colors.LIGHT_GREEN);
		}
		
		ArrayAdapter<CharSequence> adapterCurrency = ArrayAdapter
				.createFromResource(this, R.array.activity_generic_currency, 
						R.layout.spinner_white);
		adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCurrency.setAdapter(adapterCurrency);
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

}
