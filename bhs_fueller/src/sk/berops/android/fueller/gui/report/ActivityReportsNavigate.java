package sk.berops.android.fueller.gui.report;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.gui.ActivityEntryAdd;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityReportsNavigate extends Activity {

	/**
	 * Pie chart diplaying the total distributions of the total car expenses
	 */
	protected PieChart chart;

	/**
	 * Duration of the pie chart animation
	 */
	private final int ANIMATE_MILLIS = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_show);

		attachGuiObjects();
		initializeGuiObjects();
	}

	private void attachGuiObjects() {
		chart = (PieChart) findViewById(R.id.activity_stats_show_chart);
	}

	private void initializeGuiObjects() {
		initializeChart();
	}

	private void initializeChart() {
		PieDataSet dataset = getPieDataSet();
		dataset.setSliceSpace(2);
		dataset.setSelectionShift(3);
		dataset.setValueFormatter(new PercentFormatter());
		dataset.setValueTextSize(11);
		dataset.setValueTextColor(Color.WHITE);		

		PieData data = new PieData(getValues(), dataset);
		chart.setData(data);
		chart.setUsePercentValues(true);
		chart.setDescription(MainActivity.garage.getActiveCar().getNickname());

		chart.setExtraOffsets(5, 10, 5, 5);
		chart.setDragDecelerationFrictionCoef(0.9f);

		chart.setDrawHoleEnabled(true);
		chart.setHoleColorTransparent(true);

		chart.setTransparentCircleColor(Color.WHITE);
		chart.setTransparentCircleAlpha(100);

		chart.setHoleRadius(60);
		chart.setTransparentCircleRadius(64);

		chart.setDrawCenterText(true);

		chart.setRotationAngle(0);
		chart.setRotationEnabled(true);

		Legend legend = chart.getLegend();
		legend.setPosition(LegendPosition.BELOW_CHART_RIGHT);
		legend.setXEntrySpace(7);
		legend.setYEntrySpace(0);
		legend.setYOffset(0);

		chart.animateXY(ANIMATE_MILLIS, ANIMATE_MILLIS);
		chart.highlightValues(null);
		chart.invalidate();
	}

	/**
	 * Method to help to provide the legend for the chart
	 * 
	 * @return Expense types
	 */
	private ArrayList<String> getValues() {
		ArrayList<String> values = new ArrayList<String>();
		History history = MainActivity.garage.getActiveCar().getHistory();
		Consumption consumption = history.getEntries().getLast().getConsumption();
		for (ExpenseType t : ExpenseType.values()) {
			if (consumption.getTotalCostPerEntryType().get(t) == null) {
				continue;
			}
			values.add(t.getExpenseType());
		}
		return values;
	}

	/**
	 * Method to collect the values for the pie chart
	 * 
	 * @return Costs per expense type
	 */
	private PieDataSet getPieDataSet() {
		ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<com.github.mikephil.charting.data.Entry>();
		History history = MainActivity.garage.getActiveCar().getHistory();
		Consumption consumption = history.getEntries().getLast().getConsumption();
		for (ExpenseType t : ExpenseType.values()) {
			if (consumption.getTotalCostPerEntryType().get(t) == null) {
				continue;
			}
			double value = consumption.getTotalCostPerEntryType().get(t);
			com.github.mikephil.charting.data.Entry entry = new com.github.mikephil.charting.data.Entry(
					(float) value, t.getId());
			entries.add(entry);
		}

		PieDataSet dataset = new PieDataSet(entries, "Expense Distribution");

		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);
		
		dataset.setColors(colors);
		return dataset;
	}

	/**
	 * React on the screen clicks
	 * 
	 * @param view
	 */
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_stats_show_button_fuelling:
			startActivity(new Intent(this, ActivityEntriesShow.class));
			break;
		case R.id.activity_stats_show_button_charts:
			startActivity(new Intent(this, ActivityCharts.class));
			break;
		}
	}
}
