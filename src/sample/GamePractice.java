package sample;

/**
 * Created by Murzynas on 2016-04-18.
 */

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GamePractice extends Application {

    public ArrayList<Circle> circles = new ArrayList<>();
    public static Pane canvas;
    public final static DataController dataController = DataController.getInstance();

    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);


        primaryStage.setTitle("Circles");
        primaryStage.setScene(scene);
        primaryStage.show();

        Circle circle = new Circle(15, Color.BLUE);
        circle.relocate(100, 100);

        canvas.getChildren().addAll(circle);

        Button btn = new Button();
        btn.setText("Add Circle");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               dataController.addCircleData("x", 2.0f);
            }
        });

        btn.relocate(10, 10);
        canvas.getChildren().add(btn);


        Rectangle rect = new Rectangle (100, 40, 100, 100);
        rect.setArcHeight(50);
        rect.setArcWidth(50);
        rect.setFill(Color.VIOLET);

        ScaleTransition st = new ScaleTransition(Duration.millis(2000), rect);
        st.setByX(1.5f);
        st.setByY(1.5f);
        st.setCycleCount(4);
        st.setAutoReverse(true);
        canvas.getChildren().add(rect);
        st.play();

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            double deltaX = 3;
            double deltaY = 3;
            @Override
            public void handle(final ActionEvent t) {
//                canvas.getChildren().clear();
//                for(int i=0; i<dataController.getCircleDatas().size(); i++) {
//                    Circle c = new Circle(15, Color.RED);
//                    c.relocate(100+(30*i), 100+(30*i));
//                    canvas.getChildren().addAll(c);
//                }
//                circle.setLayoutX(circle.getLayoutX() + deltaX);
//                circle.setLayoutY(circle.getLayoutY() + deltaY);
//                final Bounds bounds = canvas.getBoundsInLocal();
//                final boolean atRightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
//                final boolean atLeftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
//                final boolean atBottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
//                final boolean atTopBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());
//
//                if (atRightBorder || atLeftBorder) {
//                    deltaX *= -1;
//                }
//                if (atBottomBorder || atTopBorder) {
//                    deltaY *= -1;
//                }
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}