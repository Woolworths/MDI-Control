import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
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
        root.getChildren().add(titleBar);

        titleBar.setTitle(control.getTitle());
        control.titleProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
            titleBar.setTitle(newValue);
            //control.autosize();
        });
    }
}

class TitleBar extends HBox {
    MdiSubWindow control;

    private final Text label = new Text();

    public static final String DEFAULT_STYLE_CLASS = "window-titlebar";

    public TitleBar(MdiSubWindow w) {
        this.control = w;
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