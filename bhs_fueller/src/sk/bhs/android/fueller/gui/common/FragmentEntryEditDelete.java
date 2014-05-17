package sk.bhs.android.fueller.gui.common;

import sk.bhs.android.fueller.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class FragmentEntryEditDelete extends DialogFragment {

	public interface EntryEditDeleteDialogListener {
		public void OnDialogEditClick(DialogFragment dialog);
		public void OnDialogDeleteClick(DialogFragment dialog);
	}
	
	EntryEditDeleteDialogListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (EntryEditDeleteDialogListener) activity;
		} catch (ClassCastException e) {
			System.out.println("Class doesn't implement EntryEditDeleteDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(R.string.fragment_entry_edit_delete_edit_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.fragment_entry_edit_delete_edit_toast, Toast.LENGTH_SHORT)
				.show();
				listener.OnDialogEditClick(FragmentEntryEditDelete.this);
			}
		});
		builder.setNegativeButton(R.string.fragment_entry_edit_delete_delete_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.fragment_entry_edit_delete_delete_toast, Toast.LENGTH_SHORT)
						.show();
				listener.OnDialogDeleteClick(FragmentEntryEditDelete.this);
			}
		});
		return builder.create();
	}
}
