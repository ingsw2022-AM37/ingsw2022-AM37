package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.network.ClientSocket;
import java.io.IOException;
import java.util.Scanner;

public class Client {

    /**
     * Client's nickname
     */
    private static String nickname = null;

    /**
     * Net-feature class associated to the client
     */
    private static ClientSocket clientSocket;

    /**
     * It represents if it's this client's turn
     */
    private static boolean isPlaying = false;

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String input;
        String address;
        String port;
        int temp;

        //connection to server written in cli at the start
        try {
            if (args.length == 2) {
                address = args[0];
                port = args[1];

                ClientSocket.connectToServer(address, Integer.parseInt(port));
            }
        }
        //if start connection fails this happens
        catch (IOException e) {
            while (!ClientSocket.isConnectedToServer()) {

                System.out.println("Write 'close game' or 'serverAddress-serverPort' (example: 127.0.0.0-7000)");
                input = scanner.nextLine();

                if (input.equals("close game")) {
                    return;
                } else {
                    for (temp = 0; temp < input.length(); temp++)
                        if (input.charAt(temp) == '-')
                            break;

                    address = input.substring(0, temp);
                    port = input.substring(temp + 1);

                    try {
                        ClientSocket.connectToServer(address, Integer.parseInt(port));
                        break;
                    } catch (IOException r) {
                        System.out.println("A error occurred");
                    }
                }
            }
        }

        //prepare input and output
        ClientSocket.setOutput();
        ClientSocket.setInput();
        if (!ClientSocket.isConnectedToServer())
            return;

        //choose nickname
        while (nickname == null) {

            System.out.println("Insert a nickname");
            input = scanner.nextLine();

            /TODO
            //costruisco la classe messaggio login con nickname l'input usando Gson

            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                ClientSocket.disconnect();
                return;
            }

            /TODO
            // clientsocket si aspetta il messaggio o OK o error, se ok imposta il nickname, altrimenti non
            //fa nulla
        }

        //choose number of players
        do {
            System.out.println("Now choose number of players for the game: '3' or '2' ");
            input = scanner.nextLine();
        } while (Integer.parseInt(input) != 3 && Integer.parseInt(input) != 2);

        //costruiso la classe per mandare il messaggio
        /TODO

        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            ClientSocket.disconnect();
            return;
        }


    }

    /**
     * @param string Nickname to be setted for the player
     */
    static public void setNickname(String string) {
        nickname = string;
    }

    /**
     * @return Nickname of player
     */
    static public String getNickName() {
        return nickname;
    }

}
