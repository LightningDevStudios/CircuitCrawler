package com.lds.game.event;

/**
 * A delegate to be called back when a level has finished loading.
 * @author Lightning Development Studios
 */
public interface GameInitializedListener 
{
    /**
     * Called when a level has finished loading.
     */
	void onGameInitialized();
}
