package sk.berops.android.fueller.gui.common;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Expense;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.tags.TagAdapter;

public abstract class ActivityEntryGenericAdd extends ActivityExpenseAdd implements DatePickerDialog.OnDateSetListener {

	protected EditText editTextMileage;
	protected TextView textViewDisplayDate;
	protected TextView textViewDistanceUnit;
	protected Button buttonAddTag;
	protected RecyclerView recyclerViewTags;
	protected TagAdapter tagAdapter;
	
	protected Entry entry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.expense = (Expense) this.entry;
		super.onCreate(savedInstanceState);
		
		updateTextAddEventDate();
	}
	
	@Override
	protected abstract void attachGuiObjects();
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		editTextMileage.setHintTextColor(Colors.LIGHT_GREEN);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		textViewDistanceUnit.setText(car.getDistanceUnit().getUnit());
		tagAdapter = new TagAdapter(entry.getTags());
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
	
	private void updateMileage() throws FieldsEmptyException {
		Car car = MainActivity.garage.getActiveCar();
		double mileage = 0;
		try {
			mileage = Double.parseDouble(editTextMileage.getText().toString());
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_generic_mileage_hint);
		}
		
		entry.setMileage(mileage);
		car.setCurrentMileage(mileage);
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		entry.setEventDate(c.getTime());
		updateTextAddEventDate();
	}
	
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateMileage();
	}
	
	public void saveFieldsAndPersist(View view) throws FieldsEmptyException {
		Car car = MainActivity.garage.getActiveCar();
		entry.setCar(car);
		if (!editMode) {
			car.getHistory().getEntries().add(entry);
		}
		super.saveFieldsAndPersist(view);
	}
}