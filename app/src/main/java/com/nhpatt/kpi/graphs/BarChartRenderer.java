package com.nhpatt.kpi.graphs;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class BarChartRenderer {

    public void drawChart(BarChart barChart, List<String> xValues, List<Float> yValues, int color) {

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barChart.setDescription("");

        barChart.setData(createData(xValues, yValues, color));
        barChart.invalidate();
    }

    private BarData createData(List<String> xValues, List<Float> yValues, int color) {

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < yValues.size(); i++) {
            barEntries.add(new BarEntry(yValues.get(i), i));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setBarSpacePercent(35f);
        barDataSet.setColor(color);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(xValues, dataSets);
        data.setValueTextSize(10f);

        return data;
    }
}
