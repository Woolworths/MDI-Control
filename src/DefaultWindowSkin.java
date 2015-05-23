import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DefaultWindowSkin extends SkinBase<MdiSubWindow> {
    private MdiSubWindow control;

    private TitleBar titleBar;
    private Pane root = new Pane();

    public DefaultWindowSkin(MdiSubWindow w) {
        super(w);

        this.control = w;

        titleBar = new TitleBar(control);
        titleBar.setTitle("");

        init();
    }

    private void init() {
        getChildren().add(root);

        control.setPrefSize(200, 200);
        control.makeFocusable();
        control.makeDragable(titleBar);
        control.makeResizable(40);

        root.getChildren().add(titleBar);
        titleBar.setTitle(control.getTitle());
        control.titleProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
            titleBar.setTitle(newValue);
            //control.autosize();
        });

        root.getChildren().add(control.getContentPane());
        //control.getContentPane().setManaged(false);
        control.contentPaneProperty().addListener((ObservableValue<? extends Pane> ov, Pane oldValue, Pane newValue) -> {
            root.getChildren().remove(oldValue);
            root.getChildren().add(newValue);
            //newValue.setManaged(false);
        });

        titleBar.setStyle(control.getStyle());

        control.styleProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            titleBar.setStyle(t1);
        });

        titleBar.getStyleClass().setAll(control.getTitleBarStyleClass());


    }
}

class TitleBar extends BorderPane {
    MdiSubWindow control;

    private final Text label = new Text();

    public static final String DEFAULT_STYLE_CLASS = "window-titlebar";

    public TitleBar(MdiSubWindow w) {
        this.control = w;

        //setManaged(false);

        setLeft(label);

        getLabel().setText(control.getTitle());
    }

    public String getTitle() {
        return getLabel().getText();
    }

    public void setTitle(String title) {
        getLabel().setText(title);
    }

    public final Text getLabel() {
        return label;
    }
}