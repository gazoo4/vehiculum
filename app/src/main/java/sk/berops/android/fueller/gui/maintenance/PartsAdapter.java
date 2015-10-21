package sk.berops.android.fueller.gui.maintenance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;

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
		
		textViewID.setTextAppearance(context, R.style.plain_text);
		textViewComment.setTextAppearance(context, R.style.plain_text);
		textViewQuantity.setTextAppearance(context, R.style.plain_text);
		textViewPrice.setTextAppearance(context, R.style.plain_text);
		textViewCurrency.setTextAppearance(context, R.style.plain_text);
		
		textViewID.setText(part.getProducerPartID());
		textViewComment.setText(part.getComment());
		textViewPrice.setText(Double.toString(part.getCost()));
		textViewQuantity.setText(Integer.toString(part.getQuantity()) + "x");
		textViewCurrency.setText(part.getCurrency().getUnit());
		
		return rowView;
	}
}