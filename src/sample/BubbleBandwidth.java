package sample;

/**
 * Created by Murzynas on 2016-04-18.
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class BubbleBandwidth extends Application {

    public ArrayList<CircleData> circles = new ArrayList<>();
    public static AnchorPane canvas;
    public static Pane circlesPane;
    public CircleDataController circleDataController;

    private final static int CANVAS_WIDTH = 800;
    private final static int CANVAS_HEIGHT = 800;


    /**
     * Variables used to determine which transition should be used for a circle being drawn right now.
     */
    private ArrayList<Transition> transitions;
    private int transitionsIndex;

    /**
     * Variable used to determine current scale multiplier of the view.
     */
    private double scaleFactor = 1.0;

    /**
     * Two variables used for dragging of the view.
     */
    private double pressedX;
    private double pressedY;

    /**
     * Used to determine whether we need to update the view or not.
     * Set to true every time a change in data occurs.
     * Set to false when update was finished.
     */
    private boolean updateNeeded;

    @Override
    public void start(final Stage primaryStage) {
        circleDataController = new CircleDataController();
        transitions = new ArrayList<>();
        transitions.add(new Transition(0, 1)); //UP
        transitions.add(new Transition(1, 1)); //RIGHT-UP
        transitions.add(new Transition(1, 0));  //RIGHT
        transitions.add(new Transition(1, -1)); //RIGHT-DOWN
        transitions.add(new Transition(0, -1)); //DOWN
        transitions.add(new Transition(-1, -1)); //LEFT-DOWN
        transitions.add(new Transition(-1, 0)); //LEFT
        transitions.add(new Transition(-1, 1)); //LEFT-UP

        transitionsIndex = 0;
        canvas = new AnchorPane();
        circlesPane =  new Pane();
        circlesPane.setMinHeight(CANVAS_HEIGHT);
        circlesPane.setMinWidth(CANVAS_WIDTH-200);
        circlesPane.getStyleClass().add("bordered-pane");

        final Scene scene = new Scene(canvas, CANVAS_WIDTH, CANVAS_HEIGHT);

        circlesPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0 || (event.getDeltaY() < 0 && circlesPane.getScaleY() < 1.0)) {
                    return;
                }

                scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 1/1.1;
                circlesPane.setScaleX(circlesPane.getScaleX() * scaleFactor);
                circlesPane.setScaleY(circlesPane.getScaleY() * scaleFactor);
            }
        });

        circlesPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
            }
        });

        circlesPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                circlesPane.setTranslateX(circlesPane.getTranslateX() + event.getX() - pressedX);
                circlesPane.setTranslateY(circlesPane.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        });

        primaryStage.setTitle("Bubble Bandwidth");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.getChildren().add(circlesPane);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent t) {
                if(updateNeeded) {
                    circlesPane.setVisible(false);
                    circlesPane.getChildren().clear();
                    circles.clear();
                    HashMap<String, CircleData> circleDatas = circleDataController.getCirclesDatasMap();
                    for(String key : circleDatas.keySet()) {

                        CircleData cd = circleDatas.get(key);
                        cd.recalculatePercents(circleDataController.getSummedCirclesValues());

                        Circle c = new Circle(cd.getPercentsValue()*scaleFactor, cd.getColor());
                        Tooltip.install(
                                c,
                                createTooltip(cd)
                        );

                        c.setStrokeWidth(1.0);
                        c.setStroke(Color.BLACK);
                        cd.setCircle(c);
                        circles.add(cd);
                        c.setId(cd.getId());
                        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                Circle targetCircle = (Circle) mouseEvent.getTarget();
                                CircleData circleData = circleDataController.getCirclesDatasMap().get(targetCircle.getId());
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

                        circlesPane.getChildren().addAll(c);
                    }
                    updateNeeded = false;
                }

                for(CircleData c : circles) {
                    checkShapeIntersection(c);
                }
                circlesPane.setVisible(true);
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    /**
     * Used to operate on data concerning circles. This method will determine whether we must add a new circle,
     * modify existing one or remove it.
     * @param id - id of circle that we are going to add/modify/delete
     * @param value - value for circle that will be modified/added
     * @throws Exception - throws exception if given value is invalid or when value = -1 (points to removal operation)
     * but id does not exists
     */
    public void newData(String id, Float value) throws Exception {
        HashMap<String, CircleData> circlesMap = circleDataController.getCirclesDatasMap();
        if(value <= 0) {
            if(value == -1) {
                if(circlesMap.containsKey(id)) {
                    circleDataController.removeCircle(id);
                    updateNeeded = true;
                } else {
                    throw new Exception("Circle with given id="+id+" does not exist and so can't be deleted!");
                }
            } else {
                throw new Exception("Invalid value! Value parameter must be either greater than 0 " +
                                    "(adding new circle or editing existing one) or equal to '-1'(deleting existing circle) !");
            }
        }

        if(circlesMap.containsKey(id)) {
            circleDataController.modifyExistingCirlce(id, value);
        } else {
            circleDataController.addNewCircle(id, value, transitions.get(transitionsIndex));
            transitionsIndex++;
            if(transitionsIndex >= transitions.size()) {
                transitionsIndex = 0;
            }
        }

        updateNeeded = true;
    }

    /**
     * Creates tooltip for a given circle.
     * It contains description of that circle: it's id, value and percentage value.
     * @param cd - data of the circle we create tooltip for
     * @return
     */
    private Tooltip createTooltip(CircleData cd) {
        Tooltip tooltip = new Tooltip("ID: "+cd.getId()+"\n"+
                "VALUE: "+cd.getValue()+"\n"+
                "PERCENTAGE: "+cd.getPercentsValue());
        tooltip.setTextAlignment(TextAlignment.CENTER);

        return tooltip;
    }

    /**
     * Checks collisions between given circle and all other circles.
     * If collision is detected then we move one with the smaller id away until it's not colliding anymore.
     * @param cd1 - data of the circle we check collisions with
     */
    private void checkShapeIntersection(CircleData cd1) {
        Shape circle1 = cd1.getCircle();
        for (CircleData cd2 : circles) {
            Shape circle2 = cd2.getCircle();
            if (!circle2.equals(circle1)) {
                boolean intersect = circle1.getBoundsInParent().intersects(circle2.getBoundsInParent());

                Circle c1 = (Circle) circle1;
                Circle c2 = (Circle) circle2;
                while(intersect){
                    intersect = circle1.getBoundsInParent().intersects(circle2.getBoundsInParent());
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

    public static void main(final String[] args) {
        launch(args);
    }
}