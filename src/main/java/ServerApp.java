import it.polimi.ingsw.am37.network.server.Server;

import java.util.Arrays;

public class ServerApp {
    private static void parseArgument (String args) {

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.loadServer(60000);
    }
}
