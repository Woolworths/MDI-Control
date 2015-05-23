import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class MdiSubWindow extends Control {
    public static final String DEFAULT_STYLE = "default.css";

    public static final String DEFAULT_STYLE_CLASS = "window";

    private final StringProperty titleBarStyleClassProperty =
            new SimpleStringProperty("window-titlebar");

    private final Property<Pane> contentPaneProperty = new SimpleObjectProperty<>();

    private StringProperty titleProperty = new SimpleStringProperty("Title");

    private boolean RESIZE_BOTTOM;
    private boolean RESIZE_RIGHT;

    public MdiSubWindow() {
        init();
    }

    public MdiSubWindow(String title) {
        setTitle(title);
        init();
    }

    private void init() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setContentPane(new StackPane());
    }

    public void makeFocusable() {
        this.setOnMouseClicked(mouseEvent -> {
            toFront();
        });
    }

    public void makeDragable(Node what) {
        final Delta dragDelta = new Delta();
        what.setOnMousePressed(mouseEvent -> {
            dragDelta.x = getLayoutX() - mouseEvent.getScreenX();
            dragDelta.y = getLayoutY() - mouseEvent.getScreenY();
            //also bring to front when moving
            toFront();
        });
        what.setOnMouseDragged(mouseEvent -> {
            setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
            setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
        });
    }

    public void makeResizable(double mouseBorderWidth) {
        this.setOnMouseMoved(mouseEvent -> {
            //local window's coordiantes
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            //window size
            double width = this.boundsInLocalProperty().get().getWidth();
            double height = this.boundsInLocalProperty().get().getHeight();
            //if we on the edge, change state and cursor
            if (Math.abs(mouseX - width) < mouseBorderWidth
                    && Math.abs(mouseY - height) < mouseBorderWidth) {
                RESIZE_RIGHT = true;
                RESIZE_BOTTOM = true;
                this.setCursor(Cursor.NW_RESIZE);
            } else {
                RESIZE_BOTTOM = false;
                RESIZE_RIGHT = false;
                this.setCursor(Cursor.DEFAULT);
            }

        });
        this.setOnMouseDragged(mouseEvent -> {
            //resize root
            Region region = (Region) getChildren().get(0);
            //resize logic depends on state
            if (RESIZE_BOTTOM && RESIZE_RIGHT) {
                region.setPrefSize(mouseEvent.getX(), mouseEvent.getY());
            } else if (RESIZE_RIGHT) {
                region.setPrefWidth(mouseEvent.getX());
            } else if (RESIZE_BOTTOM) {
                region.setPrefHeight(mouseEvent.getY());
            }
        });
    }

    @Override
    public String getUserAgentStylesheet() {
        return this.getClass().getResource(DEFAULT_STYLE).toExternalForm();
    }

    public String getTitle() {
        return titleProperty.get();
    }

    public void setTitle(String title) {
        this.titleProperty.set(title);
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public Pane getContentPane() {
        return contentPaneProperty.getValue();
    }

    public void setContentPane(Pane contentPane) {
        contentPaneProperty.setValue(contentPane);
    }

    public Property<Pane> contentPaneProperty() {
        return contentPaneProperty;
    }

    public String getTitleBarStyleClass() {
        return titleBarStyleClassProperty.get();
    }

    public void setTitleBarStyleClass(String name) {
        titleBarStyleClassProperty.set(name);
    }

    public StringProperty titleBarStyleClassProperty() {
        return titleBarStyleClassProperty;
    }

    private static class Delta {
        double x, y;
    }
}