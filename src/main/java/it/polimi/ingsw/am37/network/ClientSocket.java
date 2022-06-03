package it.polimi.ingsw.am37.network;

import com.google.gson.Gson;
import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.ClientStatus;
import it.polimi.ingsw.am37.message.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSocket implements Runnable {
    /**
     * Flag to disable disconnection for debug purpose
     */
    final static boolean debugMode = false;
    private final static Gson defaultMessageSerializer = new MessageGsonBuilder().registerMessageAdapter()
            .registerUpdatableObjectAdapter()
            .registerStudentContainerAdapter()
            .getGsonBuilder()
            .create();

    /**
     * Socket used to connect
     */
    private final Socket socket;
    private final Client client;
    /**
     * Last received message (different from ping)
     */
    private final BlockingQueue<Message> messageBuffer;
    /**
     * Boolean which represents the current state of client's connection
     */
    private boolean connectedToServer;
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

    /**
     * Construct a socket to comunicate with the server using provided parameters, then try to communicate with it
     *
     * @param address Server's IP
     * @param port    Server's port
     * @throws IOException When connection is failed
     */
    public ClientSocket(String address, int port, Client client) throws IOException {
        socket = new Socket(address, port);
        connectedToServer = true;
        this.client = client;
        this.messageBuffer = new LinkedBlockingQueue<>();
        setInputAndOutput();
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
     * @return if we are connected to the server or not
     */
    public boolean isConnectedToServer() {
        return connectedToServer;
    }

    public BlockingQueue<Message> getMessageBuffer() {
        return messageBuffer;
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
     * Tries to close socket and input/output stream and set connectedToServer to false, then close the game
     */
    private void onDisconnect() {
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
     * Message received from server and executed
     */
    private void readMessage() {
        String json;
        Message message;
        Timer timer = new Timer();
        if (!debugMode) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onDisconnect();
                }
            }, 5000);
        }
        try {
            json = dataInputStream.readUTF();
            message = defaultMessageSerializer
                    .fromJson(json, Message.class);
            timer.cancel();
            if (message.getMessageType() != MessageType.PING) {
                messageBuffer.add(message);
                switch (message.getMessageType()) {
                    case START_GAME -> {
                        synchronized (client) {
                            client.notify();
                        }
                    }
                    case PLANNING_PHASE -> {
                        PlanningPhaseMessage planningPhaseMessage = (PlanningPhaseMessage) message;
                        client.setStatus(ClientStatus.PLAYINGASSISTANT);
                        client.getView().yourTurn();
                    }
                    case UPDATE -> {
                        UpdateMessage updateMessage = (UpdateMessage) message;
                        client.getView()
                                .getReducedModel()
                                .update(updateMessage.getUpdatedObjects()
                                        .values()
                                        .stream()
                                        .flatMap(List::stream)
                                        .toList());
                    }
                    case NEXT_TURN -> {
                        NextTurnMessage nextTurnMessage = (NextTurnMessage) message;
                        if (Objects.equals(nextTurnMessage.getNextPlayerNickname(), client.getNickname())) {
                            client.setStatus(ClientStatus.MOVINGSTUDENTS);
                            client.getView().yourTurn();

                        } else {
                            client.getView().hisTurn(nextTurnMessage.getNextPlayerNickname());
                            client.setStatus(ClientStatus.WAITINGFORTURN);
                        }
                    }
                    case END_GAME -> client.getView().printWinner(((EndGameMessage) message).getWinnerNickname());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            onDisconnect();
        }

    }

    /**
     * This thread is dedicated to ping with server and receive messages
     */
    @Override
    public void run() {
        messagePing();
        while (connectedToServer) readMessage();
    }

    /**
     * Send a message to the server; if any error occur disconnect the socket
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        //TODO PER ORA LO LASCIAMO IN SOSPESO, POI DECIDIAMO SE METTERE QUESTA FUNZIONE
        // ogni 0,3 secondi manda un ping, metto un contatore statico che incremento ad ogni messaggio, arrivato a
        // 700 avviso che se non viene mandato un messaggio valido a breve verrà disconnesso
        // gestisco anche quando non è il mio turno ovviamente questo non deve accadere
        if (connectedToServer) {
            String json = defaultMessageSerializer.toJson(message);
            Timer timer = new Timer();
            if (!debugMode) timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onDisconnect();
                }
            }, 5000);
            try {
                dataOutputStream.writeUTF(json);
                dataOutputStream.flush();
                timer.cancel();
            } catch (IOException e) {
                onDisconnect();
            }
        }
    }

    /**
     * Create socket's InputStream
     */
    private void setInput() {
        Timer timer = new Timer();
        if (!debugMode) timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onDisconnect();
            }
        }, 5000);
        try {
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            timer.cancel();
        } catch (IOException e) {
            onDisconnect();
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
     * Create socket's OutputStream
     */
    private void setOutput() {
        Timer timer = new Timer();
        if (!debugMode) timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onDisconnect();
            }
        }, 5000);
        try {
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            timer.cancel();
        } catch (IOException e) {
            onDisconnect();
        }
    }
}




