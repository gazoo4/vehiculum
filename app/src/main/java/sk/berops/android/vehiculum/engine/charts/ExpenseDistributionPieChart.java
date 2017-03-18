package sk.berops.android.vehiculum.engine.charts;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.report.ActivityReportsNavigate;

/**
 * Created by bernard.halas on 12/11/2015.
 * <p/>
 * This class is meant to provide data for the expense distribution pie charts based on the expected source scope or filter
 */
public class ExpenseDistributionPieChart {

    private int depth;
    private Entry entry;
    private Consumption consumption;

    /**
     * Constructor
     */
    public ExpenseDistributionPieChart() {
	    init();
    }

    /**
     * Method to extract the data for charting.
     * @return compiled data values for the chart
     */
    private PieDataSet getPieDataSet() {
        if (entry == null) {
            return null;
        }
        String label = "";
	    double cost = 0.0;
	    if (depth == 0) {
		    label = "overall: ";
		    cost = consumption.getTotalCost();
	    }
	    label = label + UnitConstants.convertUnitCost(cost);
        PieDataSet dataSet = new PieDataSet(consumption.getPieChartVals(), consumption.getPieChartLabel(depth));
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(0);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextSize(ActivityReportsNavigate.TEXT_SIZE);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setColors(consumption.getPieChartColors());

        return dataSet;
    }

    /**
     * Method for resetting the chart view and to display the expenses at the root level
     */
    public void init() {
        depth = 0;
	    entry = MainActivity.garage.getActiveCar().getHistory().getEntries().getLast();
	    consumption = entry.getConsumption();
	    consumption.reloadChartData(depth);
    }

	public void invokeSelection(Integer i) {
		Entry.ExpenseType type = Entry.ExpenseType.getExpenseType(i);
		entry = MainActivity.garage.getActiveCar().getHistory().getEntriesFiltered(type).getLast();
		consumption = entry.getConsumption();
		consumption.reloadChartData(++depth);
	}

    /**
     * Method for merging the x-values and the legend for pie chart together
     * @return values and the legend for a pie chart
     */
    public PieData getPieData() {
        return new PieData(getPieDataSet());
    }

    public int getDepth() {
        return depth;
    }
}