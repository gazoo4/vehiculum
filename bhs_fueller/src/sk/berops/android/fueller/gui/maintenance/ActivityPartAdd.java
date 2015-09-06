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
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart.Condition;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart.Originality;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.common.ActivityExpenseAdd;
import sk.berops.android.fueller.gui.common.ActivityGenericPartAdd;
import sk.berops.android.fueller.gui.common.ActivityRecordAdd;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.UtilsActivity;

public class ActivityPartAdd extends ActivityGenericPartAdd {

	protected Spinner spinnerOriginality;

	protected ReplacementPart part;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_part_add);
		if (part == null) {
			part = new ReplacementPart();
		}

		super.genericPart = (GenericPart) this.part;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		editTextCost = (EditText) findViewById(R.id.activity_part_add_price);
		editTextComment = (EditText) findViewById(R.id.activity_part_add_comment);
		editTextProducer = (EditText) findViewById(R.id.activity_part_add_producer);
		editTextProducerPartId = (EditText) findViewById(R.id.activity_part_add_manufacturer_part_id);
		editTextCarmakerPartId = (EditText) findViewById(R.id.activity_part_add_carmaker_part_id);
		editTextQuantity = (EditText) findViewById(R.id.activity_part_add_quantity);
		spinnerCurrency = (Spinner) findViewById(R.id.activity_part_add_currency);
		spinnerCondition = (Spinner) findViewById(R.id.activity_part_add_condition);
		spinnerOriginality = (Spinner) findViewById(R.id.activity_part_add_originality);
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		UtilsActivity.styleSpinner(spinnerOriginality, this, R.array.activity_part_add_originality);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}

	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateOriginality();
	}

	private void updateOriginality() {
		Originality originality = Originality.getOriginality(spinnerOriginality.getSelectedItemPosition());
		part.setOriginality(originality);
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
			} catch (FieldsEmptyException e) {
				e.throwAlert();
			}
			break;
		}
	}
}
