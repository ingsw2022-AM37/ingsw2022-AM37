package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.LobbyRequestMessage;
import it.polimi.ingsw.am37.message.LoginMessage;
import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.message.MessageType;
import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.IOException;
import java.util.*;

public class Client {


    /**
     * client identifier
     */
    private static String UUID;

    /**
     * View which can be at real time GUI or CLI, based on the information given by player at the start
     */
    private static AbstractView view;

    /**
     * Client's nickname
     */
    private static String nickname = null;

    /**
     * Net-feature class associated to the client
     */
    private static ClientSocket clientSocket;

    /**
     * It represents the state of client during a game
     */
    private static ClientStatus status;

    /**
     * @return view of the client, used to display some informations
     */
    public static AbstractView getView() {
        return view;
    }

    /**
     * @return Player's identifier
     */
    public static String getUUID() {
        return UUID;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {

        boolean wrongInitialInput;
        final String address = "address";
        final String port = "port";
        final String graphics = "graphics";


        view = new CliView();
        HashMap<String, String> params = new HashMap<>();

        wrongInitialInput = tryConnectionWithArgs(args, address, port, graphics, params);

        tryConnectionAgain(wrongInitialInput, address, port, graphics, params);

        //preparing gui
        if (params.get(graphics).equals("gui"))
            view = new GuiView();

        //start listening thread
        new Thread(new ClientSocket()).start();

        chooseNickname();

        chooseLobby();


    }


    /**
     * @param args     It's the array of string created by main class
     * @param address  It's how we call address in first input in terminal
     * @param port     It's how we call port in first input in terminal
     * @param graphics It's how we call graphics in first input in terminal
     * @param params   It's the HashMap with the three parameters and their value
     * @return if initial input was wrong
     */
    static private boolean tryConnectionWithArgs(String[] args, String address, String port, String graphics, HashMap<String, String> params) {

        final int expectedArguments = 6;
        int i = 0;
        boolean wrongInitialInput = false;


        List<String> list = Arrays.stream(args).map(String::toLowerCase).toList();
        args = list.toArray(new String[0]);

        if (args.length != expectedArguments) {
            view.wrongInsertFewArguments();
            wrongInitialInput = true;
        } else {
            while (i < args.length) {

                if (!(args[i].equals("--" + port) || args[i].equals("--" + address) || args[i].equals("--" + graphics))) {
                    view.wrongInsert();
                    wrongInitialInput = true;
                    break;
                }

                params.put(args[i].substring(2), args[i + 1]);
                i = i + 2;
            }

            if (params.containsKey(port)) {
                try {
                    i = Integer.parseInt(params.get(port));
                } catch (NumberFormatException e) {
                    view.wrongInsertPort();
                    wrongInitialInput = true;
                }
            }

            if (params.containsKey(address) && params.containsKey(graphics)) {

                if (!params.get(graphics).equals("cli") && !params.get(graphics).equals("gui")) {
                    view.wrongInsertGraphics();
                    wrongInitialInput = true;
                }

                if (!wrongInitialInput) {
                    try {
                        ClientSocket.connectToServer(params.get(address), Integer.parseInt(params.get(port)));
                    } catch (IOException e) {
                        view.wrongServer();
                        wrongInitialInput = true;
                    }
                }
            }
        }

        return wrongInitialInput;
    }

    /**
     * @param wrongInitialInput If initial input was wrong
     * @param address           It's how we call address in first input in terminal
     * @param port              It's how we call port in first input in terminal
     * @param graphics          It's how we call graphics in first input in terminal
     * @param params            It's the HashMap with the three parameters and their value
     */
    static private void tryConnectionAgain(boolean wrongInitialInput, String address, String port, String graphics, HashMap<String, String> params) {

        final String defaultGraphics = "cli";
        final int defaultPort = 60000;
        final String defaultAddress = "localhost";
        String response;


        while (wrongInitialInput) {

            response = view.askDefault();

            if ((response.equals("close game")))
                return;

            else {
                if (response.equals("yes")) {
                    params = new HashMap<>();
                    params.put(address, defaultAddress);
                    params.put(port, Integer.toString(defaultPort));
                    params.put(graphics, defaultGraphics);
                } else {
                    params = new HashMap<>();
                    response = view.insertYourParameters(address, port, graphics, params);
                    if (response.equals("close game"))
                        return;
                }

                try {
                    ClientSocket.connectToServer(params.get(address), Integer.parseInt(params.get(port)));
                    wrongInitialInput = false;
                } catch (IOException e) {
                    view.wrongServer();
                }
            }
        }
    }

    /**
     * Method used to set nickname, if chosen name is available then set it
     */
    static private void chooseNickname() {

        String tempNick;
        Message message;


        while (nickname == null) {

            tempNick = view.chooseNickname();

            if (tempNick.equals("close game"))
                ClientSocket.closeGame();

            message = new LoginMessage(UUID, tempNick);
            ClientSocket.sendMessage(message);

            while (ClientSocket.getMessageReceived()) {
                try {
                    ClientSocket.getWaitObject().wait();
                    ClientSocket.resetMessageReceived();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.ERROR)
                ;
            else
                setNickname(tempNick);
        }
    }

    /**
     * Method used to choose lobby, you have to insert number of players and if you want advanced rules
     */
    static private void chooseLobby() {

        String response;
        String response2;
        int i;
        boolean j;
        Message message;


        response = view.requestAdvancedRules();
        if (response.equals("close game"))
            return;

        if (response.equals("yes"))
            j = true;
        else
            j = false;

        response2 = view.requestNumPlayers();

        if (response2.equals("close game"))
            return;
        else
            i = Integer.parseInt(response2);

        message = new LobbyRequestMessage(UUID, i, j);

        ClientSocket.sendMessage(message);

    }


    /**
     * @param string Nickname to be set for the player
     */
    static private void setNickname(String string) {
        nickname = string;
    }


}
