package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.am37.client.gui.controller.GameSceneController.*;

public class BoardsController extends GenericController {
    private final static int leftShift = 590;
    private final static Coordinate shiftedBoardCoordinates = new Coordinate(345, 270);
    private final static Dimension boardDimension = new Dimension(880, 367);
    private final List<String> playersNickname = new ArrayList<>();
    private final List<ImageView> entranceStudentImageViews = new ArrayList<>();
    private final List<ImageView> diningStudentImageViews = new ArrayList<>();
    private final List<ImageView> professorImageViews = new ArrayList<>();
    private final List<ImageView> towerImageViews = new ArrayList<>();
    public Label nickname2;
    public Label nickname3;
    public Pane boardsPane;

    public void draw(List<Player> players) {
        if (playersNickname.isEmpty()) {
            players.forEach(player -> playersNickname.add(player.getPlayerId()));
            nickname2.setText(playersNickname.get(0));
            if (playersNickname.size() == 1) {
                nickname3.setVisible(false);
            } else {
                nickname3.setText(playersNickname.get(1));
                ImageView board = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/assets/images/Board.png"))));
                drawWithDimension(boardDimension, board);
                placeObjectNoAnimation(shiftedBoardCoordinates, board);
                boardsPane.getChildren().add(board);
            }
        }
        for (Player player : players) {
            drawBoard(player.getBoard(), playersNickname.indexOf(player.getPlayerId()));
        }
    }

    private void drawBoard(Board board, int shift) {
        drawEntrance(board.getEntrance(), shift);
        drawDining(board.getDiningRoom(), shift);
        drawProfessors(board.getProfTable(), shift);
        drawTowers(board.getTowers(), shift);
    }

    private void drawDining(LimitedStudentsContainer diningRoom, int shift) {
        ImageView imageView;
        diningStudentImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
        diningStudentImageViews.clear();
        for (FactionColor color : FactionColor.values()) {
            Coordinate firstCoordinate = studentDiningFirstCoordinatesFromColor.get(color);
            for (int i = 0; i < diningRoom.getByColor(color); i++) {
                imageView = new ImageView(studentImageFromColor.get(color));
                imageView.setMouseTransparent(true);
                drawWithDimension(studentAndProfDimension, imageView);
                placeObjectNoAnimation(new Coordinate(
                        firstCoordinate.x() + shift * leftShift,
                        firstCoordinate.y() + i * ySpaceStudentsDining), imageView);
                boardsPane.getChildren().add(imageView);
                diningStudentImageViews.add(imageView);
            }
        }
    }

    private void drawEntrance(LimitedStudentsContainer entrance, int shift) {
        ImageView imageView;
        entranceStudentImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
        entranceStudentImageViews.clear();
        int generalIndex = 0;
        for (FactionColor color : FactionColor.values()) {
            for (int i = 0; i < entrance.getByColor(color); i++) {
                imageView = new ImageView(studentImageFromColor.get(color));
                imageView.setMouseTransparent(true);
                drawWithDimension(studentAndProfDimension, imageView);
                placeObjectNoAnimation(new Coordinate(
                        firstEntranceCoordinate.x() + (generalIndex % firstLineEntranceAndTowersSize) * xSpaceStudents +
                                shift * leftShift,
                        firstEntranceCoordinate.y() +
                                (generalIndex / firstLineEntranceAndTowersSize) * ySpaceStudentsEntrance), imageView);
                boardsPane.getChildren().add(imageView);
                entranceStudentImageViews.add(imageView);
                generalIndex++;
            }
        }
    }

    private void drawProfessors(boolean[] profTable, int shift) {
        ImageView imageView;
        professorImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
        professorImageViews.clear();
        for (FactionColor color : FactionColor.values()) {
            if (profTable[color.getIndex()]) {
                imageView = new ImageView(studentImageFromColor.get(color));
                imageView.setMouseTransparent(true);
                drawWithDimension(studentAndProfDimension, imageView);
                placeObjectNoAnimation(new Coordinate(
                        profCoordinateFromColor.get(color).x() + shift * leftShift,
                        profCoordinateFromColor.get(color).y()), imageView);
                boardsPane.getChildren().add(imageView);
                professorImageViews.add(imageView);
            }
        }
    }

    private void drawTowers(LimitedTowerContainer towers, int shift) {
        ImageView imageView;
        TowerColor color = towers.getCurrentTower();
        towerImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
        towerImageViews.clear();
        for (int i = 0; i < towers.getCurrentSize(); i++) {
            imageView = new ImageView(towerImageFromColor.get(color));
            imageView.setMouseTransparent(true);
            drawWithDimension(towerDimension, imageView);
            placeObjectNoAnimation(new Coordinate(
                    firstTowerCoordinate.x() + (i % firstLineEntranceAndTowersSize) * xSpaceTowers + shift * leftShift,
                    firstTowerCoordinate.y() + (i < firstLineEntranceAndTowersSize ? 0 : 1) * ySpaceTowers), imageView);
            boardsPane.getChildren().add(imageView);
            towerImageViews.add(imageView);

        }
    }

}
