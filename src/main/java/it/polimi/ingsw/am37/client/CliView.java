package it.polimi.ingsw.am37.client;

import java.util.HashMap;
import java.util.Scanner;

public class CliView extends AbstractView {

    /**
     * This method notifies if address is unknown
     *
     * @param address Address written by the player during connection to server
     */
    public void ifNonLocalhostAddress(String address) { //TODO se voglio metto isReachable senza qusta print e richiedo se non è raggiungibile
        if (!address.equals("localhost"))
            System.out.println(" You have put an address different from \"localhost\", if this doesn't exists it will be considered \"localhost\" \n");
    }

    /**
     * Notify if a player has inserted fewer parameters than expected during opening of the terminal
     */
    public void wrongInsertFewArguments() {
        System.out.println(" You have written too few arguments \n");
    }

    /**
     * Generic notification of an input error
     */
    public void wrongInsert() {
        System.out.println(" You have written wrong parameters \n");
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    public void wrongInsertPort() {
        System.out.println(" You haven't written a number as server's port \n");
    }

    /**
     * Notify when a string between "cli" or "gui" was expected but another string was given
     */
    public void wrongInsertGraphics() {
        System.out.println(" You had to choose between \"cli\" or \"gui\" \n");
    }

    /**
     * Notify when requested server is unreachable
     */
    public void wrongServer() {
        System.out.println(" This server is unreachable \n");
    }

    /**
     * Method used to ask if player wants to use default parameters for connection
     *
     * @return Client's response
     */
    public String askDefault() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(" Do you want to use default options? Please write \"yes\" or \"no\" or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his parameters
     *
     * @param address  It's how address parameter is called
     * @param port     It's how port parameter is called
     * @param graphics It's how graphics parameter is called
     * @return Client's decision
     */
    public String insertYourParameters(String address, String port, String graphics) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" Write server's address or \"close game\": \n");
            String s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            ifNonLocalhostAddress(s);
            Client.getParams().put(address, s);
            System.out.println(" Write server's port or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            try {
                int num = Integer.parseInt(s);
                Client.getParams().put(port, Integer.toString(num));
            } catch (NumberFormatException e) {
                wrongInsertPort();
                continue;
            }
            System.out.println(" Write \"cli\" or \"gui\" or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("cli"))
                Client.getParams().put(graphics, "cli");
            else if (s.equals("gui"))
                Client.getParams().put(graphics, "gui");
            else if (s.equals("close game"))
                return s;
            else {
                wrongInsertGraphics();
                continue;
            }

            return "true";
        }
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    public String chooseNickname() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Please insert a nickname or write \"close game\": \n");
        return scanner.nextLine().trim().replaceAll(" +", " ");
    }

    /**
     * Method to notify if client or server has lost the connection
     */
    public void notifyInternetCrash() {
        System.out.println(" Game has lost the connection, it tried to reconnect but it failed. Game is now closing \n");
    }

    /**
     * Method used to ask player the total players of the game he wants to join in
     *
     * @return Player's choice
     */
    public String requestNumPlayers() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(" Please insert number of players you want in your match (yourself included), they can be \"two\" or \"three\" \n. You can also write \"close game\": ");
            s = scanner.nextLine().trim().replaceAll(" +", " ");
            if (s.equals("two"))
                s = "2";
            else if (s.equals("three"))
                s = "3";
            if (s.equals("2") || s.equals("3") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    /**
     * Method used to ask player if he wants to use advanced rules or not
     *
     * @return Player's choice
     */
    public String requestAdvancedRules() {

        String s;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" Please insert \"yes\" or \"no\" according if you want to play with advanced rules or write \"close game\": \n");
            s = scanner.nextLine().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;

            wrongInsert();
        }
    }

    /**
     * Method used to ask which assistant player want to use
     *
     * @return The chosen assistant
     */
    public int askAssistant() { //TODO posso controllare che il numero scelto sia anche compreso in quelli che ho

        Scanner scanner = new Scanner(System.in);
        String s;

        System.out.println(" Please insert the value of your chosen assistant: \n");

        while (true) {

            s = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                if (Integer.parseInt(s) > 0 && Integer.parseInt(s) < 11)
                    return Integer.parseInt(s);

                wrongInsert();
            } catch (NumberFormatException e) {
                wrongInsert();
            }
        }
    }

    /**
     * Method used to tell player possible commands
     */
    public void possibleChoices() {

        System.out.println(" These are your possible inputs, write one of sequent numbers: \n");
        System.out.println(" \"0\" : print this screen again ");
        System.out.println(" \"1\" : close the game ");
        System.out.println(" \"2\" : playing assistant ");
        System.out.println(" \"3\" : moving students ");
        System.out.println(" \"4\" : moving Mother Nature ");
        System.out.println(" \"5\" : picking students from cloud ");

        //TODO


    }

    /**
     * @return Player's command at any time
     */
    public String takeInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine().trim().replaceAll(" +", " ");
    }

    /**
     * Tell player this input isn't ok for now
     */
    public void impossibleInputForNow() {
        System.out.println(" You can't do it now, choose something else \n");
    }

    /**
     * Ask player which students want to move and where
     *
     * @return HashMap with responses of the player
     */
    public HashMap<String, String> askStudents() { //TODO metodo da finire, controllo che muovo in tutto tre studenti e in più devo averli nella entrance quelli scelti

        Scanner scanner = new Scanner(System.in);
        String s1, s2, s3, s4;

        System.out.println(" Select the color of students you want to move, write \"R\" (red) or \"B\" (blue) or \"Y\" (yellow) or \"G\" (green) or \"P\" (pink) \n");

        s1 = scanner.nextLine().trim().replaceAll(" +", " ");

        if (!(s1.equals("r") || s1.equals("b") || s1.equals("y") || s1.equals("p") || s1.equals("g")))
            wrongInsert();

        System.out.println(" Write the number of student/s you want to move ");

        s2 = scanner.nextLine().trim().replaceAll(" +", " ");

        try {
            Integer.parseInt(s2);
        } catch (NumberFormatException e) {
            wrongInsert();
        }

        System.out.println(" Write now \"D\" if you want to move students from entrance to dining or \"I\" for moving to one island \n");

        s3 = scanner.nextLine().trim().replaceAll(" +", " ");

        if (!(s2.equals("d") || s2.equals("i")))
            wrongInsert();

        System.out.println(" Write now \"n\" where n is the number of island, available islands are: "); //TODO faccio una sorta di traduttore, non dico gli id direttamente ma le posizioni(quindi islands.size - 1) e poi traduco andando a vedere l'id dell'isola prima di mandare

        s4 = scanner.nextLine().trim().replaceAll(" +", " ");

        //TODO qui devo controllare che il numero in stringa s3 sia compreso nella dimensione dell'array delle isole, senno rihiedo


        //TODO alla fine faccio che il metodo mi torna una hashmap di stringhe, posizione 0 il colore, posizione 1 il numero, posizione 3 la destinazione, posizione 4 l'isola se ho scelto l'isola
        return null;
    }


    /**
     * @return Where mother nature has to go
     */
    public int askMotherNature() {

        System.out.println("Now choose your destination, available islands are"); //TODO aggiungo il numero di isole available e faccio sempre la traduzione che avevo detto,quindi il return da island id, mentre il mio controllo lo faccio sugli indici della size -1

        return 0;

    }

    /**
     * @return which cloud player has chosen to take
     */
    public String askCloud() {

        System.out.println(" Now choose a cloud to pick "); //TODO faccio il metodo dando la scelta tra le due disponibili sempre col traduttore

        return null;

    }

    /**
     * Tell the player it's his turn
     */
    public void yourTurn() {
        System.out.println(" It's your turn \n");
    }

    /**
     * @param nick nickname of player who has to play the current turn
     */
    public void hisTurn(String nick) {
        System.out.println(" It's " + nick + "'s turn \n");
    }

    /**
     * Method used to tell a player he has to play the assistant card
     */
    public void mustPlayAssistant() {
        System.out.println(" You have to play now an assistant card \n");
    }

    /**
     * Method used to tell the player he is waiting for the match
     */
    public void waitingMatch() {
        System.out.println(" You are now waiting for the start of the game \n");
    }

    /**
     * Method to tell the player the game has begun
     */
    public void gameStarted() {
        System.out.println(" Game is now stared \n");
    }

    /**
     * @param nick the winner player
     */
    public void printWinner(String nick) {
        System.out.println(nick.toUpperCase() + " has won the game!!! Now it's closing \n");

    }


}
