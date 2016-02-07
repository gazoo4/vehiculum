package sk.berops.android.fueller.gui.tags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Created by Bernard Halas on 11/29/15.
 */
public class FragmentTagManager extends DialogFragment implements TagTreeCallbackListener {

    private AlertDialog dialog;
    private RecyclerView tagTreeView;
    private TagTreeAdapter tagTreeAdapter;
	private Tag selectedTag;
	private OnTagSelectedListener callback;

	private static final int CREATE_TAG_REQUEST = 0;
    private static final int EDIT_TAG_REQUEST = 1;

	public interface OnTagSelectedListener {
		void onTagSelected(Tag tag);
	}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), getTheme());
        Resources r = getActivity().getResources();
        dialogBuilder.setTitle(r.getString(R.string.fragment_tag_manager_select_tag_hint));

        String positiveText = r.getString(R.string.fragment_tag_manager_select_button);
	    String neutralText = r.getString(R.string.fragment_tag_manager_edit_button);
	    String negativeText = r.getString(R.string.fragment_tag_manager_delete_button);

        dialogBuilder.setPositiveButton(positiveText, null);
	    dialogBuilder.setNeutralButton(neutralText, null);
	    dialogBuilder.setNegativeButton(negativeText, null);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_tag_manager, null);
        dialog = dialogBuilder.create();
        dialog.setView(view);
	    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
		    @Override
		    public void onShow(DialogInterface dialogInterface) {
			    Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
			    positive.setOnClickListener(new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
						if (callback != null) {
							Tag rootTag = MainActivity.garage.getRootTag();
							tagTreeAdapter.removePlaceholder(rootTag, true);
							callback.onTagSelected(selectedTag);
						}
					    dismiss();
				    }
			    });

			    Button neutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
			    neutral.setOnClickListener(new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
					    editTag(selectedTag.getParent(), selectedTag);
				    }
			    });

			    Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
			    negative.setOnClickListener(new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
					    // TODO: Here we need to delete the tag from all the entries where it exist
					    tagTreeAdapter.deleteTag();
				    }
			    });
		    }
	    });
        dialog.show();

        attachGuiObjects(view);
        initializeGuiObjects();
        return dialog;
    }

    private void attachGuiObjects(View view) {
        tagTreeView = (RecyclerView) view.findViewById(R.id.fragment_tag_manager_tag_tree_recyclerview);
    }

    private void initializeGuiObjects() {
        tagTreeAdapter = new TagTreeAdapter(MainActivity.garage.getRootTag(), this);
        tagTreeView.setAdapter(tagTreeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tagTreeView.setLayoutManager(layoutManager);
        showButtons(false);
    }

	public void setCallback(OnTagSelectedListener callback) {
		this.callback = callback;
	}

    protected void showButtons(boolean show) {
	    int visibility = show ? View.VISIBLE : View.GONE;
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(visibility);
	    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(visibility);
	    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(visibility);
    }

    /**
     * This method is used to notify that the tag editing has been successfully completed.
     * From here onwards we need to redraw the list of the tags.
     * @param requestCode Callback request identifier
     * @param resultCode Callback result identifier
     * @param data Intent carrying more result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
	    if (requestCode == CREATE_TAG_REQUEST) {
		    if (resultCode == FragmentTagEditor.EDIT_TAG_SUCCESS) {
			    int size = selectedTag.getChildren().size();
			    Tag last = selectedTag.getChildren().get(size - 1);
			    tagTreeAdapter.notifyTagCreated(last);
		    }
	    } else if (requestCode == EDIT_TAG_REQUEST) {
            if (resultCode == FragmentTagEditor.EDIT_TAG_SUCCESS) {
                tagTreeAdapter.notifyTagUpdated(selectedTag);
            }
        }
    }

    /*
    ##################### TagTreeCallbackListener implementation methods follow ####################
     */

	/**
	 * Method to handle the tag edition.
	 * @param parent Indicate the parent tag to which a child should be created
	 */
	@Override
    public void createTag(Tag parent) {
		editTag(parent, null);
    }

	/**
	 * Call FragmentTagEditor for editing or creating tag
	 * @param parent Parent tog to which the child is being edited
	 * @param tag Child tag being edited. If empty, the child needs to be created
	 */
	@Override
    public void editTag(Tag parent, Tag tag) {
		// For creation we need to store the parent
		// Otherwise we store the child
		selectedTag = tag == null ? parent : tag;
		FragmentTagEditor tagEditor = new FragmentTagEditor();
		tagEditor.setParent(parent);
		tagEditor.setTag(tag);

        // Necessary for a callback from the tagEditor fragment
		if (tag == null) {
			tagEditor.setTargetFragment(this, CREATE_TAG_REQUEST);
		} else {
			tagEditor.setTargetFragment(this, EDIT_TAG_REQUEST);
		}
        tagEditor.show(getFragmentManager(), "tagEditor");
    }

	/**
	 * Method responsible for handling the tag select/deselect events and showing/hiding alert buttons accordingly.
	 * @param tag Tag to be selected. If null, all tags will be deselected and the buttons will be hidden.
	 */
    @Override
    public void toggleSelection(Tag tag) {

        if (tag == null || tag == MainActivity.garage.getRootTag()) {
	        // Placeholder for new tag has been clicked. Don't highlight anything and hide buttons
	        selectedTag = null;
            showButtons(false);
        } else {
	        // New tag has been highlighted. Show buttons.
	        selectedTag = tag;
            showButtons(true);
        }
    }
}