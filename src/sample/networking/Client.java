package sample.networking;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
public class Client {

    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Thread listener;
    private String user;
    private ProtocolClient protocolClient;

    public Client(Socket socket, String user) {
        this.socket = socket;
        this.user = user;
        try {
            this.reader = new DataInputStream(socket.getInputStream());
            this.writer = new DataOutputStream(socket.getOutputStream());
            this.protocolClient = new ProtocolClient(writer, reader, user);
            this.protocolClient.send("USERNAME#"+ this.user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = new Thread(new Listener());
        listener.start();
    }

    public void disconnect() {
        try {
            writer.writeUTF("DISCONNECTED#" + user);
            listener.interrupt();
            reader.close();
            writer.close();
            socket.close();
        } catch (SocketException e) {
            listener.interrupt();

            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public ProtocolClient getProtocolClient() {
        return protocolClient;
    }

    private class Listener implements Runnable{
        @Override
        public void run() {
            boolean active = true;
            while(active){
                try {
                    protocolClient.execute(reader.readUTF());
                } catch (SocketException e) {
                    disconnect();
                    active = false;
                } catch (IOException e) {

                }
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
