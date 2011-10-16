package com.lds.game.event;

/**
 * A delegate to be called back when a level has finished.
 * @author Lightning Development Studios
 */
public interface GameOverListener
{
    /**
     * Called when the game is over.
     * @param winning A value indicating whether the game ended in victory or not.
     */
	void onGameOver(boolean winning);
}
