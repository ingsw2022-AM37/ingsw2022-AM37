package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.am37.client.gui.observer.GuiObserver.ClickableObjectType.*;
import static java.lang.Math.*;
import static java.util.Map.entry;

/**
 * Controller of main scene during the game
 */
public class GameSceneController extends GenericController {

    /**
     * The vertically space constants between two students in the dining room
     */
    protected final static double ySpaceStudentsDining = 42;
    /**
     * The horizontal space constants between two towers in the board
     */
    protected final static double xSpaceTowers = 50;
    /**
     * The vertically space constants between two towers in the board
     */
    protected final static double ySpaceTowers = 65;
    /**
     * The horizontal space constants between two students in the entrance room
     */
    protected final static double xSpaceStudents = 59;
    /**
     * The horizontal space constants between two row of students in the entrance room
     */
    protected final static double ySpaceStudentsEntrance = 50;
    /**
     * The coordiantes of the first student in the entrance room
     */
    protected final static Coordinate firstEntranceCoordinate = new Coordinate(57, 42);
    /**
     * The coordiantes of the first tower in the board
     */
    protected final static Coordinate firstTowerCoordinate = new Coordinate(107, 740);
    /**
     * How many students or tower the first line could contains
     */
    protected final static int firstLineEntranceAndTowersSize = 4;
    /**
     * The dimension of the tower image
     */
    protected static final Dimension towerDimension = new Dimension(50, 28);
    /**
     * The dimension of the student and professors image
     */
    protected static final Dimension studentAndProfDimension = new Dimension(40, 40);
    /**
     * A static map with the image for each professor color
     */
    protected final static Map<FactionColor, Image> profImageFromColor = Map.ofEntries(entry(FactionColor.BLUE,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/BlueTeacher.png")))), entry(FactionColor.PINK,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/PinkTeacher.png")))), entry(FactionColor.GREEN,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/GreenTeacher.png")))), entry(FactionColor.YELLOW,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets" +
                            "/images/YellowTeacher.png")))), entry(FactionColor.RED,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" + "/RedTeacher.png")))));


    //-------------------------------------------------------------------------------
    //Position and dimensions information
    /**
     * A static map with the coordinates for each professor
     */
    protected final static Map<FactionColor, Coordinate> profCoordinateFromColor =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(55, 636)), entry(FactionColor.PINK,
                            new Coordinate(115, 636)), entry(FactionColor.GREEN, new Coordinate(295, 636)),
                    entry(FactionColor.YELLOW, new Coordinate(175, 636)), entry(FactionColor.RED, new Coordinate(235,
                            636)));
    /**
     * A static map with the coordinates for students badge of each color over the character
     */
    protected final static Map<FactionColor, Coordinate> studentShiftsForCharacter =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(98, 31)), entry(FactionColor.PINK,
                            new Coordinate(15, 101)), entry(FactionColor.GREEN, new Coordinate(98, 101)),
                    entry(FactionColor.YELLOW, new Coordinate(15, 31)), entry(FactionColor.RED, new Coordinate(57,
                            136)));
    /**
     * A static map with the image for each student color
     */
    protected final static Map<FactionColor, Image> studentImageFromColor = Map.ofEntries(entry(FactionColor.BLUE,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/BlueStudent.png")))), entry(FactionColor.PINK,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/PinkStudent.png")))), entry(FactionColor.GREEN,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/GreenStudent.png")))), entry(FactionColor.YELLOW,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets" +
                            "/images/YellowStudent.png")))), entry(FactionColor.RED,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" + "/RedStudent.png")))));
    /**
     * A static map with the coordinates for of first student of each color
     */
    protected final static Map<FactionColor, Coordinate> studentDiningFirstCoordinatesFromColor =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(57, 177)), entry(FactionColor.PINK,
                            new Coordinate(116, 177)), entry(FactionColor.GREEN, new Coordinate(293, 177)),
                    entry(FactionColor.YELLOW, new Coordinate(175, 177)), entry(FactionColor.RED, new Coordinate(234,
                            177)));
    /**
     * A static map with the image for each tower color
     */
    protected final static Map<TowerColor, Image> towerImageFromColor = Map.ofEntries(entry(TowerColor.BLACK,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/BlackTower.png")))), entry(TowerColor.GRAY,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/GrayTower.png")))), entry(TowerColor.WHITE,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" + "/WhiteTower.png"))))

    );
    /**
     * Id prefix of each island fxid, should be followed by its game id
     */
    private final static String ISLAND_PREFIX = "island_";
    /**
     * Id prefix of each character fxid, should be followed by the name of its effect
     */
    private final static String CHARACTER_PREFIX = "character_";
    /**
     * Id prefix of each cloud fxid, should be followed by its game id
     */
    private final static String CLOUD_PREFIX = "cloud_";
    /**
     * Id prefix of each card in the assistan deck fxid, should be followed by its cardvalue
     */
    private final static String DECK_PREFIX = "deck_";
    /**
     * The font of each label
     */
    private final static Font labelFont = new Font("System Bold", 20);
    /**
     * The coordinates of the island circle center
     */
    private final static Coordinate islandCircleCenter = new Coordinate(850, 340);
    /**
     * A list with the clouds (if game has only two players use only elements 0 and 1)
     */
    private final static List<Coordinate> cloudsCoordinate = Arrays.asList(new Coordinate(680, 340),
            new Coordinate(880, 340), new Coordinate(780, 540));
    /**
     * The coordinates to shif the no entry tile from the center of the island
     */
    private final static Coordinate shiftNoEntryTile = new Coordinate(29, 90);
    /**
     * The coordinates to shif the mother nature image from the center of the island
     */
    private final static Coordinate shiftMotherNature = new Coordinate(50, 0);
    /**
     * The coordinates to shif the the tower image from the center of the island
     */
    private final static Coordinate shiftTowerIsland = new Coordinate(2, 0);
    /**
     * A list of coordinates to shif the additional price of each character from the top left of the character
     */
    private final static List<Coordinate> additionalPriceCharacters = Arrays.asList(new Coordinate(1460, 800),
            new Coordinate(1604, 800), new Coordinate(1748, 800));
    /**
     * The coordinate of the coin image
     */
    private final static Coordinate coinCoordinate = new Coordinate(1172, 869);
    /**
     * The coordinates to shif the no entry tile from the center of the island
     */
    private final static int distanceCharactersCards = 144;
    /**
     * The dimension of the cloud image
     */
    private static final Dimension cloudDimension = new Dimension(150, 150);
    /**
     * The dimension of the island image
     */
    private static final Dimension islandDimension = new Dimension(166.6, 166.6);
    /**
     * The dimension of the mother nature image
     */
    private static final Dimension motherNatureDimension = new Dimension(60, 50);
    /**
     * The dimension of the no entry tile image
     */
    private static final Dimension noEntryDimension = new Dimension(30, 30);
    /**
     * The dimension of the assistant and character card image
     */
    private static final Dimension assistantAndCharacterDimension = new Dimension(166.66, 113.33);
    /**
     * The dimension of the coin image
     */
    private static final Dimension coinDimension = new Dimension(105, 95);
    /**
     * The dimension of the islands circle
     */
    private static final int maxRadiusIslands = 350;
    /**
     * The image of the cloud
     */
    private final static Image cloudImage =
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" + "/Cloud.png")));
    /**
     * A static map that associate character effect name to their images
     */
    private final static Map<String, Image> characterImageFromName =
            Map.ofEntries(entry(Effect.MONK.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/MONK" +
                                    ".jpg")))), entry(Effect.GRANDMA.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/GRANDMA.jpg")))), entry(Effect.JESTER.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/JESTER" +
                                    ".jpg")))), entry(Effect.PRINCESS.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/PRINCESS.jpg")))), entry(Effect.FARMER.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/FARMER" +
                                    ".jpg")))), entry(Effect.HERALD.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/HERALD" +
                                    ".jpg")))), entry(Effect.MAGIC_POSTMAN.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/MAGIC_POSTMAN.jpg")))), entry(Effect.CENTAUR.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/CENTAUR.jpg")))), entry(Effect.KNIGHT.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/KNIGHT" +
                                    ".jpg")))), entry(Effect.MUSHROOM_MAN.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/MUSHROOM_MAN.jpg")))), entry(Effect.MINSTREL.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters" +
                                    "/MINSTREL.jpg")))), entry(Effect.THIEF.getCharacterName(),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                            "/assets/images/characters/THIEF" + ".jpg")))));
    /**
     * A static map that associated card value with its assistant image
     */
    private final static Map<Integer, Image> assistantImageFromValue = Map.ofEntries(entry(1,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant1.png")))), entry(2,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant2.png")))), entry(3,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant3.png")))), entry(4,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant4.png")))), entry(5,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant5.png")))), entry(6,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant6.png")))), entry(7,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant7.png")))), entry(8,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant8.png")))), entry(9,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant9.png")))), entry(10,
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/assistants" + "/Assistant10.png")))));
    /**
     * A static list that contains available island images (3 available)
     */
    private final static List<Image> islandImage =
            Arrays.asList(new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images/Island1" +
                            ".png"))), new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" +
                            "/Island2.png"))),
                    new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets" + "/images/Island3.png"))));
    /**
     * The mother nature image
     */
    private final static Image motherNatureImage =
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets" + "/images/MotherNature.png")));
    /**
     * The no entry tile image
     */
    private final static Image noEntryTileImage =
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets" + "/images/NoEntryTile.png")));
    /**
     * The coin image
     */
    private final static Image coinImage =
            new Image(Objects.requireNonNull(GameSceneController.class.getResourceAsStream(
                    "/assets/images" + "/Coin.png")));
    //-------------------------------------------------------------------------------
    //Stand alone images
    /**
     * A static map that associated each color with the shif of the color image from the center of the island
     */
    private final static Map<FactionColor, Coordinate> studentsShiftPositionsOnIslandsAndClouds =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(140, 41)), entry(FactionColor.PINK,
                            new Coordinate(16, 116)), entry(FactionColor.GREEN, new Coordinate(114, 116)),
                    entry(FactionColor.YELLOW, new Coordinate(-10, 41)), entry(FactionColor.RED, new Coordinate(63,
                            -9)));

    //-------------------------------------------------------------------------------
    //Link between info and images or coordinates
    /**
     * The mother nature image view
     */
    private final static ImageView motherNature = new ImageView(motherNatureImage);
    /**
     * The mother nature image view
     */
    private final static ImageView coin = new ImageView(coinImage);
    /**
     * A supoport for firing property change events
     */
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    /**
     * The controller of the scene that show others player board
     */
    private final BoardsController boardsController;
    /**
     * The root pane of the scene and also the background
     */
    public Pane wallpaperPane;
    /**
     * Grid where to inser the assistant deck of the player
     */
    public GridPane assistantsGrid;
    /**
     * The label that present the characters
     */
    public Label charactersLabel;
    /**
     * The hbox layout that contains the available characters
     */
    public HBox charactersHBox;
    /**
     * The hbox layout that contains the last assistant played
     */
    public HBox assistantsHBox;


    //-------------------------------------------------------------------------------
    //ImageView of everything
    private ArrayList<ImageView> studentsEntranceView = new ArrayList<>();
    private ArrayList<ImageView> studentsDiningView = new ArrayList<>();
    private ArrayList<ImageView> towersBoardView = new ArrayList<>();
    private ArrayList<ImageView> professorsView = new ArrayList<>();
    private ArrayList<ImageView> cloudsView = new ArrayList<>();
    private ArrayList<ImageView> islandsView = new ArrayList<>();
    private ArrayList<ImageView> totalStudentsOnIslandsView = new ArrayList<>();
    private ArrayList<ImageView> totalStudentsOnCloudsView = new ArrayList<>();
    private ArrayList<Label> totalLabelsOnIslandsView = new ArrayList<>();
    private ArrayList<Label> totalLabelsOnCloudsView = new ArrayList<>();
    private ArrayList<ImageView> noEntryView = new ArrayList<>();
    private ArrayList<ImageView> deckAssistantsView = new ArrayList<>();
    private ArrayList<ImageView> charactersView = new ArrayList<>();
    private ArrayList<ImageView> charactersInfoView = new ArrayList<>();
    private ArrayList<Label> totalCharactersLabelsView = new ArrayList<>();
    private ArrayList<ImageView> assistantPlayedView = new ArrayList<>();
    private ArrayList<Label> totalLabelsAssistantsView = new ArrayList<>();
    private ArrayList<ImageView> towerIslandView = new ArrayList<>();
    /**
     * The label tha show the number of coins of each player
     */
    public Label numOfCoins;
    /**
     * Save the status of the other boards scene if visible or not
     */
    private boolean isOtherBoardVisible = false;

    /**
     * Default constructor of the game scene controller that link button actions and load the FXML file
     */
    public GameSceneController() {
        motherNature.setOnMouseClicked(this::motherNatureClicked);
        motherNature.setCursor(Cursor.HAND);
        Stage othersWindows = new Stage();
        othersWindows.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/scenes/Boards.fxml"));
        try {
            othersWindows.setScene(new Scene(loader.load()));
            this.boardsController = loader.getController();
            othersWindows.setOnCloseRequest((event) -> {
                isOtherBoardVisible = false;
                othersWindows.hide();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Place an imageView at the provided coordiantes
     *
     * @param coordinate the coordinates to place the object to
     * @param temp       the object to place
     */
    protected static void placeObjectNoAnimation(Coordinate coordinate, ImageView temp) {

        temp.setLayoutX(coordinate.x);
        temp.setLayoutY(coordinate.y);
    }

    /**
     * Se the dimension provided to the image view
     *
     * @param dimension the dimendion of the image view
     * @param temp      the image view to set dimension on
     */
    protected static void drawWithDimension(Dimension dimension, ImageView temp) {
        temp.setFitWidth(dimension.width);
        temp.setFitHeight(dimension.height);
    }

    //-------------------------------------------------------------------------------

    /**
     * The callback of an assistant in the deck when clicked
     */
    public void assistantDeckClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(DECK_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_ASSISTANT.name(), null, id);
        }
    }

    /**
     * Utils function that remove labels from view
     *
     * @param label an array list of label to remove
     */
    private void cancelLabelsFromPane(ArrayList<Label> label) {
        for (Label value : label) {
            wallpaperPane.getChildren().remove(value);
        }
    }

    /**
     * Utils function that remove image view from the root panel
     *
     * @param view the arraylist of objects to remove
     */
    private void cancelVisibleViewWallpaperPane(ArrayList<ImageView> view) {
        for (int i = 0; i < view.size(); i++) {
            wallpaperPane.getChildren().remove(view.get(i));
        }
    }

    /**
     * Utils function to change the number of the coins displayed
     *
     * @param num the number to display
     */
    public void changeCoins(int num) {
        numOfCoins.setText(Integer.toString(num));
        numOfCoins.setVisible(true);
    }

    /**
     * Callback for character clicked on
     *
     * @param mouseEvent the event of the mouse that clicked this character
     */
    public void characterClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(CHARACTER_PREFIX + "(.+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_CHARACTER.name(), null, id);
        }
    }

    /**
     * Callback for clouds clicked on
     *
     * @param mouseEvent the event of the mouse that clicked this clous
     */
    public void cloudClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(CLOUD_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_CLOUD.name(), null, id);
        }
    }

    /**
     * Callback for dining clicked on
     *
     * @param mouseEvent the event of the mouse that clicked this character
     */
    public void diningClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_DINING.name(), null, null);
    }


    //-------------------------------------------------------------------------------
    //Methods for drawing my board

    /**
     * Fill the main board with data from various fields
     *
     * @param entrance   the entrande of the board
     * @param dining     the dining room of the board
     * @param professors the professors table as an array of boolean
     * @param towers     the tower cotainers of this board
     */
    public void drawBoard(HashMap<FactionColor, Integer> entrance, HashMap<FactionColor, Integer> dining,
                          boolean[] professors, LimitedTowerContainer towers) {
        drawEntrance(entrance);
        drawDining(dining);
        drawProfessors(professors);
        drawTowers(towers);
    }

    /**
     * Draw a list of characters
     *
     * @param characters the characters to draw as a list
     */
    public void drawCharacters(List<Character> characters) {

        cancelVisibleViewWallpaperPane(charactersInfoView);
        charactersInfoView = new ArrayList<>();
        charactersLabel.setVisible(true);
        cancelLabelsFromPane(totalCharactersLabelsView);
        totalCharactersLabelsView = new ArrayList<>();
        charactersView = new ArrayList<>();
        charactersHBox.getChildren().clear();


        int i = 0;
        int var = 0;

        for (Character c : characters) {
            ImageView imageView = new ImageView();
            imageView.setId(CHARACTER_PREFIX + c.getEffectType());
            imageView.setOnMouseClicked(this::characterClicked);
            imageView.setCursor(Cursor.HAND);
            imageView.setImage(characterImageFromName.get(c.getEffectType().getCharacterName()));
            drawWithDimension(assistantAndCharacterDimension, imageView);
            charactersHBox.getChildren().add(imageView);
            charactersView.add(imageView);


            int temp = c.getCurrentPrice() - c.getEffectType().getInitialPrice();
            Label labelPriceAdditional = new Label("+" + temp);
            labelPriceAdditional.setTextFill(Paint.valueOf("#ffffff"));
            labelPriceAdditional.setFont(labelFont);
            totalCharactersLabelsView.add(labelPriceAdditional);
            labelPriceAdditional.setLayoutX(additionalPriceCharacters.get(i).x);
            labelPriceAdditional.setLayoutY(additionalPriceCharacters.get(i).y);
            wallpaperPane.getChildren().add(labelPriceAdditional);

            Label label = new Label(c.getEffectType().name());
            label.setFont(labelFont);
            label.setRotate(270);
            charactersHBox.getChildren().add(new Group(label));
            totalCharactersLabelsView.add(label);

            //Here starts images over characters with special specs

            if (c.getEffectType().getCharacterName().equals(Effect.MONK.getCharacterName()) ||
                    c.getEffectType().getCharacterName().equals(Effect.JESTER.getCharacterName()) ||
                    c.getEffectType().getCharacterName().equals(Effect.PRINCESS.getCharacterName()) ||
                    c.getEffectType().getCharacterName().equals(Effect.MUSHROOM_MAN.getCharacterName()) ||
                    c.getEffectType().getCharacterName().equals(Effect.THIEF.getCharacterName())) {

                Bounds bounds = imageView.localToScene(imageView.getBoundsInLocal());
                for (FactionColor color : FactionColor.values()) {
                    ImageView student = new ImageView();
                    student.setImage(studentImageFromColor.get(color));
                    student.setMouseTransparent(true);
                    drawWithDimension(studentAndProfDimension, student);
                    placeObjectNoAnimation(new Coordinate(
                            bounds.getMinX() + var + studentShiftsForCharacter.get(color).x,
                            bounds.getMinY() + studentShiftsForCharacter.get(color).y), student);
                    wallpaperPane.getChildren().add(student);
                    charactersInfoView.add(student);

                    if (c.getEffectType().getCharacterName().equals(Effect.MONK.getCharacterName()) ||
                            c.getEffectType().getCharacterName().equals(Effect.JESTER.getCharacterName()) ||
                            c.getEffectType().getCharacterName().equals(Effect.PRINCESS.getCharacterName())) {
                        Label numStudents = new Label(Integer.toString(c.getState().getContainer().getByColor(color)));
                        numStudents.setFont(labelFont);
                        numStudents.setTextFill(Paint.valueOf("#ffffff"));
                        numStudents.setLayoutX(bounds.getMinX() + var + studentShiftsForCharacter.get(color).x);
                        numStudents.setLayoutY(bounds.getMinY() + studentShiftsForCharacter.get(color).y);
                        numStudents.setMouseTransparent(true);
                        wallpaperPane.getChildren().add(numStudents);
                        totalCharactersLabelsView.add(numStudents);
                    }
                }
            } else if (c.getEffectType().getCharacterName().equals(Effect.GRANDMA.getCharacterName())) {

                Bounds bounds = imageView.localToScene(imageView.getBoundsInLocal());
                ImageView noEntryTile = new ImageView();
                noEntryTile.setImage(noEntryTileImage);
                drawWithDimension(noEntryDimension, noEntryTile);
                placeObjectNoAnimation(new Coordinate(bounds.getMinX() + var + 25, bounds.getMaxY() - 20), noEntryTile);
                wallpaperPane.getChildren().add(noEntryTile);
                charactersInfoView.add(noEntryTile);
                noEntryTile.setMouseTransparent(true);

                Label numNoEntry = new Label(Integer.toString(c.getState().getNoEntryTiles()));
                numNoEntry.setFont(labelFont);
                numNoEntry.setTextFill(Paint.valueOf("#ffffff"));
                numNoEntry.setLayoutX(bounds.getMinX() + var + 25);
                numNoEntry.setLayoutY(bounds.getMaxY() - 20);
                numNoEntry.setMouseTransparent(true);
                wallpaperPane.getChildren().add(numNoEntry);
                totalCharactersLabelsView.add(numNoEntry);
            }

            var = var + distanceCharactersCards;
            i++;
        }

        if (!wallpaperPane.getChildren().contains(coin)) {
            drawWithDimension(coinDimension, coin);
            placeObjectNoAnimation(coinCoordinate, coin);
            wallpaperPane.getChildren().add(coin);
        }

    }

    /**
     * Utils method that get some infomration about a circle and images (all of the same size) that should be placed on
     * that circuit and return the list of coordinates where to place that image to
     *
     * @param center           the coordinates of the center of the circle
     * @param dimension        the dimenstion of the images to display
     * @param numberOfElements the number of elements to display
     * @param radius           the radius of the circle
     * @return the list of coordinates where to place the images
     */
    private List<Coordinate> drawCircle(Coordinate center, Dimension dimension, int numberOfElements, int radius) {
        List<Coordinate> result = new ArrayList<>(numberOfElements);
        double angle = 2 * PI / numberOfElements;
        double currentAngle;
        Coordinate currentCenter;
        for (int i = 0; i < numberOfElements; i++) {
            currentAngle = i * angle + PI / 2;
            currentCenter = new Coordinate(
                    center.x + (radius * cos(currentAngle)), center.y + (radius * sin(currentAngle)));
            result.add(new Coordinate(
                    currentCenter.x - dimension.getCenter().x, currentCenter.y + dimension.getCenter().y));
        }
        return result;
    }

    /**
     * Draw the clouds provided as a list
     *
     * @param clouds the clouds to draw
     */
    public void drawClouds(List<Cloud> clouds) {


        cancelVisibleViewWallpaperPane(totalStudentsOnCloudsView);
        totalStudentsOnCloudsView = new ArrayList<>();

        cancelLabelsFromPane(totalLabelsOnCloudsView);
        totalLabelsOnCloudsView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(cloudsView);
        cloudsView = new ArrayList<>();

        for (int i = 0; i < clouds.size(); i++) {

            ImageView temp = new ImageView();
            temp.setImage(cloudImage);
            temp.setId(CLOUD_PREFIX + clouds.get(i).getCloudId());
            temp.setOnMouseClicked(this::cloudClicked);
            temp.setCursor(Cursor.HAND);
            drawWithDimension(cloudDimension, temp);
            placeObjectNoAnimation(cloudsCoordinate.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            cloudsView.add(temp);

            for (FactionColor color : FactionColor.values()) {

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = cloudsCoordinate.get(i)
                        .addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
                placeObjectNoAnimation(tempCoordinate, tempColor);
                drawWithDimension(studentAndProfDimension, temp);
                wallpaperPane.getChildren().add(tempColor);
                totalStudentsOnCloudsView.add(tempColor);
                tempColor.setMouseTransparent(true);

                Label label = new Label(String.valueOf(clouds.get(i).getByColor(color)));
                label.setFont(labelFont);
                label.setTextFill(Paint.valueOf("#ffffff"));
                wallpaperPane.getChildren().add(label);
                label.setLayoutX(tempCoordinate.x);
                label.setLayoutY(tempCoordinate.y);
                label.setMouseTransparent(true);
                totalLabelsOnCloudsView.add(label);

            }
        }

        for (ImageView imageView : cloudsView)
            drawWithDimension(cloudDimension, imageView);
    }

    /**
     * Draw the deck of the Client user as a grid
     *
     * @param assistants a list of assistant that represents the deck of each player
     * @see it.polimi.ingsw.am37.model.Player
     */
    public void drawDeck(List<Assistant> assistants) {

        deckAssistantsView = new ArrayList<>();
        assistantsGrid.getChildren().clear();


        for (int currentAssistant = 0; currentAssistant < assistants.size(); currentAssistant++) {

            ImageView imageView = new ImageView();
            imageView.setId(DECK_PREFIX + assistants.get(currentAssistant).getCardValue());
            imageView.setOnMouseClicked(this::assistantDeckClicked);
            imageView.setCursor(Cursor.HAND);
            imageView.setImage(assistantImageFromValue.get(assistants.get(currentAssistant).getCardValue()));
            drawWithDimension(assistantAndCharacterDimension, imageView);
            assistantsGrid.add(imageView, currentAssistant % 4, currentAssistant / 4);
            deckAssistantsView.add(imageView);
        }
    }


    //-------------------------------------------------------------------------------
    //Methods for drawing coordinates and dimensions

    /**
     * Draw the dining of a board provided like a hashmap of color and their quantity for each color
     *
     * @param dining the hashamp representing the dining room of a board
     */
    private void drawDining(HashMap<FactionColor, Integer> dining) {

        cancelVisibleViewWallpaperPane(studentsDiningView);

        studentsDiningView = new ArrayList<>();

        ImageView temp = null;

        for (FactionColor color : FactionColor.values()) {

            double additionalY = 0;

            for (int i = 0; i < dining.get(color); i++) {

                temp = new ImageView();
                temp.setMouseTransparent(true);
                temp.setImage(studentImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                drawWithCoordinateEnablingAnimation(new Coordinate(studentDiningFirstCoordinatesFromColor.get(color).x,
                        studentDiningFirstCoordinatesFromColor.get(color).y + additionalY), temp);
                studentsDiningView.add(temp);
                additionalY = additionalY + ySpaceStudentsDining;
            }
        }
    }

    /**
     * Draw the entrance room of a board, provided as an hasmap of color and their quantity
     *
     * @param entrance the hashmap that represent the entrance
     */
    private void drawEntrance(HashMap<FactionColor, Integer> entrance) {

        cancelVisibleViewWallpaperPane(studentsEntranceView);

        studentsEntranceView = new ArrayList<>();

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;
        boolean firstLine = true;

        for (FactionColor color : FactionColor.values()) {

            for (int i = 0; i < entrance.get(color); i++) {

                temp = new ImageView();
                temp.setMouseTransparent(true);
                temp.setImage(studentImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                drawWithCoordinateEnablingAnimation(new Coordinate(
                        firstEntranceCoordinate.x + additionalX, firstEntranceCoordinate.y + additionalY), temp);
                studentsEntranceView.add(temp);
                posInLine = posInLine + 1;
                additionalX = additionalX + xSpaceStudents;
                if (posInLine == firstLineEntranceAndTowersSize && firstLine) {
                    additionalX = 0;
                    posInLine = 0;
                    additionalY = ySpaceStudentsEntrance;
                    firstLine = false;
                }
            }
        }
    }

    /**
     * Thi method draw the islands like a circle around a spcific center.
     *
     * @param islands the islands to draw
     */
    public void drawIslands(List<Island> islands) {

        int numIslandImage = 0;


        cancelVisibleViewWallpaperPane(towerIslandView);
        towerIslandView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(noEntryView);
        noEntryView = new ArrayList<>();

        cancelLabelsFromPane(totalLabelsOnIslandsView);
        totalLabelsOnIslandsView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(islandsView);
        islandsView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(totalStudentsOnIslandsView);
        totalStudentsOnIslandsView = new ArrayList<>();

        int numIslands = islands.size();

        List<Coordinate> islandsCoordinates = drawCircle(islandCircleCenter, islandDimension, numIslands,
                maxRadiusIslands);

        for (int i = 0; i < islands.size(); i++) {

            ImageView temp = new ImageView();
            temp.setId(ISLAND_PREFIX + islands.get(i).getIslandId());
            temp.setOnMouseClicked(this::islandClicked);
            temp.setCursor(Cursor.HAND);
            temp.setImage(islandImage.get(numIslandImage));
            numIslandImage++;
            if (numIslandImage == islandImage.size()) numIslandImage = 0;
            drawWithDimension(islandDimension, temp);
            placeObjectNoAnimation(islandsCoordinates.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            islandsView.add(temp);


            for (FactionColor color : FactionColor.values()) {

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = islandsCoordinates.get(i)
                        .addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
                placeObjectNoAnimation(tempCoordinate, tempColor);
                drawWithDimension(studentAndProfDimension, temp);
                wallpaperPane.getChildren().add(tempColor);
                totalStudentsOnIslandsView.add(tempColor);
                tempColor.setMouseTransparent(true);

                Label label = new Label(String.valueOf(islands.get(i).getByColor(color)));
                label.setFont(labelFont);
                label.setTextFill(Paint.valueOf("#ffffff"));
                wallpaperPane.getChildren().add(label);
                label.setLayoutX(tempCoordinate.x);
                label.setLayoutY(tempCoordinate.y);
                label.setMouseTransparent(true);
                totalLabelsOnIslandsView.add(label);
            }


            if (islands.get(i).getMotherNatureHere()) {
                if (!wallpaperPane.getChildren().contains(motherNature)) wallpaperPane.getChildren().add(motherNature);
                drawWithDimension(motherNatureDimension, motherNature);
                Coordinate centreIsland = getCentreImageNoAnimation(islandsView.get(i));
                centreIsland = centreIsland.addCoordinate(shiftMotherNature);
                placeObjectNoAnimation(centreIsland, motherNature);
                motherNature.toFront();
                motherNature.setMouseTransparent(false);
            }

            if (islands.get(i).getCurrentTower() != TowerColor.NONE) {

                ImageView towerTemp = new ImageView();
                towerTemp.setMouseTransparent(true);
                towerTemp.setImage(towerImageFromColor.get(islands.get(i).getCurrentTower()));
                drawWithDimension(towerDimension, towerTemp);
                Coordinate centreIsland = getCentreImageNoAnimation(islandsView.get(i));
                centreIsland = centreIsland.addCoordinate(shiftTowerIsland);
                placeObjectNoAnimation(centreIsland, towerTemp);
                wallpaperPane.getChildren().add(towerTemp);
                towerIslandView.add(towerTemp);

                Label numTowers = new Label(Integer.toString(islands.get(i).getNumIslands()));
                numTowers.setMouseTransparent(true);
                numTowers.setFont(labelFont);
                numTowers.setTextFill(Paint.valueOf("#ffffff"));
                wallpaperPane.getChildren().add(numTowers);
                totalLabelsOnIslandsView.add(numTowers);
                numTowers.setLayoutX(towerTemp.getLayoutX() + 9);
                numTowers.setLayoutY(towerTemp.getLayoutY() + 48);
            }


            if (islands.get(i).getNoEntryTile() != 0) {
                temp = new ImageView();
                temp.setImage(noEntryTileImage);
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(noEntryDimension, temp);
                noEntryView.add(temp);
                temp.setMouseTransparent(true);
                Coordinate centreIsland = getCentreImageNoAnimation(islandsView.get(i));
                centreIsland = centreIsland.addCoordinate(shiftNoEntryTile);
                placeObjectNoAnimation(centreIsland, temp);

                Label label = new Label(String.valueOf(islands.get(i).getNoEntryTile()));
                label.setFont(labelFont);
                label.setTextFill(Paint.valueOf("#ffffff"));
                wallpaperPane.getChildren().add(label);
                label.setLayoutX(centreIsland.x);
                label.setLayoutY(centreIsland.y);
                label.setMouseTransparent(true);
                totalLabelsOnIslandsView.add(label);

            }
        }

        for (ImageView imageView : islandsView)
            drawWithDimension(islandDimension, imageView);
    }

    /**
     * Draw the attional board of others players
     *
     * @param updatedPlayer the list of the players updated to sync the board to
     */
    public void drawOthersBoard(List<Player> updatedPlayer) {
        if (boardsController != null) {
            boardsController.draw(updatedPlayer);
        }
    }

    /**
     * Draw the last assistants played by the players provided
     *
     * @param players the players to draw the last assistant played of
     */
    public void drawPlayedAssistants(List<Player> players) {

        totalLabelsAssistantsView = new ArrayList<>();
        assistantPlayedView = new ArrayList<>();
        assistantsHBox.getChildren().clear();

        for (Player p : players) {
            if (p.getLastAssistantPlayed() != null) {
                ImageView imageView = new ImageView();
                imageView.setImage(assistantImageFromValue.get(p.getLastAssistantPlayed().getCardValue()));
                drawWithDimension(assistantAndCharacterDimension, imageView);
                assistantsHBox.getChildren().add(imageView);
                assistantPlayedView.add(imageView);
                Label label = new Label(p.getPlayerId());
                label.setFont(labelFont);
                label.setRotate(270);
                label.setTextFill(Paint.valueOf("#ffffff"));
                assistantsHBox.getChildren().add(new Group(label));
                totalLabelsAssistantsView.add(label);
            }
        }
    }

    /**
     * Draw the professors table in the current dining
     *
     * @param professors the professors table to draw
     */
    private void drawProfessors(boolean[] professors) {

        cancelVisibleViewWallpaperPane(professorsView);

        professorsView = new ArrayList<>();

        for (FactionColor color : FactionColor.values())
            if (professors[color.getIndex()]) {
                ImageView temp = new ImageView();
                temp.setImage(profImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                placeObjectNoAnimation(new Coordinate(profCoordinateFromColor.get(color).x,
                        profCoordinateFromColor.get(color).y), temp);
                professorsView.add(temp);
            }
    }

    /**
     * Draw the towers of a board
     *
     * @param towers the tower cotainer to draw
     */
    private void drawTowers(LimitedTowerContainer towers) {

        cancelVisibleViewWallpaperPane(towersBoardView);

        towersBoardView = new ArrayList<>();

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;


        for (int i = 0; i < towers.getCurrentSize(); i++) {
            temp = new ImageView();
            temp.setImage(towerImageFromColor.get(towers.getCurrentTower()));
            wallpaperPane.getChildren().add(temp);
            drawWithDimension(towerDimension, temp);
            placeObjectNoAnimation(new Coordinate(
                    firstTowerCoordinate.x + additionalX, firstTowerCoordinate.y + additionalY), temp);
            towersBoardView.add(temp);
            posInLine = posInLine + 1;
            additionalX = additionalX + xSpaceTowers;
            if (posInLine == firstLineEntranceAndTowersSize) {
                additionalX = 0;
                posInLine = 0;
                additionalY = ySpaceTowers;
            }
        }
    }

    /**
     * Draw an image view and enable the animation of the movement to the next posizion
     *
     * @param coordinate where to place the new image
     * @param temp       the image to draw
     */
    private void drawWithCoordinateEnablingAnimation(Coordinate coordinate, ImageView temp) {

        placeObjectNoAnimation(new Coordinate(0, 0), temp);

        temp.setTranslateX(coordinate.x);
        temp.setTranslateY(coordinate.y);
    }


    //-------------------------------------------------------------------------------
    //Methods for external input

    /**
     * Callback when entrance is clicked
     *
     * @param mouseEvent the mouse etrance click event
     */
    public void entranceClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_ENTRANCE.name(), null, null);
    }

    /**
     * REturn the coordintes of center of an image view
     *
     * @param imageView
     * @return the coordinates of the center
     */
    private Coordinate getCentreImageNoAnimation(ImageView imageView) {

        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();

        return new Coordinate(x + imageView.getFitHeight(), y + imageView.getFitWidth());
    }

    /**
     * CAllback when island is clicked
     *
     * @param mouseEvent the action event of clicking to an island
     */
    public void islandClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(ISLAND_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_ISLAND.name(), null, id);
        }
    }

    /**
     * Callback when mother nature is clicked
     *
     * @param mouseEvent the event of cliking on mother nature
     */
    public void motherNatureClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_MOTHER_NATURE.name(), null, null);
    }


    //-------------------------------------------------------------------------------
    //Not-graphics related functions

    /**
     * Register a listener to the support of this
     *
     * @param listener the listner to register
     */
    public void registerListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Show the dialog with other boards, fired when the others board function is pressed
     *
     * @param actionEvent
     */
    public void showBoards(ActionEvent actionEvent) {
        if (!isOtherBoardVisible) {
            isOtherBoardVisible = true;
            ((Stage) boardsController.boardsPane.getScene().getWindow()).show();
        }
    }

    /**
     * A class representing the 2 dimension of an image
     *
     * @param height the height of the image
     * @param width  the width of the image
     */
    protected record Dimension(double height, double width) {

        private Coordinate getCenter() {
            return new Coordinate(width / 2, height / 2);
        }
    }

    /**
     * The coordinate of an objects from the top left pane of the parent
     *
     * @param x the horizontal coordinates
     * @param y the vertical coordiantes
     */
    protected record Coordinate(double x, double y) {

        /**
         * Utils function that add coordintes
         *
         * @param temp the cooridante to add to this
         * @return a new coordinates that is the sum of this and the provided one
         */
        private Coordinate addCoordinate(Coordinate temp) {
            return new Coordinate(x + temp.x, y + temp.y);
        }
    }

}
