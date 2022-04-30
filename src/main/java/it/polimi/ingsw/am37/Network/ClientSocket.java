package it.polimi.ingsw.am37.Network;

import it.polimi.ingsw.am37.Client.Client;

import java.io.*;
import java.net.Socket;


public class ClientSocket extends Thread {

    /**
     *
     */
    Client client;

    /**
     *
     */
    Socket socket;

    /**
     *
     */
    boolean connectedToServer = false;

    /**
     *
     */
    public ClientSocket(Client client, String address, int port) {
        this.client = client;
    }

    /**
     * @param address
     * @param port
     * @throws IOException
     */
    public void connectToServer(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.connectedToServer = true;
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
                this.disconnect();

                return;
                //Here connection with server is failed, call a method from client's view which tell to close the game
            } catch (IOException r) {
                this.connectedToServer = false;

                return;
                //Here connection with server is failed, call a method from client's view which tell to close the game
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
                    //Here connection with server is failed, call a method from client's view which tell to close the game
                } catch (IOException r) {
                    this.connectedToServer = false;

                    return;
                    //Here connection with server is failed, call a method from client's view which tell to close the game
                }
            }

            jsonObject = new JsonParser().parse(json).getAsJsonObject();

            // Here starts view update based on the message

        }
    }

    /**
     * @param message
     * @throws IOException
     */
    public void sendMessage(Message message) throws IOException {

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
    public void disconnect() throws IOException {
        this.connectedToServer = false;
        socket.close();
    }

}
