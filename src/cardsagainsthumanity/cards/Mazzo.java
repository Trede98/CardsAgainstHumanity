package cardsagainsthumanity.cards;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class Mazzo {

    private final TypeCard typeCard;
    private ArrayList<Card> mazzo;
    private DatabaseConnector databaseConnector;

    public Mazzo(TypeCard typeCard) {
        this.typeCard = typeCard;
        try {
            String path = Mazzo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            databaseConnector = new DatabaseConnector(path + "\\CAH\\cards.db", "", "");
            switch (typeCard){
                case WHITE: mazzo = new CardLoader(databaseConnector, TypeCard.WHITE).getBuffer(); break;
                case BLACK: mazzo = new CardLoader(databaseConnector, TypeCard.BLACK).getBuffer(); break;
            }
        } catch (URISyntaxException e) {

        }
    }


    public Card getCard(){
        Random r = new Random();
        Card c = mazzo.remove(r.nextInt(mazzo.size()));
        mazzo.trimToSize();
        return c;
    }
}
