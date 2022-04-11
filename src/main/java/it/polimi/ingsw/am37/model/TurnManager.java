package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.AssistantImpossibleToPlay;
import it.polimi.ingsw.am37.model.exceptions.NoIslandConquerorException;
import it.polimi.ingsw.am37.model.exceptions.NoProfChangeException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TurnManager {

    /**
     * If you can take a professor even if there is a draw
     */
    boolean profWithDraw;

    /**
     * Players in the game
     */
    private ArrayList<Player> players;

    /**
     * Who is playinh
     */
    private Player currentPlayer;

    /**
     * If coins are enabled in the game
     */
    private boolean coinsEnabled;

    /**
     * Total number of players
     */
    private int numOfPlayers;

    /**
     * Map to memorize who used profWithDraw to steal professors, at the end of the turn it will be reverted
     */
    private HashMap<FactionColor, Player> stolenProf;

    /**
     * Players who haven't played in the round yet
     */
    private ArrayList<Player> notPlayedInRound;

    /**
     * Players in the order of when they choose their assistant
     */
    private ArrayList<Player> orderPlayed;

    /**
     * Default constructor
     */
    public TurnManager(boolean coinsEnabled, int numOfPlayers) {
        this.players = new ArrayList<>();
        this.coinsEnabled = coinsEnabled;
        this.numOfPlayers = numOfPlayers;
        this.profWithDraw = false;
    }


    /**
     * This method moves students and checks also the professors and coins
     *
     * @param container The students added to current player's dining room
     */
    public void addStudentsToDining(StudentsContainer container) {

        currentPlayer.getBoard().getDiningRoom().uniteContainers(container);

        if (coinsEnabled) {
            for (int i = 0; i < currentPlayer.getBoard().calculateCoin(currentPlayer.getBoard().getDiningRoom()); i++) {
                currentPlayer.receiveCoin();
            }
        }
        for (FactionColor color : FactionColor.values()) {
            if (container.getByColor(color) > 0) {
                checkProfessors(color);
            }
        }
    }

    /**
     * This method also checks for professors, so if you lose students you can lose their professor too
     *
     * @param container The students needed to be removed from currentPlayer dining room
     * @throws NoProfChangeException When there is a draw situation
     */
    public void removeStudentsFromDining(LimitedStudentsContainer container) throws NoProfChangeException {

        HashMap<Player, Integer> playerPower = new HashMap<>();
        boolean[] controlledProf;
        Player exProfOwner = null;
        int boolToInt;
        boolean switchProf = false;
        int numStudentsControlling = 0;
        int max1 = 0;
        int max2 = 0;
        Player playerMax1 = null;
        Player playerMax2 = null;
        Player currentProfOwner = null;


        currentPlayer.getBoard().getDiningRoom().removeContainer(container);
        if (coinsEnabled) currentPlayer.getBoard().checkCoins(currentPlayer.getBoard().getDiningRoom());


        for (FactionColor color : FactionColor.values()) {
            if (container.getByColor(color) > 0) {

                playerPower = new HashMap<>();
                exProfOwner = null;
                switchProf = false;
                numStudentsControlling = 0;
                max1 = 0;
                max2 = 0;
                playerMax1 = null;
                playerMax2 = null;
                currentProfOwner = null;

                for (Player player : players) {
                    controlledProf = player.getBoard().getProfTable();
                    boolToInt = player.getBoard().getDiningRoom().getByColor(color);
                    if (controlledProf[color.getIndex()])
                        currentProfOwner = player;
                    playerPower.put(player, boolToInt);
                }

                for (Player player : players) {
                    if (playerPower.get(player) > max1) {
                        max2 = max1;
                        playerMax2 = playerMax1;
                        max1 = playerPower.get(player);
                        playerMax1 = player;
                    } else if (playerPower.get(player) > max2) {
                        max2 = playerPower.get(player);
                        playerMax2 = player;
                    }
                }
                if (max1 == max2 && max1 > playerPower.get(currentProfOwner) && playerMax1.getBoard().getTowers().getCurrentTower() != currentProfOwner.getBoard().getTowers().getCurrentTower() && playerMax2.getBoard().getTowers().getCurrentTower() != currentProfOwner.getBoard().getTowers().getCurrentTower())
                    throw new NoProfChangeException();

                numStudentsControlling = playerPower.get(currentProfOwner);
                exProfOwner = currentProfOwner;

                for (Player player : players)
                    if (playerPower.get(player) > numStudentsControlling && player.getBoard().getTowers().getCurrentTower() != currentProfOwner.getBoard().getTowers().getCurrentTower()) {
                        currentProfOwner = player;
                        numStudentsControlling = playerPower.get(player);
                        switchProf = true;
                    }

                if (switchProf) {
                    exProfOwner.getBoard().removeProf(color);
                    currentProfOwner.getBoard().addProf(color);
                }
            }
        }
    }

    /**
     * @param assistant Assistant played by the currentPlayer
     */
    public void useAssistant(Assistant assistant) {
        currentPlayer.useAssistant(assistant);
    }

    /**
     * This method prepare players, boards, currentPlayer and initialize other objects
     *
     * @param bag Bag containing total students of the game
     */
    public void setUp(Bag bag) {

        Random random = new Random();

        for (int cont = 0; cont < numOfPlayers; cont++)
            players.add(new Player());

        final int studentEntranceThreePlayers = 9;
        final int studentEntranceTwoPlayers = 7;

        int i = 0;
        for (Player player : players) {
            player.setBoard(new Board(numOfPlayers, TowerColor.values()[i], coinsEnabled, player));
            i++;
        }

        if (numOfPlayers == 2 || numOfPlayers == 4)
            for (Player player : players) {
                player.getBoard().addStudentsToEntrance(bag.extractStudents(studentEntranceTwoPlayers));
            }

        else
            for (Player player : players) {
                player.getBoard().addStudentsToEntrance(bag.extractStudents(studentEntranceThreePlayers));
            }

        this.currentPlayer = getPlayers().get(random.nextInt(getPlayers().size()));

        this.orderPlayed = new ArrayList<>();
        this.notPlayedInRound = new ArrayList<>();

        orderPlayed.add(0, currentPlayer);

        stolenProf = new HashMap<>();

        notPlayedInRound.addAll(players);

    }

    /**
     * @param cloud Cloud from which you remove students and move them to currentPlayer's entrance
     */
    public void studentCloudToEntrance(Cloud cloud) {
        currentPlayer.getBoard().addStudentsToEntrance(cloud.removeStudents());
    }

    /**
     * @param island    Island to where you move students
     * @param container Students you have picked from currentPlayer's entrance
     */
    public void studentsEntranceToIsland(Island island, LimitedStudentsContainer container) {
        currentPlayer.getBoard().removeStudentsFromEntrance(container);
        FixedUnlimitedStudentsContainer temp = new FixedUnlimitedStudentsContainer();
        for (FactionColor color : FactionColor.values())
            temp.addStudents(container.getByColor(color), color);
        island.addStudents(temp);
    }

    /**
     * @return Total players in the game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Flag to conquer professors even if you have same the number of students of your opponent
     */
    public void setProfWithDraw() {
        this.profWithDraw = true;
    }

    /**
     * Method used for deleting all characters' effects
     */
    public void resetFlags() {
        this.profWithDraw = false;
        for (FactionColor color : FactionColor.values())
            if (stolenProf.containsKey(color)) {
                stolenProf.get(color).getBoard().addProf(color);
                currentPlayer.getBoard().removeProf(color);
                stolenProf.remove(color);
            }
    }

    /**
     * @return The currentPlayer
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * @param player Player to be set as currentPlayer
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * This method is used to check after adding students to dining room if it's possible to take control of a professor
     *
     * @param color Faction of students you want to check professors
     */
    public void checkProfessors(FactionColor color) {
        boolean switchProf = false;
        boolean profNotAssigned = true;
        Player exControllingStudents = null;

        for (Player player : players)
            if (player.getBoard().getProfTable()[color.getIndex()])
                profNotAssigned = false;

        if (profNotAssigned) {
            currentPlayer.getBoard().addProf(color);
        } else {
            if (!currentPlayer.getBoard().getProfTable()[color.getIndex()]) {
                for (Player player : players) {
                    if (profWithDraw) {
                        if (currentPlayer.getBoard().getDiningRoom().getByColor(color) >= player.getBoard().getDiningRoom().getByColor(color) && player.getBoard().getProfTable()[color.getIndex()] && currentPlayer.getBoard().getTowers().getCurrentTower() != player.getBoard().getTowers().getCurrentTower()) {
                            exControllingStudents = player;
                            stolenProf.put(color, player);
                            switchProf = true;
                        }
                    } else {
                        if (currentPlayer.getBoard().getDiningRoom().getByColor(color) > player.getBoard().getDiningRoom().getByColor(color) && player.getBoard().getProfTable()[color.getIndex()] && currentPlayer.getBoard().getTowers().getCurrentTower() != player.getBoard().getTowers().getCurrentTower()) {
                            exControllingStudents = player;
                            switchProf = true;
                        }
                    }
                }
            }
            if (switchProf) {
                exControllingStudents.getBoard().removeProf(color);
                currentPlayer.getBoard().addProf(color);
            }
        }
    }

    //Idea: notPlayedInRound ha tutti i player che devono ancora giocare il proprio turno, ma ovviamente in questo
    // metodo hanno tutti giocato il proprio assistente di già. Devo dunque controllare chi ha l'assistente
    // con il numero più basso tra quelli presenti in notPlayedInRound e fare setCurrentPlayer, poi lo tolgo da
    // notPlayedInRound. All'inizio del metodo faccio un if, se notPlayedInRound è vuoto lo riempo con tutti i player.
    // mi salvo in firstToChoseAssistant il primo che tolgo da notPlayedInRound --> NO
    // HO CAMBIATO: ora c'è orderPlayed: firstToChoseAssistant è orderPlayed.get(0), questa modifca serve:
    // ad esempio gli ultimi due giocatori hanno giocato un assistente uguale perchè obbligati, chi inizia?
    // quello che ha giocato prima l'assistente, ovvero fai il controllo di chi ha l'assistente più basso,
    // se due o più hanno il più basso uguale allora
    //1) prendi chi di quelli viene prima in orderPlayed
    // 2) ELIMINI il giocatore che è settato come currentPlayer da orderPlad --> IMPORTANTE!!!

    //--> //praticamente OderPlayed si riempe nel metodo sotto uno a uno e si svuota qui uno a uno, invece notPlayedInRound si riempono
    //e svuotano in entrambi i metodi(uno riempe tutto e toglie uno a uno e laltro viceversa
    public void nextTurnPlayer() {

        if (notPlayedInRound.isEmpty()) {
            notPlayedInRound.addAll(players);
        }


    }

    // anche qui uso notPlayedInRound, serve un if all'inizio per riempire l'array quando questo è vuoto.
    // scorro tutti i giocatori ( senza fare controlli specifici) PARTENDO da currentPlayer( penso quindi che devi usare gli indici e un contatore pari a size
    // , es: current = 1, parto da 1, faccio 2 e poi torno a 0) e chiamo useAssistant, man mano li tolgo da currentlayer e li aggiungi
    // in ordine in orderPlayed
    public void nextTurnAssistant(Assistant assistant) throws AssistantImpossibleToPlay {

        //quando faccio il ciclo dei Players per vedere che l'assistente che ho giocato
        //(parametro in input del metodo) sia diverso da tutti gli altri lastAssistant mi ricordo di
        // escludere il player stesso, o con torre o con currentPlayer
        //dopo il primo if, se non c'è nessun assistante tra i last degli altri lo gioco
        // altrimenti serve un altro if per controllare tra i deck degli altri giocatori
        //(escludo sempre il corrente) se c'era una possibilità di giocare un assistente
        // diverso i lastassistant degli altri giocatori, se c'era lancio l'eccezione,
        // altrimenti gioco la carta
        //IMPORTANTE: mi salvo anche l'ordine in cui i giocatori hanno giocato l'assistente,
        // questo perchè a fronte di un pareggio possibile in nextTurnPlayer gioca chi ha usato prima
        // l'assistente, quindi ho tolto l'indice mentre ho messo un arraylist di players ordinato in base
        // all'ordine con cui hanno giocato gli assistenti


        // NEI TEST USA setUp PERCHE LA CREAZIONE DEGLI ARRAYLIST LA FA IN SETUP, NON NEL COSTRUTTORE!!!

        boolean neverEqualToOtherAssistant = false;

        if (notPlayedInRound.isEmpty()) {
            notPlayedInRound.addAll(players);
            this.currentPlayer = orderPlayed.get(0);
            for (Player player : players)
                player.setLastAssistantPlayedNull();
        }

        if (notPlayedInRound.size() == numOfPlayers)
            orderPlayed.add(0, currentPlayer);

    }

}