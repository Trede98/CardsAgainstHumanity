package cardsagainsthumanity.networking;

import cardsagainsthumanity.game.Game;
import cardsagainsthumanity.interfaccie.InterfacciaConsole;
import cardsagainsthumanity.interfaccie.InterfacciaHost;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Server  extends Thread{

    private ServerSocket serverSocket;
    private int maxNum;
    private int cont;
    private String user;
    private final int min = 3;
    private Game game;
    private ProtocolServer protocolServer;
    private ArrayList<LinkedSocked> threadGroup;

    public Server(int port, String user, int maxNum) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            this.maxNum = maxNum;
            cont = 0;
            this.user = user;
            threadGroup = new ArrayList<>();
            protocolServer = new ProtocolServer(threadGroup);
            game = new Game(protocolServer, min);
            protocolServer.setGame(game);
            InterfacciaConsole interfacciaConsole = new InterfacciaConsole(this);
            InterfacciaHost interfacciaHost = new InterfacciaHost(port, user);
            interfacciaConsole.setServer(this);
            protocolServer.setConsole(interfacciaConsole);
        } catch (IOException e) {
            throw new IOException("Error while creating the server");
        }

    }

    @Override
    public void run() {
        boolean interrupted = false;
        try {
            while (!interrupted){
                if(cont < maxNum){
                    try {
                        Socket socket = serverSocket.accept();
                        LinkedSocked t = new LinkedSocked(socket);
                        t.start();
                        threadGroup.add(t);
                        protocolServer.setThreadsGroup(threadGroup);
                        cont++;
                    } catch (IOException e) {
                    }
                }
                    TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e){
            interrupted = true;
            for (LinkedSocked s : threadGroup){
                game.removePlayer(s.getUser());
                s.interrupt();
            }
            threadGroup.clear();
            if(protocolServer.getClock().isAlive()) {
                protocolServer.getClock().interrupt();
            }
        }

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public class LinkedSocked extends Thread {

        private DataInputStream reader;
        private DataOutputStream writer;
        private String user;

        public LinkedSocked(Socket socket) {
            this.user = null;
            try {
                this.reader = new DataInputStream(socket.getInputStream());
                this.writer = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean linked = true;
            while (linked) {

                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    linked = false;
                }

                try {
                    String line = reader.readUTF();
                    if (user == null && line.startsWith("USERNAME#")) {
                        user = line.substring(9);
                        this.setName(user);
                        protocolServer.getGame().addPlayer(user);
                        protocolServer.sendName(user);
                        protocolServer.send("ADDPLAYER#"+ user, PointerToSend.ALL, null);
                    } else {
                        protocolServer.execute(line);
                    }
                } catch (SocketException e) {
                    linked = false;
                } catch (EOFException e){
                    linked = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            threadGroup.remove(this);
            protocolServer.setThreadsGroup(threadGroup);
        }

        public DataOutputStream getWriter() {
            return writer;
        }

        public String getUser() {
            return user;
        }


    }
}

