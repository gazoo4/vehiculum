package sk.berops.android.fueller.gui.maintenance;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;
import sk.berops.android.fueller.gui.common.GuiUtils;

public class ActivityTyreChange extends ActivityAddEventGeneric {
	
	class PriceCalculateListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			refreshCosts();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}
	
	Double cost = 0.0;
	Double laborCost, extraMaterialCost, tyresCost;
	
	private Car car;
	protected static TyreChangeEntry tyreChangeEntryStatic; //used to access the entry from subsequent Activities
	protected TyreChangeEntry tyreChangeEntry;
	protected EditText editTextTyresCost;
	protected EditText editTextLaborCost;
	protected EditText editTextSmallPartsCost;
	
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_change);
		car = MainActivity.garage.getActiveCar();
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry();
		}
		tyreChangeEntry.setExpenseType(ExpenseType.TYRES);
		
		super.entry = (Entry) this.tyreChangeEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		editTextTyresCost = (EditText) findViewById(R.id.activity_tyre_change_tyres_cost);
		editTextLaborCost = (EditText) findViewById(R.id.activity_tyre_change_labor_cost);
		editTextSmallPartsCost = (EditText) findViewById(R.id.activity_tyre_change_small_parts_cost);
		editTextCost = (EditText) findViewById(R.id.activity_tyre_change_total_cost);
		editTextMileage = (EditText) findViewById(R.id.activity_tyre_change_mileage);
		editTextComment = (EditText) findViewById(R.id.activity_tyre_change_comment);

		textViewDisplayDate = (TextView) findViewById(R.id.activity_tyre_change_date_text);
		
		PriceCalculateListener priceCalculator = new PriceCalculateListener();
		editTextTyresCost.addTextChangedListener(priceCalculator);
		editTextLaborCost.addTextChangedListener(priceCalculator);
		editTextSmallPartsCost.addTextChangedListener(priceCalculator);
	}

	@Override
	protected void styleGuiObjects() {
		// TODO Auto-generated method stub
	}
	
	private void readCosts() {
		cost = 0.0;
		try {
			laborCost = GuiUtils.extractDouble(editTextLaborCost);
			tyreChangeEntry.setLaborCost(laborCost);
			cost += laborCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_labor_cost_hint));
		}
		try {
			extraMaterialCost = GuiUtils.extractDouble(editTextSmallPartsCost);
			tyreChangeEntry.setExtraMaterialCost(extraMaterialCost);
			cost += extraMaterialCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}
		try {
			tyresCost = GuiUtils.extractDouble(editTextTyresCost);
			tyreChangeEntry.setTyresCost(tyresCost);
			cost += tyresCost;
		} catch (NumberFormatException ex) {
			Log.d("DEBUG", "Reading costs, hit an empty field: "+ getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}
	}
	
	protected void refreshCosts() {
		readCosts();
		editTextCost.setText(cost.toString());
	}
	
	@Override
	protected void updateCost() {
		readCosts();
		//entry.setCost(cost);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_tyre_change_button_advanced:
			tyreChangeEntryStatic = tyreChangeEntry;
			startActivity(new Intent(this, ActivityTyreChangeScheme.class));
			break;
		case R.id.activity_tyre_change_button_commit:
			entryOK = true;
			saveEntry(view);
			if (entryOK) {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			}
			break;
		}
	}
	
	public void saveEntry(View view) {
		updateCost();
	}
}
