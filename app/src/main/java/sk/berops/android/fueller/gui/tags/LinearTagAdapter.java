package sk.berops.android.fueller.gui.tags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.tags.Tag;

/**
 * Adapter responsible for showing the list of attached tags to an entry. Also allows removing the old tags.
 *
 * Created by Bernard Halas on 11/29/15.
 */
public class LinearTagAdapter extends RecyclerView.Adapter<LinearTagAdapter.ViewHolder> {

	private ArrayList<Tag> tags;
	private Tag tag;

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
		public TextView textViewName;
		private Context context;

		public ViewHolder(View view, Context context) {
			super(view);
			this.context = context;
			textViewName = (TextView) view.findViewById(R.id.list_tags_name);

			// Attach listener
			view.setClickable(true);
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View view) {
			tag = tags.get(getLayoutPosition());
			String message = context.getResources().getString(R.string.activity_generic_linear_tag_deletion_alert_1);
			message += " " + tag.getName() + " ";
			message += context.getResources().getString(R.string.activity_generic_linear_tag_deletion_alert_2);

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message);
			builder.setPositiveButton(R.string.fragment_generic_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User confirmed tag removal
					notifyTagDeleted(tag);
				}
			});
			builder.setNegativeButton(R.string.fragment_generic_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
			builder.create();
			builder.show();
		}

		public boolean onLongClick(View view) {
			onClick(view);
			return true;
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

		return new LinearTagAdapter.ViewHolder(view, parent.getContext());
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

	/**
	 * Method processing a notification of a new tag addition
	 * @param tag being added
	 */
	public void notifyTagAdded(Tag tag) {
		tags.add(tag);
		notifyItemInserted(tags.size() - 1);
	}

	/**
	 * Method processing a notification of a tag deletion
	 * @param tag being deleted
	 */
	public void notifyTagDeleted(Tag tag) {
		int position = tags.indexOf(tag);
		if (position != -1) {
			// If the tag is attached to the entry currently being displayed
			tags.remove(position);
			notifyItemRemoved(position);
		}
	}
}