package sk.berops.android.fueller.gui.report;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.LinkedList;

import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.UnitConstants.ConsumptionUnit;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.BurreaucraticEntry;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.InsuranceEntry;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;
import sk.berops.android.fueller.dataModel.expense.ServiceEntry;
import sk.berops.android.fueller.dataModel.expense.TollEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TextFormatter;
import sk.berops.android.fueller.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
	
	private Entry getEntry(int position) {
		int id = entries.size() - 1 - position;
		if (id < 0) {
			return null;
		}
		return entries.get(id);
	}
	
	@Override
	public int getViewTypeCount() {
		return Entry.ExpenseType.COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getEntry(position).getExpenseType().getId();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entry entry = getEntry(position);
		if (entry == null) {
			return convertView;
		}
		
		switch (entry.getExpenseType()) {
		case TOLL:
			return getViewTollEntry((TollEntry) entry, convertView, parent);
		case FUEL:
			return getViewFuellingEntry((FuellingEntry) entry, convertView, parent);
		case MAINTENANCE:
			return getViewMaintenanceEntry((MaintenanceEntry) entry, convertView, parent);
		case SERVICE:
			return getViewServiceEntry((ServiceEntry) entry, convertView, parent);
		case TYRES:
			// return getViewTyreEntry((TyreEntry) entry, convertView, parent);
			break;
		case BURREAUCRATIC:
			return getViewBurreaucraticEntry((BurreaucraticEntry) entry, convertView, parent);
		case INSURANCE:
			return getViewInsuranceEntry((InsuranceEntry) entry, convertView, parent);
		default:
			break;
		}
		
		return convertView;
	}

	private View getViewFuellingEntry(FuellingEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_fuelling, parent, false);
		}

		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_fuelling_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_fuelling_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_fuelling_date);
		TextView totalPrice = (TextView) convertView.findViewById(R.id.list_stats_fuelling_total_price);
		TextView totalPriceUnit = (TextView) convertView.findViewById(R.id.list_stats_fuelling_total_price_unit);
		TextView unitPrice = (TextView) convertView.findViewById(R.id.list_stats_fuelling_unit_price);
		TextView unitPriceUnit = (TextView) convertView.findViewById(R.id.list_stats_fuelling_unit_price_unit);
		TextView volume = (TextView) convertView.findViewById(R.id.list_stats_fuelling_volume);
		TextView volumeUnit = (TextView) convertView.findViewById(R.id.list_stats_fuelling_volume_unit);
		TextView fuel = (TextView) convertView.findViewById(R.id.list_stats_fuelling_fuel);
		TextView consumption = (TextView) convertView.findViewById(R.id.list_stats_fuelling_consumption);
		TextView consumptionUnit = (TextView) convertView.findViewById(R.id.list_stats_fuelling_consumption_unit);

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

		consumptionUnit.setText(preferences.getConsumptionUnit().getUnit());

		FuelConsumption c = entry.getFuelConsumption();
		if (c.getAverageSinceLast() == 0.0) {
			consumption.setText("--.--");
		} else {
			consumption.setText(TextFormatter.format(c.getAverageSinceLast(), "##0.00"));
			double avgConsumption = c.getAveragePerFuelType().get(entry.getFuelType());
			double lastConsumption = c.getAverageSinceLast();
			double relativeChange = (lastConsumption / avgConsumption - 0.8) / 0.4;
			consumption.setTextColor(GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, relativeChange));
		}
		
		fuel.setTextColor(entry.getFuelType().getColor());

		return convertView;
	}

	private View getViewMaintenanceEntry(MaintenanceEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_maintenance, parent, false);
		}

		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_maintenance_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_maintenance_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_maintenance_date);
		TextView partsCost = (TextView) convertView.findViewById(R.id.list_stats_maintenance_parts_cost);
		TextView partsCostUnit = (TextView) convertView.findViewById(R.id.list_stats_maintenance_parts_cost_unit);
		TextView totalCost = (TextView) convertView.findViewById(R.id.list_stats_maintenance_total_cost);
		TextView totalCostUnit = (TextView) convertView.findViewById(R.id.list_stats_maintenance_total_cost_unit);
		TextView entryType = (TextView) convertView.findViewById(R.id.list_stats_maintenance_entry_type);
		TextView comment = (TextView) convertView.findViewById(R.id.list_stats_maintenance_comment);

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

		return convertView;
	}
	
	private View getViewServiceEntry(ServiceEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_service, parent, false);
		}

		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_service_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_service_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_service_date);
		TextView serviceType = (TextView) convertView.findViewById(R.id.list_stats_service_type);
		TextView totalCost = (TextView) convertView.findViewById(R.id.list_stats_service_cost);
		TextView totalCostUnit = (TextView) convertView.findViewById(R.id.list_stats_service_cost_unit);
		TextView entryType = (TextView) convertView.findViewById(R.id.list_stats_service_entry_type);
		TextView comment = (TextView) convertView.findViewById(R.id.list_stats_service_comment);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));
		
		serviceType.setText(entry.getType().getType());
		
		double totalCostNative = Currency.convert(entry.getCostSI(), Currency.Unit.getUnit(0), preferences.getCurrency(), entry.getEventDate());
		totalCost.setText(TextFormatter.format(totalCostNative, "####0.00"));
		totalCostUnit.setText(preferences.getCurrency().getUnit());
		
		entryType.setText(entry.getExpenseType().getExpenseType());
		comment.setText(entry.getComment());
		
		return convertView;
	}
	
	private View getViewInsuranceEntry(InsuranceEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_insurance, parent, false);
		}
		
		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_insurance_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_insurance_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_insurance_date);
		TextView insuranceType = (TextView) convertView.findViewById(R.id.list_stats_insurance_type);
		TextView totalCost = (TextView) convertView.findViewById(R.id.list_stats_insurance_cost);
		TextView totalCostUnit = (TextView) convertView.findViewById(R.id.list_stats_insurance_cost_unit);
		TextView entryType = (TextView) convertView.findViewById(R.id.list_stats_insurance_entry_type);
		TextView comment = (TextView) convertView.findViewById(R.id.list_stats_insurance_comment);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));
		
		insuranceType.setText(entry.getType().getType());
		
		double totalCostNative = Currency.convert(entry.getCostSI(), Currency.Unit.getUnit(0), preferences.getCurrency(), entry.getEventDate());
		totalCost.setText(TextFormatter.format(totalCostNative, "####0.00"));
		totalCostUnit.setText(preferences.getCurrency().getUnit());
		
		entryType.setText(entry.getExpenseType().getExpenseType());
		comment.setText(entry.getComment());
		
		return convertView;
	}
	
	private View getViewTollEntry(TollEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_toll, parent, false);
		}

		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_toll_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_toll_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_toll_date);
		TextView tollType = (TextView) convertView.findViewById(R.id.list_stats_toll_type);
		TextView totalCost = (TextView) convertView.findViewById(R.id.list_stats_toll_cost);
		TextView totalCostUnit = (TextView) convertView.findViewById(R.id.list_stats_toll_cost_unit);
		TextView entryType = (TextView) convertView.findViewById(R.id.list_stats_toll_entry_type);
		TextView comment = (TextView) convertView.findViewById(R.id.list_stats_toll_comment);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));
		
		tollType.setText(entry.getType().getType());
		
		double totalCostNative = Currency.convert(entry.getCostSI(), Currency.Unit.getUnit(0), preferences.getCurrency(), entry.getEventDate());
		totalCost.setText(TextFormatter.format(totalCostNative, "####0.00"));
		totalCostUnit.setText(preferences.getCurrency().getUnit());
		
		entryType.setText(entry.getExpenseType().getExpenseType());
		comment.setText(entry.getComment());
		
		return convertView;
	}
	
	private View getViewBurreaucraticEntry(BurreaucraticEntry entry, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_stats_burreaucratic, parent, false);
		}

		TextView dynamicId = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_dynamic_id);
		TextView mileage = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_mileage);
		TextView date = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_date);
		TextView totalCost = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_cost);
		TextView totalCostUnit = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_cost_unit);
		TextView entryType = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_entry_type);
		TextView comment = (TextView) convertView.findViewById(R.id.list_stats_burreaucratic_comment);

		dynamicId.setText(Integer.toString(entry.getDynamicId()));

		mileage.setText(TextFormatter.format(entry.getMileage(), "#######.#") + " "
				+ MainActivity.garage.getActiveCar().getDistanceUnit().getUnit());

		date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(entry.getEventDate()));
		
		double totalCostNative = Currency.convert(entry.getCostSI(), Currency.Unit.getUnit(0), preferences.getCurrency(), entry.getEventDate());
		totalCost.setText(TextFormatter.format(totalCostNative, "####0.00"));
		totalCostUnit.setText(preferences.getCurrency().getUnit());
		
		entryType.setText(entry.getExpenseType().getExpenseType());
		comment.setText(entry.getComment());
		
		return convertView;
	}

}