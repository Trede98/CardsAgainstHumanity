package sample.interfaccie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.networking.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerConsole implements Initializable {

    private Server server;

    @FXML
    Button btnSend;

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    public void addCommand(){
        Platform.runLater(() -> {
            send(textField.getText());
            if(textField.getText().toUpperCase().equals("EXIT")){
                try {
                    server.getServerSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server.interrupt();
            }

            textField.setText("");
        });
    }

    public void send(String text){
        Platform.runLater(() -> {
            textArea.setText(textArea.getText() + "Server: " + text + "\n");
        });
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setEditable(false);
        textArea.setWrapText(true);
    }
}
