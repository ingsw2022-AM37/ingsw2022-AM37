package it.polimi.ingsw.am37.network;

import com.google.gson.Gson;
import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.ClientStatus;
import it.polimi.ingsw.am37.message.*;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class connect the client with its clientHandler
 */
public class ClientSocket implements Runnable {
    /**
     * Flag to disable disconnection for debug purpose
     */
    final static boolean debug_disableTimers = false;
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
     * This blocking queue contains the response or the status message sent by server after each action; contains only
     * messages of type {@link ConfirmMessage},{@link ErrorMessage},{@link UpdateMessage} or
     * {@link ActiveLobbiesMessage}
     */
    private final BlockingQueue<Message> responseBuffer;
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
        this.responseBuffer = new LinkedBlockingQueue<>();
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

    public BlockingQueue<Message> getResponseBuffer() {
        return responseBuffer;
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
        if (!debug_disableTimers) {
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
                switch (message.getMessageType()) {
                    case START_GAME -> {
                        synchronized (client) {
                            client.notify();
                        }
                    }
                    case PLANNING_PHASE -> {
                        client.setStatus(ClientStatus.PLAYINGASSISTANT);
                        client.getView().yourTurn();
                    }
                    case UPDATE -> {
                        responseBuffer.add(message);
                        UpdateMessage updateMessage = (UpdateMessage) message;
                        client.getView()
                                .updateView(updateMessage, client);
                    }
                    case NEXT_TURN -> {
                        NextTurnMessage nextTurnMessage = (NextTurnMessage) message;
                        if (Objects.equals(nextTurnMessage.getNextPlayerNickname(), client.getNickname())) {
                            client.getView().yourTurn();
                            client.setStatus(ClientStatus.MOVINGSTUDENTS);

                        } else {
                            client.getView().hisTurn(nextTurnMessage.getNextPlayerNickname());
                            client.setStatus(ClientStatus.WAITINGFORTURN);
                        }
                    }
                    case END_GAME -> client.getView().printWinner(((EndGameMessage) message).getWinnerNickname());
                    case CONFIRM, ERROR, ACTIVE_LOBBIES -> responseBuffer.add(message);
                }
            }
        } catch (IOException e) {
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
     * Send a message to the server and clear the queue of read message; if any error occur disconnect the socket
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        responseBuffer.clear();
        if (connectedToServer) {
            String json = defaultMessageSerializer.toJson(message);
            Timer timer = new Timer();
            if (!debug_disableTimers) timer.schedule(new TimerTask() {
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
        if (!debug_disableTimers) timer.schedule(new TimerTask() {
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
        if (!debug_disableTimers) timer.schedule(new TimerTask() {
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




