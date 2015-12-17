package sk.berops.android.fueller.gui.tags;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.tags.Tag;

/**
 * Created by Bernard Halas on 12/1/15.
 */
public class TagTreeAdapter extends RecyclerView.Adapter<TagTreeAdapter.ViewHolder> {

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textViewName;

		public ViewHolder(View view) {
			super(view);

			textViewName = (TextView) view.findViewById(R.id.list_tree_tags_name);
		}
	}

	private ArrayList<Tag> tags;
	public TagTreeAdapter(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public TagTreeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_tree_tags, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TagTreeAdapter.ViewHolder holder, int position) {
		Tag tag = tags.get(position);
		holder.textViewName.setText(tag.getName());
	}

	@Override
	public int getItemCount() {
		return tags.size();
	}
}
