package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.util.Map.entry;

public class GameSceneController extends GenericController {


    //--------------------------------------//

    private final static double xSpaceStudents = 59;
    private final static double ySpaceStudentsEntrance = 50;
    private final static double ySpaceStudentsDining = 42;
    private final static double xSpaceTowers = 50;
    private final static double ySpaceTowers = 65;
    private final static Coordinate firstEntrance = new Coordinate(57, 42);
    private final static Coordinate firstTower = new Coordinate(107, 740);
    private final int firstLineEntranceAndTowers = 4;

    Map<FactionColor, Image> profImageFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Image(getClass().getResourceAsStream("/assets/images/BlueTeacher.png"))),
            entry(FactionColor.PINK, new Image(getClass().getResourceAsStream("/assets/images/PinkTeacher.png"))),
            entry(FactionColor.GREEN, new Image(getClass().getResourceAsStream("/assets/images/GreenTeacher.png"))),
            entry(FactionColor.YELLOW, new Image(getClass().getResourceAsStream("/assets/images/YellowTeacher.png"))),
            entry(FactionColor.RED, new Image(getClass().getResourceAsStream("/assets/images/RedTeacher.png")))
    );
    Map<FactionColor, Coordinate> profCoordinateFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Coordinate(55, 636)),
            entry(FactionColor.PINK, new Coordinate(115, 117)),
            entry(FactionColor.GREEN, new Coordinate(295, 117)),
            entry(FactionColor.YELLOW, new Coordinate(175, 117)),
            entry(FactionColor.RED, new Coordinate(235, 117))
    );
    Map<FactionColor, Image> studentImageFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Image(getClass().getResourceAsStream("/assets/images/BlueStudent.png"))),
            entry(FactionColor.PINK, new Image(getClass().getResourceAsStream("/assets/images/PinkStudent.png"))),
            entry(FactionColor.GREEN, new Image(getClass().getResourceAsStream("/assets/images/GreenStudent.png"))),
            entry(FactionColor.YELLOW, new Image(getClass().getResourceAsStream("/assets/images/YellowStudent.png"))),
            entry(FactionColor.RED, new Image(getClass().getResourceAsStream("/assets/images/RedStudent.png")))
    );

    Map<FactionColor, Coordinate> studentDiningFirstCoordinatesFromColor = Map.ofEntries(
            entry(FactionColor.BLUE, new Coordinate(57, 117)),
            entry(FactionColor.PINK, new Coordinate(116, 117)),
            entry(FactionColor.GREEN, new Coordinate(293, 117)),
            entry(FactionColor.YELLOW, new Coordinate(175, 117)),
            entry(FactionColor.RED, new Coordinate(234, 117))
    );

    Map<TowerColor, Image> towerImageFromColor = Map.ofEntries(
            entry(TowerColor.BLACK, new Image(getClass().getResourceAsStream("/assets/images/BlackTower.png"))),
            entry(TowerColor.GRAY, new Image(getClass().getResourceAsStream("/assets/images/GrayTower.png"))),
            entry(TowerColor.WHITE, new Image(getClass().getResourceAsStream("/assets/images/WhiteTower.png")))

    );
    private ArrayList<ImageView> studentsEntranceView = new ArrayList<>();
    private ArrayList<ImageView> studentsDiningView = new ArrayList<>();
    private ArrayList<ImageView> towersView = new ArrayList<>();
    private ArrayList<ImageView> professorsView = new ArrayList<>();

    //--------------------------------------//


    private final static Font labelFont = new Font("System Bold", 20);
    final Dimension islandDimension = new Dimension(166.6, 166.6);
    final Dimension assistantDimension = new Dimension(166.66, 113.33);
    final Dimension cloudsDimension = new Dimension(150, 200);
    final Coordinate islandCenter = new Coordinate(850, 340);
    public Pane wallpaperPane;
    public GridPane assistantsGrid;
    public HBox assistantsHBox;
    public HBox cloudsHBox;
    private List<Island> islands;

    public void diningClicked(MouseEvent mouseEvent) {
    }

    /*public void drawCharacters(List<Character> characters) {
        for (Character c :
                characters) {
            ImageView imageView = new ImageView(getClass().getResource(
                            "/assets/images/Character" + c + ".png")
                    .toString());
            imageView.setFitWidth(assistantDimension.width);
            imageView.setFitHeight(assistantDimension.height);
            imageView.setId("lastAssistantPlayedBy" + c.getEffectType());
            assistantsHBox.getChildren().add(imageView);
            Label label = new Label(c.getEffectType().name());
            label.setFont(labelFont);
            label.setRotate(270);
            assistantsHBox.getChildren().add(new Group(label));
        }
    }*/

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
        for (Cloud cloud :
                clouds) {
            ImageView imageView = new ImageView(getClass().getResource(
                            "/assets/images/Cloud.png")
                    .toString());
            imageView.setFitWidth(cloudsDimension.width);
            imageView.setFitHeight(cloudsDimension.height);
            cloudsHBox.getChildren().add(imageView);
        }
    }

    public void drawDeck(List<Assistant> assistants) {
        for (int currentAssistant = 0; currentAssistant < assistants.size(); currentAssistant++) {
            ImageView imageView = new ImageView(getClass().getResource(
                            "/assets/images/assistants/Assistant" + assistants.get(currentAssistant).getCardValue() + ".png")
                    .toString());
            imageView.setFitHeight(assistantDimension.height);
            imageView.setFitWidth(assistantDimension.width);
            imageView.setId("island" + islands.get(currentAssistant).getIslandId());
            assistantsGrid.add(imageView, currentAssistant % 4, currentAssistant / 4);
        }
    }

    public void drawIslands(List<Island> islands) {
        this.islands = islands;
        List<Coordinate> coordinates = drawCircle(islandCenter, islandDimension, islands.size(), 350);
        for (int i = 0; i < islands.size(); i++) {
            ImageView imageView = new ImageView(getClass().getResource("/assets/images/Island" + ((i % 3) + 1) + ".png")
                    .toString());
            imageView.setFitHeight(islandDimension.height);
            imageView.setFitWidth(islandDimension.width);
            imageView.setId("island" + islands.get(i).getIslandId());
            imageView.relocate(coordinates.get(i).x, coordinates.get(i).y);
            wallpaperPane.getChildren().add(imageView);
        }
    }

    public void drawPlayedAssistants(List<Player> players) {
        for (Player p :
                players) {
            if (p.getLastAssistantPlayed() != null) {
                ImageView imageView = new ImageView(getClass().getResource(
                                "/assets/images/assistants/Assistant" + p.getLastAssistantPlayed().getCardValue() +
                                        ".png")
                        .toString());
                imageView.setFitWidth(assistantDimension.width);
                imageView.setFitHeight(assistantDimension.height);
                imageView.setId("lastAssistantPlayedBy" + p.getPlayerId());
                assistantsHBox.getChildren().add(imageView);
                Label label = new Label(p.getPlayerId());
                label.setFont(labelFont);
                label.setRotate(270);
                assistantsHBox.getChildren().add(new Group(label));
            }
        }
    }

    public void entranceClicked(MouseEvent mouseEvent) {
    }

    public void profPlaceClicked(MouseEvent mouseEvent) {
    }

    public void showBoards(ActionEvent actionEvent) {
        drawIslands(islands);
    }

    public void towerPlaceClicked(MouseEvent mouseEvent) {
    }

    private record Coordinate(double x, double y) {
    }

    private record Dimension(double height, double width) {
        public Coordinate getCenter() {
            return new Coordinate(width / 2, height / 2);
        }
    }

    //--------------------------------------//
    private void drawEntrance(HashMap<FactionColor, Integer> entrance) {

        for (int i = 0; i < studentsEntranceView.size(); i++)
            wallpaperPane.getChildren().remove(studentsEntranceView.get(i));

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
                temp.setX(0);
                temp.setY(0);
                temp.setFitWidth(40);
                temp.setFitHeight(40);
                temp.setTranslateX(firstEntrance.x + additionalX);
                temp.setTranslateY(firstEntrance.y + additionalY);
                studentsEntranceView.add(temp);
                posInLine = posInLine + 1;
                additionalX = additionalX + xSpaceStudents;
                if (posInLine == firstLineEntranceAndTowers) {
                    additionalX = 0;
                    posInLine = 0;
                    additionalY = ySpaceStudentsEntrance;
                }
            }
        }
    }

    private void drawDining(HashMap<FactionColor, Integer> dining) {

        for (int i = 0; i < studentsDiningView.size(); i++)
            wallpaperPane.getChildren().remove(studentsDiningView.get(i));

        studentsDiningView = new ArrayList<>();

        ImageView temp = null;

        for (FactionColor color : FactionColor.values()) {

            double additionalY = 0;

            for (int i = 0; i < dining.get(color); i++) {

                temp = new ImageView();
                temp.setImage(studentImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                temp.setX(0);
                temp.setY(0);
                temp.setFitWidth(40);
                temp.setFitHeight(40);
                temp.setTranslateX(studentDiningFirstCoordinatesFromColor.get(color).x);
                temp.setTranslateY(studentDiningFirstCoordinatesFromColor.get(color).y + additionalY);
                studentsDiningView.add(temp);
                additionalY = additionalY + ySpaceStudentsDining;
            }
        }
    }

    private void drawProfessors(boolean[] professors) {

        for (int i = 0; i < professorsView.size(); i++)
            wallpaperPane.getChildren().remove(professorsView.get(i));

        professorsView = new ArrayList<>();

        for (FactionColor color : FactionColor.values())
            if (professors[color.getIndex()]) {
                ImageView temp = new ImageView();
                temp.setImage(profImageFromColor.get(color));
                wallpaperPane.getChildren().add(temp);
                temp.setFitWidth(40);
                temp.setFitHeight(40);
                temp.setX(profCoordinateFromColor.get(color).x);
                temp.setY(profCoordinateFromColor.get(color).y);
                professorsView.add(temp);
            }
    }

    private void drawTowers(LimitedTowerContainer towers) {

        for (int i = 0; i < towersView.size(); i++)
            wallpaperPane.getChildren().remove(towersView.get(i));

        towersView = new ArrayList<>();

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;


        for (int i = 0; i < towers.getCurrentSize(); i++) {
            temp = new ImageView();
            temp.setImage(towerImageFromColor.get(towers.getCurrentTower()));
            wallpaperPane.getChildren().add(temp);
            temp.setFitWidth(28);
            temp.setFitHeight(50);
            temp.setX(firstTower.x + additionalX);
            temp.setY(firstTower.y + additionalY);
            towersView.add(temp);
            posInLine = posInLine + 1;
            additionalX = additionalX + xSpaceTowers;
            if (posInLine == firstLineEntranceAndTowers) {
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


}
