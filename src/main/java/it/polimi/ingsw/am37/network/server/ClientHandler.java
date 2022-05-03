package it.polimi.ingsw.am37.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class ClientHandler {

    /**
     *
     */
    MessageReceiver messageReceiver;

    /**
     *
     */
    Socket clientSocket;

    /**
     *
     */
    boolean connectedToClient;

    /**
     *
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * @return
     */
    public boolean isConnected() {
        return this.connectedToClient;
    }

    /**
     * @param messageReceiver
     */
    public void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    /**
     * @param message
     * @throws IOException
     */
    public void sendMessageToClient(Message message) throws IOException {
        messageReceiver.sendMessage(message);
    }

    /**
     * @throws IOException
     */
    public void run() throws IOException {

        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            try {
                this.disconnect();
                return;
                //Here connection with client is failed, client no long plays in the game
            } catch (IOException r) {
                this.connectedToClient = false;
                return;
                //Here connection with client is failed, client no long plays in the game
            }
        }

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        JsonObject jsonObject = null;

        while (true) {

            String json = null;
            try {
                json = dataInputStream.readUTF();
            } catch (IOException e) {
                try {
                    this.disconnect();
                    return;
                    //Here connection with client is failed, client no long plays in the game
                } catch (IOException r) {
                    this.connectedToClient = false;
                    return;
                    //Here connection with client is failed, client no long plays in the game
                }
            }

            jsonObject = JsonParser.parseString(json).getAsJsonObject();

            //controllo che tipo è e trasformo nel messaggio opportuno

            messageReceiver.onMessageReceived(message);
        }
    }

    /**
     * @throws IOException
     */
    public void disconnect() throws IOException {
        this.connectedToClient = false;
        clientSocket.close();
    }

}
