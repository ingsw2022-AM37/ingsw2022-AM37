package it.polimi.ingsw.am37.network.server;

import com.google.gson.JsonParseException;
import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.message.MessageGsonBuilder;
import it.polimi.ingsw.am37.message.MessageType;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.exceptions.InternetException;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * This is the counterpart of client in server
 */
public class ClientHandler implements Runnable {
    /**
     * Flag for disable disconnection when ping timeout fails on debug
     */
    private final static boolean debug_disableTimers = false;

    /**
     * Input stream
     */
    private InputStream inputStream;

    /**
     * Output stream
     */
    private OutputStream outputStream;

    /**
     * DataInput stream used for reading messages
     */
    private DataInputStream dataInputStream;

    /**
     * DataOutput stream used for sending messages
     */
    private DataOutputStream dataOutputStream;

    /**
     * It's the recipient of client's messages: it can be the "central" server or a lobby
     */
    private MessageReceiver messageReceiver;

    /**
     * It's the socket connected to the handler
     */
    private final Socket clientSocket;

    /**
     * A boolean value which represents the state of connection
     */
    private boolean connectedToClient;

    /**
     * UUID of the client
     */
    private String UUID = null;

    /**
     * @return client UUID
     */
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * Default Constructor
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.connectedToClient = true;
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
     * @throws InternetException Thrown when connection is failed
     */
    @SuppressWarnings({"Convert2Lambda", "unchecked", "rawtypes"})
    public void sendMessageToClient(Message message) throws InternetException {
        message.setUUID(UUID);
        String json = new MessageGsonBuilder().registerMessageAdapter().registerStudentContainerAdapter().registerUpdatableObjectAdapter().getGsonBuilder().create().toJson(message);
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            Callable<Object> r = new Callable() {
                @Override
                public Object call() throws IOException {
                    dataOutputStream.writeUTF(json);
                    dataOutputStream.flush();
                    return null;
                }
            };
            Future<?> f = service.submit(r);
            f.get(5, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            disconnect();
            throw new InternetException();
        } finally {
            service.shutdown();
        }
    }

    /**
     * Additional thread used to listen messages from the client
     */
    @Override
    public void run() {
        Message message;
        setInputAndOutput();

        while (connectedToClient) {
            try {
                message = readMessage();
            } catch (InternetException e) {
                return;
            }
            if (message.getMessageType() == MessageType.PING)
                try {
                    sendMessageToClient(message);
                } catch (InternetException e) {
                    return;
                }
            else {
                try {
                    messageReceiver.onMessageReceived(message, this);
                } catch (InternetException e) {
                    return;
                }
            }
        }
    }

    /**
     * Set there isn't connection with client and tries to close the socket
     */
    public void disconnect() {
        this.connectedToClient = false;
        messageReceiver.onDisconnect(UUID);
        try {
            dataInputStream.close();
            dataOutputStream.close();
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create clientHandler's OutputStream
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
            outputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            timer.cancel();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create clientHandler's InputStream
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
            inputStream = clientSocket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            timer.cancel();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Create streams for clientHandler
     */
    private void setInputAndOutput() {
        setInput();
        setOutput();
    }

    /**
     * @return Message received from client
     * @throws InternetException If connection is failed
     */
    @SuppressWarnings({"Convert2Lambda", "unchecked", "rawtypes"})
    private Message readMessage() throws InternetException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            Callable<Message> r = new Callable() {
                @Override
                public Message call() throws IOException {
                    Message message = null;
                    String json = dataInputStream.readUTF();
                    try {
                        message = new MessageGsonBuilder().registerMessageAdapter().registerStudentContainerAdapter().getGsonBuilder().create().fromJson(json, Message.class);
                    } catch (JsonParseException e) {
                        disconnect();
                    }
                    if (UUID == null)
                        UUID = message.getUUID();
                    return message;
                }
            };
            Future<Message> messageFuture = service.submit(r);
            messageFuture.get(5, TimeUnit.SECONDS);
            return messageFuture.get();
        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            if (!debug_disableTimers) disconnect();
            throw new InternetException();
        } finally {
            service.shutdown();
        }

    }
}
