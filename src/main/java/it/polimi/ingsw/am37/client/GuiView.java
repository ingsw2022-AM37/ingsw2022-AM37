package it.polimi.ingsw.am37.client;

import java.util.Scanner;

public class GuiView extends AbstractView {
    public void notifyInternetCrash() {
        //TODO ancora da implementare attraverso JAVAFX
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
        //TODO
    }
}
