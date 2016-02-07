package sk.berops.android.fueller.gui.common;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Expense;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.tags.FragmentTagManager;
import sk.berops.android.fueller.gui.tags.LinearTagAdapter;

public abstract class ActivityEntryGenericAdd extends ActivityExpenseAdd implements DatePickerDialog.OnDateSetListener, FragmentTagManager.OnTagSelectedListener {

	protected EditText editTextMileage;
	protected TextView textViewDisplayDate;
	protected TextView textViewDistanceUnit;
	protected RecyclerView recyclerViewLinearTags;
	protected LinearTagAdapter linearTagAdapter;

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
	}

	protected void initializeTags(int tagListId) {
		recyclerViewLinearTags = (RecyclerView) findViewById(tagListId);
		recyclerViewLinearTags.setHasFixedSize(false);
		linearTagAdapter = new LinearTagAdapter(entry.getTags());
		recyclerViewLinearTags.setAdapter(linearTagAdapter);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerViewLinearTags.setLayoutManager(layoutManager);
	}

	public void onClickShowDatePickerDialog(View v) {
		FragmentDatePicker fragment = new FragmentDatePicker();
		fragment.show(getFragmentManager(), "datePicker");
	}

	public void onClickTagAdd(View v) {
		FragmentTagManager fragment = new FragmentTagManager();
		fragment.setCallback(this);
		fragment.show(getFragmentManager(), "tagManager");
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



	/*
    ############ FragmentTagManager.OnTagSelectedListener implementation methods follow ############
     */

	public void onTagSelected(Tag tag) {
		String toast = getString(R.string.activity_generic_tag_toast_start) + " " + tag.getName() + " ";

		if (entry.getTags().contains(tag)) {
			toast += getString(R.string.activity_generic_tag_alert_end);
		} else {
			entry.addTag(tag);
			linearTagAdapter.notifyTagAdded(tag);
			toast += getString(R.string.activity_generic_tag_toast_end);
		}

		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}
}