package sample;

/**
 * Created by Murzynas on 2016-04-18.
 */

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BubbleBandwidth extends Application {

    public HashMap<String, CircleData> cachedMap;
    public ArrayList<CircleData> circles = new ArrayList<>();
    public static Pane canvas;
    public final static DataController dataController = DataController.getInstance();
    public static Button addBtn;
    public static Button dltBtn;
    private boolean updateNeeded;

    private final static int CANVAS_WIDTH = 1000;
    private final static int CANVAS_HEIGHT = 1000;

    private ArrayList<Transition> transitions;
    private int transitionsIndex;

    private double scaleFactor = 1.0;
    private double pressedX;
    private double pressedY;


    @Override
    public void start(final Stage primaryStage) {
        transitions = new ArrayList<>();
        transitions.add(new Transition(0, 5)); //UP
        transitions.add(new Transition(5, 5)); //RIGHT-UP
        transitions.add(new Transition(5, 0));  //RIGHT
        transitions.add(new Transition(5, -5)); //RIGHT-DOWN
        transitions.add(new Transition(0, -5)); //DOWN
        transitions.add(new Transition(-5, -5)); //LEFT-DOWN
        transitions.add(new Transition(-5, 0)); //LEFT
        transitions.add(new Transition(-5, 5)); //LEFT-UP

        transitionsIndex = 0;
        cachedMap = dataController.getCirclesDatasMap();
        canvas = new Pane();

        final Scene scene = new Scene(canvas, CANVAS_WIDTH, CANVAS_HEIGHT);

        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0 || (event.getDeltaY() < 0 && canvas.getScaleY() < 1.0)) {
                    return;
                }

                scaleFactor =
                        (event.getDeltaY() > 0)
                                ? 1.1
                                : 1/1.1;

                canvas.setScaleX(canvas.getScaleX() * scaleFactor);
                canvas.setScaleY(canvas.getScaleY() * scaleFactor);
            }
        });

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                canvas.setTranslateX(canvas.getTranslateX() + event.getX() - pressedX);
                canvas.setTranslateY(canvas.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        });

        primaryStage.setTitle("Bubble Bandwidth");
        primaryStage.setScene(scene);
        primaryStage.show();

        addBtn = new Button();
        addBtn.setText("Add Circle");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Random generator = new Random();
               dataController.addNewCircleData(String.valueOf(dataController.uniqueId++), (float)generator.nextInt(20), transitions.get(transitionsIndex));
                cachedMap = dataController.getCirclesDatasMap();
                updateNeeded = true;
                transitionsIndex++;
                if(transitionsIndex >= transitions.size()) {
                    transitionsIndex = 0;
                }
            }
        });
        scene.getChildren().add(addBtn);
        addBtn.relocate(10, 10);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent t) {
                if(updateNeeded) {
                    canvas.getChildren().clear();
                    circles.clear();
                    Random generator = new Random();
                    HashMap<String, CircleData> circleDatas = dataController.getCirclesDatasMap();
                    for(String key : circleDatas.keySet()) {

                        CircleData cd = circleDatas.get(key);
                        cd.recalculatePercents(dataController.getSummedCirclesValues());

//                        double overlapingSquareField = (cd.getPercentsValue()/100)*(CANVAS_HEIGHT*CANVAS_WIDTH*0.1);
//                        double radius = Math.sqrt(overlapingSquareField)/2;
//
//                        System.out.println(overlapingSquareField);
                        Circle c = new Circle(cd.getPercentsValue()*scaleFactor, cd.getColor());

                        c.setStrokeWidth(1.0);
                        c.setStroke(Color.BLACK);
                        cd.setCircle(c);
                        circles.add(cd);
                        c.setId(cd.getId());
                        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                Circle targetCircle = (Circle) mouseEvent.getTarget();
                                CircleData circleData = dataController.getCirclesDatasMap().get(targetCircle.getId());
                                System.out.println(circleData.getId()+" | "+circleData.getValue()+
                                        " | "+circleData.getPercentsValue()+"%");
                            }
                        });
                        if(cd.getX() == -1 && cd.getY() == -1) {
                            cd.setX(400);
                            cd.setY(300);
                        }
                        c.setCenterX(cd.getX());
                        c.setCenterY(cd.getY());
//                        Text text = createText(cd.getId(),cd.getX(), cd.getY());
//                        text.relocate(c.getCenterX(), c.getCenterY());
//                        text.setTextAlignment(TextAlignment.CENTER);

                        ScaleTransition st = new ScaleTransition(Duration.millis(1000), c);
                        st.setByX(1.5f);
                        st.setByY(1.5f);
                        st.setCycleCount(1);
                        canvas.getChildren().addAll(c); //,text
                        st.play();
                    }
                    updateNeeded = false;
                    canvas.getChildren().add(addBtn);
                    addBtn.relocate(10, 10);
                }

                for(CircleData c : circles) {
                    checkShapeIntersection(c);
                }

//                if(canvas.getChildren().get(0)!= null) {
//                    Circle circle = (Circle) canvas.getChildren().get(0);
//                    circle.setLayoutX(circle.getLayoutX() + 3);
//                    circle.setLayoutY(circle.getLayoutY() + 3);
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

    private void checkShapeIntersection(CircleData cd1) {
        Shape circle1 = cd1.getCircle();
        for (CircleData cd2 : circles) {
            Shape circle2 = cd2.getCircle();
            if (circle2 != circle1) {
                Shape intersect = Shape.intersect(circle1, circle2);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    Circle c1 = (Circle) circle1;
                    Circle c2 = (Circle) circle2;
                    if(Integer.parseInt(c1.getId()) < Integer.parseInt(c2.getId())) {
                        Transition transition = cd2.getTransition();
                        circle2.setLayoutX(circle2.getLayoutX()+ transition.x);
                        circle2.setLayoutY(circle2.getLayoutY()+ transition.y);
                    } else {
                        Transition transition = cd1.getTransition();
                        circle1.setLayoutX(circle1.getLayoutX() + transition.x);
                        circle1.setLayoutY(circle1.getLayoutY() + transition.y);
                    }
                }
            }
        }

    }

    private Text createText(String string, double x, double y) {
        Text text = new Text(string);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setStyle(
                "-fx-font-family: \"Times New Roman\";" +
                        "-fx-font-style: italic;"
        );
        return text;
    }

    public static void main(final String[] args) {
        launch(args);

    }
}