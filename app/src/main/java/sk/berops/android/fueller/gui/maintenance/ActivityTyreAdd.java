package sk.berops.android.fueller.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.Tyre.Season;
import sk.berops.android.fueller.gui.common.ActivityGenericPartAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;

public class ActivityTyreAdd extends ActivityGenericPartAdd {

	private EditText editTextModel;
	private EditText editTextWidth;
	private EditText editTextHeight;
	private EditText editTextDiameter;
	private EditText editTextWeightIndex;
	private EditText editTextSpeedIndex;
	private EditText editTextDot;
	private EditText editTextThreadLevel;
	private Spinner spinnerSeason;
	
	protected boolean editMode;
	protected Tyre tyre;

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

	private void updateModel() {
		tyre.setModel(editTextModel.getText().toString());
	}
	
	private void updateWidth() {
		tyre.setWidth(GuiUtils.extractInteger(editTextWidth));
	}
	
	private void updateHeight() {
		tyre.setHeight(GuiUtils.extractInteger(editTextHeight));
	}
	
	private void updateDiameter() {
		tyre.setDiameter(GuiUtils.extractDouble(editTextDiameter));
	}
	
	private void updateWeightIndex() {
		tyre.setWeightIndex(GuiUtils.extractInteger(editTextWeightIndex));
	}
	
	private void updateSpeedIndex() {
		tyre.setSpeedIndex(editTextSpeedIndex.getText().toString());
	}
	
	private void updateDot() {
		tyre.setDot(editTextDot.getText().toString());
	}
	
	private void updateThreadLevel() {
		tyre.setThreadLevel(GuiUtils.extractDouble(editTextThreadLevel));
	}
	
	private void updateSeason() {
		Season season = Season.getSeason(spinnerSeason.getSelectedItemPosition());
		tyre.setSeason(season);
	}
	
	protected void updateFields() throws FieldsEmptyException {
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

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_tyre_add_button_commit:
			try {
				updateFields();
				Intent returnIntent = new Intent();
				returnIntent.putExtra("tyre", tyre);
				setResult(RESULT_OK, returnIntent);
				finish();
			} catch (FieldsEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}
}
