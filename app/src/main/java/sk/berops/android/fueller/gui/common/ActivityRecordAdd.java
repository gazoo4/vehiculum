package sk.berops.android.fueller.gui.common;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.gui.DefaultActivity;
import sk.berops.android.fueller.gui.MainActivity;

public abstract class ActivityRecordAdd extends DefaultActivity {

	protected EditText editTextComment;
	
	protected Car car;
	protected Record record;
	protected boolean editMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		car = MainActivity.garage.getActiveCar();
		super.onCreate(savedInstanceState);
	}
	
	public void throwAlertFieldsEmpty(int id) throws FieldsEmptyException {
		throw new FieldsEmptyException(this, id); 
	}
	
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
