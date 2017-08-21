package sk.berops.android.vehiculum.gui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.UnitConstants;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.gui.DefaultActivity;
import sk.berops.android.vehiculum.gui.MainActivity;

import static android.graphics.Color.WHITE;


/**
 * @author Bernard Halas
 * @date 3/18/17
 */

public class ActivityCharts2 extends DefaultActivity {
	LineChart chart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadAndDisplay();
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_android_chart);
	}

	@Override
	protected void attachGuiObjects() {
		chart = (LineChart) findViewById(R.id.activity_android_chart_linechart);
	}

	private void loadAndDisplay() {
		HashMap<FuellingEntry.FuelType, LinkedList<Entry>> chartEntries = new HashMap<>();

		LinkedList<FuellingEntry> fEntries = MainActivity.garage.getActiveCar().getHistory().getFuellingEntries();

		// Collect the Consumption values
		for (FuellingEntry fe: fEntries) {
			chartEntries.putIfAbsent(fe.getFuelType(), new LinkedList<>());

			LinkedList<Entry> entries = chartEntries.get(fe.getFuelType());
			entries.add(new Entry((float) fe.getMileage(), (float) fe.getFuelConsumption().getLastConsumption()));
		}

		// Load the values into chart DataSet objects
		LineData data = new LineData();
		for (FuellingEntry.FuelType type: chartEntries.keySet()) {
			LineDataSet dataSet = new LineDataSet(chartEntries.get(type), type.getType());
			dataSet.setColor(type.getColor());
			dataSet.setValueTextColor(Color.DKGRAY);

			data.addDataSet(dataSet);
		}

		// Load the chart
		chart.setData(data);
		// Refresh the chart
		chart.invalidate();
	}
}
