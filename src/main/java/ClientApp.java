import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.PlayerAbortException;

import java.util.Locale;

/**
 * Used to launch the game
 */
public class ClientApp {

    /**
     * address name
     */
    final static String addressOptionName = "address";

    /**
     * port name
     */
    final static String portOptionName = "port";

    /**
     * graphics name
     */
    final static String graphicsOptionName = "graphics";

    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        Client client;
        Locale.setDefault(Locale.ENGLISH);
        try {
            client = parseArgs(args);
            if (client != null) client.start();
        } catch (PlayerAbortException e) {
            System.err.println("As requested, game is now closing");
        }
    }

    /**
     * Parse the strings provided when launching the executables and create a Client instance if possible: if no
     * arguments are provided creates a GUI Client, and then it will ask connection parameters; if some arguments are
     * provided but badly formatted or if graphic is not provided print a guide page and return null client; if all
     * provided argument
     *
     * @param args the executable provided options
     * @return a Client objects: may be null if badly formatted arguments are present or graphics isn't provided,
     * otherwise a client instance with all correct provided arguments
     * @throws PlayerAbortException when the player desire to end game
     */
    public static Client parseArgs(String[] args) throws PlayerAbortException {
        boolean badFormatted = false;
        boolean noData = false;
        String address = null;
        String port = null;
        String graphics = null;
        if (args.length == 0) noData = true;
        else if (args.length % 2 == 1) badFormatted = true;
        else {
            for (int i = 0; i < args.length; i = i + 2) {
                if (args[i].equals("--" + addressOptionName) || args[i].equals("-" + addressOptionName.charAt(0)))
                    address = args[i + 1];
                else if (args[i].equals("--" + portOptionName) || args[i].equals("-" + portOptionName.charAt(0)))
                    port = args[i + 1];
                else if (args[i].equals("--" + graphicsOptionName) ||
                        args[i].equals("-" + graphicsOptionName.charAt(0))) graphics = args[i + 1];
                else badFormatted = true;
            }

        }
        if (noData) return new Client(null, null, "GUI");
        else if (badFormatted || graphics == null) printUsage();
        else return new Client(address, port, graphics);
        return null;
    }

    /**
     * This print a short display screen to inform about the correct usage of the run arguments
     */
    public static void printUsage() {
        System.out.println("Impossible start with provided arguments. Possible arguments (order doesn't matter)");
        System.out.println("\t-p\t--port\t\tthe port of server");
        System.out.println("\t-a\t--address\t\tthe address of server");
        System.out.println("\t-g\t--graphics\t\tthe UI mode {\"GUI\",\"CLI\"}");
    }
}
