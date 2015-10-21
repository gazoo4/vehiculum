package sk.berops.android.fueller.gui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import sk.berops.android.fueller.R;

public class FragmentEntryEditDeleteActivate extends DialogFragment {

	public interface EntryEditDeleteActivateDialogListener {
		public void OnDialogEditClick(DialogFragment dialog);
		public void OnDialogDeleteClick(DialogFragment dialog);
		public void OnDialogActivateClick(DialogFragment dialog);
	}
	
	EntryEditDeleteActivateDialogListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (EntryEditDeleteActivateDialogListener) activity;
		} catch (ClassCastException e) {
			System.out.println("Class doesn't implement EntryEditDeleteActivateDialogListener");
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
