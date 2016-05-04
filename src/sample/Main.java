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
                }
            }
        });
        thread.start();

        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true){
                    try{
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    panel2.setTemperature(random.nextDouble() * 100.0);
                }
            }
        });
        thread2.start();

        final BubbleBandwidth bubbleBandwidth = new BubbleBandwidth();

        final Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleBandwidth.startApplication();
            }
        });
        thread3.start();

        final Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true){
                    try{
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    try {
                        String id = String.valueOf(random.nextInt(20));
                        float value = (float)((random.nextFloat() * 200.0) + 1.0);
                        bubbleBandwidth.newData(id, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread4.start();

    }

}