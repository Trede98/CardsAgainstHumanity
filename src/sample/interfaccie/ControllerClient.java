package sample.interfaccie;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sample.networking.ProtocolClient;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ControllerClient implements ControllerInterfaccie, Initializable {

    @FXML
    JFXListView<Pane> yourCards;

    @FXML
    JFXListView<Pane> selectedCards;

    @FXML
    JFXListView<Label> punti;

    @FXML
    AnchorPane pane;

    @FXML
    StackPane blackCard;

    @FXML
    Label labelPane;

    @FXML
    Label label;

    @FXML
    JFXSpinner spinner;

    @FXML
    JFXButton btnConfirm;

    @FXML
    Label labelCzar;

    private ProtocolClient protocolClient;

    private HashMap<String, Label> labelHashMap;
    private HashMap<String, Integer> pointsHashMap;
    private String cards = "";
    private int numCards;
    private boolean cardCzar = false;
    private boolean gameStarted = false;


    @FXML
    public void confirmCard(){

        if(cardCzar){
            Platform.runLater(() -> {
                WhiteMultipleHBox selected = (WhiteMultipleHBox) selectedCards.getSelectionModel().getSelectedItem();
                Iterator it = selected.getChildren().iterator();
                String cards1 = "";
                while (it.hasNext()){
                    Label l = (Label) it.next();
                    cards1 = cards1 + l.getText() + "@@";
                }
                protocolClient.send("CARDWINNING#"+ cards1);
                cardCzar = false;
                numCards = 0;
            });
        }else {

        Platform.runLater(() -> {
            if(numCards > 0){
                WhiteCardPane selected = (WhiteCardPane) yourCards.getSelectionModel().getSelectedItem();
                cards = cards + selected.getPhrase() + "@@";
                yourCards.getItems().remove(selected);
                numCards--;
                if(numCards == 0){
                    protocolClient.send("CARDSELECTED#"+ cards + "#" + protocolClient.getUser());
                    cards = "";
                }
            }
        });
        }
    }


    void setProtocolClient(ProtocolClient protocolClient) {
        this.protocolClient = protocolClient;
    }

    @Override
    public void firstRound(){
        gameStarted = true;
        Platform.runLater(() -> {
            spinner.setVisible(false);
            spinner.setDisable(true);
            label.setVisible(false);
            label.setDisable(true);
            blackCard.setVisible(true);
            btnConfirm.setVisible(true);
            btnConfirm.setDisable(false);
        });
    }

    @Override
    public void addPlayer(String user) {
        Platform.runLater(() -> {
            pointsHashMap.put(user, 0);
            labelHashMap.put(user, new Label(user + ": 0 awesome points"));
            punti.getItems().add(labelHashMap.get(user));
        });
    }


    @Override
    public void removePlayer(String user){
        Platform.runLater(() -> punti.getItems().remove(labelHashMap.get(user)));
    }

    @Override
    public void pointPlus(String user, String card) {
        Platform.runLater(() -> {
            int punti1 = pointsHashMap.get(user) + 1;
            Label l = labelHashMap.get(user);
            l.setText(user + ": " + punti1 + " awesome points");
            pointsHashMap.put(user, punti1);
            labelCzar.setText(user + " WON THE ROUND");
            labelCzar.setVisible(true);

            Iterator it = selectedCards.getItems().iterator();
            while (it.hasNext()){
                WhiteMultipleHBox wmb = (WhiteMultipleHBox) it.next();
                if(!card.equals(wmb.getAllPhrase())){
                    it.remove();
                }
            }
        });
    }

    @Override
    public void addWhiteCard(String phrase) {
        Platform.runLater(() -> {
            if(yourCards.getItems().size() < 10)
            yourCards.getItems().add(new WhiteCardPane(blackCard, labelPane, phrase));
        });
    }

    @Override
    public void changeBlackText(String text){
        Platform.runLater(() -> labelPane.setText(text));
        String s = text;
        numCards = 0;
        if(!s.contains("_____")){
            numCards = 1;
        } else {
            while (s.contains("_____")){
                s = s.substring(s.indexOf("_____")+1, s.length());
                numCards++;
            }
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelHashMap = new HashMap<>();
        pointsHashMap = new HashMap<>();
    }

    public void setCardCzar(boolean cardCzar) {
        this.cardCzar = cardCzar;
    }

    @Override
    public void setNewRound() {
        Platform.runLater(() -> {
            if(cardCzar){
                labelCzar.setText("YOU ARE THE CARD CZAR");
                btnConfirm.setDisable(true);
                btnConfirm.setVisible(false);
                yourCards.setDisable(true);
                yourCards.setVisible(false);
                labelCzar.setVisible(true);
            } else {
                yourCards.setDisable(false);
                yourCards.setVisible(true);
                btnConfirm.setDisable(false);
                btnConfirm.setVisible(true);
                labelCzar.setVisible(false);
            }
            selectedCards.getItems().clear();
        });
    }

    @Override
    public void startSelection(String cards){
        String[] all = cards.split("!!");
        for (String anAll : all) {
            WhiteMultipleHBox whb = new WhiteMultipleHBox(labelPane.getFont(), anAll, blackCard.getWidth(), blackCard.getHeight());
            Platform.runLater(() -> selectedCards.getItems().add(whb));
        }

        Platform.runLater(() -> {
            if(!cardCzar){
                yourCards.setDisable(true);
                yourCards.setVisible(false);
                btnConfirm.setDisable(true);
                btnConfirm.setVisible(false);
            } else {
                btnConfirm.setDisable(false);
                btnConfirm.setVisible(true);
            }
        });
    }

    @Override
    public void resetGame() {
        gameStarted = false;
        Platform.runLater(() -> {
            yourCards.getItems().clear();
            selectedCards.getItems().clear();
            spinner.setVisible(true);
            spinner.setDisable(false);
            label.setVisible(true);
            label.setDisable(false);
            blackCard.setVisible(false);
            btnConfirm.setVisible(false);
            btnConfirm.setDisable(true);
            labelPane.setText("");
            labelCzar.setVisible(false);
        });
    }

    @Override
    public void endGame(String user) {
        gameStarted = false;
        Platform.runLater(() -> {
            btnConfirm.setVisible(false);
            btnConfirm.setDisable(true);
            labelCzar.setText(user + " WON THE GAME");
            labelCzar.setVisible(true);
        });

    }

    @Override
    public void deleteAll(){
        Platform.runLater(() -> punti.getItems().clear());

    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
