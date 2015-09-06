package sk.berops.android.fueller.gui.maintenance;

import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import android.content.Intent;
import android.os.Bundle;

public class ActivityPartEdit extends ActivityPartAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		part = (ReplacementPart) intent.getExtras().get("ReplacementPart");
		super.part = part;
		super.editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextProducer.setText(part.getProducer());
		editTextProducerPartId.setText(part.getProducerPartID());
		editTextCarmakerPartId.setText(part.getCarmakerPartID());
		editTextComment.setText(part.getComment());
		editTextCost.setText(Double.toString(part.getCost()));
		editTextQuantity.setText(Integer.toString(part.getQuantity()));
		
		spinnerCurrency.setSelection(part.getCurrency().getId());
		spinnerCondition.setSelection(part.getCondition().getId());
		spinnerOriginality.setSelection(part.getOriginality().getId());
	}
}
