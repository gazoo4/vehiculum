package sk.berops.android.fueller.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart.Condition;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart.Originality;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.common.ActivityAddExpense;
import sk.berops.android.fueller.gui.common.ActivityAddRecord;
import sk.berops.android.fueller.gui.common.GuiUtils;

public class ActivityPartAdd extends ActivityAddExpense {

	EditText editTextProducer;
	EditText editTextManufacturerPartID;
	EditText editTextCarmakerPartID;
	Spinner spinnerCondition;
	Spinner spinnerOriginality;

	protected boolean editMode;
	protected ReplacementPart part;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_part_add);
		if (part == null) {
			part = new ReplacementPart();
		}

		super.record = (Record) this.part;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		editTextCost = (EditText) findViewById(R.id.activity_part_add_price);
		editTextComment = (EditText) findViewById(R.id.activity_part_add_comment);
		editTextProducer = (EditText) findViewById(R.id.activity_part_add_producer);
		editTextManufacturerPartID = (EditText) findViewById(R.id.activity_part_add_manufacturer_part_id);
		editTextCarmakerPartID = (EditText) findViewById(R.id.activity_part_add_carmaker_part_id);
		spinnerCurrency = (Spinner) findViewById(R.id.activity_part_add_currency);
		spinnerCondition = (Spinner) findViewById(R.id.activity_part_add_condition);
		spinnerOriginality = (Spinner) findViewById(R.id.activity_part_add_originality);
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		editTextProducer.setHintTextColor(Colors.LIGHT_GREEN);
		editTextManufacturerPartID.setHintTextColor(Colors.LIGHT_GREEN);
		editTextCarmakerPartID.setHintTextColor(Colors.LIGHT_GREEN);

		ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter
				.createFromResource(this, R.array.activity_part_add_condition,
						R.layout.spinner_white);
		adapterCondition
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCondition.setAdapter(adapterCondition);

		ArrayAdapter<CharSequence> adapterOriginality = ArrayAdapter
				.createFromResource(this,
						R.array.activity_part_add_originality,
						R.layout.spinner_white);
		adapterOriginality
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOriginality.setAdapter(adapterOriginality);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();

	}

	private void updateFields() {
		updateProducer();
		updatePartsID();
		updatePrice();
		updateComment();
		updateOriginality();
		updateCondition();
	}

	private void updateProducer() {
		part.setProducer(editTextProducer.getText().toString());
	}

	private void updatePartsID() {
		part.setCarmakerPartID(editTextCarmakerPartID.getText().toString());
		part.setProducerPartID(editTextManufacturerPartID.getText().toString());
	}

	private void updatePrice() {
		try {
			Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
			part.setPrice(GuiUtils.extractDouble(editTextCost), currency);
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(
					R.string.activity_part_add_price_hint));
		}
	}

	private void updateComment() {
		part.setComment(editTextComment.getText().toString());
	}

	private void updateOriginality() {
		Originality originality = Originality.getOriginality(spinnerOriginality.getSelectedItemPosition());
		part.setOriginality(originality);
	}

	private void updateCondition() {
		Condition condition = Condition.getCondition(spinnerCondition.getSelectedItemPosition());
		part.setCondition(condition);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_part_add_button_commit:
			updateFields();
			Intent returnIntent = new Intent();
			returnIntent.putExtra("part", part);
			setResult(RESULT_OK, returnIntent);
			finish();
			break;
		}
	}

}
