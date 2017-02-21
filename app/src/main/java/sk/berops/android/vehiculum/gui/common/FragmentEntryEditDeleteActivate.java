package sk.berops.android.vehiculum.gui.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import sk.berops.android.vehiculum.R;

public class FragmentEntryEditDeleteActivate extends DialogFragment {

	public interface EntryEditDeleteActivateDialogListener {
		public void OnDialogEditClick(DialogFragment dialog);
		public void OnDialogDeleteClick(DialogFragment dialog);
		public void OnDialogActivateClick(DialogFragment dialog);
	}
	
	private EntryEditDeleteActivateDialogListener listener;
	public static final String LOG_TAG = "E/D/A fragment";

	@TargetApi(23)
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		onAttachToContext(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			onAttachToContext(activity);
		}
	}

	protected void onAttachToContext(Context context) {
		try {
			listener = (EntryEditDeleteActivateDialogListener) context;
		} catch (ClassCastException e) {
			Log.e(LOG_TAG, ""+ context.toString() + " must implement FragmentEntryEditDeleteActivate.EntryEditDeleteActivateDialogListener");
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setNegativeButton(R.string.fragment_entry_edit_delete_delete_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.fragment_entry_edit_delete_delete_toast, Toast.LENGTH_SHORT)
						.show();
				listener.OnDialogDeleteClick(FragmentEntryEditDeleteActivate.this);
			}
		});
		builder.setNeutralButton(R.string.fragment_entry_edit_delete_edit_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.fragment_entry_edit_delete_edit_toast, Toast.LENGTH_SHORT)
				.show();
				listener.OnDialogEditClick(FragmentEntryEditDeleteActivate.this);
			}
		});
		builder.setPositiveButton(R.string.fragment_entry_edit_delete_activate_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.fragment_entry_edit_delete_activate_toast, Toast.LENGTH_SHORT)
						.show();
				listener.OnDialogActivateClick(FragmentEntryEditDeleteActivate.this);
			}
		});

		return builder.create();
	}
}
