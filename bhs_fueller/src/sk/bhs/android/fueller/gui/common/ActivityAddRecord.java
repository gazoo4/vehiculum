package sk.bhs.android.fueller.gui.common;

import java.util.Calendar;

import sk.bhs.android.fueller.R;
import sk.bhs.android.fueller.dataModel.Record;
import sk.bhs.android.fueller.gui.MainActivity;
import sk.bhs.android.fueller.io.xml.XMLHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public abstract class ActivityAddRecord extends Activity {
	
	protected Record record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		attachGuiObjects();
	}
	
	protected abstract void attachGuiObjects();
	
	protected void updateCreationDate() {
		final Calendar c = Calendar.getInstance();
		record.setCreationDate(c.getTime());
	}
	
	protected void updateModifiedDate() {
		final Calendar c = Calendar.getInstance();
		record.setModifiedDate(c.getTime());
	}
	
	public void throwAlertFieldsEmpty(String field) {
		AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
		alertDialog.setMessage("" + getResources().getString(R.string.activity_entry_add_fields_missing_alert) + " " + field);
		alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "Ok button Clicked ", Toast.LENGTH_LONG).show();
			}
		});
		
		alertDialog.show();
	}
	
	public void saveFieldsAndPersist(View view) {
		if (record.getCreationDate() == null) {
			updateCreationDate();
		} else {
			updateModifiedDate();
		}
		persistGarage();
	}
	
	public void persistGarage() {
		MainActivity.dataHandler.persistGarage(MainActivity.garage);
	}
}
