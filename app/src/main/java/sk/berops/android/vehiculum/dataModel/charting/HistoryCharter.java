package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.Vehiculum;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.History;
import sk.berops.android.vehiculum.engine.calculation.NewGenConsumption;
import sk.berops.android.vehiculum.gui.common.TextFormatter;

/**
 * @author Bernard Halas
 * @date 8/31/17
 */

public class HistoryCharter extends PieCharter {
	private History history;
	/**
	 * Map holding the latest entry object for each entry type
	 */
	private HashMap<Entry.ExpenseType, Entry> lastEntries;

	/**
	 * Constructor
	 * @param history
	 */
	public HistoryCharter(History history) {
		this.history = history;
	}

	/**
	 * Method responsible for populating {@link HistoryCharter#lastEntries}. The reason we are interested in
	 * the last entries is that for each entry-type this variable holds accumulated consumption data
	 * which is then used for charting.
	 */
	@Override
	public void refreshData() {
		super.refreshData();

		lastEntries = new HashMap<>();
		for (Entry e: history.getEntries()) {
			lastEntries.put(e.getExpenseType(), e);
		}

		for (Entry.ExpenseType t: lastEntries.keySet()) {
			Entry lastEntry = lastEntries.get(t);
			NewGenConsumption c = lastEntry.getConsumption();
			vals.add(new PieEntry(c.getTotalTypeCost().getPreferredValue().floatValue()));
			colors.add(t.getColor());
			relays.add(lastEntry.getPieCharter());
		}

		String total = Vehiculum.context.getString(R.string.generic_charts_total);
		double value = history.getEntries().getLast().getConsumption().getTotalCost().getPreferredValue();
		Currency.Unit unit = history.getEntries().getLast().getConsumption().getTotalCost().getPreferredUnit();

		label = total + ": " + TextFormatter.format(value, "######.##") + " " + unit.getSymbol();
	}
}