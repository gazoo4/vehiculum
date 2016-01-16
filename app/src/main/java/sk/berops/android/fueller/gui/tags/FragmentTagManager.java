package sk.berops.android.fueller.gui.tags;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Created by Bernard Halas on 11/29/15.
 */
public class FragmentTagManager extends DialogFragment implements TagTreeCallbackListener {

    private Button buttonDelete;
    private Button buttonEdit;
    private TextView textViewTagName;
    private EditText editTextTagName;
    private TextView textViewColor;
    private View layoutSelect;
    private View layoutEdit;

    private RecyclerView tagTreeView;
    private TagTreeAdapter tagTreeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_manager, container, false);
        attachGuiObjects(view);
        initializeGuiObjects();
        return view;
    }

    private void attachGuiObjects(View view) {
        tagTreeView = (RecyclerView) view.findViewById(R.id.fragment_tag_manager_tag_tree_recyclerview);
        buttonDelete = (Button) view.findViewById(R.id.fragment_tag_manager_delete_button);
        buttonEdit = (Button) view.findViewById(R.id.fragment_tag_manager_edit_button);
        textViewTagName = (TextView) view.findViewById(R.id.fragment_tag_manager_tag_name_textview);
        editTextTagName = (EditText) view.findViewById(R.id.fragment_tag_manager_tag_name_edittext);
        textViewColor = (TextView) view.findViewById(R.id.fragment_tag_manager_color_textview);
        layoutSelect = view.findViewById(R.id.fragment_tag_manager_select_layout);
        layoutEdit = view.findViewById(R.id.fragment_tag_manager_edit_layout);
    }

    private void initializeGuiObjects() {
        tagTreeAdapter = new TagTreeAdapter(MainActivity.garage.getRootTag().getChildren(), this);
        tagTreeView.setAdapter(tagTreeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tagTreeView.setLayoutManager(layoutManager);
    }

    public void onTagShortClick(Tag tag) {

    }

    public void onTagLongClick(Tag tag) {

    }

    public void onNewTagClick(Tag parent) {

    }

    public void switchToSelectLayout() {
        layoutSelect.setVisibility(View.VISIBLE);
        layoutEdit.setVisibility(View.GONE);
    }

    public void switchToEditLayout() {
        layoutSelect.setVisibility(View.GONE);
        layoutEdit.setVisibility(View.VISIBLE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_tag_manager_delete_button:
                break;
            case R.id.fragment_tag_manager_edit_button:
                break;
        }
    }
}