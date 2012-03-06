package com.ltdev.cc.puzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ltdev.cc.event.PuzzleFailListener;
import com.ltdev.cc.event.PuzzleInitializedListener;
import com.ltdev.cc.event.PuzzleSuccessListener;

/**
 * An interface that all puzzles implement.
 * @author Lightning Development Studios
 */
public interface IPuzzle extends GLSurfaceView.Renderer
{
    /**
     * Receives touch input.
     * @param event The Android touch event data.
     */
	void onTouchEvent(MotionEvent event);
	
	/**
	 * Puzzles will receive the system Context some time between the initializaiton and onSurfaceCreated.
	 * @param context An Android Context.
	 */
	void setContext(Context context);
	
	/**
	 * Receives an object to synchronize the rendering thread with the main system thread.
	 * @param o The syncronized object.
	 */
	void setSyncObj(Object o);
	
	/**
	 * Puzzles will receive an initialized event listener. Not using the listener may result in unexpected behavior.
	 * @param listener The event callback for when the puzzle is done initializing.
	 */
	void setPuzzleInitializedEvent(PuzzleInitializedListener listener);
	
	/**
	 * Puzzles will receive an event listener to be called if the player successfully completes the puzzle. The listener is guaranteed to end the calling Activity.
	 * @param listener The event callback for when the player wins the puzzle.
	 */
	void setPuzzleSuccessEvent(PuzzleSuccessListener listener);
	
	/**
	 * Puzzles will receive an event listener to be called if the player fails to complete the puzzle. The listener is guaranteed to end the calling Activity.
	 * @param listener The event callback for when the player wins the puzzle.
	 */
	void setPuzzleFailEvent(PuzzleFailListener listener);
}
