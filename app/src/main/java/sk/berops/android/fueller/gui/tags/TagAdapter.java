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
 * Created by Bernard Halas on 11/29/15.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textViewName;

		public ViewHolder(View view) {
			super(view);

			textViewName = (TextView) view.findViewById(R.id.list_tags_name);
		}
	}

	private ArrayList<Tag> tags;

	public TagAdapter(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_tags, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TagAdapter.ViewHolder holder, int position) {
		Tag tag = tags.get(position);
		holder.textViewName.setText(tag.getName());
	}

	@Override
	public int getItemCount() {
		return tags.size();
	}
}
