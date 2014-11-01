package sk.berops.android.fueller.gui.maintenance;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;

public class ActivityTyreChange extends ActivityAddEventGeneric {
	
	private Car car;
	private LinkedList<Axle> axles; 
	protected TyreChangeEntry tyreChangeEntry;
	protected EditText editTextLaborCost;
	protected EditText editTextSmallPartsCost;
	private LinearLayout tyreChangeScheme;
	
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_change);
		car = MainActivity.garage.getActiveCar();
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry(car);
		}
		tyreChangeEntry.setExpenseType(ExpenseType.TYRES);
		
		super.entry = (Entry) this.tyreChangeEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
		
		buildDynamicLayout();
	}
	
	private void buildDynamicLayout() {
		tyreChangeScheme = (LinearLayout) findViewById(R.id.activity_tyre_change_scheme_layout);
		axles = car.getAxles();
		
		Axle axle;
		Iterator<Axle> axIt = axles.iterator();
		while (axIt.hasNext()) {
			axle = axIt.next();
			addAxleToLayout(tyreChangeScheme, axle);
		}
	}
	
	private void addAxleToLayout(LinearLayout tyreChangeScheme, Axle axle) {
		Tyre[] tyres = axle.getTyres();
		Context context = this;
		
		LinearLayout axleScheme = new LinearLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		axleScheme.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView imageView;
		int id = getResources().getIdentifier("tyre", "drawable", getPackageName());
		
		for (Tyre tyre : tyres) {
			imageView = new ImageView(context);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.tyre));
			imageView.setPadding(30, 30, 30, 30);
			axleScheme.addView(imageView, params);
			Log.d("DEBUG", Integer.toString(axleScheme.getChildCount()));
		}
		
		tyreChangeScheme.addView(axleScheme);
	}

	@Override
	protected void attachGuiObjects() {
		editTextCost = null; //this edit text is not used in this view

		editTextLaborCost = (EditText) findViewById(R.id.activity_tyre_change_labor_cost);
		editTextSmallPartsCost = (EditText) findViewById(R.id.activity_tyre_change_small_parts_cost);
		editTextMileage = (EditText) findViewById(R.id.activity_tyre_change_mileage);
		editTextComment = (EditText) findViewById(R.id.activity_tyre_change_comment);
		textViewDisplayDate = (TextView) findViewById(R.id.activity_tyre_change_date_text);
	}

	@Override
	protected void styleGuiObjects() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void updateCost() {
		Double cost = 0.0;
		try {
			cost += Double.parseDouble(editTextLaborCost.getText().toString());
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(R.string.activity_tyre_change_labor_cost_hint));
		}
		try {
			cost += Double.parseDouble(editTextSmallPartsCost.getText().toString());
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(R.string.activity_tyre_change_small_parts_cost_hint));
		}
		entry.setCost(cost);
	}
	
	public void onClick(View view) {
		entryOK = true;
		saveEntry(view);
		if (entryOK) {
			switch (view.getId()) {
			case R.id.activity_tyre_change_button_commit:
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
				break;
			}
		}
	}
	
	public void saveEntry(View view) {
		// TODO
		//updateFuelVolume();
		//updateFuelType();
		//updateFuelPrice();
	}
}
