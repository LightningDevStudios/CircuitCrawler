package com.lds.game.event;

/**
 * A delegate to be called back when a puzzle ends without being successfully completed.
 * @author Lightning Development Studios
 */
public interface PuzzleFailListener 
{
    /**
     * Called when a puzzle ends without being successfully completed.
     */
	void onPuzzleFail();
}
