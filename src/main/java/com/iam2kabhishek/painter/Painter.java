package com.iam2kabhishek.painter;

// import java.awt.image.RenderedImage;
// import javafx.swing.SwingFXUtils;
// import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Painter extends Application {

    private final int CANVAS_WIDTH = 1280;
    private final int CANVAS_HEIGHT = 720;

    private void setupFill(Button fillBtn, GraphicsContext gc, ColorPicker cpFill) {
        fillBtn.setOnAction(e -> {
            gc.setFill(cpFill.getValue());
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        });
    }

    private void setupClear(Stack<Shape> undoHistory, Stack<Shape> redoHistory, Button clear, GraphicsContext gc) {
        clear.setOnAction(e -> {
            gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            undoHistory.clear();
            redoHistory.clear();
        });
    }

    private void setupUndo(Stack<Shape> undoHistory, Stack<Shape> redoHistory, Button undo, GraphicsContext gc) {
        undo.setOnAction(e -> {
            if (!undoHistory.empty()) {
                gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                Shape removedShape = undoHistory.lastElement();
                if (removedShape.getClass() == Line.class) {
                    Line tempLine = (Line) removedShape;
                    tempLine.setFill(gc.getFill());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(),
                            tempLine.getEndY()));

                } else if (removedShape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) removedShape;
                    tempRect.setFill(gc.getFill());
                    tempRect.setStroke(gc.getStroke());
                    tempRect.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(
                            new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                } else if (removedShape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) removedShape;
                    tempCirc.setStrokeWidth(gc.getLineWidth());
                    tempCirc.setFill(gc.getFill());
                    tempCirc.setStroke(gc.getStroke());
                    redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                } else if (removedShape.getClass() == Ellipse.class) {
                    Ellipse tempEllipse = (Ellipse) removedShape;
                    tempEllipse.setFill(gc.getFill());
                    tempEllipse.setStroke(gc.getStroke());
                    tempEllipse.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(
                            new Ellipse(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(),
                                    tempEllipse.getRadiusY()));
                }
                Shape lastRedo = redoHistory.lastElement();
                lastRedo.setFill(removedShape.getFill());
                lastRedo.setStroke(removedShape.getStroke());
                lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                undoHistory.pop();

                for (int i = 0; i < undoHistory.size(); i++) {
                    Shape shape = undoHistory.elementAt(i);
                    if (shape.getClass() == Line.class) {
                        Line temp = (Line) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                    } else if (shape.getClass() == Rectangle.class) {
                        Rectangle temp = (Rectangle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                        gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                    } else if (shape.getClass() == Circle.class) {
                        Circle temp = (Circle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                    } else if (shape.getClass() == Ellipse.class) {
                        Ellipse temp = (Ellipse) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                    }
                }
            } else {
                System.out.println("there is no action to undo");
            }
        });
    }

    private void setupRedo(Stack<Shape> undoHistory, Stack<Shape> redoHistory, Button redo, GraphicsContext gc) {
        redo.setOnAction(e -> {
            if (!redoHistory.empty()) {
                Shape shape = redoHistory.lastElement();
                gc.setLineWidth(shape.getStrokeWidth());
                gc.setStroke(shape.getStroke());
                gc.setFill(shape.getFill());

                redoHistory.pop();
                if (shape.getClass() == Line.class) {
                    Line tempLine = (Line) shape;
                    gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                    undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(),
                            tempLine.getEndY()));
                } else if (shape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) shape;
                    gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                    gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

                    undoHistory.push(
                            new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                } else if (shape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) shape;
                    gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(),
                            tempCirc.getRadius());
                    gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(),
                            tempCirc.getRadius());

                    undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                } else if (shape.getClass() == Ellipse.class) {
                    Ellipse tempEllipse = (Ellipse) shape;
                    gc.fillOval(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(),
                            tempEllipse.getRadiusY());
                    gc.strokeOval(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(),
                            tempEllipse.getRadiusY());

                    undoHistory.push(
                            new Ellipse(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(),
                                    tempEllipse.getRadiusY()));
                }
                Shape lastUndo = undoHistory.lastElement();
                lastUndo.setFill(gc.getFill());
                lastUndo.setStroke(gc.getStroke());
                lastUndo.setStrokeWidth(gc.getLineWidth());
            } else {
                System.out.println("there is no action to redo");
            }
        });
    }

    private void setupSave(Stage primaryStage, Button save, Canvas canvas) {
        save.setOnAction((e) -> {
            FileChooser saveFile = new FileChooser();
            saveFile.setTitle("Save File");

            File file = saveFile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    // TODO: Issues with imports, fix this
                    WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                    canvas.snapshot(null, writableImage);
                    // OutputStream output = new FileOutputStream(file);
                    // ImageIO.write(Stream.wrap(output), "png", writableImage);

                } catch (Exception ex) {
                    System.out.println("Error!");
                }
            }

        });
    }

    private void setupOpen(Stage primaryStage, Button open, GraphicsContext gc) {
        open.setOnAction((e) -> {
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    gc.drawImage(img, 0, 0);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        Stack<Shape> undoHistory = new Stack();
        Stack<Shape> redoHistory = new Stack();

        ToggleButton drawBtn = new ToggleButton("Draw");
        ToggleButton rubberBtn = new ToggleButton("Rubber");
        ToggleButton lineBtn = new ToggleButton("Line");
        ToggleButton rectBtn = new ToggleButton("Rectangle");
        ToggleButton circleBtn = new ToggleButton("Circle");
        ToggleButton ellipseBtn = new ToggleButton("Ellipse");
        ToggleButton textBtn = new ToggleButton("Text");

        ToggleButton[] toolsArr = { drawBtn, rubberBtn, lineBtn, rectBtn, circleBtn, ellipseBtn, textBtn };

        ToggleGroup tools = new ToggleGroup();

        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }

        ColorPicker cpLine = new ColorPicker(Color.BLACK);
        ColorPicker cpFill = new ColorPicker(Color.valueOf("#1688f0"));

        TextArea text = new TextArea();
        text.setPrefRowCount(1);

        Slider slider = new Slider(1, 50, 3);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Label lineColor = new Label("Line Color");
        Label fillColor = new Label("Fill Color");
        Label lineWidth = new Label("3.0");

        Button fillBtn = new Button("Fill");
        Button clearBtn = new Button("Clear");
        Button undoBtn = new Button("Undo");
        Button redoBtn = new Button("Redo");
        Button saveBtn = new Button("Save");
        Button openBtn = new Button("Open");
        Button exitBtn = new Button("Exit");

        Button[] basicArr = { fillBtn, undoBtn, redoBtn, clearBtn, saveBtn, openBtn, exitBtn };

        for (Button btn : basicArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #1688f0;");
        }
        exitBtn.setStyle("-fx-background-color: #f11b71;");

        VBox buttons = new VBox(10);
        buttons.getChildren().addAll(drawBtn, rubberBtn, lineBtn, rectBtn, circleBtn, ellipseBtn,
                textBtn, text, lineColor, cpLine, fillColor, cpFill, lineWidth, slider, fillBtn, clearBtn, undoBtn,
                redoBtn, openBtn, saveBtn, exitBtn);
        buttons.setPadding(new Insets(5));
        buttons.setStyle("-fx-background-color: #999");
        buttons.setPrefWidth(100);

        // Draw Canvas
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);

        Line line = new Line();
        Rectangle rectangle = new Rectangle();
        Circle circle = new Circle();
        Ellipse ellipse = new Ellipse();

        canvas.setOnMousePressed(e -> {
            if (drawBtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            } else if (rubberBtn.isSelected()) {
                double rubberWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - rubberWidth / 2, e.getY() - rubberWidth / 2, rubberWidth, rubberWidth);
            } else if (lineBtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());
            } else if (rectBtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                rectangle.setX(e.getX());
                rectangle.setY(e.getY());
            } else if (circleBtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                circle.setCenterX(e.getX());
                circle.setCenterY(e.getY());
            } else if (ellipseBtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                ellipse.setCenterX(e.getX());
                ellipse.setCenterY(e.getY());
            } else if (textBtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.fillText(text.getText(), e.getX(), e.getY());
                gc.strokeText(text.getText(), e.getX(), e.getY());
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (drawBtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            } else if (rubberBtn.isSelected()) {
                double rubberWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - rubberWidth / 2, e.getY() - rubberWidth / 2, rubberWidth, rubberWidth);
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (drawBtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            } else if (rubberBtn.isSelected()) {
                double rubberWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - rubberWidth / 2, e.getY() - rubberWidth / 2, rubberWidth, rubberWidth);
            } else if (lineBtn.isSelected()) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            } else if (rectBtn.isSelected()) {
                rectangle.setWidth(Math.abs((e.getX() - rectangle.getX())));
                rectangle.setHeight(Math.abs((e.getY() - rectangle.getY())));
                // rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if (rectangle.getX() > e.getX()) {
                    rectangle.setX(e.getX());
                }
                // rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if (rectangle.getY() > e.getY()) {
                    rectangle.setY(e.getY());
                }

                gc.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

                undoHistory.push(
                        new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));

            } else if (circleBtn.isSelected()) {
                circle.setRadius(
                        (Math.abs(e.getX() - circle.getCenterX()) + Math.abs(e.getY() - circle.getCenterY())) / 2);

                if (circle.getCenterX() > e.getX()) {
                    circle.setCenterX(e.getX());
                }
                if (circle.getCenterY() > e.getY()) {
                    circle.setCenterY(e.getY());
                }

                gc.fillOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());
                gc.strokeOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());

                undoHistory.push(new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius()));
            } else if (ellipseBtn.isSelected()) {
                ellipse.setRadiusX(Math.abs(e.getX() - ellipse.getCenterX()));
                ellipse.setRadiusY(Math.abs(e.getY() - ellipse.getCenterY()));

                if (ellipse.getCenterX() > e.getX()) {
                    ellipse.setCenterX(e.getX());
                }
                if (ellipse.getCenterY() > e.getY()) {
                    ellipse.setCenterY(e.getY());
                }

                gc.strokeOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                gc.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());

                undoHistory
                        .push(new Ellipse(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(),
                                ellipse.getRadiusY()));
            }
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(gc.getFill());
            lastUndo.setStroke(gc.getStroke());
            lastUndo.setStrokeWidth(gc.getLineWidth());

        });
        // color picker
        cpLine.setOnAction(e -> {
            gc.setStroke(cpLine.getValue());
        });
        cpFill.setOnAction(e -> {
            gc.setFill(cpFill.getValue());
        });

        // slider
        slider.valueProperty().addListener(e -> {
            double width = slider.getValue();
            if (textBtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                lineWidth.setText(String.format("%.1f", width));
                return;
            }
            lineWidth.setText(String.format("%.1f", width));
            gc.setLineWidth(width);
        });

        setupFill(fillBtn, gc, cpFill);
        setupUndo(undoHistory, redoHistory, undoBtn, gc);
        setupRedo(undoHistory, redoHistory, redoBtn, gc);
        setupClear(undoHistory, redoHistory, clearBtn, gc);
        setupOpen(primaryStage, openBtn, gc);
        setupSave(primaryStage, saveBtn, canvas);
        setupExit(primaryStage, exitBtn);

        BorderPane pane = new BorderPane();
        pane.setLeft(buttons);
        pane.setCenter(canvas);

        Scene scene = new Scene(pane, 1400, 760);

        primaryStage.setTitle("Painter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
