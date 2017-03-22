package sk.berops.android.vehiculum.gui.maintenance;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;

public class PartsAdapter extends ArrayAdapter<ReplacementPart> {

	private LinkedList<ReplacementPart> parts;
	private final Context context;
	private Preferences preferences = Preferences.getInstance();
	
	public PartsAdapter(Context context, LinkedList<ReplacementPart> parts) {
		super(context, R.layout.list_maintenance_parts, parts);
		this.context = context;
		this.parts = parts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReplacementPart part = parts.get(parts.size() - 1 - position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//TODO: listen to the warning below for smoother scrolling
		View rowView = inflater.inflate(R.layout.list_maintenance_parts, parent, false);
		
		TextView textViewID = (TextView) rowView.findViewById(R.id.list_maintenance_parts_part_id);
		TextView textViewComment = (TextView) rowView.findViewById(R.id.list_maintenance_parts_comment);
		TextView textViewQuantity = (TextView) rowView.findViewById(R.id.list_maintenance_parts_quantity);
		TextView textViewPrice = (TextView) rowView.findViewById(R.id.list_maintenance_parts_price);
		TextView textViewCurrency = (TextView) rowView.findViewById(R.id.list_maintenance_parts_currency);

		if (Build.VERSION.SDK_INT < 23) {
			TextViewCompat.setTextAppearance(textViewID, R.style.plain_text);
			TextViewCompat.setTextAppearance(textViewComment, R.style.plain_text);
			TextViewCompat.setTextAppearance(textViewQuantity, R.style.plain_text);
			TextViewCompat.setTextAppearance(textViewPrice, R.style.plain_text);
			TextViewCompat.setTextAppearance(textViewCurrency, R.style.plain_text);
		} else {
			textViewID.setTextAppearance(R.style.plain_text);
			textViewComment.setTextAppearance(R.style.plain_text);
			textViewQuantity.setTextAppearance(R.style.plain_text);
			textViewPrice.setTextAppearance(R.style.plain_text);
			textViewCurrency.setTextAppearance(R.style.plain_text);
		}
		
		textViewID.setText(part.getProducerPartID());
		textViewComment.setText(part.getComment());
		textViewPrice.setText(Double.toString(part.getCost()));
		textViewQuantity.setText(Integer.toString(part.getQuantity()) + "x");
		textViewCurrency.setText(part.getCurrency().getUnitIsoCode());
		
		return rowView;
	}
}