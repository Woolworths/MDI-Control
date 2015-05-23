import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MdiSubWindow extends Control {
    public static final String DEFAULT_STYLE = "default.css";

    public static final String DEFAULT_STYLE_CLASS = "window";

    private final Property<Pane> contentPaneProperty = new SimpleObjectProperty<>();

    private StringProperty titleProperty = new SimpleStringProperty("Title");



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

        createDefaultSkin();
    }

    @Override
    public String getUserAgentStylesheet() {
        return this.getClass().getResource(DEFAULT_STYLE).toExternalForm();
    }

    public String getTitle() {
        return titleProperty.get();
    }

    public void setTitle(String titleProperty) {
        this.titleProperty.set(titleProperty);
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
}
