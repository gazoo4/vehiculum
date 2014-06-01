package sk.berops.android.fueller.gui.report;

import java.util.ArrayList;
import java.util.HashMap;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.engine.charts.HistoryViewData;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class ActivityCharts extends Activity {
	
	private int resolution = 100;
	private Car car = MainActivity.garage.getActiveCar();
	private History history = car.getHistory();
	private FuelType fuelType = history.getFuellingEntries().getLast().getFuelType();
	
	//keep in mind here we should go through all the fuels so that we can show the consumption from all of them
	//http://android-graphview.org/#api
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charts);
		attachGuiObjects();
		generateChart(car.getInitialMileage(), car.getCurrentMileage());
	}
	
	public void attachGuiObjects() {
	}
	
	private void generateChart(double domainStart, double domainEnd) {
		GraphView graphView = new LineGraphView(this, "Consumption History");
		int dataSize = (int) (domainEnd - domainStart) + 1;
		HashMap<FuelType, ArrayList<HistoryViewData>> graphDataMap = new HashMap<FuelType, ArrayList<HistoryViewData>>(); 
		
		// initialize HistoryViewData for all fuel types
		for (FuelType type : FuelType.values()) {
			graphDataMap.put(type, new ArrayList<HistoryViewData>());
		}
		
		// fill-in the HistoryViewData with all the average consumptions
		for (FuellingEntry e : history.getFuellingEntries()) {
			graphDataMap.get(e.getFuelType()).add(new HistoryViewData(e.getMileage(), e.getFloatingConsumption()));
		}
		
		// remove those HistoryViewData which are empty (as there was no re-fuelling of this type)
		for (FuelType type : FuelType.values()) {
			if (graphDataMap.get(type).size() < 3) {
				graphDataMap.remove(type);
				continue;
			}
		}
		
		HistoryViewData[] data;
		GraphViewSeriesStyle style;
		GraphViewSeries series;
		// add all the data series to the chart
		for (FuelType type : graphDataMap.keySet()) {
			data = graphDataMap.get(type).toArray(new HistoryViewData[0]);
			style = new GraphViewSeriesStyle(type.getColor(), 4);
			series = new GraphViewSeries(type.getType(), style, data);
			graphView.addSeries(series);
		}
		
		graphView.setBackgroundColor(Color.BLACK);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setGridColor(Color.GRAY);
		graphView.getGraphViewStyle().setTextSize(20);
		graphView.setScrollable(true);
		graphView.setScalable(true);
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.setLegendWidth(200);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_charts_layout);
		layout.addView(graphView);
		
	}
}
