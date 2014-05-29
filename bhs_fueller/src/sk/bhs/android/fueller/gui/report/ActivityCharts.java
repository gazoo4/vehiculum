package sk.bhs.android.fueller.gui.report;

import java.util.Arrays;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import sk.bhs.android.fueller.R;
import android.app.Activity;
import android.os.Bundle;

public class ActivityCharts extends Activity {
	
	private static final int HISTORY_SIZE = 300;
	
	private XYPlot historyPlot;
	private SimpleXYSeries averageConsumptionHistorySeries = null;
	private SimpleXYSeries floatingConsumptionHistorySeries = null;
	
	//keep in mind here we should go through all the fuels so that we can show the consumption from all of them
	//https://bitbucket.org/androidplot/androidplot/src/master/Examples/DemoApp/src/com/androidplot/demos/OrientationSensorExampleActivity.java?at=master
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charts);
		attachGuiObjects();
		
		historyPlot = (XYPlot) findViewById(R.id.activity_charts_plot_consumption);
	}
	
	public void attachGuiObjects() {
	}

	
}
