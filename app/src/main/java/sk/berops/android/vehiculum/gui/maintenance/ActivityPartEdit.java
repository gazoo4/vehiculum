package sk.berops.android.vehiculum.gui.maintenance;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;

public class ActivityPartEdit extends ActivityPartAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		part = (ReplacementPart) intent.getExtras().get("ReplacementPart");
		super.editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextBrand.setText(part.getBrand());
		editTextProducerPartId.setText(part.getProducerPartID());
		editTextCarmakerPartId.setText(part.getCarmakerPartID());
		editTextComment.setText(part.getComment());
		editTextCost.setText(Double.toString(part.getCost().getRecordedValue()));
		editTextQuantity.setText(Integer.toString(part.getQuantity()));
		
		spinnerCurrency.setSelection(part.getCost().getRecordedUnit().getId());
		spinnerCondition.setSelection(part.getCondition().getId());
		spinnerOriginality.setSelection(part.getOriginality().getId());
	}
}
