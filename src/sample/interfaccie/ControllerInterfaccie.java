package sample.interfaccie;

/**
 * Created by Giovanni on 24/04/2016.
 */
public interface ControllerInterfaccie {

    void changeBlackText(String text);

    void firstRound();

    void addPlayer(String user);

    void removePlayer(String user);

    void pointPlus(String user, String card);

    void addWhiteCard(String phrase);

    void setCardCzar(boolean cardCzar);

    void setNewRound();

    void startSelection(String cards);

    void resetGame();

    void endGame(String user);

    void deleteAll();

    boolean isGameStarted();
}
