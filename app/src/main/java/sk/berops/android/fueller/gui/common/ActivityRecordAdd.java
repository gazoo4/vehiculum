package sk.berops.android.fueller.gui.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.MainActivity;

public abstract class ActivityRecordAdd extends Activity {

	protected EditText editTextComment;
	
	protected Car car;
	protected Record record;
	protected boolean editMode;

	protected LinkedList<EditText> listEditTexts;
	protected LinkedList<ImageView> listIcons;
	protected HashMap<Integer, Spinner> mapSpinners;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		car = MainActivity.garage.getActiveCar();

		listEditTexts = new LinkedList<>();
		listIcons = new LinkedList<>();
		mapSpinners = new HashMap<>();
		
		attachGuiObjects();
		styleGuiObjects();
		initializeGuiObjects();
	}
	
	public void throwAlertFieldsEmpty(int id) throws FieldsEmptyException {
		throw new FieldsEmptyException(this, id); 
	}
	
	protected abstract void attachGuiObjects();
	
	protected void styleGuiObjects() {
		for (EditText e: listEditTexts) {
			UtilsActivity.styleEditText(e);
		}

		Spinner s;
		for (Integer id: mapSpinners.keySet()) {
			s = mapSpinners.get(id);
			UtilsActivity.styleSpinner(s, this, id);
		}

		for (ImageView i: listIcons) {
			UtilsActivity.tintIcon(i);
		}
	}
	
	protected abstract void initializeGuiObjects();
	
	protected void updateFields() throws FieldsEmptyException {
		updateComment();
		if (record.getCreationDate() == null) {
			updateCreationDate();
		} else {
			updateModifiedDate();
		}
	}
	
	protected void updateComment() {
		if (editTextComment != null) {
			record.setComment(editTextComment.getText().toString());
		}
	}
	
	protected void updateCreationDate() {
		final Calendar c = Calendar.getInstance();
		record.setCreationDate(c.getTime());
	}
	
	protected void updateModifiedDate() {
		final Calendar c = Calendar.getInstance();
		record.setModifiedDate(c.getTime());
	}
	
	public void saveFieldsAndPersist(View view) throws FieldsEmptyException {
		updateFields();
		persistGarage();
	}
	
	public void persistGarage() {
		MainActivity.dataHandler.persistGarage(MainActivity.garage);
		MainActivity.garage.initAfterLoad();
	}

}
