package sk.berops.android.fueller.gui.maintenance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.Tyre.Season;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.fueller.gui.common.ActivityExpenseAdd;
import sk.berops.android.fueller.gui.common.ActivityGenericPartAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.UtilsActivity;

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
		setContentView(R.layout.activity_tyre_add);

		if (tyre == null) {
			tyre = new Tyre();
		}

		super.genericPart = (GenericPart) this.tyre;
		super.onCreate(savedInstanceState);
	}

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

	}

	protected void styleGuiObjects() {
		super.styleGuiObjects();
		UtilsActivity.styleEditText(editTextBrand);
		UtilsActivity.styleEditText(editTextModel);
		UtilsActivity.styleEditText(editTextWidth);
		UtilsActivity.styleEditText(editTextHeight);
		UtilsActivity.styleEditText(editTextDiameter);
		UtilsActivity.styleEditText(editTextWeightIndex);
		UtilsActivity.styleEditText(editTextSpeedIndex);
		UtilsActivity.styleEditText(editTextDot);
		UtilsActivity.styleEditText(editTextThreadLevel);
		
		UtilsActivity.styleSpinner(spinnerSeason, this, R.array.activity_tyre_add_season);
	}

	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
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
