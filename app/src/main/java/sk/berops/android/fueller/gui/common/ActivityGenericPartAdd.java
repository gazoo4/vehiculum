package sk.berops.android.fueller.gui.common;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart;
import sk.berops.android.fueller.dataModel.maintenance.GenericPart.Condition;

public abstract class ActivityGenericPartAdd extends ActivityExpenseAdd {
	
	protected EditText editTextBrand;
	protected EditText editTextProducerPartId;
	protected EditText editTextCarmakerPartId;
	protected EditText editTextQuantity;
	protected Spinner spinnerCondition;
	
	protected GenericPart genericPart;
	
	protected int quantity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.expense = genericPart;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		UtilsActivity.styleEditText(editTextBrand);
		UtilsActivity.styleEditText(editTextProducerPartId);
		UtilsActivity.styleEditText(editTextCarmakerPartId);
		UtilsActivity.styleEditText(editTextQuantity);
		UtilsActivity.styleSpinner(spinnerCondition, this, R.array.activity_generic_part_add_condition);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException{
		super.updateFields();
		updateBrand();
		updatePartsId();
		updateQuantity();
		updateCondition();
	}
	
	protected void updateBrand() {
		genericPart.setBrand(editTextBrand.getText().toString());
	}
	
	protected void updatePartsId() {
		genericPart.setCarmakerPartID(editTextCarmakerPartId.getText().toString());
		genericPart.setProducerPartID(editTextProducerPartId.getText().toString());
	}
	
	protected void updateQuantity() throws FieldsEmptyException {
		try {
			genericPart.setQuantity(GuiUtils.extractInteger(editTextQuantity));
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_generic_part_add_quantity_hint);
		}
	}
	
	private void updateCondition() {
		Condition condition = Condition.getCondition(spinnerCondition.getSelectedItemPosition());
		genericPart.setCondition(condition);
	}
}
