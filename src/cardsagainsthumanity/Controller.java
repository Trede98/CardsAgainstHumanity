package cardsagainsthumanity;

import cardsagainsthumanity.cards.DatabaseConnector;
import cardsagainsthumanity.cards.Mazzo;
import cardsagainsthumanity.interfaccie.InterfacciaClient;
import cardsagainsthumanity.networking.Server;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    JFXButton hostBtn; //Bottone host

    @FXML
    JFXButton connectBtn; //Bottone connect

    @FXML
    JFXTextField tfUser; //TextField user

    @FXML
    JFXTextField tfPort; //TextField porta

    @FXML
    JFXTextField tfIP; //TextField ip

    @FXML
    JFXSnackbar snack; //Snackbar

    @FXML
    BorderPane pane; //Pane

    DatabaseConnector databaseConnector; //Connessione al database

    private String lastValue; //Valore di riferimento alla riga del database


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        snack.registerSnackbarContainer(pane);
        databaseInitialization();
    }


    /**
     * Metodo che inizializza gli ultimi dati salvati dal Database
     */
    private void databaseInitialization(){
        String path;
        try {
            path = Mazzo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            databaseConnector = new DatabaseConnector(path + "\\CAH\\cards.db", "", "");

            try {
                ResultSet rs = databaseConnector.executeQuery("SELECT * FROM LastInfo");

                if(rs.next()){
                    tfUser.setText(rs.getString("Name"));
                    tfIP.setText(rs.getString("Ip"));
                    tfPort.setText(rs.getString("Port"));
                    lastValue = rs.getString("Name");
                } else {
                    lastValue = "";
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo che avvia il server.
     */
    @FXML
    public void startServer(){
        if(!checkCorrectInformation(tfPort.getText(), TypeInfo.PORT) || !checkCorrectInformation(tfUser.getText(), TypeInfo.USERNAME)){
            snack.fireEvent(new JFXSnackbar.SnackbarEvent(("You must insert a valid PORT and USERNAME")));
        } else {
            try{
                Server server = new Server(Integer.parseInt(tfPort.getText()), tfUser.getText(), 10);
                server.start();
                updateInfo();
                Stage stage = (Stage) hostBtn.getScene().getWindow();
                stage.close();
            } catch (IOException ex){
                snack.fireEvent(new JFXSnackbar.SnackbarEvent(ex.getMessage()));
                System.out.println(ex.getMessage());
            }

        }

    }

    /**
     * Metodo che avvia il client
     */
    @FXML
    public void startClient(){
        if(!checkCorrectInformation(tfPort.getText(), TypeInfo.PORT) || !checkCorrectInformation(tfUser.getText(), TypeInfo.USERNAME) || !checkCorrectInformation(tfIP.getText(), TypeInfo.IP)){
            snack.fireEvent(new JFXSnackbar.SnackbarEvent(("You must insert a valid PORT, USERNAME and ADDRESS")));
        } else {
            boolean chiudi = false;
            try {
                new InterfacciaClient(tfIP.getText(), Integer.parseInt(tfPort.getText()), tfUser.getText());
            } catch (ConnectionExceptionClose e){
                chiudi = true;
                snack.fireEvent(new JFXSnackbar.SnackbarEvent("Error joining the server"));
            } finally {
                if (!chiudi){
                    updateInfo();
                    Stage stage = (Stage) connectBtn.getScene().getWindow();
                    stage.close();
                }

            }

        }

    }


    /**
     * Metodo che aggiorna le informazioni nel database.
     */
    public void updateInfo(){
        if(!lastValue.equals("")){
            try {
                databaseConnector.noReturnQuery("DELETE FROM LastInfo WHERE Name='" + lastValue + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            String s = "INSERT INTO LastInfo (Name,Ip,Port) VALUES ('"+tfUser.getText()+ "','"+ tfIP.getText() + "','" + tfPort.getText() + "');";
            databaseConnector.noReturnQuery(s);
            databaseConnector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Controlla che i parametri siano corretti basandosi sul tipo passato.
     * @param field il parametro da controllare
     * @param type il tipo di parametro
     * @return true se il parametro è corretto
     */
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

    /**
     * Contiene i tre tipi di parametri esistenti
     */
    private enum TypeInfo{
        IP, PORT, USERNAME
    }
}


