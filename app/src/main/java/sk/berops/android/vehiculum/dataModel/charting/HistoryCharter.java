package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.Vehiculum;
import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.History;
import sk.berops.android.vehiculum.engine.calculation.NewGenConsumption;
import sk.berops.android.vehiculum.gui.common.TextFormatter;

/**
 * @author Bernard Halas
 * @date 8/31/17
 */

public class HistoryCharter extends Charter {
	private History history;
	private HashMap<Entry.ExpenseType, Entry> lastEntries;
	private ArrayList<PieEntry> vals;
	private ArrayList<Integer> colors;

	public HistoryCharter(History history) {
		this.history = history;
		loadLastEntries();
	}

	private void loadLastEntries() {
		lastEntries = new HashMap<>();
		for (Entry e: history.getEntries()) {
			lastEntries.put(e.getExpenseType(), e);
		}

		vals = new ArrayList<>();
		colors = new ArrayList<>();
		for (Entry.ExpenseType t: lastEntries.keySet()) {
			NewGenConsumption c = lastEntries.get(t).getConsumption();
			vals.add(new PieEntry(c.getTotalTypeCost().getPreferredValue().floatValue()));
			colors.add(t.getColor());
		}
	}

	@Override
	public ArrayList<PieEntry> generatePieChartVals() {
		if (vals == null) {
			loadLastEntries();
		}

		return vals;
	}

	@Override
	public ArrayList<Integer> generatePieChartColors() {
		if (colors == null) {
			loadLastEntries();
		}

		return colors;
	}

	@Override
	public String generatePieChartLabel() {
		String total = Vehiculum.context.getString(R.string.generic_charts_total);
		double value = history.getEntries().getLast().getConsumption().getTotalCost().getPreferredValue();
		return total + ": " + TextFormatter.format(value, "######.##");
	}
}