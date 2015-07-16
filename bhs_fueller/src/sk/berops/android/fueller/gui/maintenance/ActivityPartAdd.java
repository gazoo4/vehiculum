package sk.berops.android.fueller.gui.maintenance;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.gui.common.ActivityAddExpense;
import sk.berops.android.fueller.gui.common.ActivityAddRecord;

public class ActivityPartAdd extends ActivityAddExpense {
	
	EditText editTextProducer;
	EditText editTextManufacturerPartID;
	EditText editTextCarmakerPartID;
	
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
		editTextProducer = (EditText) findViewById(R.id.activity_part_add_producer);
		editTextManufacturerPartID = (EditText) findViewById(R.id.activity_part_add_manufacturer_part_id);
		editTextCarmakerPartID = (EditText) findViewById(R.id.activity_part_add_carmaker_part_id);
		spinnerCurrency = (Spinner) findViewById(R.id.activity_part_add_currency);
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		
	}
	
	public void onClick(View view) {
		
	}

}
