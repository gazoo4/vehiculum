package sk.berops.android.fueller.gui.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ActivityAddEventGeneric extends ActivityAddRecord implements DatePickerDialog.OnDateSetListener {

	protected EditText editTextMileage;
	protected EditText editTextCost;
	protected EditText editTextComment;
	protected TextView textViewDisplayDate;
	
	protected Entry entry;
	protected boolean editMode;
	protected boolean entryOK;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.record = (Record) this.entry;
		super.onCreate(savedInstanceState);
		
		updateTextAddEventDate();
	}
	
	public void showDatePickerDialog(View v) {
		FragmentDatePicker fragment = new FragmentDatePicker();
		fragment.show(getFragmentManager(), "datePicker");
	}

	private void updateTextAddEventDate() {
		if (entry.getEventDate() == null) {
			entry.setEventDate(Calendar.getInstance().getTime());
		}
		textViewDisplayDate.setText(DateFormat.getDateInstance().format(entry.getEventDate()));
	}
	
	private void updateMileage() {
		double mileage = 0;
		try {
			mileage = Double.parseDouble(editTextMileage.getText().toString());
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(R.string.activity_refuel_mileage_hint));
		}
		
		switch (MainActivity.garage.getActiveCar().getDistanceUnit()) {
			case KILOMETER: entry.setMileage(mileage); break;
			case MILE: entry.setMileage(mileage*1.60934); break;
			default: System.out.println("Very weird distance unit");
		}
		MainActivity.garage.getActiveCar().setCurrentMileage(entry.getMileage());
	}
	
	protected void updateCost() {
		try {
			entry.setCost(Double.parseDouble(editTextCost.getText().toString()));
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(getResources().getString(R.string.activity_refuel_cost_hint));
		}
	}
	
	private void updateComment() {
		entry.setComment(editTextComment.getText().toString());
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		entry.setEventDate(c.getTime());
		updateTextAddEventDate();
	}
	
	public void throwAlertFieldsEmpty(String field) {
		entryOK = false;
		super.throwAlertFieldsEmpty(field);
	}
	
	public void saveFieldsAndPersist(View view) {
		updateMileage();
		updateCost();
		updateComment();
		if (!editMode) {
			MainActivity.garage.getActiveCar().getHistory().getEntries().add(entry);
		}
		super.saveFieldsAndPersist(view);
	}
}