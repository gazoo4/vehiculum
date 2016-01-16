package sk.berops.android.fueller.gui.tags;

import sk.berops.android.fueller.dataModel.tags.Tag;

/**
 * Created by bernard.halas on 02/01/2016.
 */
public interface TagTreeCallbackListener {
    public void onTagShortClick(Tag tag);
    public void onTagLongClick(Tag tag);
    public void onNewTagClick(Tag parent);
}
