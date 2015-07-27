package sk.berops.android.fueller.gui.report;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.LinkedList;

import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.UnitConstants.ConsumptionUnit;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TextFormatter;
import sk.berops.android.fueller.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntriesReportAdapter extends ArrayAdapter<Entry> {

	private LinkedList<Entry> entries;
	private final Context context;
	private Preferences preferences = Preferences.getInstance();

	public EntriesReportAdapter(Context context, LinkedList<Entry> entries) {
		super(context, R.layout.list_stats_fuelling, entries);
		this.context = context;
		this.entries = entries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entry entry = entries.get(entries.size() - 1 - position);

		switch (entry.getExpenseType()) {
		case FEE:
			return null;
		case FUEL:
			return getViewFuellingEntry((FuellingEntry) entry, convertView, parent);
		case MAINTENANCE:
			return getViewMaintenanceEntry((MaintenanceEntry) entry, convertView, parent);
		case SERVICE:
			return null;
		case TYRES:
			return null;
		default:
			return null;
		}
	}

	private View getViewFuellingEntry(FuellingEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO: listen to the warning below for smoother scrolling
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

		consumption.setShadowLayer(15, 0, 0, Color.WHITE);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));

		totalPrice.setText(TextFormatter.format(entry.getCost(), "####0.00"));
		totalPriceUnit.setText(entry.getCurrency().getUnit());

		unitPrice.setText(TextFormatter.format(entry.getFuelPrice(), "####0.000"));
		unitPriceUnit.setText(preferences.getCurrency().getUnit() + "/"
				+ preferences.getVolumeUnit().getUnit());

		volume.setText(TextFormatter.format(entry.getFuelVolume(), "###0.00"));
		volumeUnit.setText(preferences.getVolumeUnit().getUnit() + "s");

		fuel.setText(entry.getFuelType().toString());

		consumption.setText(TextFormatter.format(entry.getFuelConsumption().getAverageSinceLast(), "##0.00"));
		consumptionUnit.setText(preferences.getConsumptionUnit().getUnit());

		fuel.setTextColor(entry.getFuelType().getColor());

		FuelConsumption c = entry.getFuelConsumption();
		double avgConsumption = c.getAveragePerFuelType().get(entry.getFuelType());
		double lastConsumption = c.getAverageSinceLast();
		double relativeChange = (lastConsumption / avgConsumption - 0.8) / 0.4;
		consumption.setTextColor(GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange));

		return rowView;
	}

	private View getViewMaintenanceEntry(MaintenanceEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO: listen to the warning below for smoother scrolling
		View rowView = inflater.inflate(R.layout.list_stats_maintenance, parent, false);

		TextView dynamicId = (TextView) rowView.findViewById(R.id.list_stats_maintenance_dynamic_id);
		TextView mileage = (TextView) rowView.findViewById(R.id.list_stats_maintenance_mileage);
		TextView date = (TextView) rowView.findViewById(R.id.list_stats_maintenance_date);
		TextView partsCost = (TextView) rowView.findViewById(R.id.list_stats_maintenance_parts_cost);
		TextView partsCostUnit = (TextView) rowView.findViewById(R.id.list_stats_maintenance_parts_cost_unit);
		TextView totalCost = (TextView) rowView.findViewById(R.id.list_stats_maintenance_total_cost);
		TextView totalCostUnit = (TextView) rowView.findViewById(R.id.list_stats_maintenance_total_cost_unit);
		TextView entryType = (TextView) rowView.findViewById(R.id.list_stats_maintenance_entry_type);
		TextView comment = (TextView) rowView.findViewById(R.id.list_stats_maintenance_comment);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));

		double partsCostNative = Currency.convert(entry.getPartsCostSI(), Currency.Unit.getUnit(0),
				preferences.getCurrency(), entry.getEventDate());
		partsCost.setText(TextFormatter.format(partsCostNative, "####0.00"));
		partsCostUnit.setText(preferences.getCurrency().getUnit());

		double totalCostNative = Currency.convert(entry.getCostSI(), Currency.Unit.getUnit(0), preferences.getCurrency(), entry.getEventDate());
		totalCost.setText(TextFormatter.format(totalCostNative, "####0.00"));
		totalCostUnit.setText(preferences.getCurrency().getUnit());
		
		entryType.setText(entry.getExpenseType().getExpenseType());
		comment.setText(entry.getComment());

		return rowView;
	}

}