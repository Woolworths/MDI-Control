import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

public class MdiTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MdiArea mdiArea = new MdiArea();

        MdiSubWindow w1 = new MdiSubWindow();
        w1.setTitle("mad or/....");
        w1.setContentPane(new StackPane(new Label("OKOK")));
        w1.setContentPane(new StackPane(new Button("fds")));

        InternalWindow w2 = new InternalWindow(new Button("dsa"), "Window 2", 12.0, 12.0);

        Window w3 = new Window();
        w3.setTitle("hello");
        w3.boundsListenerEnabledProperty().setValue(false);

        //mdiArea.getChildren().addAll(w1, w2, w3);
        mdiArea.addWindow(w1);

        primaryStage.setScene(new Scene(mdiArea));
        primaryStage.show();
    }
}
