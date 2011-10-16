package com.lds.game.event;

/**
 * A delegate to be called back when the puzzle has finished loading.
 * @author Robert Rouhani
 *
 */
public interface PuzzleInitializedListener 
{
    /**
     * Called back when the puzzle has finished loading.
     */
	void onInitialized();
}
