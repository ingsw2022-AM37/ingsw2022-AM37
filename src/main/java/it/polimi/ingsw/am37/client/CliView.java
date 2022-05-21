package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CliView extends AbstractView {

    /**
     * This method notifies if address is unknown
     *
     * @param address Address written by the player during connection to server
     */
    public void ifNonLocalhostAddress(String address) {
        if (!address.equals("localhost"))
            System.out.println(" You have put an address different from \"localhost\", if this doesn't exists it will" +
                    " be considered \"localhost\" \n");
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
            System.out.println(" Do you want to use default options? Please write \"yes\" or \"no\" or \"close " +
                    "game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
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
        System.out.println(" Game has lost the connection, it tried to reconnect but it failed. Game is now closing " +
                "\n");
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
            System.out.println(" Please insert number of players you want in your match (yourself included), they can" +
                    " be \"two\" or \"three\" \n. You can also write \"close game\": ");
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
            System.out.println(" Please insert \"yes\" or \"no\" according if you want to play with advanced rules or" +
                    " write \"close game\": \n");
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
    public int askAssistant() {

        Scanner scanner = new Scanner(System.in);
        String s;
        boolean present = false;
        int num;


        while (true) {

            System.out.println(" Please insert the value of your chosen assistant: \n");

            s = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                num = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }

            if (getReducedModel().getPlayers().get(Client.getNickname()).getAssistantsDeck().containsKey(num))
                return num;
            else if (getReducedModel().getPlayers().get(Client.getNickname()).getAssistantsDeck().containsKey(num) &&
                    num > 0 && num < 11)
                System.out.println(" You have already played this assistant \n");
            else
                wrongInsert();

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
        System.out.println(" \"6\" : playing a character");

        //TODO in questo stile mancano i metodi che si riferiscono ai vari update per stampare le situazioni correnti
        // (le varie view)


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
    public HashMap<String, String> askStudents() {

        HashMap<String, String> response;
        Scanner scanner = new Scanner(System.in);
        String s1, s2, s3, s4;
        FactionColor color = null;
        boolean ok;

        while (true) {

            response = new HashMap<>();
            System.out.println(" Select the color of students you want to move, write \"R\" (red) or \"B\" (blue) or " +
                    "\"Y\" (yellow) or \"G\" (green) or \"P\" (pink) \n");

            s1 = scanner.nextLine().trim().replaceAll(" +", " ");

            if (!(s1.equals("r") || s1.equals("b") || s1.equals("y") || s1.equals("p") || s1.equals("g"))) {
                wrongInsert();
                continue;
            }

            response.put("color", s1);


            switch (s1) {
                case "r" -> color = FactionColor.RED;
                case "b" -> color = FactionColor.BLUE;
                case "g" -> color = FactionColor.GREEN;
                case "y" -> color = FactionColor.YELLOW;
                case "p" -> color = FactionColor.PINK;
            }

            System.out.println(" Write the number of student/s you want to move ");

            s2 = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                Integer.parseInt(s2);
                if (Integer.parseInt(s2) + Client.getTotalStudentsInTurn() > 3 ||
                        getReducedModel().getBoards().get(Client.getNickname()).getEntrance().getByColor(color) <
                                Integer.parseInt(s2)) {
                    wrongInsert();
                    continue;
                }
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }

            Client.addTotalStudentsInTurn(Integer.parseInt(s2));

            response.put("number", s2);

            System.out.println(" Write now \"D\" if you want to move students from entrance to dining or \"I\" for " +
                    "moving to one island \n");

            s3 = scanner.nextLine().trim().replaceAll(" +", " ");

            if (!(s3.equals("d") || s2.equals("i"))) {
                wrongInsert();
                continue;
            }

            if (s3.equals("d"))
                return response;

            System.out.println(" Write now \"n\" where n is the number of island, available islands are: ");
            for (Island island : getReducedModel().getIslands())
                System.out.println(" Island " + island.getIslandId());

            s4 = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                Integer.parseInt(s4);
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }

            ok = false;
            for (Island island : getReducedModel().getIslands())
                if (island.getIslandId() == Integer.parseInt(s4)) {
                    response.put("islandDest", s4);
                    ok = true;
                }

            if (ok)
                return response;
            else
                wrongInsert();
        }
    }


    /**
     * @return Where mother nature has to go
     */
    public int askMotherNature() {

        Scanner scanner = new Scanner(System.in);
        String response;
        int numResponse;

        System.out.println(" Now choose your destination, available islands are : \n");
        for (Island island : getReducedModel().getIslands())
            System.out.println(" Island " + island.getIslandId());

        while (true) {

            response = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                numResponse = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }

            for (Island island : getReducedModel().getIslands())
                if (island.getIslandId() == numResponse)
                    return numResponse;

            System.out.println(" You have written an invalid Island, please try again: \n");

        }

    }

    /**
     * @return which cloud player has chosen to take
     */
    public String askCloud() {

        String response;
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Now choose a cloud to pick, number \"1\" or \"2\" or \"3\" :  \n");


        while (true) {
            response = scanner.nextLine().trim().replaceAll(" +", " ");
            if (response.equals("1") || response.equals("2") || response.equals("3"))
                return response;
        }

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

    /**
     * Method used to ask a player which character he wants to play
     */
    public void askCharacter() {
        //TODO
    }


    /**
     * Method used when an error message come from server
     */
    public void impossibleAssistant() {
        System.out.println("You can't play this assistant now, try another one \n");
    }

    /**
     * Method used when an error message come from server
     */
    public void impossibleStudents() {
        System.out.println(" You can't move these students \n");
    }

    /**
     * Method used when an error message come from server
     */
    public void impossibleMotherNature() {
        System.out.println(" You can't move mother nature to that position \n");
    }


    /**
     * Method used when an error message come from server
     */
    public void impossibleCloud() {
        System.out.println(" You can't choose this cloud \n");
    }

    /**
     * Method used when an error message come from server
     */
    public void impossibleCharacter() {
        System.out.println(" You can't play a character now \n");
    }

    /**
     * This function draw the current status of the table from the current player's point of view: draw all islands,
     * boards and players status
     */
    @Override
    public void showTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        AnsiConsole.systemInstall();
        AnsiConsole.out().print(Ansi.ansi().eraseScreen());
        for (Island island :
                reducedModel.getIslands()) {
            drawIsland(island);
        }
        for (Cloud cloud :
                reducedModel.getClouds().values()) {
            drawCloud(cloud);
        }
        AnsiConsole.systemUninstall();
    }

    protected void drawIsland(Island island) {
        System.out.print(Ansi.ansi()
                .a("🏝️  Isola ")
                .a(island.getIslandId())
                .a(" (dim ")
                .a(island.getNumIslands())
                .a("): "));
        System.out.print(Ansi.ansi().render(island.getStudentsOnIsland().getStudentsAsString()));
        if (island.getMotherNatureHere()) System.out.println(Ansi.ansi().a(" ⬅ mother nature"));
        System.out.println();
    }

    protected void drawCloud(Cloud cloud) {
        System.out.println(Ansi.ansi().a("☁️Cloud ").a(cloud.getCloudId()).a(": ").render(cloud.getStudentsAsString()));
    }

    protected void drawAssistant(Assistant assistant) {
        System.out.print(Ansi.ansi().fgCyan().a(assistant).reset());
    }

    protected void drawBoard(Board board) {
        System.out.println("\tEntrace: " + Ansi.ansi().render(board.getEntrance().getStudentsAsString()));
        System.out.println("\tDining: " + Ansi.ansi().render(board.getDiningRoom().getStudentsAsString()));
        System.out.println("\tTowers: " + Ansi.ansi().render(board.getTowers().getTowersAsString()));
    }

    /**
     * This method print on console a colorful representation of the last assistant played and his entire board
     *
     * @param player the players to show status of
     */
    @Override
    public void showPlayerStatus(Player player) {
        System.out.println(player.getPlayerId() + " status:");
        if (player.getLastAssistantPlayed() != null) {
            drawAssistant(player.getLastAssistantPlayed());
            System.out.println();
        }
        if (player.getBoard() != null) drawBoard(player.getBoard());
    }

    @Override
    public void showDeck(Player player) {
        System.out.println("Your deck is: [");
        List<Assistant> assistants = player.getAssistantsDeck().values().stream().toList();
        for (int i = 0; i < assistants.size(); i++) {
            if (i % 2 == 0)
                System.out.print("\t");
            drawAssistant(assistants.get(i));
            if (i != assistants.size() - 1)
                System.out.print(", ");
            if (i % 2 == 1 || i == assistants.size() - 1)
                System.out.println();
        }
        System.out.println("]");
    }
}
