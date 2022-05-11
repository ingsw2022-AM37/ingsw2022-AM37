package it.polimi.ingsw.am37.client;

import java.util.HashMap;
import java.util.Scanner;

public class GuiView extends AbstractView {


    public void wrongInsertFewArguments() {
        System.out.println(" You have written too few arguments \n");
    }

    public void wrongInsert() {
        System.out.println(" You have written wrong parameters \n");
    }

    public void wrongInsertPort() {
        System.out.println(" You haven't written a number as server's port \n");
    }

    public void wrongInsertGraphics() {
        System.out.println(" You had to choose between \"cli\" or \"gui\" \n");
    }

    public void wrongServer() {
        System.out.println(" This server is unreachable \n");
    }

    public String askDefault() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(" Do you want to use default options? Please write \"yes\" or \"no\" or \"close game\" \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    public String insertYourParameters(String address, String port, String graphics, HashMap<String, String> params) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" Write server's address or \"close game\" \n");
            String s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            params.put(address, s);
            System.out.println(" Write server's port or \"close game\" \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            try {
                int num = Integer.parseInt(s);
                params.put(port, Integer.toString(num));
            } catch (NumberFormatException e) {
                wrongInsert();
                continue;
            }
            System.out.println(" Write \"cli\" or \"gui\" or \"close game\" \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("cli"))
                params.put(graphics, "cli");
            else if (s.equals("gui"))
                params.put(graphics, "gui");
            else if (s.equals("close game"))
                return s;
            else {
                wrongInsert();
            }
        }
    }

    public String chooseNickname() {
        //TODO
        return null;
    }

    public void notifyInternetCrash() {
        //TODO
    }

    public String requestNumPlayers() {
        //TODO
        return null;
    }

    public String requestAdvancedRules() {
        //TODO
        return null;
    }


}
