package sk.berops.android.fueller.gui.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.gui.Colors;

public class UtilsActivity {
	public static void styleEditText(EditText editText) {
		if (editText != null) {
			editText.setHintTextColor(Colors.LIGHT_GREEN);
		}
	}
	
	public static void styleSpinner(Spinner spinner, Context context, int arrayID) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(context, arrayID, 
						R.layout.spinner_white);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
