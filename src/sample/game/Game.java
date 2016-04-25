package sample.game;

import sample.cards.Mazzo;
import sample.cards.TypeCard;
import sample.networking.PointerToSend;
import sample.networking.ProtocolServer;
import sample.networking.Server.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private HashMap<String,Integer> points;
    private ArrayList<String> giocatori;
    private ProtocolServer protocolServer;
    private Mazzo mazzoBianco;
    private Mazzo mazzoNero;
    private int min;
    private int cardCzarIndex;
    private String cardCzar;
    private String blackCard;
    private boolean started;

    public Game(ProtocolServer protocolServer, int min) {
        this.protocolServer = protocolServer;
        this.min = min;
        points = new HashMap<>();
        giocatori = new ArrayList<>();
        cardCzarIndex = 0;
        cardCzar = "";

    }

    public Game() {

    }

    private void end(String type){
        started = false;
        cardCzarIndex = 0;
        cardCzar = "";
        (protocolServer.getClock()).interrupt();
        protocolServer.send("END#" + type, PointerToSend.ALL, null);
        if(type.equals("NEEDPLAYER")){
            protocolServer.reset();
        }
    }

    public void start(){
        if(protocolServer.getThreadsGroup().size() >= min){
        started = true;
        mazzoNero = new Mazzo(TypeCard.BLACK);
        mazzoBianco = new Mazzo(TypeCard.WHITE);
        protocolServer.send("STARTGAME", PointerToSend.ALL, null);
        sender(10);
        sendBlackCard();
        }
    }

    private void sender(int number){
        for (LinkedSocked s : protocolServer.getThreadsGroup()) {
            points.put(s.getUser(), 0);
            for (int i = 0; i < number; i++) {
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


    private void sendWhiteCard(String username){
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

        if(protocolServer.getThreadsGroup().size() < min && started){
            end("NEEDPLAYER");
        } else if(username.equals(cardCzar) && started){
            protocolServer.getClock().setSalta(true);
        }
    }

    public void addPlayer(String username){
        points.put(username, 0);
        giocatori.add(username);

        if(started && protocolServer.getClock().isChoosing()){
            protocolServer.send("STARTGAME", PointerToSend.USER, username);
            for (int i = 0; i < 10; i++){
                sendWhiteCard(username);
            }
            protocolServer.send("CHANGEBLACKCARD#"+ blackCard, PointerToSend.USER, username);
            protocolServer.send("NEWROUND", PointerToSend.USER, username);
        } else if(started){
            protocolServer.send("STARTGAME", PointerToSend.USER, username);
            for (int i = 0; i < 10; i++){
                sendWhiteCard(username);
            }
        }
    }

    private boolean checkVictory(){
        if(points.containsValue(10)){
            for (Object user : points.keySet()) {
                if (points.get(user) == 10) {
                    end("VICTORY#" + user);
                }
            }
        }
        return false;
    }

    public String nextCardCzar(){
        if(cardCzarIndex >= giocatori.size()){
            cardCzarIndex = 0;
        }
        cardCzar = giocatori.get(cardCzarIndex);
        cardCzarIndex++;
        return cardCzar;
    }
}


