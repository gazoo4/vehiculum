package sk.berops.android.fueller.engine.charts;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.report.ActivityReportsNavigate;

/**
 * Created by bernard.halas on 12/11/2015.
 * <p/>
 * This class is meant to provide data for the expense distribution pie charts based on the expected source scope or filter
 */
public class ExpenseDistributionPieChart {

    private Consumption consumption;

    public ExpenseDistributionPieChart() {
        consumption = MainActivity.garage.getActiveCar().getHistory().getEntries().getLast().getConsumption();
        consumption.reloadChartData();
    }

    private PieDataSet getPieDataSet() {
        if (consumption == null) {
            return null;
        }
        PieDataSet dataSet = new PieDataSet(consumption.getPieChartVals(), MainActivity.garage.getActiveCar().getNickname());
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(3);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextSize(ActivityReportsNavigate.TEXT_SIZE);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setColors(consumption.getPieChartColors());

        return dataSet;
    }

    /**
     * Method to help to provide the legend for the chart
     *
     * @return Expense types
     */
    private ArrayList<String> getLegend() {
        if (consumption == null) {
            return null;
        }

        return consumption.getPieChartLegend();
    }

    public PieData getPieData() {
        return new PieData(getLegend(), getPieDataSet());
    }
}