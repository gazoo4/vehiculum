package sk.berops.android.vehiculum.gui.fuelling;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
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

    protected EditText editTextVolume;
    protected Spinner spinnerFuelType;
    protected Spinner spinnerVolumeUnit;
    protected FuellingEntry fuellingEntry;
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
        textViewDistanceUnit = (TextView) findViewById(R.id.activity_refuel_distance_unit);
        textViewPrice = (TextView) findViewById(R.id.activity_refuel_price_text);

        editTextMileage = (EditText) findViewById(R.id.activity_refuel_mileage);
        editTextCost = (EditText) findViewById(R.id.activity_refuel_cost);
        editTextComment = (EditText) findViewById(R.id.activity_refuel_comment);
        editTextVolume = (EditText) findViewById(R.id.activity_refuel_volume);

        buttonDate = (Button) findViewById(R.id.activity_refuel_date_button);
        buttonTagAdd = (Button) findViewById(R.id.activity_refuel_button_tag_add);

        spinnerFuelType = (Spinner) findViewById(R.id.activity_refuel_fuel_type);
        spinnerCurrency = (Spinner) findViewById(R.id.activity_refuel_currency);
        spinnerVolumeUnit = (Spinner) findViewById(R.id.activity_refuel_volume_unit);

        listButtons.add(buttonDate);
        listButtons.add(buttonTagAdd);

        listEditTexts.add(editTextMileage);
        listEditTexts.add(editTextCost);
        listEditTexts.add(editTextComment);
        listEditTexts.add(editTextVolume);

        mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
        mapSpinners.put(R.array.activity_refuel_fuel_type, spinnerFuelType);
        mapSpinners.put(R.array.activity_refuel_volume_unit, spinnerVolumeUnit);
    }

    @Override
    protected void initializeGuiObjects() {
        super.initializeGuiObjects();
        initializeTags(R.id.activity_refuel_tags_recyclerview);

        FuelType fuelType;
        try {
            fuelType = car.getHistory().getFuellingEntries().getLast().getFuelType();
        } catch (NoSuchElementException e) {
            fuelType = FuelType.getFuelType(0);
        }
        spinnerFuelType.setSelection(fuelType.getId());

        UnitConstants.VolumeUnit volumeUnit;
        volumeUnit = car.getVolumeUnit();
        spinnerVolumeUnit.setSelection(volumeUnit.getId());

        PriceCalculateListener priceCalculator = new PriceCalculateListener();
        editTextCost.addTextChangedListener(priceCalculator);
        editTextVolume.addTextChangedListener(priceCalculator);
        spinnerCurrency.setOnItemSelectedListener(priceCalculator);
        spinnerVolumeUnit.setOnItemSelectedListener(priceCalculator);
    }

    protected void refreshPrice() {
        double volume;
        double cost;
        double price;
        Currency.Unit currency;
        UnitConstants.VolumeUnit volumeUnit;

        try {
            //TODO: here we should reflect the units we've bought in here
            volume = GuiUtils.extractDouble(editTextVolume);
            cost = GuiUtils.extractDouble(editTextCost);
            price = cost / volume;

            String formattedPrice;
            String unit;
            DecimalFormat df = new DecimalFormat("##.###");
            formattedPrice = df.format(price);

            currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
            volumeUnit = UnitConstants.VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition());
            unit = "" + currency.getUnit() + "/" + volumeUnit.getUnit();
            textViewPrice.setText(formattedPrice + " " + unit);
        } catch (NumberFormatException e) {
            // Both the fields need to be filled-in (volume, cost)
        }
    }

    private void updateFuelVolume() throws NotFoundException, FieldEmptyException {
        double volume = 0;
        UnitConstants.VolumeUnit volumeUnit;
        volumeUnit = UnitConstants.VolumeUnit.getVolumeUnit(spinnerVolumeUnit.getSelectedItemPosition());
        try {
            volume = GuiUtils.extractDouble(editTextVolume);
        } catch (NumberFormatException ex) {
            throwAlertFieldsEmpty(R.string.activity_refuel_volume_hint);
        }
        fuellingEntry.setFuelVolume(volume, volumeUnit);
    }

    private void updateFuelType() {
        fuellingEntry.setFuelType(FuellingEntry.FuelType
                .getFuelType(spinnerFuelType.getSelectedItemPosition()));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_refuel_button_commit:
                try {
                    super.saveFieldsAndPersist(view);
                    startActivity(new Intent(this, MainActivity.class));
                } catch (FieldEmptyException ex) {
                    ex.throwAlert();
                }
                break;
        }
    }

    @Override
    protected void updateFields() throws FieldEmptyException {
        super.updateFields();
        updateFuelVolume();
        updateFuelType();
    }

    class PriceCalculateListener implements TextWatcher, OnItemSelectedListener {

        @Override
        public void afterTextChanged(Editable s) {
            refreshPrice();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            refreshPrice();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    }
}
