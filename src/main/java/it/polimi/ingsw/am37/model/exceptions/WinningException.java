package it.polimi.ingsw.am37.model.exceptions;

import it.polimi.ingsw.am37.model.Player;

/**
 * Exception used to notify who is the winner
 */
public class WinningException extends RuntimeException {

    final Player winner;

    public WinningException(Player winner) {
        this.winner = winner;
    }

    public WinningException(String message, Player winner) {
        super(message);
        this.winner = winner;
    }

    public WinningException(String message, Throwable cause, Player winner) {
        super(message, cause);
        this.winner = winner;
    }

    public WinningException(Throwable cause, Player winner) {
        super(cause);
        this.winner = winner;
    }

    public WinningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Player winner) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}
