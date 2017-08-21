package sk.berops.android.vehiculum.gui.report;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.expense.History;
import sk.berops.android.vehiculum.engine.charts.ExpenseDistributionPieChart;
import sk.berops.android.vehiculum.gui.DefaultActivity;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.TextFormatter;

public class ActivityReportsNavigate extends DefaultActivity {

    private Button buttonShowFuellings;
    private Button buttonShowEntries;

    /**
     * Size of the text in chart (including the legend)
     */
    public static final float TEXT_SIZE = 14f;

    /**
     * Pie chart displaying the total distributions of the total car expenses
     */
    protected PieChart chart;

    /**
     * Duration of the pie chart animation effect
     */
    private final int ANIMATE_MILLIS = 1500;

    /**
     * Object responsible for extracting the PieDataSet structures from various Consumptions for the charting
     */
    private ExpenseDistributionPieChart chartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_stats_show);
    }

    @Override
    protected void attachGuiObjects() {
        buttonShowEntries = (Button) findViewById(R.id.activity_stats_show_button_fuelling);
        buttonShowFuellings = (Button) findViewById(R.id.activity_stats_show_button_charts);

        chart = (PieChart) findViewById(R.id.activity_stats_show_chart);

        listButtons.add(buttonShowEntries);
        listButtons.add(buttonShowFuellings);
    }

    @Override
    protected void initializeGuiObjects() {
        super.initializeGuiObjects();
        initializeChart();
    }

    /**
     * Initialize the default chart state. This means, generate chart based on the top-level view of the expenses.
     */
    private void initializeChart() {
        chartManager = new ExpenseDistributionPieChart();
        chart.setUsePercentValues(true);
        chart.setCenterTextSize(TEXT_SIZE);

	    chart.setCenterTextColor(Color.WHITE);
	    chart.setCenterTextSize(TEXT_SIZE);

	    chart.setEntryLabelColor(Color.WHITE);
	    chart.setEntryLabelTextSize(TEXT_SIZE);

        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.7f);

        chart.setDrawHoleEnabled(true);
	    chart.setHoleColor(0x00000000);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(0);

        chart.setHoleRadius(60);
        chart.setTransparentCircleRadius(64);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

	    chart.highlightValues(null);

	    Description description = new Description();
	    description.setText(MainActivity.garage.getActiveCar().getNickname());
	    description.setTextSize(TEXT_SIZE);
	    description.setTextColor(Color.WHITE);
	    chart.setDescription(description);

        // listener to allow a deep dive into the chart values by clicking on the pie section of interest
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
	        @Override
	        public void onValueSelected(Entry e, Highlight h) {
		        Integer typeId = (Integer) e.getData();
		        chartManager.invokeSelection(typeId);
		        redrawChart();
	        }

	        @Override
            public void onNothingSelected() {
                reset();
            }
        });

        initLegend();
	    redrawChart();
    }

    /**
     * Display the new data loaded into the chartMananger
     */
    private void redrawChart() {
        chart.setData(chartManager.getPieData());
        chart.animateXY(ANIMATE_MILLIS, ANIMATE_MILLIS);
        chart.invalidate();
    }

    private void initLegend() {
	    Legend legend = chart.getLegend();
	    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
	    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
	    legend.setOrientation(Legend.LegendOrientation.VERTICAL);
	    legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
	    legend.setXEntrySpace(3);
	    legend.setYEntrySpace(0);
	    legend.setYOffset(0);
	    legend.setTextColor(Color.WHITE);
	    legend.setTextSize(TEXT_SIZE);
	    legend.setForm(Legend.LegendForm.LINE);
    }

    /**
     * Reset the chart view to defaults
     */
    private void reset() {
        chartManager.init();
        redrawChart();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_stats_show_button_fuelling:
                startActivity(new Intent(this, ActivityEntriesShow.class));
                break;
            case R.id.activity_stats_show_button_charts:
                startActivity(new Intent(this, ActivityCharts2.class));
                break;
        }
    }

    /**
     * If the back button is pressed, we want to display the chart from a one level higher perspective.
     * Once we reach the top level view in the chart, the usual back button functionality applies.
     */
    @Override
    public void onBackPressed() {
        if (chartManager.getDepth() == 0) {
	        super.onBackPressed();
        } else {
	        // TODO: instead of reset we should go one level higher
	        reset();
        }
    }
}