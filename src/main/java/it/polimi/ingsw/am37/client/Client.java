package it.polimi.ingsw.am37.client;

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

        boolean notReady = true;
        String graphics = null;
        int port;
        String address;
        final int expectedArguments = 6;
        view = new CliView();
        //first block only used in first while(notReady), when you are connecting to the server

        int i = 0;


        //begin of first part of the code, you are trying to connect to the server
        List<String> list = Arrays.stream(args).map(String::toLowerCase).toList();

        initialInfo:
        while (notReady) {

            final Map<String, String> params = new HashMap<>();
            args = list.toArray(new String[0]);

            if (args.length == 1 && args[0].equals("close"))
                return;

            if (args.length != expectedArguments) {
                list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                continue initialInfo;
            }

            while (i < args.length) {

                if (!(args[i].equals("-port") || args[i].equals("-address") || args[i].equals("-graphics"))) {
                    list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                    continue initialInfo;
                } else if (args[i + 1].charAt(0) != '-')
                    params.put(args[i].substring(1), args[i + 1]);

                else {
                    list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                    continue initialInfo;
                }

                i = i + 2;
            }

            if (!(params.containsKey("address") && params.containsKey("graphics") && params.containsKey("port"))) {
                list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                continue initialInfo;
            }

            try {
                port = Integer.parseInt(params.get("port"));
            } catch (NumberFormatException e) {
                list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                continue initialInfo;
            }

            address = params.get("address");
            graphics = params.get("graphics");

            if (!graphics.equals("cli") && !graphics.equals("gui")) {
                list = new ArrayList<>(Arrays.asList(view.wrongInsert().split(" ")));
                continue initialInfo;
            }


            try {
                ClientSocket.connectToServer(address, port);
                notReady = false;
            } catch (IOException e) {
                list = new ArrayList<>(Arrays.asList(view.wrongServer().split(" ")));
            }

        }

        if (graphics.equals("gui"))
            view = new GuiView();

        //start listening thread
        new Thread(new ClientSocket()).start();

        //choose nickname
        while (nickname == null) {

            String tempNick;

            tempNick = view.chooseNickname();

            if (tempNick.equals("close"))
                //TODO in clientSocket un closeGame che e uguale a disconnect ma non dice che la connessione e stata persa

                Message message = new LoginMessage(UUID, tempNick);

            try {
                ClientSocket.getWaitObject().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();  //TODO che gli faccio fare qua? chiudo il gioco?
            }

            if (ClientSocket.getMessageBuffer().getMessageType() == MessageType.ERROR)
                ;
            else
                setNickname(tempNick);
        }

        //choose lobby

        //TODO chiedo al giocatore il numero di giocatori con cui vuole giocare e se vuole usare le regole avanzate. In questo caso dopo aver inviato il messaggio non mi aspetto una conferma


    }


    /**
     * @param string Nickname to be setted for the player
     */
    static public void setNickname(String string) {
        nickname = string;
    }


}
