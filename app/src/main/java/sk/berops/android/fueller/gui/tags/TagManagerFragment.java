package sk.berops.android.fueller.gui.tags;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Created by Bernard Halas on 11/29/15.
 */
public class TagManagerFragment extends DialogFragment {

	private Button buttonCommit;
	private Button buttonDelete;
	private Button buttonEdit;
	private TextView textViewTagName;
	private EditText editTextTagName;
	private TextView textViewColor;

	private RecyclerView treeView;
	private TagTreeAdapter tagTreeAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tag_manager, container, false);
		attachGuiObjects(view);
		initializeGuiObjects();
		return view;
	}

	private void attachGuiObjects(View view) {
		treeView = (RecyclerView) view.findViewById(R.id.fragment_tag_manager_tag_tree_recyclerview);
		buttonCommit = (Button) view.findViewById(R.id.fragment_tag_manager_commit_button);
		buttonDelete = (Button) view.findViewById(R.id.fragment_tag_manager_delete_button);
		buttonEdit = (Button) view.findViewById(R.id.fragment_tag_manager_edit_button);
		textViewTagName = (TextView) view.findViewById(R.id.fragment_tag_manager_tag_name_textview);
		editTextTagName = (EditText) view.findViewById(R.id.fragment_tag_manager_tag_name_edittext);
		textViewColor = (TextView) view.findViewById(R.id.fragment_tag_manager_color_textview);
	}

	private void initializeGuiObjects() {
		tagTreeAdapter = new TagTreeAdapter(MainActivity.garage.getRootTag().getChildren());
		treeView.setAdapter(tagTreeAdapter);
		buttonCommit.setVisibility(View.GONE);
		buttonDelete.setVisibility(View.GONE);
		buttonEdit.setVisibility(View.GONE);
		textViewTagName.setVisibility(View.GONE);
		textViewColor.setVisibility(View.GONE);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.fragment_tag_manager_commit_button:
				break;
			case R.id.fragment_tag_manager_delete_button:
				break;
			case R.id.fragment_tag_manager_edit_button:
				break;
		}
	}
}
