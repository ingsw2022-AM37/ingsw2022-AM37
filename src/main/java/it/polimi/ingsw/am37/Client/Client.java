package it.polimi.ingsw.am37.Client;

import it.polimi.ingsw.am37.Network.ClientSocket;

import java.util.Scanner;
import java.io.IOException;
import java.net.Socket;

public class Client {

    String nickname;

    ClientSocket socket;

    public Client() {

        socket = new ClientSocket(this);

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        String input = null;
        String address = null;
        String port = null;
        int temp;

        //connection to server written in cli at the start
        try {
            if (args.length == 2) {
                address = args[0];
                port = args[1];

                client.socket.connectToServer(address, Integer.parseInt(port));
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
                        client.socket.connectToServer(address, Integer.parseInt(port));
                        break;
                    } catch (IOException r) {
                    }

                }
            }
        }

        //scelta nome


    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickName() {
        return this.nickname;
    }

    public void login(String nickname) {

    }

}
