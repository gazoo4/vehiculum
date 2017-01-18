package sk.berops.android.fueller.gui.tyres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.FieldEmptyException;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.Tyre.Season;
import sk.berops.android.fueller.gui.common.ActivityGenericPartAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;

import static sk.berops.android.fueller.gui.common.GuiUtils.isEmptyEditText;

public class ActivityTyreAdd extends ActivityGenericPartAdd {

	protected EditText editTextModel;
	protected EditText editTextWidth;
	protected EditText editTextHeight;
	protected EditText editTextDiameter;
	protected EditText editTextWeightIndex;
	protected EditText editTextSpeedIndex;
	protected EditText editTextDot;
	protected EditText editTextThreadLevel;
	protected Spinner spinnerSeason;

	protected boolean editMode;
	protected Tyre tyre;

	public static final String INTENT_TYRE = "add tyre";
	public static final String INTENT_COUNT = "tyres count";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (tyre == null) {
			tyre = new Tyre();
		}

		super.genericPart = this.tyre;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_tyre_add);
	}

	@Override
	protected void attachGuiObjects() {
		editTextBrand = (EditText) findViewById(R.id.activity_tyre_add_brand);
		editTextModel = (EditText) findViewById(R.id.activity_tyre_add_model);
		editTextWidth = (EditText) findViewById(R.id.activity_tyre_add_width);
		editTextHeight = (EditText) findViewById(R.id.activity_tyre_add_height);
		editTextDiameter = (EditText) findViewById(R.id.activity_tyre_add_diameter);
		editTextWeightIndex = (EditText) findViewById(R.id.activity_tyre_add_weight_index);
		editTextSpeedIndex = (EditText) findViewById(R.id.activity_tyre_add_speed_index);
		editTextDot = (EditText) findViewById(R.id.activity_tyre_add_dot);
		editTextThreadLevel = (EditText) findViewById(R.id.activity_tyre_add_thread_level);
		editTextQuantity = (EditText) findViewById(R.id.activity_tyre_add_quantity);
		editTextCost = (EditText) findViewById(R.id.activity_tyre_add_price);
		editTextComment = (EditText) findViewById(R.id.activity_tyre_add_comment);
		spinnerSeason = (Spinner) findViewById(R.id.activity_tyre_add_season);
		spinnerCondition = (Spinner) findViewById(R.id.activity_tyre_add_condition);
		spinnerCurrency = (Spinner) findViewById(R.id.activity_tyre_add_currency);

		listEditTexts.add(editTextBrand);
		listEditTexts.add(editTextModel);
		listEditTexts.add(editTextWidth);
		listEditTexts.add(editTextHeight);
		listEditTexts.add(editTextDiameter);
		listEditTexts.add(editTextWeightIndex);
		listEditTexts.add(editTextSpeedIndex);
		listEditTexts.add(editTextDot);
		listEditTexts.add(editTextThreadLevel);
		listEditTexts.add(editTextQuantity);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
		mapSpinners.put(R.array.activity_generic_part_add_condition, spinnerCondition);
		mapSpinners.put(R.array.activity_tyre_add_season, spinnerSeason);
	}

	@Override
	protected void initializeGuiObjects() {
		spinnerCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (spinnerCondition.getSelectedItemPosition() == 0) {
					// If the tyre is new, we don't need to bother with the threadLevel.
					editTextThreadLevel.setVisibility(View.GONE);
				} else {
					editTextThreadLevel.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}

	/**
	 * Method to update the brand of the tyre. This is a mandatory field, that's why an exception
	 * is thrown if this information is not supplied.
	 * @throws FieldEmptyException
	 */
	@Override
	protected void updateBrand() throws FieldEmptyException {
		if (isEmptyEditText(editTextBrand)) {
			throwAlertFieldsEmpty(R.string.activity_tyre_add_brand_hint);
		}
		super.updateBrand();
	}

	/**
	 * Method to update the model name of the tyre. This is a mandatory field, that's why an
	 * exception is thrown if this information is not supplied.
	 * @throws FieldEmptyException
	 */
	private void updateModel() throws FieldEmptyException {
		if (isEmptyEditText(editTextModel)) {
			throwAlertFieldsEmpty(R.string.activity_tyre_add_model_hint);
		}
		tyre.setModel(editTextModel.getText().toString());
	}
	
	private void updateWidth() {
		if (isEmptyEditText(editTextWidth)) {
			return;
		}
		tyre.setWidth(GuiUtils.extractInteger(editTextWidth));
	}
	
	private void updateHeight() {
		if (isEmptyEditText(editTextHeight)) {
			return;
		}
		tyre.setHeight(GuiUtils.extractInteger(editTextHeight));
	}
	
	private void updateDiameter() {
		if (isEmptyEditText(editTextDiameter)) {
			return;
		}
		tyre.setDiameter(GuiUtils.extractDouble(editTextDiameter));
	}
	
	private void updateWeightIndex() {
		if (isEmptyEditText(editTextWeightIndex)) {
			return;
		}
		tyre.setWeightIndex(GuiUtils.extractInteger(editTextWeightIndex));
	}
	
	private void updateSpeedIndex() {
		if (isEmptyEditText(editTextSpeedIndex)) {
			return;
		}
		tyre.setSpeedIndex(editTextSpeedIndex.getText().toString());
	}
	
	private void updateDot() {
		if (isEmptyEditText(editTextDot)) {
			return;
		}
		tyre.setDot(editTextDot.getText().toString());
	}
	
	private void updateThreadLevel() {
		if (spinnerCondition.getSelectedItemPosition() == 0) {
			// If the tyre is new, set threadLevel to max and we're done here
			tyre.setThreadLevel(tyre.NEW_TYRE_THREAD_LEVEL);
		} else if (isEmptyEditText(editTextThreadLevel)) {
			return;
		} else {
			tyre.setThreadLevel(GuiUtils.extractDouble(editTextThreadLevel));
		}
	}
	
	private void updateSeason() {
		Season season = Season.getSeason(spinnerSeason.getSelectedItemPosition());
		tyre.setSeason(season);
	}
	
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateModel();
		updateWidth();
		updateHeight();
		updateDiameter();
		updateWeightIndex();
		updateSpeedIndex();
		updateDot();
		updateThreadLevel();
		updateSeason();
		if (editMode) {
			updateModifiedDate();
		} else {
			updateCreationDate();
		}
	}

	private int getTyreCount() {
		if (isEmptyEditText(editTextQuantity)) {
			return 1;
		}
		return GuiUtils.extractInteger(editTextQuantity);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_tyre_add_button_commit:
			try {
				updateFields();
				Intent returnIntent = new Intent();
				returnIntent.putExtra(INTENT_TYRE, tyre);
				returnIntent.putExtra(INTENT_COUNT, getTyreCount());
				setResult(RESULT_OK, returnIntent);
				finish();
			} catch (FieldEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}
}