package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.Assistant;
import it.polimi.ingsw.am37.model.Cloud;
import it.polimi.ingsw.am37.model.Island;
import it.polimi.ingsw.am37.model.Player;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class GameSceneController extends GenericController {


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
}
