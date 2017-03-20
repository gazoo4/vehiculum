package sk.berops.android.vehiculum.gui.report;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

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

		List<Entry> entries;
		float xValue;
		float yValue;
		LineData data = new LineData();
		for (FuellingEntry.FuelType type : FuellingEntry.FuelType.values()) {
			List<FuellingEntry> fuellingEntries = MainActivity.garage.getActiveCar().getHistory().getFuellingEntriesFiltered(type);
			if (fuellingEntries.size() < 2) {
				continue;
			}

			entries = new ArrayList<>();
			for (sk.berops.android.vehiculum.dataModel.expense.FuellingEntry e : fuellingEntries) {
				xValue = (float) UnitConstants.convertDistanceFromSI(e.getMileageSI());
				yValue = (float) e.getFuelConsumption().getFloatingAveragePerFuelType().get(type).doubleValue();
				entries.add(new Entry(xValue, yValue));
			}

			LineDataSet dataSet = new LineDataSet(entries, "Consumption");
			dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
			dataSet.setColor(type.getColor());
			dataSet.setValueTextColor(Color.WHITE);

			data.addDataSet(dataSet);
		}

		chart.setScaleYEnabled(false);
		chart.setPinchZoom(true);
		chart.setData(data);
		chart.invalidate();
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_android_chart);
	}

	@Override
	protected void attachGuiObjects() {
		chart = (LineChart) findViewById(R.id.activity_android_chart_linechart);
	}
}
