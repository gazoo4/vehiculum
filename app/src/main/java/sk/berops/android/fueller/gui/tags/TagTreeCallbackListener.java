package sk.berops.android.fueller.gui.tags;

import sk.berops.android.fueller.dataModel.tags.Tag;

/**
 * Created by bernard.halas on 02/01/2016.
 */
public interface TagTreeCallbackListener {
	/**
	 * Method to handle the tag edition.
	 * @param parent Indicate the parent tag to which a child should be created
	 */
	void createTag(Tag parent);

	/**
	 * Method to handle the tag edition or creation.
	 * @param parent Parent tog to which the child is being edited
	 * @param tag Child tag being edited. If empty, the child needs to be created
	 */
	void editTag(Tag parent, Tag tag);

	/**
	 * Method responsible for handling the tag select/deselect events and showing/hiding alert buttons accordingly.
	 * @param tag Tag to be selected. If null, all tags will be deselected and the buttons will be hidden.
	 */
	void toggleSelection(Tag tag);
}