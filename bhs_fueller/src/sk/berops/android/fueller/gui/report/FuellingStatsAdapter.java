package sk.berops.android.fueller.gui.report;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FuellingStatsAdapter extends ArrayAdapter<FuellingEntry> {

	private LinkedList<FuellingEntry> entries;
	private final Context context;

	public FuellingStatsAdapter(Context context, LinkedList<FuellingEntry> entries) {
		super(context, R.layout.list_stats_fuelling, entries);
		this.context = context;
		this.entries = entries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FuellingEntry entry = entries.get(entries.size() - 1 - position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_stats_fuelling, parent, false);
		
		TextView dynamicId = (TextView) rowView.findViewById(R.id.list_stats_entry_dynamic_id);
		TextView date = (TextView) rowView.findViewById(R.id.list_stats_fuelling_date);
		TextView totalPrice = (TextView) rowView.findViewById(R.id.list_stats_fuelling_total_price);
		TextView unitPrice = (TextView) rowView.findViewById(R.id.list_stats_fuelling_unit_price);
		TextView volume = (TextView) rowView.findViewById(R.id.list_stats_fuelling_volume);
		TextView fuel = (TextView) rowView.findViewById(R.id.list_stats_fuelling_fuel);
		TextView consumption = (TextView) rowView.findViewById(R.id.list_stats_fuelling_consumption);
		
		DecimalFormat df = new DecimalFormat("##0.00#");
		dynamicId.setText(Integer.toString(entry.getDynamicId()));
		date.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(entry.getEventDate()));
		totalPrice.setText(df.format(entry.getCost()));
		unitPrice.setText(df.format(entry.getFuelPrice()));
		volume.setText(df.format(entry.getFuelVolume()));
		fuel.setText(entry.getFuelType().toString());
		fuel.setBackgroundColor(entry.getFuelType().getColor());
		fuel.setTextColor(Color.WHITE);
		consumption.setText(df.format(entry.getConsumptionOld()));
		return rowView;
	}

}