package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.EffectHandler;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class CliView extends AbstractView {
    /**
     * Construct a new console type view, registering and enabling the Jansi library output stream above the
     * {@link System#out} standard stream.
     */
    public CliView() {
        AnsiConsole.systemInstall();
        Runtime.getRuntime().addShutdownHook(new Thread(AnsiConsole::systemUninstall));
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    /**
     * Draw an assistant
     *
     * @param assistant the assistant to draw
     */
    protected static void drawAssistant(Assistant assistant) {
        System.out.print(ansi().fgCyan().a(assistant).reset());
    }

    /**
     * Draw a player's board
     *
     * @param board the board to draw
     */
    protected static void drawBoard(Board board) {
        System.out.println("\tEntrance: " + ansi().render(board.getEntrance().getStudentsAsString()));
        System.out.println("\tDining: " + ansi().render(board.getDiningRoom().getStudentsAsString()));
        System.out.println("\tTowers: " + ansi().render(board.getTowers().getTowersAsString()));
    }

    /**
     * Draw a cloud
     *
     * @param cloud the cloud to draw
     */
    protected static void drawCloud(Cloud cloud) {
        System.out.println(ansi().a("☁️Cloud ").a(cloud.getCloudId()).a(": ").render(cloud.getStudentsAsString()));
    }

    /**
     * Draw an island
     *
     * @param island the island to draw
     */
    protected static void drawIsland(Island island) {
        System.out.print(ansi().a("🏝️  Isola ")
                .a(island.getIslandId())
                .a(" (dim ")
                .a(island.getNumIslands())
                .a("): "));
        System.out.print(ansi().render(island.getStudentsOnIsland().getStudentsAsString()));
        System.out.print(ansi().a("\t Tower: "));
        if (island.getCurrentTower() != TowerColor.NONE) System.out.print(ansi().render(
                "@|" + island.getCurrentTower().color + " " + island.getCurrentTower().name() + "|@"));
        else System.out.print("❌");
        if (island.getMotherNatureHere()) System.out.print(ansi().a(" ⬅ mother nature"));
        System.out.println();
    }

    /**
     * Draw a character
     *
     * @param character the character to draw
     */
    protected static void drawCharacter(Character character) {
        System.out.print(
                "\t " + character.getEffectType().name().toLowerCase() + ": price " + character.getCurrentPrice() +
                        " coins");
        if (character.getState().getContainer() != null) System.out.print(
                "\t students: " + ansi().render(character.getState().getContainer().getStudentsAsString()));
        else if (character.getState().getNoEntryTiles() != EffectHandler.DEFAULT_NOENTRYTILES)
            System.out.print("\t no entry tales: " + character.getState().getNoEntryTiles());
    }

    /**
     * Method to notify if client or server has lost the connection
     */
    public void notifyInternetCrash() {
        System.out.println("Game has lost the connection, it tried to reconnect but it failed. Game is now closing");
    }

    /**
     * Generic notification of an input error
     */
    public void wrongInsert() {
        System.out.println("You have written wrong parameters");
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    public void wrongInsertPort() {
        System.out.println("You haven't written a number as server's port");
    }

    /**
     * Notify when a string between "cli" or "gui" was expected but another string was given
     */
    public void wrongInsertGraphics() {
        System.out.println("You had to choose between \"cli\" or \"gui\"");
    }

    /**
     * Notify when requested server is unreachable
     */
    public void wrongServer() {
        System.out.println("This server is unreachable");
    }

    /**
     * Method used to ask a player which character he wants to play
     *
     * @return the effect of the selected character
     */
    public Effect askCharacter() {
        displayInfo("Which character you want to play? ");
        Scanner scanner = new Scanner(System.in);
        int response = scanner.nextInt();
        return ((Character) reducedModel.getCharacters().toArray()[response]).getEffectType();
    }

    /**
     * Method used to ask if player wants to use default parameters for connection
     *
     * @return Client's response
     */
    @Override
    public Boolean askConfirm(String message) {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(ansi().render(message));
            System.out.print(ansi().render(" Please insert @|bold,italic yes|@ or @|bold,italic no|@ or @|bold,italic" +
                    " close game|@: "));
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            switch (s.toLowerCase()) {
                case "yes", "y" -> {
                    return true;
                }
                case "no", "n" -> {
                    return false;
                }
                case "close game" -> {
                    return null;
                }
                default -> wrongInsert();
            }
        }
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    public String chooseNickname() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please insert a nickname or write \"close game\": ");
        return scanner.nextLine().trim().replaceAll(" ", "");
    }

    @Override
    public Client.LobbyParameters askLobbyParameters() {
        Boolean advancedRules = askConfirm(
                "Do you want to play with advanced rules?");
        if (advancedRules == null) return null;
        int numPlayers = 0;
        while (numPlayers != 2 && numPlayers != 3) {
            System.out.print("Please insert number of players you want in your match (yourself included), they can" +
                    " be \"two\" or \"three\". \nYou can also write \"close game\": ");
            String input = new Scanner(System.in).nextLine().trim().replaceAll(" +", " ");
            switch (input.toLowerCase()) {
                case "two", "2" -> numPlayers = 2;
                case "three", "3" -> numPlayers = 3;
                case "close game" -> {
                    return null;
                }
                default -> numPlayers = -1;
            }
        }
        return new Client.LobbyParameters(advancedRules, numPlayers);
    }

    /**
     * Method used to ask which assistant player want to use
     *
     * @return The chosen assistant
     */
    public int askAssistant(Client client) {
        Scanner scanner = new Scanner(System.in);
        String s;
        int num;
        showDeck(client.getView().getReducedModel().getPlayers().get(client.getNickname()));

        while (true) {

            System.out.print("Please insert the value of your chosen assistant: ");

            s = scanner.nextLine().trim().replaceAll(" +", " ");

            try {
                num = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }

            if (getReducedModel().getPlayers().get(client.getNickname()).getAssistantsDeck().containsKey(num))
                return num;
            else if (getReducedModel().getPlayers().get(client.getNickname()).getAssistantsDeck().containsKey(num) &&
                    num > 0 && num < 11) System.out.println("You have already played this assistant");
            else System.out.println("You don't have this assistant");

        }
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
     *
     * @return Client's decision
     */
    @Override
    public Client.ConnectionParameters askConnectionParameters() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Write server's address or \"close game\": ");
            String addressInput = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (addressInput.equals("close game")) return null;
            if (!Objects.equals(addressInput.toLowerCase(), "localhost")) displayImportant("i.notLocalhost");
            System.out.print("Write server's port or \"close game\": ");
            String portInput = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (portInput.equals("close game")) return null;
            try {
                int port = Integer.parseInt(portInput);
                if (port > 65535 || port < 1) throw new NumberFormatException();
                return new Client.ConnectionParameters(addressInput, port);
            } catch (NumberFormatException e) {
                wrongInsertPort();
            }
        }
    }

    @Override
    public int askIsland() {
        Scanner scanner = new Scanner(System.in);
        int input;
        displayInfo("Available islands are:");
        for (Island island :
                reducedModel.getIslands()) {
            drawIsland(island);
        }
        while (true) {
            displayInfo("Which island do you want? Please insert its index");
            input = scanner.nextInt();
            int finalInput = input;
            if (reducedModel.getIslands().stream().filter(i -> i.getIslandId() == finalInput).findFirst().isEmpty()) {
                displayError("The island with the provided id is not present");
            } else {
                return input;
            }
        }
    }

    @Override
    public FactionColor askColor(Client client) {
        String input;
        Scanner scanner = new Scanner(System.in);
        Optional<FactionColor> color;
        while (true) {
            displayInfo("Select one of the color available: write @|red,bold R|@ or @|blue,bold B|@ or " +
                    "@|yellow,bold Y|@ or @|green,bold G|@ or @|magenta,bold P|@");
            input = scanner.nextLine().trim().replaceAll(" +", " ");
            String finalInput = input;
            color = Arrays.stream(FactionColor.values()).filter(c -> c.name().charAt(0) ==
                    finalInput.toUpperCase().charAt(0)).findFirst();
            if (color.isEmpty()) {
                displayError(client.getMessageString("e.wrongColor"));
            } else {
                break;
            }
        }
        return color.get();
    }

    @Override
    public StudentsContainer askStudentFromDining(Client client, int num) {
        int students;
        String input;
        Scanner scanner = new Scanner(System.in);
        StudentsContainer container = new LimitedStudentsContainer(num);
        showPlayerStatus(getReducedModel().getPlayers().get(client.getNickname()), client.getSettings()
                .advancedRulesEnabled());

        while (true) {
            displayInfo("You have to move " + (num - container.size()) + " students");
            displayInfo("Select the color of students you want to move, write @|red,bold R|@ or @|blue,bold B|@ or " +
                    "@|yellow,bold Y|@ or @|green,bold G|@ or @|magenta,bold P|@");
            input = scanner.nextLine().trim().replaceAll(" +", " ");
            String finalInput = input;
            Optional<FactionColor> color = Arrays.stream(FactionColor.values()).filter(c -> c.name().charAt(0) ==
                    finalInput.toUpperCase().charAt(0)).findFirst();
            if (color.isEmpty()) {
                displayError(client.getMessageString("e.wrongColor"));
                if (!askConfirm("Do you want to try again to move some students?"))
                    return null;
                continue;
            } else {
                displayInfo("Write the number of students you want to move of color " + color.get());
                try {
                    students = Integer.parseInt(scanner.nextLine().trim().replaceAll(" +", " "));
                    if (students > num || students > reducedModel.getPlayers()
                            .get(client.getNickname())
                            .getBoard()
                            .getDiningRoom()
                            .getByColor(color.get())) {
                        displayError("You have tried to move to much students");
                        if (!askConfirm("Do you want to try again to move some students?"))
                            return null;
                        continue;
                    }
                    container.addStudents(students, color.get());
                } catch (NumberFormatException e) {
                    displayError(client.getMessageString("e.wrongNumber"));
                    if (!askConfirm("Do you want to try again to move some students?"))
                        return null;
                    continue;
                }
            }
            if (container.size() == num)
                break;
        }
        return container;
    }

    @Override
    public StudentsContainer askStudentsFromCharacter(Character character, int num, Client client) {
        int students;
        String input;
        Scanner scanner = new Scanner(System.in);
        StudentsContainer container = new LimitedStudentsContainer(num);
        drawCharacter(character);

        while (true) {
            displayInfo("You have to move " + (num - container.size()) + " students");
            displayInfo("Select the color of students you want to move, write @|red,bold R|@ or @|blue,bold B|@ or " +
                    "@|yellow,bold Y|@ or @|green,bold G|@ or @|magenta,bold P|@");
            input = scanner.nextLine().trim().replaceAll(" +", " ");
            String finalInput = input;
            Optional<FactionColor> color = Arrays.stream(FactionColor.values()).filter(c -> c.name().charAt(0) ==
                    finalInput.toUpperCase().charAt(0)).findFirst();
            if (color.isEmpty()) {
                displayError(client.getMessageString("e.wrongColor"));
                if (!askConfirm("Do you want to try again to move some students?"))
                    return null;
                continue;
            } else {
                displayInfo("Write the number of students you want to move of color " + color.get());
                try {
                    students = Integer.parseInt(scanner.nextLine().trim().replaceAll(" +", " "));
                    if (students > num || students > character.getState().getContainer().getByColor(color.get())) {
                        displayError("You have tried to move to much students");
                        if (!askConfirm("Do you want to try again to move some students?"))
                            return null;
                        continue;
                    }
                    container.addStudents(students, color.get());
                } catch (NumberFormatException e) {
                    displayError(client.getMessageString("e.wrongNumber"));
                    if (!askConfirm("Do you want to try again to move some students?"))
                        return null;
                    continue;
                }
            }
            if (container.size() == num)
                break;
        }
        return container;
    }

    /**
     * Ask players to provide a group of students
     *
     * @return HashMap with responses of the player
     */
    public StudentsContainer askStudentsFromEntrance(Client client, int num) {
        int students;
        String input;
        Scanner scanner = new Scanner(System.in);
        StudentsContainer container = new LimitedStudentsContainer(num == 0 ?
                GameManager.MAX_FOR_MOVEMENTS - client.getTotalStudentsInTurn() : num);
        showPlayerStatus(getReducedModel().getPlayers().get(client.getNickname()), client.getSettings()
                .advancedRulesEnabled());

        while (true) {

            displayInfo("You have to move " + (num == 0
                    ? (GameManager.MAX_FOR_MOVEMENTS - client.getTotalStudentsInTurn())
                    : num) +
                    " in this turn");

            displayInfo("Select the color of students you want to move, write @|red,bold R|@ or @|blue,bold B|@ or " +
                    "@|yellow,bold Y|@ or @|green,bold G|@ or @|magenta,bold P|@");
            input = scanner.nextLine().trim().replaceAll(" +", " ");
            String finalInput = input;
            Optional<FactionColor> color = Arrays.stream(FactionColor.values()).filter(c -> c.name().charAt(0) ==
                    finalInput.toUpperCase().charAt(0)).findFirst();
            if (color.isEmpty()) {
                displayError(client.getMessageString("e.wrongColor"));
                if (!askConfirm("Do you want to try again to move some students?"))
                    return null;
                continue;
            } else {
                displayInfo("Write the number of students you want to move of color " + color.get());
                try {
                    students = Integer.parseInt(scanner.nextLine().trim().replaceAll(" +", " "));
                    if (students > (num == 0 ? GameManager.MAX_FOR_MOVEMENTS - client.getTotalStudentsInTurn() : num) ||
                            students >
                                    reducedModel.getPlayers()
                                            .get(client.getNickname())
                                            .getBoard()
                                            .getEntrance()
                                            .getByColor(color.get())) {
                        displayError("You have tried to move to much students");
                        if (!askConfirm("Do you want to try again to move some students?"))
                            return null;
                        continue;
                    }
                    container.addStudents(students, color.get());
                } catch (NumberFormatException e) {
                    displayError(client.getMessageString("e.wrongNumber"));
                    if (!askConfirm("Do you want to try again to move some students?"))
                        return null;
                    continue;
                }
            }

            if (num == 0) {
                client.addTotalStudentsInTurn(students);
                if (client.getTotalStudentsInTurn() == GameManager.MAX_FOR_MOVEMENTS ||
                        !askConfirm("Do you want to move more students?"))
                    break;
            } else if (container.size() == num)
                break;
        }
        return container;
    }

    /**
     * @return Where mother nature has to go
     */
    public int askMotherNature() {

        Scanner scanner = new Scanner(System.in);
        String response;
        int numResponse;

        System.out.println("Now choose your destination, available islands are :");
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
                if (island.getIslandId() == numResponse) return numResponse;

            System.out.println("You have written an invalid Island, please try again: \n");

        }

    }

    /**
     * @return which cloud player has chosen to take
     */
    public String askCloud() {

        String response;
        Scanner scanner = new Scanner(System.in);
        showTable();
        System.out.print("Now choose a cloud to pick, number \"1\" or \"2\" or \"3\":");


        while (true) {
            response = scanner.nextLine().trim().replaceAll(" +", " ");
            if (response.equals("1") || response.equals("2") || response.equals("3")) return response;
        }

    }

    /**
     * Tell the player it's his turn
     */
    public void yourTurn() {
        System.out.println("\nIt's your turn. Please press enter to proceed...");
    }

    @Override
    public void displayInfo(String message) {
        System.out.println(ansi().fgDefault().render(message).reset());
    }

    @Override
    public void displayImportant(String message) {
        System.out.println(ansi().fgYellow().render(message).reset());
    }

    @Override
    public void displayError(String message) {
        System.out.println(ansi().fgRed().render(message).reset());
    }

    @Override
    public void updateView(UpdateMessage updateMessage, Client client) {
        reducedModel.update(updateMessage.getUpdatedObjects()
                .values()
                .stream()
                .flatMap(List::stream)
                .toList());
    }

    /**
     * @param nick nickname of player who has to play the current turn
     */
    public void hisTurn(String nick) {
        System.out.println("\nIt's " + nick + "'s turn");
    }

    /**
     * Method used to tell the player he is waiting for the match
     */
    public void waitingMatch() {
        System.out.println("You are now waiting for the start of the game");
    }

    /**
     * Method to tell the player the game has begun
     */
    public void gameStarted() {
        displayImportant(messagesConstants.getProperty("i.gameStart"));
    }

    /**
     * @param nick the winner player
     */
    public void printWinner(String nick) {
        System.out.println(nick.toUpperCase() + "has won the game!!!");

    }

    /**
     * This method print on console a colorful representation of the last assistant played and his entire board
     *
     * @param player        the players to show status of
     * @param advancedRules if the advanced rules are enabled or not
     */
    @Override
    public void showPlayerStatus(Player player, boolean advancedRules) {
        System.out.println(player.getPlayerId() + " status:");
        if (player.getLastAssistantPlayed() != null) {
            drawAssistant(player.getLastAssistantPlayed());
            System.out.println();
        }
        if (player.getBoard() != null) drawBoard(player.getBoard());
        if (advancedRules) System.out.println("Coins: " + player.getNumberOfCoins());
    }

    /**
     * Method used to ask which player you want to look at
     *
     * @return chosen player
     */
    public Player askPlayer() {

        Scanner scanner = new Scanner(System.in);
        String response;

        showPlayersNicknames();

        System.out.println("choose now one nickname from above: ");

        while (true) {
            response = scanner.nextLine().trim().replaceAll(" +", " ");
            for (String nickname : getReducedModel().getPlayers().keySet())
                if (nickname.equals(response)) return getReducedModel().getPlayers().get(nickname);

            System.out.println("You have to choose one nickname from available! Please try again");
        }
    }

    /**
     * Method used to show players in game
     */
    public void showPlayersNicknames() {

        System.out.println("Players in this game are: ");
        for (String nickname : getReducedModel().getPlayers().keySet())
            System.out.println(nickname);

        System.out.println("\n");


    }

    /**
     * Method used to display connection info
     *
     * @param client the client to display info about
     */
    public void showConnection(Client client) {

        System.out.println("Your are current connected to: " + client.getAddress() + ":" + client.getPort());

    }

    /**
     * This function draw the current status of the table for all players point of view: draw all islands and clouds
     */
    @Override
    public void showTable() {
        for (Island island : reducedModel.getIslands()) {
            drawIsland(island);
        }
        for (Cloud cloud : reducedModel.getClouds().values()) {
            drawCloud(cloud);
        }
    }

    /**
     * @return Player's command at any time
     */
    public ActionType takeInput(Client client) {
        List<ActionType> actions = ActionType.getActionByStatus(client.getStatus(), client.getSettings()
                .advancedRulesEnabled());
        //System.out.println(ansi().eraseScreen());
        System.out.flush();
        System.out.println("Current available actions:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(ansi().a("\t").bold().a(i).reset().a(":\t" + actions.get(i).description));
        }
        System.out.print("Please insert the number of the desired action: ");
        try {
            String input = new Scanner(System.in).nextLine().trim().replaceAll(" +", " ");
            return actions.get(Integer.parseInt(input));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return ActionType.SHOW_MENU;
        }
    }

    /**
     * Show the assistant deck of the provided player. Each player must see only it's deck
     *
     * @param player the player to show the deck
     */
    @Override
    public void showDeck(Player player) {
        System.out.println("Your deck is: [");
        List<Assistant> assistants = player.getAssistantsDeck().values().stream().toList();
        for (int i = 0; i < assistants.size(); i++) {
            if (i % 2 == 0) System.out.print("\t");
            drawAssistant(assistants.get(i));
            if (i != assistants.size() - 1) System.out.print(", ");
            if (i % 2 == 1 || i == assistants.size() - 1) System.out.println();
        }
        System.out.println("]");
    }

    /**
     * Show all the characters in the client model
     */
    @Override
    public void showCharacters() {
        for (int i = 0; i < reducedModel.getCharacters().size(); i++) {
            System.out.print(ansi().bold().a(i).reset().a("\t:"));
            drawCharacter((Character) reducedModel.getCharacters().toArray()[i]);
            System.out.println();
        }
    }
}
