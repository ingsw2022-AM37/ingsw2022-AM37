package it.polimi.ingsw.am37.network;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.message.MessageGsonBuilder;
import it.polimi.ingsw.am37.message.MessageType;
import it.polimi.ingsw.am37.message.PingMessage;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class ClientSocket implements Runnable {

    /**
     * Used for creating a loop for client's waiting after a message is received
     */
    private static boolean messageReceived = false;

    /**
     * Object used by main thread for waiting on after it sent a message
     */
    private static final Object waitObject = new Object();

    /**
     * Buffer used to store a message (different from ping) when the secondary thread reads it
     */
    private static Message messageBuffer = null;

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
     * @return waitObject used for synchronzize
     */
    static public Object getWaitObject() {
        return waitObject;
    }

    /**
     * @return if a message is received from server
     */
    static public boolean getMessageReceived() {
        return messageReceived;
    }

    /**
     * Set messageReceived to false
     */
    static public void resetMessageReceived() {
        messageReceived = false;
    }

    /**
     * @return buffer used to trade messages
     */
    static public Message getMessageBuffer() {
        return messageBuffer;
    }

    /**
     * @return Current state of client's connection
     */
    static public boolean isConnectedToServer() {

        return connectedToServer;
    }

    /**
     * Tries to close socket and input/output stream and set connectedToServer to false, then close the game
     */
    static public void closeGame() {

        connectedToServer = false;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                killGame();
            }
        }, 2000);

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
     * Tries to close socket and input/output stream and set connectedToServer to false, then close the game
     */
    static private void disconnect() {

        connectedToServer = false;

        Client.getView().notifyInternetCrash();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                killGame();
            }
        }, 2000);

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
    static public void sendMessage(Message message) {

        String json = new MessageGsonBuilder().getGsonBuilder().create().toJson(message);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
            }
        }, 5000);

        try {
            dataOutputStream.writeUTF(json);
            dataOutputStream.flush();
            timer.cancel();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create socket's OutputStream
     */
    static private void setOutput() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
            }
        }, 5000);

        try {
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            timer.cancel();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create socket's InputStream
     */
    static private void setInput() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
            }
        }, 5000);

        try {
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            timer.cancel();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create streams for socket
     */
    static public void setInputandOutput() {
        setInput();
        setOutput();
    }

    /**
     * @return Message received from server
     */
    static private void readMessage() {

        String json;
        Timer timer = new Timer();
        Message message = null;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
            }
        }, 5000);

        try {
            json = dataInputStream.readUTF();
            message = new MessageGsonBuilder().getGsonBuilder().create().fromJson(json, Message.class);
            timer.cancel();
            if (message.getMessageType() != MessageType.PING) {
                messageBuffer = new MessageGsonBuilder().getGsonBuilder().create().fromJson(json, Message.class);
                messageReceived = true;
                waitObject.notifyAll();
            }

        } catch (IOException e) {
            disconnect();
        }

    }

    /**
     * close the game
     */
    static private void killGame() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Runtime.getRuntime().halt(0);
            }
        }, 5000);

        System.exit(0);
        timer.cancel();
    }

    /**
     * This thread is dedicated to ping with server and receive messages
     */
    @Override
    public void run() {

        Message message;

        while (connectedToServer) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            message = new PingMessage(Client.getUUID());

            sendMessage(message);
            readMessage();

        }
    }

}




