package cardsagainsthumanity;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import cardsagainsthumanity.interfaccie.InterfacciaClient;
import cardsagainsthumanity.networking.Server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    JFXButton hostBtn;

    @FXML
    JFXTextField tfUser;

    @FXML
    JFXTextField tfPort;

    @FXML
    JFXTextField tfIP;

    @FXML
    JFXButton connectBtn;

    @FXML
    JFXSnackbar snack;

    @FXML
    AnchorPane pane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        snack.registerSnackbarContainer(pane);
    }


    @FXML
    public void startServer(){
        if(!checkCorrectInformation(tfPort.getText(), TypeInfo.PORT) || !checkCorrectInformation(tfUser.getText(), TypeInfo.USERNAME)){
            snack.fireEvent(new JFXSnackbar.SnackbarEvent(("You must insert a valid PORT and USERNAME")));
        } else {
            try{
                Server server = new Server(Integer.parseInt(tfPort.getText()), tfUser.getText(), 10);
                server.start();
                Stage stage = (Stage) hostBtn.getScene().getWindow();
                stage.close();
            } catch (IOException ex){
                snack.fireEvent(new JFXSnackbar.SnackbarEvent(ex.getMessage()));
            }

        }

    }

    @FXML
    public void startClient(){
        if(!checkCorrectInformation(tfPort.getText(), TypeInfo.PORT) || !checkCorrectInformation(tfUser.getText(), TypeInfo.USERNAME) || !checkCorrectInformation(tfIP.getText(), TypeInfo.IP)){
            snack.fireEvent(new JFXSnackbar.SnackbarEvent(("You must insert a valid PORT, USERNAME and ADDRESS")));
        } else {
            try {
                InterfacciaClient interfacciaClient = new InterfacciaClient(tfIP.getText(), Integer.parseInt(tfPort.getText()), tfUser.getText());
                Stage stage = (Stage) connectBtn.getScene().getWindow();
                stage.close();
            } catch (ConnectException e){
                snack.fireEvent(new JFXSnackbar.SnackbarEvent(e.getMessage()));
            }

        }

    }

    public boolean checkCorrectInformation(String field, TypeInfo type){
        switch (type){
            case IP:
                String[] p = field.split("\\.");
                if(p.length == 4){
                    for (int i = 0; i < p.length; i++){
                        try{
                            Integer.parseInt(p[i]);
                        }catch(Exception e) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
                break;
            case PORT:
                try{
                    Integer.parseInt(field);
                }catch(Exception e) {
                    return false;
                }
                break;
            case USERNAME:
                String[] pr = field.split(" ");
                if(pr[0].equals("")) return false;
                return pr.length != 0;
        }
        return true;
    }

    private enum TypeInfo{
        IP, PORT, USERNAME
    }
}


