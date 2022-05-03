package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.Lobby;
import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.network.MessageReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements MessageReceiver {

    /**
     *
     */
    private static int port;
    /**
     * Associates the UUID of the Client with the ClientHandler.
     */
    private static HashMap<String, ClientHandler> clientHandlerMap;
    /**
     * List to keep track of active Lobbies.
     */
    private static ArrayList<Lobby> activeLobbies;
    /**
     * List to keep track of the players' nicknames.
     */
    private static ArrayList<String> nicknames;
    /**
     *
     */
    private String address;

    /**
     *
     */
    public Server(String serverAddress, int serverPort) {
        port = serverPort;
        address = serverAddress;

        new Thread(() -> {
            //TODO: LOGGER
            ServerSocket ss;
            Socket socket;
            try {
                ss = new ServerSocket(serverPort);
                System.out.println("ServerSocket awaiting connections...");
                socket = ss.accept();
                System.out.println("Connection from " + socket + "!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClientHandler ch = new ClientHandler(socket);
            new Thread(ch).start();
        }).start();
    }

    public static void main(String[] args) {
        //FIXME: if port and address doesn't make sense LOGIC
        int port = 48651;
        String address = "localhost";
        if (args.length == 4) {
            address = args[0];
            port = Integer.parseInt(args[1]);
        }
        Server server = new Server(address, port);
    }

    /**
     * @param UUID
     * @param client
     */
    public void addClient(String UUID, ClientHandler client) {

    }

    /**
     * @param client
     */
    private void removeClient(ClientHandler client) {

    }

    /**
     * @param client
     */
    private void onDisconnect(ClientHandler client) {

    }

    /**
     *
     */
    private void createLobby() {

    }


    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the message received.
     */
    @Override
    public void onMessageReceived(Message message) {
        //FIXME: Remove hardcoded messages type
        switch (message.getAction()) {
            case MessageType.LOGIN:
                if (nicknames.contains(message.getPayload().getNickname())) {
                    Message response = new Message();
                    sendMessage(response);
                }

                break;
            case MessageType.REQUEST_LOBBY:

                break;
            default:

                break;

        }
    }

    /**
     * @param message the message that must be sent
     */
    @Override
    public void sendMessage(Message message) {
        //TODO: Prendi il client id dal messaggio, usa la mappa per poter associarlo al suo CH, chiama sendMessage di CH
    }
}
