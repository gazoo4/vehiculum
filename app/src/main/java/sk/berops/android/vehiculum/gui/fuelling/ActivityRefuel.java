package sk.berops.android.vehiculum.gui.fuelling;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.NoSuchElementException;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.vehiculum.gui.common.GuiUtils;

public class ActivityRefuel extends ActivityEntryGenericAdd {

    protected EditText editTextQuantity;
    protected Spinner spinnerFuelType;
    protected Spinner spinnerQuantityUnit;
    protected FuellingEntry fuellingEntry;
	protected FuelUnitAdapter fuelUnitAdapter;
    private TextView textViewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (fuellingEntry == null) {
            fuellingEntry = new FuellingEntry();
        }
        super.entry = this.fuellingEntry;

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_refuel);
    }

    @Override
    protected void attachGuiObjects() {
	    super.attachGuiObjects();
        textViewDistanceUnit = (TextView) findViewById(R.id.activity_refuel_distance_unit);
        textViewPrice = (TextView) findViewById(R.id.activity_refuel_price_text);

        editTextMileage = (EditText) findViewById(R.id.activity_refuel_mileage);
        editTextCost = (EditText) findViewById(R.id.activity_refuel_cost);
        editTextComment = (EditText) findViewById(R.id.activity_refuel_comment);
        editTextQuantity = (EditText) findViewById(R.id.activity_refuel_quantity);

        spinnerFuelType = (Spinner) findViewById(R.id.activity_refuel_fuel_type);
        spinnerCurrency = (Spinner) findViewById(R.id.activity_refuel_currency);
        spinnerQuantityUnit = (Spinner) findViewById(R.id.activity_refuel_quantity_unit);

        listEditTexts.add(editTextMileage);
        listEditTexts.add(editTextCost);
        listEditTexts.add(editTextComment);
        listEditTexts.add(editTextQuantity);

        mapSpinners.put(R.array.activity_refuel_fuel_type, spinnerFuelType);
    }

    @Override
    protected void initializeGuiObjects() {
        super.initializeGuiObjects();

        FuelType fuelType;
        try {
            fuelType = car.getHistory().getFuellingEntries().getLast().getFuelType();
        } catch (NoSuchElementException e) {
            fuelType = FuelType.getFuelType(0);
        }

	    spinnerFuelType.setSelection(fuelType.getId());
	    CharSequence[] unitList = getResources().getStringArray(R.array.activity_refuel_quantity_unit);
	    fuelUnitAdapter = new FuelUnitAdapter(this, R.layout.spinner_white, unitList);
	    fuelUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerQuantityUnit.setAdapter(fuelUnitAdapter);
        refreshFuelType(fuelType);
    }

	@Override
	public void afterTextChanged(Editable s) {
		super.afterTextChanged(s);
		if (s == editTextQuantity.getText()
				|| s == editTextCost.getText()) {
			refreshPrice();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		super.onItemSelected(parent, view, position, id);
		if (parent == spinnerCurrency
				|| parent == spinnerQuantityUnit) {
			refreshPrice();
		}

		if (parent == spinnerFuelType) {
			refreshFuelType(FuelType.getFuelType(spinnerFuelType.getSelectedItemPosition()));
		}
	}

	protected void refreshFuelType(FuelType type) {
		fuelUnitAdapter.mask(type);
		UnitConstants.QuantityUnit quantityUnit;
		quantityUnit = preferences.getQuantityUnit(type);
		spinnerQuantityUnit.setSelection(quantityUnit.getId());
	}

    protected void refreshPrice() {
        double quantity;
        double cost;
        double price;
        Currency.Unit currency;
        UnitConstants.QuantityUnit quantityUnit;

        try {
            quantity = GuiUtils.extractDouble(editTextQuantity);
            cost = GuiUtils.extractDouble(editTextCost);
            price = cost / quantity;

            String formattedPrice;
            String unit;
            DecimalFormat df = new DecimalFormat("##.###");
            formattedPrice = df.format(price);

            currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
            quantityUnit = UnitConstants.QuantityUnit.getQuantityUnit(spinnerQuantityUnit.getSelectedItemPosition());
            unit = "" + currency.getUnitIsoCode() + "/" + quantityUnit.getUnit();
            textViewPrice.setText(formattedPrice + " " + unit);
        } catch (NumberFormatException e) {
            // Both the fields need to be filled-in (quantity, cost)
        }
    }

    private void updateFuelQuantity() throws NotFoundException, FieldEmptyException {
        double quantity = 0;
        UnitConstants.QuantityUnit quantityUnit;
        quantityUnit = UnitConstants.QuantityUnit.getQuantityUnit(spinnerQuantityUnit.getSelectedItemPosition());
        try {
            quantity = GuiUtils.extractDouble(editTextQuantity);
        } catch (NumberFormatException ex) {
            throwAlertFieldsEmpty(R.string.activity_refuel_quantity_hint);
        }
        fuellingEntry.setFuelQuantity(quantity, quantityUnit);
    }

    private void updateFuelType() {
        fuellingEntry.setFuelType(FuellingEntry.FuelType
                .getFuelType(spinnerFuelType.getSelectedItemPosition()));
    }

    @Override
    public boolean onClick(View view) {
	    if (super.onClick(view)) {
		    startActivity(new Intent(this, MainActivity.class));
		    return true;
	    }

	    return false;
    }

    @Override
    protected void updateFields() throws FieldEmptyException {
        super.updateFields();
        updateFuelQuantity();
        updateFuelType();
    }
}
