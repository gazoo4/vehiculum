package sk.berops.android.fueller.dataModel.tags;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Created by Bernard Halas on 11/29/15.
 *
 * Tag class used for tagging Expenses or other Records
 */
public class Tag extends Record implements Comparable<Tag> {
	/**
	 * Link to the parent tag in the tag tree structure.
	 * No need to persist it, it's dynamically created through initAfterLoad().
	 */
	private Tag parent;

	/**
	 * Links to the child tags in the tag tree structure
	 */
	@ElementList(inline = true, required = false)
	private ArrayList<Tag> children;

	/**
	 * Name of the tag
	 */
	@Element(name = "name", required = true)
	private String name;

	/**
	 * Customized color of the tag
	 */
	@Element(name = "color", required = false)
	private int color;

	/**
	 * Depth of the tag in the tag tree structure. Root tag is always 0.
	 */
	private int depth;

	/**
	 * Constructor
	 */
	public Tag() {
		super();
	}

	/**
	 * Initialize the tag tree and it's elements (tags)
	 */
	public void initAfterLoad() {
		int childDepth = depth + 1;
		for (Tag t : getChildren()) {
			t.setDepth(childDepth);
			t.setParent(this);
			t.initAfterLoad();
		}
	}

	/**
	 * Method to search all known tags by tag ID
	 * @param id of the tag
	 * @return tag
	 */
	public static Tag getTag(UUID id) {
		Tag root = MainActivity.garage.getRootTag();
		return root.searchTreeById(id);
	}

	/**
	 * Recursive search for the tags in the tag tree by tag ID. Start from the current tag.
	 * @param id to base the tag search on.
	 * @return Tag which has the provided ID. If not found, return null.
	 */
	private Tag searchTreeById(UUID id) {
		if (getUuid() == id) {
			return this;
		} else {
			Tag result;
			for (Tag child : getChildren()) {
				if (child == null) continue;
				result = child.searchTreeById(id);
				if (result != null) {
					return result;
				}
			}
			return null;
		}
	}

	/**
	 * Link to the parent tag in the tag tree structure
	 */
	public Tag getParent() {
		return parent;
	}

	/**
	 * Method used to search for grandparents. Height == 1 equals parent; height == 2 equals grandparent
	 * @param height
	 * @return Tag
	 */
	public Tag getParent(int height) {
		if (height == 0) {
			return this;
		} else if (height == 1) {
			return parent;
		} else {
			return parent.getParent(height - 1);
		}
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	/**
	 * Links to the child tags in the tag tree structure
	 */
	public ArrayList<Tag> getChildren() {
		if (children == null) {
			children = new ArrayList<Tag>();
		}
		return children;
	}

	public void setChildren(ArrayList<Tag> children) {
		this.children = children;
	}

	/**
	 * Name of the tag
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Customized color of the tag
	 */
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Depth of the tag in the tag tree structure. Root tag is always 0.
	 */
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Method to browse the tag tree starting from this tag.
	 * @return tag tree as sequentially browsed
	 */
	public ArrayList<Tag> getChildTree() {
		ArrayList<Tag> tree = new ArrayList<Tag>();
		tree.add(this);

		for (Tag child : getChildren()) {
			tree.addAll(child.getChildTree());
		}

		return tree;
	}

	/**
	 * Method to create a new tag under this parent
	 * @return The new tag
	 */
	public Tag createChild() {
		Tag child = new Tag();

		// Child's depth is one more than ours
		child.setDepth(getDepth() + 1);

		// Update the creation date
		child.setCreationDate(Calendar.getInstance().getTime());

		// Initialize the bottom->up links (to parent)
		child.setParent(this);

		// Initialize the top->down links (to children)
		getChildren().add(child);

		return child;
	}

	/**
	 * Method used to generate full tag's path using default separator '>'
	 * @return String containing path
	 */
	public String getPath() {
		return getPath('>');
	}

	/**
	 * Method used to generate full tag's path using custom separator
	 * @param separator
	 * @return
	 */
	public String getPath(char separator) {
		String path = "";
		if (parent != null) {
			path += parent.getPath() + getName() + " ";
		}
		path += separator + " ";
		return path;
	}

	/**
	 * Return all the tags known to our garage.
	 * @return list of known tags
	 */
	public static ArrayList<Tag> getAllTags() {
		Tag root = MainActivity.garage.getRootTag();
		if (root == null) {
			return new ArrayList<Tag>();
		}

		return root.getChildTree();
	}

	@Override
	public int compareTo(Tag another) {
		return this.getName().compareTo(another.getName());
	}
}
