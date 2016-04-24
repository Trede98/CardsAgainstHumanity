package sample.networking;

import sample.Controller;
import sample.interfaccie.ControllerClient;
import sample.interfaccie.ControllerHost;
import sample.interfaccie.ControllerInterfaccie;
import sample.interfaccie.InterfacciaHost;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ProtocolClient {


    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String user;
    private ControllerInterfaccie controller;


    public ProtocolClient(DataOutputStream dataOutputStream, DataInputStream dataInputStream, String user) {
        this.inputStream = dataInputStream;
        this.outputStream = dataOutputStream;
        this.user = user;
    }




    public void execute(String frame){
        System.out.println(user + "----------" + frame);
        String[] elements = frame.split("#");
        switch (elements[0]){
            case "STARTGAME":
                controller.firstRound();
                break;
            case "DELETEALL":
                controller.deleteAll();
                break;
            case "CHANGEBLACKCARD":
                controller.changeBlackText(elements[1]);
                break;
            case "POINTPLUS":
                controller.pointPlus(elements[1], elements[2]);
                break;
            case "ADDWHITECARD":
                controller.addWhiteCard(elements[1]);
                break;
            case "SELECTING":
                controller.startSelection(elements[1]);
                break;
            case "CARDCZAR":
                controller.setCardCzar(true);
                break;
            case "NEWROUND":
                controller.setNewRound();
                break;
            case "ADDPLAYER":
                controller.addPlayer(elements[1]);
                break;
            case "REMOVEPLAYER":
                controller.removePlayer(elements[1]);
            case "END":
                if(elements[1].equals("VICTORY")){
                    controller.endGame(elements[2]);
                } else {
                    controller.resetGame();
                }

                break;
            default:
                System.out.println("Unvalid frame");
                break;
        }
    }

    public void send(String toSend){
        try {
            outputStream.writeUTF(toSend);
        } catch (IOException e) {

        }
    }

    public String getUser() {
        return user;
    }

    public void setController(ControllerInterfaccie controller) {
        this.controller = controller;
    }
}
