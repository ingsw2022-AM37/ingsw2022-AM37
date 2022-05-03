package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    private static boolean end = false;

    private static String nickname = null;

    private static ClientSocket socket;

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
            while (true) {

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

        //Thread is running, the client is reading possible messages
        socket.start();

        //choose nickname
        while (nickname == null) {

            System.out.println("Insert a nickname");
            input = scanner.nextLine();

            /TODO
            //costruisco la classe messaggio login con nickname l'input usando Gson

            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                System.out.println("A connection error occurred, game is now closing");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException d) {
                    System.out.println("A connection error occurred, game is now closing");
                }
                end = true;
                return;
            }

            try {
                nickname.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //choose number of players
        do {
            System.out.println("Now choose number of players for the game: '3' or '2' ");
            input = scanner.nextLine();
        } while (Integer.parseInt(input) != 3 && Integer.parseInt(input) != 2);

        //costruiso la classe per mandare il messaggio
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("A connection error occurred, game is now closing");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException d) {
                System.out.println("A connection error occurred, game is now closing");
            }
            end = true;
            return;
        }


    }

    static public void setNickname(String string) {
        nickname = string;
    }

    static public String getNickName() {
        return nickname;
    }

    static public boolean getEnd() {
        return end;
    }
}
