package cardsagainsthumanity.cards;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class Mazzo {

    private final TypeCard typeCard;
    private ArrayList<Card> mazzo;

    public Mazzo(TypeCard typeCard) {
        this.typeCard = typeCard;
        try {
            String path = Mazzo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            switch (typeCard){
                case WHITE: mazzo = new CardLoader(path + "\\CAH\\WhiteCards.txt", TypeCard.WHITE).getBuffer(); break;
                case BLACK: mazzo = new CardLoader(path + "\\CAH\\BlackCards.txt", TypeCard.BLACK).getBuffer(); break;
            }
        } catch (URISyntaxException e) {

        }
        switch (typeCard){
            case WHITE: mazzo = new CardLoader("CAH\\WhiteCards.txt", TypeCard.WHITE).getBuffer(); break;
            case BLACK: mazzo = new CardLoader("CAH\\BlackCards.txt", TypeCard.BLACK).getBuffer(); break;
        }
    }


    public Card getCard(){
        Random r = new Random();
        Card c = mazzo.remove(r.nextInt(mazzo.size()));
        mazzo.trimToSize();
        return c;
    }
}
