import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MdiTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MdiArea mdiArea = new MdiArea();

        mdiArea.getChildren().add(new MdiSubWindow());

        primaryStage.setScene(new Scene(mdiArea));
        primaryStage.show();
    }
}
