package sk.berops.android.fueller.gui.tags;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Class responsible for maintaining a tag tree view on the tag structure. Allowing modification
 * of the whole tag structure and invokes FragmentTagEditor for creating and editing the tags.
 *
 * Created by Bernard Halas on 12/1/15.
 */
public class TagTreeAdapter extends RecyclerView.Adapter<TagTreeAdapter.ViewHolder> {

	private ArrayList<Tag> tags;
	private Tag rootTag;
	private Tag highlightedTag;
	private TagTreeCallbackListener callback;

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public TextView tabulator;
		public TextView textViewName;
		public TextView textViewNewTag;
		public View colorCircle;
		public View divider;
		public TagTreeHolderClickable listener;

		public ViewHolder(View view, TagTreeHolderClickable listener) {
			super(view);

			// Attach the viewholder's objects
			tabulator = (TextView) view.findViewById(R.id.list_tree_tags_tabulator);
			textViewName = (TextView) view.findViewById(R.id.list_tree_tags_name);
			textViewNewTag = (TextView) view.findViewById(R.id.list_tree_tags_new_tag);
			colorCircle = view.findViewById(R.id.list_tree_tags_color_circle);
			divider = view.findViewById(R.id.list_tree_tags_divider);

			// Attach listeners
			this.listener = listener;
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
			view.setClickable(true);
		}

		@Override
		public void onClick(View view) {
			if (tags.get(this.getLayoutPosition()) == null) {
				listener.onPlaceholderShortClicked(view, this.getLayoutPosition());
			} else {
				listener.onTagShortClicked(view, this.getLayoutPosition());
			}
		}

		@Override
		public boolean onLongClick(View view) {
			return listener.onTagLongClicked(view, this.getLayoutPosition());
		}
	}

	public interface TagTreeHolderClickable {
		void onPlaceholderShortClicked(View caller, int position);
		void onTagShortClicked(View caller, int position);
		boolean onTagLongClicked(View caller, int position);
	}

	public class TagTreeHolderListener implements TagTreeHolderClickable {

		public void onPlaceholderShortClicked(View caller, int position) {
			Tag parent = getPlaceholdersParent(rootTag);

			// Ordinary tag creation request
			callback.createTag(parent);
		}

		/**
		 * Method to callback the FragmentTagManager with the request to create a new tag or edit the existing one.
		 * @param caller Calling view
		 * @param position Position of the item in the recycle view
		 */
		public void onTagShortClicked(View caller, int position) {
			checkDiscrepancy("on tag short clicked start");
			// Ask for a redraw of the old highlightedTag (as whatever that was, it'll change now)
			notifyItemChanged(tags.indexOf(highlightedTag));
			Tag tag = tags.get(position);

			if (tag == highlightedTag) {
				// If we click on the same (highlighted) tag twice, collapse it and expand it's parent
				collapseTree(tag);
				createPlaceholder(tag.getParent());
			} else {
				moveFocus(highlightedTag, tag);
			}

			// Ensure that the now selected tag is properly re-drawn as it has changed it's state
			notifyItemChanged(tags.indexOf(highlightedTag));

			// Let the FragmentTagManager know which tag has been selected
			callback.toggleSelection(highlightedTag);
			checkDiscrepancy("on tag short clicked start");
		}

		/**
		 * Method to callback the FragmentTagManager with the request to to edit the existing tag.
		 * @param caller Calling view
		 * @param position Position of the item in the recycle view
		 * @return True if the call was handled here. False if otherwise.
		 */
		public boolean onTagLongClicked(View caller, int position) {
			if (tags.get(position) == null) return true;
			notifyItemChanged(tags.indexOf(highlightedTag));

			// We've clicked on an existing tag and we want to edit it.
			// Call the editTag through the callback.
			Tag tag = tags.get(position);
			callback.editTag(tag.getParent(), tag);

			return true;
		}
	}

	/**
	 * Constructor
	 * @param rootTag
	 * @param callback
	 */
	public TagTreeAdapter(Tag rootTag, TagTreeCallbackListener callback) {
		this.callback = callback;
		this.rootTag = rootTag;
		this.tags = new ArrayList<>();

		setHighlightedTag(rootTag);
		initializeTags();
	}

	@Override
	public TagTreeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.list_tree_tags, parent, false);

		return new TagTreeAdapter.ViewHolder(view, new TagTreeHolderListener());
	}

	@Override
	public void onBindViewHolder(TagTreeAdapter.ViewHolder holder, int position) {
		Tag tag = tags.get(position);
		String tabulator;

		// Mark the item as selected it needs to be highlighted
		holder.itemView.setSelected(highlightedTag != null && highlightedTag == tags.get(position));

		// Deal with the tabulator
		// If the tag has any children, indicate that with '>' or 'v' (if the tag is expanded)
		if (tag == null) {
			// Before the "create new tag" we want to have '+'
			tabulator = "+";
		} else if (tag.getChildren().size() == 0) {
			// If the existing tag doesn't have children, we want to use '+'
			tabulator = " ";
		} else {
			if (isExpanded(tag)) {
				// The tag is expanded. Show tabulator as 'v'
				tabulator = "v";
			} else {
				// The tag is collapsed. Show tabulator as '>'
				tabulator = ">";
			}
		}
		holder.tabulator.setText(tabulator);

		int paddingDepth;
		if (tag == null) {
			// Deal with the placeholder
			paddingDepth = getPlaceholdersParent(rootTag).getDepth() + 1;
		} else {
			paddingDepth = tag.getDepth();
		}
		holder.tabulator.setPadding(--paddingDepth * 25, 0, 10, 0);

		// Deal with the displayed name of the tag
		// If there's a null slot in the tag list, this is a placeholder for a new tag;
		// In that case, make the new tag offer visible
		if (tag == null) {
			holder.textViewNewTag.setVisibility(View.VISIBLE);
			holder.textViewName.setVisibility(View.GONE);
		} else {
			holder.textViewNewTag.setVisibility(View.GONE);
			holder.textViewName.setText(tag.getName());
			holder.textViewName.setVisibility(View.VISIBLE);
		}

		// Fill in the color circle
		if (tag != null) {
			holder.colorCircle.setVisibility(View.VISIBLE);
			((GradientDrawable) holder.colorCircle.getBackground()).setColor(tag.getColor());
		} else {
			holder.colorCircle.setVisibility(View.INVISIBLE);
		}

		// Deal with the divider
		if (position == tags.size() - 1) {
			holder.divider.setVisibility(View.GONE);
		} else {
			holder.divider.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return tags.size();
	}

	/**
	 * Used for the initial tag display
	 */
	protected void initializeTags() {
		tags.clear();
		removePlaceholder(false);
		highlightedTag = rootTag;
		tags.addAll(rootTag.getChildren());
		checkDiscrepancy("initializeTags");
		this.notifyDataSetChanged();
		createPlaceholder(rootTag);
	}

	/**
	 * Create a placeholder for a new tag. If a placeholder already exists, link it under the suggested parent tag
	 * @param parent Parent tag for the intended placeholder
	 */
	private void createPlaceholder(Tag parent) {
		checkDiscrepancy("createPlaceholder start");
		removePlaceholder(parent, false);

		int parentPosition = parent == rootTag ? -1 : tags.indexOf(parent);
		int childrenCount = parent.getChildren().size();

		// Now attach the placeholder to the parent
		parent.getChildren().add(null);

		// Populate the list
		tags.add(parentPosition + childrenCount + 1, null);

		// Notify the adapter
		notifyItemInserted(parentPosition + childrenCount + 1);
		notifyItemChanged(parentPosition);
		setHighlightedTag(parent);
		checkDiscrepancy("createPlaceholder end");
	}

	/**
	 * Search the whole tag tree for a possible placeholder and remove it
	 * @param updateTagList should the placeholder be removed from the visual tag list as well?
	 * @return true if placeholder was contained
	 */
	protected boolean removePlaceholder(boolean updateTagList) {
		return removePlaceholder(rootTag, updateTagList);
	}

	/**
	 * * Search the parent tag tree for a possible placeholder and remove it
	 * @param parent tag
	 * @param updateTagList should the placeholder be removed from the visual tag list as well?
	 * @return true if placeholder was contained
	 */
	protected boolean removePlaceholder(Tag parent, boolean updateTagList) {
		if (updateTagList) checkDiscrepancy("removePlaceholder start");
		boolean placeholderRemoved = false;
		int parentIndex = tags.indexOf(parent);
		int placeholderIndex;
		while ((placeholderIndex = parent.getChildren().indexOf(null)) != -1) {
			parent.getChildren().remove(placeholderIndex);
			if (updateTagList) {
				tags.remove(parentIndex + 1 + placeholderIndex);
				notifyItemRemoved(parentIndex + 1 + placeholderIndex);
			}
			placeholderRemoved = true;
		}

		for (Tag t : parent.getChildren()) {
			if (removePlaceholder(t, updateTagList)) {
				placeholderRemoved = true;
			}
		}
		if (updateTagList) checkDiscrepancy("removePlaceholder end");
		return placeholderRemoved;
	}

	/**
	 * Method to tell whether the Tag is expanded or not. Meaning whether it is on a path between a highlighted tag and the root tag
	 * @param tag
	 * @return ture if the tag is expanded
	 */
	private boolean isExpanded(Tag tag) {
		// Root tag is implicitly always expanded
		if (tag == rootTag) return true;

		Tag expandedTag = highlightedTag;
		while (expandedTag.getParent() != null) {
			// Until we hit the rootTag
			if (expandedTag == tag) {
				// Check if the questioned tag is on the expanded path
				return true;
			}
			// Move one level higher
			expandedTag = expandedTag.getParent();
		}

		return false;
	}

	/**
	 * Remove all the children of the supplied tag from the recycler view
	 * @param tag
	 */
	private void collapseTree(Tag tag) {
		checkDiscrepancy("collapseTree start");
		if (tag == null || ! isExpanded(tag)) {
			return;
		}

		// If the currently expanded view is several levels beneath, collapse all of the levels
		// one-by-one
		while (highlightedTag != tag) {
			collapseTree(highlightedTag);
		}

		ArrayList<Tag> children = tag.getChildren();
		int tagIndex = tags.indexOf(tag);

		for (int i = tagIndex + children.size() ; i > tagIndex; i--) {
			// Removing the children from the view
			tags.remove(i);
			notifyItemRemoved(i);
		}

		// Clean-up the placeholder
		if (children.contains(null)) {
			children.remove(null);
		}

		// Highlighted tag has been expanded, but now it's not. Move the highlighted tag one level up
		if (tag != rootTag)	setHighlightedTag(tag.getParent());
		checkDiscrepancy("collapseTreeEnd");
	}

	/**
	 * Add all the children of the tag to the view
	 * @param tag
	 */
	private void expandTree(Tag tag) {
		checkDiscrepancy("expandTree start");
		if (tag == null || isExpanded(tag)) {
			return;
		}

		ArrayList<Tag> children = tag.getChildren();
		int parentIndex = tag == rootTag ? -1 : tags.indexOf(tag);

		int childIndex;
		for (int i = 0 ; i < children.size(); i++) {
			// Adding children one-by-one
			childIndex = parentIndex + 1 + i;
			tags.add(childIndex, children.get(i));
			notifyItemInserted(childIndex);
		}

		highlightedTag = tag;
		createPlaceholder(tag);
		checkDiscrepancy("expandTree end");
	}

	/**
	 * Method to search for the placeholder's owner within the selected tag tree
	 * @param tag Being the parent tag of the tree to be searched
	 * @return Placeholder's owner. Returns null if no placeholder is found.
	 */
	private Tag getPlaceholdersParent(Tag tag) {
		Tag parent;
		if (tag == null) {
			tag = rootTag;
		}

		for (Tag t : tag.getChildren()) {
			if (t == null) {
				return tag;
			} else {
				parent = getPlaceholdersParent(t);
				if (parent != null) {
					return parent;
				}
			}
		}

		return null;
	}

	/**
	 * Reflect a successful tag creation in our structures
	 */
	protected void notifyTagCreated(Tag tag) {
		int position = tags.indexOf(null);
		tags.set(position, tag);
		notifyItemChanged(position);

		Tag parent = highlightedTag == null ? rootTag : highlightedTag;
		createPlaceholder(parent);
	}

	protected void notifyTagUpdated(Tag tag) {
		notifyItemChanged(tags.indexOf(tag));

		Tag parent = tag.getParent();
		// Do we need to move the focus?
		if (highlightedTag != parent) moveFocus(highlightedTag, parent);
	}

	/**
	 * Process the tag deletion in the views and also in the list of the provided entries
	 */
	protected void deleteTag(ArrayList<Entry> entries, Tag tag) {
		checkDiscrepancy("delete tag start");

		ArrayList<Tag> children = createChildrenTree(tag);
		if (entries != null) {
			for (Entry e : entries) {
				// Clean-up the entries from the tag being deleted
				e.getTags().remove(tag);
				for (Tag t : children) {
					// Check each entry for each tag from the list
					e.removeTag(t);
				}
			}
		}

		collapseTree(tag);

		int position = tags.indexOf(tag);
		tags.remove(position);
		notifyItemRemoved(position);

		Tag parent = tag.getParent();
		parent.getChildren().remove(tag);
		tag.setParent(null);

		createPlaceholder(parent);
		checkDiscrepancy("delete tag end");
	}

	/**
	 * Method used to collect the Entries which will be affected by a tag deletion
	 * @return list of entries that will be affected
	 */
	protected ArrayList<Entry> getEntriesForTagDeletion(Tag tag) {
		Garage garage = MainActivity.garage;
		ArrayList<Entry> entries = new ArrayList<>();
		ArrayList<Tag> children = createChildrenTree(tag);

		for (Car c : garage.getCars()) {
			for (Entry e : c.getHistory().getEntries()) {
				for (Tag t : e.getTags()) {
					// Scan all the tags in all the entries in all the cars
					if (t != null && children.contains(t)) {
						// To see if they contain the tag being deleted or any of its children
						entries.add(e);
						break;
					}
				}
			}
		}

		return entries;
	}

	private ArrayList<Tag> createChildrenTree(Tag tag) {
		ArrayList<Tag> children = new ArrayList<>();
		children.add(tag);
		Iterator<Tag> i = children.iterator();
		Tag t;
		// Populate the children array with all the children Tags (both direct and indirect)
		while (i.hasNext()) {
			t = i.next();
			if (t.getChildren() != null)  children.addAll(t.getChildren());
		}

		return children;
	}

	private void setHighlightedTag(Tag highlightedTag) {
		this.highlightedTag = highlightedTag;
		notifyItemChanged(tags.indexOf(highlightedTag));
		callback.toggleSelection(highlightedTag);
	}

	private void moveFocus(Tag from, Tag to) {
		checkDiscrepancy("moveFocus start");
		// See the relative level at which we're expanding the new tag
		int depthDiff = from.getDepth() - to.getDepth();

		if (depthDiff > 0) {
			if (isExpanded(to)) {
				// If we're shifting the focus upwards on an already expanded grandparent
				// we don't want to collapse and re-expand the same tag as this would cause
				// blinking. So instead we collapse everything until the tag's direct child
				// and we add a placeholder
				Tag directChild = from.getParent(depthDiff - 1);
				collapseTree(directChild);
				notifyItemChanged(tags.indexOf(directChild));
				createPlaceholder(to);
			} else {
				// We want to shift the focus to a sibling of a grand parent
				// Collapse tags up the tree as far as necessary
				collapseTree(from.getParent(depthDiff));
				// Expand the other selected tag
				expandTree(to);
			}
		} else if (depthDiff == 0) {
			// We've clicked on a sibling
			collapseTree(from);
			expandTree(to);
		} else {
			// We clicked on the child tag. So remove the current placeholder + expand the child.
			removePlaceholder(from, true);
			expandTree(to);
		}
		checkDiscrepancy("moveFocus end");
	}

	/*
    ##################### Useful only for the debugging purposes ####################
     */

	private void checkDiscrepancy(String label) {
		ArrayList<String> serializedTags = serializeTags(rootTag);

		boolean discrepancy = false;
		if (tags.size() != serializedTags.size()) {
			System.out.println("Length mismatch");
			System.out.println(serializedTags.size() + " --- " + tags.size());
			discrepancy = true;
		} else {
			String tagName;
			for (int i = 0; i < tags.size(); i++) {
				tagName = tags.get(i) == null ? "null" : tags.get(i).getName();
				if (! tagName.equals(serializedTags.get(i).substring(3))) {
					discrepancy = true;
				}
			}
		}

		if (discrepancy) {
			printDiscrepancy(serializedTags, label);
		}
	}

	private void printDiscrepancy(ArrayList<String> serializedTags, String label) {
		System.out.println("!!! DISCREPANCY FOUND !!!");
		System.out.println("At label: " + label);

		int size = tags.size() - serializedTags.size();

		String tagName;
		for (int i = 0; i < ((tags.size() + serializedTags.size() - Math.abs(size)) / 2); i++) {
			if (tags.get(i) == null) {
				tagName = "null";
			} else {
				tagName = tags.get(i).getName();
			}
			System.out.println(serializedTags.get(i) + " --- " + tagName);
		}

		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (tags.get(i) == null) {
					tagName = "null";
				} else {
					tagName = tags.get(i).getName();
				}
				System.out.println("[   ] --- " + tagName);
			}
		} else if (size < 0) {
			for (int i = 0; i < Math.abs(size); i++) {
				System.out.println(serializedTags.get(i) + " --- [   ]");
			}
		}

		System.out.println("**************************");
	}

	ArrayList<String> serializeTags(Tag tag) {
		ArrayList<String> list = new ArrayList<>();
		if (tag.getParent() != null && !isExpanded(tag.getParent())) return list;

		if (tag != rootTag) list.add(tag.getDepth() + ": " + tag.getName());

		if (tag == highlightedTag || isExpanded(tag)) {
			for (Tag t : tag.getChildren()) {
				if (t == null) {
					list.add(tag.getDepth() + 1 + ": null");
				} else {
					list.addAll(serializeTags(t));
				}
			}
		}

		return list;
	}
}