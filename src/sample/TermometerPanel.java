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

    private static final int THREAD_SLEEP = 1000;
    private static final double MIN_TEMPERATURE = 0.0;
    private static final double MAX_TEMPERATURE = 100.0;
    private static final int W = 700;
    private static final int H = 450;

    private ThermometerPlot thermometerPlot = new ThermometerPlot();

    public TermometerPanel() {
        try {
            thermometerInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTemperature(double temperature){
        if (temperature > MAX_TEMPERATURE){
            temperature = MAX_TEMPERATURE;
        } else if (temperature < MIN_TEMPERATURE){
            temperature = MIN_TEMPERATURE;
        }
        DefaultValueDataset defaultValueDataset = new DefaultValueDataset((int)temperature);
        thermometerPlot.setDataset(defaultValueDataset);
    }

    public void setTemperature(int temperature){
        if (temperature > (int)MAX_TEMPERATURE){
            temperature = (int)MAX_TEMPERATURE;
        } else if (temperature < (int)MIN_TEMPERATURE){
            temperature = (int)MIN_TEMPERATURE;
        }
        DefaultValueDataset defaultValueDataset = new DefaultValueDataset(temperature);
        thermometerPlot.setDataset(defaultValueDataset);
    }

    private void thermometerInit(){
        thermometerPlot.setSubrangePaint(0, Color.green.darker());
        thermometerPlot.setSubrangePaint(1, Color.orange);
        thermometerPlot.setSubrangePaint(2, Color.red.darker());
        JFreeChart chart = new JFreeChart("Temperature", JFreeChart.DEFAULT_TITLE_FONT, thermometerPlot, true);
        this.add(new ChartPanel(chart, W, H, W, H, W, H, false, true, true, true, true, true));
    }

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