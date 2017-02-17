package sk.berops.android.caramel.dataModel.expense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import sk.berops.android.caramel.R;

public class FieldEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -769695813536797169L;

	private int fieldID;
	private Context context;

	public FieldEmptyException(Context context, int fieldID) {
		super();
		this.setFieldID(fieldID);
		this.setContext(context);
	}

	public void throwAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
		alertDialog.setMessage(""
				+ getContext().getResources().getString(R.string.activity_entry_add_field_missing_alert)
				+ " " + getFieldName());
		alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getContext().getApplicationContext(), "Ok button Clicked ", Toast.LENGTH_LONG)
						.show();
			}
		});

		alertDialog.show();
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	private String getFieldName() {
		return getContext().getResources().getString(getFieldID());
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
