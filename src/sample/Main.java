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

        final TermometerPanel panel = new TermometerPanel();

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(700, 450);
        frame.setVisible(true);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    panel.setTemperature(random.nextDouble() * 100.0);
                }
            }
        });
        thread.start();

    }

}