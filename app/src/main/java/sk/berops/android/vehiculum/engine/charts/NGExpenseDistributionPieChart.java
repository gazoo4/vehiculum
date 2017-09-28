package sk.berops.android.vehiculum.engine.charts;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.report.ActivityReportsNavigate;

/**
 * @author Bernard Halas
 * @date 9/22/17
 * Class responsible for drawing a pie-chart with expense distribution
 */

public class NGExpenseDistributionPieChart {

	private PieCharter charter;
	/**
	 * Constructor
	 */
	public NGExpenseDistributionPieChart() {
		reload();
	}

	public void reload() {
		charter = MainActivity.garage.getActiveCar().getHistory().getPieCharter();
	}

	public PieData getPieData() {
		return new PieData(getPieDataSet());
	}

	public PieDataSet getPieDataSet() {
		PieDataSet dataSet = new PieDataSet(charter.getPieChartVals(), charter.getPieChartLabel());

		dataSet.setSliceSpace(2);
		dataSet.setSelectionShift(0);
		dataSet.setValueFormatter(new PercentFormatter());
		dataSet.setValueTextSize(ActivityReportsNavigate.TEXT_SIZE);
		dataSet.setValueTextColor(Color.WHITE);
		dataSet.setColors(charter.getPieChartColors());

		return dataSet;
	}

	public boolean zoomIn(Integer i) {
		return charter.zoomIn(i);
	}

	public boolean onBackPressed() {
		return charter.zoomOut();
	}
}
