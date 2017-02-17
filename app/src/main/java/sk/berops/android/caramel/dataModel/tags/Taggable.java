package sk.berops.android.caramel.dataModel.tags;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Bernard Halas on 11/29/15.
 *
 * This interface allows to attach tags to the class that implements it
 */
public interface Taggable {

	void addTag(Tag tag);
	void removeTag(Tag tag);
	void clearTags();
	ArrayList<UUID> getTagUuids();
	void setTagUuids(ArrayList<UUID> tagUuids);
	ArrayList<Tag> getTags();
	void setTags(ArrayList<Tag> tags);
}
