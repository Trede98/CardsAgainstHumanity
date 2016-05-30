package cardsagainsthumanity.interfaccie;

import cardsagainsthumanity.ConnectionExceptionClose;
import cardsagainsthumanity.cards.Mazzo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import cardsagainsthumanity.networking.Client;

import java.net.Socket;

public class InterfacciaClient {

    private ControllerClient controllerClient; //Controller dello stage
    private Client client; //Client
    private String user; //Username
    private FXMLLoader fxmlLoader; //Loader
    private Stage s; //Stage

    /**
     * Costruttore principale
     * @param ip ip a cui collegarsi
     * @param port la porta a cui collegarsi
     * @param user lo username da utilizzare
     * @throws ConnectionExceptionClose chiude lo stage se la connessione è rifiutata
     */
    public InterfacciaClient(String ip, int port, String user) throws ConnectionExceptionClose {
        s = new Stage();
        this.user = user;
        try {
            start(s);
            client = new Client(new Socket(ip, port), user);
            controllerClient = fxmlLoader.<ControllerClient>getController();
            initInterface();
        } catch (Exception e) {
            throw new ConnectionExceptionClose(s, "Connection Refused");
        }

    }

    /**
     * Fa partire lo stage
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader(getClass().getResource("interfacciaClient.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Client " + user);
        primaryStage.setScene(new Scene(root));
        String path = Mazzo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "CAH/icon.png";
        primaryStage.getIcons().add(new Image("file:"+path));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> client.disconnect());
    }


    /**
     * Imposta i valori necessari al clien e al protocollo.
     */
    private void initInterface(){
        controllerClient.setProtocolClient(client.getProtocolClient());
        client.getProtocolClient().setController(controllerClient);
    }

}
