package it.polimi.ingsw.am37.network;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.ClientStatus;
import it.polimi.ingsw.am37.message.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ClientSocket implements Runnable {

    /**
     * Flag to disable disconnection for debug purpose
     */
    final static boolean debugMode = true;

    /**
     * Used for creating a loop for client's waiting after a message is received
     */
    private boolean waitingMessage = true;

    /**
     * Object used by main thread for waiting on after it sent a message
     */
    private final Object waitObject = new Object();

    /**
     * Buffer used to store a message (different from ping) when the secondary thread reads it
     */
    private Message messageBuffer = null;

    /**
     * Socket used to connect
     */
    private Socket socket;

    /**
     * Boolean which represents the current state of client's connection
     */
    private boolean connectedToServer = false;

    /**
     * Input stream
     */
    private InputStream inputStream;

    /**
     * Output stream
     */
    private OutputStream outputStream;

    /**
     * DataOutput stream used for sending messages
     */
    private DataOutputStream dataOutputStream;

    /**
     * DataInput stream used for reading messages
     */
    private DataInputStream dataInputStream;

    final Client client;

    /**
     * @param address Server's IP
     * @param port    Server's port
     * @throws IOException When connection is failed
     */
    public void connectToServer(String address, int port) throws IOException {

        socket = new Socket(address, port);
        connectedToServer = true;
        setInputAndOutput();
    }

    public ClientSocket(Client client){
        this.client = client;
    }

    /**
     * @return waitObject used for synchronize
     */
    public Object getWaitObject() {
        return waitObject;
    }

    /**
     * @return if a message is received from server
     */
    public boolean getWaitingMessage() {
        return waitingMessage;
    }

    /**
     * Set messageReceived to false
     */
    public void setWaitingMessage(boolean bool) {
        waitingMessage = bool;
    }


    /**
     * @return buffer used to trade messages
     */
    public Message getMessageBuffer() {
        return messageBuffer;
    }

    /**
     * @return Current state of client's connection
     */
    public boolean isConnectedToServer() {

        return connectedToServer;
    }

    /**
     * Tries to close socket and input/output stream and set connectedToServer to false, then close the game
     */
    public void closeGame() {

        if (connectedToServer) {
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
        } else {
            killGame();
        }

    }

    /**
     * Tries to close socket and input/output stream and set connectedToServer to false, then close the game
     */
    private void disconnect() {

        connectedToServer = false;

        client.getView().notifyInternetCrash();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                killGame();
            }
        }, 3000);

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
    public void sendMessage(Message message) {

        //TODO PER ORA LO LASCIAMO IN SOSPESO, POI DECIDIAMO SE METTERE QUESTA FUNZIONE
        // ogni 0,3 secondi manda un ping, metto un contatore statico che incremento ad ogni messaggio, arrivato a
        // 700 avviso che se non viene mandato un messaggio valido a breve verrà disconnesso
        // gestisco anche quando non è il mio turno ovviamente questo non deve accadere

        if (connectedToServer) {
            String json = new MessageGsonBuilder().registerMessageAdapter()
                    .registerStudentContainerAdapter()
                    .getGsonBuilder()
                    .create()
                    .toJson(message);

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
    }

    /**
     * Create socket's OutputStream
     */
    private void setOutput() {

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
    private void setInput() {

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
    private void setInputAndOutput() {
        setInput();
        setOutput();
    }

    /**
     * Message received from server and executed
     */
    private void readMessage() {

        String json;
        Message message = null;
        Timer timer = new Timer();

        if (!debugMode) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    disconnect();
                }
            }, 5000);
        }

        try {
            json = dataInputStream.readUTF();
            message = new MessageGsonBuilder().registerMessageAdapter()
                    .registerStudentContainerAdapter()
                    .registerUpdatableObjectAdapter()
                    .getGsonBuilder()
                    .create()
                    .fromJson(json, Message.class);
            timer.cancel();
            if (message.getMessageType() != MessageType.PING && message.getMessageType() != MessageType.NEXT_TURN &&
                    message.getMessageType() != MessageType.PLANNING_PHASE && message.getMessageType() != MessageType.START_GAME && message.getMessageType() != MessageType.END_GAME) {
                messageBuffer = new MessageGsonBuilder().registerMessageAdapter()
                        .registerStudentContainerAdapter()
                        .getGsonBuilder()
                        .create()
                        .fromJson(json, Message.class);
                waitingMessage = false;
                synchronized (waitObject) {
                    waitObject.notifyAll();
                }
            }

            if (message.getMessageType() == MessageType.START_GAME) {
                synchronized (waitObject) {
                    waitObject.notifyAll();
                }
                client.beginGame();
            } else if (message.getMessageType() == MessageType.END_GAME) {
                EndGameMessage endGameMessage;
                endGameMessage = (EndGameMessage) message;

                client.setStatus(ClientStatus.ENDGAME);

                client.getView().printWinner(endGameMessage.getWinnerNickname());

                //It empties the file with configurations because game is ended correctly
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/myConfigurations/resilience.txt"), StandardCharsets.UTF_8);

            } else if (message.getMessageType() == MessageType.UPDATE) {
                client.getView()
                        .getReducedModel()
                        .update(((UpdateMessage) message).getUpdatedObjects()
                                .values()
                                .stream()
                                .flatMap(List::stream)
                                .toList());
            } else if (message.getMessageType() == MessageType.NEXT_TURN) {
                NextTurnMessage nextTurnMessage;

                nextTurnMessage = (NextTurnMessage) message;

                if (nextTurnMessage.getNextPlayerNickname().equals(client.getNickname())) {
                    client.setStatus(ClientStatus.MOVINGSTUDENTS);
                    client.getView().yourTurn();
                } else
                    client.getView().hisTurn(nextTurnMessage.getNextPlayerNickname());

            } else if (message.getMessageType() == MessageType.PLANNING_PHASE) {
                client.getView().mustPlayAssistant();
                client.setStatus(ClientStatus.PLAYINGASSISTANT);
            }


        } catch (IOException e) {
            disconnect();

        }

    }

    /**
     * close the game
     */
    private void killGame() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Runtime.getRuntime().halt(0);
            }
        }, 3000);

        System.exit(0);
        timer.cancel();
    }

    /**
     * Method used for sending ping
     */
    private void messagePing() {

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message message;
                message = new PingMessage(client.getUUID());

                sendMessage(message);
            }
        }, 300, 300);


    }

    /**
     * This thread is dedicated to ping with server and receive messages
     */
    @Override
    public void run() {

        messagePing();

        while (connectedToServer)
            readMessage();

    }

}




