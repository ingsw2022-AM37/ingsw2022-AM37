package it.polimi.ingsw.am37.client;

public class CliView extends AbstractView {
    public void notifyInternetCrash() {
        System.out.println("Game has lost the connection, it tried to reconnect but it failed. Game is now closing");
    }


}
