package sk.berops.android.vehiculum.gui.common;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.maintenance.GenericPart;
import sk.berops.android.vehiculum.dataModel.maintenance.GenericPart.Condition;

public abstract class ActivityGenericPartAdd extends ActivityExpenseAdd {
	
	protected EditText editTextBrand;
	protected EditText editTextProducerPartId;
	protected EditText editTextCarmakerPartId;
	protected EditText editTextQuantity;
	protected Spinner spinnerCondition;
	
	protected GenericPart genericPart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.expense = genericPart;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateBrand();
		updatePartsId();
		updateCondition();
	}
	
	protected void updateBrand() throws FieldEmptyException {
		if (editTextBrand.getText() != null) {
			genericPart.setBrand(editTextBrand.getText().toString());
		}
	}
	
	protected void updatePartsId() {
		if (editTextCarmakerPartId != null
				&& editTextCarmakerPartId.getText() != null) {
			genericPart.setCarmakerPartID(editTextCarmakerPartId.getText().toString());
		}

		if (editTextProducerPartId != null
				&& editTextProducerPartId.getText() != null) {
			genericPart.setProducerPartID(editTextProducerPartId.getText().toString());
		}
	}
	
	private void updateCondition() {
		Condition condition = Condition.getCondition(spinnerCondition.getSelectedItemPosition());
		genericPart.setCondition(condition);
	}
}
