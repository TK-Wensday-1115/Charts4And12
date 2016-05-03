package sample;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Dom-Pc on 2016-04-19.
 */
public class Main {

    public static void main(String[] args){

        final JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout(5, 5));
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Thermometer");

        final TermometerPanel panel = new TermometerPanel("Temperature 1", 0.0, 200.0);
        final TermometerPanel panel2 = new TermometerPanel("Temperature 2", 0.0, 100.0);

        frame.getContentPane().add(panel, BorderLayout.WEST);
        frame.getContentPane().add(panel2, BorderLayout.EAST);
        frame.setSize(1400, 490);
        frame.setVisible(true);
        frame.setResizable(false);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true){
                    try{
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    panel.setTemperature(random.nextDouble() * 200.0);
                    panel2.setTemperature(random.nextDouble() * 200.0);
                }
            }
        });
        thread.start();

    }

}