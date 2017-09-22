package sk.berops.android.vehiculum.engine.charts;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import sk.berops.android.vehiculum.dataModel.charting.PieChartable;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.report.ActivityReportsNavigate;

/**
 * @author Bernard Halas
 * @date 9/22/17
 * Class responsible for drawing a pie-chart with expense distribution
 */

public class NGExpenseDistributionPieChart {

	private PieChartable chartable;
	/**
	 * Constructor
	 */
	public NGExpenseDistributionPieChart() {
		reload();
	}

	public void reload() {
		chartable = MainActivity.garage.getActiveCar().getHistory();
	}

	public PieData getPieData() {
		return new PieData(getPieDataSet());
	}

	public PieDataSet getPieDataSet() {
		if (chartable == null) return null;

		PieDataSet dataSet = new PieDataSet(chartable.getPieChartVals(), chartable.getPieChartLabel());

		dataSet.setSliceSpace(2);
		dataSet.setSelectionShift(0);
		dataSet.setValueFormatter(new PercentFormatter());
		dataSet.setValueTextSize(ActivityReportsNavigate.TEXT_SIZE);
		dataSet.setValueTextColor(Color.WHITE);
		dataSet.setColors(chartable.getPieChartColors());

		return dataSet;
	}

	public boolean invokeSelection(Integer i) {
		return chartable.invokeSelection(i);
	}
}
