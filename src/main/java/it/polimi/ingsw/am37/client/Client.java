package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.IOException;
import java.util.*;

public class Client {


    /**
     * Method used to start the game after joining lobby
     */
    private static boolean inGame = false;

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
     * It represents the state of client during a game
     */
    private static ClientStatus status;

    /**
     * @return view of the client, used to display some information
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
     * HashMap with address, port and graphics chosen for the match
     */
    private static HashMap<String, String> params;
    /**
     * It's the sum of moved students in action phase
     */
    private static int totalStudentsInTurn = 0;


    /**
     * Main method
     */
    public static void main(String[] args) {

        status = ClientStatus.LOGGING;

        String response;
        boolean wrongInitialInput;
        boolean actionOkay = false;
        final String address = "address";
        final String port = "port";
        final String graphics = "graphics";
        inGame = false;


        view = new CliView();
        params = new HashMap<>();

        UUID = java.util.UUID.randomUUID().toString();

        wrongInitialInput = tryConnectionWithArgs(args, address, port, graphics);

        tryConnectionAgain(wrongInitialInput, address, port, graphics);


        //preparing gui
        if (params.get(graphics).equals("gui"))
            view = new GuiView();


        status = ClientStatus.CHOOSINGNAME;

        //start listening thread
        new Thread(new ClientSocket()).start();

        chooseNickname();


        status = ClientStatus.CHOOSINGLOBBY;
        chooseLobby();

        view.waitingMatch();

        while (!inGame) {
            synchronized (ClientSocket.getWaitObject()) {
                try {
                    ClientSocket.getWaitObject().wait();

                } catch (InterruptedException e) {
                    ;
                }
            }
        }

        view.gameStarted();


        view.possibleChoices();
        status = ClientStatus.WAITINGFORTURN;
        while (ClientSocket.isConnectedToServer()) {

            response = view.takeInput();

            if (response.equals("0"))
                view.possibleChoices();

            else if (response.equals("1"))
                ClientSocket.closeGame();

            else if (response.equals("2") && status == ClientStatus.PLAYINGASSISTANT) {
                actionOkay = playAssistant();
                if (actionOkay)
                    status = ClientStatus.WAITINGFORTURN;
                else
                    view.impossibleAssistant();
            } else if (response.equals("3") && status == ClientStatus.MOVINGSTUDENTS) {
                actionOkay = moveStudents();
                if (actionOkay && totalStudentsInTurn == 3) {
                    status = ClientStatus.MOVINGMOTHERNATURE;
                    totalStudentsInTurn = 0;
                } else
                    view.impossibleStudents();
            } else if (response.equals("4") && status == ClientStatus.MOVINGMOTHERNATURE) {
                actionOkay = moveMotherNature();
                if (actionOkay)
                    status = ClientStatus.CHOOSINGCLOUD;
                else
                    view.impossibleMotherNature();
            } else if (response.equals("5") && status == ClientStatus.CHOOSINGCLOUD) {
                actionOkay = chooseCloud();
                if (actionOkay)
                    status = ClientStatus.WAITINGFORTURN;
                else
                    view.impossibleCloud();
            } else if (response.equals("6")) {
                actionOkay = playCharacter();
                if (!actionOkay)
                    view.impossibleCharacter();
            } else if (response.equals("7")) {
                Player player = view.askPlayer();
                view.showPlayerStatus(player);
            } else if (response.equals("8")) {
                Player player = view.askPlayer();
                view.showDeck(player);
            } else if (response.equals("9")) {
                view.showTable();
            } else if (response.equals("10")) {
                view.showPlayersNicknames();
            } else if (response.equals("11")) {
                view.showConnection(params, address, port);
            } else
                view.wrongInsert();

        }
    }

    /**
     * @param action New status for the client
     */
    static public void setStatus(ClientStatus action) {
        status = action;
    }


    /**
     * @param args     It's the array of string created by main class
     * @param address  It's how we call address in first input in terminal
     * @param port     It's how we call port in first input in terminal
     * @param graphics It's how we call graphics in first input in terminal
     * @return if initial input was wrong
     */
    static private boolean tryConnectionWithArgs(String[] args, String address, String port, String graphics) {

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

            if (!wrongInitialInput)
                view.ifNonLocalhostAddress(params.get(address));

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
     */
    static private void tryConnectionAgain(boolean wrongInitialInput, String address, String port, String graphics) {

        final String defaultGraphics = "cli";
        final int defaultPort = 60000;
        final String defaultAddress = "localhost";
        String response;


        while (wrongInitialInput) {

            response = view.askDefault();

            if ((response.equals("close game")))
                ClientSocket.closeGame();

            else {
                if (response.equals("yes")) {
                    params = new HashMap<>();
                    params.put(address, defaultAddress);
                    params.put(port, Integer.toString(defaultPort));
                    params.put(graphics, defaultGraphics);
                } else {
                    params = new HashMap<>();
                    response = view.insertYourParameters(address, port, graphics);
                    if (response.equals("close game"))
                        ClientSocket.closeGame();
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
     * Method used after sending a message to wait the response
     */
    static private void waitResponse() {

        while (ClientSocket.getWaitingMessage()) {
            synchronized (ClientSocket.getWaitObject()) {
                try {
                    ClientSocket.getWaitObject().wait();
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
        ClientSocket.setWaitingMessage(true);

    }

    /**
     * Method used to check the message in the buffer
     */
    static private boolean onMessage() {

        if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.ERROR) {
            view.impossibleInputForNow();
            return false;
        } else if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.UPDATE)
            return true;

        return false;

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

            waitResponse();

            if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.ERROR)
                view.impossibleInputForNow();

            else if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.CONFIRM)
                setNickname(tempNick);
        }
    }

    /**
     * Method used to choose lobby, you have to insert number of players and if you want advanced rules
     */
    static private void chooseLobby() {

        String response;
        String response2;
        int i = 0;
        boolean j;
        Message message;


        response = view.requestAdvancedRules();
        if (response.equals("close game"))
            ClientSocket.closeGame();

        if (response.equals("yes"))
            j = true;
        else
            j = false;

        response2 = view.requestNumPlayers();

        if (response2.equals("close game"))
            ClientSocket.closeGame();
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

    /**
     * @return My nickname
     */
    static public String getNickname() {
        return nickname;
    }


    /**
     * @return HashMap with values
     */
    static public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * @return if server has sent an exception for our assistant
     */
    static private boolean playAssistant() {
        int val;

        val = view.askAssistant();
        Message message = new PlayAssistantMessage(UUID, val);

        ClientSocket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * @return if server has sent an exception for our movement
     */
    static private boolean moveStudents() {

        HashMap<String, String> response;
        Message message;
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        FactionColor color = null;

        response = view.askStudents();
        for (FactionColor temp : FactionColor.values())
            if (response.get("color").equals(temp.toString()))
                color = temp;

        container.addStudents(Integer.parseInt(response.get("number")), color);

        if (response.get("destination").equals("d")) {
            message = new StudentsToDiningMessage(UUID, container);

            ClientSocket.sendMessage(message);
            waitResponse();
            return onMessage();
        } else {

            message = new StudentsToIslandMessage(UUID, container, Integer.parseInt(response.get("islandDest")));

            ClientSocket.sendMessage(message);
            waitResponse();
            return onMessage();
        }
    }

    /**
     * @return if server has an exception for the movement
     */
    static private boolean moveMotherNature() {

        int islandId;

        islandId = view.askMotherNature();

        Message message = new MoveMotherNatureMessage(UUID, islandId);

        ClientSocket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * @return if server has an exception for our choice
     */
    static private boolean chooseCloud() {

        String cloudId;

        cloudId = view.askCloud();

        Message message = new ChooseCloudMessage(UUID, cloudId);

        ClientSocket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * Method used to set beginGame
     */
    static public void beginGame() {
        inGame = true;
    }

    static private boolean playCharacter() {

        //TODO DA FARE IL METODO CHE CHIEDE VIEW.ASKCHARACTER E POI CREA UN MESSAGGIO E LO MANDA CON IL PERSONAGGIO SCELTO

        return false;
    }


    /**
     * @return total students moved in action phase in a turn
     */
    public static int getTotalStudentsInTurn() {
        return totalStudentsInTurn;
    }

    /**
     * @param num students moved
     */
    public static void addTotalStudentsInTurn(int num) {
        totalStudentsInTurn = totalStudentsInTurn + num;
    }


}
