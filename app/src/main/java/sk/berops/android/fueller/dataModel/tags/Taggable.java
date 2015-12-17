package sk.berops.android.fueller.dataModel.tags;

import java.util.ArrayList;

/**
 * Created by Bernard Halas on 11/29/15.
 *
 * This interface allows to attach tags to the class that implements it
 */
public interface Taggable {

	void addTag(Tag tag);
	void removeTag(Tag tag);
	void clearTags();
	ArrayList<Long> getTagIds();
	void setTagIds(ArrayList<Long> tagIds);
	ArrayList<Tag> getTags();
	void setTags(ArrayList<Tag> tags);
}
