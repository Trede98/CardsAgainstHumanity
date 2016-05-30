package cardsagainsthumanity;

import javafx.stage.Stage;

import java.net.ConnectException;

/**
 * Created by Giovanni on 28/05/16.
 */
public class ConnectionExceptionClose extends ConnectException {

    /**
     * Costruttore eccezione che chiude lo stage
     * @param s stage da chiudere
     * @param m messaggio di errore
     */
    public ConnectionExceptionClose(Stage s, String m){
        super(m);
        s.close();
    }

}
