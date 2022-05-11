package it.polimi.ingsw.am37.client;

import java.util.HashMap;

public abstract class AbstractView {

    public abstract void notifyInternetCrash();

    public abstract void wrongInsertFewArguments();

    public abstract void wrongInsert();

    public abstract void wrongInsertPort();

    public abstract void wrongInsertGraphics();

    public abstract void wrongServer();

    public abstract String askDefault();

    public abstract String insertYourParameters(String address, String port, String graphics, HashMap<String, String> params);

    public abstract String chooseNickname();

    public abstract String requestAdvancedRules();

    public abstract String requestNumPlayers();

}
