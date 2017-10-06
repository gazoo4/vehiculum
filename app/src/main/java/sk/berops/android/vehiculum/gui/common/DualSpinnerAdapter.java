package sk.berops.android.vehiculum.gui.common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.function.Function;


/**
 * @author Bernard Halas
 * @date 10/5/17
 */

public class DualSpinnerAdapter extends ArrayAdapter<DualString> {

	private LayoutInflater mInflater;
	private Context mContext;

	/**
	 * The resource indicating what views to inflate to display the content of this
	 * array adapter.
	 */
	private final int mResource;

	/**
	 * The resource indicating what views to inflate to display the content of this
	 * array adapter in a drop down widget.
	 */
	private int mDropDownResource;

	/**
	 * Contains the list of objects that represent the data of this ArrayAdapter.
	 * The content of this list is referred to as "the array" in the documentation.
	 */
	private List<DualString> mObjects;

	public DualSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CharSequence> shorts, List<CharSequence> longs) {
		this(context, resource, DualString.of(shorts, longs));
	}

	public DualSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DualString> objects) {
		super(context, resource, 0, objects);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mResource = mDropDownResource = resource;
	}

	@Override
	public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		return createViewFromResource(mInflater, position, convertView, parent, mResource, s -> s.getCollapsed() );
	}

	private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
	                                             @Nullable View convertView, @NonNull ViewGroup parent,
	                                             int resource, Function<DualString, CharSequence> extractString) {
		final View view;
		final TextView text;

		if (convertView == null) {
			view = inflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			text = (TextView) view;
		} catch (ClassCastException e) {
			Log.e(this.getClass().toString(), "You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					this.getClass().toString() +" requires the resource ID to be a TextView", e);
		}

		final DualString item = getItem(position);
		text.setText(extractString.apply(item));

		return view;
	}

	/**
	 * <p>Sets the layout resource to create the drop down views.</p>
	 *
	 * @param resource the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	public void setDropDownViewResource(@LayoutRes int resource) {
		mDropDownResource = resource;
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		return createViewFromResource(mInflater, position, convertView, parent, mDropDownResource, s -> s.getExpanded());
	}
}
