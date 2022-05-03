package it.polimi.ingsw.am37.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.am37.client.Client;
import java.io.*;
import java.net.Socket;


public class ClientSocket {

    /**
     * Client associated to its net-features class
     */
    private static Client client;

    /**
     * Socket used to connect
     */
    private static Socket socket;

    /**
     * Boolean which represents the current state of client's connection
     */
    private static boolean connectedToServer = false;

    /**
     * Input stream
     */
    private static InputStream inputStream;

    /**
     * Output stream
     */
    private static OutputStream outputStream;

    /**
     * DataOutput stream used for sending messages
     */
    private static DataOutputStream dataOutputStream;

    /**
     * DataInput stream used for reading messages
     */
    private static DataInputStream dataInputStream;

    /**
     * @param address Server's IP
     * @param port    Server's port
     * @throws IOException When connection is failed
     */
    static public void connectToServer(String address, int port) throws IOException {

        socket = new Socket(address, port);
        connectedToServer = true;
    }

    /**
     * @return Current state of client's connection
     */
    static public boolean isConnectedToServer() {

        return connectedToServer;
    }

    /**
     * Tries to close socket and input/output stream and set connectedToServer to false
     */
    static public void disconnect() {

        connectedToServer = false;

        /TODO metodo per chiamare la view e dire che mi sto disconnettendo

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            dataInputStream.close();
            dataOutputStream.close();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message Client's message needed to be sent to server, if error occurs client will be disconnected
     */
    static public void sendMessage(Message message) throws IOException {

        /TODO conversione messaggio a stringa json

        dataOutputStream.writeUTF(json);
        dataOutputStream.flush();
    }

    /**
     * Create socket's OutputStream
     */
    static public void setOutput() {

        try {
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create socket's InputStream
     */
    static public void setInput() {

        try {
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
        } catch (IOException e) {
            disconnect();
        }
    }



}
