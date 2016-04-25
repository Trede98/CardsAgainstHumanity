package sample.interfaccie;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.networking.Client;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Giovanni on 23/04/2016.
 */
public class InterfacciaClient {

    private ControllerClient controllerClient;
    private Client client;
    private String user;
    private FXMLLoader fxmlLoader;

    public InterfacciaClient(String ip, int port, String user) throws ConnectException {
        Stage s = new Stage();
        try {
            this.user = user;
            start(s);
            try{
                client = new Client(new Socket(ip, port), user);
                controllerClient = fxmlLoader.<ControllerClient>getController();
                initInterface();
            } catch (ConnectException e){
                s.close();
                throw new ConnectException("There's no server for this IP");
            }
        } catch (Exception e) {

        }
    }

    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader(getClass().getResource("interfacciaClient.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Client " + user);
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
        controllerClient.setProtocolClient(client.getProtocolClient());
        client.getProtocolClient().setController(controllerClient);
    }

}
