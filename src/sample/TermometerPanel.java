package sample;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Dom-Pc on 2016-04-18.
 */
public class TermometerPanel extends JPanel implements Runnable {

    private static final int THREAD_SLEEP = 100;
    private static final int W = 700;
    private static final int H = 450;
    private static final double HALF_MEASURE = 0.5;
    private static final double THREE_FOURTH_MEASURE = 0.75;
    private static final int FIRST_SUBRANGE = 0;
    private static final int SECOND_SUBRANGE = 1;
    private static final int THIRD_SUBRANGE = 2;

    private final double MIN_TEMPERATURE;
    private final double MAX_TEMPERATURE;

    private ThermometerPlot thermometerPlot = new ThermometerPlot();

    /**
     * Constructor for TermometerPanel, creates panel in which is instance of Termometer Chart to display temperature.
     * It has method to set current value of temperature to display.
     * @param title - is the title of chart to be displayed
     * @param min - is minimum of temperature range
     * @param max - is maximum of temperature range
     */
    public TermometerPanel(String title, double min, double max) {
        MIN_TEMPERATURE = min;
        MAX_TEMPERATURE = max;
        try {
            thermometerInit(title);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Private method for initializing thermometer parameters like title, range, subranges and colors of subranges.
     * @param title - is the title of chart to be displayed
     */
    private void thermometerInit(String title){
        thermometerPlot.setSubrange(FIRST_SUBRANGE, MIN_TEMPERATURE, HALF_MEASURE * MAX_TEMPERATURE);
        thermometerPlot.setSubrange(SECOND_SUBRANGE, HALF_MEASURE * MAX_TEMPERATURE, THREE_FOURTH_MEASURE * MAX_TEMPERATURE);
        thermometerPlot.setSubrange(THIRD_SUBRANGE, THREE_FOURTH_MEASURE * MAX_TEMPERATURE, MAX_TEMPERATURE);
        thermometerPlot.setSubrangePaint(FIRST_SUBRANGE, Color.green.darker());
        thermometerPlot.setSubrangePaint(SECOND_SUBRANGE, Color.orange);
        thermometerPlot.setSubrangePaint(THIRD_SUBRANGE, Color.red.darker());
        thermometerPlot.setLowerBound(MIN_TEMPERATURE);
        thermometerPlot.setUpperBound(MAX_TEMPERATURE);
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, thermometerPlot, true);
        this.add(new ChartPanel(chart, W, H, W, H, W, H, false, true, true, true, true, true));
    }

    /**
     * Is public method that allows to set the temperature of thermometer. Takes double and casts it into int.
     * @param temperature - is the temperature to be set.
     */
    public void setTemperature(double temperature){
        if (temperature > MAX_TEMPERATURE){
            temperature = MAX_TEMPERATURE;
        } else if (temperature < MIN_TEMPERATURE){
            temperature = MIN_TEMPERATURE;
        }
        DefaultValueDataset defaultValueDataset = new DefaultValueDataset((int)temperature);
        thermometerPlot.setDataset(defaultValueDataset);
    }

    /**
     * Inner method that runs after initializing class because it's swing component and is controlled by Swing Event Scheduler.
     * It repaints panel with thermometer every 100 miliseconds.
     */
    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(THREAD_SLEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

}