import it.polimi.ingsw.am37.network.server.Server;

import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Server launcher
 */
public class ServerApp {
    /**
     * Port's value
     */
    static private String portValue;

    /**
     * @param args the arguments received in input.
     */
    public static void main(String[] args) {
        Server server = new Server();
        parseArgument(args);
        server.loadServer(Integer.parseInt(portValue));
    }

    /**
     * @param args the arguments received in input.
     */
    private static void parseArgument(String[] args) {
        boolean wrongInitialInput = tryConnectionWithArgs(args);
        tryConnectionAgain(wrongInitialInput);
    }

    /**
     * @param args It's the array of string created by main class
     * @return if initial input was wrong
     */
    public static boolean tryConnectionWithArgs(String[] args) {
        final int expectedArguments = 2;
        int i = 0;
        boolean wrongInitialInput = false;
        String portString = "port";

        List<String> list = Arrays.stream(args).map(String::toLowerCase).toList();
        args = list.toArray(new String[0]);
        if (args.length != expectedArguments) {
            wrongInsertFewArguments();
            wrongInitialInput = true;
        } else {
            while (i < args.length) {
                if (!(args[i].equals("--" + portString))) {
                    wrongInsert();
                    wrongInitialInput = true;
                    break;
                }
                portValue = args[i + 1];
                i = i + 2;
            }
            try {
                Integer.parseInt(portValue);
            } catch (NumberFormatException e) {
                wrongInsertPort();
                wrongInitialInput = true;
            }
        }
        return wrongInitialInput;
    }

    /**
     * tries to get the parameters again.
     *
     * @param wrongInitialInput If initial input was wrong
     */
    public static void tryConnectionAgain(boolean wrongInitialInput) {
        final int defaultPort = 60000;
        String response;

        while (wrongInitialInput) {
            response = askDefault();
            if ((response.equals("exit")))
                closeGame();
            else {
                if (response.equals("y")) {
                    portValue = Integer.toString(defaultPort);
                } else {
                    response = insertYourParameters();
                    if (response.equals("exit"))
                        closeGame();
                }
            }
            wrongInitialInput = false;
        }
    }

    /**
     * Method used if server manager decided to don't use default setting for connection, so he will be asked to insert his parameters
     *
     * @return Server's decision
     */
    private static String insertYourParameters() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(ansi().render("Write server's @|bold,italic port|@ or @|bold,italic exit|@:"));
            String s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("exit"))
                return s;
            try {
                int num = Integer.parseInt(s);
                portValue = Integer.toString(num);
            } catch (NumberFormatException e) {
                wrongInsertPort();
                continue;
            }
            return "true";
        }
    }

    /**
     * Method used to ask if server manager wants to use default parameters for connection
     *
     * @return Response
     */
    private static String askDefault() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(ansi().render("Do you want to use default options? @|bold,italic y/n|@ or @|bold,italic exit|@:"));
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("y") || s.equals("n") || s.equals("exit"))
                return s;
            wrongInsert();
        }
    }

    private static void closeGame() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 2000);
        Runtime.getRuntime().halt(0);
        timer.cancel();
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    private static void wrongInsertPort() {
        System.out.println("You haven't written a number as server's port");
    }

    /**
     * Notify if a player has inserted fewer parameters than expected during opening of the terminal
     */
    private static void wrongInsertFewArguments() {
        System.out.println("You have written too few arguments");
    }

    /**
     * Generic notification of an input error
     */
    private static void wrongInsert() {
        System.out.println("You have written wrong parameters");
    }
}
