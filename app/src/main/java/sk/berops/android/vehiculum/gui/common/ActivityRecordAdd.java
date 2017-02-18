package sk.berops.android.vehiculum.gui.common;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.gui.DefaultActivity;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.io.xml.GaragePersistException;

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
	
	public void throwAlertFieldsEmpty(int id) throws FieldEmptyException {
		throw new FieldEmptyException(this, id);
	}
	
	protected void updateFields() throws FieldEmptyException {
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
	
	public void saveFieldsAndPersist(View view) throws FieldEmptyException {
		updateFields();
		persistGarage();
	}
	
	public void persistGarage() {
		try {
			MainActivity.dataHandler.persistGarage(this, MainActivity.garage);
		} catch (GaragePersistException e) {
			Log.d("ERROR", "Problem when saving the changes");
		}
		MainActivity.garage.initAfterLoad();
	}
}
