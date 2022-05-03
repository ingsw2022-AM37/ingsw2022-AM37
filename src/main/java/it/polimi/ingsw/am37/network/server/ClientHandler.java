package it.polimi.ingsw.am37.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.am37.network.MessageReceiver;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    /**
     * It's the recipient of client's messages: it can be the "central" server or a lobby
     */
    private MessageReceiver messageReceiver;

    /**
     * It's the socket connected to the handler
     */
    private Socket clientSocket;

    /**
     * A boolean value which represents the state of connection
     */
    private boolean connectedToClient = true;

    /**
     * Default Constructor
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * @return If the clientHandler is connected to its client
     */
    public boolean isConnectedToClient() {
        return this.connectedToClient;
    }

    /**
     * @param messageReceiver Actual messageReceiver
     */
    public void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    /**
     * @param message To be sent to its client
     */
    public void sendMessageToClient(Message message) throws IOException {
        messageReceiver.sendMessage(message);
    }

    /**
     * Additional thread used to listen messages from the client
     */
    @Override
    public void run() {

        if (!connectedToClient)
            return;

        InputStream inputStream;
        JsonObject jsonObject;
        String json;

        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            disconnect();
            return;
        }

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        while (connectedToClient) {

            try {
                json = dataInputStream.readUTF();
            } catch (IOException e) {
                disconnect();
                return;
            }

            jsonObject = JsonParser.parseString(json).getAsJsonObject();

            /TODO
            //controllo che tipo è e trasformo nel messaggio opportuno

            messageReceiver.onMessageReceived(message);
        }
    }

    /**
     * Set there isn't connection with client and tries to close the socket
     */
    public void disconnect() {

        this.connectedToClient = false;

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
