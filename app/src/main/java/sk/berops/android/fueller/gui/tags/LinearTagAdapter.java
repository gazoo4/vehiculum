package sk.berops.android.fueller.gui.tags;

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
public class LinearTagAdapter extends RecyclerView.Adapter<LinearTagAdapter.ViewHolder> {

	private ArrayList<Tag> tags;

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textViewName;

		public ViewHolder(View view) {
			super(view);

			textViewName = (TextView) view.findViewById(R.id.list_tags_name);
		}
	}

	public LinearTagAdapter(ArrayList<Tag> tags) {
		this.tags = tags;
		notifyDataSetChanged();
	}

	@Override
	public LinearTagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.list_tags, parent, false);

		return new LinearTagAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(LinearTagAdapter.ViewHolder holder, int position) {
		Tag tag = tags.get(position);
		holder.textViewName.setText(tag.getName());
	}

	@Override
	public int getItemCount() {
		return tags.size();
	}

	public void notifyTagAdded(Tag tag) {
		tags.add(tag);
		notifyItemInserted(tags.size() - 1);
	}
}