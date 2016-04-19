package sample;

import org.jfree.chart.plot.JThermometer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dom-Pc on 2016-04-18.
 */
public class TermometerPanel extends JPanel implements Runnable {

    private static final int THREAD_SLEEP = 1000;
    private static final double MIN_TEMPERATURE = 0.0;
    private static final double MAX_TEMPERATURE = 100.0;

    private JThermometer thermometer = new JThermometer();

    public void setTemperature(double temperature){
        if (temperature > MAX_TEMPERATURE){
            temperature = MAX_TEMPERATURE;
        } else if (temperature < MIN_TEMPERATURE){
            temperature = MIN_TEMPERATURE;
        }
        thermometer.setValue(temperature);
    }

    public TermometerPanel() {
        try {
            thermometerInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void thermometerInit(){
        thermometer.setValue(MIN_TEMPERATURE);
        thermometer.setBackground(Color.WHITE);
        thermometer.setOutlinePaint(null);
        //thermometer.setSubrangePaint();
        thermometer.setShowValueLines(true);
        thermometer.setFollowDataInSubranges(true);
        thermometer.setRange(MIN_TEMPERATURE, MAX_TEMPERATURE);
        thermometer.setSubrangeInfo(0, MIN_TEMPERATURE, MAX_TEMPERATURE, MIN_TEMPERATURE, MAX_TEMPERATURE);
        thermometer.addSubtitle("Temperature");
        this.add(thermometer);
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