package eecs1021;

//Imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//Creating grapherClass
public class grapherClass {
    private TimeSeries series;
    private TimeSeriesCollection dataset;
    private JFreeChart chart;
    private JFrame frame;
    private ChartPanel chartPanel;

    public grapherClass() {
        //Initializing series and the dataset
        series = new TimeSeries("Moisture Level");
        dataset = new TimeSeriesCollection(series);

        //Creating the graphs axis
        chart = ChartFactory.createTimeSeriesChart(
                "Moisture Level vs Time",
                "Time",
                "Moisture Level",
                dataset,
                false,
                true,
                false
        );

        //Setting colour to white, and setting lower and upper bound limits
        chart.getXYPlot().getRangeAxis().setLowerBound(0.0);
        chart.getXYPlot().getRangeAxis().setUpperBound(100.0);

        //Creating the actual chart and making it visible
        chartPanel = new ChartPanel(chart);
        frame = new JFrame("Moisture Level Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    //Plot Graph method
    public void plotGraph(ArrayList<Integer> moistureLevels) {
        //for loop adds the data to the series
        for (int i = 0; i < moistureLevels.size(); i++) {
            Second currentSecond = new Second();
            series.addOrUpdate(currentSecond, moistureLevels.get(i));
        }
        chart.fireChartChanged();
    }
}

