package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import javafx.event.ActionEvent;
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

import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static java.util.Map.entry;

public class GameSceneController extends GenericController {

    public Pane wallpaperPane;
    public GridPane assistantsGrid;
    public HBox assistantsHBox;

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
    private final List<Coordinate> cloudsCoordinate = Arrays.asList(new Coordinate(680, 340), new Coordinate(880, 340), new Coordinate(780, 540));
    private final Coordinate shiftNoEntryTile = new Coordinate(29, 90);
    private final Coordinate shiftMotherNature = new Coordinate(18, 0);

    private final Dimension towerDimension = new Dimension(50, 28);
    private final Dimension studentAndProfDimension = new Dimension(40, 40);
    private final Dimension cloudDimension = new Dimension(150, 150);
    private final Dimension islandDimension = new Dimension(166.6, 166.6);
    private final Dimension motherNatureDimension = new Dimension(60, 50);
    private final Dimension noEntryDimension = new Dimension(30, 30);
    private final Dimension assistantDimension = new Dimension(166.66, 113.33);
    private final int maxRadiusIslands = 350;


    //-------------------------------------------------------------------------------
    //Stand alone images
    private final Image cloudImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/Cloud.png")));
    private final List<Image> islandImage = Arrays.asList(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/Island1.png"))), new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/Island2.png"))), new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/Island3.png"))));
    private final Image motherNatureImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/MotherNature.png")));
    private final Image noEntryTileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/NoEntryTile.png")));

    //-------------------------------------------------------------------------------
    //Link between factions and other info

    private final Map<FactionColor, Image> profImageFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/BlueTeacher.png")))),
            entry(FactionColor.PINK, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/PinkTeacher.png")))),
            entry(FactionColor.GREEN, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/GreenTeacher.png")))),
            entry(FactionColor.YELLOW, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/YellowTeacher.png")))),
            entry(FactionColor.RED, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/RedTeacher.png"))))
    );
    private final Map<FactionColor, Coordinate> profCoordinateFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Coordinate(55, 636)),
            entry(FactionColor.PINK, new Coordinate(115, 117)),
            entry(FactionColor.GREEN, new Coordinate(295, 117)),
            entry(FactionColor.YELLOW, new Coordinate(175, 117)),
            entry(FactionColor.RED, new Coordinate(235, 117))
    );
    private final Map<FactionColor, Image> studentImageFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/BlueStudent.png")))),
            entry(FactionColor.PINK, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/PinkStudent.png")))),
            entry(FactionColor.GREEN, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/GreenStudent.png")))),
            entry(FactionColor.YELLOW, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/YellowStudent.png")))),
            entry(FactionColor.RED, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/RedStudent.png"))))
    );

    private final Map<FactionColor, Coordinate> studentDiningFirstCoordinatesFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Coordinate(57, 117)),
            entry(FactionColor.PINK, new Coordinate(116, 117)),
            entry(FactionColor.GREEN, new Coordinate(293, 117)),
            entry(FactionColor.YELLOW, new Coordinate(175, 117)),
            entry(FactionColor.RED, new Coordinate(234, 117))
    );

    private final Map<TowerColor, Image> towerImageFromColor = Map.ofEntries(
            entry(TowerColor.BLACK, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/BlackTower.png")))),
            entry(TowerColor.GRAY, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/GrayTower.png")))),
            entry(TowerColor.WHITE, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/WhiteTower.png"))))

    );

    private final Map<FactionColor, Coordinate> studentsShiftPositionsOnIslandsAndClouds = Map.ofEntries(
            entry(FactionColor.BLUE, new Coordinate(140, 41)),
            entry(FactionColor.PINK, new Coordinate(16, 116)),
            entry(FactionColor.GREEN, new Coordinate(114, 116) ),
            entry(FactionColor.YELLOW, new Coordinate(-10, 41)),
            entry(FactionColor.RED, new Coordinate(63, -9) )
    );



    //-------------------------------------------------------------------------------
    //ImageView of everything
    private ArrayList<ImageView> studentsEntranceView = new ArrayList<>();
    private ArrayList<ImageView> studentsDiningView = new ArrayList<>();
    private ArrayList<ImageView> towersView = new ArrayList<>();
    private ArrayList<ImageView> professorsView = new ArrayList<>();
    private ArrayList<ImageView> cloudsView = new ArrayList<>();
    private ArrayList<ImageView> islandsView = new ArrayList<>();
    private ArrayList<ImageView> totalStudentsOnIslandsView = new ArrayList<>();
    private ArrayList<ImageView> totalStudentsOnCloudsView = new ArrayList<>();
    private ArrayList<Label> totalLabelsOnIslandsView = new ArrayList<>();
    private ArrayList<Label> totalLabelsOnCloudsView = new ArrayList<>();
    private ArrayList<ImageView> noEntryView = new ArrayList<>();

    private final ImageView motherNature = new ImageView(motherNatureImage);

    private void cancelVisibleViewWallpaperPane(ArrayList<ImageView> view){
        for(int i=0; i<view.size();i++){
            wallpaperPane.getChildren().remove(view.get(i));
        }
    }

    private void cancelLabelsFromPane(ArrayList<Label> label){
        for(int i=0; i<label.size();i++){
            wallpaperPane.getChildren().remove(label.get(i));
        }
    }

    //-------------------------------------------------------------------------------

    public void drawCharacters(List<Character> characters) {
        /*for (Character c :
                characters) {
            ImageView imageView = new ImageView(getClass().getResource(
                            "/assets/images/" + c.getEffectType().name() + ".jpg")
                    .toString());
            imageView.setFitWidth(assistantDimension.width);
            imageView.setFitHeight(assistantDimension.height);
            imageView.setId("character_" + c.getEffectType().name());
            assistantsHBox.getChildren().add(imageView);
            Label label = new Label(c.getEffectType().name());
            label.setFont(labelFont);
            label.setRotate(270);
            assistantsHBox.getChildren().add(new Group(label));
        }*/
    }

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

        for(int i=0; i<clouds.size(); i++){

            ImageView temp = new ImageView();
            temp.setImage(cloudImage);
            drawWithDimension(cloudDimension, temp);
            drawWithCoordinateDisablingAnimation(cloudsCoordinate.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            cloudsView.add(temp);

            for(FactionColor color:FactionColor.values()){

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = cloudsCoordinate.get(i).addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
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

        for(ImageView imageView: cloudsView)
            drawWithDimension(cloudDimension, imageView);
    }

    public void drawDeck(List<Assistant> assistants) {
        /*final Pattern deckAssistantPattern = Pattern.compile("deck_assistant_.*");
        if (wallpaperPane.getChildren().stream().anyMatch(n -> deckAssistantPattern.matcher(n.getId()).matches())) {
            wallpaperPane.getChildren()
                    .stream()
                    .filter(n -> deckAssistantPattern.matcher(n.getId()).matches())
                    .forEach(n -> wallpaperPane.getChildren().remove(n));
        }
        for (int currentAssistant = 0; currentAssistant < assistants.size(); currentAssistant++) {
            ImageView imageView = new ImageView(getClass().getResource(
                            "/assets/images/assistants/Assistant" + assistants.get(currentAssistant).getCardValue() + ".png")
                    .toString());
            imageView.setFitHeight(assistantDimension.height);
            imageView.setFitWidth(assistantDimension.width);
            imageView.setId("deck_assistant_" + assistants.get(currentAssistant).getCardValue());
            assistantsGrid.add(imageView, currentAssistant % 4, currentAssistant / 4);
        }*/
    }

    public void drawIslands(List<Island> islands) {

        int numIslandImage = 0;

        cancelVisibleViewWallpaperPane(noEntryView);
        noEntryView = new ArrayList<>();

        cancelLabelsFromPane(totalLabelsOnIslandsView);
        totalLabelsOnIslandsView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(islandsView);
        islandsView = new ArrayList<>();

        cancelVisibleViewWallpaperPane(totalStudentsOnIslandsView);
        totalStudentsOnIslandsView = new ArrayList<>();

        int numIslands = islands.size();

        List<Coordinate> islandsCoordinates = drawCircle(islandCircleCenter, islandDimension, numIslands, maxRadiusIslands);

        for(int i=0; i<islands.size(); i++){

            ImageView temp = new ImageView();
            temp.setImage(islandImage.get(numIslandImage));
            numIslandImage++;
            if(numIslandImage == islandImage.size())
                numIslandImage = 0;
            drawWithDimension(islandDimension, temp);
            drawWithCoordinateDisablingAnimation(islandsCoordinates.get(i), temp);
            wallpaperPane.getChildren().add(temp);
            islandsView.add(temp);


            for(FactionColor color : FactionColor.values()){

                ImageView tempColor = new ImageView();
                tempColor.setImage(studentImageFromColor.get(color));
                Coordinate tempCoordinate = islandsCoordinates.get(i).addCoordinate(studentsShiftPositionsOnIslandsAndClouds.get(color));
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
                if (!wallpaperPane.getChildren().contains(motherNature))
                    wallpaperPane.getChildren().add(motherNature);
                drawWithDimension(motherNatureDimension, motherNature);
                Coordinate centreIsland = getCentreImageNoAnimation(islandsView.get(i));
                centreIsland = centreIsland.addCoordinate(shiftMotherNature);
                drawWithCoordinateDisablingAnimation(centreIsland, motherNature);
                motherNature.setMouseTransparent(true);
            }

            if(islands.get(i).getNoEntryTile()!=0) {
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

        for(ImageView imageView: islandsView)
            drawWithDimension(islandDimension, imageView);
    }


    public void drawPlayedAssistants(List<Player> players) {
        /*assistantsHBox.getChildren().clear();
        for (Player p : players) {
            if (p.getLastAssistantPlayed() != null) {
                ImageView imageView = new ImageView(getClass().getResource(
                                "/assets/images/assistants/Assistant" + p.getLastAssistantPlayed().getCardValue() +
                                        ".png")
                        .toString());
                imageView.setFitWidth(assistantDimension.width);
                imageView.setFitHeight(assistantDimension.height);
                imageView.setId("last_assistant_by_" + p.getPlayerId());
                assistantsHBox.getChildren().add(imageView);
                Label label = new Label(p.getPlayerId());
                label.setFont(labelFont);
                label.setRotate(270);
                assistantsHBox.getChildren().add(new Group(label));
            }
        }*/
    }



    //-------------------------------------------------------------------------------
    //Methods for drawing my board

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
                temp.setImage(studentImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                drawWithCoordinateEnablingAnimation(new Coordinate(firstEntranceCoordinate.x + additionalX,firstEntranceCoordinate.y + additionalY ), temp);
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

    private void drawDining(HashMap<FactionColor, Integer> dining) {

        cancelVisibleViewWallpaperPane(studentsDiningView);

        studentsDiningView = new ArrayList<>();

        ImageView temp = null;

        for (FactionColor color : FactionColor.values()) {

            double additionalY = 0;

            for (int i = 0; i < dining.get(color); i++) {

                temp = new ImageView();
                temp.setImage(studentImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                drawWithCoordinateEnablingAnimation(new Coordinate(studentDiningFirstCoordinatesFromColor.get(color).x,studentDiningFirstCoordinatesFromColor.get(color).y + additionalY ), temp);
                studentsDiningView.add(temp);
                additionalY = additionalY + ySpaceStudentsDining;
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
                drawWithCoordinateDisablingAnimation(new Coordinate(profCoordinateFromColor.get(color).x, profCoordinateFromColor.get(color).y), temp);
                professorsView.add(temp);
            }
    }

    private void drawTowers(LimitedTowerContainer towers) {

        cancelVisibleViewWallpaperPane(towersView);

        towersView = new ArrayList<>();

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;


        for (int i = 0; i < towers.getCurrentSize(); i++) {
            temp = new ImageView();
            temp.setImage(towerImageFromColor.get(towers.getCurrentTower()));
            wallpaperPane.getChildren().add(temp);
            drawWithDimension(towerDimension, temp);
            drawWithCoordinateDisablingAnimation(new Coordinate(firstTowerCoordinate.x + additionalX, firstTowerCoordinate.y + additionalY), temp);
            towersView.add(temp);
            posInLine = posInLine + 1;
            additionalX = additionalX + xSpaceTowers;
            if (posInLine == firstLineEntranceAndTowersSize) {
                additionalX = 0;
                posInLine = 0;
                additionalY = ySpaceTowers;
            }
        }
    }

    public void drawBoard(HashMap<FactionColor, Integer> entrance, HashMap<FactionColor, Integer> dining, boolean[] professors, LimitedTowerContainer towers) {

        drawEntrance(entrance);
        drawDining(dining);
        drawProfessors(professors);
        drawTowers(towers);
    }


    //-------------------------------------------------------------------------------
    //Methods for drawing coordinates and dimensions

    private void drawWithDimension(Dimension dimension, ImageView temp){
        temp.setFitWidth(dimension.width);
        temp.setFitHeight(dimension.height);
    }

    private void drawWithCoordinateEnablingAnimation(Coordinate coordinate, ImageView temp){

        drawWithCoordinateDisablingAnimation(new Coordinate(0,0), temp);

        temp.setTranslateX(coordinate.x);
        temp.setTranslateY(coordinate.y);
    }

    private void drawWithCoordinateDisablingAnimation(Coordinate coordinate, ImageView temp){

        temp.setLayoutX(coordinate.x);
        temp.setLayoutY(coordinate.y);
    }

    private record Coordinate(double x, double y) {

        private Coordinate addCoordinate(Coordinate temp){
            return new Coordinate(x + temp.x, y + temp.y);
        }
    }

    private record Dimension(double height, double width) {

        private Coordinate getCenter() {
            return new Coordinate(width / 2, height / 2);
        }
    }

    private Coordinate getCentreImageNoAnimation(ImageView imageView){

        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();

        return new Coordinate(x + imageView.getFitHeight(), y + imageView.getFitWidth());
    }


    //-------------------------------------------------------------------------------
    //Methods for external input

    public void entranceClicked(MouseEvent mouseEvent) {
    }

    public void profPlaceClicked(MouseEvent mouseEvent) {
    }

    public void showBoards(ActionEvent actionEvent) {
    }

    public void towerPlaceClicked(MouseEvent mouseEvent) {
    }

    public void diningClicked(MouseEvent mouseEvent) {
    }

}
