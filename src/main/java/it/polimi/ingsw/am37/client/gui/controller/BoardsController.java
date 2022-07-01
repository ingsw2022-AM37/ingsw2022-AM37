package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am37.client.gui.controller.GameSceneController.*;

/**
 * Controller used for the graphics of other's players boards
 */
public class BoardsController extends GenericController {

    /**
     * shift to right for 590 pixels, it's useful to draw elements on second (right) board
     */
    private final static int leftShift = 590;

    /**
     * ArrayList with all nicknames
     */
    private final List<String> playersNickname = new ArrayList<>();

    /**
     * ArrayList with ImageView of students on board's entrance, useful to save them for removing from screen when necessary
     */
    private final List<ImageView> entranceStudentImageViews = new ArrayList<>();

    /**
     * ArrayList with ImageView of students on board's dining, useful to save them for removing from screen when necessary
     */
    private final List<ImageView> diningStudentImageViews = new ArrayList<>();

    /**
     * ArrayList with ImageView of professors on board's table, useful to save them for removing from screen when necessary
     */
    private final List<ImageView> professorImageViews = new ArrayList<>();

    /**
     * ArrayList with ImageView of towers on board's, useful to save them for removing from screen when necessary
     */
    private final List<ImageView> towerImageViews = new ArrayList<>();

    /**
     * nickname of second player
     */
    public Label nickname2;

    /**
     * nickname of third player
     */
    public Label nickname3;

    /**
     * board of third player
     */
    public ImageView board_3;

    /**
     * Pane of the scene
     */
    public Pane boardsPane;

    /**
     * Method used to control the logic of drawing others' boards
     * @param players players in the game
     */
    public void draw(List<Player> players) {
        if (playersNickname.isEmpty()) {
            players.forEach(player -> playersNickname.add(player.getPlayerId()));
            nickname2.setText(playersNickname.get(0));
            if (playersNickname.size() == 1) {
                nickname3.setVisible(false);
                board_3.setVisible(false);
            } else {
                nickname3.setText(playersNickname.get(1));
            }
        }
        if (players.size() > 0) {
            diningStudentImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
            diningStudentImageViews.clear();
            entranceStudentImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
            entranceStudentImageViews.clear();
            professorImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
            professorImageViews.clear();
            towerImageViews.forEach(iV -> boardsPane.getChildren().remove(iV));
            towerImageViews.clear();
            for (Player player : players) {
                drawBoard(player.getBoard(), playersNickname.indexOf(player.getPlayerId()));
            }
        }
    }

    /**
     * Method used to draw boards of other players
     * @param board It's th board of another player
     * @param shift It's a number which tell if the player's board is second or third
     */
    private void drawBoard(Board board, int shift) {
        drawEntrance(board.getEntrance(), shift);
        drawDining(board.getDiningRoom(), shift);
        drawProfessors(board.getProfTable(), shift);
        drawTowers(board.getTowers(), shift);
    }

    /**
     * Method used for drawing students on dining
     * @param diningRoom Student container with students on dining
     * @param shift It's a number which tell if the player's board is second or third
     */
    private void drawDining(LimitedStudentsContainer diningRoom, int shift) {


        ImageView temp = null;

        for (FactionColor color : FactionColor.values()) {

            double additionalY = 0;

            for (int i = 0; i < diningRoom.getByColor(color); i++) {

                temp = new ImageView();
                temp.setMouseTransparent(true);
                temp.setImage(studentImageFromColor.get(color));
                boardsPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                placeObjectNoAnimation(new Coordinate(studentDiningFirstCoordinatesFromColor.get(color).x() + shift*leftShift,
                        studentDiningFirstCoordinatesFromColor.get(color).y() + additionalY), temp);
                diningStudentImageViews.add(temp);
                additionalY = additionalY + ySpaceStudentsDining;
            }
        }



    }

    /**
     * Method used for drawing students on entrance
     * @param entrance Student container with students on entrance
     * @param shift It's a number which tell if the player's board is second or third
     */
    private void drawEntrance(LimitedStudentsContainer entrance, int shift) {

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;
        boolean firstLine = true;

        for (FactionColor color : FactionColor.values()) {

            for (int i = 0; i < entrance.getByColor(color); i++) {

                temp = new ImageView();
                temp.setMouseTransparent(true);
                temp.setImage(studentImageFromColor.get(color));
                boardsPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                placeObjectNoAnimation(new Coordinate(
                        firstEntranceCoordinate.x() + additionalX + shift*leftShift, firstEntranceCoordinate.y() + additionalY), temp);
                entranceStudentImageViews.add(temp);
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
     * Method used for drawing students on prof table
     * @param profTable boolean which shows controlled professors
     * @param shift It's a number which tell if the player's board is second or third
     */
    private void drawProfessors(boolean[] profTable, int shift) {

        for (FactionColor color : FactionColor.values())
            if (profTable[color.getIndex()]) {
                ImageView temp = new ImageView();
                temp.setImage(profImageFromColor.get(color));
                boardsPane.getChildren().add(temp);
                drawWithDimension(studentAndProfDimension, temp);
                placeObjectNoAnimation(new Coordinate(profCoordinateFromColor.get(color).x()+shift*leftShift,
                        profCoordinateFromColor.get(color).y()), temp);
                professorImageViews.add(temp);
            }


    }

    /**
     *
     * @param towers containers of towers, useful to know their color and number
     * @param shift It's a number which tell if the player's board is second or third
     */
    private void drawTowers(LimitedTowerContainer towers, int shift) {

        int posInLine = 0;
        double additionalX = 0;
        double additionalY = 0;
        ImageView temp = null;


        for (int i = 0; i < towers.getCurrentSize(); i++) {
            temp = new ImageView();
            temp.setImage(towerImageFromColor.get(towers.getCurrentTower()));
            boardsPane.getChildren().add(temp);
            drawWithDimension(towerDimension, temp);
            placeObjectNoAnimation(new Coordinate(
                    firstTowerCoordinate.x() + additionalX + shift*leftShift, firstTowerCoordinate.y() + additionalY), temp);
            towerImageViews.add(temp);
            posInLine = posInLine + 1;
            additionalX = additionalX + xSpaceTowers;
            if (posInLine == firstLineEntranceAndTowersSize) {
                additionalX = 0;
                posInLine = 0;
                additionalY = ySpaceTowers;
            }
        }




    }

}
