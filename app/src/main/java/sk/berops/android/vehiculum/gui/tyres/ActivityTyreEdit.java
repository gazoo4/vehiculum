package sk.berops.android.vehiculum.gui.tyres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

/**
 * @author Bernard Halas
 * @date 1/18/17
 */

public class ActivityTyreEdit extends ActivityTyreAdd {
	protected static String INTENT_TYRE = "Tyre";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		tyre = (Tyre) intent.getExtras().get(INTENT_TYRE);
		super.editMode = true;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		editTextBrand.setText(tyre.getBrand());
		editTextModel.setText(tyre.getModel());
		editTextWidth.setText(Integer.toString(tyre.getWidth()));
		editTextHeight.setText(Integer.toString(tyre.getHeight()));
		editTextDiameter.setText(Double.toString(tyre.getDiameter()));
		editTextWeightIndex.setText(Integer.toString(tyre.getWeightIndex()));
		editTextSpeedIndex.setText(tyre.getSpeedIndex());
		editTextDot.setText(tyre.getDot());
		editTextQuantity.setText(Integer.toString(1));
		editTextQuantity.setVisibility(View.INVISIBLE);
		editTextCost.setText(Double.toString(tyre.getCost()));
		editTextComment.setText(tyre.getComment());

		spinnerCurrency.setSelection(tyre.getCurrency().getId());
		spinnerSeason.setSelection(tyre.getSeason().getId());
		spinnerCondition.setSelection(tyre.getCondition().getId());
	}
}
