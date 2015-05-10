package sk.berops.android.fueller.gui.report;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.LinkedList;

import sk.berops.android.fueller.configuration.UnitSettings;
import sk.berops.android.fueller.configuration.UserSettings;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.TextFormatter;
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
	private UnitSettings settings;

	public FuellingStatsAdapter(Context context, LinkedList<FuellingEntry> entries) {
		super(context, R.layout.list_stats_fuelling, entries);
		this.context = context;
		this.entries = entries;
		this.settings = MainActivity.garage.getSettings().getUnitSettings();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FuellingEntry entry = entries.get(entries.size() - 1 - position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//TODO: listen to the warning below for smoother scrolling
		View rowView = inflater.inflate(R.layout.list_stats_fuelling, parent, false);
		
		TextView dynamicId = (TextView) rowView.findViewById(R.id.list_stats_fuelling_dynamic_id);
		TextView mileage = (TextView) rowView.findViewById(R.id.list_stats_fuelling_mileage);
		TextView date = (TextView) rowView.findViewById(R.id.list_stats_fuelling_date);
		TextView totalPrice = (TextView) rowView.findViewById(R.id.list_stats_fuelling_total_price);
		TextView totalPriceUnit = (TextView) rowView.findViewById(R.id.list_stats_fuelling_total_price_unit);
		TextView unitPrice = (TextView) rowView.findViewById(R.id.list_stats_fuelling_unit_price);
		TextView unitPriceUnit = (TextView) rowView.findViewById(R.id.list_stats_fuelling_unit_price_unit);
		TextView volume = (TextView) rowView.findViewById(R.id.list_stats_fuelling_volume);
		TextView volumeUnit = (TextView) rowView.findViewById(R.id.list_stats_fuelling_volume_unit);
		TextView fuel = (TextView) rowView.findViewById(R.id.list_stats_fuelling_fuel);
		TextView consumption = (TextView) rowView.findViewById(R.id.list_stats_fuelling_consumption);
		TextView consumptionUnit = (TextView) rowView.findViewById(R.id.list_stats_fuelling_consumption_unit);
		
		dynamicId.setTextAppearance(context, R.style.plain_text_small);
		mileage.setTextAppearance(context, R.style.plain_text_small);
		date.setTextAppearance(context, R.style.plain_text_small);
		totalPrice.setTextAppearance(context, R.style.plain_text);
		totalPriceUnit.setTextAppearance(context, R.style.plain_text_small);
		unitPrice.setTextAppearance(context, R.style.plain_text);
		unitPriceUnit.setTextAppearance(context, R.style.plain_text_small);
		volume.setTextAppearance(context, R.style.plain_text);
		volumeUnit.setTextAppearance(context, R.style.plain_text_small);
		fuel.setTextAppearance(context, R.style.plain_text); 
		consumption.setTextAppearance(context, R.style.plain_text_big); 
		consumption.setShadowLayer(15, 0, 0, Color.WHITE);
		consumptionUnit.setTextAppearance(context, R.style.plain_text_small);
		
		dynamicId.setText(Integer.toString(entry.getDynamicId()));
		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") +" "+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());
		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));
		totalPrice.setText(TextFormatter.format(entry.getCost(),"####0.00"));
		totalPriceUnit.setText(settings.getCurrency().getUnit());
		unitPrice.setText(TextFormatter.format(entry.getFuelPrice(), "####0.000"));
		unitPriceUnit.setText(settings.getCurrency().getUnit() +"/"+ settings.getVolumeUnit().getUnit());
		volume.setText(TextFormatter.format(entry.getFuelVolume(), "###0.00"));
		volumeUnit.setText(settings.getVolumeUnit().getUnit());
		fuel.setText(entry.getFuelType().toString());
		fuel.setTextColor(entry.getFuelType().getColor());
		consumption.setText(TextFormatter.format(entry.getFuelConsumption().getAverageSinceLast(),"##0.00"));
		consumptionUnit.setText(settings.getConsumptionUnit().getUnit());
		return rowView;
	}
}