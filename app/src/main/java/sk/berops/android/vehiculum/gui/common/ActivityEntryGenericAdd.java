package sk.berops.android.vehiculum.gui.common;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.tags.Tag;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.tags.FragmentTagManager;
import sk.berops.android.vehiculum.gui.tags.LinearTagAdapter;

public abstract class ActivityEntryGenericAdd extends ActivityExpenseAdd implements DatePickerDialog.OnDateSetListener, FragmentTagManager.TagAttachControlListener {

	protected EditText editTextMileage;
	protected Button buttonDate;
	protected Button buttonTagAdd;
	protected TextView textViewDistanceUnit;
	protected RecyclerView recyclerViewLinearTags;
	protected LinearTagAdapter linearTagAdapter;

	protected Entry entry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.expense = this.entry;
		super.onCreate(savedInstanceState);

		updateTextAddEventDate();
	}

	@Override
	protected void attachGuiObjects() {
		buttonDate = (Button) findViewById(R.id.activity_common_new_record_button_date);
		buttonTagAdd = (Button) findViewById(R.id.activity_common_new_record_button_tag_add);

		listButtons.add(buttonDate);
		listButtons.add(buttonTagAdd);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		initializeTags(R.id.activity_new_event_common_tags_recyclerview);
		textViewDistanceUnit.setText(car.getDistanceUnit().getUnit());

		if (editMode) {
			buttonDate.setText(DateFormat.getDateInstance().format(entry.getEventDate()));
			editTextMileage.setText(Double.valueOf(entry.getMileage()).toString());
			editTextCost.setText(Double.valueOf(entry.getCost().getRecordedValue()).toString());
			editTextComment.setText(entry.getComment());
			spinnerCurrency.setSelection(entry.getCost().getRecordedUnit().getId());
		}
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
		buttonDate.setText(DateFormat.getDateInstance().format(entry.getEventDate()));
	}

	private void updateMileage() throws FieldEmptyException {
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

	@Override
	protected void reloadReferences() {
		expense = entry;
		super.reloadReferences();
	}

	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateMileage();
	}

	public void saveFieldsAndPersist(View view) throws FieldEmptyException {
		Car car = MainActivity.garage.getActiveCar();
		entry.setCar(car);
		if (editMode) {
			// As the object references might get broken (there might be serialization
			// and deserialization of the entry happening), we need to remove the entry by UUID
			// (and other fields used in Entry.equals() and then to add it again to the list below
			car.getHistory().getEntries().remove(entry);
		}
		car.getHistory().getEntries().add(entry);
		super.saveFieldsAndPersist(view);
	}

	/**
	 * Method used to handle button clicks, mainly for saving the records
	 * @param view
	 * @return true if the click has been handled in this method
	 */
	public boolean onClick(View view) {
		switch(view.getId()) {
			case R.id.activity_common_new_record_button_save:
				try {
					saveFieldsAndPersist(view);
					return true;
				} catch (FieldEmptyException ex) {
					ex.throwAlert();
				}
				break;
		}

		return false;
	}

	/*
    ############ FragmentTagManager.OnTagSelectedListener implementation method follows ############
     */

	/**
	 * Method responsible for notifying the linear tag adapter about the tag selection.
	 * @param tag selected
	 */
	public void onTagSelected(Tag tag) {
		String toast = getString(R.string.activity_generic_tag_toast_start) + " " + tag.getName() + " ";

		if (entry.getTags().contains(tag)) {
			toast += getString(R.string.activity_generic_tag_alert_end);
		} else {
			entry.addTag(tag);
			linearTagAdapter.notifyTagAdded(tag);
			toast += getString(R.string.activity_generic_tag_toast_end);
			try {
				MainActivity.dataHandler.saveGarage(MainActivity.garage);
			} catch (IOException ex) {
				Log.e("ERROR", "Tag saving failed: "+ ex.getMessage());
				toast = "Saving failed";
			}
		}

		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method responsible for deleting a tag from the linear tag adapter in the entry modification view
	 * @param tag deleted
	 */
	public void onTagDeleted(Tag tag) {
		linearTagAdapter.notifyTagDeleted(tag);
		try {
			MainActivity.dataHandler.saveGarage(MainActivity.garage);
		} catch (IOException ex) {
			Log.e("ERROR", "Tag deletion failed: "+ ex.getMessage());
			String toast = "Saving failed";
			Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
		}
	}
}