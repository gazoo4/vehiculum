package sk.berops.android.vehiculum.gui.fuelling;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;


/**
 * @author Bernard Halas
 * @date 3/14/17
 */

public class FuelUnitAdapter extends ArrayAdapter<CharSequence> {
	boolean[] hidden;

	public FuelUnitAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull CharSequence[] objects) {
		super(context, resource, objects);
		hidden = new boolean[objects.length];
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = null;

		if (hidden[position]) {
			TextView tv = new TextView(getContext());
			tv.setHeight(0);
			tv.setVisibility(View.GONE);
			v = tv;
		} else {
			v = super.getDropDownView(position, null, parent);
		}

		parent.setVerticalScrollBarEnabled(false);
		return v;
	}

	public void mask(FuellingEntry.FuelType type) {
		if (type == null) {
			type = FuellingEntry.FuelType.GASOLINE;
		}
		UnitConstants.QuantityUnit[] units = UnitConstants.QuantityUnit.values();
		for (int i = 0; i < units.length; i++) {
			// List all the units. If they are of a different substance than the selected fuel, hide them
			hidden[i] = (units[i].getSubstance() != type.getSubstance());
		}

		notifyDataSetChanged();
	}
}
