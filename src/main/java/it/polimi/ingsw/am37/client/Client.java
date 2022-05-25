package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {

    /**
     *
     */
    private OutputStreamWriter writer;

    /**
     * Method used to start the game after joining lobby
     */
    private boolean inGame = false;

    /**
     * client identifier
     */
    private String UUID;

    /**
     * View which can be at real time GUI or CLI, based on the information given by player at the start
     */
    private AbstractView view;

    /**
     * Client's nickname
     */
    private String nickname = null;

    /**
     * It represents the state of client during a game
     */
    private ClientStatus status;

    /**
     * @return view of the client, used to display some information
     */
    public AbstractView getView() {
        return view;
    }

    /**
     * @return Player's identifier
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * HashMap with address, port and graphics chosen for the match
     */
    private HashMap<String, String> params;
    /**
     * It's the sum of moved students in action phase
     */
    private int totalStudentsInTurn = 0;

    private ClientSocket socket;


    /**
     * Main method
     */
    public Client(String[] args) {

        status = ClientStatus.LOGIN;

        String response;
        boolean wrongInitialInput;
        boolean actionOkay = false;
        final String address = "address";
        final String port = "port";
        final String graphics = "graphics";
        inGame = false;

        InputStreamReader reader;
        BufferedReader bufferedReader = null;

        int oldLobby = -1;
        List<Integer> totalLobbies;
        boolean inOldGame = false;


        try {
            writer =
                    new OutputStreamWriter(new FileOutputStream("src/main/resources/myConfigurations/resilience.txt")
                            , StandardCharsets.UTF_8);
            reader = new InputStreamReader(new FileInputStream("src/main/resources/myConfigurations/resilience.txt"),
                    StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);
        } catch (IOException e) {
            System.err.println(" Impossible to use resilience ");
        }

        view = new CliView(this);
        params = new HashMap<>();

        UUID = java.util.UUID.randomUUID().toString();

        wrongInitialInput = tryConnectionWithArgs(args, address, port, graphics);

        tryConnectionAgain(wrongInitialInput, address, port, graphics);

        ActiveLobbiesMessage message;
        message = (ActiveLobbiesMessage) socket.getMessageBuffer();
        totalLobbies = message.getLobbyIDs();

        //preparing gui
        if (params.get(graphics).equals("gui"))
            view = new GuiView();

        status = ClientStatus.CHOOSINGNAME;

        try {
            UUID = bufferedReader.readLine();
            nickname = bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println(" Impossible to use resilience ");
        }

        if (nickname != null && UUID != null)
            inOldGame = true;

        if (UUID == null)
            UUID = java.util.UUID.randomUUID().toString();

        try {
            String numLobby = bufferedReader.readLine();
            if ((numLobby != null))
                oldLobby = Integer.parseInt(numLobby);

        } catch (IOException e) {
            System.err.println(" Impossible to use resilience ");
        }

        if (totalLobbies.contains(oldLobby))
            inOldGame = true;

        else {
            try {
                OutputStreamWriter writer2 = new OutputStreamWriter(new FileOutputStream("src/main/resources" +
                        "/myConfigurations/resilience.txt"), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                System.err.println(" Error emptying config. file");
            }
        }


        if (inOldGame) {

            try {

                int numPlayers = Integer.parseInt(bufferedReader.readLine());
                boolean advancedRules = bufferedReader.readLine().equals("yes");

                sendNickname(UUID, nickname);

                waitResponse();

                if (socket.getMessageBuffer().getMessageType() == MessageType.ERROR) {
                    inOldGame = false;
                    nickname = null;

                    try {
                        OutputStreamWriter writer2 = new OutputStreamWriter(new FileOutputStream("src/main/resources" +
                                "/myConfigurations/resilience.txt"), StandardCharsets.UTF_8);
                    } catch (FileNotFoundException e) {
                        System.err.println(" Error emptying config. file");
                    }
                }

                if (inOldGame) {
                    status = ClientStatus.CHOOSINGLOBBY;
                    sendLobbyMessage(UUID, advancedRules, numPlayers);
                }

            } catch (IOException e) {
                System.err.println(" Impossible to use resilience ");
            }
        }

        if (!inOldGame) {
            chooseNickname();
            status = ClientStatus.CHOOSINGLOBBY;
            chooseLobby();
        }

        view.waitingMatch();
    }
    public void start() {
        String response;
        boolean actionOkay = false;
        final String address = "address";
        final String port = "port";
        while (!inGame) {
            synchronized (socket.getWaitObject()) {
                try {
                    socket.getWaitObject().wait();

                } catch (InterruptedException e) {
                    ;
                }
            }
        }

        view.gameStarted();


        view.possibleChoices();
        status = ClientStatus.WAITINGFORTURN;
        while (socket.isConnectedToServer()) {

            response = view.takeInput();

            if (response.equals("0"))
                view.possibleChoices();

            else if (response.equals("1"))
                socket.closeGame();

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
                }
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
    public void setStatus(ClientStatus action) {
        status = action;
    }


    /**
     * @param args     It's the array of string created by main class
     * @param address  It's how we call address in first input in terminal
     * @param port     It's how we call port in first input in terminal
     * @param graphics It's how we call graphics in first input in terminal
     * @return if initial input was wrong
     */
    private boolean tryConnectionWithArgs(String[] args, String address, String port, String graphics) {

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
                        this.socket = new ClientSocket(this);
                        socket.connectToServer(params.get(address), Integer.parseInt(params.get(port)));
                        //start listening thread
                        new Thread(socket).start();
                        waitResponse();
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
    private void tryConnectionAgain(boolean wrongInitialInput, String address, String port, String graphics) {

        final String defaultGraphics = "cli";
        final int defaultPort = 60000;
        final String defaultAddress = "localhost";
        String response;


        while (wrongInitialInput) {

            response = view.askDefault();

            if ((response.equals("close game")))
                socket.closeGame();

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
                        socket.closeGame();
                }

                try {
                    this.socket = new ClientSocket(this);
                    socket.connectToServer(params.get(address), Integer.parseInt(params.get(port)));
                    wrongInitialInput = false;
                    //start listening thread
                    new Thread(socket).start();
                    waitResponse();
                } catch (IOException e) {
                    view.wrongServer();
                }
            }
        }
    }

    /**
     * Method used after sending a message to wait the response
     */
    private void waitResponse() {

        while (socket.getWaitingMessage()) {
            synchronized (socket.getWaitObject()) {
                try {
                    socket.getWaitObject().wait();
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
        socket.setWaitingMessage(true);

    }

    /**
     * Method used to check the message in the buffer
     */
    private boolean onMessage() {

        if (socket.getMessageBuffer().getMessageType() == MessageType.ERROR) {
            ErrorMessage message = (ErrorMessage) socket.getMessageBuffer();
            System.out.println("\n" + message.getMessage() + "\n");
            return false;
        } else if (socket.getMessageBuffer().getMessageType() == MessageType.UPDATE)
            return true;

        return false;

    }

    /**
     * Method used to set nickname, if chosen name is available then set it
     */
    private void chooseNickname() {

        String tempNick;
        Message message;


        while (nickname == null) {

            tempNick = view.chooseNickname();

            if (tempNick.equals("close game"))
                socket.closeGame();

            sendNickname(UUID, tempNick);

            waitResponse();

            if (socket.getMessageBuffer().getMessageType() == MessageType.ERROR) {
                ErrorMessage mes = (ErrorMessage) socket.getMessageBuffer();
                System.out.println("\n" + mes.getMessage() + "\n");
            } else if (socket.getMessageBuffer().getMessageType() == MessageType.CONFIRM) {
                setNickname(tempNick);

                try {
                    writer.write(UUID + "\n");
                    writer.flush();
                    writer.write(tempNick + "\n");
                    writer.flush();
                } catch (IOException e) {
                    System.err.println(" Impossible to use resilience ");
                }
            }
        }
    }

    /**
     * @param UUID     UUID of the client
     * @param nickname chosen nickname
     */
    private void sendNickname(String UUID, String nickname) {

        Message message = new LoginMessage(UUID, nickname);
        socket.sendMessage(message);

    }

    /**
     * Method used to choose lobby, you have to insert number of players and if you want advanced rules
     */
    private void chooseLobby() {

        String response;
        String response2;
        int i = 0;
        boolean j;
        Message message;


        response = view.requestAdvancedRules();
        if (response.equals("close game"))
            socket.closeGame();

        if (response.equals("yes"))
            j = true;
        else
            j = false;

        response2 = view.requestNumPlayers();

        if (response2.equals("close game"))
            socket.closeGame();
        else
            i = Integer.parseInt(response2);

        sendLobbyMessage(UUID, j, i);

        waitResponse();

        if (socket.getMessageBuffer().getMessageType() == MessageType.CONFIRM) {

            ConfirmMessage confirmMessage = (ConfirmMessage) socket.getMessageBuffer();
            String s = Integer.toString(confirmMessage.getLobbyId());
            try {
                writer.write(s + "\n");
                writer.flush();
            } catch (IOException e) {
                System.err.println(" Impossible to use resilience ");
            }

        }

        try {
            writer.write(response2 + "\n");
            writer.flush();
            writer.write(response + "\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println(" Impossible to use resilience ");
        }

    }

    /**
     * @param UUID          UUID of the client
     * @param advancedRules If player wants avanced rules
     * @param numPlayers    num of players in the match
     */
    private void sendLobbyMessage(String UUID, boolean advancedRules, int numPlayers) {

        Message message = new LobbyRequestMessage(UUID, numPlayers, advancedRules);

        socket.sendMessage(message);
    }


    /**
     * @param string Nickname to be set for the player
     */
    private void setNickname(String string) {
        nickname = string;
    }

    /**
     * @return My nickname
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * @return HashMap with values
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * @return if server has sent an exception for our assistant
     */
    private boolean playAssistant() {
        int val;

        val = view.askAssistant();
        Message message = new PlayAssistantMessage(UUID, val);

        socket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * @return if server has sent an exception for our movement
     */
    private boolean moveStudents() {

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

            socket.sendMessage(message);
            waitResponse();
            return onMessage();
        } else {

            message = new StudentsToIslandMessage(UUID, container, Integer.parseInt(response.get("islandDest")));

            socket.sendMessage(message);
            waitResponse();
            return onMessage();
        }
    }

    /**
     * @return if server has an exception for the movement
     */
    private boolean moveMotherNature() {

        int islandId;

        islandId = view.askMotherNature();

        Message message = new MoveMotherNatureMessage(UUID, islandId);

        socket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * @return if server has an exception for our choice
     */
    private boolean chooseCloud() {

        String cloudId;

        cloudId = view.askCloud();

        Message message = new ChooseCloudMessage(UUID, cloudId);

        socket.sendMessage(message);

        waitResponse();

        return onMessage();
    }

    /**
     * Method used to set beginGame
     */
    public void beginGame() {
        inGame = true;
    }

    private boolean playCharacter() {

        //TODO DA FARE IL METODO CHE CHIEDE VIEW.ASKCHARACTER E POI CREA UN MESSAGGIO E LO MANDA CON IL PERSONAGGIO SCELTO

        return false;
    }


    /**
     * @return total students moved in action phase in a turn
     */
    public int getTotalStudentsInTurn() {
        return totalStudentsInTurn;
    }

    /**
     * @param num students moved
     */
    public void addTotalStudentsInTurn(int num) {
        totalStudentsInTurn = totalStudentsInTurn + num;
    }


}
