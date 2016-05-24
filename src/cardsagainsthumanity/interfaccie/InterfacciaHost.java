package cardsagainsthumanity.interfaccie;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import cardsagainsthumanity.networking.Client;

import java.net.Socket;

/**
 * Created by Giovanni on 22/04/2016.
 */
public class InterfacciaHost {


    private ControllerHost controllerHost;
    private Client client;
    private String user;
    private FXMLLoader fxmlLoader;

    public InterfacciaHost(int port, String user) {
        try {
            this.user = user;
            start(new Stage());
            client = new Client(new Socket("127.0.0.1", port), user);
            controllerHost = fxmlLoader.<ControllerHost>getController();
            initInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader(getClass().getResource("interfacciaHost.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Client Host " + user);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.disconnect();
            }
        });
    }

    private void initInterface(){
        controllerHost.setProtocolClient(client.getProtocolClient());
        client.getProtocolClient().setController(controllerHost);
    }
}
