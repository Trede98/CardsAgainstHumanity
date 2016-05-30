package cardsagainsthumanity;

import cardsagainsthumanity.cards.Mazzo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Cards Against Humanity");
        primaryStage.setScene(new Scene(root));
        String path = Mazzo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "CAH/icon.png";
        primaryStage.getIcons().add(new Image("file:"+path));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
