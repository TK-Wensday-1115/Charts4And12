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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BubbleBandwidth extends Application {

    public HashMap<String, CircleData> cachedMap;
    public ArrayList<CircleData> circles = new ArrayList<>();
    public static AnchorPane canvas;
    public static Pane circlesPane;
    public final static CircleDataController CIRCLE_DATA_CONTROLLER = CircleDataController.getInstance();
    public static Button addBtn;
    public static Button dltBtn;
    private boolean updateNeeded;
    private String idToModify = "";

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
        cachedMap = CIRCLE_DATA_CONTROLLER.getCirclesDatasMap();
        canvas = new AnchorPane();
        circlesPane =  new Pane();
        circlesPane.setMinHeight(CANVAS_HEIGHT);
        circlesPane.setMinWidth(CANVAS_WIDTH-200);
        circlesPane.getStylesheets().add("../src/sample/style.css");
        circlesPane.getStyleClass().add("bordered-pane");

        final Scene scene = new Scene(canvas, CANVAS_WIDTH, CANVAS_HEIGHT);

        circlesPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0 || (event.getDeltaY() < 0 && circlesPane.getScaleY() < 1.0)) {
                    return;
                }

                scaleFactor =
                        (event.getDeltaY() > 0)
                                ? 1.1
                                : 1/1.1;

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

        addBtn = new Button();
        addBtn.setText("Add Circle");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Random generator = new Random();
               CIRCLE_DATA_CONTROLLER.addNewCircleData(String.valueOf(CIRCLE_DATA_CONTROLLER.uniqueId++), (float)generator.nextInt(20), transitions.get(transitionsIndex));
                cachedMap = CIRCLE_DATA_CONTROLLER.getCirclesDatasMap();
                updateNeeded = true;
                transitionsIndex++;
                if(transitionsIndex >= transitions.size()) {
                    transitionsIndex = 0;
                }
            }
        });

        Button modifyBtn = new Button();
        modifyBtn.setText("Modify Circle");
        modifyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Random generator = new Random();
                int circlesCount = circles.size();
                    int i = generator.nextInt(circlesCount-1);
                    String id = circles.get(i).getId();
                    CIRCLE_DATA_CONTROLLER.modifyCircle(id, 4f);
                    idToModify = id;
            }
        });

        canvas.getChildren().add(addBtn);
        canvas.getChildren().add(modifyBtn);
        canvas.getChildren().add(circlesPane);
        AnchorPane.setRightAnchor(addBtn, 1d);
        AnchorPane.setTopAnchor(addBtn, 1d);
        AnchorPane.setRightAnchor(modifyBtn, 1d);
        AnchorPane.setTopAnchor(modifyBtn, 40d);
        AnchorPane.setLeftAnchor(circlesPane, 1d);
        AnchorPane.setTopAnchor(circlesPane, 1d);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent t) {
                if(updateNeeded) {
                    circlesPane.getChildren().clear();
                    circles.clear();
                    HashMap<String, CircleData> circleDatas = CIRCLE_DATA_CONTROLLER.getCirclesDatasMap();
                    for(String key : circleDatas.keySet()) {

                        CircleData cd = circleDatas.get(key);
                        cd.recalculatePercents(CIRCLE_DATA_CONTROLLER.getSummedCirclesValues());

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
                                CircleData circleData = CIRCLE_DATA_CONTROLLER.getCirclesDatasMap().get(targetCircle.getId());
                                System.out.println(circleData.getId()+" | "+circleData.getValue()+
                                        " | "+circleData.getPercentsValue()+"%");
//                                Text text = createText(circleData.getId()+" | "+circleData.getValue()+
//                                        " | "+circleData.getPercentsValue()+"%",targetCircle.getCenterX(), targetCircle.getCenterY());
//                                canvas.getChildren().add(text);
                            }
                        });
                        if(cd.getX() == -1 && cd.getY() == -1) {
                            cd.setX(400);
                            cd.setY(300);
                        }
                        c.setCenterX(cd.getX());
                        c.setCenterY(cd.getY());

//                        ScaleTransition st = new ScaleTransition(Duration.millis(1000), c);
//                        st.setByX(1.5f);
//                        st.setByY(1.5f);
//                        st.setCycleCount(1);
                        circlesPane.getChildren().addAll(c); //,text
                        //st.play();
                    }
                    updateNeeded = false;
                } else if(!idToModify.isEmpty()) {




                    idToModify = "";
                }

                for(CircleData c : circles) {
                    checkShapeIntersection(c);
                }

            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    private Tooltip createTooltip(CircleData cd) {
        Tooltip tooltip = new Tooltip("ID: "+cd.getId()+"\n"+
                "VALUE: "+cd.getValue()+"\n"+
                "PERCENTAGE: "+cd.getPercentsValue());
        tooltip.setTextAlignment(TextAlignment.CENTER);

        return tooltip;
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