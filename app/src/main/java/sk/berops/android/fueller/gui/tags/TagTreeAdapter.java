package sk.berops.android.fueller.gui.tags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.MainActivity;

/**
 * Created by Bernard Halas on 12/1/15.
 */
public class TagTreeAdapter extends RecyclerView.Adapter<TagTreeAdapter.ViewHolder> {

	private ArrayList<Tag> tags;
	private Tag rootTag;
	private TagTreeCallbackListener callback;

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public TextView textViewName;
		public TextView textViewNewTag;
		public TagTreeHolderClickable listener;

		public ViewHolder(View view, TagTreeHolderClickable listener) {
			super(view);

			textViewName = (TextView) view.findViewById(R.id.list_tree_tags_name);
			textViewNewTag = (TextView) view.findViewById(R.id.list_tree_tags_new_tag);

			this.listener = listener;
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View view) {
			listener.onTagShortClicked(view, this.getLayoutPosition());
		}

		@Override
		public boolean onLongClick(View view) {
			return listener.onTagLongClicked(view, this.getLayoutPosition());
		}

		public static interface TagTreeHolderClickable {
			public void onTagShortClicked(View caller, int position);
			public boolean onTagLongClicked(View caller, int position);
		}
	}

	public class TagTreeHolderListener implements ViewHolder.TagTreeHolderClickable {
		public void onTagShortClicked(View caller, int position) {
			if (tags.get(position) == null) {
				if (position == 0) {
					callback.onNewTagClick(rootTag);
				} else {
					callback.onNewTagClick(tags.get(position - 1));
				}
				System.out.println("Clicked on null on " + position);
			} else {
				callback.onTagShortClick(tags.get(position));
				System.out.println("Clicked on "+ position);
				return;
			}
		}

		public boolean onTagLongClicked(View caller, int position) {
			if (tags.get(position) == null) {
				if (position == 0) {
					callback.onNewTagClick(rootTag);
				} else {
					callback.onNewTagClick(tags.get(position - 1));
				}
				System.out.println("Long-clicked on null on "+ position);
			} else {
				callback.onTagLongClick(tags.get(position));
				System.out.println("Long-clicked on "+ position);
			}
			return true;
		}
	}

	public TagTreeAdapter(ArrayList<Tag> tags, TagTreeCallbackListener callback) {
		if (tags == null) {
			tags = new ArrayList<Tag>();
			// add a placeholder for a new tag
			tags.add(null);
		}
		this.tags = tags;
		this.callback = callback;
		this.rootTag = MainActivity.garage.getRootTag();
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
		// if there's a null slot in the tag list, this is a placeholder for a new tag;
		// in that case, make the new tag offer visible
		if (tag == null) {
			holder.textViewNewTag.setVisibility(View.VISIBLE);
			holder.textViewName.setVisibility(View.GONE);
		} else {
			holder.textViewNewTag.setVisibility(View.GONE);
			holder.textViewName.setText(tag.getName());
			holder.textViewName.setVisibility(View.VISIBLE);
			holder.textViewName.setPadding(tag.getDepth() * 2, 0, 0, 0);
		}
	}

	@Override
	public int getItemCount() {
		return tags.size();
	}
}