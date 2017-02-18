package sk.berops.android.vehiculum.gui.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.gui.Colors;

public class UtilsActivity {
	private static final int DRAWABLE_COLOR = 0xFFFFFFFF;
	private static final int EDITTEXT_COLOR = Colors.LIGHT_GREEN;

	public static void styleButton(Button button) {
		if (button == null) return;
		if (button.getCompoundDrawables()[0] == null) return;
		
		button.getCompoundDrawables()[0].setTint(DRAWABLE_COLOR);
	}

	public static void styleEditText(EditText editText) {
		if (editText == null) return;

		editText.setHintTextColor(EDITTEXT_COLOR);
	}
	
	public static void styleSpinner(Spinner spinner, Context context, int arrayID) {
		if (spinner == null || context == null) return;

		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(context, arrayID, 
						R.layout.spinner_white);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	public static void tintIcon(ImageView icon) {
		if (icon == null) return;

		icon.setColorFilter(DRAWABLE_COLOR);
	}
}
