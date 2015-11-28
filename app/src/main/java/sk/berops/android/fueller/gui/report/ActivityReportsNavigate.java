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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.engine.charts.ExpenseDistributionPieChart;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.TextFormatter;

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

    /**
     * Initialize the default chart state. This means, generate chart based on the top-level view of the expenses.
     */
    private void initializeChart() {
        chartManager = new ExpenseDistributionPieChart();
        chart.setData(chartManager.getPieData());
        chart.setUsePercentValues(true);
        chart.setCenterTextSize(TEXT_SIZE);

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

        updateDescription(-1);

        // listener to allow a deep dive into the chart values by clicking on the pie section of interest
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Integer typeId = (Integer) e.getData();
                chartManager.invokeSelection(typeId);
                updateDescription(typeId);
                redrawChart();
            }

            @Override
            public void onNothingSelected() {
                reset();
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

    /**
     * Display the new data loaded into the chartMananger
     */
    private void redrawChart() {
        chart.setData(chartManager.getPieData());
        chart.animateXY(ANIMATE_MILLIS, ANIMATE_MILLIS);
        chart.invalidate();
    }

    /**
     * Reset the chart view to defaults
     */
    private void reset() {
        chartManager.reset();
        updateDescription(-1);
        redrawChart();
    }

    /**
     * Generate chart description based on the scope of the displayed expense data
     * @param typeId -1 if looking at the overall view, otherwise the ExpenseType id
     */
    private void updateDescription(int typeId) {
        String description;
        double costs = 0.0;
        History history = MainActivity.garage.getActiveCar().getHistory();

        // If -1, we're reviewing the whole expense dataset.
        if (typeId == -1) {
            description = getString(R.string.activity_reports_navigate_chart_title);
            costs = history.getTotalCost();
        } else {
            sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType type = sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType.getExpenseType(typeId);
            description = type.getExpenseType();
            costs = history.getEntries().getLast().getConsumption().getTotalCostPerEntryType().get(type);
        }

        // Null for automatic currency format chooser.
        // TODO: add currency here. Ideally from locales.
        description += ": " + TextFormatter.format(costs, null);
        chart.setDescription(description);
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

    /**
     * If the back button is pressed, we want to display the chart from a one level higher perspective.
     * Once we reach the top level view in the chart, the usual back button functionality applies.
     */
    @Override
    public void onBackPressed() {
        if (chartManager.getDepth() != 0) {
            reset();
        } else {
            super.onBackPressed();
        }
    }
}