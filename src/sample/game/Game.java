package sample.game;

import sample.cards.Mazzo;
import sample.cards.TypeCard;
import sample.networking.PointerToSend;
import sample.networking.ProtocolServer;
import sample.networking.Server;
import sample.networking.Server.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Giovanni on 21/04/2016.
 */
public class Game {

    private HashMap<String,Integer> points;
    private ArrayList<String> giocatori;
    private ProtocolServer protocolServer;
    private Mazzo mazzoBianco;
    private Mazzo mazzoNero;
    private int min;
    private int cardCzar;
    private String blackCard;

    public Game(ProtocolServer protocolServer, int min) {
        this.protocolServer = protocolServer;
        this.min = min;
        points = new HashMap<>();
        giocatori = new ArrayList<>();
        cardCzar = 0;

    }

    public Game() {

    }

    public void end(String type){
        (protocolServer.getClock()).interrupt();
        protocolServer.send("END#" + type, PointerToSend.ALL, null);
        if(type.equals("NEEDPLAYER")){
            protocolServer.reset();
        }
    }

    public void start(){
        if(protocolServer.getThreadsGroup().size() >= min){
        mazzoNero = new Mazzo(TypeCard.BLACK);
        mazzoBianco = new Mazzo(TypeCard.WHITE);
        protocolServer.send("STARTGAME", PointerToSend.ALL, null);
        sender(10);
        sendBlackCard();
        }
    }

    public void sender(int number){
        Iterator it = protocolServer.getThreadsGroup().iterator();
        while (it.hasNext()){
            LinkedSocked s = (LinkedSocked) it.next();
            points.put(s.getUser(), 0);
            for (int i = 0; i < number; i++){
                sendWhiteCard(s.getUser());
            }
        }
    }

    public void refillWhiteCard(){
        int i = 0;
        if(!blackCard.contains("_____")){
            i = 1;
        } else {
            while (blackCard.contains("_____")) {
                blackCard = blackCard.substring(blackCard.indexOf("_____") + 1, blackCard.length());
                i++;
            }
        }
        sender(i);
    }


    public  void sendWhiteCard (String username){
        protocolServer.send("ADDWHITECARD#"+ mazzoBianco.getCard().getContent(), PointerToSend.USER ,username);
    }

    public  void sendBlackCard (){
        blackCard = mazzoNero.getCard().getContent();
        protocolServer.send("CHANGEBLACKCARD#"+ blackCard, PointerToSend.ALL, null);
    }

    public void addPoint(String username){
        int point = points.get(username) + 1;
        points.put(username, point);
        checkVictory();
    }

    public void removePlayer(String username){
        points.remove(username);
        giocatori.remove(username);
        giocatori.trimToSize();
        if(protocolServer.getThreadsGroup().size() < min) end("NEEDPLAYER");
    }

    public void addPlayer(String username){
        points.put(username, 0);
        giocatori.add(username);
    }

    public boolean checkVictory(){
        if(points.containsValue(1)){
            Iterator it = points.keySet().iterator();
            while (it.hasNext()){
                String user = (String) it.next();
                if(points.get(user) == 1){
                    end("VICTORY#" + user);
                }
            }
        }
        return false;
    }

    public String nextCardCzar(){
        if(cardCzar == giocatori.size()){
            cardCzar = 0;
        }
        String s = giocatori.get(cardCzar);
        cardCzar++;
        return s;
    }
}


