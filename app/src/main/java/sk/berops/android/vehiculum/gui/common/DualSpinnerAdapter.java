package sk.berops.android.vehiculum.gui.common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bernard Halas
 * @date 10/5/17
 */

public class DualSpinnerAdapter extends ArrayAdapter<DualString> {

	public DualSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CharSequence> shorts, List<CharSequence> longs) {
		this(context, resource, DualString.of(shorts, longs));
	}

	public DualSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DualString> objects) {
		super(context, resource, 0, objects);
	}

	@Override
	public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

	}
}
