package sk.berops.android.caramel.gui.tags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import java.util.Calendar;

import sk.berops.android.caramel.R;
import sk.berops.android.caramel.dataModel.tags.Tag;

/**
 * Created by Bernard Halas on 1/16/16.
 */
public class FragmentTagEditor extends DialogFragment {

	private Tag tag;
	private Tag parent;
	private Resources r;

	private TextView textViewTagPath;
	private EditText editTextTagName;
	private AlertDialog dialog;

	private LobsterPicker colorPicker;
	private LobsterShadeSlider shadeSlider;

	public static final int EDIT_TAG_SUCCESS = 0;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), getTheme());
		r = getActivity().getResources();
		dialogBuilder.setTitle(r.getString(R.string.fragment_tag_editor_modify_tag_hint));

		String positiveText = r.getString(R.string.fragment_tag_editor_save_button);
		dialogBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
			/**
			 * This method will be invoked when a button in the dialog is clicked.
			 *
			 * @param dialog The dialog that received the click.
			 * @param which The button that was clicked (e.g.
			 *            {@link DialogInterface#BUTTON1}) or the position
			 *            of the item clicked.
			 */
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which != DialogInterface.BUTTON_POSITIVE) {
					// If we didn't click save, we shouldn't save anything then
					return;
				}

				// Field extraction from the fragment follows
				String name = editTextTagName.getText().toString();
				name.trim();

				if (name.equals("")) {
					// Throw alert that the tag name cannot be empty and exit from the tag saving process
					// TODO: add name text listener. If empty, hide the button "Save" and this AlertDialog can be dropped
					new AlertDialog.Builder(getActivity())
							.setTitle(r.getString(R.string.fragment_generic_sorry))
							.setMessage(r.getString(R.string.fragment_tag_editor_name_required))
							.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
					return;
				}

				if (tag == null) {
					// Clear the placeholder
					parent.getChildren().remove(null);
					// Tag creation
					tag = parent.createChild();
				} else {
					// If this is a tag update, then reflect that in the Tag modified date
					tag.setModifiedDate(Calendar.getInstance().getTime());
				}

				// Fields update follows
				tag.setName(name);
				tag.setColor(colorPicker.getColor());

				// Here we notify the calling fragment that we've modified the tag successfully
				// and that the calling fragment needs to re-draw the list of the tags
				getTargetFragment().onActivityResult(getTargetRequestCode(), EDIT_TAG_SUCCESS, null);
			}
		});

		dialog = dialogBuilder.create();

		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_tag_editor, null);
		dialog.setView(view);
		dialog.show();

		attachGuiObjects(view);
		initializeGuiObjects();
		return dialog;
	}

	private void attachGuiObjects(View view) {
		textViewTagPath = (TextView) view.findViewById(R.id.fragment_tag_editor_tag_path);
		editTextTagName = (EditText) view.findViewById(R.id.fragment_tag_editor_tag_name);
		colorPicker = (LobsterPicker) view.findViewById(R.id.fragment_tag_editor_color_picker);
		shadeSlider = (LobsterShadeSlider) view.findViewById(R.id.fragment_tag_editor_shade_slider);
	}

	private void initializeGuiObjects() {
		textViewTagPath.setText(parent.getPath());
		textViewTagPath.setSelected(true);
		colorPicker.addDecorator(shadeSlider);

		if (tag == null) {
			dialog.setTitle(r.getString(R.string.fragment_tag_editor_create_tag_hint));
		} else {
			// Initializing the tag specific fields makes sense only if we're editing tag (not in the scope of a tag creation)
			editTextTagName.setText(tag.getName());
			colorPicker.setColor(tag.getColor());
		}
	}

	protected void setTag(Tag tag) {
		this.tag = tag;
	}

	protected void setParent(Tag parent) {
		this.parent = parent;
	}
}