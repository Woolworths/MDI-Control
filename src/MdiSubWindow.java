import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 *
 * Fully customizable MDI window. Construct your layout and just setRoot(layout)
 *
 * @author Andrey Zelyaev
 *
 */
public class MdiSubWindow extends Region {

    IDestroyable mDestroyable;

    private boolean RESIZE_BOTTOM;
    private boolean RESIZE_RIGHT;
    // TODO need layout logic
    private boolean RESIZE_TOP;
    private boolean RESIZE_LEFT;

    private boolean MOVABLE = true;

    private boolean isResizable = false;

    private final StringProperty windowTitleProperty = new SimpleStringProperty("Title");

    private final Property<Pane> contentPaneProperty = new SimpleObjectProperty<>();

    private double mX;
    private double mY;

    public MdiSubWindow() {
        init();
    }

    public MdiSubWindow(String title) {
        setTitle(title);
        init();
    }

    private void init() {
        /*BorderPane pane = makeDefaultBorderPane(title);
        pane.setCenter(content);
        getChildren().add(pane);*/
        // all focusable
        makeFocusable(this, true);
        setResizable(true, 4, true);
    }

    /**
     * Enable mt for window
     *
     * @param rotate
     * @param pinchZoom
     */
    public void setMultitouch(boolean rotate, boolean pinchZoom) {
        initMultitouch(rotate, pinchZoom);
    }

    private void initMultitouch(boolean rotate, boolean pinchZoom) {
        setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if (pinchZoom) {
                    setScaleX(getScaleX() * event.getZoomFactor());
                    setScaleY(getScaleY() * event.getZoomFactor());
                    event.consume();
                }
            }
        });
        setOnRotate(new EventHandler<RotateEvent>() {
            @Override
            public void handle(RotateEvent event) {
                if (rotate) {
                    setRotate(getRotate() + event.getAngle());
                    event.consume();
                }
            }
        });
    }

    /**
     * Allow to resize window
     *
     * @param isResizable
     * @param mouseBorder
     *            in pixels
     * @param changeCursorOnMove
     *            change cursor on mouse move above
     */
    public void setResizable(boolean isResizable, double mouseBorder,
                             boolean changeCursorOnMove) {
        this.isResizable = isResizable;
        initResizeLogic(mouseBorder, changeCursorOnMove);
    }

    public void setXY(double x, double y) {
        mX = x;
        mY = y;
    }

    /**
     * Set node for window
     *
     * @param node
     */
    public void setRoot(Node node) {
        getChildren().add(node);
    }

    /**
     * Make node draggable by mouse
     *
     * @param what
     * @param isMovable
     */
    public void makeDragable(Node what, boolean isMovable) {
        final Delta dragDelta = new Delta();
        what.setOnMousePressed(mouseEvent -> {
            if (isMovable && MOVABLE) {
                dragDelta.x = getLayoutX() - mouseEvent.getScreenX();
                dragDelta.y = getLayoutY() - mouseEvent.getScreenY();
            }
            toFront();
        });
        what.setOnMouseDragged(mouseEvent -> {
            if (isMovable && MOVABLE) {
                setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
                setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
    }

    /**
     * Make node focusable(bring to front) by mouse
     *
     * @param what
     * @param isFocusable
     */
    public void makeFocusable(Node what, boolean isFocusable) {

        what.setOnMouseClicked(mouseEvent -> {
            toFront();
        });

    }

    /**
     * set destroyable if you need dispose specific resources
     *
     * @param destroyable
     *            call inside {@link DefaultWindowCloseEventHandler}
     */
    public void setDestroyable(IDestroyable destroyable) {
        this.mDestroyable = destroyable;
    }

    // TODO incapsulate default customization
    private BorderPane makeDefaultBorderPane(String title) {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-border-width: 1; -fx-border-color: burlywood");
        pane.setTop(buildDefaultTitleBar(title));
        this.setLayoutX(mX);
        this.setLayoutY(mY);
        return pane;
    }

    private Node buildDefaultTitleBar(String title) {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: burlywood; -fx-padding: 5");
        Label label = new Label(title);
        label.setStyle("-fx-text-fill: midnightblue;");
        pane.setLeft(label);

        Button closeButton = new Button("X");
        closeButton.setOnAction(new DefaultWindowCloseEventHandler(this));
        pane.setRight(closeButton);
        makeDragable(pane, true);
        return pane;
    }

    private void initResizeLogic(double mouseBorder, boolean changeCursor) {
        setOnMouseMoved(mouseEvent -> {
            if (isResizable) {

                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();

                double width = this.boundsInLocalProperty().get().getWidth();
                double height = this.boundsInLocalProperty().get().getHeight();

                if (Math.abs(mouseX - width) < mouseBorder + 3
                        && Math.abs(mouseY - height) < mouseBorder + 3) {
                    RESIZE_RIGHT = true;
                    RESIZE_BOTTOM = true;
                    if (changeCursor)
                        this.setCursor(Cursor.NW_RESIZE);
                } else if (Math.abs(mouseX - width) < mouseBorder) {

                    RESIZE_RIGHT = true;
                    RESIZE_BOTTOM = false;
                    if (changeCursor)
                        this.setCursor(Cursor.E_RESIZE);
                } else if (Math.abs(mouseY - height) < mouseBorder) {

                    RESIZE_BOTTOM = true;
                    RESIZE_RIGHT = false;
                    if (changeCursor)
                        this.setCursor(Cursor.N_RESIZE);
                } else {
                    RESIZE_BOTTOM = false;
                    RESIZE_RIGHT = false;
                    if (changeCursor)
                        this.setCursor(Cursor.DEFAULT);
                }

                if (RESIZE_BOTTOM || RESIZE_RIGHT)
                    MOVABLE = false;
                else
                    MOVABLE = true;
            }
        });
        setOnMouseDragged(mouseEvent -> {
            if (isResizable) {
                Node node = getChildren().get(0);
                if (!(node instanceof Pane))
                    throw new ClassCastException(
                            "Resizing avilable only for Pane");
                Pane n = (Pane) node;
                if (RESIZE_BOTTOM && RESIZE_RIGHT) {
                    n.setPrefSize(mouseEvent.getX(), mouseEvent.getY());
                } else if (RESIZE_RIGHT) {
                    n.setPrefWidth(mouseEvent.getX());

                } else if (RESIZE_BOTTOM) {
                    n.setPrefHeight(mouseEvent.getY());
                }
            }
        });
    }

    public static class DefaultWindowCloseEventHandler implements
            EventHandler<ActionEvent> {

        MdiSubWindow mWindow;

        public DefaultWindowCloseEventHandler(MdiSubWindow window) {
            mWindow = window;
        }

        @Override
        public void handle(ActionEvent event) {
            if (mWindow.mDestroyable != null)
                mWindow.mDestroyable.onDestroy();
            Parent p = mWindow.getParent();
            if (!(p instanceof Pane)) // TODO seems bad?
                throw new ClassCastException(
                        "You need implement setOnClose themselves");
            Pane parentPane = (Pane) p;
            parentPane.getChildren().remove(mWindow);

        }
    }

    public final String getTitle() {
        return windowTitleProperty.get();
    }

    public final void setTitle(String title) {
        windowTitleProperty.set(title);
    }

    private static class Delta {
        double x, y;
    }

}
