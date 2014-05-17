package sk.bhs.android.fueller.gui;

import java.text.DateFormat;
import java.util.Calendar;

import sk.bhs.android.fueller.R;
import sk.bhs.android.fueller.dataModel.expense.FuellingEntry;
import sk.bhs.android.fueller.gui.common.FragmentDatePicker;
import sk.bhs.android.fueller.gui.fuelling.ActivityRefuel;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class ActivityEntryAdd extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry_add);
		
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_entry_add_button_refuel:
			startActivity(new Intent(this, ActivityRefuel.class)); //this or getApplicationContext()
			break;
		}
	}
}

