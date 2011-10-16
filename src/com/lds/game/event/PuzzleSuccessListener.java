package com.lds.game.event;

/**
 * A delegate to be called back when a puzzle is successfully completed.
 * @author Lightning Development Studios
 */
public interface PuzzleSuccessListener 
{
    /**
     * Called when a puzzle is successfully completed.
     */
	void onPuzzleSuccess();
}
