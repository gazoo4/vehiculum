package sk.berops.android.fueller.gui.report;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.engine.charts.ExpenseDistributionPieChart;
import sk.berops.android.fueller.gui.MainActivity;

public class ActivityReportsNavigate extends Activity {

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
        chartManager = new ExpenseDistributionPieChart();
        chart.setData(chartManager.getPieData());
        chart.setUsePercentValues(true);
        chart.setCenterTextSize(TEXT_SIZE);

        chart.setDescription(MainActivity.garage.getActiveCar().getNickname());
        chart.setDescriptionColor(Color.WHITE);
        chart.setDescriptionTextSize(TEXT_SIZE);

        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.7f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(0);

        chart.setHoleRadius(60);
        chart.setTransparentCircleRadius(64);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //chartManager.invokeSelection((Integer) e.getData());
                redrawChart(chartManager.getPieData());
            }

            @Override
            public void onNothingSelected() {
                //chartManager.resetView();
                redrawChart(chartManager.getPieData());
            }
        });

        Legend legend = chart.getLegend();
        legend.setPosition(LegendPosition.LEFT_OF_CHART);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setXEntrySpace(3);
        legend.setYEntrySpace(0);
        legend.setYOffset(0);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(TEXT_SIZE);
        legend.setForm(Legend.LegendForm.LINE);

        chart.animateXY(ANIMATE_MILLIS, ANIMATE_MILLIS);
        chart.highlightValues(null);
        chart.invalidate();
    }

    private void redrawChart(PieData data) {
        chart.setData(data);
        chart.invalidate();
    }

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