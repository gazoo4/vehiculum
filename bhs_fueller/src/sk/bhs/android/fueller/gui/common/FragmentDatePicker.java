package sk.bhs.android.fueller.gui.common;

import java.util.Calendar;

import sk.bhs.android.fueller.dataModel.expense.Entry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.widget.DatePicker;

public class FragmentDatePicker extends DialogFragment {
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        // Create a new instance of DatePickerDialog and return it
        OnDateSetListener callback = (OnDateSetListener) getActivity();
		return new DatePickerDialog(getActivity(), callback, year, month, day);
    }
}
