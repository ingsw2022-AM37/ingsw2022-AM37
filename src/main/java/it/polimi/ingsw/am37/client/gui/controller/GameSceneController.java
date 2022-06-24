package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.am37.client.gui.observer.GuiObserver.ClickableObjectType.*;
import static java.lang.Math.*;
import static java.util.Map.entry;

public class GameSceneController extends GenericController {

    private final static String ISLAND_PREFIX = "island_";
    private final static String CHARACTER_PREFIX = "character_";

    public Pane wallpaperPane;
    public GridPane assistantsGrid;
    public Label charactersLabel;
    public HBox charactersHBox;
    public HBox assistantsHBox;
    public Label numOfCoins;
    private final static String CLOUD_PREFIX = "cloud_";
    private final static String DECK_PREFIX = "deck_";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private final boolean isOtherBoardVisible = false;


    //-------------------------------------------------------------------------------
    //Position and dimensions information

    private final static Font labelFont = new Font("System Bold", 20);

    private final static double xSpaceStudents = 59;
    private final static double ySpaceStudentsEntrance = 50;
    private final static double ySpaceStudentsDining = 42;

    private final static double xSpaceTowers = 50;
    private final static double ySpaceTowers = 65;

    private final static Coordinate firstEntranceCoordinate = new Coordinate(57, 42);
    private final static Coordinate firstTowerCoordinate = new Coordinate(107, 740);
    private final int firstLineEntranceAndTowersSize = 4;
    private final Coordinate islandCircleCenter = new Coordinate(850, 340);
    private final List<Coordinate> cloudsCoordinate = Arrays.asList(new Coordinate(680, 340), new Coordinate(880,
            340), new Coordinate(780, 540));
    private final Coordinate shiftNoEntryTile = new Coordinate(29, 90);
    private final Coordinate shiftMotherNature = new Coordinate(50, 0);
    private final Coordinate shiftTowerIsland = new Coordinate(2, 0);
    private final List<Coordinate> additionalPriceCharacters = Arrays.asList(new Coordinate(1460, 800),
            new Coordinate(1604, 800), new Coordinate(1748, 800));
    private final Coordinate coinCoordinate = new Coordinate(1172, 869);
    private final int distanceCharactersCards = 144;

    private final Dimension towerDimension = new Dimension(50, 28);
    private final Dimension studentAndProfDimension = new Dimension(40, 40);
    private final Dimension cloudDimension = new Dimension(150, 150);
    private final Dimension islandDimension = new Dimension(166.6, 166.6);
    private final Dimension motherNatureDimension = new Dimension(60, 50);
    private final Dimension noEntryDimension = new Dimension(30, 30);
    private final Dimension assistantAndCharacterDimension = new Dimension(166.66, 113.33);
    private final Dimension coinDimension = new Dimension(105, 95);
    private final int maxRadiusIslands = 350;


    //-------------------------------------------------------------------------------
    //Stand alone images
    private final Image cloudImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images" + "/Cloud.png")));
    private final List<Image> islandImage =
            Arrays.asList(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/Island1" +
                            ".png"))), new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/Island2.png"))), new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets" + "/images/Island3.png"))));
    private final Image motherNatureImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets" + "/images/MotherNature.png")));
    private final Image noEntryTileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets" + "/images/NoEntryTile.png")));
    private final Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images" + "/Coin.png")));

    //-------------------------------------------------------------------------------
    //Link between info and images or coordinates


    private final Map<String, Image> characterImageFromName = Map.ofEntries(entry(Effect.MONK.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters/MONK" +
                            ".jpg")))), entry(Effect.GRANDMA.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/characters/GRANDMA.jpg")))), entry(Effect.JESTER.getCharacterName(), new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/characters/JESTER" +
                    ".jpg")))), entry(Effect.PRINCESS.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters" +
                            "/PRINCESS.jpg")))), entry(Effect.FARMER.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters/FARMER" +
                            ".jpg")))), entry(Effect.HERALD.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters/HERALD" +
                            ".jpg")))), entry(Effect.MAGIC_POSTMAN.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters" +
                            "/MAGIC_POSTMAN.jpg")))), entry(Effect.CENTAUR.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters" +
                            "/CENTAUR.jpg")))), entry(Effect.KNIGHT.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters/KNIGHT" +
                            ".jpg")))), entry(Effect.MUSHROOM_MAN.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters" +
                            "/MUSHROOM_MAN.jpg")))), entry(Effect.MINSTREL.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters" +
                            "/MINSTREL.jpg")))), entry(Effect.THIEF.getCharacterName(),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/characters/THIEF" + ".jpg")))));

    private final Map<Integer, Image> assistantImageFromValue = Map.ofEntries(entry(1,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/assistants" +
                            "/Assistant1.png")))), entry(2,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant2.png")))), entry(3, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant3.png")))), entry(4, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant4.png")))), entry(5, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant5.png")))), entry(6, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant6.png")))), entry(7, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant7.png")))), entry(8, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant8.png")))), entry(9, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" +
                    "/Assistant9.png")))), entry(10, new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/assets/images/assistants" + "/Assistant10.png")))));


    private final Map<FactionColor, Image> profImageFromColor = Map.ofEntries(entry(FactionColor.BLUE,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/BlueTeacher.png")))), entry(FactionColor.PINK,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/PinkTeacher.png")))), entry(FactionColor.GREEN,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/GreenTeacher.png")))), entry(FactionColor.YELLOW,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets" +
                            "/images/YellowTeacher.png")))), entry(FactionColor.RED,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" + "/RedTeacher.png")))));
    private final Map<FactionColor, Coordinate> profCoordinateFromColor = Map.ofEntries(entry(FactionColor.BLUE,
            new Coordinate(55, 636)), entry(FactionColor.PINK, new Coordinate(115, 636)), entry(FactionColor.GREEN,
            new Coordinate(295, 636)), entry(FactionColor.YELLOW, new Coordinate(175, 636)), entry(FactionColor.RED,
            new Coordinate(235, 636)));

    private final Map<FactionColor, Coordinate> studentShiftsForCharacter = Map.ofEntries(entry(FactionColor.BLUE,
            new Coordinate(98, 31)), entry(FactionColor.PINK, new Coordinate(15, 101)), entry(FactionColor.GREEN,
            new Coordinate(98, 101)), entry(FactionColor.YELLOW, new Coordinate(15, 31)), entry(FactionColor.RED,
            new Coordinate(57, 136)));

    private final Map<FactionColor, Image> studentImageFromColor = Map.ofEntries(entry(FactionColor.BLUE,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/BlueStudent.png")))), entry(FactionColor.PINK,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/PinkStudent.png")))), entry(FactionColor.GREEN,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/GreenStudent.png")))), entry(FactionColor.YELLOW,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets" +
                            "/images/YellowStudent.png")))), entry(FactionColor.RED,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" + "/RedStudent.png")))));

    private final Map<FactionColor, Coordinate> studentDiningFirstCoordinatesFromColor =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(57, 177)), entry(FactionColor.PINK,
                            new Coordinate(116, 177)), entry(FactionColor.GREEN, new Coordinate(293, 177)),
                    entry(FactionColor.YELLOW, new Coordinate(175, 177)), entry(FactionColor.RED, new Coordinate(234,
                            177)));

    private final Map<TowerColor, Image> towerImageFromColor = Map.ofEntries(entry(TowerColor.BLACK,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/BlackTower.png")))), entry(TowerColor.GRAY,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" +
                            "/GrayTower.png")))), entry(TowerColor.WHITE,
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images" + "/WhiteTower.png"))))

    );

    private final Map<FactionColor, Coordinate> studentsShiftPositionsOnIslandsAndClouds =
            Map.ofEntries(entry(FactionColor.BLUE, new Coordinate(140, 41)), entry(FactionColor.PINK,
                            new Coordinate(16, 116)), entry(FactionColor.GREEN, new Coordinate(114, 116)),
                    entry(FactionColor.YELLOW, new Coordinate(-10, 41)), entry(FactionColor.RED, new Coordinate(63,
                            -9)));


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

    private final ImageView motherNature = new ImageView(motherNatureImage);
    private final ImageView coin = new ImageView(coinImage);

    public GameSceneController() {
        motherNature.setOnMouseClicked(this::motherNatureClicked);
    }

    public void assistantDeckClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(DECK_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_ASSISTANT.name(), null, id);
        }
    }

    private void cancelLabelsFromPane(ArrayList<Label> label) {
        for (Label value : label) {
            wallpaperPane.getChildren().remove(value);
        }
    }

    //-------------------------------------------------------------------------------

    private void cancelVisibleViewWallpaperPane(ArrayList<ImageView> view) {
        for (int i = 0; i < view.size(); i++) {
            wallpaperPane.getChildren().remove(view.get(i));
        }
    }

    public void changeCoins(int num) {
        numOfCoins.setText(Integer.toString(num));
        numOfCoins.setVisible(true);
    }

    public void characterClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(CHARACTER_PREFIX + "(.+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_CHARACTER.name(), null, id);
        }
    }

    public void cloudClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(CLOUD_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_CLOUD.name(), null, id);
        }
    }

    public void diningClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_DINING.name(), null, null);
    }

    public void drawBoard(HashMap<FactionColor, Integer> entrance, HashMap<FactionColor, Integer> dining,
                          boolean[] professors, LimitedTowerContainer towers) {

        drawEntrance(entrance);
        drawDining(dining);
        drawProfessors(professors);
        drawTowers(towers);
    }

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
                    drawWithCoordinateDisablingAnimation(new Coordinate(
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
                drawWithCoordinateDisablingAnimation(new Coordinate(
                        bounds.getMinX() + var + 25, bounds.getMaxY() - 20), noEntryTile);
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
            drawWithCoordinateDisablingAnimation(coinCoordinate, coin);
            wallpaperPane.getChildren().add(coin);
        }

    }


    //-------------------------------------------------------------------------------
    //Methods for drawing my board

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
            drawWithDimension(cloudDimension, temp);
            drawWithCoordinateDisablingAnimation(cloudsCoordinate.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            cloudsView.add(temp);

            for (FactionColor color : FactionColor.values()) {

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = cloudsCoordinate.get(i)
                        .addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
                drawWithCoordinateDisablingAnimation(tempCoordinate, tempColor);
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

    public void drawDeck(List<Assistant> assistants) {

        deckAssistantsView = new ArrayList<>();
        assistantsGrid.getChildren().clear();


        for (int currentAssistant = 0; currentAssistant < assistants.size(); currentAssistant++) {

            ImageView imageView = new ImageView();
            imageView.setId(DECK_PREFIX + assistants.get(currentAssistant).getCardValue());
            imageView.setOnMouseClicked(this::assistantDeckClicked);
            imageView.setImage(assistantImageFromValue.get(assistants.get(currentAssistant).getCardValue()));
            drawWithDimension(assistantAndCharacterDimension, imageView);
            assistantsGrid.add(imageView, currentAssistant % 4, currentAssistant / 4);
            deckAssistantsView.add(imageView);
        }
    }

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

    private void drawEntrance(HashMap<FactionColor, Integer> entrance) {

        cancelVisibleViewWallpaperPane(studentsEntranceView);

        studentsEntranceView = new ArrayList<>();

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;

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
                if (posInLine == firstLineEntranceAndTowersSize) {
                    additionalX = 0;
                    posInLine = 0;
                    additionalY = ySpaceStudentsEntrance;
                }
            }
        }
    }


    //-------------------------------------------------------------------------------
    //Methods for drawing coordinates and dimensions

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
            temp.setImage(islandImage.get(numIslandImage));
            numIslandImage++;
            if (numIslandImage == islandImage.size()) numIslandImage = 0;
            drawWithDimension(islandDimension, temp);
            drawWithCoordinateDisablingAnimation(islandsCoordinates.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            islandsView.add(temp);


            for (FactionColor color : FactionColor.values()) {

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = islandsCoordinates.get(i)
                        .addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
                drawWithCoordinateDisablingAnimation(tempCoordinate, tempColor);
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
                drawWithCoordinateDisablingAnimation(centreIsland, motherNature);
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
                drawWithCoordinateDisablingAnimation(centreIsland, towerTemp);
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
                drawWithCoordinateDisablingAnimation(centreIsland, temp);

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
                assistantsHBox.getChildren().add(new Group(label));
                totalLabelsAssistantsView.add(label);
            }
        }
    }

    private void drawProfessors(boolean[] professors) {

        cancelVisibleViewWallpaperPane(professorsView);

        professorsView = new ArrayList<>();

        for (FactionColor color : FactionColor.values())
            if (professors[color.getIndex()]) {
                ImageView temp = new ImageView();
                temp.setImage(profImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                drawWithCoordinateDisablingAnimation(new Coordinate(profCoordinateFromColor.get(color).x,
                        profCoordinateFromColor.get(color).y), temp);
                professorsView.add(temp);
            }
    }

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
            drawWithCoordinateDisablingAnimation(new Coordinate(
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

    private void drawWithCoordinateDisablingAnimation(Coordinate coordinate, ImageView temp) {

        temp.setLayoutX(coordinate.x);
        temp.setLayoutY(coordinate.y);
    }

    private void drawWithCoordinateEnablingAnimation(Coordinate coordinate, ImageView temp) {

        drawWithCoordinateDisablingAnimation(new Coordinate(0, 0), temp);

        temp.setTranslateX(coordinate.x);
        temp.setTranslateY(coordinate.y);
    }

    private void drawWithDimension(Dimension dimension, ImageView temp) {
        temp.setFitWidth(dimension.width);
        temp.setFitHeight(dimension.height);
    }


    //-------------------------------------------------------------------------------
    //Methods for external input

    public void entranceClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_ENTRANCE.name(), null, null);
    }

    private Coordinate getCentreImageNoAnimation(ImageView imageView) {

        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();

        return new Coordinate(x + imageView.getFitHeight(), y + imageView.getFitWidth());
    }

    public void islandClicked(MouseEvent mouseEvent) {
        String jfxId = mouseEvent.getPickResult().getIntersectedNode().getId();
        Matcher m = Pattern.compile(ISLAND_PREFIX + "(\\d+)").matcher(jfxId);
        if (m.find()) {
            String id = m.group(1);
            changeSupport.firePropertyChange(CO_ISLAND.name(), null, id);
        }
    }

    public void motherNatureClicked(MouseEvent mouseEvent) {
        changeSupport.firePropertyChange(CO_MOTHER_NATURE.name(), null, null);
    }


    //-------------------------------------------------------------------------------
    //Not-graphics related functions
    public void registerListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void showBoards(ActionEvent actionEvent) {
        //TODO pop the windows
    }

    private record Dimension(double height, double width) {

        private Coordinate getCenter() {
            return new Coordinate(width / 2, height / 2);
        }
    }

    private record Coordinate(double x, double y) {

        private Coordinate addCoordinate(Coordinate temp) {
            return new Coordinate(x + temp.x, y + temp.y);
        }
    }

}
