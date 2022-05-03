package it.polimi.ingsw.am37.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.am37.client.Client;

import java.io.*;
import java.net.Socket;


public class ClientSocket extends Thread {

    /**
     *
     */
    private static Client client;

    /**
     *
     */
    private static Socket socket;

    /**
     *
     */
    private static boolean connectedToServer = false;

    /**
     * @param address
     * @param port
     * @throws IOException
     */
    static public void connectToServer(String address, int port) throws IOException {
        socket = new Socket(address, port);
        connectedToServer = true;
    }

    /**
     * @return
     */
    static public boolean isConnectedToServer() {

        return connectedToServer;
    }

    /**
     * @param message
     * @throws IOException
     */
    static public void sendMessage(Message message) throws IOException {

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        Gson gson = new Gson();
        String json = gson.toJson(message);

        dataOutputStream.writeUTF(json);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    /**
     * @throws IOException
     */
    static public void disconnect() throws IOException {
        connectedToServer = false;
        socket.close();
    }

    /**
     *
     */
    public void run() {

        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            try {
                disconnect();

                return;
                //Here connection with server is failed, call a method from client's view which tell to close the game
            } catch (IOException r) {
                connectedToServer = false;

                return;
                //Here connection with server is failed, call a method from client's view which tell to close the game
            }
        }

        DataInputStream dataInputStream = new DataInputStream(inputStream);
        JsonObject jsonObject = null;

        while (true) {

            if (Client.getEnd())
                return;

            String json = null;

            try {
                json = dataInputStream.readUTF();
            } catch (IOException e) {
                try {
                    disconnect();

                    return;
                    //Here connection with server is failed, call a method from client's view which tell to close the game
                } catch (IOException r) {
                    connectedToServer = false;

                    return;
                    //Here connection with server is failed, call a method from client's view which tell to close the game
                }
            }

            jsonObject = JsonParser.parseString(json).getAsJsonObject();
            // Here starts view update based on the message


            //se jsonobject.get(type).getasstring è login distinguo due casi, se il messaggio è okay allora
            //chiamo client.setnickname col nome esatto e risveglio il thread con notifyall, altrimenti non faccio nulla


        }
    }

}
