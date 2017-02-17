package sk.berops.android.caramel.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.caramel.R;
import sk.berops.android.caramel.dataModel.expense.FieldEmptyException;
import sk.berops.android.caramel.dataModel.maintenance.ReplacementPart;
import sk.berops.android.caramel.dataModel.maintenance.ReplacementPart.Originality;
import sk.berops.android.caramel.gui.common.ActivityGenericPartAdd;
import sk.berops.android.caramel.gui.common.GuiUtils;

public class ActivityPartAdd extends ActivityGenericPartAdd {

	protected Spinner spinnerOriginality;

	protected ReplacementPart part;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (part == null) {
			part = new ReplacementPart();
		}

		super.genericPart = this.part;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_part_add);
	}

	@Override
	protected void attachGuiObjects() {
		editTextCost = (EditText) findViewById(R.id.activity_part_add_price);
		editTextComment = (EditText) findViewById(R.id.activity_part_add_comment);
		editTextBrand = (EditText) findViewById(R.id.activity_part_add_brand);
		editTextProducerPartId = (EditText) findViewById(R.id.activity_part_add_manufacturer_part_id);
		editTextCarmakerPartId = (EditText) findViewById(R.id.activity_part_add_carmaker_part_id);
		editTextQuantity = (EditText) findViewById(R.id.activity_part_add_quantity);
		spinnerCurrency = (Spinner) findViewById(R.id.activity_part_add_currency);
		spinnerCondition = (Spinner) findViewById(R.id.activity_part_add_condition);
		spinnerOriginality = (Spinner) findViewById(R.id.activity_part_add_originality);

		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);
		listEditTexts.add(editTextBrand);
		listEditTexts.add(editTextProducerPartId);
		listEditTexts.add(editTextCarmakerPartId);
		listEditTexts.add(editTextQuantity);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
		mapSpinners.put(R.array.activity_generic_part_add_condition, spinnerCondition);
		mapSpinners.put(R.array.activity_part_add_originality, spinnerOriginality);
	}

	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateOriginality();
		updateQuantity();
	}

	private void updateOriginality() {
		Originality originality = Originality.getOriginality(spinnerOriginality.getSelectedItemPosition());
		part.setOriginality(originality);
	}

	protected void updateQuantity() throws FieldEmptyException {
		try {
			if (editTextQuantity.getText() == null) {
				// If user doesn't enter a quantifier, we assume he means 1 piece
				part.setQuantity(1);
			} else {
				part.setQuantity(GuiUtils.extractInteger(editTextQuantity));
			}
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_generic_part_add_quantity_hint);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_part_add_button_commit:
			try {
			updateFields();
			Intent returnIntent = new Intent();
			returnIntent.putExtra("part", part);
			setResult(RESULT_OK, returnIntent);
			finish();
			} catch (FieldEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}
}
