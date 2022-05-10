package it.polimi.ingsw.am37.client;

import java.util.Scanner;

public class CliView extends AbstractView {
    public void notifyInternetCrash() {
        System.out.println(" Game has lost the connection, it tried to reconnect but it failed. Game is now closing \n");
    }

    public String wrongInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" You have written one or more invalid arguments, please insert: ''   -port 'port number' -address 'server address' -graphics 'cli or gui'   '' in any order \n");
        System.out.println(" For closing the game write: 'close' \n");
        return scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
    }

    public String wrongServer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Server is unreachable, retry or write 'close' for exit the game \n");
        return scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
    }

    public String chooseNickname() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Please insert a nickname or write 'close' for closing the game: \n");
        return scanner.nextLine().trim().replaceAll(" +", " ");
    }


}
