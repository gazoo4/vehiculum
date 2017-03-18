package sk.berops.android.vehiculum.engine.charts;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.History;
import sk.berops.android.vehiculum.gui.MainActivity;

/**
 * Created by bernard.halas on 25/10/2015.
 * <p/>
 * Class responsible for generating and managing the pie chart that's showing the distribution rate among expense categories
 */
public class ConsumptionDistributionPieChart {

    /**
     * Method to collect the values for the pie chart
     *
     * @return Costs per expense type
     */
    public static PieDataSet getPieDataSet() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        History history = MainActivity.garage.getActiveCar().getHistory();
        Consumption consumption = history.getEntries().getLast().getConsumption();
        for (sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType t : sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType.values()) {
            if (consumption.getTotalCostPerEntryType().get(t) == null) {
                continue;
            }
            double value = consumption.getTotalCostPerEntryType().get(t);
            com.github.mikephil.charting.data.PieEntry entry = new com.github.mikephil.charting.data.PieEntry(
                    (float) value, t.getId());
            entries.add(entry);
            colors.add(t.getColor());
        }

        PieDataSet dataset = new PieDataSet(entries, "Expense Distribution");

        dataset.setColors(colors);
        return dataset;
    }
}
